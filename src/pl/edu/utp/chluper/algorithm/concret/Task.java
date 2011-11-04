/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.utp.chluper.algorithm.concret;

import pl.edu.utp.chluper.algorithm.Decision;
import pl.edu.utp.chluper.algorithm.DecisionType;
import pl.edu.utp.chluper.algorithm.graph.Graph;
import pl.edu.utp.chluper.environment.element.Book;
import pl.edu.utp.chluper.environment.element.Location;
import pl.edu.utp.chluper.environment.view.DeskView;
import pl.edu.utp.chluper.environment.view.RobotEnvironmentView;

/**
 *
 * @author zbychu
 */

public class Task {
    
    

    private Book Bbook = null;
    private Integer Intbook = null;
    private String robotName = null;
    private Integer deskNumber = null;
    private boolean isDoing = false;
    private boolean isDone = false;
    private DecisionType whatToDo = null;

    TaskType type = TaskType.WAIT;
    
    public boolean equals(Task task){
        if(task.type!=this.type)
            return false;
        if(Bbook == null)
            if(task.Intbook != this.Intbook)
                return false;
        if(Intbook == null)
              if(task.Bbook != this.getBBook())
            return false;    
        if(deskNumber != task.getDeskNumber())
            return false;
//DODANE 
 //       if(this.whatToDo != task.getWhatToDo())
 //           return false;
//DODANE        
        return true;
    }
    /*
    TAKE_FROM_DESK(),
    DELIVER_TO_DESK(),
    WAIT(),
     */
    public Task(TaskType decision, DecisionType whatToDo, Integer desk, Integer book){
        this.type = decision;
        this.whatToDo = whatToDo;
        this.deskNumber = desk;
        this.Intbook= book;  

    }
        public Task(TaskType decision, DecisionType whatToDo, Integer desk, Book book){
        this.type = decision;
        this.whatToDo = whatToDo;
        this.deskNumber = desk;
        this.Bbook= book;

    }
        public boolean isDoing(){
            return this.isDoing;

        }

        public boolean isSet(){
            if(robotName == null)
                return false;
            else
                return true;
        }
        public void setRobot(String name){
            robotName = name;
        }
        public String getRobot(){
            return robotName;
        }
        public TaskType getType(){
            return this.type;
        }
        public Book getBBook(){
            return this.Bbook;
        }
        public int getDeskNumber(){
            return this.deskNumber;
        }
        public Integer getIntBook(){
            return this.Intbook;
        }
        public void setDoing(){
            this.isDoing=true;
        }
        public DecisionType getWhatToDo(){
            return this.whatToDo;
        }

    
}
