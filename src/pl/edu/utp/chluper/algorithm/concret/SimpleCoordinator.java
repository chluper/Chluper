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

/**
 *
 * @author damian
 */
public class SimpleCoordinator extends AbstractCoordinator {

    private LinkedList<Integer> desks = new LinkedList<Integer>();
    private HashSet<String> robotsOnDuty = new HashSet<String>();
    private LinkedList<Integer> desksToDo = new LinkedList<Integer>();
    // private HashSet<Integer> obslugiwane_desks = new HashSet<Integer>();
    //c private HashSet<String> obslugiwane_robots = new HashSet<String>();
    private HashMap<String, SimpleCoordinatorDecision> zadania = new HashMap<String, SimpleCoordinatorDecision>();

    public SimpleCoordinator(RobotEnvironmentView environmentView) {

        for (DeskView desk : environmentView.getDeskViews()) {
            //    desk.
            desks.addFirst(desk.getNumber());

        }

    }

    public void coordinate(RobotEnvironmentView environmentView) {

        // TODO zaimplementwoac


        SimpleCoordinatorDecision decyzja = null;
        //DeskView doObslugi = null;
        //na poczatku wyszukujemy czy sa zadania
        for (int i = 0; i < desks.size(); i++) {
            int numberOfDesk = nextDeskNumber();
            DeskView desk = environmentView.getDeskViewByNumber(numberOfDesk);
            // logger.level2("LICZBA KSIAZEK: " + desk.getBooksToReturn().size() + " NA BIORKU: "+numberOfDesk);

            //tutaj nie do konca tak ma byc. jezeli pierwsza decyzja bedzie juz obslugiwana przez robota
            //to dopiero bedzie pozniej sprawdzone i koordynator nie przydzieli zadania (bo zrobi break'a). 
            //powinno byc tak, ze wraz z sprawdzeniem, jakie zadanie ma byc wykonywane, trzeba sprawdzic czy 
            //robot juz to wykonuje... 
            if (!desk.getWishList().isEmpty()) {
                //teraz, trzeba sprawdzic czy to zadanie nie jest przydzielone komus, jezeli tak to pozniej nie zostanie przydzielony
                //zaden robor do innego zadania (chociaz beda)
                decyzja = new SimpleCoordinatorDecision(SimpleCoordinatorDecisionType.DELIVER_TO_DESK, numberOfDesk);

                for (String doingRobot : zadania.keySet()) {
                    SimpleCoordinatorDecision zadanieRobota = zadania.get(doingRobot);

                    if (zadanieRobota.getDecisionType() == decyzja.getDecisionType() && zadanieRobota.getArg0() == decyzja.getArg0()) {
                        logger.level2("TO ZADANIE JEST JUZ WYKONYWANE!");

                        decyzja = null;
                        break;
                    }
                }

                if (decyzja != null) {
                    break;
                }
            }
            if (!desk.getBooksToReturn().isEmpty()) {
                decyzja = new SimpleCoordinatorDecision(SimpleCoordinatorDecisionType.TAKE_FROM_DESK, numberOfDesk);

                for (String doingRobot : zadania.keySet()) {
                    SimpleCoordinatorDecision zadanieRobota = zadania.get(doingRobot);

                    if (zadanieRobota.getDecisionType() == decyzja.getDecisionType() && zadanieRobota.getArg0() == decyzja.getArg0()) {
                        logger.level2("TO ZADANIE JEST JUZ WYKONYWANE!");

                        decyzja = null;
                        break;
                    }
                }

                if (decyzja != null) {
                    break;
                }
            }

        }

        RobotView freeRobot = null;
        //jezeli nie ma nic do pracy, to po co szukac robotow
        if (decyzja != null) {
            for (RobotView robot : environmentView.getRobotViews()) {

                if (!robotsOnDuty.contains(robot.getName())) {
                    //robot jest wolny i skory do pracy
                    logger.level2("znaleziono wolnego robota!");
                    robotsOnDuty.add(robot.getName());
                    zadania.put(robot.getName(), decyzja);
                    freeRobot = robot;
                    break;
                }
            }
        }



    }
    //  return new Decision(DecisionType.WAIT);

    private int nextDeskNumber() {
        int number = desks.removeLast();
        desks.addFirst(number);
        return number;
    }

    public SimpleCoordinatorDecision getSimpleCoordinatorDecision(RobotView robot) {
        if (zadania.containsKey(robot.getName())) {
            return zadania.get(robot.getName());
        } else {
            return new SimpleCoordinatorDecision(SimpleCoordinatorDecisionType.WAIT);
        }
    }

    public void isFree(String name) {
        zadania.remove(name);
        robotsOnDuty.remove(name);

    }
}
