/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.utp.chluper.environment.element;

import pl.edu.utp.chluper.nvironment.EnvinronmentException;
import pl.edu.utp.chluper.environment.view.BookshelfView;
import pl.edu.utp.chluper.environment.view.ElementView;
import pl.edu.utp.chluper.simulation.logging.LoggingHandler;

/**
 *
 * @author damian
 */
public class Bookshelf implements Element, BookshelfView, Comparable<Bookshelf> {

	// lokalizacja
	private final Location location;
	// obrot
	private final Turn turn;
	// numer polki
	private final int number;
	// calkowita ilosc polek
	private final int totalCount;
	// lokalizacja postoju robota
	private final Location robotPadLocation;
	// logger
	private LoggingHandler logger;

	/**
	 * Tworzy polke
	 * @param location
	 * @param turn
	 * @param number numer polki
	 * @param totalCount calkowita liczba polek
	 */
	public Bookshelf(Location location, Turn turn, int number, int totalCount) {
		this.location = location;
		this.turn = turn;
		this.number = number;
		this.totalCount = totalCount;
		this.robotPadLocation = location.move(turn, 1);
	}

	/**
	 * Zwraca zwrot elementu
	 * @return
	 */
	public Turn getTurn() {
		return turn;
	}

	public ElementType getElementType() {
		return ElementType.BOOKSHELF;
	}

	/**
	 * Zwraca polozenie elementu
	 * @return
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * Zwraca numer polki
	 * @return
	 */
	public int getNumber() {
		return number;
	}

	/**
	 * Zwraca calkowita liczbe
	 * @return
	 */
	public int getTotalCount() {
		return totalCount;
	}

	/**
	 * Zwraca polozenie miejsca postoju robota
	 * @return
	 */
	public Location getRobotPadLocation() {
		return robotPadLocation;
	}

	/**
	 * Metoda pobiera ksiazke z polki
	 * @param isbn
	 * @return
	 * @exception SimulationException gdy isbn sie nie zgadza
	 */
	public Book take(int isbn) {
		if (!isValidIsbn(isbn)) {
			logger.level3("Proba pobrania ksiazki o niepasujacym isbn: " + isbn);
			throw new EnvinronmentException("nieprawidlowy isbn: " + isbn + " ksiazki branej z polki: " + toString());
		}
		Book book = new Book(isbn);
		logger.level1("Pobrano ksiazke: " + book);
		return book;
	}

	/**
	 * Metoda do zwracania ksiazki na polke
	 * @param book
	 * @exception SimulationException gdy isbn sie nie zgadza, lub ksiazka nie jest przeznaczona do zwrotu
	 */
	public void put(Book book) {
		if (!isValidIsbn(book.getIsbn())) {
			logger.level3("Proba odlozenia ksiazki o niepasujacym isbn: " + book);
			throw new EnvinronmentException("nieprawidlowy isbn ksiazki: " + book + " odkladanej na polke: " + toString());
		}
		logger.level1("Odlozono ksiazke: " + book);
	}

	/**
	 * Zwraca true jesli isbn jest prawidlowy
	 * @param isbn
	 * @return
	 */
	public boolean isValidIsbn(int isbn) {
		return (isbn % totalCount == number);
	}

	/**
	 * Ustawia obiekt logowania
	 * @param logger
	 */
	public void setLogger(LoggingHandler logger) {
		this.logger = logger;
	}

	/**
	 * Porownuje ksiazki na podstawie ISBN
	 * @param o
	 * @return
	 */
	public int compareTo(Bookshelf o) {
		return o.number - number;
	}

	/**
	 * Zwraca widok elementu
	 * @return
	 */
	public BookshelfView getElementView() {
		final Bookshelf bookshelf = this;
		return new BookshelfView() {

			public Turn getTurn() {
				return bookshelf.turn;
			}

			public int getNumber() {
				return bookshelf.number;
			}

			public int getTotalCount() {
				return bookshelf.totalCount;
			}

			public Location getRobotPadLocation() {
				return bookshelf.robotPadLocation;
			}

			public boolean isValidIsbn(int isbn) {
				return bookshelf.isValidIsbn(isbn);
			}

			public ElementType getElementType() {
				return bookshelf.getElementType();
			}

			public Location getLocation() {
				return bookshelf.location;
			}

			@Override
			public String toString() {
				return "[" + bookshelf.getClass().getSimpleName() + "View|" + bookshelf.number + "/" + bookshelf.totalCount + "|" + bookshelf.location + "|" + bookshelf.turn + "]";
			}
		};
	}

	@Override
	public String toString() {
		return "[" + this.getClass().getSimpleName() + "|" + number + "/" + totalCount + "|" + location + "|" + turn + "]";
	}
}
