/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.edu.utp.chluper.environment.view;

import pl.edu.utp.chluper.environment.element.Location;
import pl.edu.utp.chluper.environment.element.Turn;

/**
 *
 * @author damian
 */
public interface BookshelfView extends ElementView {

	/**
	 * Zwraca zwrot elementu
	 * @return
	 */
	public Turn getTurn();

	/**
	 * Zwraca numer polki
	 * @return
	 */
	public int getNumber();

	/**
	 * Zwraca calkowita liczbe
	 * @return
	 */
	public int getTotalCount();

	/**
	 * Zwraca polozenie miejsca postoju robota
	 * @return
	 */
	public Location getRobotPadLocation();

	/**
	 * Zwraca true jesli isbn jest prawidlowy
	 * @param isbn
	 * @return
	 */
	public boolean isValidIsbn(int isbn);

}
