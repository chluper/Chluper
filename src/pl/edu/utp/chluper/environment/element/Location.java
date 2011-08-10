/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.utp.chluper.environment.element;

/**
 * Połozenie elementu
 * @author damian
 */
public class Location {

	private final int x;
	private final int y;

	public Location(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Zwraca polozenie w poziomie
	 * @return
	 */
	public int getX() {
		return x;
	}

	/**
	 * Zwraca polozenie w pionie
	 * @return
	 */
	public int getY() {
		return y;
	}

	/**
	 * Zwraca polozenie po przesunieciu
	 * @param turn kierunek przesuniecia
	 * @param steps ilosc krokow
	 * @return
	 */
	public Location move(Turn turn, int steps) {
		return new Location(x + turn.getxPart() * steps, y + turn.getyPart() * steps);
	}

	/**
	 * Zwarca kierunek w ktorym trzeba by sie przemieszczac do kolejnej lokalizacji
	 * @param anotherLocation
	 * @param steps ilosc krokow
	 * @return kierunek lub null jesli nie da sie okreslic
	 */
	public Turn turnToLocation(Location anotherLocation, int steps) {
		return Turn.getTurn(this, anotherLocation, steps);
	}

	@Override
	public String toString() {
		return "" + x + ":" + y;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Location other = (Location) obj;
		if (this.x != other.x) {
			return false;
		}
		if (this.y != other.y) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 97 * hash + this.x;
		hash = 97 * hash + this.y;
		return hash;
	}
}
