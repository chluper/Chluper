/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.edu.utp.chluper.algorithm;

import pl.edu.utp.chluper.environment.view.RobotEnvironmentView;
import pl.edu.utp.chluper.simulation.logging.LoggingHandler;

/**
 * Interfejs koordynatora robotow
 * @author damian
 */
public interface Coordinator {

	/**
	 * Metoda wywolywana przed decyzjami robotow
	 * @param environmentView
	 */
	public void coordinate(RobotEnvironmentView environmentView);
        
	/**
     * Ustawianie logera algorytmowi
     * @param logger
     */
    public void setLogger(LoggingHandler logger);

}
