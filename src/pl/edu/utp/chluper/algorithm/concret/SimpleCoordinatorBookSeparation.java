/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.utp.chluper.algorithm.concret;

import java.util.HashMap;
import java.util.LinkedList;
import pl.edu.utp.chluper.algorithm.util.AbstractCoordinator;
import pl.edu.utp.chluper.environment.view.DeskView;
import pl.edu.utp.chluper.environment.view.RobotEnvironmentView;
import pl.edu.utp.chluper.environment.view.RobotView;

/**
 *
 * @author kinga
 */
public class SimpleCoordinatorBookSeparation extends AbstractCoordinator {

    private LinkedList<Integer> desks = new LinkedList<Integer>();  //biurka
    private HashMap<String, Enum> robotTaskToDo = new HashMap<String, Enum>(); // zadania robotów
    private HashMap<Integer, String> robotBookToDo = new HashMap<Integer, String>(); //książki dla robotów
    private HashMap<String, Integer> robotDeskToDo = new HashMap<String, Integer>(); //biurko dla robota

    enum RobotDecision {

        DELIVER_TO_DESK,
        DELIVER_TO_BOOKSHELF,
        WAIT;
    }

    public SimpleCoordinatorBookSeparation(RobotEnvironmentView environmentView) {
        //tworzenie listy biurek, które znajdują się w bibliotece
        for (DeskView desk : environmentView.getDeskViews()) {
            desks.addFirst(desk.getNumber());
        }
    }

