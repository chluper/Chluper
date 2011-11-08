/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.utp.chluper;

import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JFrame;
import pl.edu.utp.chluper.algorithm.concret.SimpleCoordinatedAlgorithm;
import pl.edu.utp.chluper.algorithm.concret.SimpleCoordinator;
import pl.edu.utp.chluper.algorithm.util.DeliverAlgorithm;
import pl.edu.utp.chluper.algorithm.util.RouteAlgorithm;
import pl.edu.utp.chluper.nvironment.Environment;
import pl.edu.utp.chluper.environment.element.Robot;
import pl.edu.utp.chluper.gui.EnvironmentPanel;
import pl.edu.utp.chluper.nvironment.util.EnvironmantCreator;
import pl.edu.utp.chluper.simulation.Simulation;
import pl.edu.utp.chluper.simulation.logging.ConsoleLogger;
import pl.edu.utp.chluper.simulation.logging.LoggerGroup;
import pl.edu.utp.chluper.simulation.logging.LoggingLevel;
import pl.edu.utp.chluper.simulation.scenario.FixedRateScenario;
import pl.edu.utp.chluper.simulation.scenario.Scenario;

/**
 *
 * @author damian
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        // tworzenie srodowiska
        final EnvironmantCreator creator = new EnvironmantCreator();
        final Environment environment = creator.createEnviroment(10, 5, 2, 3, 5);

        // koordynator
        //final Coordinator coordinator = new SimpleCoordinator(environment.getRobotEnvironmentView());
        final SimpleCoordinator coordinator = new SimpleCoordinator(environment.getRobotEnvironmentView());

        //final SimpleCoordinatorBookSeparation coordinatorBookSeparation = new SimpleCoordinatorBookSeparation(environment.getRobotEnvironmentView());
        // dodawanie robotow
        //Robot robot = new Robot("A", 1);
        //robot.setAlgorithm(new RouteAlgorithm(new DeliverAlgorithm(new DeliverToDeskAlgorithm(environment.getRobotEnvironmentView()))));
        //environment.putRobot(robot);
//		robot = new Robot("B", 1);
//		robot.setAlgorithm(new RouteAlgorithm(new DeliverAlgorithm(new TakeFromDeskAlgorithm(environment.getRobotEnvironmentView()))));
//		environment.putRobot(robot);                
//        Robot robot = new Robot("A", 1);
//        robot.setAlgorithm(new RouteAlgorithm(new DeliverAlgorithm(new SimpleCoordintatedAlgorithBookSeparation(coordinatorBookSeparation))));
//        environment.putRobot(robot);
		Robot robot = new Robot("A", 3);
		robot.setAlgorithm(new RouteAlgorithm(new DeliverAlgorithm(new SimpleCoordinatedAlgorithm(coordinator))));
		environment.putRobot(robot);
                
                robot = new Robot("B", 3);
		robot.setAlgorithm(new RouteAlgorithm(new DeliverAlgorithm(new SimpleCoordinatedAlgorithm(coordinator))));
		environment.putRobot(robot);
                
                robot = new Robot("C", 3);
		robot.setAlgorithm(new RouteAlgorithm(new DeliverAlgorithm(new SimpleCoordinatedAlgorithm(coordinator))));
		environment.putRobot(robot);


//        robot = new Robot("B", 1);
//        robot.setAlgorithm(new RouteAlgorithm(new DeliverAlgorithm(new SimpleCoordintatedAlgorithBookSeparation(coordinatorBookSeparation))));
//        environment.putRobot(robot);
//        robot = new Robot("C", 2);
//        robot.setAlgorithm(new RouteAlgorithm(new DeliverAlgorithm(new SimpleCoordintatedAlgorithBookSeparation(coordinatorBookSeparation))));
//        environment.putRobot(robot);
//        
//                robot = new Robot("D", 2);
//        robot.setAlgorithm(new RouteAlgorithm(new DeliverAlgorithm(new SimpleCoordintatedAlgorithBookSeparation(coordinatorBookSeparation))));
//        environment.putRobot(robot);
//        
//        robot = new Robot("E", 2);
//        robot.setAlgorithm(new RouteAlgorithm(new DeliverAlgorithm(new SimpleCoordintatedAlgorithBookSeparation(coordinatorBookSeparation))));
//        environment.putRobot(robot);
//        
//                robot = new Robot("F", 2);
//        robot.setAlgorithm(new RouteAlgorithm(new DeliverAlgorithm(new SimpleCoordintatedAlgorithBookSeparation(coordinatorBookSeparation))));
//        environment.putRobot(robot);
//        
//                robot = new Robot("g", 2);
//        robot.setAlgorithm(new RouteAlgorithm(new DeliverAlgorithm(new SimpleCoordintatedAlgorithBookSeparation(coordinatorBookSeparation))));
//        environment.putRobot(robot);



//		robot = new Robot("D", 1);
//		robot.setAlgorithm(new RouteAlgorithm(new DeliverAlgorithm(new SimpleCoordinatedAlgorithmBo(coordinator))));
//		environment.putRobot(robot);

        // tworzenie scenariusza i symulacji
        final Scenario scenario = new FixedRateScenario(5);
//        final Simulation simulation = new Simulation(environment, scenario, coordinatorBookSeparation);
        final Simulation simulation = new Simulation(environment, scenario, coordinator);


        // logger
        final ConsoleLogger cl = new ConsoleLogger();
        //simulation.getLoggingAgent().addListener(cl, LoggingLevel.LEVEL3);
//        simulation.getLoggingAgent().addListener(cl, LoggingLevel.LEVEL1, LoggerGroup.STUDENT);
//        simulation.getLoggingAgent().addListener(cl, LoggingLevel.LEVEL2, LoggerGroup.STUDENT_ALGORITHM);
//        simulation.getLoggingAgent().addListener(cl, LoggingLevel.LEVEL1, LoggerGroup.STUDENT_MANAGER);
//		simulation.getLoggingAgent().addListener(cl, LoggingLevel.LEVEL2, LoggerGroup.EXECUTOR);
//		simulation.getLoggingAgent().addListener(cl, LoggingLevel.LEVEL2, "Robot-A");
//		simulation.getLoggingAgent().addListener(cl, LoggingLevel.LEVEL2, "Algorithm-A");
//                simulation.getLoggingAgent().addListener(cl, LoggingLevel.LEVEL2, "Algorithm-B");
                simulation.getLoggingAgent().addListener(cl, LoggingLevel.LEVEL2, "Algorithm-C");
		simulation.getLoggingAgent().addListener(cl, LoggingLevel.LEVEL2, LoggerGroup.COORDINATOR);
//                //simulation.getLoggingAgent().addListener(cl, LoggingLevel.LEVEL1, LoggerGroup.ALGORITHM);
        //simulation.getLoggingAgent().addListener(cl, LoggingLevel.LEVEL1, LoggerGroup.DESK);

        // tworzenie gui
        final JFrame frame = new JFrame("Simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final EnvironmentPanel ev = new EnvironmentPanel();
        ev.setEnvironment(environment);
        frame.add(ev);
        frame.pack();
        frame.setVisible(true);

        // tworzenie zadania dla timera
        final TimerTask timerTask = new TimerTask() {

            @Override
            public void run() {
                // tykniecie symulatora
                simulation.tick();
                // odrysowanie srodowiska
                ev.repaint();
                // wypisywanie statystyk
            }
        };

        // uruchamianie 
        final Timer timer = new Timer();
        timer.schedule(timerTask, 0, 200);
    }
}
