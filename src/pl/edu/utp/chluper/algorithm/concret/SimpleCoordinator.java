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
        for (int i=0;i<desks.size();i++){
            int numberOfDesk = nextDeskNumber();
            DeskView desk=environmentView.getDeskViewByNumber(numberOfDesk);
            if(!desk.getBooksToReturn().isEmpty()){
                decyzja = new SimpleCoordinatorDecision(SimpleCoordinatorDecisionType.TAKE_FROM_DESK, numberOfDesk);
                break;
            } 
            if(!desk.getWishList().isEmpty()){
                decyzja = new SimpleCoordinatorDecision(SimpleCoordinatorDecisionType.DELIVER_TO_DESK, numberOfDesk);
                break;
            }
        }
        //trzeba sprawdzic czy dane zadanie nie jest juz przez jakiegos robota wykonywane
        if(decyzja!= null)
        for (String doingRobot : zadania.keySet()){
            SimpleCoordinatorDecision zadanieRobota = zadania.get(doingRobot);
            if(zadanieRobota.getDecisionType() == decyzja.getDecisionType() && zadanieRobota.getArg0() == decyzja.getArg0()){
                logger.level2("TO ZADANIE JEST JUZ WYKONYWANE!");
                decyzja = null;
                break;
            }
        }
            
            
        RobotView freeRobot = null;
        //jezeli nie ma nic do pracy, to po co szukac robotow
        if(decyzja != null){
        for(RobotView robot : environmentView.getRobotViews()){

                    if(!robotsOnDuty.contains(robot.getName())){
                        //robot jest wolny i skory do pracy
                        logger.level2("znaleziono wolnego robota!");
                        robotsOnDuty.add(robot.getName());
                        zadania.put(robot.getName(), decyzja);
                        freeRobot=robot;
                        break;
                    } else{
                        logger.level2("ten robot pracuje!");
                        logger.level2(robot.getName());
                        SimpleCoordinatorDecision decision= zadania.get(robot.getName());
                        if(decision.getDecisionType()==SimpleCoordinatorDecisionType.TAKE_FROM_DESK && robot.getCache().isEmpty()){
                            logger.level2("ROBOT SKONCZYL PRACE!");
                            zadania.remove(robot.getName());
                            robotsOnDuty.remove(robot.getName());
                        }
                        if(decision.getDecisionType()==SimpleCoordinatorDecisionType.DELIVER_TO_DESK && robot.getCache().isEmpty()){
                            logger.level2("ROBOT SKONCZYL PRACE!");
                            zadania.remove(robot.getName());
                            robotsOnDuty.remove(robot.getName());
                        }
                    }        
        }
        }else
            //nie ma nic do pracy, roboty czekaja
        {
            
        }
        //pora posprzatac
            
        if(freeRobot == null)
        {
            logger.level2("brak wolnych robotow, lub zadan");
        }
       
        logger.level2("oto zadania: ");
        for (String nazwaRobota : zadania.keySet()){
            SimpleCoordinatorDecision zadanie = zadania.get(nazwaRobota);
            logger.level2("nazwa robota: "+nazwaRobota);
            if(zadanie.getDecisionType()==SimpleCoordinatorDecisionType.DELIVER_TO_DESK)
            logger.level2("typ akcji: dostarczanie do biurka " );
            else
                logger.level2("typ akcji: zabieranie z biurka ");
            logger.level2("biurko: "+ zadanie.getArg0());
            
        }
        
        
        /*
        DeskView deskWithBooks = null;
        for (int i = 0; i < desks.size(); i++) {
            int number = nextDeskNumber();
            if (!environmentView.getDeskViewByNumber(number).getBooksToReturn().isEmpty()) {
                desksToDo.add(number);
                
            }
        }
        if (desksToDo.isEmpty()) {
            logger.level2("BRAK KSIAZEK");
        } else {
            logger.level2("SA KSIAZKI");
            String RobotName = null;
            if (robotsOnDuty.isEmpty()) {
                logger.level2("ZADEN ROBOT NIE PRACUJE");
                for (RobotView robot : environmentView.getRobotViews()) {
                    robotsOnDuty.add(robot.getName());
                    RobotName = robot.getName();
                    break;
                }
            } else {
                for (RobotView robot : environmentView.getRobotViews()) {
                    
                    if (!robotsOnDuty.contains(robot.getName())) {
                        for (Decision decyzje : zadania.values()) {
                            
                                if(decyzje.getDecisionType()!=DecisionType.TAKE_FROM_DESK && (Integer)decyzje.getArg0()!=deskWithBooks.getNumber())    
                                {  logger.level2("juz ktos to nakurwia to: "+decyzje.getDecisionType());
                                logger.level2("i to: "+decyzje.getArg0());
                                robotsOnDuty.add(robot.getName());
                            
                                name = robot.getName();
                        }

                        }


                    }
                }
            }
            if (name != null) {
                logger.level2("ROBOT: " + name + " MA WYKONAC ZADANIE ODDANIA KSIAZKI NA POLKE");
                zadania.put(name, new Decision(DecisionType.TAKE_FROM_DESK, deskWithBooks.getNumber(), deskWithBooks.getBooksToReturn().get(0).getIsbn()));
            } else {
                logger.level2("WSZYSTKIE ROBOTY PRACUJA");
            }
        }*/

    }
    //  return new Decision(DecisionType.WAIT);
    
    private int nextDeskNumber() {
        int number = desks.removeLast();
        desks.addFirst(number);
        return number;
    }

    public SimpleCoordinatorDecision getSimpleCoordinatorDecision(RobotView robot) {
        if(zadania.containsKey(robot.getName()))
            return zadania.get(robot.getName());
        else
            return new SimpleCoordinatorDecision(SimpleCoordinatorDecisionType.WAIT);
    }
    
    public void isFree(String name){
        zadania.remove(name);
        robotsOnDuty.remove(name);
        
    }
}
