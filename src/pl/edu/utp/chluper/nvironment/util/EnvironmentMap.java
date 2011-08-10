/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.utp.chluper.nvironment.util;

import pl.edu.utp.chluper.environment.element.ElementType;
import pl.edu.utp.chluper.environment.element.Location;
import pl.edu.utp.chluper.environment.element.Turn;
import pl.edu.utp.chluper.simulation.SimulationException;

/**
 * Kalsa reprezentujaca mape srodowiska
 * @author damian
 */
public class EnvironmentMap {

    private static final String DEFAULT_MAP = "" +
            "###########\n" +
            "#rrrrrrrrr#\n" +
            "#BrBBrBBrB#\n" +
            "#BrBBrBBrB#\n" +
            "#BrBBrBBrB#\n" +
            "#BrBBrBBrB#\n" +
            "#BrBBrBBrB#\n" +
            "#rrrrRrrrr#\n" +
            "#####D#####\n" +
            "#esssssssE#\n" +
            "###########";
    private final ElementType[][] map;

    /**
     * Metoda wczytuje mape ze stringa
     * <ul>
     * <li> B - polka z ksiazkami
     * <li> D - biorko do wydawania ksiazek
     * <li> # - sciana
     * <li> R - miejsce startowe robota
     * <li> r - starefa dla robota
     * <li> E - wejscie studentow
     * <li> e - wyjscie studentow
     * <li> s - strefa poruszania studentow
     * </ul>
     * @param textMap
     */
    public EnvironmentMap(String textMap) {
        // wyluskiwanie wierszy
        String[] textLines = textMap.split("\n");
        // tworzenie mapy
        map = new ElementType[textLines[0].length()][textLines.length];
        // numer linii i elementow
        int y = 0;
        for (String textLine : textLines) {
            // sprawdzanie ilosci elementow
            if (textLine.length() != textLines[0].length()) {
                throw new SimulationException("Nieprawidlowa ilosc elementow w wierszu: " + textLine);
            }
            int x = 0;
            for (char element : textLine.toCharArray()) {
                switch (element) {
                    case 'B':
                        map[x][y] = ElementType.BOOKSHELF;
                        break;
                    case 'D':
                        map[x][y] = ElementType.DESK;
                        break;
                    case '#':
                        map[x][y] = ElementType.OBSTRUCTION;
                        break;
                    case 'R':
                        map[x][y] = ElementType.ROBOT_START_POINT;
                        break;
                    case 'r':
                        map[x][y] = ElementType.ROBOT_AREA;
                        break;
                    case 'E':
                        map[x][y] = ElementType.STUDENT_ENTRY;
                        break;
                    case 'e':
                        map[x][y] = ElementType.STUDENT_EXIT;
                        break;
                    case 's':
                        map[x][y] = ElementType.STUDENT_AREA;
                        break;
                    default:
                        throw new SimulationException("Nieobslugiwany znak w mapie: " + element);
                }
                x++;
            }
            y++;
        }
    }

    /**
     * Zwraca szerokosc mapy
     * @return
     */
    public int getWidth() {
        return map.length;
    }

    /**
     * Zwraca wysokosc mapy
     * @return
     */
    public int getHeight() {
        return map[0].length;
    }

    /**
     * Zwraca typ elementu w danym polozeniu
     * @param x polozenie w poziomie 
     * @param y polozenie w pionie
     * @return
     */
    public ElementType get(int x, int y) {
        return map[x][y];
    }

    /**
     * Metoda zlicza ilosc elementow danego typu
     * @param elementType
     * @return
     */
    public int getElementCount(ElementType elementType) {
        int counter = 0;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j].equals(elementType)) {
                    counter++;
                }
            }
        }
        return counter;
    }

    /**
     * Metoda generuje domyslna mape
     */
    public EnvironmentMap() {
        this(DEFAULT_MAP);
    }

    /**
     * Metoda zwraca kierunek do sasiada danego typu
     * @param x polozenie odpytywanego elementu
     * @param y polozenie odpytywanego elementu
     * @param neighbourType typ poszukiwanego sasiada
     * @return kierunek do sasiada lub null jesli takiego nie ma
     */
    public Turn getNeighbourDirection(int x, int y, ElementType neighbourType) {
        for (Turn turn : Turn.values()) {
            // ustalanie polozenia sasiada
            int neighbourX = (turn.move(new Location(x, y), 1)).getX();
            int neighbourY = (turn.move(new Location(x, y), 1)).getY();
            // okreslenie czy to miesci sie w tablicy
            if (neighbourX >= 0 && neighbourX < map.length && neighbourY >= 0 && neighbourY < map[0].length) {
                if (neighbourType.equals(map[neighbourX][neighbourY])) {
                    return turn;
                }
            }
        }
        // nie znalazlo sie nic o takim kierunku
        return null;
    }
}
