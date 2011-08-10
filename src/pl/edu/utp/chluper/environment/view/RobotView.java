/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.edu.utp.chluper.environment.view;

import java.util.List;
import pl.edu.utp.chluper.environment.element.Book;

/**
 * Widok robota
 * @author damian
 */
public interface RobotView extends ElementView {

	/**
	 * Zwraca ilosc ksiazek ktore moze jednoczesnie transportowac robot
	 * @return
	 */
	public int getCacheLimit();

	/**
	 * Zawartosc kieszeni robota
	 * @return
	 */
	public List<Book> getCache();

	/**
	 * Zwraca nazwe robota
	 * @return
	 */
	public String getName();

}
