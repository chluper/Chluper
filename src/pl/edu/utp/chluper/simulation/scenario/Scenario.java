/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.edu.utp.chluper.simulation.scenario;

import pl.edu.utp.chluper.environment.element.Student;

/**
 * Interfejs sceariusza
 * @author damian
 */
public interface Scenario {

    /**
     * Metoda pobiera kolejnego studenta w kolejnym tiku
     * @return student z zadaniami lub null jesli w tym takcie takiego nie ma
     */
    public Student next();

}
