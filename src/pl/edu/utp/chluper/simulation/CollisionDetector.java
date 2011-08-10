/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.edu.utp.chluper.simulation;

import java.util.Collection;
import pl.edu.utp.chluper.environment.element.Element;
import pl.edu.utp.chluper.environment.element.Location;

/**
 * Klasa sluzaca do wykrywania kolizji
 * @author damian
 */
public class CollisionDetector {

	private final boolean[][] map;

	/**
	 * Tworzy wykrywacz kolizji
	 * @param environmentWidth
	 * @param environmentHeigth
	 * @param elements elementy srodowiska ktore maja byc brane pod uwage
	 */
	public CollisionDetector(int environmentWidth, int environmentHeigth, Collection<? extends Element> elements) {
		map = new boolean[environmentWidth][environmentHeigth];
		for (Element element : elements) {
			addElement(element);
		}
	}

	/**
	 * Dodaje elementy - przeszkody do mapy
	 * @param elements
	 */
	public void addElements(Collection<? extends Element> elements) {
		for (Element element : elements) {
			addElement(element);
		}
	}

	/**
	 * Metoda okresla, w wybranym polozeniu dochodzi do kolizji
	 * @param location
	 * @return
	 */
	public boolean check(Location location) {
		return map[location.getX()][location.getY()];
	}

	/**
	 * Oznakowuje polozenie elementu jako kolizyjne
	 * @param element
	 */
	private void addElement(Element element) {
		if (element.getElementType().isCollisional()) {
			map[element.getLocation().getX()][element.getLocation().getY()] = true;
		}
	}

}
