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

	public SimpleCoordinatedAlgorithm(SimpleCoordinator coordinator) {
		this.coordinator = coordinator;
	}

	public Decision decide(RobotView controlledRobot, RobotEnvironmentView environmentView) {
            
            SimpleCoordinatorDecision decyzja = coordinator.getSimpleCoordinatorDecision(controlledRobot);

    
            switch(decyzja.getDecisionType()){
                case DELIVER_TO_DESK: {
                    if(controlledRobot.getCache().isEmpty() && environmentView.getDeskViewByNumber((Integer)decyzja.getArg0()).getWishList().isEmpty())
                        return new Decision(DecisionType.WAIT);
                        if(controlledRobot.getCache().isEmpty()){
                       
                        DeskView biurko = environmentView.getDeskViewByNumber((Integer)decyzja.getArg0());
                        return new Decision(DecisionType.TAKE_FROM_BOOKSHELF, biurko.getWishList().get(0));
                    }else{
                        DeskView biurko = environmentView.getDeskViewByNumber((Integer)decyzja.getArg0());
                        return new Decision(DecisionType.DELIVER_TO_DESK, biurko.getNumber(), controlledRobot.getCache().get(0).getIsbn());
                    }
                        
                  //  logger.level2("koordynator: dostarczyc ksiazke do biurka");
                  //  return new Decision(DecisionType.WAIT);
                }
                    
                case WAIT:{
                    logger.level2("koordynator: czekaj");
                    return new Decision(DecisionType.WAIT);
                    
                }
                case TAKE_FROM_DESK: {
                    logger.level2("koordynator: wez z biruka");
                    if(controlledRobot.getCache().isEmpty()){
                        DeskView biurko = environmentView.getDeskViewByNumber((Integer)decyzja.getArg0());
                       logger.level2("Pobieranie ksiazki " + biurko.getBooksToReturn().get(0) + " z biurka:" + biurko );
				return new Decision(DecisionType.TAKE_FROM_DESK, biurko.getNumber(), biurko.getBooksToReturn().get(0).getIsbn());
                    } 
                    else{
                        logger.level2("Dostarczanie ksiazki na pulke:" + controlledRobot.getCache().get(0));
                        
                        return new Decision(DecisionType.DELIVER_TO_BOOKSHELF, controlledRobot.getCache().get(0).getIsbn());
                    }

                }
                default : {
                    logger.level2("chuj wie...");
                    break;
                }
                    
            }
            return new Decision(DecisionType.WAIT);
        }
            //    return new Decision(DecisionType.DELIVER_TO_BOOKSHELF, controlledRobot.getCache().get(0).getIsbn());

	}




