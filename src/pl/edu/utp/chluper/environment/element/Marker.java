/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.utp.chluper.environment.element;

import pl.edu.utp.chluper.environment.view.ElementView;
import pl.edu.utp.chluper.environment.view.MarkerView;
import pl.edu.utp.chluper.simulation.SimulationException;

/**
 *
 * @author damian
 */
public class Marker implements Element, MarkerView {

	private final Location location;
	// typ
	private final ElementType elementType;

	public Marker(Location location, ElementType elementType) {
		this.location = location;
		this.elementType = elementType;
		if (elementType.isCollisional()) {
			throw new SimulationException("Typ: " + elementType + " nie jest przezroczysty");
		}
	}

	public ElementType getElementType() {
		return elementType;
	}

	public Location getLocation() {
		return location;
	}

	/**
	 * Zwraca ten sam obiekt
	 * @return
	 */
	public MarkerView getElementView() {
		return this;
	}

	@Override
	public String toString() {
		return "[" + this.getClass().getSimpleName() + "|" + elementType + "|" + location + "]";
	}
}
