/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.edu.utp.chluper.simulation.scenario;

import pl.edu.utp.chluper.simulation.scenario.Scenario;
import java.util.Random;
import pl.edu.utp.chluper.environment.element.Student;

/**
 * losowy scenariusz pojawiania sie studentow
 * @author damian
 */
public class RandomScenario implements Scenario {

    private static final Random RANDOM = new Random(System.currentTimeMillis());



    public Student next() {
        // TODO zaimplementowac losowe generowanie studentow
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
