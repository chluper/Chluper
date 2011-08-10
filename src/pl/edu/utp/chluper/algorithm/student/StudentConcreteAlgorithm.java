/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.utp.chluper.algorithm.student;

import pl.edu.utp.chluper.algorithm.AlgorithmException;
import pl.edu.utp.chluper.environment.view.StudentEnvironmentView;
import pl.edu.utp.chluper.environment.element.Desk;
import pl.edu.utp.chluper.environment.element.Marker;
import pl.edu.utp.chluper.environment.element.Student;
import pl.edu.utp.chluper.environment.element.Turn;
import pl.edu.utp.chluper.environment.view.DeskView;
import pl.edu.utp.chluper.environment.view.MarkerView;
import pl.edu.utp.chluper.environment.view.StudentView;
import pl.edu.utp.chluper.simulation.logging.LoggingHandler;

/**
 * algorytm dzialania studenta
 * wymaga opakowania przez StudentRouteAlgorithm
 * @author damian
 */
public class StudentConcreteAlgorithm implements StudentAlgorithm {

	// stany studenta
	private static enum StudentState {
		// idz do biurka

		GO_TO_DESK,
		// zamow
		RETURN_BOOKS,
		// zwroc
		RESERVE_BOOKS,
		// czekaj na dostawe
		WAIT_FOR_DELIVER,
		// idz do wyjscia
		GO_TO_EXIT,;
	}
	// stan studenta
	private StudentState state = StudentState.GO_TO_DESK;
	// logger
	private LoggingHandler logger;

	/**
	 * Konstruktor
	 */
	public StudentConcreteAlgorithm() {
	}

	public StudentDecision decide(StudentView controlledStudent, StudentEnvironmentView environmentView) {
		// docelowe bourko
		DeskView desk = environmentView.getDeskViewByNumber(controlledStudent.getTargetDeskNumber());
		// w zaleznosci od stanu
		switch (state) {
			case GO_TO_DESK: {
				// jesli dotarlismy
				if (desk.getStudentPadLocation().equals(controlledStudent.getLocation())) {
					logger.level2("Osiagniete bourko: " + desk);
					state = StudentState.RETURN_BOOKS;
					logger.level2("Zmiana stanu na: " + StudentState.RETURN_BOOKS);
				} else {
					logger.level2("Przemieszczanie sie do biurka: " + desk);
					return new StudentDecision(StudentDecisionType.GO_TO, desk.getStudentPadLocation(), null);
				}
			}
			case RETURN_BOOKS: {
				// jesli jest cos do oddania
				if (!controlledStudent.getBooksToReturn().isEmpty()) {
					// jesli jest zamkniete
					if (!desk.isOpened()) {
						logger.level1("Oczekiwanie na otwarcie bourka: " + desk);
						return new StudentDecision(StudentDecisionType.WAIT, null, null);
					} else {
						logger.level2("Oddawanie ksiazki na bourko: " + desk);
						return new StudentDecision(StudentDecisionType.PUT, Turn.getTurn(controlledStudent.getLocation(), desk.getLocation(), 1), controlledStudent.getBooksToReturn().get(0).getIsbn());
					}
				} else {
					state = StudentState.RESERVE_BOOKS;
					logger.level2("Zmiana stanu na: " + StudentState.RESERVE_BOOKS);
				}
			}
			case RESERVE_BOOKS: {
				// jesli jest cos do zamowienia
				if (!controlledStudent.getWishList().isEmpty()) {
					// jesli jest zamkniete
					if (!desk.isOpened()) {
						logger.level1("Oczekiwanie na otwarcie bourka: " + desk);
						return new StudentDecision(StudentDecisionType.WAIT, null, null);
					} else {
						state = StudentState.WAIT_FOR_DELIVER;
						logger.level2("Zamawianie ksiazek na bourku: " + desk);
						logger.level2("Zmiana stanu na: " + StudentState.WAIT_FOR_DELIVER);
						return new StudentDecision(StudentDecisionType.REQUEST, Turn.getTurn(controlledStudent.getLocation(), desk.getLocation(), 1),null);
					}
				} else {
					state = StudentState.WAIT_FOR_DELIVER;
					logger.level2("Zmiana stanu na: " + StudentState.WAIT_FOR_DELIVER);
				}
			}
			case WAIT_FOR_DELIVER: {
				// jesli nie jestesmy szczesliwi
				if (!controlledStudent.isSatisfied()) {
					// jesli cos lezy na biurku
					if (!desk.getBooksToLend().isEmpty()) {
						logger.level2("Podnoszenie ksiazki z biurka: " + desk);
						return new StudentDecision(StudentDecisionType.TAKE, Turn.getTurn(controlledStudent.getLocation(), desk.getLocation(), 1), desk.getBooksToLend().get(0).getIsbn());
					} else {
						logger.level2("Oczekiwanie na dostarczenie ksiazek na biurko: " + desk);
						return new StudentDecision(StudentDecisionType.WAIT, null, null);
					}
				}
				state = StudentState.GO_TO_EXIT;
				logger.level2("Zmiana stanu na: " + StudentState.GO_TO_EXIT);
			}
			case GO_TO_EXIT: {
				// jesli dotarlismy to wychodzimy
				for (MarkerView exit : environmentView.getStudentExitMarkerViews()) {
					if (exit.getLocation().equals(controlledStudent.getLocation())) {
						logger.level2("Wychodzenie");
						return new StudentDecision(StudentDecisionType.LEAVE, null, null);
					}
				}
				for (MarkerView exit : environmentView.getStudentExitMarkerViews()) {
					logger.level2("Przemieszczanie sie do wyjscia");
					return new StudentDecision(StudentDecisionType.GO_TO, exit.getLocation(), null);
				}
			}
			default:
				throw new AlgorithmException("To nie powinno sie zdarzyc");
		}
	}

	/**
     * Ustawianie logera algorytmowi
     * @param logger
     */
    public void setLogger(LoggingHandler logger) {
		this.logger = logger;
	}
}
