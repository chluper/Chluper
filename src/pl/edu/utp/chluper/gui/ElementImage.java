/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.utp.chluper.gui;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;

/**
 * Klasa reprezentuje widok elementu
 * @author damian
 */
public class ElementImage {

	// welkosc elementu (bok)
	public static final int ELEMENT_SIZE = 32;
	// sciezka do obrazkow
	private static final String IMAGE_DIR_PATH = "/res/graphics/";
	// reprezentacja elementu
	private final BufferedImage bi;

	/**
	 * Metoda tworzy obiekt reprezentujacy graficznie element
	 * @param imagePath sciezka do pliku z obrazkiem w /res/graphics/
	 * @param alpha wartosc kanalu alfa dolozonego do danego obrazka
	 */
	public ElementImage(String imagePath, float alpha) {
		// wczytywanie obrazka
		ImageIcon orginalImage = new javax.swing.ImageIcon(getClass().getResource(IMAGE_DIR_PATH + imagePath));
		// tworzenie wlasciwego obrazka
		bi = new BufferedImage(ELEMENT_SIZE, ELEMENT_SIZE, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = bi.createGraphics();
		// dodawanie przezroczytsosci
		AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC, alpha);
		g.setComposite(ac);
		// wrysowanie oryginalnego obrazka
		g.drawImage(orginalImage.getImage(), 0, 0, ELEMENT_SIZE, ELEMENT_SIZE, null);
		g.dispose();


	}

	/**
	 * Metoda zwraca obiekt image
	 * @return
	 */
	public Image getImage() {
		return bi;
	}
}
