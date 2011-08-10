/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.edu.utp.chluper.simulation.logging;

/**
 * Informacja do logowania
 * @author damian
 */
public class LoggingMessage {

    // poziom logowania
    private final LoggingLevel loggingLevel;
    // grupa zrudla logera
    private final LoggerGroup loggerGroup;
    // id logera
    private final String loggerId;
    // takt symulacji
    private final int simulationTick;
    // komunikat
    private final String message;

    public LoggingMessage(LoggingLevel loggingLevel, LoggerGroup loggerGroup, String loggerId, int simulationTick, String message) {
	this.loggingLevel = loggingLevel;
	this.loggerGroup = loggerGroup;
	this.loggerId = loggerId;
	this.simulationTick = simulationTick;
	this.message = message;
    }

    /**
     * Zwraca grupe loggerow
     * @return
     */
    public LoggerGroup getLoggerGroup() {
	return loggerGroup;
    }

    /**
     * zwraca identyfikator loggera
     * @return
     */
    public String getLoggerId() {
	return loggerId;
    }

    /**
     * zwraca poziom logowania
     * @return
     */
    public LoggingLevel getLoggingLevel() {
	return loggingLevel;
    }

    /**
     * Zwraca komunikat
     * @return
     */
    public String getMessage() {
	return message;
    }

    /**
     * Zwraca takt symulacji logowania
     * @return
     */
    public int getSimulationTick() {
	return simulationTick;
    }

    @Override
    public String toString() {
	return "" + simulationTick + " " + loggingLevel + "|" + loggerGroup + "|" + loggerId + ": " + message;
    }

}