    public void coordinate(RobotEnvironmentView environmentView) {
        //Tworzenie listy robotów, jakie są dostępne
        if (robotTaskToDo.isEmpty()) {
            for (RobotView robot : environmentView.getRobotViews()) {
                robotTaskToDo.put(robot.getName(), RobotDecision.WAIT);
            }
            logger.level2("Koordynator utworzył listę dostępnych robotów. Robotów czekających na zadanie: " + robotTaskToDo.size());
        }

        //wybieranie biurka do obsługi
        DeskView deskWithWishes = null;
        for (int i = 0; i < desks.size(); i++) {
            int number = nextDeskNumber();
            if (!environmentView.getDeskViewByNumber(number).getBooksToReturn().isEmpty() || !environmentView.getDeskViewByNumber(number).getWishList().isEmpty()) {
                deskWithWishes = environmentView.getDeskViewByNumber(number);
                logger.level1("Koordynator wybrał biurko, które będzie obsługiwane. Numer biurka: " + deskWithWishes.getNumber());
                break;
            }
        }

        //Jeśli jest biurko, które coś zwiera
        if (deskWithWishes != null) {
            //Jeśli jest jakiś robot, który się nudzi
            if (robotTaskToDo.containsValue(RobotDecision.WAIT)) {
                for (String robotFree : robotTaskToDo.keySet()) {
                    //Jeśli to jest właśnie ten robot
                    if (robotTaskToDo.get(robotFree).equals(RobotDecision.WAIT)) {
                        //Jeśli są książki do oddania
                        if (!deskWithWishes.getBooksToReturn().isEmpty() && !robotBookToDo.containsValue(robotFree)) {
                            //Pobieranie książki do zwrotu
                            getBooksToReturn(robotFree, deskWithWishes);
                        }
                        //Jeśli są książki do przyniesienia
                        if (!deskWithWishes.getWishList().isEmpty() && !robotBookToDo.containsValue(robotFree)) {
                            //Pobieranie ksiązki do przyniesienia
                            getBooksFromWishList(robotFree, deskWithWishes);
                        }
                    }
                }
            }

            /*
             * Sprawdzanie czy zadania zostały zakończone: jeśli książki nie ma
             * na biurku albo w kieszonce robota oznacza, że zadanie zostało
             * skończone. Sprawdzanie następuję zgodnie z przydzielonymi 
             * zadaniami: DELIVER_TO_BOOKSHELF i DELIVER_TO_DESK
             */
            for (RobotView robot : environmentView.getRobotViews()) {
                switch (taskToDo(robot.getName())) {
                    case DELIVER_TO_BOOKSHELF:
                        //jeśli są jakieś biurka obsługiwane przez robota
                        if (robotDeskToDo.containsKey(robot.getName())) {
                            //przypisujemy ksiązki, które obsługuje
                            int bookToFinish = finishBookToReturn(robot, environmentView.getDeskViewByNumber(deskToDo(robot.getName())));
                            //Jeśli książka została obslużona, czyli nie ma jej na biurku ani w kieszonce robota
                            if (bookToFinish != -1) {
                                logger.level2("Robot: " + robot + " Zakończył obsługiwanie książki z zadania: " + robotTaskToDo.get(robot.getName()));
                                //Następuje kończenie zadania, tzn usuwanie ksiązki z HashMapty robotBookToDo
                                robotBookToDo.remove(bookToFinish);
                                //Jeśli wszystkie książki, które robot miał obsłużyć są juz usunięte, tzn w robotBookToDo nie ma wartości z nazwą robota
                                if (!robotBookToDo.containsValue(robot.getName())) {
                                    logger.level2("Robot: " + robot + " Zakończył zadanie: " + robotTaskToDo.get(robot.getName()));
                                    //Oznacza, że robot skończył obsługiwać biurko i należy usunąć z listy robotDeskToDo
                                    robotDeskToDo.remove(robot.getName());
                                    //A robot jest juz wolny i ponownie się nudzi.
                                    robotTaskToDo.put(robot.getName(), RobotDecision.WAIT);
                                }
                            }
                        }
                        break;
                    case DELIVER_TO_DESK:
                        //jeśli są jakieś biurka obsługiwane przez robota
                        if (robotDeskToDo.containsKey(robot.getName())) {
                            //przypisujemy ksiązki, które obsługuje
                            int bookToFinish = finishBookFromWishList(robot, environmentView.getDeskViewByNumber(deskToDo(robot.getName())));
                            //Jeśli książka została obslużona, czyli nie ma jej na biurku ani w kieszonce robota
                            if (bookToFinish != -1) {
                                logger.level2("Robot: " + robot + " Zakończył obsługiwanie książki z zadania: " + robotTaskToDo.get(robot.getName()));
                                //Następuje kończenie zadania, tzn usuwanie ksiązki z HashMapty robotBookToDo
                                robotBookToDo.remove(bookToFinish);
                                //Jeśli wszystkie książki, które robot miał obsłużyć są juz usunięte, tzn w robotBookToDo nie ma wartości z nazwą robota
                                if (!robotBookToDo.containsValue(robot.getName())) {
                                    logger.level2("Robot: " + robot + " Zakończył zadanie: " + robotTaskToDo.get(robot.getName()));
                                    //Oznacza, że robot skończył obsługiwać biurko i należy usunąć z listy robotDeskToDo
                                    robotDeskToDo.remove(robot.getName());
                                    //A robot jest juz wolny i ponownie się nudzi.
                                    robotTaskToDo.put(robot.getName(), RobotDecision.WAIT);
                                }
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }

    /**
     * Metoda wybierająca ksiązki booksToReturn, które należy przydzielić do robotów
     * @param robot - nazwa robota
     * @param deskWithWishes - biurko z życzeniami
     * @return 
     */
    private HashMap getBooksToReturn(String robot, DeskView deskWithWishes) {
        for (int i = 0; i < deskWithWishes.getBooksToReturn().size(); i++) {
            //jeśli wybrana książka nie jest przydzielona do żadnego robota
            if (!robotBookToDo.containsKey(deskWithWishes.getBooksToReturn().get(i).hashCode())) {
                //wpisanie ksiązki do robotBookToDo
                robotBookToDo.put(deskWithWishes.getBooksToReturn().get(i).hashCode(), robot);
                //przypisanie biurka do robota 
                robotDeskToDo.put(robot, deskWithWishes.getNumber());
                //zmiana zadania dla robota
                robotTaskToDo.put(robot, RobotDecision.DELIVER_TO_BOOKSHELF);
                logger.level2("Książką do oddania numer " + deskWithWishes.getBooksToReturn().get(i) + " hashCode " + deskWithWishes.getBooksToReturn().get(i).hashCode() + " Zajmie się robot: " + robot);
                break;
            }
        }
        return robotBookToDo;
    }

    /**
     * Metoda wybierająca książki z WihsList, które należy obsłużyć
     * @param robot - widok robota
     * @param deskWithWishes - biurko z życzeniami
     * @return 
     */
    private HashMap getBooksFromWishList(String robot, DeskView deskWithWishes) {
        for (int i = 0; i < deskWithWishes.getWishList().size(); i++) {
            //jeśli wybrana książka nie jest przydzielona do żadnego robota
            if (!robotBookToDo.containsKey(deskWithWishes.getWishList().get(i).hashCode())) {
                //wpisanie ksiązki do robotBookToDo
                robotDeskToDo.put(robot, deskWithWishes.getNumber());
                //przypisanie biurka do robota 
                robotBookToDo.put(deskWithWishes.getWishList().get(i).hashCode(), robot);
                //zmiana zadania dla robota
                robotTaskToDo.put(robot, RobotDecision.DELIVER_TO_DESK);
                logger.level2("Książką do przyniesienia numer " + deskWithWishes.getWishList().get(i) + " hashCode " + deskWithWishes.getWishList().get(i).hashCode() + " Zajmie się robot: " + robot);
                break;
            }
        }
        return robotBookToDo;
    }

    /**
     * Metoda wyznaczająca kolejne biurko do obsługi
     * @return 
     */
    private int nextDeskNumber() {
        int number = desks.removeLast();
        desks.addFirst(number);
        return number;
    }

    /**
     * Metoda odpowiadająca za sprawdzenie czy książka do oddania została już 
     * odniesiona na pułkę i czy robot skończył zadanie.
     * @param robot - widok robota
     * @param desk - widok biurka
     */
    private int finishBookToReturn(RobotView robot, DeskView desk) {
        boolean steelExistOnDesk = false;   //ksiązka jest caly czas na biurku
        boolean steelExistInRobotCache = false; //książka jest cały czas w kieszące robota
        int bookTofinish = bookToDo(robot.getName());
        for (int i = 0; i < desk.getBooksToReturn().size(); i++) {
            if (desk.getBooksToReturn().get(i).hashCode() == bookTofinish) {
                steelExistOnDesk = true;
                break;
            } else {
                steelExistOnDesk = false;
            }
        }
        for (int i = 0; i < robot.getCache().size(); i++) {
            if (robot.getCache().get(i).hashCode() == bookTofinish) {
                steelExistInRobotCache = true;
                break;
            } else {
                steelExistInRobotCache = false;
            }
        }
        if (steelExistInRobotCache == false && steelExistOnDesk == false) {
            return bookTofinish;
        } else {
            return -1;
        }
    }

    /**
     * Metoda odpowiadająca za sprawdzenie czy książka z wishlist została już
     * dostarczona do studenta
     * @param robot
     * @param desk
     * @return 
     */
    private int finishBookFromWishList(RobotView robot, DeskView desk) {
        boolean steelExistOnList = false;
        boolean steelExistInRobotCache = false;
        int bookTofinish = bookToDo(robot.getName());
        for (int i = 0; i < desk.getWishList().size(); i++) {
            if (desk.getWishList().get(i).hashCode() == bookTofinish) {
                steelExistOnList = true;
                break;
            } else {
                steelExistOnList = false;
            }
        }
        for (int i = 0; i < robot.getCache().size(); i++) {
            if (robot.getCache().get(i).hashCode() == bookTofinish) {
                steelExistInRobotCache = true;
                break;
            } else {
                steelExistInRobotCache = false;
            }
        }
        if (steelExistInRobotCache == false && steelExistOnList == false) {
            return bookTofinish;
        } else {
            return -1;
        }
    }

    /**
     * Wybieranie zadania, które jest przypisane do robot
     * @param robot
     * @return 
     */
    public RobotDecision taskToDo(String robot) {
        return (RobotDecision) robotTaskToDo.get(robot);
    }

    /**
     * Wybieranie biurka, które robot ma obsłużyć
     * @param robot
     * @return 
     */
    public int deskToDo(String robot) {
        if (robotDeskToDo.containsKey(robot)) {
            return robotDeskToDo.get(robot);
        } else {
            return -1;
        }
    }

    /**
     * Wybieranie ksiązki, która ma zostac obsłużona
     * @param robot
     * @return 
     */
    public int bookToDo(String robot) {
        if (robotBookToDo.containsValue(robot)) {
            for (int bookToDo : robotBookToDo.keySet()) {
                if (robotBookToDo.get(bookToDo).equals(robot)) {
                    return bookToDo;
                }
            }
            return -1;
        } else {
            return -1;
        }
    }
}
