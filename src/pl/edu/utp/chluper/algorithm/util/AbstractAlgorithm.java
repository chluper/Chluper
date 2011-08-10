/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.utp.chluper.algorithm.util;

import pl.edu.utp.chluper.algorithm.Algorithm;
import pl.edu.utp.chluper.simulation.logging.LoggingHandler;

/**
 * Klasa algorytmu z funkcjonalnoscia ktora moze sie powtarzac
 * @author damian
 */
public abstract class AbstractAlgorithm implements Algorithm {

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
