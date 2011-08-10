/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.utp.chluper.environment.element;

/**
 *
 * @author damian
 */
public enum Turn {

	EAST(1, 0),
	WEST(-1, 0),
	NORTH(0, -1),
	SOUTH(0, 1),;
	// skladowa x-owa
	private final int xPart;
	// skladowa y-owa
	private final int yPart;

	private Turn(int xPart, int yPart) {
		this.xPart = xPart;
		this.yPart = yPart;
	}

	/**
	 * Zwraca skladowa przesuniecia w x
	 * @return
	 */
	public int getxPart() {
		return xPart;
	}

	/**
	 * Zwraca skladowa przesuniecia w y
	 * @return
	 */
	public int getyPart() {
		return yPart;
	}

	/**
	 * Przesuwa o zadana ilosc krokow w danym kierunku
	 * @param location
	 * @param steps
	 * @return
	 */
	public Location move(Location location, int steps) {
		return new Location(location.getX() + xPart * steps, location.getY() + yPart * steps);
	}

	/**
	 * Zwraca kierunak w ktorym trzeba by sie przemieszczac z jednej lokalizacji do drugiej
	 * @param from lokalizacja poczatkowa
	 * @param to lokalizacja koncowa
	 * @param steps o ile krokow trzeba by sie przemieszczac
	 * @return kierunek, jesli mozna to okreslic lub null
	 */
	public static Turn getTurn(Location from, Location to, int steps) {
		for (Turn turn : values()) {
			if (to.equals(turn.move(from, steps))) {
				return turn;
			}
		}
		return null;
	}
}
