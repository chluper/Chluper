/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.utp.chluper.environment.element;

import java.util.ArrayList;
import java.util.List;
import pl.edu.utp.chluper.nvironment.*;
import pl.edu.utp.chluper.environment.view.DeskView;
import pl.edu.utp.chluper.environment.view.ElementView;
import pl.edu.utp.chluper.simulation.logging.LoggingHandler;

/**
 *
 * @author damian
 */
public class Desk implements Element {

	// lokalizacja
	private final Location location;
	// obrot
	private final Turn turn;
	// numer polki
	private final int number;
	// lokalizacja postoju robota
	private final Location robotPadLocation;
	// lokalizacja postoju studenta
	private final Location studentPadLocation;
	// ksiazki do oddania z ksiazkami do oddania
	private final List<Book> booksToReturn = new ArrayList<Book>();
	// lista elementow do wypozyczenia
	private final List<Integer> wishList = new ArrayList<Integer>();
	// kieszen z wypozyczonymi ksiazkami
	private final List<Book> booksToLend = new ArrayList<Book>();
	// pojemnosc
	private final int booksLimit;
	// logger
	private LoggingHandler logger;

	/**
	 * Tworzy biorko, od przodu miejsce postoju studenta, od robota
	 * @param location
	 * @param turn
	 * @param number
	 * @param robotPadLocation
	 * @param studentPadLocation
	 */
	public Desk(Location location, Turn turn, int number, int maxSize) {
		this.location = location;
		this.turn = turn;
		this.number = number;
		this.robotPadLocation = location.move(turn, -1);
		this.studentPadLocation = location.move(turn, 1);
		this.booksLimit = maxSize;
	}

	/**
	 * Zwraca zwrot elementu
	 * @return
	 */
	public Turn getTurn() {
		return turn;
	}

	public ElementType getElementType() {
		return ElementType.DESK;
	}

	/**
	 * Zwraca polozenie elementu
	 * @return
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * Zwraca numer biurka
	 * @return
	 */
	public int getNumber() {
		return number;
	}

	/**
	 * Zwraca miejsce postoju robota
	 * @return
	 */
	public Location getRobotPadLocation() {
		return robotPadLocation;
	}

	/**
	 * Zwraca miejsce postoju robota
	 * @return
	 */
	public Location getStudentPadLocation() {
		return studentPadLocation;
	}

	/**
	 * Zwraca liste ksiazeklezocych na biurku do wypozyczenia
	 * @return
	 */
	public List<Book> getBooksToLend() {
		return booksToLend;
	}

	/**
	 * Zwraca liste ksiazek lezocych na biurku do oddania
	 * @return
	 */
	public List<Book> getBooksToReturn() {
		return booksToReturn;
	}

	/**
	 * Zwraca liste zamowionych ksiazek
	 * @return
	 */
	public List<Integer> getWishList() {
		return wishList;
	}

	/**
	 * Zwraca maksymalna ilosc ksiazek ktora moze sie jednoczesnie znajdowac na biurku
	 * @return
	 */
	public int getBooksLimit() {
		return booksLimit;
	}

	/**
	 * Okresla czy biurko jest otwarte, tzn czy obsluguje studentow, jesli nie to trzeba poczekac
	 * @return
	 */
	public boolean isOpened() {
		return booksToLend.size() + booksToReturn.size() < booksLimit;
	}

	/**
	 * Ustawia obiekt logowania
	 * @param logger
	 */
	public void setLogger(LoggingHandler logger) {
		this.logger = logger;
	}

	/**
	 * Robot kladzie ksiazke do wypozyczenia
	 * Jesli ksiazka nie byla zamawiana, to zostaje przelozona na polke do zwrotu
	 * @param bookToLend
	 */
	public void putBookToLend(Book bookToLend) {
		boolean wasOnList = wishList.remove((Integer) bookToLend.getIsbn());
		// w zaleznosci czy bylo na liscie
		if (wasOnList) {
			booksToLend.add(bookToLend);
			logger.level1("Polozono ksiazke do wypozyczenia: " + bookToLend);
		} else {
			booksToReturn.add(bookToLend);
			logger.level1("Polozono ksiazke do oddania: " + bookToLend);
		}

	}

