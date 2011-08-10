/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.edu.utp.chluper.algorithm.concret;

import pl.edu.utp.chluper.algorithm.Algorithm;
import pl.edu.utp.chluper.algorithm.Decision;
import pl.edu.utp.chluper.algorithm.DecisionType;
import pl.edu.utp.chluper.algorithm.util.AbstractAlgorithm;
import pl.edu.utp.chluper.environment.element.Book;
import pl.edu.utp.chluper.environment.view.RobotEnvironmentView;
import pl.edu.utp.chluper.environment.element.Desk;
import pl.edu.utp.chluper.environment.element.Robot;
import pl.edu.utp.chluper.environment.view.DeskView;
import pl.edu.utp.chluper.environment.view.RobotView;
import pl.edu.utp.chluper.simulation.logging.LoggingHandler;

/**
 * Prosty algorytm
 * Wymaga obudowania przez DeliverAlgorithm
 * Obsluguje tylko 1 ksiazke na raz
 * @author damian
 */
public class SimpleAlgorithm extends AbstractAlgorithm {

	/**
	 * Metoda wywolywana w momentach, kiedy trzeba podjac decyzje
	 * @param controlledRobot robot, ktorym aktualnie sterujemy, tylko do odczyty
	 * @param environmentView widok srodowiska z punktu widzenia robota, tylko do odczytu
	 * @return decyzja ktora podejmuje robot
	 */
	public Decision decide(RobotView controlledRobot, RobotEnvironmentView environmentView) {
		// okreslamy czy robot ma cos w kieszeni
		if (controlledRobot.getCache().isEmpty()) {
			// kieszen jest pusta
			logger.level2("Kieszen pusta");
			
			// sprawdzamy czyjakis student czegos nie potrzebuje
			// przegladamy wszystkie biurka
			for (DeskView desk : environmentView.getDeskViews()) {
				// sprawdzamy liste zamowien na biurku
				for (Integer isbn : desk.getWishList()) {
					// jesli cos znalezlismy, to jedziemy po to
					logger.level2("Jedziemy do polki po ksiazke do wypozyczenia: " + isbn);
					// rozkaz automatycznie (na podstawie isbn ksiazki) okresli o ktora polke chodzi
					return new Decision(DecisionType.TAKE_FROM_BOOKSHELF, isbn);
				}
			}

			// jesli nikt nic nie zamawia, to mozemy sprobowac odlazyc jakas ksiazke na polke
			for (DeskView desk : environmentView.getDeskViews()) {
				// sprawdzamy liste zamowien na biurku
				for (Book bookToReturn : desk.getBooksToReturn()) {
					logger.level2("Jedziemy po ksiazke do oddania do bourka: " + desk);
					// jesli cos znalezlismy, to jedziemy po to
					return new Decision(DecisionType.TAKE_FROM_DESK, desk.getNumber(), bookToReturn.getIsbn());
				}
			}

			// jesli nic nie ma do zrobienia to czekamy
			return new Decision(DecisionType.WAIT);

		} else {
			// jest cos w kieszeni

			// ksiazka ktora jest w kieszeni
			Book bookInCache = controlledRobot.getCache().get(0);
			logger.level2("Mamy w kieszeni ksiazke: " + bookInCache);
			// skoro mamy ksiazke w kieszeni, to sa 2 mozliwosci
			// albo student ta ksiazke zamowil i trzeba mu dostarczyc
			// albo trzeba ja odwiezc na polke

			// sprawdzamy czy ktos zamowil
			// przegladamy wszystkie biurka
			for (DeskView desk : environmentView.getDeskViews()) {
				// sprawdzamy liste zamowien na biurku
				for (Integer isbn : desk.getWishList()) {
					// jesli to to samo co ktos zamowil
					if (bookInCache.getIsbn() == isbn) {
						logger.level2("Jedziemy dostarczyc ksiazke do bourka: " + desk);
						// zwracanie rozkazu dostarczenia tam ksiazki
						return new Decision(DecisionType.DELIVER_TO_DESK, desk.getNumber(), bookInCache.getIsbn());
					}
				}
			}
			logger.level2("Jedziemy odlozyc ksiazke na polke");
			// widac nikt tej ksiazki nie potrzebuje wiec zawozimy na polke
			// rozkaz automatycznie (na podstawie isbn ksiazki) okresli o ktora polke chodzi
			return new Decision(DecisionType.DELIVER_TO_BOOKSHELF, bookInCache.getIsbn());
		}
		
	}

}
