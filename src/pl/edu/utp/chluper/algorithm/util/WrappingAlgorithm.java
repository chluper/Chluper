/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.utp.chluper.algorithm.util;

import pl.edu.utp.chluper.algorithm.Algorithm;
import pl.edu.utp.chluper.simulation.logging.LoggingHandler;

/**
 * Klasa abstrakcyjna algorytmow opakowujacych, sluza one do wykonywania tylko okreslonych operacji
 * @author damian
 */
public abstract class WrappingAlgorithm extends AbstractAlgorithm {

	// algorytm wewnetrzny
	protected final Algorithm internalAlgorithm;

	/**
	 * Konstruktor przyjmuje algorytm wewnetrzny
	 * @param internalAlgorithm
	 */
	public WrappingAlgorithm(Algorithm internalAlgorithm) {
		this.internalAlgorithm = internalAlgorithm;
	}

	/**
	 * Metoda zwraca algorytm wewnetrzny
	 * @return
	 */
	public Algorithm getInternalAlgorithm() {
		return internalAlgorithm;
	}

	/**
	 * Ustawia obiekt logowania
	 * @param logger
	 */
	@Override
	public void setLogger(LoggingHandler logger) {
		this.logger = logger;
		internalAlgorithm.setLogger(logger);
	}
}
