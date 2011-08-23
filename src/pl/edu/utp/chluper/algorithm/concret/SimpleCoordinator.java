/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.utp.chluper.algorithm.concret;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import pl.edu.utp.chluper.algorithm.util.AbstractCoordinator;
import pl.edu.utp.chluper.environment.element.Desk;
import pl.edu.utp.chluper.environment.view.DeskView;
import pl.edu.utp.chluper.environment.view.RobotEnvironmentView;
import pl.edu.utp.chluper.environment.view.RobotView;

/**
 *
 * @author damian & kinga
 */
public class SimpleCoordinator extends AbstractCoordinator {

    private LinkedList<Integer> desks = new LinkedList<Integer>();
    private HashMap<String, Enum> robotsState = new HashMap<String, Enum>();
    private HashMap<String, DeskView> deskToDo = new HashMap<String, DeskView>();
    //private LinkedList<Integer, Integer> currentTaskBooksToReturn = new HashMap<Integer, Integer>();
    //private HashMap<Integer, Integer> currentTaskBooksToReturn = new HashMap<Integer, Integer>();
    //private HashMap<Integer, DeskView> currentTaskWishList = new HashMap<Integer, DeskView>();
    private DeskView deskWithWishes;

    //Stany robotów
    public enum RobotState {

        WAITING,
        BUSY;
    }

    //Decyzja
//    public enum RobotDecisionType {
//
//        WAIT,
//        DELIVER_TO_DESK,
//        DELIVER_TO_BOOKSHELF;
//    }
    public SimpleCoordinator(RobotEnvironmentView environmentView) {
        DeskView deskWithWishes = null;
        for (DeskView desk : environmentView.getDeskViews()) {
            desks.addFirst(desk.getNumber());
        }
    }

    public void coordinate(RobotEnvironmentView environmentView) {
        //Tworzenie listy robotów obsługujących bibliotekę
        if (robotsState.isEmpty()) {
            for (RobotView robot : environmentView.getRobotViews()) {
                robotsState.put(robot.getName(), RobotState.WAITING);
            }
            logger.level2("Koordynator zczytał listę dostępnych robotów. Robotów czekających na zadanie: " + robotsState.size());
        }

        //Wyszukiwanie biurka, które bedzie obsłużone
        if (deskWithWishes == null) {
            for (int i = 0; i < desks.size(); i++) {
                int number = nextDeskNumber();
                if (!environmentView.getDeskViewByNumber(number).getWishList().isEmpty() || !environmentView.getDeskViewByNumber(number).getBooksToReturn().isEmpty()) {
                    deskWithWishes = environmentView.getDeskViewByNumber(number);
                    logger.level2("Koordynator wybrał biurko, które będzie obsługiwane. Numer biurka: " + deskWithWishes.getNumber());
                    break;
                }
            }
        } else {
//            if (!deskWithWishes.getWishList().isEmpty()) {
//                logger.level2("Obsługiwanie listy życzeń z biurka numer: " + deskWithWishes.getNumber());
//                for (int i = 0; i < deskWithWishes.getWishList().size(); i++) {
//                    deskWithWishes.getWishList().get(i);
//                }
//            }
            if (!deskWithWishes.getBooksToReturn().isEmpty() || !deskWithWishes.getWishList().isEmpty()) {
                //logger.level2("Obsługiwanie oddanych książek (sztuk: " + deskWithWishes.getBooksToReturn().size() + ") z biurka numer: " + deskWithWishes.getNumber());
                //for (int i = 0; i < deskWithWishes.getBooksToReturn().size(); i++) {

                for (String waitingRobot : robotsState.keySet()) {
                    System.out.println(deskToDo);
                    if (!deskToDo.containsValue(deskWithWishes)) {
                        if (robotsState.get(waitingRobot).equals(RobotState.WAITING)) {
                            
                            //logger.level2("Książką numer: " + deskWithWishes.getBooksToReturn().get(i).getIsbn() + " zajmie się robot: " + waitingRobot.getName());
                            logger.level2("Biurkiem numer: " + deskWithWishes.getNumber() + " zajmie się robot: " + waitingRobot);
                            robotsState.remove(waitingRobot);
                            robotsState.put(waitingRobot, RobotState.BUSY);
                            deskToDo.put(waitingRobot, deskWithWishes);
                            break;
                        }
                    } else {
                        logger.level2("To biurko jest już obsługiwane!");
                    }
                }

//}
                deskWithWishes = null;
            }
        }
    }

    public DeskView getDesksToDo(String robotName) {
        if (deskToDo.get(robotName) != null) {
            DeskView deksToDoNumber = deskToDo.get(robotName);
            return deksToDoNumber;
        } else {
            return null;
        }
    }

//    public HashMap<String, Integer> getDeskToDo() {
//        return deskToDo;
//    }
    private int nextDeskNumber() {
        int number = desks.removeLast();
        desks.addFirst(number);
        return number;
    }
}
