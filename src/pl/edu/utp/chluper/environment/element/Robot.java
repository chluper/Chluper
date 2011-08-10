/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.utp.chluper.environment.element;

import java.util.ArrayList;
import java.util.List;
import pl.edu.utp.chluper.algorithm.Algorithm;
import pl.edu.utp.chluper.nvironment.EnvinronmentException;
import pl.edu.utp.chluper.environment.view.ElementView;
import pl.edu.utp.chluper.environment.view.RobotView;
import pl.edu.utp.chluper.simulation.logging.LoggingHandler;

/**
 *
 * @author damian
 */
public class Robot implements Element, RobotView {

	// nazwa
	private final String name;
	// polozenie
	private Location location = null;
	// ilosc miejsca na ksiazki
	private final int cacheLimit;
	// kieszen na ksiazki
	private final List<Book> cache = new ArrayList<Book>();
	// algorytm
	private Algorithm algorithm = null;
	// logger
	private LoggingHandler logger;

	/**
	 * Tworzy obiekt robota
	 */
	public Robot(String name, int cacheLimit) {
		this.name = name;
		this.cacheLimit = cacheLimit;
	}

	/**
	 * Tworzy obiekt robota
	 * @param location poczatkowa lokalizacja
	 */
	public Robot(String name, Location location, int cacheLimit) {
		this.name = name;
		this.cacheLimit = cacheLimit;
		this.location = location;
	}

	public ElementType getElementType() {
		return ElementType.ROBOT;
	}

	/**
	 * Pobiera lokalizacje robota
	 * @return
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * Ustawia nowa lokalizacje robota
	 * @param location
	 */
	public void setLocation(Location location) {
		if (logger != null) {
			logger.level1("Zmiana polozenia na: " + location);
		}
		this.location = location;
	}

	/**
	 * Zwraca ilosc ksiazek ktore moze jednoczesnie transportowac robot
	 * @return
	 */
	public int getCacheLimit() {
		return cacheLimit;
	}

	/**
	 * Zawartosc kieszeni robota
	 * @return
	 */
	public List<Book> getCache() {
		return cache;
	}

	/**
	 * Zwracaalgorytm robota
	 * @return
	 */
	public Algorithm getAlgorithm() {
		return algorithm;
	}

	/**
	 * Zwraca nazwe robota
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Ustawia algorytm
	 * @param algorithm
	 */
	public void setAlgorithm(Algorithm algorithm) {
		this.algorithm = algorithm;
	}

	/**
	 * Metoda wklada ksiazke do kieszeni
	 * @param book
	 */
	public void putToCache(Book book) {
		if (cache.size() >= cacheLimit) {
			logger.level3("Proba wlozenia ksiazki do pelnej kieszeni: " + book);
			throw new EnvinronmentException("Proba wlozenia przepelnienia kieszeni robota: " + this);
		}
		logger.level1("Wlozono ksiazke do kieszeni: " + book);
		cache.add(book);
	}

	/**
	 * Metoda wyjmuje ksiazke z kieszeni
	 * @param isbn
	 * @return true jesli ksiazka tam byla
	 */
	public Book takeFromCache(int isbn) {
		for (Book book : cache) {
			if (book.getIsbn() == isbn) {
				if (cache.remove(book)) {
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
	 * Zwraca kopie elementu
	 * @return
	 */
	public RobotView getElementView() {
		final Robot robot = this;
		return new RobotView() {

			// zatrzaskiwanie
			private final Location locationCopy = robot.location;
			private final List<Book> cacheCopy = new ArrayList<Book>(robot.cache);

			public int getCacheLimit() {
				return robot.cacheLimit;
			}

			public List<Book> getCache() {
				return cacheCopy;
			}

			public String getName() {
				return robot.name;
			}

			public ElementType getElementType() {
				return robot.getElementType();
			}

			public Location getLocation() {
				return locationCopy;
			}

			@Override
			public String toString() {
				return "[" + robot.getClass().getSimpleName() + "View|" + robot.name + "|" + robot.location + "|" + cacheCopy + "/" + robot.cacheLimit + "]";
			}
		};
	}

	@Override
	public String toString() {
		return "[" + this.getClass().getSimpleName() + "|" + name + "|" + location + "|" + cache + "/" + cacheLimit + "]";
	}
}
