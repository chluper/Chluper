/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.edu.utp.chluper.simulation.logging;

/**
 * Interfejs sluchacza zdarzen logowania
 * @author damian
 */
public interface LoggingListener {

    /**
     * Metoda wywolywana kiedy pojawi sie nowa wiadomosc do zalogowania
     * @param message
     */
    public void newMessage(LoggingMessage message);

}
