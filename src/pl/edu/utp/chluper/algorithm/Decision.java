/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.edu.utp.chluper.algorithm;

/**
 *
 * @author damian
 */
public class Decision {

    private final DecisionType decisionType;
    private final Object arg0;
    private final Object arg1;

    /**
     * Metoda tworzy decyzje
     * @param decisionType
     * @param arg0
     * @param arg1
     */
    public Decision(DecisionType decisionType, Object arg0, Object arg1) {
        this.decisionType = decisionType;
        this.arg0 = arg0;
        this.arg1 = arg1;
    }

	/**
     * Metoda tworzy decyzje
     * @param decisionType
     * @param arg0
     */
    public Decision(DecisionType decisionType, Object arg0) {
        this.decisionType = decisionType;
        this.arg0 = arg0;
        this.arg1 = null;
    }

	/**
     * Metoda tworzy decyzje
     * @param decisionType
     */
    public Decision(DecisionType decisionType) {
        this.decisionType = decisionType;
        this.arg0 = null;
        this.arg1 = null;
    }

    /**
     * Metoda zwraca decyzje 
     * @return
     */
    public DecisionType getDecisionType() {
        return decisionType;
    }

    public Object getArg0() {
        return arg0;
    }

    public Object getArg1() {
        return arg1;
    }

	@Override
	public String toString() {
		return "[" + this.getClass().getSimpleName() + "|" + decisionType + "|" + arg0 + "|" + arg1 + "]";
	}



}
