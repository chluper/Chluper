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
 * @author damian & kinga
 */
public class SimpleCoordinatedAlgorithm extends AbstractAlgorithm {

    private final SimpleCoordinator simpleCoordinator;

    public SimpleCoordinatedAlgorithm(SimpleCoordinator SimpleCoordinator) {
        this.simpleCoordinator = SimpleCoordinator;
    }

    public Decision decide(RobotView controlledRobot, RobotEnvironmentView environmentView) {
        int deskToDoNumber = simpleCoordinator.getDesksToDo(controlledRobot.getName());
        if (deskToDoNumber != -1) {

            DeskView deskToDo = environmentView.getDeskViewByNumber(deskToDoNumber);
            if (!environmentView.getDeskViewByNumber(deskToDo.getNumber()).getBooksToReturn().isEmpty() || !environmentView.getDeskViewByNumber(deskToDo.getNumber()).getWishList().isEmpty()) {
                if (!environmentView.getDeskViewByNumber(deskToDo.getNumber()).getBooksToReturn().isEmpty()) {
                    if (controlledRobot.getCache().isEmpty()) {
                        logger.level2("Pobieranie ksiazki " + deskToDo.getBooksToReturn().get(0) + " z biurka:" + deskToDo);
                        return new Decision(DecisionType.TAKE_FROM_DESK, deskToDo.getNumber(), deskToDo.getBooksToReturn().get(0).getIsbn());
                    } else {
                        logger.level2("Dostarczanie ksiazki na pulke:" + controlledRobot.getCache().get(0));
                        return new Decision(DecisionType.DELIVER_TO_BOOKSHELF, controlledRobot.getCache().get(0).getIsbn());
                    }
                } else {
                    if (controlledRobot.getCache().isEmpty()) {
                        logger.level2("Pobieranie ksiazki:" + deskToDo.getWishList().get(0) + " dla biurka: " + deskToDo.getNumber());
                        return new Decision(DecisionType.TAKE_FROM_BOOKSHELF, deskToDo.getWishList().get(0));
                    } else {
                        if (controlledRobot.getCache().get(0).getIsbn() == deskToDo.getWishList().get(0)) {
                            logger.level2("Dostarczani ksiazki:" + controlledRobot.getCache().get(0) + " do biurka: " + deskToDo.getNumber());
                            return new Decision(DecisionType.DELIVER_TO_DESK, deskToDo.getNumber(), controlledRobot.getCache().get(0).getIsbn());
                        } else {
                            logger.level2("Dostarczanie ksiazki na pulke:" + controlledRobot.getCache().get(0));
                            return new Decision(DecisionType.DELIVER_TO_BOOKSHELF, controlledRobot.getCache().get(0).getIsbn());
                        }
                    }
                }
            } else {
                simpleCoordinator.removeTaskFromDeskToDo(controlledRobot.getName(), deskToDo.getNumber());
                return new Decision(DecisionType.WAIT);
            }
        } else {
            return new Decision(DecisionType.WAIT);
        }
    }
}
