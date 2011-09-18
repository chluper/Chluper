/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.utp.chluper.algorithm.concret;

import pl.edu.utp.chluper.algorithm.Coordinator;
import pl.edu.utp.chluper.algorithm.Decision;
import pl.edu.utp.chluper.algorithm.DecisionType;
import pl.edu.utp.chluper.algorithm.util.AbstractAlgorithm;
import pl.edu.utp.chluper.environment.view.RobotEnvironmentView;
import pl.edu.utp.chluper.environment.view.RobotView;
import pl.edu.utp.chluper.algorithm.concret.SimpleCoordinatorDecisionType;
import pl.edu.utp.chluper.algorithm.concret.SimpleCoordinatorDecision;
import pl.edu.utp.chluper.environment.view.DeskView;

/**
 *
 * @author damian
 */
public class SimpleCoordinatedAlgorithm extends AbstractAlgorithm {

    private final SimpleCoordinator coordinator;
    private DecisionType doing = DecisionType.WAIT;

    public SimpleCoordinatedAlgorithm(SimpleCoordinator coordinator) {
        this.coordinator = coordinator;
    }

    public Decision decide(RobotView controlledRobot, RobotEnvironmentView environmentView) {

        SimpleCoordinatorDecision decyzja = coordinator.getSimpleCoordinatorDecision(controlledRobot);


        switch (decyzja.getDecisionType()) {
            case DELIVER_TO_DESK: {


                if (doing == DecisionType.WAIT) {
                    doing = DecisionType.TAKE_FROM_BOOKSHELF;

                    DeskView biurko = environmentView.getDeskViewByNumber(decyzja.getArg0());
                    return new Decision(DecisionType.TAKE_FROM_BOOKSHELF, decyzja.getArg1().getIsbn());
                }
                if (doing == DecisionType.TAKE_FROM_BOOKSHELF) {
                    doing = DecisionType.DELIVER_TO_DESK;
                    DeskView biurko = environmentView.getDeskViewByNumber(decyzja.getArg0());
                    return new Decision(DecisionType.DELIVER_TO_DESK, biurko.getNumber(), decyzja.getArg1().getIsbn());
                }
                if (doing == DecisionType.DELIVER_TO_DESK) {
                    coordinator.isFree(controlledRobot.getName());

                    doing = DecisionType.WAIT;
                    return new Decision(DecisionType.WAIT);
                }

                break;
            }

            case WAIT: {
                logger.level2("koordynator: czekaj");
                doing = DecisionType.WAIT;
                return new Decision(DecisionType.WAIT);

            }
            case TAKE_FROM_DESK: {

                if (doing == DecisionType.WAIT) {
                    DeskView biurko = environmentView.getDeskViewByNumber(decyzja.getArg0());
                    doing = DecisionType.TAKE_FROM_DESK;                                  //biurko.getBooksToReturn().get(0).getIsbn()
                    return new Decision(DecisionType.TAKE_FROM_DESK, biurko.getNumber(), decyzja.getArg1().getIsbn());
                    //tutaj bedzie pobierany w drugim argumencie (1) numer isbn ksiazki badz lista
                }


                if (doing == DecisionType.TAKE_FROM_DESK) {
                    doing = DecisionType.DELIVER_TO_BOOKSHELF;                //controlledRobot.getCache().get(0).getIsbn()
                    return new Decision(DecisionType.DELIVER_TO_BOOKSHELF, decyzja.getArg1().getIsbn());
                }

                if (doing == DecisionType.DELIVER_TO_BOOKSHELF) {
                    doing = DecisionType.WAIT;
                    coordinator.isFree(controlledRobot.getName());
                    return new Decision(DecisionType.WAIT);

                }

                break;
            }
            default: {
                logger.level2("chyba to nie powinno wystapic");
                doing = DecisionType.WAIT;
                break;
            }

        }
        return new Decision(DecisionType.WAIT);
    }
}
