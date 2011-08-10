/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.edu.utp.chluper.algorithm.student;

/**
 *
 * @author damian
 */
public enum StudentDecisionType {
	/**
     * Przemiesc sie<br>
	 * Rozkaz wykonywany bezposrednio przez symulator
     * <ul>
     * <li> arg0 - Turn - kierunek ruchu
     * </ul>
     */
    MOVE(),
    /**
     * Wez ksiazke z polki lub biurka <br>
	 * Rozkaz wykonywany bezposrednio przez symulator
     * <ul>
     * <li> arg0 - Turn - kierunek (biurko lub polka)
     * <li> arg1 - Integer - ISBN
     * <ul>
     */
    TAKE(),
    /**
     * Poluz ksiazke <br>
	 * Rozkaz wykonywany bezposrednio przez symulator
     * <ul>
     * <li> arg0 - Turn - kierunek (biurko)
	 * <li> arg1 - Integer - ISBN
     * <ul>
     */
    PUT(),
	/**
     * Zamow ksiazki <br>
	 * Rozkaz wykonywany bezposrednio przez symulator
     * <ul>
     * <li> arg0 - Turn - kierunek (biurko)
     * <ul>
     */
    REQUEST(),
	/**
	 * Nie rob nic <br>
	 * Rozkaz wykonywany bezposrednio przez symulator
	 */
	WAIT(),
	/**
	 * Wyjdz, tylko jesli jest na polu wyjscie <br>
	 * Rozkaz wykonywany bezposrednio przez symulator
	 */
	LEAVE(),
    /**
     * Idz do lokalizacji
     * <ul>
     * <li> arg0 - Location - lokalizacja
     * </ul>
     */
    GO_TO(),
}
