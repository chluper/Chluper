/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.utp.chluper.algorithm.concret;

import pl.edu.utp.chluper.algorithm.Decision;
import pl.edu.utp.chluper.algorithm.DecisionType;
import pl.edu.utp.chluper.algorithm.concret.RobotStates.RobotState;
import pl.edu.utp.chluper.algorithm.util.AbstractAlgorithm;
import pl.edu.utp.chluper.environment.element.Book;
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
        if(simpleCoordinator.getIncanceOfRobotStates().getRobotState(controlledRobot.getName()) == null){
            simpleCoordinator.getIncanceOfRobotStates().setRobotState(controlledRobot.getName(), RobotState.WAITING_FOR_TASK);
        }
        int deskToDoNumber = simpleCoordinator.getDesksToDo(controlledRobot.getName());
        if (deskToDoNumber != -1) {
            switch (simpleCoordinator.getIncanceOfRobotStates().getRobotState(controlledRobot.getName())) {
                case WAITING_FOR_TASK:
                    simpleCoordinator.getIncanceOfRobotStates().setRobotState(controlledRobot.getName(), RobotState.READY_TO_DO_TASKS);
                case READY_TO_DO_TASKS:
                    simpleCoordinator.getIncanceOfRobotStates().setRobotState(controlledRobot.getName(), RobotState.TAKE);
                case TAKE:
                    if (!environmentView.getDeskViewByNumber(deskToDoNumber).getBooksToReturn().isEmpty()) {
                        for (int i = 0; i < environmentView.getDeskViewByNumber(deskToDoNumber).getBooksToReturn().size(); i++) {
                            if (controlledRobot.getCache().size() < controlledRobot.getCacheLimit()) {
                                if (!controlledRobot.getCache().contains(environmentView.getDeskViewByNumber(deskToDoNumber).getBooksToReturn().get(i))) {
                                    return new Decision(DecisionType.TAKE_FROM_DESK, deskToDoNumber, environmentView.getDeskViewByNumber(deskToDoNumber).getBooksToReturn().get(i).getIsbn());
                                } else {
                                    continue;
                                }
                            } else {
                                simpleCoordinator.getIncanceOfRobotStates().setRobotState(controlledRobot.getName(), RobotState.GO);
                            }
                        }
                    }
                    if (!environmentView.getDeskViewByNumber(deskToDoNumber).getWishList().isEmpty()) {
                        for (int i = 0; i < environmentView.getDeskViewByNumber(deskToDoNumber).getWishList().size(); i++) {
                            if (controlledRobot.getCache().size() < controlledRobot.getCacheLimit()) {
                                if (!isInCache(environmentView.getDeskViewByNumber(deskToDoNumber).getWishList().get(i), controlledRobot)) {
                                    return new Decision(DecisionType.TAKE_FROM_BOOKSHELF, environmentView.getDeskViewByNumber(deskToDoNumber).getWishList().get(i));
                                } else {
                                    continue;
                                }
                            } else {
                                simpleCoordinator.getIncanceOfRobotStates().setRobotState(controlledRobot.getName(), RobotState.GO);
                            }
                        }
                    }
                    simpleCoordinator.getIncanceOfRobotStates().setRobotState(controlledRobot.getName(), RobotState.GO);
                case GO:
                    for (Book bookFromCache : controlledRobot.getCache()) {
                        if (environmentView.getDeskViewByNumber(deskToDoNumber).getWishList().contains(bookFromCache.getIsbn())) {
                            logger.level2("Dostarczanie ksiazki na biurko:" + bookFromCache.getIsbn() + " do biurka: " + deskToDoNumber);
                            return new Decision(DecisionType.DELIVER_TO_DESK, deskToDoNumber, bookFromCache.getIsbn());
                        } else {
                            logger.level2("Dostarczanie ksiazki na pulke:" + bookFromCache.getIsbn());
                            return new Decision(DecisionType.DELIVER_TO_BOOKSHELF, bookFromCache.getIsbn());
                        }
                    }
                    if (controlledRobot.getCache().isEmpty()) {
                        simpleCoordinator.removeTaskFromDeskToDo(controlledRobot.getName(), deskToDoNumber);
                        simpleCoordinator.getIncanceOfRobotStates().setRobotState(controlledRobot.getName(), RobotState.WAITING_FOR_TASK);
                    }
                    return new Decision(DecisionType.WAIT);
                default:
                    simpleCoordinator.getIncanceOfRobotStates().setRobotState(controlledRobot.getName(), RobotState.WAITING_FOR_TASK);
                    return new Decision(DecisionType.WAIT);
            }
        } else {
            return new Decision(DecisionType.WAIT);
        }
    }

    private boolean isInCache(int isbn, RobotView robotView) {
        for (Book bookInCache : robotView.getCache()) {
            if (bookInCache.getIsbn() == isbn) {
                return true;
            }
        }
        return false;
    }
}