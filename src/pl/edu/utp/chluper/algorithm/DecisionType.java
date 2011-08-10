/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.edu.utp.chluper.algorithm;

/**
 *
 * @author damian
 */
public enum DecisionType {
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
     * <li> arg0 - Turn - kierunek (biurko lub polka)
	 * <li> arg1 - Integer - ISBN
     * <ul>
     */
    PUT(),
	/**
	 * Nie rob nic <br>
	 * Rozkaz wykonywany bezposrednio przez symulator
	 */
	WAIT(),
    /**
     * Idz do lokalizacji
     * <ul>
     * <li> arg0 - Location - lokalizacja
     * </ul>
     */
    GO_TO(),
	/**
     * Idz do biurka
     * <ul>
     * <li> arg0 - DeskView - biorko
     * </ul>
     */
	GO_TO_DESK(),
	/**
     * Idz do polki
     * <ul>
     * <li> arg0 - BookshelfView - isbn
     * </ul>
     */
	GO_TO_BOOKSHELF(),
	/**
     * Dostarcz ksiazke do biurkar
     * <ul>
     * <li> arg0 - Integer|DeskView - numer lub widok biurka
	 * <li> arg1 - Integer|Book - ISBN lub ksiazka
     * <ul>
     */
	DELIVER_TO_DESK(),
	/**
     * Dostarcz ksiazke do polka
     * <ul>
     * <li> arg0 - Integer|Book - ISBN lub ksiazka
     * <ul>
     */
	DELIVER_TO_BOOKSHELF(),
	/**
     * Wez ksiazke z biorka
     * <ul>
     * <li> arg0 - Integer|DeskView - numerlub widok biurka
	 * <li> arg1 - Integer|Book - ISBN lub ksiazka
     * <ul>
     */
	TAKE_FROM_DESK(),
	/**
     * Wez ksiazke z polki
     * <ul>
     * <li> arg0 - Integer - ISBN 
     * <ul>
     */
	TAKE_FROM_BOOKSHELF(),
    ;

    private DecisionType() {
    }

  

}
