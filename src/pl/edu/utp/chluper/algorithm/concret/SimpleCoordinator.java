/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.utp.chluper.algorithm.concret;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import pl.edu.utp.chluper.algorithm.util.AbstractCoordinator;
import pl.edu.utp.chluper.environment.view.DeskView;
import pl.edu.utp.chluper.environment.view.RobotEnvironmentView;
import pl.edu.utp.chluper.environment.view.RobotView;

/**
 *
 * @author damian & kinga
 */
public class SimpleCoordinator extends AbstractCoordinator {

    private LinkedList<Integer> desks = new LinkedList<Integer>();  //biurka
    private ArrayList<RobotView> robots = new ArrayList<RobotView>();   //roboty
    private HashMap<String, Integer> deskToDo = new HashMap<String, Integer>(); //biurka obslugiwane

    public SimpleCoordinator(RobotEnvironmentView environmentView) {
        
        //tworzenie listy biurek, które znajdują się w bibliotece
        for (DeskView desk : environmentView.getDeskViews()) {
            desks.addFirst(desk.getNumber());
        }
    }

    public void coordinate(RobotEnvironmentView environmentView) {
        //Tworzenie listy robotów, które znajdują się w bibliotece
        if (robots.isEmpty()) {
            for (RobotView robot : environmentView.getRobotViews()) {
                robots.add(robot);
            }
            logger.level2("Koordynator zczytał listę dostępnych robotów. Robotów czekających na zadanie: " + robots.size());
        }
        //Wybieranie zajętego biurka, które powinno być obsługiwane
        DeskView deskWithWishes = null;
        for (int i = 0; i < desks.size(); i++) {
            int number = nextDeskNumber();
            if (!environmentView.getDeskViewByNumber(number).getBooksToReturn().isEmpty() || !environmentView.getDeskViewByNumber(number).getWishList().isEmpty()) {
                if (!deskToDo.containsValue(number)) {
                    deskWithWishes = environmentView.getDeskViewByNumber(number);
                    logger.level2("Koordynator wybrał biurko, które będzie obsługiwane. Numer biurka: " + deskWithWishes.getNumber());
                    break;
                }

            }
        }
        
        if (deskWithWishes != null) {
            //Jeśli biurka są zajęte....
            if (!deskWithWishes.getBooksToReturn().isEmpty() || !deskWithWishes.getWishList().isEmpty()) {
                //Wybieranie wolnego robota, który może je obsłużyć.
                //Przelatywanie listy robotów
                for (RobotView waitingRobot : robots) {
                    //Sprawdzanie czy robot nie jest już w deskToDo
                    if (!deskToDo.containsKey(waitingRobot.getName())) {
                        //Sprawdzanie czy biurko nie jest już w deskToDo
                        if (!deskToDo.containsValue(deskWithWishes.getNumber())) {
                            //Przypisywanie biurka do robota
                            logger.level2("Biurkiem numer: " + deskWithWishes.getNumber() + " zajmie się robot: " + waitingRobot.getName());
                            deskToDo.put(waitingRobot.getName(), deskWithWishes.getNumber());
                            break;

                        } else {
                            logger.level2("To biurko jest już obsługiwane!");
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
}
