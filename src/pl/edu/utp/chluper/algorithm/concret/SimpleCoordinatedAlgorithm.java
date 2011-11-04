/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package pl.edu.utp.chluper.algorithm.concret;

import pl.edu.utp.chluper.algorithm.Decision;
import pl.edu.utp.chluper.algorithm.DecisionType;
import pl.edu.utp.chluper.algorithm.util.AbstractAlgorithm;
import pl.edu.utp.chluper.environment.view.RobotEnvironmentView;
import pl.edu.utp.chluper.environment.view.RobotView;

/**
 *
 * @author damian
 */
public class SimpleCoordinatedAlgorithm extends AbstractAlgorithm {

    private final SimpleCoordinator coordinator;
    private Task task = null;
    // private boolean nextStep = false;

    public SimpleCoordinatedAlgorithm(SimpleCoordinator coordinator) {
        this.coordinator = coordinator;

    }

    public Decision decide(RobotView controlledRobot, RobotEnvironmentView environmentView) {

        if (this.task == null) {
            this.task = coordinator.getTask(controlledRobot);
        } else {
            coordinator.isFree(controlledRobot.getName(), task);
            this.task = coordinator.getTask(controlledRobot);
        }

        if (this.task == null) {
            return new Decision(DecisionType.WAIT);
        } else {

            switch (task.getWhatToDo()) {
                case TAKE_FROM_BOOKSHELF:
                    return new Decision(DecisionType.TAKE_FROM_BOOKSHELF, task.getIntBook());

                case TAKE_FROM_DESK:
                    return new Decision(DecisionType.TAKE_FROM_DESK, task.getDeskNumber(), task.getBBook().getIsbn());

                case DELIVER_TO_DESK:
                    return new Decision(DecisionType.DELIVER_TO_DESK, task.getDeskNumber(), task.getIntBook());

                case DELIVER_TO_BOOKSHELF:
                    return new Decision(DecisionType.DELIVER_TO_BOOKSHELF, task.getBBook().getIsbn());

                case WAIT:
                    return new Decision(DecisionType.WAIT);

                default:
                    return new Decision(DecisionType.WAIT);
            }

        }
    }
}