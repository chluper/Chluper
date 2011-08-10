/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.edu.utp.chluper.environment.view;

import java.util.List;
import pl.edu.utp.chluper.environment.element.Book;
import pl.edu.utp.chluper.environment.element.Location;
import pl.edu.utp.chluper.environment.element.Turn;

/**
 * widok biurka
 * @author damian
 */
public interface DeskView extends ElementView {

	/**
	 * Zwraca zwrot elementu
	 * @return
	 */
	public Turn getTurn();

	/**
	 * Zwraca numer biurka
	 * @return
	 */
	public int getNumber();

	/**
	 * Zwraca miejsce postoju robota
	 * @return
	 */
	public Location getRobotPadLocation();

	/**
	 * Zwraca miejsce postoju robota
	 * @return
	 */
	public Location getStudentPadLocation();

	/**
	 * Zwraca maksymalna ilosc ksiazek ktora moze sie jednoczesnie znajdowac na biurku
	 * @return
	 */
	public int getBooksLimit();

	/**
	 * Zwraca liste ksiazeklezocych na biurku do wypozyczenia
	 * @return
	 */
	public List<Book> getBooksToLend();

	/**
	 * Zwraca liste ksiazek lezocych na biurku do oddania
	 * @return
	 */
	public List<Book> getBooksToReturn();

	/**
	 * Zwraca liste zamowionych ksiazek
	 * @return
	 */
	public List<Integer> getWishList();

	/**
	 * Okresla czy biurko jest otwarte, tzn czy obsluguje studentow, jesli nie to trzeba poczekac
	 * @return
	 */
	public boolean isOpened();

}
