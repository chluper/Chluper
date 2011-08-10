/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.utp.chluper.algorithm;

import pl.edu.utp.chluper.environment.view.RobotEnvironmentView;
import pl.edu.utp.chluper.environment.view.RobotView;
import pl.edu.utp.chluper.simulation.logging.LoggingHandler;

/**
 * Interfejs decyzji
 * @author damian
 */
public interface Algorithm {

    /**
     * Metoda prosi o decyzje o tym co robic
     * @param controlledRobot robot wobec ktorego ma byc podjeta decyzja
     * @param environmentView widok srodowiska z punktu widzenia robota
     * @return podjeta przez robota decyzja
     */
    public Decision decide(RobotView controlledRobot, RobotEnvironmentView environmentView);
    
    /**
     * Ustawianie logera algorytmowi
     * @param logger
     */
    public void setLogger(LoggingHandler logger);
}
