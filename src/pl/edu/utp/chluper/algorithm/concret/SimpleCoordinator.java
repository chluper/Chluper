/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.utp.chluper.algorithm.concret;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import pl.edu.utp.chluper.algorithm.Decision;
import pl.edu.utp.chluper.algorithm.DecisionType;
import pl.edu.utp.chluper.algorithm.util.AbstractCoordinator;
import pl.edu.utp.chluper.environment.view.DeskView;
import pl.edu.utp.chluper.environment.view.RobotEnvironmentView;
import pl.edu.utp.chluper.environment.view.RobotView;
import pl.edu.utp.chluper.environment.element.Book;

/**
 *
 * @author damian
 */
public class SimpleCoordinator extends AbstractCoordinator {

    private LinkedList<Integer> desks = new LinkedList<Integer>();
    private bookDesk rozdzielanie;
    private HashMap<String, SimpleCoordinatorDecision> zadania = new HashMap<String, SimpleCoordinatorDecision>();

    public SimpleCoordinator(RobotEnvironmentView environmentView) {

        rozdzielanie = new bookDesk(environmentView);

        for (DeskView desk : environmentView.getDeskViews()) {
            desks.addFirst(desk.getNumber());
        }

    }

    public void coordinate(RobotEnvironmentView environmentView) {

        // TODO zaimplementwoac
        String freeRobot = null;
        for (RobotView robotName : environmentView.getRobotViews()) {
            if (!zadania.containsKey(robotName.getName())) {
                freeRobot = robotName.getName();
                break;
            }
        }
        if (freeRobot != null) {
            SimpleCoordinatorDecision decyzja = null;
            for (int i = 0; i < desks.size(); i++) {
                int numberOfDesk = nextDeskNumber();
                
                Integer deliverToDeskBook = rozdzielanie.getBookDeliver_to_desk(numberOfDesk, environmentView);
                if (deliverToDeskBook != null) {
                    //mamy ksiazke, moze warto sprawdzic czy jest nastepna?
                    decyzja = new SimpleCoordinatorDecision(SimpleCoordinatorDecisionType.DELIVER_TO_DESK, numberOfDesk, new Book(deliverToDeskBook));
                    break;
                }
                
                Book takeFromDeskBook = rozdzielanie.getBookTake_from_desk(numberOfDesk, environmentView);
                if (takeFromDeskBook != null) {
                    //mamy ksiazke, moze warto pobrac nastepna?
                    decyzja = new SimpleCoordinatorDecision(SimpleCoordinatorDecisionType.TAKE_FROM_DESK, numberOfDesk, takeFromDeskBook);
                    break;
                }
            }

            if (decyzja != null) {
                zadania.put(freeRobot, decyzja);
            }
        }

    }


    private int nextDeskNumber() {
        int number = desks.removeLast();
        desks.addFirst(number);
        return number;
    }

    private void returnDeskNumber() {
        desks.addLast(desks.removeFirst());
    }

    public SimpleCoordinatorDecision getSimpleCoordinatorDecision(RobotView robot) {
        if (zadania.containsKey(robot.getName())) {
            return zadania.get(robot.getName());
        } else {
            return new SimpleCoordinatorDecision(SimpleCoordinatorDecisionType.WAIT);
        }
    }

    public void isFree(String name) {

        SimpleCoordinatorDecision decyzja = zadania.get(name);
        zadania.remove(name);
        rozdzielanie.free(decyzja);

    }
}

class bookDesk {

    HashMap<Integer, HashSet> take_from_desk = new HashMap<Integer, HashSet>();
    HashMap<Integer, HashSet> deliver_to_desk = new HashMap<Integer, HashSet>();

    bookDesk(RobotEnvironmentView environmentView) {
        for (DeskView desk : environmentView.getDeskViews()) {
            take_from_desk.put(desk.getNumber(), new HashSet<Book>());
            deliver_to_desk.put(desk.getNumber(), new HashSet<Integer>());

        }

    }

    public Book getBookTake_from_desk(int desk, RobotEnvironmentView environmentView) {
        if (!environmentView.getDeskViewByNumber(desk).getBooksToReturn().isEmpty()) {
            HashSet<Book> existBooks = take_from_desk.get(desk);
            for (Book books : environmentView.getDeskViewByNumber(desk).getBooksToReturn()) {
                if (!existBooks.contains(books)) {
                    //tutaj mozna dodac ksiazke dla jakiegos robota?
                    existBooks.add(books);
                    take_from_desk.put(desk, existBooks);
                    return books;
                }
            }

        }
        return null;
    }

    public Integer getBookDeliver_to_desk(int desk, RobotEnvironmentView environmentView) {
        if (!environmentView.getDeskViewByNumber(desk).getWishList().isEmpty()) {
            HashSet existBooks = deliver_to_desk.get(desk);
            for (Integer books : environmentView.getDeskViewByNumber(desk).getWishList()) {
                if (!existBooks.contains(books)) {
                    existBooks.add(books);
                    deliver_to_desk.put(desk, existBooks);
                    return books;
                }
            }

        }
        return null;
    }

    public void free(SimpleCoordinatorDecision decyzja) {
        HashSet existBooks;
        switch (decyzja.getDecisionType()) {
            case TAKE_FROM_DESK:
                existBooks = take_from_desk.get(decyzja.getArg0());
                existBooks.remove(decyzja.getArg1());
                take_from_desk.put(decyzja.getArg0(), existBooks);
                break;
            case DELIVER_TO_DESK:
                existBooks = deliver_to_desk.get(decyzja.getArg0());
                existBooks.remove(decyzja.getArg1().getIsbn());
                take_from_desk.put(decyzja.getArg0(), existBooks);
                break;

        }

    }
}