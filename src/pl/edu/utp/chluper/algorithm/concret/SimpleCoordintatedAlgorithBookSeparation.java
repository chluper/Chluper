/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.utp.chluper.algorithm.concret;

import java.util.ArrayList;
import pl.edu.utp.chluper.algorithm.Decision;
import pl.edu.utp.chluper.algorithm.DecisionType;
import pl.edu.utp.chluper.algorithm.concret.RobotStates.RobotState;
import pl.edu.utp.chluper.algorithm.util.AbstractAlgorithm;
import pl.edu.utp.chluper.environment.element.Book;
import pl.edu.utp.chluper.environment.view.RobotEnvironmentView;
import pl.edu.utp.chluper.environment.view.RobotView;

/**
 *
 * @author kinga
 */
public class SimpleCoordintatedAlgorithBookSeparation extends AbstractAlgorithm {

    private final SimpleCoordinatorBookSeparation simpleCoordinatorBookSeparation;
    private ArrayList<RobotTask> robotTasks = new ArrayList<RobotTask>();
    private ArrayList<RobotTask> supportedRobotTask = new ArrayList<RobotTask>();

    public SimpleCoordintatedAlgorithBookSeparation(SimpleCoordinatorBookSeparation simpleCoordinatorBookSeparation) {
        this.simpleCoordinatorBookSeparation = simpleCoordinatorBookSeparation;
    }

