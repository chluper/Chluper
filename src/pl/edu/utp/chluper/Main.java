/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.edu.utp.chluper;

import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JFrame;
import pl.edu.utp.chluper.algorithm.Coordinator;
import pl.edu.utp.chluper.algorithm.concret.DeliverToDeskAlgorithm;
import pl.edu.utp.chluper.algorithm.concret.SimpleCoordinatedAlgorithm;
import pl.edu.utp.chluper.algorithm.concret.SimpleCoordinator;
import pl.edu.utp.chluper.algorithm.concret.TakeFromDeskAlgorithm;
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
        final Environment environment = creator.createEnviroment(8, 5, 2, 5, 5);

		// koordynator
		//final Coordinator coordinator = new SimpleCoordinator(environment.getRobotEnvironmentView());
                final SimpleCoordinator coordinator = new SimpleCoordinator(environment.getRobotEnvironmentView());
		// dodawanie robotow
		//Robot robot = new Robot("A", 1);
		//robot.setAlgorithm(new RouteAlgorithm(new DeliverAlgorithm(new DeliverToDeskAlgorithm(environment.getRobotEnvironmentView()))));
		//environment.putRobot(robot);
//		robot = new Robot("B", 1);
//		robot.setAlgorithm(new RouteAlgorithm(new DeliverAlgorithm(new TakeFromDeskAlgorithm(environment.getRobotEnvironmentView()))));
//		environment.putRobot(robot);
		Robot robot = new Robot("A", 1);
		robot.setAlgorithm(new RouteAlgorithm(new DeliverAlgorithm(new SimpleCoordinatedAlgorithm(coordinator))));
		environment.putRobot(robot);                
		robot = new Robot("C", 1);
		robot.setAlgorithm(new RouteAlgorithm(new DeliverAlgorithm(new SimpleCoordinatedAlgorithm(coordinator))));
		environment.putRobot(robot);
		robot = new Robot("D", 1);
		robot.setAlgorithm(new RouteAlgorithm(new DeliverAlgorithm(new SimpleCoordinatedAlgorithm(coordinator))));
		environment.putRobot(robot);

		// tworzenie scenariusza i symulacji
		final Scenario scenario = new FixedRateScenario(5);
		final Simulation simulation = new Simulation(environment, scenario, coordinator);

		// logger
		final ConsoleLogger cl = new ConsoleLogger();
		//simulation.getLoggingAgent().addListener(cl, LoggingLevel.LEVEL3);
//		simulation.getLoggingAgent().addListener(cl, LoggingLevel.LEVEL1, LoggerGroup.STUDENT);
//		simulation.getLoggingAgent().addListener(cl, LoggingLevel.LEVEL2, LoggerGroup.STUDENT_ALGORITHM);
//		simulation.getLoggingAgent().addListener(cl, LoggingLevel.LEVEL1, LoggerGroup.STUDENT_MANAGER);
//		simulation.getLoggingAgent().addListener(cl, LoggingLevel.LEVEL2, LoggerGroup.EXECUTOR);
//		simulation.getLoggingAgent().addListener(cl, LoggingLevel.LEVEL2, "Robot-A");
		simulation.getLoggingAgent().addListener(cl, LoggingLevel.LEVEL2, "Algorithm-C");
		simulation.getLoggingAgent().addListener(cl, LoggingLevel.LEVEL3, LoggerGroup.COORDINATOR);
                //simulation.getLoggingAgent().addListener(cl, LoggingLevel.LEVEL1, LoggerGroup.ROBOT);
		simulation.getLoggingAgent().addListener(cl, LoggingLevel.LEVEL1, LoggerGroup.DESK);

		// tworzenie gui
        final JFrame frame = new JFrame("Simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final EnvironmentPanel ev = new EnvironmentPanel();
        ev.setEnvironment(environment);
        frame.add(ev);
        frame.pack();
        frame.setVisible(true);

//		// przeskok taktow
//		for (int i = 0; i < 170; i++) {
//			simulation.tick();
//		}

		// tworzenie zadania dla timera
		final TimerTask timerTask = new TimerTask() {

			@Override
			public void run() {
				// tykniecie symulatora
				simulation.tick();
				// odrysowanie srodowiska
				ev.repaint();
				// wypisywanie statystyk
//				System.out.println(simulation.getTickCounter() + " : " + simulation.getStatistics());
			}
		};

		// uruchamianie 
		final Timer timer = new Timer();
		timer.schedule(timerTask, 0, 200);

    }

}
