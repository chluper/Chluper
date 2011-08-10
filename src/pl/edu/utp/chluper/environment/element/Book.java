/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.utp.chluper.environment.element;

/**
 *
 * @author damian
 */
public class Book {

	// maksymalny ISBN
	public static final int MAX_ISBN = 1000;
	// isbn
	private final int isbn;

	/**
	 * Tworzy ksiazke
	 * @param isbn
	 */
	public Book(int isbn) {
		this.isbn = isbn;
	}

	/**
	 *
	 * @return
	 */
	public int getIsbn() {
		return isbn;
	}

	@Override
	public String toString() {
		return "[" + this.getClass().getSimpleName() + "|" + isbn + "]";
	}
}
