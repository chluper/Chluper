/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.utp.chluper.algorithm.concret;

import pl.edu.utp.chluper.environment.element.Book;

/**
 *
 * @author kinga
 */
public class RobotTask {

    private int deskNumber;
    private String robotName;
    private int bookToLend;
    private Book bookToReturn;
    private RobotTaskToDo robotTaskToDo;
    private boolean isInCache;

    public RobotTask(int deskNumber, String robotName, int bookToLend, Book bookToReturn, RobotTaskToDo robotTaskToDo, boolean isInCache) {
        this.deskNumber = deskNumber;
        this.robotName = robotName;
        this.bookToLend = bookToLend;
        this.bookToReturn = bookToReturn;
        this.robotTaskToDo = robotTaskToDo;
        this.isInCache = isInCache;
    }

    public boolean isIsInCache() {
        return isInCache;
    }

    public void setIsInCache(boolean isInCache) {
        this.isInCache = isInCache;
    }

    public int getBookToLend() {
        return bookToLend;
    }

    public Book getBookToReturn() {
        return bookToReturn;
    }

    public int getDeskNumber() {
        return deskNumber;
    }

    public String getRobotName() {
        return robotName;
    }

    public RobotTaskToDo getRobotTaskToDo() {
        return robotTaskToDo;
    }
}
