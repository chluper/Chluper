/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.edu.utp.chluper.environment.element;

/**
 *
 * @author damian
 */
public enum ElementType {

    // polka na ksiazki
    BOOKSHELF(false, true),
    // biorko
    DESK(false, true),
    // przeszkoda
    OBSTRUCTION(false, true),
    // robot
    ROBOT(true, true),
    // student wypozyczajacy
    STUDENT(true, true),
    // pole do jezdzenia robota
    ROBOT_AREA(false, false),
    // pole startu robota
    ROBOT_START_POINT(false, false),
    // pole do chodzenia studenta
    STUDENT_AREA(false, false),
    // pole wejsciowe studentow
    STUDENT_ENTRY(false, false),
    // pole wyjsciowe studentow
    STUDENT_EXIT(false, false),

    

    ;
    // element poruszajacy sie
    private final boolean moving;
    // element przezroczysty (nie podlegajacy kolizji)
    private final boolean collisional;

    private ElementType(boolean moving, boolean colisional) {
	this.moving = moving;
	this.collisional = colisional;
    }

    /**
     * Okresla czy element jest przezroczysty (nie podlega kolizja)
     * @return
     */
    public boolean isCollisional() {
	return collisional;
    }

    /**
     * Okresla czy element jest zdolny do poruszania sie
     * @return
     */
    public boolean isMoving() {
	return moving;
    }

   
}
