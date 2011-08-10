/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.utp.chluper.algorithm.concret;

import java.util.LinkedList;
import pl.edu.utp.chluper.algorithm.Decision;
import pl.edu.utp.chluper.algorithm.DecisionType;
import pl.edu.utp.chluper.algorithm.util.AbstractAlgorithm;
import pl.edu.utp.chluper.environment.view.RobotEnvironmentView;
import pl.edu.utp.chluper.environment.view.DeskView;
import pl.edu.utp.chluper.environment.view.RobotView;

/**
 *
 * @author damian
 */
public class TakeFromDeskAlgorithm extends AbstractAlgorithm {

	private LinkedList<Integer> desks = new LinkedList<Integer>();

	public TakeFromDeskAlgorithm(RobotEnvironmentView environmentView) {

		for (DeskView desk : environmentView.getDeskViews()) {
			desks.addFirst(desk.getNumber());
		}
	}

	public Decision decide(RobotView controlledRobot, RobotEnvironmentView environmentView) {
		if (controlledRobot.getCache().isEmpty()) {

			DeskView deskWithBooks = null;
			for (int i = 0; i < desks.size(); i++) {
				int number = nextDeskNumber();
				if (!environmentView.getDeskViewByNumber(number).getBooksToReturn().isEmpty()) {
					deskWithBooks = environmentView.getDeskViewByNumber(number);
					break;
				}
			}
			if (deskWithBooks == null) {
				logger.level2("Oczekiwanie na ksiazki na biurku");
				return new Decision(DecisionType.WAIT);
			} else {
				logger.level2("Pobieranie ksiazki " + deskWithBooks.getBooksToReturn().get(0) + " z biurka:" + deskWithBooks );
				return new Decision(DecisionType.TAKE_FROM_DESK, deskWithBooks.getNumber(), deskWithBooks.getBooksToReturn().get(0).getIsbn());
			}
		} else {
			logger.level2("Dostarczanie ksiazki na pulke:" + controlledRobot.getCache().get(0));
			return new Decision(DecisionType.DELIVER_TO_BOOKSHELF, controlledRobot.getCache().get(0).getIsbn());
		}
	}

	private int nextDeskNumber() {
		int number = desks.removeLast();
		desks.addFirst(number);
		return number;
	}

}
