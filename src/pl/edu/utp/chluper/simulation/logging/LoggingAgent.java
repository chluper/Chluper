/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.utp.chluper.simulation.logging;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Klasa zajmujaca sie gromadzeniem i
 * @author damian
 */
public class LoggingAgent {

	private class ListenerDetails {
		// obiekt sluchacza

		final LoggingListener listener;
		// poziom logowania
		final LoggingLevel loggingLevel;
		// grupa zrudla logera
		final LoggerGroup loggerGroup;
		// id logera
		final String loggerId;
		// konstruktor

		public ListenerDetails(LoggingListener listener, LoggingLevel loggingLevel, LoggerGroup loggerGroup, String loggerId) {
			this.listener = listener;
			this.loggingLevel = loggingLevel;
			this.loggerGroup = loggerGroup;
			this.loggerId = loggerId;
		}
	}
	// listenery
	private final List<ListenerDetails> listeners = new CopyOnWriteArrayList<ListenerDetails>();
	// aktualny takt symulacji
	private int currentSimulationTick = 0;

	/**
	 * Dodaje sluchacza wszystkich logow okreslonego poziomu
	 * @param listener
	 * @param loggingLevel
	 */
	public void addListener(LoggingListener listener, LoggingLevel level) {
		listeners.add(new ListenerDetails(listener, level, LoggerGroup.ALL, null));
	}

	/**
	 * Dodaje sluchacza logow z okreslonej grupy zrodel okreslonego poziomu
	 * @param listener
	 * @param loggingLevel
	 */
	public void addListener(LoggingListener listener, LoggingLevel level, LoggerGroup loggerGroup) {
		listeners.add(new ListenerDetails(listener, level, loggerGroup, null));
	}

	/**
	 * Dodaje sluchacza logow z okreslonego zrodla okreslonego poziomu
	 * @param listener
	 * @param loggingLevel
	 */
	public void addListener(LoggingListener listener, LoggingLevel level, String loggerId) {
		listeners.add(new ListenerDetails(listener, level, LoggerGroup.ALL, loggerId));
	}

	/**
	 * Usuwa sluchacza
	 * @param listener
	 */
	public void removeListener(LoggingListener listener) {
		for (ListenerDetails listenerDetails : listeners) {
			if (listenerDetails.listener == listener) {
				listeners.remove(listenerDetails);
			}
		}
	}

	/**
	 * Ustawia aktualny takt symulacji
	 * @param currentSimulationTick
	 */
	public void setCurrentSimulationTick(int currentSimulationTick) {
		this.currentSimulationTick = currentSimulationTick;
	}

	public int getCurrentSimulationTick() {
		return currentSimulationTick;
	}

	/**
	 * Tworzy uchwyt do logowania
	 * @param grupa do ktorej ma nalezec obiekt
	 * @return
	 */
	public LoggingHandler createLoggingHandler(final LoggerGroup loggerGroup, final String loggerId) {
		return new LoggingHandler() {

			public void level1(String message) {
				log(new LoggingMessage(LoggingLevel.LEVEL1, loggerGroup, loggerId, currentSimulationTick, message));
			}

			public void level2(String message) {
				log(new LoggingMessage(LoggingLevel.LEVEL2, loggerGroup, loggerId, currentSimulationTick, message));
			}

			public void level3(String message) {
				log(new LoggingMessage(LoggingLevel.LEVEL3, loggerGroup, loggerId, currentSimulationTick, message));
			}
		};
	}

	/**
	 * Metoda loguje dany komunikat
	 * @param message
	 */
	private void log(LoggingMessage message) {
		for (ListenerDetails listenerDetails : listeners) {
			// jesli poziom logowania jest wystarczajacy
			if (message.getLoggingLevel().getLevel() >= listenerDetails.loggingLevel.getLevel()) {
				// jesli grupa pasuje
				if ((message.getLoggerGroup().getMask() & listenerDetails.loggerGroup.getMask()) != 0) {
					// czy sprawdzac identyfikator
					if (listenerDetails.loggerId == null) {
						listenerDetails.listener.newMessage(message);
					} else {
						// sprawdzanie identyfikatora
						if (listenerDetails.loggerId.equals(message.getLoggerId())) {
							listenerDetails.listener.newMessage(message);
						}
					}
				}
			}

		}
	}
}
