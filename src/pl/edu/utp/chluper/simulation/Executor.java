/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.utp.chluper.simulation;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import pl.edu.utp.chluper.algorithm.Coordinator;
import pl.edu.utp.chluper.algorithm.Decision;
import pl.edu.utp.chluper.algorithm.student.StudentDecision;
import pl.edu.utp.chluper.environment.element.Book;
import pl.edu.utp.chluper.nvironment.Environment;
import pl.edu.utp.chluper.environment.view.RobotEnvironmentView;
import pl.edu.utp.chluper.environment.view.StudentEnvironmentView;
import pl.edu.utp.chluper.environment.element.Bookshelf;
import pl.edu.utp.chluper.environment.element.Desk;
import pl.edu.utp.chluper.environment.element.Element;
import pl.edu.utp.chluper.environment.element.ElementType;
import pl.edu.utp.chluper.environment.element.Location;
import pl.edu.utp.chluper.environment.element.Robot;
import pl.edu.utp.chluper.environment.element.Student;
import pl.edu.utp.chluper.environment.element.Turn;
import pl.edu.utp.chluper.simulation.logging.LoggerGroup;
import pl.edu.utp.chluper.simulation.logging.LoggingAgent;
import pl.edu.utp.chluper.simulation.logging.LoggingHandler;
import pl.edu.utp.chluper.simulation.scenario.Scenario;

/**
 * Klasa odpowiedzialna za realizowanie algorytmow
 * @author damian
 */
public class Executor {

	// srodowisko
	private final Environment environment;
	// arbiter
	private final StudentManager manager;
	// koordynatorzy
	private final Collection<Coordinator> coordinators;
	// logger
	private final LoggingHandler logger;

	public Executor(Environment environment, Scenario scenario, LoggingAgent loggingAgent, Collection<Coordinator> coordinators) {
		this.environment = environment;
		this.coordinators = coordinators;
		this.manager = new StudentManager(environment.getDesks().size(), scenario, loggingAgent);
		this.logger = loggingAgent.createLoggingHandler(LoggerGroup.EXECUTOR, "Executor");
	}

	/**
	 * Metoda wywolywana co takt symulacji
	 */
	public void tick() {
		// tykniecie arbitera
		manager.tick();
		// wyluskiwanie widokow srodowiska
		RobotEnvironmentView robotEnvironmentView = environment.getRobotEnvironmentView();
		StudentEnvironmentView studentEnvironmentView = environment.getStudentEnvironmentView();

		// dzialanie na koordynatorach
		for (Coordinator coordinator : coordinators) {
			coordinator.coordinate(robotEnvironmentView);
		}

		// tworzenie map decyzji
		Map<Robot, Decision> robotDecisions = new HashMap<Robot, Decision>();
		Map<Student, StudentDecision> studentDecisions = new HashMap<Student, StudentDecision>();

		// wyluskiwanie decyzji
		for (Robot robot : environment.getRobots()) {
			robotDecisions.put(robot, robot.getAlgorithm().decide(robot.getElementView(), robotEnvironmentView));
		}
		for (Student student : environment.getStudents()) {
			studentDecisions.put(student, student.getAlgorithm().decide(student.getElementView(), studentEnvironmentView));
		}

		// tworzenie wykrywacza kolizji
		Set<Element> constantElements = new HashSet<Element>();
		constantElements.addAll(environment.getBookshelfs());
		constantElements.addAll(environment.getDesks());
		constantElements.addAll(environment.getObstructions());
		CollisionDetector collisionDetector = new CollisionDetector(environment.getWidth(), environment.getHeight(), constantElements);

		// wykonywanie decyzji
		for (Student student : studentDecisions.keySet()) {
			logger.level2("wykonywanie: " + studentDecisions.get(student) + " przez studenta: " + student);
			execute(student, studentDecisions.get(student), collisionDetector);
		}
		for (Robot robot : robotDecisions.keySet()) {
			logger.level2("wykonywanie: " + robotDecisions.get(robot) + " przez robota: " + robot);
			execute(robot, robotDecisions.get(robot), collisionDetector);
		}
		// dodawanie studentow
		for (Element entry : environment.getMarkers()) {
			if (entry.getElementType() == ElementType.STUDENT_ENTRY) {
				Student newStudent = manager.takeAvailablStudent();
				if (newStudent != null) {
					newStudent.setLocation(entry.getLocation());
					logger.level2("Dodawanie studenta do srodowiska: " + newStudent);
					environment.addStudent(newStudent);
				}
			}
		}
	}

