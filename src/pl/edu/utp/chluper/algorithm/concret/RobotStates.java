/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.utp.chluper.algorithm.concret;

import java.util.HashMap;

/**
 *
 * @author kinga
 */
public class RobotStates {

    private HashMap<String, Enum> robotState = new HashMap<String, Enum>(); // stan Robota

    public enum RobotState {

        WAITING_FOR_TASK,
        TAKE,
        GO,
        READY_TO_DO_TASKS;
    }

    public HashMap<String, Enum> getRobotState() {
        return robotState;
    }

    /**
     * Ustawia aktualny stan robota
     * @param robotName
     * @param robotTask
     * @return 
     */
    public HashMap<String, Enum> setRobotState(String robotName, RobotState robotTask) {
        robotState.put(robotName, robotTask);
        return robotState;
    }

    /**
     * Pobiera aktualny stan robota
     * @param robotName
     * @return 
     */
    public RobotState getRobotState(String robotName) {
        return (RobotState) robotState.get(robotName);
    }
}
