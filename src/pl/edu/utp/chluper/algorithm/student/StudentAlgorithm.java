/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.edu.utp.chluper.algorithm.student;

import pl.edu.utp.chluper.environment.view.StudentEnvironmentView;
import pl.edu.utp.chluper.environment.view.StudentView;
import pl.edu.utp.chluper.simulation.logging.LoggingHandler;

/**
 * Interfejs algorytmu studenta
 * @author damian
 */
public interface StudentAlgorithm {

	/**
	 * Metoda prosi o decyzje o tym co robic
	 * @param controlledStudent student wobec ktorego ma byc podjeta decyzja
	 * @param environmentView widok srodowiska z punktu widzenia studenta
	 * @return podjeta przez studenta decyzja
	 */
    public StudentDecision decide(StudentView controlledStudent, StudentEnvironmentView environmentView);

	/**
     * Ustawianie logera algorytmowi
     * @param logger
     */
    public void setLogger(LoggingHandler logger);
}
