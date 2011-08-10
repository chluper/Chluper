/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.edu.utp.chluper.environment.view;

import java.util.Collection;

/**
 * Klasa reprezentujaca widok elementow zmieniajacych sie
 * @author damian
 */
public interface ChangingEnvironmentView extends EnvironmentView {

   

    /**
     * Metoda zwraca wszystkich studentow
     * @return
     */
    public Collection<StudentView> getStudentViews();

    /**
     * Metoda zwraca wszystkie roboty
     * @return
     */
    public Collection<RobotView> getRobotViews();

	/**
     * Metoda zwraca wszystkie bourka
     * @return
     */
    public Collection<DeskView> getDeskViews();

}
