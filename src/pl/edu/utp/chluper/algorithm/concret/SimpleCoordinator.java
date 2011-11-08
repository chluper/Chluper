/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.utp.chluper.algorithm.concret;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import pl.edu.utp.chluper.algorithm.concret.RobotStates.RobotState;
import pl.edu.utp.chluper.algorithm.util.AbstractCoordinator;
import pl.edu.utp.chluper.environment.view.DeskView;
import pl.edu.utp.chluper.environment.view.RobotEnvironmentView;
import pl.edu.utp.chluper.environment.view.RobotView;

/**
 *
 * @author kinga
 */
public class SimpleCoordinator extends AbstractCoordinator {

    private LinkedList<Integer> desks = new LinkedList<Integer>();  //biurka
    private final RobotStates robotStates = new RobotStates();
    //private ArrayList<RobotView> robots = new ArrayList<RobotView>();   //roboty
    private HashMap<String, Integer> deskToDo = new HashMap<String, Integer>(); //biurka obslugiwane

    public SimpleCoordinator(RobotEnvironmentView environmentView) {
        //tworzenie listy biurek, które znajdują się w bibliotece
        for (DeskView desk : environmentView.getDeskViews()) {
            desks.addFirst(desk.getNumber());
        }
    }

    public void coordinate(RobotEnvironmentView environmentView) {
        //Wybieranie zajętego biurka, które powinno być obsługiwane

        DeskView deskWithWishes = null;
        for (int i = 0; i < desks.size(); i++) {
            int number = nextDeskNumber();
            if (!environmentView.getDeskViewByNumber(number).getBooksToReturn().isEmpty() || !environmentView.getDeskViewByNumber(number).getWishList().isEmpty()) {
                deskWithWishes = environmentView.getDeskViewByNumber(number);
                logger.level1("Koordynator wybrał biurko, które będzie obsługiwane. Numer biurka: " + deskWithWishes.getNumber());
                break;
            }
        }

        if (deskWithWishes != null) {
            if (robotStates.getRobotState().containsValue(RobotState.WAITING_FOR_TASK)) {
                for (String robotFree : robotStates.getRobotState().keySet()) {
                    //Jeśli to jest właśnie ten robot
                    if (robotStates.getRobotState().get(robotFree).equals(RobotState.WAITING_FOR_TASK)) {
                        //Jeśli biurko nie jest obsługiwane
                        if (!deskToDo.containsValue(deskWithWishes.getNumber())) {
                            logger.level1("Biurkiem numer: " + deskWithWishes.getNumber() + " zajmie się robot: " + robotFree);
                            deskToDo.put(robotFree, deskWithWishes.getNumber());
                            break;
                        } else {
                            logger.level1("To biurko jest już obsługiwane!");
                        }
                    }
                }
            }
        }
    }

    public HashMap<String, Integer> removeTaskFromDeskToDo(String robotName, int deskNumber) {

        if (!deskToDo.isEmpty() && deskToDo.get(robotName).equals(deskNumber)) {
            deskToDo.remove(robotName);
            logger.level2("Robot" + robotName + " zakończył obsługiwanie biurka " + deskNumber);
        }
        return deskToDo;
    }

    public int getDesksToDo(String robotName) {
        if (deskToDo.get(robotName) != null) {
            int deksToDoNumber = deskToDo.get(robotName);
            return deksToDoNumber;
        } else {
            return -1;
        }
    }

    private int nextDeskNumber() {
        int number = desks.removeLast();
        desks.addFirst(number);
        return number;
    }

    public RobotStates getIncanceOfRobotStates() {
        return robotStates;
    }
}
