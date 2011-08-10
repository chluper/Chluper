/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.utp.chluper.nvironment.util;

import java.util.Collection;
import java.util.HashSet;
import pl.edu.utp.chluper.environment.element.Bookshelf;
import pl.edu.utp.chluper.environment.element.Desk;
import pl.edu.utp.chluper.environment.element.Element;
import pl.edu.utp.chluper.environment.element.ElementType;
import pl.edu.utp.chluper.nvironment.Environment;
import pl.edu.utp.chluper.environment.element.Location;
import pl.edu.utp.chluper.environment.element.Marker;
import pl.edu.utp.chluper.environment.element.Obstruction;
import pl.edu.utp.chluper.environment.element.Turn;
import pl.edu.utp.chluper.simulation.SimulationException;

/**
 *
 * @author damian
 */
public class EnvironmantCreator {

    /**
     * Metoda tworzy srodowisko na podstawie mapy
     * Srodowisko nie zawiera robotow-
     * @param environmentMap
     * @return
     */
    public Environment createEnvironment(EnvironmentMap environmentMap, int booksOnDeskLimit) {

        Collection<Element> elements = new HashSet<Element>();
        // okreslenie ilosci pulek z ksiazkami
        final int bookshelfTotalCount = environmentMap.getElementCount(ElementType.BOOKSHELF);
        int bookshelfCounter = 0;
		int deskCounter = 0;

        // przeszukiwanie mapy
        for (int i = 0; i < environmentMap.getWidth(); i++) {
            for (int j = 0; j < environmentMap.getHeight(); j++) {
                // okreslenie polozenia
                Location location = new Location(i, j);
                switch (environmentMap.get(i, j)) {
                    case BOOKSHELF: {

                        // kierunek ustawiania
                        Turn turn = environmentMap.getNeighbourDirection(j, j, ElementType.ROBOT_AREA);
                        if (turn == null) {
                            throw new SimulationException("Element z pozycji: " + i + ":" + j + "nie posiada dostepu do robotow");
                        }
                        // dodawanie elementu
                        elements.add(new Bookshelf(location, turn, bookshelfCounter, bookshelfTotalCount));
                        bookshelfCounter++;
                        break;
                    }
                    case DESK: {

                        // kierunek
                        Turn turn = environmentMap.getNeighbourDirection(j, j, ElementType.ROBOT_AREA);
                        if (turn == null) {
                            throw new SimulationException("Element z pozycji: " + i + ":" + j + "nie posiada dostepu do studentow");
                        }
                        // sprawdzanie czy z tylu jest pad dla robota
                        if (!environmentMap.get(location.move(turn, -2).getX(), location.move(turn, -2).getY()).equals(ElementType.ROBOT_AREA) && !environmentMap.get(location.move(turn, -2).getX(), location.move(turn, -2).getY()).equals(ElementType.ROBOT_START_POINT)) {
                            throw new SimulationException("Element z pozycji: " + i + ":" + j + "nie posiada dostepu do robota w odpowiednim miejscu");
                        }
                        // dodawanie elementu
                        elements.add(new Desk(location, turn, deskCounter, booksOnDeskLimit));
						deskCounter++;
                        break;
                    }
                    case OBSTRUCTION:
                        elements.add(new Obstruction(location));
                        break;
                    case ROBOT_AREA:
                        elements.add(new Marker(location, ElementType.ROBOT_AREA));
                        break;
                    case ROBOT_START_POINT:
                        elements.add(new Marker(location, ElementType.ROBOT_START_POINT));
                        elements.add(new Marker(location, ElementType.ROBOT_AREA));
                        break;
                    case STUDENT_ENTRY:
                        elements.add(new Marker(location, ElementType.STUDENT_ENTRY));
                        elements.add(new Marker(location, ElementType.STUDENT_AREA));
                        break;
                    case STUDENT_EXIT:
                        elements.add(new Marker(location, ElementType.STUDENT_EXIT));
                        elements.add(new Marker(location, ElementType.STUDENT_AREA));
                        break;
                    case STUDENT_AREA:
                        elements.add(new Marker(location, ElementType.STUDENT_AREA));
                        break;
                    default:
                        throw new SimulationException("Niedopuszczalny rodzaj elementu w mapie srodowiska");
                }
            }
        }

        // tworzenie srodowiska
        Environment environment = new Environment(environmentMap.getWidth(), environmentMap.getHeight(), elements);
        return environment;
    }

    /**
     * Metoda tworzy srodowisko na podstawie mapy textowej
     * @param textMap
     */
    public Environment createEnvironment(String textMap, int booksOnDeskLimit) {
        return createEnvironment(new EnvironmentMap(textMap), booksOnDeskLimit);
    }

