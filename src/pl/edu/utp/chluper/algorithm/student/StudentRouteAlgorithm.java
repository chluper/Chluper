/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.utp.chluper.algorithm.student;

import java.util.List;
import pl.edu.utp.chluper.algorithm.graph.Graph;
import pl.edu.utp.chluper.environment.view.StudentEnvironmentView;
import pl.edu.utp.chluper.environment.element.Location;
import pl.edu.utp.chluper.environment.element.Student;
import pl.edu.utp.chluper.environment.element.Turn;
import pl.edu.utp.chluper.environment.view.StudentView;
import pl.edu.utp.chluper.simulation.logging.LoggingHandler;

/**
 *
 * @author damian
 */
public class StudentRouteAlgorithm implements StudentAlgorithm {

	// algorytm wewnetrzny
	private final StudentAlgorithm internalAlgorithm;
	// cel do ktorego zmiezamy
	private Location target = null;
	// logger
	private LoggingHandler logger;

	/**
	 * Tworzy algorytm owijacz
	 * @param internalAlgorithm
	 */
	public StudentRouteAlgorithm(StudentAlgorithm internalAlgorithm) {
		this.internalAlgorithm = internalAlgorithm;
	}

	public StudentAlgorithm getInternalAlgorithm() {
		return internalAlgorithm;
	}

	public StudentDecision decide(StudentView controlledStudent, StudentEnvironmentView environmentView) {
		// jesli juz dotarlismy do celu
		if (controlledStudent.getLocation().equals(target)) {
			logger.level1("[StudentRouteAlgorithm] Osiagnieto cel:" + target);
			// dotarlismy
			target = null;
		}
		// jesli nie mamy celu
		if (target == null) {
			// pytamy algorytm wewnetrzny o decyzje
			StudentDecision internalAlgorithmDecision = internalAlgorithm.decide(controlledStudent, environmentView);
			logger.level1("[StudentRouteAlgorithm] Wykonywanie rozkazu: " + internalAlgorithmDecision);
			if (internalAlgorithmDecision.getDecisionType() == StudentDecisionType.GO_TO) {
				// zapis nowego celu
				target = (Location) internalAlgorithmDecision.getArg0();
				logger.level1("[StudentRouteAlgorithm] Obrano nowy cel: " + target);
			} else {
				// podejmowanie innej decyzji
				return internalAlgorithmDecision;
			}
		}
		// dazenie do celu
		// jesli stoimy na celu
		if (controlledStudent.getLocation().equals(target)) {
			logger.level1("[StudentRouteAlgorithm] Osiagneto cel: " + target);
			// to nie robimy nic bo nie ma to sensu
			return new StudentDecision(StudentDecisionType.WAIT, null, null);
		}
		// tworzenie grafu
		Graph graph = new Graph(environmentView.getWidth(), environmentView.getHeight(), controlledStudent.getLocation(), environmentView.getStudentAreaMarkerViews());
		List<Location> path = graph.getPathToLocation(target);
		logger.level1("[StudentRouteAlgorithm] Wyznaczono sciezke: " + path);
		// interesuje nsa nastepny krok wiec
		Turn turn = Turn.getTurn(path.get(0), path.get(1), 1);
		// zwracanie rozkazu
		return new StudentDecision(StudentDecisionType.MOVE, turn, null);
	}

	/**
	 * Ustawianie logera algorytmowi
	 * @param logger
	 */
	public void setLogger(LoggingHandler logger) {
		this.logger = logger;
		internalAlgorithm.setLogger(logger);
	}
}
