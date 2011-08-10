/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.edu.utp.chluper.algorithm.student;

/**
 *
 * @author damian
 */
public class StudentDecision {

	private final StudentDecisionType decisionType;
    private final Object arg0;
    private final Object arg1;

	/**
	 * Konstruktor
	 * @param decisionType typ decyzji
	 * @param arg0
	 * @param arg1
	 */
	public StudentDecision(StudentDecisionType decisionType, Object arg0, Object arg1) {
		this.decisionType = decisionType;
		this.arg0 = arg0;
		this.arg1 = arg1;
	}

	public Object getArg0() {
		return arg0;
	}

	public Object getArg1() {
		return arg1;
	}

	public StudentDecisionType getDecisionType() {
		return decisionType;
	}

	@Override
	public String toString() {
		return "[" + this.getClass().getSimpleName() + "|" + decisionType + "|" + arg0 + "|" + arg1 + "]";
	}



}
