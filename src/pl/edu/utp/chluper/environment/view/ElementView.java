/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.edu.utp.chluper.environment.view;

import pl.edu.utp.chluper.environment.element.ElementType;
import pl.edu.utp.chluper.environment.element.Location;

/**
 * Interfejs widoku kazdego elementu srodowiska
 * Widok jest zatrzasnietym obrazem elementu dla konkretnego taktu symulacji
 * @author damian
 */
public interface ElementView {

	/**
     * Zwraca typ elementu
     * @return
     */
    public ElementType getElementType();

	/**
     * Zwraca polozenie elementu
     * @return
     */
    public Location getLocation();

}
