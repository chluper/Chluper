/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.edu.utp.chluper.simulation.logging;

/**
 *
 * @author damian
 */
public enum LoggerGroup {

    
    ENVINRONMENT_ELEMENT(0x000f),
    ROBOT(0x0001),
    STUDENT(0x0002),
    BOOKSHELF(0x0004),
    DESK(0x0008),
    ALGORITHM(0x0010),
	COORDINATOR(0x0020),
    STUDENT_ALGORITHM(0x0080),
    SIMULATION(0x0f00),
	TICKS(0x0100),
    STUDENT_MANAGER(0x0200),
	EXECUTOR(0x0400),
    ALL(0xffff),
    ;
    // maska bitowa
    private final int mask;

    private LoggerGroup(int mask) {
	this.mask = mask;
    }

    /**
     * Zwraca maske bitowa
     * @return
     */
    public int getMask() {
	return mask;
    }



}
