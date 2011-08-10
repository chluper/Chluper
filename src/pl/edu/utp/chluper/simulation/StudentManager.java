/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.utp.chluper.simulation;

import java.util.LinkedList;
import pl.edu.utp.chluper.algorithm.student.StudentConcreteAlgorithm;
import pl.edu.utp.chluper.algorithm.student.StudentRouteAlgorithm;
import pl.edu.utp.chluper.environment.element.Student;
import pl.edu.utp.chluper.simulation.logging.LoggerGroup;
import pl.edu.utp.chluper.simulation.logging.LoggingAgent;
import pl.edu.utp.chluper.simulation.logging.LoggingHandler;
import pl.edu.utp.chluper.simulation.scenario.Scenario;

/**
 * Klasa odpowiedzialna za dostarczanie studentow i ich odbieranie
 * @author damian
 */
public class StudentManager {

	// ilosc biurek
	private final int totalDeskNumber;
	// scenariusz pojawiania sie studentow
	private final Scenario scenario;
	// kolejka studentow oczekujacych na wejscie
	private final LinkedList<Student> studentQueue = new LinkedList<Student>();
	// licznik tykniec
	private int tickCounter = 0;
	// lista wolnych biurek
	private final LinkedList<Integer> availableDesks = new LinkedList<Integer>();
	// statystyki
	private final Statistics statistics = new Statistics();
	// obiekt logowania
	private final LoggingAgent loggingAgent;
	// obiekt logowania dla symulatora
	private final LoggingHandler logger;

	/**
	 * Tworzy zarzadce
	 * @param deskNumber
	 * @param scenario
	 */
	public StudentManager(int deskNumber, Scenario scenario, LoggingAgent loggingAgent) {
		this.totalDeskNumber = deskNumber;
		this.scenario = scenario;
		this.loggingAgent = loggingAgent;
		this.logger = loggingAgent.createLoggingHandler(LoggerGroup.STUDENT_MANAGER, "Student-Manager");
		// wypelnianie wolnych biurek
		for (int i = 0; i < deskNumber; i++) {
			availableDesks.addLast(i);
		}
	}

	/**
	 * Metoda wywolywana co takt symulacji
	 */
	public void tick() {
		// licznik
		tickCounter++;
		// wyluskiwanie studentow
		Student student = scenario.next();
		// jesli pojawil sie nowy student
		if (student != null) {
			// nadawanie czasu powstania
			student.setCreationTick(tickCounter);
			// dodawanie studenta do kolejki
			logger.level2("Dodawanie studenta do kolejki oczekujacych: " + student);
			// pakowanie do kolejki
			studentQueue.addLast(student);
		}
	}

	/**
	 * Zwraca statystyki
	 * @return
	 */
	public Statistics getStatistics() {
		return statistics;
	}

	/**
	 * Metoda zwraca studenta jesli jest dostepny
	 * aby wszedl do biblioteki
	 * @return student lub null jesli nie ma
	 */
	public Student takeAvailablStudent() {
		// jesli jest dostepny student oraz jest dostepne biurko
		if (!studentQueue.isEmpty() && !availableDesks.isEmpty()) {
			// wyluskiwanie z kolejki
			Student student = studentQueue.removeFirst();
			// nadawanie numeru biurka
			student.setTargetDeskNumber(availableDesks.removeFirst());
			// nadawanie czasu wejscia do biblioteki
			student.setEntryTick(tickCounter);
			// nadawanie algorytmu
			student.setAlgorithm(new StudentRouteAlgorithm(new StudentConcreteAlgorithm()));

			// obiekty logowania
			String id = "Student-" + student.getNumber();
			String algorithmId = "Student-Algorithm-" + student.getNumber();
			student.setLogger(loggingAgent.createLoggingHandler(LoggerGroup.STUDENT, id));
			student.setAlgorithmLogger(loggingAgent.createLoggingHandler(LoggerGroup.STUDENT_ALGORITHM, algorithmId));

			// dodawanie studenta do kolejki
			logger.level2("Wpuszczanie studenta do biblioteki: " + student);
			// zwracanie
			return student;
		}
		return null;
	}

	/**
	 * Metoda wywolywana kiedy student wyjdzie z biblioteki
	 * @param student
	 */
	public void studentLeft(Student student) {
		// sprawdzanie czy student jest usatysfakcjonowany
		if (!student.isSatisfied()) {
			throw new SimulationException("Student " + student + " probuje wyjsc nie zalatwiajac wszystkiego");
		}

		// zapis czasu
		student.setExitTick(tickCounter);
		// zwalnianie biurka
		availableDesks.addLast(student.getTargetDeskNumber());

		// dodawanie studenta do kolejki
		logger.level2("Opuszczanie biblioteki przez studenta: " + student);

		// dodawanie do statystyk
		statistics.add(student);
	}
}
