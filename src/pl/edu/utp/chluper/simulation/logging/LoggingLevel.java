/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.edu.utp.chluper.simulation.logging;

/**
 * Poziom logowania
 * @author damian
 */
public enum LoggingLevel {

    /**
     * Najnizszy poziom logowania (debug)
     */
    LEVEL1(1),

    /**
     * Sredni poziom logowania (normalny)
     */
    LEVEL2(2),

    /**
     * Najwyzszy poziom logowania (sytuacje krytyczne)
     */
    LEVEL3(3),
    ;

    // poziom logowania
    private final int level;

    private LoggingLevel(int level) {
	this.level = level;
    }

    /**
     * Zwraca wyrazony liczbowo poziom logowania
     * @return
     */
    public int getLevel() {
	return level;
    }



}
