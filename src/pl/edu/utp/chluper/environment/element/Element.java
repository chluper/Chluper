/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.edu.utp.chluper.environment.element;

import pl.edu.utp.chluper.environment.view.ElementView;


/**
 * Interfejs kazdego elementu w srodowisku
 * @author damian
 */
public interface Element extends ElementView {

    /**
     * Metoda zwraca widok elementu typu readOnly
     * Moze byc to kopia tego obiektu lub ten sam obiekt
     * @return
     */
    public ElementView getElementView();

}
