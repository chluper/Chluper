/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.utp.chluper.algorithm.concret;

import java.util.LinkedList;
import pl.edu.utp.chluper.algorithm.Decision;
import pl.edu.utp.chluper.algorithm.concret.SimpleCoordinatorDecisionType;
import pl.edu.utp.chluper.environment.element.Book;


/**
 *
 * @author zbychu
 */

public class SimpleCoordinatorDecision{

    private final SimpleCoordinatorDecisionType decisionType;
    private final Integer arg0;
    private final LinkedList<Book> arg1;
    
    
    public SimpleCoordinatorDecision(SimpleCoordinatorDecisionType decisionType, Integer arg0, LinkedList<Book> arg1) {
        this.decisionType = decisionType;
        this.arg0 = arg0;
        this.arg1 = arg1;
    }
  
    public SimpleCoordinatorDecision(SimpleCoordinatorDecisionType decisionType, Integer arg0) {
        this.decisionType = decisionType;
        this.arg0 = arg0;
        this.arg1 = null;
    }
    
    public SimpleCoordinatorDecision(SimpleCoordinatorDecisionType decisionType) {
        this.decisionType = decisionType;
        this.arg0 = null;
        this.arg1 = null;
    }
    public SimpleCoordinatorDecisionType getDecisionType() {
        return decisionType;
    }

    public Integer getArg0() {
        return arg0;
    }

    public LinkedList<Book> getArg1() {
        return arg1;
    }
     
}
