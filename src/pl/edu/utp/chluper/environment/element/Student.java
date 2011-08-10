/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.utp.chluper.environment.element;

import pl.edu.utp.chluper.nvironment.*;
import java.util.ArrayList;
import java.util.List;
import pl.edu.utp.chluper.algorithm.student.StudentAlgorithm;
import pl.edu.utp.chluper.environment.view.StudentView;
import pl.edu.utp.chluper.simulation.logging.LoggingHandler;

/**
 *
 * @author damian
 */
public class Student implements Element, StudentView {

	public static enum StudentType {

		TYPE1,
		TYPE2,
		TYPE3,
		TYPE4,
		TYPE5,
		TYPE6,
		TYPE7,
	}
	// numer
	private final int number;
	// czas powstania
	private int creationTick = -1;
	// czas wejscia do biblioteki
	private int entryTick = -1;
	// czas wejscia do biblioteki
	private int exitTick = -1;
	// kieszen z ksiazkami do oddania
	private final List<Book> booksToReturn = new ArrayList<Book>();
	// lista elementow do wypozyczenia
	private final List<Integer> wishList = new ArrayList<Integer>();
	// kieszen z wypozyczonymi ksiazkami
	private final List<Book> lendBooks = new ArrayList<Book>();
	// typ studenta
	private final StudentType studentType;
	// polozenie
	private Location location;
	// docelowe biurko
	private int targetDeskNumber = -1;
	// algorytm
	private StudentAlgorithm algorithm = null;
	// logger
	private LoggingHandler logger;

	/**
	 * Metoda tworzy studenta
	 * @param booksToReturn
	 * @param wishList
	 * @param location
	 */
	public Student(int number, List<Book> booksToReturn, List<Integer> wishList, StudentType studentType) {
		this.number = number;
		this.booksToReturn.addAll(booksToReturn);
		this.wishList.addAll(wishList);
		this.studentType = studentType;
	}

	/**
	 * Metoda tworzy studenta
	 * @param location
	 */
	public Student(int number, StudentType studentType) {
		this.number = number;
		this.studentType = studentType;
	}

	public ElementType getElementType() {
		return ElementType.STUDENT;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		logger.level1("Zmiana polozenia na: " + location);
		this.location = location;
	}

	public StudentType getStudentType() {
		return studentType;
	}

	public int getEntryTick() {
		return entryTick;
	}

	public void setEntryTick(int entryTick) {
		this.entryTick = entryTick;
	}

	public int getTargetDeskNumber() {
		return targetDeskNumber;
	}

	public void setTargetDeskNumber(int targetDeskNumber) {
		this.targetDeskNumber = targetDeskNumber;
	}

	public int getExitTick() {
		return exitTick;
	}

	public void setExitTick(int exitTick) {
		this.exitTick = exitTick;
	}

	public int getCreationTick() {
		return creationTick;
	}

	public void setCreationTick(int creationTick) {
		this.creationTick = creationTick;
	}

	/**
	 * Zwraca liste ksiazek do oddania
	 * @return
	 */
	public List<Book> getBooksToReturn() {
		return booksToReturn;
	}

	/**
	 * Zwraca liste wypozyczonych ksiazek
	 * @return
	 */
	public List<Book> getLendBooks() {
		return lendBooks;
	}

	/**
	 * Zwraca liste numerow ksaizek do wypozyczenia
	 * @return
	 */
	public List<Integer> getWishList() {
		return wishList;
	}

	public StudentAlgorithm getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(StudentAlgorithm algorithm) {
		this.algorithm = algorithm;
	}

	/**
	 * Numer studenta
	 * @return
	 */
	public int getNumber() {
		return number;
	}

