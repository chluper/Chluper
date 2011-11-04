/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.utp.chluper.algorithm.concret;

import java.util.*;
//import pl.edu.utp.chluper.algorithm.Decision;
//import pl.edu.utp.chluper.algorithm.DecisionType;
import pl.edu.utp.chluper.algorithm.DecisionType;
import pl.edu.utp.chluper.algorithm.graph.Graph;
import pl.edu.utp.chluper.algorithm.util.AbstractCoordinator;
import pl.edu.utp.chluper.environment.view.DeskView;
import pl.edu.utp.chluper.environment.view.RobotEnvironmentView;
import pl.edu.utp.chluper.environment.view.RobotView;
import pl.edu.utp.chluper.environment.element.Book;
import pl.edu.utp.chluper.environment.element.Location;

/**
 *
 * @author damian
 */
public class SimpleCoordinator extends AbstractCoordinator {

    private LinkedList<Integer> desks = new LinkedList<Integer>();
    
    private RobotEnvironmentView environmentView = null;
    
    private LinkedList<String> freeRobots = new LinkedList<String>();
    
    private LinkedList<Task> Tasks = new LinkedList<Task>();

  //  private HashMap<String, LinkedList> tasksForRobots = new HashMap<String, LinkedList>();
    
    public SimpleCoordinator(RobotEnvironmentView environmentView) {
        this.environmentView = environmentView;
    }

    public void coordinate(RobotEnvironmentView environmentView) {

       // if(tasksForRobots.isEmpty())
         //   for(RobotView robot : environmentView.getRobotViews()){
           // tasksForRobots.put(robot.getName(), new LinkedList<Task>());
       // }
//wyszukiwanie zadan - wypelnianie Tasks----------------------------------------
        Task bufor = null;
        for (DeskView desk : environmentView.getDeskViews()) {
            if (!desk.getWishList().isEmpty()) {
                for (Integer book : desk.getWishList()) {
                    bufor = new Task(TaskType.DELIVER_TO_DESK,DecisionType.TAKE_FROM_BOOKSHELF, desk.getNumber(), book);
                    if (this.Tasks.isEmpty()) {
                        this.Tasks.add(bufor);
                        this.Tasks.add(new Task(TaskType.DELIVER_TO_DESK,DecisionType.DELIVER_TO_DESK, desk.getNumber(), book));
                    } else
                        if(!existTask(bufor)){
                            Tasks.add(bufor);
                            this.Tasks.add(new Task(TaskType.DELIVER_TO_DESK,DecisionType.DELIVER_TO_DESK, desk.getNumber(), book));
                        //    break;
                        }
                }
            }
        }
        
        for (DeskView desk : environmentView.getDeskViews()) {
            if (!desk.getBooksToReturn().isEmpty()) {
                for (Book book : desk.getBooksToReturn()) {
                    bufor = new Task(TaskType.TAKE_FROM_DESK, DecisionType.TAKE_FROM_DESK,desk.getNumber(), book);
                    if (this.Tasks.isEmpty()) {
                        this.Tasks.add(bufor);
                        this.Tasks.add(new Task(TaskType.TAKE_FROM_DESK, DecisionType.DELIVER_TO_BOOKSHELF,desk.getNumber(), book));
                    } else
                        if(!existTask(bufor)){
                            Tasks.add(bufor);
                            this.Tasks.add(new Task(TaskType.TAKE_FROM_DESK, DecisionType.DELIVER_TO_BOOKSHELF,desk.getNumber(), book));
                          //  break;
                        }
                }
            }
        }
        
//!sprawdzenie ktore roboty sa wolne (liczenie zadan)-----------------------------

        if(freeRobots.isEmpty())
            for(RobotView robot : environmentView.getRobotViews()){
                freeRobots.add(robot.getName());
            }
//przydzielanie zadan wolnym robotom--------------------------------------------
 //      if (!freeRobots.isEmpty()) {

            for (Task task : this.Tasks) {

                if (!task.isSet()){
//tutaj sa wszystkie zadania ktore sa wolne
                  //  freeRobots.addLast(freeRobots.removeFirst());
                                
                    task.setRobot(freeRobots.getFirst()); 
                    
                }
            }
            freeRobots.addLast(freeRobots.removeFirst());
        }      
 //   }
//KONIEC ALGORYTMU KOORDYNATORA
    
    private boolean existTask(Task task){
        for(Task Task : Tasks){
            if(Task.equals(task))
                return true;
        }
        return false;
    }

    private int nextDeskNumber() {
        int number = desks.removeLast();
        desks.addFirst(number);
        return number;
    }

    public Task getTask(RobotView robot) {

        if (!Tasks.isEmpty()) {
            for (Task task : this.Tasks) {
                if (robot.getName().equals(task.getRobot()) && !task.isDoing()) {
                    task.setDoing();
                    return task;
                }
            }
        }
        return null;

    }
    public void isFree(String name, Task done) {
        this.Tasks.remove(done);
        if (!freeRobots.contains(name)) {
            freeRobots.add(name);
        }
    }
}