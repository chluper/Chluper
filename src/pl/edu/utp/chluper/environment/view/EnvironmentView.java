/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.edu.utp.chluper.environment.view;

import java.util.Collection;
import pl.edu.utp.chluper.environment.element.Element;

/**
 * Interfejs reprezentuje widok srodowiska na jakas okazje
 * @author damian
 */
public interface EnvironmentView {

    /**
     * Zwraca serokosc srodowiska
     * @return
     */
    public int getWidth();

    /**
     * Zwraca wysokosc srodowiska
     * @return
     */
    public int getHeight();

    /**
     * Metoda zwraca wszystkie elementy dostepne w tym widoku srodowiska
     * @return
     */
    public Collection<ElementView> getAllElementViews();

}
