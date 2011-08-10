/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.utp.chluper.simulation;

import java.util.Collection;
import java.util.Collections;
import pl.edu.utp.chluper.algorithm.Coordinator;
import pl.edu.utp.chluper.simulation.scenario.Scenario;
import pl.edu.utp.chluper.nvironment.Environment;
import pl.edu.utp.chluper.simulation.logging.LoggerGroup;
import pl.edu.utp.chluper.simulation.logging.LoggingAgent;
import pl.edu.utp.chluper.simulation.logging.LoggingHandler;

/**
 *
 * @author damian
 */
public class Simulation {

	// srodowisko
	private final Environment enviroment;
	// scenariusz
	private final Scenario scenario;
	// koordynatorzy
	private final Collection<Coordinator> coordinators;
	// licznik tykniec
	private int tickCounter = 0;
	// egzekutor
	private final Executor executor;
	// obiekt logowania
	private final LoggingAgent loggingAgent = new LoggingAgent();
	// obiekt logowania dla symulatora
	private final LoggingHandler tickLogger = loggingAgent.createLoggingHandler(LoggerGroup.TICKS, "Simulation");

	/**
	 * Tworzy obiekt symulacji
	 * @param enviroment
	 * @param scenario
	 * @param koordynatorzy algorytmow
	 */
	public Simulation(Environment enviroment, Scenario scenario, Collection<Coordinator> coordinators) {
		this.enviroment = enviroment;
		this.scenario = scenario;
		this.coordinators = coordinators;
		this.executor = new Executor(enviroment, scenario, loggingAgent, coordinators);
		enviroment.setLoggingAgent(loggingAgent);
		int i = 0;
		for (Coordinator coordinator : coordinators) {
			coordinator.setLogger(loggingAgent.createLoggingHandler(LoggerGroup.COORDINATOR, "Coordinator-" + i));
			i++;
		}
		tickLogger.level2("Utworzono obiekt symulacji");
	}

	/**
	 * Tworzy obiekt symulacji
	 * @param enviroment
	 * @param scenario
	 */
	public Simulation(Environment enviroment, Scenario scenario) {
		this(enviroment, scenario, Collections.EMPTY_SET);
	}

	/**
	 * Tworzy obiekt symulacji
	 * @param enviroment
	 * @param scenario
	 * @param
	 */
	public Simulation(Environment enviroment, Scenario scenario, Coordinator coordinator) {
		this(enviroment, scenario, Collections.singleton(coordinator));
	}

	/**
	 * Metoda wywolywana co takt
	 * Wywoluje tez tick() dla srodowiska
	 */
	public void tick() {
		// zwiekszanie licznika
		tickCounter++;
		// zapis cyklu
		loggingAgent.setCurrentSimulationTick(tickCounter);
		tickLogger.level2("Rozpoczynanie taktu: " + tickCounter);
		// takt egzekutora
		executor.tick();
		tickLogger.level1("Konczenie taktu: " + tickCounter);
	}

	/**
	 * Zwraca obiekt statystyk
	 * @return
	 */
	public Statistics getStatistics() {
		return executor.getStatistics();
	}

	/**
	 * Zwraca aktualny licznik taktow
	 * @return
	 */
	public int getTickCounter() {
		return tickCounter;
	}

	/**
	 * Zwraca obiekt przechowyjacy logi
	 * @return
	 */
	public LoggingAgent getLoggingAgent() {
		return loggingAgent;
	}


}
