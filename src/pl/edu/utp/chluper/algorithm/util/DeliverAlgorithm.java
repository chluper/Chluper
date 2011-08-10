/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.utp.chluper.algorithm.util;

import pl.edu.utp.chluper.algorithm.Algorithm;
import pl.edu.utp.chluper.algorithm.AlgorithmException;
import pl.edu.utp.chluper.algorithm.Decision;
import pl.edu.utp.chluper.algorithm.DecisionType;
import pl.edu.utp.chluper.environment.element.Book;
import pl.edu.utp.chluper.environment.view.RobotEnvironmentView;
import pl.edu.utp.chluper.environment.element.Bookshelf;
import pl.edu.utp.chluper.environment.element.Desk;
import pl.edu.utp.chluper.environment.element.Robot;
import pl.edu.utp.chluper.environment.element.Turn;
import pl.edu.utp.chluper.environment.view.BookshelfView;
import pl.edu.utp.chluper.environment.view.DeskView;
import pl.edu.utp.chluper.environment.view.RobotView;

/**
 * Algorytm realizujacy dostarczanie ksiazek
 * Wykonuje rozkazy DELIVER_TO_DESK, DELIVER_TO_BOOKSHELF, TAKE_FROM_DESK, TAKE_FROM_BOOKSHELF
 * Wymaga obudowania przez RouteAlgorithm
 * @author damian
 */
public class DeliverAlgorithm extends WrappingAlgorithm {

	// aktualnie wykonywana operacja
	private Decision currentOperation = null;

	public DeliverAlgorithm(Algorithm internalAlgorithm) {
		super(internalAlgorithm);
	}

	public Decision decide(RobotView controlledRobot, RobotEnvironmentView environmentView) {

		// jesli aktualnie nic nie robimy
		if (currentOperation == null) {
			// pytamy algorytm wewnetrzny o decyzje
			Decision internalAlgorithmDecision = internalAlgorithm.decide(controlledRobot, environmentView);
			logger.level1("[DeliverAlgorithm] Wykonywanie rozkazu: " + internalAlgorithmDecision);
			switch (internalAlgorithmDecision.getDecisionType()) {
				// wyluskiwanie decyzji
				case DELIVER_TO_DESK:
				case DELIVER_TO_BOOKSHELF:
				case TAKE_FROM_DESK:
				case TAKE_FROM_BOOKSHELF:
					currentOperation = internalAlgorithmDecision;
					logger.level1("[DeliverAlgorithm] Wyznaczono nowa operacje: " + currentOperation);
					break;

				// jesli to zaden z obsluiwanych rozkazow, to przekazuje dalej
				default:
					return internalAlgorithmDecision;
			}
		}

		// wykonywanie operacji
		switch (currentOperation.getDecisionType()) {
			// przywozenie ksiazki
			case DELIVER_TO_DESK: {
				// wyluskiwanie argumentow
				int deskNumber = -1;
				if (currentOperation.getArg0() instanceof DeskView) {
					deskNumber = ((DeskView) currentOperation.getArg0()).getNumber();
				} else {
					deskNumber = (Integer) currentOperation.getArg0();
				}
				DeskView desk = environmentView.getDeskViewByNumber(deskNumber);
				int isbn = -1;
				if (currentOperation.getArg0() instanceof Book) {
					isbn = ((Book) currentOperation.getArg1()).getIsbn();
				} else {
					isbn = (Integer) currentOperation.getArg1();
				}

				// jesli jestesmy na wlasciwym miejscu
				if (desk.getRobotPadLocation().equals(controlledRobot.getLocation())) {
					logger.level1("[DeliverAlgorithm] Konczenie wykonywania operacji: " + currentOperation);
					currentOperation = null;
					// oddawanie ksiazki
					return new Decision(DecisionType.PUT, Turn.getTurn(controlledRobot.getLocation(), desk.getLocation(), 1), isbn);
				} else {
					return new Decision(DecisionType.GO_TO_DESK, desk);
				}
			}
			// odkladanie ksiazki
			case DELIVER_TO_BOOKSHELF: {
				// wyluskiwanie argumentow
				int isbn = -1;
				if (currentOperation.getArg0() instanceof Book) {
					isbn = ((Book) currentOperation.getArg0()).getIsbn();
				} else {
					isbn = (Integer) currentOperation.getArg0();
				}

				BookshelfView bookshelf = environmentView.getBookshelfViewByIsbn(isbn);
				// jesli jestesmy na wlasciwym miejscu
				if (bookshelf.getRobotPadLocation().equals(controlledRobot.getLocation())) {
					logger.level1("[DeliverAlgorithm] Konczenie wykonywania operacji: " + currentOperation);
					currentOperation = null;
					// oddawanie ksiazki
					return new Decision(DecisionType.PUT, Turn.getTurn(controlledRobot.getLocation(), bookshelf.getLocation(), 1), isbn);
				} else {
					return new Decision(DecisionType.GO_TO_BOOKSHELF, bookshelf);
				}
			}
			// pobieranie ksiazki z polki
			case TAKE_FROM_DESK: {
				// wyluskiwanie argumentow
				int deskNumber = -1;
				if (currentOperation.getArg0() instanceof DeskView) {
					deskNumber = ((DeskView) currentOperation.getArg0()).getNumber();
				} else {
					deskNumber = (Integer) currentOperation.getArg0();
				}
				DeskView desk = environmentView.getDeskViewByNumber(deskNumber);
				int isbn = -1;
				if (currentOperation.getArg0() instanceof Book) {
					isbn = ((Book) currentOperation.getArg1()).getIsbn();
				} else {
					isbn = (Integer) currentOperation.getArg1();
				}

				// jesli jestesmy na wlasciwym miejscu
				if (desk.getRobotPadLocation().equals(controlledRobot.getLocation())) {
					logger.level1("[DeliverAlgorithm] Konczenie wykonywania operacji: " + currentOperation);
					currentOperation = null;
					// oddawanie ksiazki
					return new Decision(DecisionType.TAKE, Turn.getTurn(controlledRobot.getLocation(), desk.getLocation(), 1), isbn);
				} else {
					return new Decision(DecisionType.GO_TO_DESK, desk);
				}
			}
			// pobieranie ksiazki z polki
			case TAKE_FROM_BOOKSHELF: {
				// wyluskiwanie argumentow
				int isbn = (Integer) currentOperation.getArg0();

				BookshelfView bookshelf = environmentView.getBookshelfViewByIsbn(isbn);
				// jesli jestesmy na wlasciwym miejscu
				if (bookshelf.getRobotPadLocation().equals(controlledRobot.getLocation())) {
					logger.level1("[DeliverAlgorithm] Konczenie wykonywania operacji: " + currentOperation);
					currentOperation = null;
					// oddawanie ksiazki
					return new Decision(DecisionType.TAKE, Turn.getTurn(controlledRobot.getLocation(), bookshelf.getLocation(), 1), isbn);
				} else {
					return new Decision(DecisionType.GO_TO_BOOKSHELF, bookshelf);
				}
			}
			default:
				throw new AlgorithmException("Ten blad nie powinien wystapic");
		}

	}
}