    public Decision decide(RobotView controlledRobot, RobotEnvironmentView environmentView) {
        robotTasks = simpleCoordinatorBookSeparation.robotTask(controlledRobot.getName());
        if (!robotTasks.isEmpty()) {
            for (RobotTask task : robotTasks) {
                switch (task.getRobotTaskToDo()) {
                    case DELIVER_TO_BOOKSHELF:
                        switch (simpleCoordinatorBookSeparation.getIncanceOfRobotStates().getRobotState(controlledRobot.getName())) {
                            case WAITING_FOR_TASK:
                                simpleCoordinatorBookSeparation.getIncanceOfRobotStates().setRobotState(controlledRobot.getName(), RobotState.READY_TO_DO_TASKS);
                            case READY_TO_DO_TASKS:
                                simpleCoordinatorBookSeparation.getIncanceOfRobotStates().setRobotState(controlledRobot.getName(), RobotState.TAKE);
                            case TAKE:
                                if (!controlledRobot.getCache().contains(task.getBookToReturn())) {
                                    logger.level2("Pobieram książkę" + task.getBookToReturn() + "z biurka: " + task.getDeskNumber());
                                    supportedRobotTask.add(task);
                                    return new Decision(DecisionType.TAKE_FROM_DESK, task.getDeskNumber(), task.getBookToReturn().getIsbn());
                                } else {
                                    if (!isUnsupoortedTasks(robotTasks, supportedRobotTask)) {
                                        simpleCoordinatorBookSeparation.getIncanceOfRobotStates().setRobotState(controlledRobot.getName(), RobotState.GO);
                                    } else {
                                        simpleCoordinatorBookSeparation.getIncanceOfRobotStates().setRobotState(controlledRobot.getName(), RobotState.READY_TO_DO_TASKS);
                                        continue;
                                    }
                                }
                            case GO:
                                logger.level2("Mam w kieszeni: " + controlledRobot.getCache().size() + " książek");
                                for (RobotTask supportedTask : supportedRobotTask) {
                                    if (supportedTask.getBookToReturn() != null) {
                                        if (controlledRobot.getCache().contains(supportedTask.getBookToReturn())) {
                                            return new Decision(DecisionType.DELIVER_TO_BOOKSHELF, supportedTask.getBookToReturn().getIsbn());
                                        } else {
                                            logger.level2("Książka do oddania: " + supportedTask.getBookToReturn() + " została obsłużona!");
                                            simpleCoordinatorBookSeparation.finishTask(supportedTask);
                                        }
                                    }
                                }
                                break;
                            default:
                                logger.level3("Błąd! Nieznany stan robota! DELIVER TO BOOKSHELF " + simpleCoordinatorBookSeparation.getIncanceOfRobotStates().getRobotState(controlledRobot.getName()));
                                return new Decision(DecisionType.WAIT);
                        }
                        break;
                    case DELIVER_TO_DESK:
                        switch (simpleCoordinatorBookSeparation.getIncanceOfRobotStates().getRobotState(controlledRobot.getName())) {
                            case WAITING_FOR_TASK:
                                simpleCoordinatorBookSeparation.getIncanceOfRobotStates().setRobotState(controlledRobot.getName(), RobotState.READY_TO_DO_TASKS);
                            case READY_TO_DO_TASKS:
                                simpleCoordinatorBookSeparation.getIncanceOfRobotStates().setRobotState(controlledRobot.getName(), RobotState.TAKE);
                            case TAKE:
                                if (isInCache(task.getBookToLend(), controlledRobot) == false) {
                                    logger.level2("Pobieram książkę: " + task.getBookToLend() + " z pułki.");
                                    supportedRobotTask.add(task);
                                    return new Decision(DecisionType.TAKE_FROM_BOOKSHELF, task.getBookToLend());
                                } else {
                                    if (!isUnsupoortedTasks(robotTasks, supportedRobotTask)) {
                                        simpleCoordinatorBookSeparation.getIncanceOfRobotStates().setRobotState(controlledRobot.getName(), RobotState.GO);
                                    } else {
                                        simpleCoordinatorBookSeparation.getIncanceOfRobotStates().setRobotState(controlledRobot.getName(), RobotState.READY_TO_DO_TASKS);
                                        continue;
                                    }
                                }
                            case GO:
                                logger.level2("Mam w kieszeni: " + controlledRobot.getCache().size() + " ksiażek.");
                                for (RobotTask supportedTask : supportedRobotTask) {
                                    if (supportedTask.getBookToLend() != -1) {
                                        if (isInCache(supportedTask.getBookToLend(), controlledRobot) == true) {
                                            return new Decision(DecisionType.DELIVER_TO_DESK, supportedTask.getDeskNumber(), supportedTask.getBookToLend());
                                        } else {
                                            logger.level2("Książka do wypożyczenia: " + supportedTask.getBookToLend() + " została obsłużona!");
                                            simpleCoordinatorBookSeparation.finishTask(supportedTask);
                                        }
                                    }
                                }
                                break;
                            default:
                                logger.level3("Błąd! Nieznany stan robota! DELIVER TO DESK " + simpleCoordinatorBookSeparation.getIncanceOfRobotStates().getRobotState(controlledRobot.getName()));
                                return new Decision(DecisionType.WAIT);
                        }
                        break;
                    default:
                        logger.level3("Błąd! Nieznane zadanie!" + task.getRobotTaskToDo());
                        return new Decision(DecisionType.WAIT);
                }
            }
            if (controlledRobot.getCache().isEmpty()) {
                logger.level2("Rozwiozłem wszystkie książki!");
                simpleCoordinatorBookSeparation.getIncanceOfRobotStates().setRobotState(controlledRobot.getName(), RobotState.READY_TO_DO_TASKS);
            }
            return new Decision(DecisionType.WAIT);
        } else {
            supportedRobotTask.clear();
            logger.level2("Oczekuję na zadania");
            simpleCoordinatorBookSeparation.getIncanceOfRobotStates().setRobotState(controlledRobot.getName(), RobotState.WAITING_FOR_TASK);
            return new Decision(DecisionType.WAIT);
        }
    }

    /**
     * Metoda sprawdzająca czy książka jest w kieszeni
     * @param isbn
     * @param robotView
     * @return 
     */
    private boolean isInCache(int isbn, RobotView robotView) {
        for (Book bookInCache : robotView.getCache()) {
            if (bookInCache.getIsbn() == isbn) {
                return true;
            }
        }
        return false;
    }

    /**
     * Metoda sprawdzająca czy są zadania, które nie zostały jeszcze obsłużone
     * @param robotTasks
     * @param supportedRobotTasks
     * @return 
     */
    private boolean isUnsupoortedTasks(ArrayList<RobotTask> robotTasks, ArrayList<RobotTask> supportedRobotTasks) {
        for (RobotTask task : robotTasks) {
            if (!supportedRobotTasks.contains(task)) {
                return true;
            }
        }
        return false;
    }
}
