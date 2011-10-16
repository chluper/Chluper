/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.utp.chluper.algorithm.concret;

import java.util.ArrayList;
import java.util.LinkedList;
import pl.edu.utp.chluper.algorithm.concret.RobotStates.RobotState;
import pl.edu.utp.chluper.algorithm.util.AbstractCoordinator;
import pl.edu.utp.chluper.environment.element.Book;
import pl.edu.utp.chluper.environment.view.DeskView;
import pl.edu.utp.chluper.environment.view.RobotEnvironmentView;

/**
 *
 * @author kinga
 */
public class SimpleCoordinatorBookSeparation extends AbstractCoordinator {
//

    private LinkedList<Integer> desks = new LinkedList<Integer>();  //biurka 
    private ArrayList<RobotTask> robotTask = new ArrayList<RobotTask>();    // zadania Robota
    private final RobotStates robotStates = new RobotStates();
    

    public SimpleCoordinatorBookSeparation(RobotEnvironmentView environmentView) {
        //tworzenie listy biurek, które znajdują się w bibliotece
        for (DeskView desk : environmentView.getDeskViews()) {
            desks.addFirst(desk.getNumber());
        }
    }

    public void coordinate(RobotEnvironmentView environmentView) {

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
            //Jeśli jest jakiś robot, który się nudzi
            if (robotStates.getRobotState().containsValue(RobotState.WAITING_FOR_TASK)) {
                for (String robotFree : robotStates.getRobotState().keySet()) {
                    //Jeśli to jest właśnie ten robot
                    if (robotStates.getRobotState().get(robotFree).equals(RobotState.WAITING_FOR_TASK)) {
                        //Jeśli są książki do oddania       
                        if (!deskWithWishes.getBooksToReturn().isEmpty()) {
                            for (int i = 0; i < deskWithWishes.getBooksToReturn().size(); i++) {
                                if (taskBookToReturnExist(deskWithWishes.getBooksToReturn().get(i), deskWithWishes.getNumber()) == false) {
                                    if (robotTask(robotFree).size() < environmentView.getRobotViewByName(robotFree).getCacheLimit()) {
                                        robotTask.add(new RobotTask(deskWithWishes.getNumber(), robotFree, -1, deskWithWishes.getBooksToReturn().get(i), RobotTaskToDo.DELIVER_TO_BOOKSHELF, false));
                                    }
                                }
                            }
                        }
                        //Jeśli są książki do przyniesienia
                        if (!deskWithWishes.getWishList().isEmpty()) {
                            for (int i = 0; i < deskWithWishes.getWishList().size(); i++) {
                                if (taskBookToLend(deskWithWishes.getWishList().get(i), deskWithWishes.getNumber()) == false) {
                                    if (robotTask(robotFree).size() < environmentView.getRobotViewByName(robotFree).getCacheLimit()) {
                                        robotTask.add(new RobotTask(deskWithWishes.getNumber(), robotFree, deskWithWishes.getWishList().get(i), null, RobotTaskToDo.DELIVER_TO_DESK, false));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Metoda wyznaczająca kolejne biurko do obsługi
     * @return 
     */
    private int nextDeskNumber() {
        int number = desks.removeLast();
        desks.addFirst(number);
        return number;
    }

    /**
     * Metoda do sprawdzania czy nie została już wcześniej zapisana do 
     * jakiegoś robota książka do zwrotu
     * @param bookToReturn
     * @param deskNumber
     * @return 
     */
    private boolean taskBookToReturnExist(Book bookToReturn, int deskNumber) {
        for (RobotTask tasks : robotTask) {
            if (tasks.getRobotTaskToDo().equals(RobotTaskToDo.DELIVER_TO_BOOKSHELF) && tasks.getBookToReturn().equals(bookToReturn) && tasks.getDeskNumber() == deskNumber) {
                return true;
            }
        }
        return false;
    }

    /**
     * Metoda sprawdzająca czy nie została wcześniej przypisana do 
     * jakiegoś robota książka do wypożyczenia
     * @param bookToLend
     * @param deskNumber
     * @return 
     */
    private boolean taskBookToLend(int bookToLend, int deskNumber) {
        for (RobotTask tasks : robotTask) {
            if (tasks.getRobotTaskToDo().equals(RobotTaskToDo.DELIVER_TO_DESK) && tasks.getBookToLend() == bookToLend && tasks.getDeskNumber() == deskNumber) {
                return true;
            }
        }
        return false;
    }

    /**
     * metoda pobierająca zadanie do zrobienia przez robota
     * @param robotName
     * @return 
     */
    public ArrayList<RobotTask> robotTask(String robotName) {
        ArrayList<RobotTask> robotTasks = new ArrayList<RobotTask>();
        for (RobotTask task : robotTask) {
            if (task.getRobotName().equals(robotName)) {
                robotTasks.add(task);
            }
        }
        return robotTasks;
    }

    /**
     * kończy zadanie
     * @param task
     * @return 
     */
    public ArrayList<RobotTask> finishTask(RobotTask task) {
        robotTask.remove(task);
        return robotTask;
    }
    
    public RobotStates getIncanceOfRobotStates(){
        return robotStates;
    }
}
