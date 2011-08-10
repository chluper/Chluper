/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.utp.chluper.environment.element;

import pl.edu.utp.chluper.environment.view.ObstructionView;

/**
 *
 * @author damian
 */
public class Obstruction implements Element, ObstructionView {

	private final Location location;

	/**
	 * Tworzy przeszkode przez ktora nie moze przejsc student ani robot
	 * @param location
	 */
	public Obstruction(Location location) {
		this.location = location;
	}

	public ElementType getElementType() {
		return ElementType.OBSTRUCTION;
	}

	public Location getLocation() {
		return location;
	}

	/**
	 * Zwraca ten sam obiekt
	 * @return
	 */
	public ObstructionView getElementView() {
		return this;
	}

	@Override
	public String toString() {
		return "[" + this.getClass().getSimpleName() + "|" + location + "]";
	}
}
