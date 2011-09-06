/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.utp.chluper.algorithm.concret;

import pl.edu.utp.chluper.algorithm.Decision;
import pl.edu.utp.chluper.algorithm.DecisionType;
import pl.edu.utp.chluper.algorithm.util.AbstractAlgorithm;
import pl.edu.utp.chluper.environment.view.DeskView;
import pl.edu.utp.chluper.environment.view.RobotEnvironmentView;
import pl.edu.utp.chluper.environment.view.RobotView;

/**
 *
 * @author kinga
 */
public class SimpleCoordintatedAlgorithBookSeparation extends AbstractAlgorithm {

    private final SimpleCoordinatorBookSeparation simpleCoordinatorBookSeparation;

    public SimpleCoordintatedAlgorithBookSeparation(SimpleCoordinatorBookSeparation simpleCoordinatorBookSeparation) {
        this.simpleCoordinatorBookSeparation = simpleCoordinatorBookSeparation;
    }

    public Decision decide(RobotView controlledRobot, RobotEnvironmentView environmentView) {
        switch (simpleCoordinatorBookSeparation.taskToDo(controlledRobot.getName())) {
            case WAIT:
                return new Decision(DecisionType.WAIT);
            case DELIVER_TO_BOOKSHELF:
                int bookToReturn = simpleCoordinatorBookSeparation.bookToDo(controlledRobot.getName());
                DeskView deskWithBooksToreturn = environmentView.getDeskViewByNumber(simpleCoordinatorBookSeparation.deskToDo(controlledRobot.getName()));
                if (bookToReturn != -1) {
                    if (controlledRobot.getCache().isEmpty()) {
                        if (deskWithBooksToreturn != null) {
                            for (int i = 0; i < deskWithBooksToreturn.getBooksToReturn().size(); i++) {
                                if (deskWithBooksToreturn.getBooksToReturn().get(i).hashCode() == bookToReturn) {
                                    logger.level2("Pobieranie ksiazki " + deskWithBooksToreturn.getBooksToReturn().get(0) + " z biurka:" + deskWithBooksToreturn.getNumber());
                                    return new Decision(DecisionType.TAKE_FROM_DESK, deskWithBooksToreturn.getNumber(), deskWithBooksToreturn.getBooksToReturn().get(i).getIsbn());
                                } else {
                                    logger.level2("Nieoczekiwany błąd! Nieznany numer książki!");
                                    return new Decision(DecisionType.WAIT);
                                }
                            }
                        } else {
                            logger.level2("Nieoczekiwany błąd! Zły numer biurka!");
                            return new Decision(DecisionType.WAIT);
                        }
                    } else {
                        logger.level2("Dostarczanie ksiazki na pulke:" + controlledRobot.getCache().get(0));
                        return new Decision(DecisionType.DELIVER_TO_BOOKSHELF, controlledRobot.getCache().get(0).getIsbn());
                    }
                } else {
                    logger.level2("Nieoczekiwany błąd! Nieznany numer książki!");
                    return new Decision(DecisionType.WAIT);
                }
            case DELIVER_TO_DESK:
                int bookToDeliver = simpleCoordinatorBookSeparation.bookToDo(controlledRobot.getName());
                DeskView deskWithWishes = environmentView.getDeskViewByNumber(simpleCoordinatorBookSeparation.deskToDo(controlledRobot.getName()));
                if (bookToDeliver != -1) {
                    if (controlledRobot.getCache().isEmpty()) {
                        if (deskWithWishes != null) {
                            for (int i = 0; i < deskWithWishes.getWishList().size(); i++) {
                                if (deskWithWishes.getWishList().get(i).hashCode() == bookToDeliver) {
                                    logger.level2("Pobieranie ksiazki:" + deskWithWishes.getWishList().get(0) + " dla biurka: " + deskWithWishes);
                                    return new Decision(DecisionType.TAKE_FROM_BOOKSHELF, deskWithWishes.getWishList().get(0));
                                } else {
                                    logger.level2("Nieoczekiwany błąd! Nieznany numer książki!");
                                    return new Decision(DecisionType.WAIT);
                                }
                            }
                        } else {
                            logger.level2("Nieoczekiwany błąd! Zły numer biurka!");
                            return new Decision(DecisionType.WAIT);
                        }
                    } else {
                        logger.level2("Dostarczani ksiazki:" + controlledRobot.getCache().get(0) + " do biurka: " + deskWithWishes);
                        return new Decision(DecisionType.DELIVER_TO_DESK, deskWithWishes.getNumber(), controlledRobot.getCache().get(0).getIsbn());
                    }
                } else {
                    logger.level2("Nieoczekiwany błąd! Nieznany numer książki!");
                    return new Decision(DecisionType.WAIT);
                }
            default:
                logger.level2("Nieoczekiwany błąd! Nieznane zadanie!");
                return new Decision(DecisionType.WAIT);
        }
    }
}
