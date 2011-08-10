/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.edu.utp.chluper.simulation.logging;

/**
 * Interfejs odpoweidzialny za przyjmowanie logow z dzialania symulatora
 * @author damian
 */
public interface LoggingHandler {
    /**
     * Logowanie wiadomosci najwazniejszych
     * @param message
     */
    public void level1(String message);

    /**
     * Logowanie wiadomosci srednio istotnych
     * @param message
     */
    public void level2(String message);

    /**
     * Logowanie wiadomosci nieistotnych
     * @param message
     */
    public void level3(String message);
}