	/**
	 * Wykonywanie decyzji robota
	 * @param robot
	 * @param decision
	 * @param detector
	 */
	private void execute(Robot robot, Decision decision, CollisionDetector detector) {
		switch (decision.getDecisionType()) {
			case MOVE: {
				Turn turn = (Turn) decision.getArg0();
				Location newLocation = robot.getLocation().move(turn, 1);
				// sprawdzanie kolizji
				if (detector.check(newLocation)) {
					logger.level3("Nastapila kolizja robota: " + robot);
					throw new SimulationException("Nastapila kolizja rbota: " + robot);
				}
				// przemieszczanie
				logger.level1("Przemieszczenie robota: " + robot);
				robot.setLocation(newLocation);
				break;
			}
			case TAKE: {
				Turn turn = (Turn) decision.getArg0();
				int isbn = (Integer) decision.getArg1();
				Location operatedLocation = robot.getLocation().move(turn, 1);
				// sprawdzamy czy tam jest biurko
				Desk desk = findDesk(operatedLocation);
				if (desk != null) {
					// sprawdzanie czy robot dobrze stoi
					if (!desk.getRobotPadLocation().equals(robot.getLocation())) {
						logger.level3("Proba podniesienia ksiazki z powietrza przez robota: " + robot);
						throw new SimulationException("Robot " + robot + " stoi na niewlasciwym miejscu aby skorzystac z biurka: " + desk);
					}
					Book book = desk.takeBookToReturn(isbn);
					logger.level2("Umieszczanie ksiazki: " + book + " z biurka: " + desk + " w kieszeni robota: " + robot);
					if (book != null) {
						robot.putToCache(book);
					}
					break;
				}
				// sprawdzanie czy to polka
				Bookshelf bookshelf = findBookshelf(operatedLocation);
				if (bookshelf != null) {
					// sprawdzanie czy robot dobrze stoi
					if (!bookshelf.getRobotPadLocation().equals(robot.getLocation())) {
						logger.level3("Proba podniesienia ksiazki z powietrza przez robota: " + robot);
						throw new SimulationException("Robot " + robot + " stoi na niewlasciwym miejscu aby skorzystac z polki: " + bookshelf);
					}
					Book book = bookshelf.take(isbn);
					logger.level2("Umieszczanie ksiazki: " + book + " z polki: " + bookshelf + " w kieszeni robota: " + robot);
					robot.putToCache(book);
					break;
				}
				logger.level3("Proba podniesienia ksiazki z powietrza przez robota: " + robot);
				throw new SimulationException("Robot " + robot + " probuje cos podniesc nie stojac przy biurku ani polce");
			}
			case PUT: {
				Turn turn = (Turn) decision.getArg0();
				int isbn = (Integer) decision.getArg1();
				Location operatedLocation = robot.getLocation().move(turn, 1);
				// sprawdzamy czy tam jest biurko
				Desk desk = findDesk(operatedLocation);
				if (desk != null) {
					// sprawdzanie czy robot dobrze stoi
					if (!desk.getRobotPadLocation().equals(robot.getLocation())) {
						logger.level3("Proba pokozenia ksiazki w powietrze przez robota: " + robot);
						throw new SimulationException("Robot " + robot + " stoi na niewlasciwym miejscu aby skorzystac z biurka: " + desk);
					}
					Book book = robot.takeFromCache(isbn);
					if (book == null) {
						logger.level3("Proba pobrania nieistniejacej ksiazki z kieszeni robota: " + robot);
						throw new SimulationException("Robot " + robot + " nie posiada ksiazki o isbn: " + isbn);
					}
					logger.level2("Odkladanie ksiazki: " + book + " z kieszeni robota: " + robot + " na biurko: " + desk);
					desk.putBookToLend(book);
					break;
				}
				// sprawdzanie czy to polka
				Bookshelf bookshelf = findBookshelf(operatedLocation);
				if (bookshelf != null) {
					// sprawdzanie czy robot dobrze stoi
					if (!bookshelf.getRobotPadLocation().equals(robot.getLocation())) {
						logger.level3("Proba pokozenia ksiazki w powietrze przez robota: " + robot);
						throw new SimulationException("Robot " + robot + " stoi na niewlasciwym miejscu aby skorzystac z biurka: " + desk);
					}
					Book book = robot.takeFromCache(isbn);
					if (book == null) {
						logger.level3("Proba pobrania nieistniejacej ksiazki z kieszeni robota: " + robot);
						throw new SimulationException("Robot " + robot + " nie posiada ksiazki o isbn: " + isbn);
					}
					logger.level2("Odkladanie ksiazki: " + book + " z kieszeni robota: " + robot + " na polke: " + bookshelf);
					bookshelf.put(book);
					break;
				}
				logger.level3("Proba pokozenia ksiazki w powietrze przez robota: " + robot);
				throw new SimulationException("Robot " + robot + " probuje cos polozyc nie stojac przy biurku ani polce");
			}
			case WAIT:
				logger.level1("Pass robota: " + robot);
				// nie robimy nic
				break;
			default:
				throw new SimulationException("Nieobslugiwany typ rozkazu: " + decision);
		}
	}