	/**
	 * metoda okresla czy student jest usatysfakcjonowany
	 * tzn oddal i wypozyczyl wszystko co chcial
	 */
	public boolean isSatisfied() {
		if (!booksToReturn.isEmpty()) {
			return false;
		}
		// sprawdzenie czy wszystko z listy jest
		// przygotowywanie drugiej listy
		List<Integer> checkList = new ArrayList<Integer>();
		checkList.addAll(wishList);

		// jesli ksiazka jest w plecaku to powinna byc na liscie
		for (Book book : lendBooks) {
			if (checkList.contains((Integer) book.getIsbn())) {
				checkList.remove((Integer) book.getIsbn());
			} else {
				return false;
			}
		}

		// jeski na liscie jest coc czego nie ma w plecaku
		return checkList.isEmpty();
	}

	/**
	 * Metoda wklada ksiazke do kieszeni
	 * @param book
	 */
	public void put(Book book) {
		boolean ok = false;
		for (Integer isbnToLend : wishList) {
			if (isbnToLend == book.getIsbn()) {
				ok = true;
			}
		}
		if (!ok) {
			logger.level3("Proba wlozenia ksiazki ktorej nie ma na liscie do kieszeni: " + book);
			throw new EnvinronmentException("Proba wypozyczenia ksiazki spoza listy przez studenta: " + this);
		}
		logger.level1("Wlozono ksiazke do kieszeni: " + book);
		lendBooks.add(book);
	}

	/**
	 * Metoda wyjmuje ksiazke z kieszeni
	 * @param isbn
	 * @return true jesli ksiazka tam byla
	 */
	public Book take(int isbn) {
		for (Book book : booksToReturn) {
			if (book.getIsbn() == isbn) {
				if (booksToReturn.remove(book)) {
					logger.level1("Wyjeto ksiazke z kieszeni: " + book);
					return book;
				}
			}
		}
		logger.level2("Proba wyjecia ksiazki ktorej nie ma z kieszeni: " + isbn);
		return null;
	}

	/**
	 * Ustawia obiekt logowania
	 * @param logger
	 */
	public void setLogger(LoggingHandler logger) {
		this.logger = logger;
	}

	/**
	 * Ustawia obiekt logowania dla algorytmu
	 * @param logger
	 */
	public void setAlgorithmLogger(LoggingHandler logger) {
		algorithm.setLogger(logger);
	}

	/**
	 * Tworzy kopie studenta
	 * @return
	 */
	public StudentView getElementView() {
		final Student student = this;
		return new StudentView() {

			// zatrzaskiwanie
			private final Location locationCopy = student.location;
			private final List<Book> booksToReturnCopy = new ArrayList<Book>(student.booksToReturn);
			private final List<Book> lendBooksCopy = new ArrayList<Book>(student.lendBooks);
			private final List<Integer> wishListCopy = new ArrayList<Integer>(student.wishList);
			private final int targetDeskNumberCopy = student.targetDeskNumber;
			private final boolean satisfied = student.isSatisfied();

			public StudentType getStudentType() {
				return student.studentType;
			}

			public int getTargetDeskNumber() {
				return targetDeskNumberCopy;
			}

			public List<Book> getBooksToReturn() {
				return booksToReturnCopy;
			}

			public List<Book> getLendBooks() {
				return lendBooksCopy;
			}

			public List<Integer> getWishList() {
				return wishListCopy;
			}

			public int getNumber() {
				return student.number;
			}

			public ElementType getElementType() {
				return student.getElementType();
			}

			public Location getLocation() {
				return locationCopy;
			}

			public boolean isSatisfied() {
				return satisfied;
			}

			@Override
			public String toString() {
				return "[" + student.getClass().getSimpleName() + "View|" + student.number + "|" + student.location + "|" + student.targetDeskNumber + "|" + booksToReturnCopy + "|" + wishListCopy + "|" + lendBooksCopy + "]";
			}
		};
	}

	@Override
	public String toString() {
		return "[" + this.getClass().getSimpleName() + "|" + number + "|" + location + "|" + targetDeskNumber + "|" + booksToReturn + "|" + wishList + "|" + lendBooks + "]";
	}
}