    /**
     * Metoda tworzy srodowisko o okreslonej budowie
     * @param bookshelfLineNumber ilosc rzedow z ksiazkami
     * @param bookshelfInLine ilosc ksiazek w wierszu
     * @param bookShelfLineSections ilosc sekcji
     * @param deskNumber
	 * @param booksOnDeskLimit maksymalna ilosc ksiazek na biurku
     * @return
     */
    public Environment createEnviroment(int bookshelfLineNumber, int bookshelfInLine, int bookShelfLineSections, int deskNumber, int booksOnDeskLimit) {
        int rowCounter = 0;
        // szerokosc
        final int width = bookshelfLineNumber * 3 + 2;
        // ilosc polek na ksiazki
        final int bookshelfTotalCount = bookshelfLineNumber * bookshelfInLine * bookShelfLineSections * 2;
        int bookshelfCounter = 0;
        Collection<Element> elements = new HashSet<Element>();

        // gorna sciana
        for (int i = 0; i < width; i++) {
            elements.add(new Obstruction(new Location(i, rowCounter)));
        }
        rowCounter++;

        // rysowanie sekcji
        for (int i = 0; i < bookShelfLineSections; i++) {

            // sciezka nad sekcja
            elements.add(new Obstruction(new Location(0, rowCounter)));
            elements.add(new Obstruction(new Location(width - 1, rowCounter)));
            for (int j = 0; j < bookshelfLineNumber * 3; j++) {
                elements.add(new Marker(new Location(j + 1, rowCounter), ElementType.ROBOT_AREA));
            }
            rowCounter++;

            // polki na ksiazki - kolejne wiersze
            for (int j = 0; j < bookshelfInLine; j++) {
                elements.add(new Obstruction(new Location(0, rowCounter)));
                elements.add(new Obstruction(new Location(width - 1, rowCounter)));
                // polki na ksiazki - kawalki rzedow
                for (int k = 0; k < bookshelfLineNumber; k++) {
                    // dodawanie polek
                    elements.add(new Bookshelf(new Location(k * 3 + 1, rowCounter), Turn.EAST, bookshelfCounter, bookshelfTotalCount));
                    bookshelfCounter++;
                    elements.add(new Marker(new Location(k * 3 + 2, rowCounter), ElementType.ROBOT_AREA));
                    elements.add(new Bookshelf(new Location(k * 3 + 3, rowCounter), Turn.WEST, bookshelfCounter, bookshelfTotalCount));
                    bookshelfCounter++;
                }
                rowCounter++;
            }
        }

        // sciezka nad sekcja
        elements.add(new Obstruction(new Location(0, rowCounter)));
        elements.add(new Obstruction(new Location(width - 1, rowCounter)));
        for (int i = 0; i < bookshelfLineNumber * 3; i++) {
            elements.add(new Marker(new Location(i + 1, rowCounter), ElementType.ROBOT_AREA));
        }
        // punkt startowy
        elements.add(new Marker(new Location(width/2, rowCounter), ElementType.ROBOT_START_POINT));
        rowCounter++;

        // miejsca na lady
        elements.add(new Obstruction(new Location(0, rowCounter)));
        elements.add(new Obstruction(new Location(width - 1, rowCounter)));
        // dystans miedzy biurkami
        final int distBetweenDesks = ((bookshelfLineNumber * 3) / (deskNumber)) - 1;
        // ilosc biurek do postawienia
        int desksLeft = deskNumber;
        // odleglosc od ostatniego
        int lastDeskLength = distBetweenDesks / 2;
        for (int i = 0; i < bookshelfLineNumber * 3; i++) {
            // sprawdzanie czy tu powinna byc lada
            if (lastDeskLength >= distBetweenDesks && desksLeft > 0) {
                elements.add(new Desk(new Location(i + 1, rowCounter), Turn.SOUTH, deskNumber - desksLeft, booksOnDeskLimit));
                desksLeft--;
                lastDeskLength = 0;
            } else {
                elements.add(new Obstruction(new Location(i + 1, rowCounter)));
                lastDeskLength++;
            }
        }
        rowCounter++;

		// miejsce przy biurkach
        elements.add(new Obstruction(new Location(0, rowCounter)));
        elements.add(new Obstruction(new Location(width - 1, rowCounter)));
        for (int i = 0; i < bookshelfLineNumber * 3; i++) {
            elements.add(new Marker(new Location(i + 1, rowCounter), ElementType.STUDENT_AREA));
        }
        rowCounter++;

        // wejscie i wyjscie
        elements.add(new Obstruction(new Location(0, rowCounter)));
        elements.add(new Obstruction(new Location(width - 1, rowCounter)));
        for (int i = 0; i < bookshelfLineNumber * 3; i++) {
            elements.add(new Marker(new Location(i + 1, rowCounter), ElementType.STUDENT_AREA));
        }
        elements.add(new Marker(new Location(1, rowCounter), ElementType.STUDENT_ENTRY));
        elements.add(new Marker(new Location(width - 2, rowCounter), ElementType.STUDENT_EXIT));
        rowCounter++;

        // dolan sciana
        for (int i = 0; i < width; i++) {
            elements.add(new Obstruction(new Location(i, rowCounter)));
        }
        rowCounter++;

        return new Environment(width, rowCounter, elements);
    }
}