	/**
	 * Student kladzie ksiazke do oddania
	 * @param bookToLend
	 */
	public void putBookToReturn(Book bookToReturn) {
		if (!isOpened()) {
			logger.level3("Proba polozenia ksiazki na zablokowane biurko");
			throw new EnvinronmentException("Polozono ksiazke na zablokowane biurko: " + this);
		}
		logger.level1("Polozono ksiazke do oddania: " + bookToReturn);
		booksToReturn.add(bookToReturn);
	}

	/**
	 * Student zamawia ksiazki
	 * @param bookToLend
	 */
	public void putLendList(List<Integer> wishList) {
		if (!isOpened()) {
			logger.level3("Proba polozenia listy zycen na zablokowane biurko");
			throw new EnvinronmentException("Polozono liste na zablokowane biurko: " + this);
		}
		logger.level1("Polozono liste zyczen: " + wishList);
		this.wishList.addAll(wishList);
	}

	/**
	 * Student bierze ksiazke do wypozyczenia o okreslonym numerze
	 * @param isbn
	 * @return true jesli sie udalo
	 */
	public Book takeBookToLend(int isbn) {
		for (Book book : booksToLend) {
			if (isbn == book.getIsbn()) {
				if (booksToLend.remove(book)) {
					logger.level1("Podniesiono ksiazke do wypozyczeni: " + book);
					return book;
				}
			}
		}
		logger.level2("Proba podniesienia ksiazki do wypozyczenia ktorej nie ma: " + isbn);
		return null;
	}

	/**
	 * Robot bierze ksiazke do oddania o okreslonym numerze
	 * @param isbn
	 * @return wyjeta ksiazka
	 */
	public Book takeBookToReturn(int isbn) {
		for (Book book : booksToReturn) {
			if (isbn == book.getIsbn()) {
				if (booksToReturn.remove(book)) {
					logger.level1("Podniesiono ksiazke do oddania: " + book);
					return book;
				}
			}
		}
		logger.level2("Proba podniesienia ksiazki do oddania ktorej nie ma: " + isbn);
		return null;
	}

	/**
	 * Zwraca ten sam obiekt
	 * @return
	 */
	public DeskView getElementView() {
		final Desk desk = this;

		return new DeskView() {

			// zatrzaskiwanie
			private final List<Book> booksToLendCopy = new ArrayList<Book>(desk.booksToLend);
			private final List<Book> booksToReturnCopy = new ArrayList<Book>(desk.booksToReturn);
			private final List<Integer> wishListCopy = new ArrayList<Integer>(desk.wishList);
			private final boolean opened = desk.isOpened();

			public Turn getTurn() {
				return desk.turn;
			}

			public int getNumber() {
				return desk.number;
			}

			public Location getRobotPadLocation() {
				return desk.robotPadLocation;
			}

			public Location getStudentPadLocation() {
				return desk.studentPadLocation;
			}

			public List<Book> getBooksToLend() {
				return booksToLendCopy;
			}

			public List<Book> getBooksToReturn() {
				return booksToReturnCopy;
			}

			public List<Integer> getWishList() {
				return wishListCopy;
			}

			public boolean isOpened() {
				return opened;
			}

			public ElementType getElementType() {
				return desk.getElementType();
			}

			public Location getLocation() {
				return desk.location;
			}

			public int getBooksLimit() {
				return desk.booksLimit;
			}

			@Override
			public String toString() {
				return "[" + desk.getClass().getSimpleName() + "View|" + desk.number + "|" + desk.location + "|" + desk.turn + "|" + booksToReturnCopy + "|" + wishListCopy + "|" + booksToLendCopy + "|" + desk.booksLimit + "]";
			}
		};
	}

	@Override
	public String toString() {
		return "[" + this.getClass().getSimpleName() + "|" + number + "|" + location + "|" + turn + "|" + booksToReturn + "|" + wishList + "|" + booksToLend + "|" + booksLimit + "]";
	}
}
