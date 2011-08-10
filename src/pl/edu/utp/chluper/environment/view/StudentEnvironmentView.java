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
public interface StudentEnvironmentView extends EnvironmentView {

	/**
	 * Zwraca widoki biurek
	 * @return
	 */
	public Collection<DeskView> getDeskViews();

	/**
	 * Zwraca widoki markerow po ktorych moze poruszac sie student
	 * @return
	 */
	public Collection<MarkerView> getStudentAreaMarkerViews();


	/**
	 * Zwraca studentow znajdujacych sie w srodowisku
	 * @return
	 */
	public Collection<StudentView> getStudentViews();


	/**
	 * Zwraca widoki markerow wejsc
	 * @return
	 */
	public Collection<MarkerView> getStudentEntryMarkerViews();

	/**
	 * Zwraca widoki markerow wyjsc
	 * @return
	 */
	public Collection<MarkerView> getStudentExitMarkerViews();

	/**
	 * Metoda wyluskuje polke na podstawie isbn ksiazki
	 * @param isbn
	 * @return
	 */
	public DeskView getDeskViewByNumber(int number);
}
