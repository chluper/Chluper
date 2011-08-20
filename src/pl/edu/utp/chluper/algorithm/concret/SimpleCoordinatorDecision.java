/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.utp.chluper.algorithm.concret;

import pl.edu.utp.chluper.algorithm.Decision;
import pl.edu.utp.chluper.algorithm.concret.SimpleCoordinatorDecisionType;


/**
 *
 * @author zbychu
 */

public class SimpleCoordinatorDecision{

    private final SimpleCoordinatorDecisionType decisionType;
    private final Object arg0;
    private final Object arg1;
    
    
 /*   public SimpleCoordinatorDecision(SimpleCoordinatorDecisionType decisionType, Object arg0, Object arg1) {
        this.decisionType = decisionType;
        this.arg0 = arg0;
        this.arg1 = arg1;
    }
   */ 
    public SimpleCoordinatorDecision(SimpleCoordinatorDecisionType decisionType, Object arg0) {
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

    public Object getArg0() {
        return arg0;
    }

    public Object getArg1() {
        return arg1;
    }
     
}
