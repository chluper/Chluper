package pl.edu.utp.chluper;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import pl.edu.utp.chluper.algorithm.concret.SimpleCoordinatedAlgorithm;
import pl.edu.utp.chluper.algorithm.concret.SimpleCoordinator;
import pl.edu.utp.chluper.algorithm.util.DeliverAlgorithm;
import pl.edu.utp.chluper.algorithm.util.RouteAlgorithm;
import pl.edu.utp.chluper.environment.element.Robot;
import pl.edu.utp.chluper.nvironment.Environment;
import pl.edu.utp.chluper.nvironment.util.EnvironmantCreator;
import pl.edu.utp.chluper.simulation.Simulation;
import pl.edu.utp.chluper.simulation.logging.ConsoleLogger;
import pl.edu.utp.chluper.simulation.scenario.FixedRateScenario;
import pl.edu.utp.chluper.simulation.scenario.Scenario;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author kinga
 */
public class SimpleCoordinatorMain {

    /**
     * @param args the command line arguments
     */
        public static void main(String[] args) throws IOException {

        List<Integer> librarySizeList = Arrays.asList(new Integer[]{2, 3, 4});
        List<Integer> desksNumber = Arrays.asList(new Integer[]{1, 2, 3, 4, 5, 6});
        List<Integer> robotsNumber = Arrays.asList(new Integer[]{1, 2, 3});
        List<Integer> cacheLimit = Arrays.asList(new Integer[]{1, 2, 3});
//        List<Class> algorithmList = Arrays.asList(new Class[]{SimpleAlgorithm.class});


        for (int size : librarySizeList) {
            for (int desk : desksNumber) {
                for (int robot : robotsNumber) {
                    for (int cache : cacheLimit) {
                        create(size, desk, robot, cache);
                    }
                }
            }
        }



    }

    public static void create(int librarySize, int desksNumber, int robotsNumber, int cacheLimit) throws IOException {

        final EnvironmantCreator creator = new EnvironmantCreator();
        final Environment environment = creator.createEnviroment(5 * librarySize, 5, librarySize, desksNumber, 5);

        final SimpleCoordinator coordinator = new SimpleCoordinator(environment.getRobotEnvironmentView());

        for (int i = 0; i < robotsNumber; i++) {
            Robot robot = new Robot("R" + i, cacheLimit);
            robot.setAlgorithm(new RouteAlgorithm(new DeliverAlgorithm(new SimpleCoordinatedAlgorithm(coordinator))));
            //robot.setAlgorithm(new RouteAlgorithm(new DeliverAlgorithm(new SimpleAlgorithm())));
            environment.putRobot(robot);
        }

        final Scenario scenario = new FixedRateScenario(5);
        final Simulation simulation = new Simulation(environment, scenario,  coordinator);
        //final Simulation simulation = new Simulation(environment, scenario);

        final ConsoleLogger cl = new ConsoleLogger();

        while (simulation.getStatistics().getStudentNumber() < 1000) {
            simulation.tick();
        }

        String s = "" + librarySize + ";" + desksNumber + ";" + robotsNumber + ";" + cacheLimit + ";" + simulation.getTickCounter() + ";" + simulation.getStatistics().getAverageServeceTime() + ";" + simulation.getStatistics().getAverageWaitingTime();
        System.out.println(s);
        save(SimpleCoordinator.class.getSimpleName(), s);
    }

    public static void save(String fileName, String line) throws IOException {
        FileWriter w = new FileWriter(fileName, true);
        w.write(line + "\n");
        w.flush();
        w.close();
    }
}
