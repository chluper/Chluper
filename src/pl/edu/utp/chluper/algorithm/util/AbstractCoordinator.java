/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.edu.utp.chluper.algorithm.util;

import pl.edu.utp.chluper.algorithm.Coordinator;
import pl.edu.utp.chluper.simulation.logging.LoggingHandler;

/**
 *
 * @author damian
 */
public abstract class AbstractCoordinator implements Coordinator {

	// logger
	protected LoggingHandler logger;

	/**
	 * Ustawia obiekt logowania
	 * @param logger
	 */
	public void setLogger(LoggingHandler logger) {
		this.logger = logger;
	}

}
