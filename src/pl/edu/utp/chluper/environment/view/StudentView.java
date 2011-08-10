/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.edu.utp.chluper.environment.view;

import java.util.List;
import pl.edu.utp.chluper.environment.element.Book;
import pl.edu.utp.chluper.environment.element.Student.StudentType;

/**
 *
 * @author damian
 */
public interface StudentView extends ElementView {

	/**
	 * Zwraca typ studenta
	 * @return
	 */
	public StudentType getStudentType();

	/**
	 * Zwraca numer biurka do ktorego idzie student
	 * @return
	 */
	public int getTargetDeskNumber();

	/**
	 * Zwraca liste ksiazek do oddania
	 * @return
	 */
	public List<Book> getBooksToReturn();

	/**
	 * Zwraca liste wypozyczonych ksiazek
	 * @return
	 */
	public List<Book> getLendBooks();

	/**
	 * Zwraca liste numerow ksaizek do wypozyczenia
	 * @return
	 */
	public List<Integer> getWishList();

	/**
	 * Numer studenta
	 * @return
	 */
	public int getNumber();

	/**
	 * metoda okresla czy student jest usatysfakcjonowany
	 * tzn oddal i wypozyczyl wszystko co chcial
	 */
	public boolean isSatisfied();
}