	/**
	 * Wykonywanie decyzji studenta
	 * @param robot
	 * @param decision
	 * @param detector
	 */
	private void execute(Student student, StudentDecision decision, CollisionDetector detector) {
		switch (decision.getDecisionType()) {
			case MOVE: {
				Turn turn = (Turn) decision.getArg0();
				Location newLocation = student.getLocation().move(turn, 1);
				// sprawdzanie kolizji
				if (detector.check(newLocation)) {
					logger.level3("Nastapila kolizja studenta: " + student);
					throw new SimulationException("Nastapila kolizja studenta: " + student);
				}
				// przemieszczanie
				logger.level1("Przemieszczenie studenta: " + student);
				student.setLocation(newLocation);
				break;
			}
			case TAKE: {
				Turn turn = (Turn) decision.getArg0();
				int isbn = (Integer) decision.getArg1();
				Location operatedLocation = student.getLocation().move(turn, 1);
				// sprawdzamy czy tam jest biurko
				Desk desk = findDesk(operatedLocation);
				if (desk != null) {
					// sprawdzanie czy robot dobrze stoi
					if (!desk.getStudentPadLocation().equals(student.getLocation())) {
						logger.level3("Proba podniesienia ksiazki z powietrza przez studenta: " + student);
						throw new SimulationException("Student " + student + " stoi na niewlasciwym miejscu aby skorzystac z biurka: " + desk);
					}
					Book book = desk.takeBookToLend(isbn);
					logger.level2("Umieszczanie ksiazki: " + book + " z biurka: " + desk + " w kieszeni studenta: " + student);
					if (book != null) {
						student.put(book);
					}
					break;
				}
				logger.level3("Proba podniesienia ksiazki z powietrza przez studenta: " + student);
				throw new SimulationException("Student " + student + " probuje cos podniesc nie stojac przy biurku");
			}
			case PUT: {
				Turn turn = (Turn) decision.getArg0();
				int isbn = (Integer) decision.getArg1();
				Location operatedLocation = student.getLocation().move(turn, 1);
				// sprawdzamy czy tam jest biurko
				Desk desk = findDesk(operatedLocation);
				if (desk != null) {
					// sprawdzanie czy robot dobrze stoi
					if (!desk.getStudentPadLocation().equals(student.getLocation())) {
						logger.level3("Proba pokozenia ksiazki w powietrze przez studenta: " + student);
						throw new SimulationException("Student " + student + " stoi na niewlasciwym miejscu aby skorzystac z biurka: " + desk);
					}
					Book book = student.take(isbn);
					if (book == null) {
						logger.level3("Proba pobrania nieistniejacej ksiazki z kieszeni studenta: " + student);
						throw new SimulationException("Student " + student + " nie posiada ksiazki o isbn: " + isbn);
					}
					logger.level2("Odkladanie ksiazki: " + book + " z kieszeni studenta: " + student + " na biurko: " + desk);
					desk.putBookToReturn(book);
					break;
				}
				logger.level3("Proba pokozenia ksiazki w powietrze przez studenta: " + student);
				throw new SimulationException("Student " + student + " probuje cos polozyc nie stojac przy biurku");
			}
			case REQUEST: {
				Turn turn = (Turn) decision.getArg0();
				List<Integer> wishList = student.getWishList();
				Location operatedLocation = student.getLocation().move(turn, 1);
				// sprawdzamy czy tam jest biurko
				Desk desk = findDesk(operatedLocation);
				if (desk != null) {
					// sprawdzanie czy student dobrze stoi
					if (!desk.getStudentPadLocation().equals(student.getLocation())) {
						logger.level3("Proba pokozenia listy zyczen w powietrze przez studenta: " + student);
						throw new SimulationException("Student " + student + " stoi na niewlasciwym miejscu aby skorzystac z biurka: " + desk);
					}
					logger.level2("Zamawianie ksiazek: " + wishList + " przez studenta: " + student + " na biurku: " + desk);
					desk.putLendList(wishList);
					break;
				}
				logger.level3("Proba pokozenia listy zyczen w powietrze przez studenta: " + student);
				throw new SimulationException("Student " + student + " probuje cos polozyc nie stojac przy biurku");
			}
			case WAIT:
				// nie robimy nic
				logger.level1("Pass studenta: " + student);
				break;
			case LEAVE: {
				boolean left = false;
				for (Element marker : environment.getMarkers()) {
					// poszukiwanie wyjsc
					if (marker.getElementType() == ElementType.STUDENT_EXIT) {
						// jesli student stoi na wyjsciu
						if (marker.getLocation().equals(student.getLocation())) {
							logger.level2("Usuwanie studenta ze srodowiska: " + student);
							// usuwanie ze srodowiska
							environment.removeStudent(student);
							// oddawanie managerowi
							manager.studentLeft(student);
							left = true;
							break;
						}
					}
				}
				if (!left) {
					throw new SimulationException("Student: " + student + " probuje wyjsc tam gdzie nie ma wyjscia");
				}
				break;
			}
			default:
				throw new SimulationException("Nieobslugiwany typ rozkazu: " + decision);
		}
	}

	/**
	 * Metoda probuje znalezc biurko w okreslonym polozeniu
	 * @param location
	 * @return biurko lub null jesli sie nie udalo
	 */
	private Desk findDesk(Location location) {
		for (Desk desk : environment.getDesks()) {
			if (desk.getLocation().equals(location)) {
				return desk;
			}
		}
		return null;
	}

	/**
	 * Metoda probuje znalezc polke w okreslonym polozeniu
	 * @param location
	 * @return polka lub null jesli sie nie udalo
	 */
	private Bookshelf findBookshelf(Location location) {
		for (Bookshelf bookshelf : environment.getBookshelfs()) {
			if (bookshelf.getLocation().equals(location)) {
				return bookshelf;
			}
		}
		return null;
	}

	/**
	 * Metoda zwraca statystyki
	 * @return
	 */
	public Statistics getStatistics() {
		return manager.getStatistics();
	}
}
