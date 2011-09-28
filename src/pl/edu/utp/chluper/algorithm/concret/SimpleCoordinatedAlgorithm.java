/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.utp.chluper.algorithm.concret;

import java.util.LinkedList;
import pl.edu.utp.chluper.algorithm.Coordinator;
import pl.edu.utp.chluper.algorithm.Decision;
import pl.edu.utp.chluper.algorithm.DecisionType;
import pl.edu.utp.chluper.algorithm.util.AbstractAlgorithm;
import pl.edu.utp.chluper.environment.view.RobotEnvironmentView;
import pl.edu.utp.chluper.environment.view.RobotView;
import pl.edu.utp.chluper.algorithm.concret.SimpleCoordinatorDecisionType;
import pl.edu.utp.chluper.algorithm.concret.SimpleCoordinatorDecision;
import pl.edu.utp.chluper.algorithm.graph.Graph;
import pl.edu.utp.chluper.environment.view.DeskView;
import pl.edu.utp.chluper.environment.element.Book;
import pl.edu.utp.chluper.environment.element.Location;

/**
 *
 * @author damian
 */
public class SimpleCoordinatedAlgorithm extends AbstractAlgorithm {

    private final SimpleCoordinator coordinator;
    private DecisionType doing = DecisionType.WAIT;
    LinkedList<Book> books=null;

    public SimpleCoordinatedAlgorithm(SimpleCoordinator coordinator) {
        this.coordinator = coordinator;
    }

    public Decision decide(RobotView controlledRobot, RobotEnvironmentView environmentView) {

        SimpleCoordinatorDecision decyzja = coordinator.getSimpleCoordinatorDecision(controlledRobot);
        
        if(books==null && decyzja.getDecisionType()!=SimpleCoordinatorDecisionType.WAIT){
            books=new LinkedList<Book>();
        for(Book book : decyzja.getArg1()){
            books.add(book);
        }
        }

        switch (decyzja.getDecisionType()) {
            case DELIVER_TO_DESK: {

                if (doing == DecisionType.WAIT) {
                    
                    DeskView biurko = environmentView.getDeskViewByNumber(decyzja.getArg0());
                    
                    int position = 0;
                    
                    //wyszukiwanie najkrotszej drogi
                    if(books.size()>=2){
                        
                    Location l1 = controlledRobot.getLocation();
                    Graph g = new Graph(environmentView.getWidth(), environmentView.getHeight(), l1, environmentView.getRobotAreaMarkerViews());
                    Location l2 = environmentView.getBookshelfViewByIsbn(books.get(position).getIsbn()).getRobotPadLocation();
                    int min = g.getDistToLocation(l2);
                   // Book vbook = books.getFirst();
               //     int position = 0;
                        for(int i=1; i<books.size();i++){
                        l2 = environmentView.getBookshelfViewByIsbn(books.get(i).getIsbn()).getRobotPadLocation();
                        if(g.getDistToLocation(l2)<min){
                            System.out.println("ZNALEZIONO MINIMUM!");
                            min=g.getDistToLocation(l2);
                            position = i;
                            
                        }
                            
                        }
                    }
                    
                    //funkcja? get te nearest?
                  //  Book book = books.removeLast();
                    Book book = books.remove(position);
                    
                    if(books.isEmpty())
                        //w tym momencie koncza sie ksiazki do wziecia z polki
                    doing = DecisionType.TAKE_FROM_BOOKSHELF;
                    
                    return new Decision(DecisionType.TAKE_FROM_BOOKSHELF, book.getIsbn());
                }
                if (doing == DecisionType.TAKE_FROM_BOOKSHELF) {
                     
                    DeskView biurko = environmentView.getDeskViewByNumber(decyzja.getArg0());
                    Book book = controlledRobot.getCache().remove(0);
                    
                    if(controlledRobot.getCache().isEmpty())
                    doing = DecisionType.DELIVER_TO_DESK;
                    
                    return new Decision(DecisionType.DELIVER_TO_DESK, biurko.getNumber(), book.getIsbn());
                }
                if (doing == DecisionType.DELIVER_TO_DESK) {
                    coordinator.isFree(controlledRobot.getName());
                    books=null;
                    doing = DecisionType.WAIT;
                    return new Decision(DecisionType.WAIT);
                }

                break;
            }

            case WAIT: {
                doing = DecisionType.WAIT;
                return new Decision(DecisionType.WAIT);

            }
            case TAKE_FROM_DESK: {

                if (doing == DecisionType.WAIT) {
                    
                    DeskView biurko = environmentView.getDeskViewByNumber(decyzja.getArg0());
                    Book book = books.removeFirst();
                    
                    if(books.isEmpty())
                    doing = DecisionType.TAKE_FROM_DESK;
                                                                                        //biurko.getBooksToReturn().get(0).getIsbn()
                    return new Decision(DecisionType.TAKE_FROM_DESK, biurko.getNumber(), book.getIsbn());
                    //tutaj bedzie pobierany w drugim argumencie (1) numer isbn ksiazki badz lista
                }


                if (doing == DecisionType.TAKE_FROM_DESK) {
                    
                    Book book = controlledRobot.getCache().get(0);
                                        
                    if(controlledRobot.getCache().size()==1)
                    doing = DecisionType.DELIVER_TO_BOOKSHELF;
                    
                                                                        //controlledRobot.getCache().get(0).getIsbn()
                    return new Decision(DecisionType.DELIVER_TO_BOOKSHELF, book.getIsbn());
                }

                if (doing == DecisionType.DELIVER_TO_BOOKSHELF) {
                    doing = DecisionType.WAIT;
                    books=null;
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
