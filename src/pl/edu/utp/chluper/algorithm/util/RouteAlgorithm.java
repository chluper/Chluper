/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.edu.utp.chluper.algorithm.util;

import java.util.List;
import pl.edu.utp.chluper.algorithm.Algorithm;
import pl.edu.utp.chluper.algorithm.Decision;
import pl.edu.utp.chluper.algorithm.DecisionType;
import pl.edu.utp.chluper.algorithm.graph.Graph;
import pl.edu.utp.chluper.environment.view.RobotEnvironmentView;
import pl.edu.utp.chluper.environment.element.Bookshelf;
import pl.edu.utp.chluper.environment.element.Desk;
import pl.edu.utp.chluper.environment.element.Location;
import pl.edu.utp.chluper.environment.element.Robot;
import pl.edu.utp.chluper.environment.element.Turn;
import pl.edu.utp.chluper.environment.view.BookshelfView;
import pl.edu.utp.chluper.environment.view.DeskView;
import pl.edu.utp.chluper.environment.view.RobotView;

/**
 * Algorytm realizujacy poruszanie sie
 * Wykonuje rozkazy GO_TO, GO_TO_DESK, GO_TO_BOOKSHELF
 * @author damian
 */
public class RouteAlgorithm extends WrappingAlgorithm {

	// cel do ktorego zmiezamy
	private Location target = null;

	/**
	 * Konstruktor 
	 * @param internalAlgorithm
	 */
	public RouteAlgorithm(Algorithm internalAlgorithm) {
		super(internalAlgorithm);
	}

	public Decision decide(RobotView controlledRobot, RobotEnvironmentView environmentView) {
		// jesli juz dotarlismy do celu
		if (controlledRobot.getLocation().equals(target)) {
			logger.level1("[RouteAlgorithm] Osiagnieto cel:" + target);
			// dotarlismy
			target = null;
		}
		// jesli nie mamy celu
		if (target == null) {
			// pytamy algorytm wewnetrzny o decyzje
			Decision internalAlgorithmDecision = internalAlgorithm.decide(controlledRobot, environmentView);
			logger.level1("[RouteAlgorithm] Wykonywanie rozkazu: " + internalAlgorithmDecision);
			switch (internalAlgorithmDecision.getDecisionType()) {
				// wyluskiwanie celu
				case GO_TO:
					target = (Location) internalAlgorithmDecision.getArg0();
					logger.level1("[RouteAlgorithm] Obrano nowy cel: " + target);
					break;
				case GO_TO_DESK:
					target = ((DeskView) internalAlgorithmDecision.getArg0()).getRobotPadLocation();
					logger.level1("[RouteAlgorithm] Obrano nowy cel: " + target);
					break;
				case GO_TO_BOOKSHELF:
					target = ((BookshelfView) internalAlgorithmDecision.getArg0()).getRobotPadLocation();
					logger.level1("[RouteAlgorithm] Obrano nowy cel: " + target);
					break;
				// jesli to zaden z obsluiwanych rozkazow, to przekazuje dalej
				default:
					return internalAlgorithmDecision;
			}
		}
		// dazenie do celu
		// jesli stoimy na celu
		if (controlledRobot.getLocation().equals(target)) {
			logger.level1("[RouteAlgorithm] Osiagneto cel: " + target);
			// to nie robimy nic bo nie ma to sensu
			return new Decision(DecisionType.WAIT);
		}
		// tworzenie grafu
		Graph graph = new Graph(environmentView.getWidth(), environmentView.getHeight(), controlledRobot.getLocation(), environmentView.getRobotAreaMarkerViews());
		List<Location> path = graph.getPathToLocation(target);
		logger.level1("[RouteAlgorithm] Wyznaczono sciezke: " + path);
		// interesuje nsa nastepny krok wiec
		Turn turn = Turn.getTurn(path.get(0), path.get(1), 1);
		// zwracanie rozkazu
		return new Decision(DecisionType.MOVE, turn);
	}

}
