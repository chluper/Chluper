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
public class DeliverToDeskAlgorithm extends AbstractAlgorithm {

	private LinkedList<Integer> desks = new LinkedList<Integer>();

	public DeliverToDeskAlgorithm(RobotEnvironmentView environmentView) {

		for (DeskView desk : environmentView.getDeskViews()) {
			desks.addFirst(desk.getNumber());
		}
	}

	public Decision decide(RobotView controlledRobot, RobotEnvironmentView environmentView) {
		// Wyszukiwanie biurka z zamowieniem.
		if (controlledRobot.getCache().isEmpty()) {

			DeskView deskWithWishes = null;
			for (int i = 0; i < desks.size(); i++) {
				int number = nextDeskNumber();
				if (!environmentView.getDeskViewByNumber(number).getWishList().isEmpty()) {
					deskWithWishes = environmentView.getDeskViewByNumber(number);
					break;
				}
			}
			if (deskWithWishes == null) {
				logger.level2("Oczekiwanie na zamowienie");
				return new Decision(DecisionType.WAIT);
			} else {
				logger.level2("Pobieranie ksiazki:" + deskWithWishes.getWishList().get(0) + " dla biurka: " + deskWithWishes);
				return new Decision(DecisionType.TAKE_FROM_BOOKSHELF, deskWithWishes.getWishList().get(0));
			}
		} else {
			DeskView targetDesk = environmentView.getDeskViewByNumber(desks.getFirst());
			logger.level2("Dostarczani ksiazki:" + controlledRobot.getCache().get(0) + " do biurka: " + targetDesk);
			return new Decision(DecisionType.DELIVER_TO_DESK, targetDesk.getNumber(), controlledRobot.getCache().get(0).getIsbn());
		}

	}

	private int nextDeskNumber() {
		int number = desks.removeLast();
		desks.addFirst(number);
		return number;
	}
}
