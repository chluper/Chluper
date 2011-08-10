/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.edu.utp.chluper.algorithm.concret;

import pl.edu.utp.chluper.algorithm.Coordinator;
import pl.edu.utp.chluper.algorithm.Decision;
import pl.edu.utp.chluper.algorithm.DecisionType;
import pl.edu.utp.chluper.algorithm.util.AbstractAlgorithm;
import pl.edu.utp.chluper.environment.view.RobotEnvironmentView;
import pl.edu.utp.chluper.environment.view.RobotView;

/**
 *
 * @author damian
 */
public class SimpleCoordinatedAlgorithm extends AbstractAlgorithm {

	private final Coordinator coordinator;

	public SimpleCoordinatedAlgorithm(Coordinator coordinator) {
		this.coordinator = coordinator;
	}

	public Decision decide(RobotView controlledRobot, RobotEnvironmentView environmentView) {
		// TODO zaimplementowac
		return new Decision(DecisionType.WAIT);
	}



}
