/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.utp.chluper.environment.view;

import java.util.Collection;

/**
 *
 * @author damian
 */
public interface RobotEnvironmentView extends EnvironmentView {

	/**
	 * Zwraca widoki polek
	 * @return
	 */
	public Collection<BookshelfView> getBookshelfViews();

	/**
	 * Zwraca widoki biurek
	 * @return
	 */
	public Collection<DeskView> getDeskViews();

	/**
	 * Zwraca widoki markerow po ktorych moze poruszac sie robot
	 * @return
	 */
	public Collection<MarkerView> getRobotAreaMarkerViews();

	/**
	 * Zwraca widoki robotow
	 * @return
	 */
	public Collection<RobotView> getRobotViews();

	/**
	 * Metoda wyluskuje polke na podstawie isbn ksiazki
	 * @param isbn
	 * @return
	 */
	public BookshelfView getBookshelfViewByIsbn(int isbn);

	/**
	 * Metoda wyluskuje polke na podstawie isbn ksiazki
	 * @param isbn
	 * @return
	 */
	public DeskView getDeskViewByNumber(int number);

}
