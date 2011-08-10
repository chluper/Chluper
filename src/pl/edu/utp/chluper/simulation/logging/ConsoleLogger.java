/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.utp.chluper.simulation.logging;

/**
 * Obiekt logujacy na konsole
 * @author damian
 */
public class ConsoleLogger implements LoggingListener {

	public void newMessage(LoggingMessage message) {
		System.out.println(message);
	}
}
