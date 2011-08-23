/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.utp.chluper.algorithm.concret;

import pl.edu.utp.chluper.algorithm.Decision;
import pl.edu.utp.chluper.algorithm.DecisionType;
import pl.edu.utp.chluper.algorithm.util.AbstractAlgorithm;
import pl.edu.utp.chluper.environment.view.DeskView;
import pl.edu.utp.chluper.environment.view.RobotEnvironmentView;
import pl.edu.utp.chluper.environment.view.RobotView;

/**
 *
 * @author damian
 */
public class SimpleCoordinatedAlgorithm extends AbstractAlgorithm {

//	private final Coordinator coordinator;
//        
//	public SimpleCoordinatedAlgorithm(Coordinator coordinator) {
//		this.coordinator = coordinator;                
//	}
    private final SimpleCoordinator simpleCoordinator;

    public SimpleCoordinatedAlgorithm(SimpleCoordinator SimpleCoordinator) {
        this.simpleCoordinator = SimpleCoordinator;
    }

    public Decision decide(RobotView controlledRobot, RobotEnvironmentView environmentView) {
        //simpleCoordinator.coordinate(environmentView);
        DeskView deskToDo = simpleCoordinator.getDesksToDo(controlledRobot.getName());
        if (deskToDo != null) {
            if (controlledRobot.getCache().isEmpty()) {
                if (!environmentView.getDeskViewByNumber(deskToDo.getNumber()).getBooksToReturn().isEmpty()) {
                    logger.level2("Pobieranie ksiazki " + deskToDo.getBooksToReturn().get(0) + " z biurka:" + deskToDo);
                    return new Decision(DecisionType.TAKE_FROM_DESK, deskToDo.getNumber(), deskToDo.getBooksToReturn().get(0).getIsbn());

                }
                else{
                    return new Decision(DecisionType.WAIT);
                }
            } else {
                logger.level2("Dostarczanie ksiazki na pulke:" + controlledRobot.getCache().get(0));
                return new Decision(DecisionType.DELIVER_TO_BOOKSHELF, controlledRobot.getCache().get(0).getIsbn());
            }

        } else {
            return new Decision(DecisionType.WAIT);
        }

    }
}
