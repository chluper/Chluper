/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.utp.chluper.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JPanel;
import pl.edu.utp.chluper.environment.view.ChangingEnvironmentView;
import pl.edu.utp.chluper.environment.element.Element;
import pl.edu.utp.chluper.nvironment.Environment;
import pl.edu.utp.chluper.environment.view.EnvironmentView;
import pl.edu.utp.chluper.environment.element.Robot;
import pl.edu.utp.chluper.environment.element.Student;
import pl.edu.utp.chluper.environment.view.ElementView;
import pl.edu.utp.chluper.environment.view.RobotView;
import pl.edu.utp.chluper.environment.view.StudentView;

/**
 * Klasa umozliwiajaca podglad srodowiska
 * @author damian
 */
public class EnvironmentPanel extends JPanel {

    // obrazki
    Map<String, ElementImage> images = new HashMap<String, ElementImage>();
    // tlo
    private Image constantElementsImage;
    private Image markersImage;
    // aktualny widok srodowiska
    private Environment environment = null;

    /**
     * Tworzy obiekt, wzytuje grafike, przyjmuje domyslny rozmair
     */
    public EnvironmentPanel() {
	images.put("bookshelf", new ElementImage("bookshelf.png", 1));
	images.put("desk", new ElementImage("desk.png", 1));
	images.put("obstruction", new ElementImage("wall2.png", 1));
	images.put("robot", new ElementImage("robot1.png", 0.7f));
	images.put("student1", new ElementImage("person1.png", 0.8f));
	images.put("student2", new ElementImage("person2.png", 0.8f));
	images.put("student3", new ElementImage("person3.png", 0.8f));
	images.put("student4", new ElementImage("person4.png", 0.8f));
	images.put("student5", new ElementImage("person5.png", 0.8f));
	images.put("student6", new ElementImage("person6.png", 0.8f));
	images.put("student7", new ElementImage("person7.png", 0.8f));
	images.put("student8", new ElementImage("person8.png", 0.8f));
	images.put("entry", new ElementImage("exit2.png", 0.4f));
	images.put("exit", new ElementImage("exit1.png", 0.4f));
	images.put("marker1", new ElementImage("marker1.png", 0.3f));
	images.put("marker2", new ElementImage("marker2.png", 0.3f));
        setEnvironment(null);
    }

    /**
     * Metoda ustawia nowe srodowisko do podgladu
     * @param environmentView widok srodowiska
     */
    public void setEnvironment(Environment environment) {
        this.environment = environment;
	if (environment != null) {
	    // wyluskiwanie pelnego widoku
	    EnvironmentView environmentView = environment.getFullEnvironmentView();
            // jesli mamu co robic
            setEnvironmentSize(environmentView.getWidth(), environmentView.getHeight());
            this.constantElementsImage = createConstantElementsImage(environmentView);
            this.markersImage = createMarkersImage(environmentView);
        } else {
            setEnvironmentSize(10, 20);
            this.constantElementsImage = null;
            this.markersImage = null;
        }
        repaint();
    }

    /**
     * rysowanie
     * @param g
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        // tlo
        g.drawImage(constantElementsImage, 0, 0, this);
        g.drawImage(markersImage, 0, 0, this);
	// wyluskiwanie elementow zmiennych
	ChangingEnvironmentView environmentView = environment.getChangingEnvironmentView();
        // elementy
        for (RobotView robotView : environmentView.getRobotViews()) {
            g.drawImage(images.get("robot").getImage(), robotView.getLocation().getX() * ElementImage.ELEMENT_SIZE, robotView.getLocation().getY() * ElementImage.ELEMENT_SIZE, null);
        }
        for (StudentView studentView : environmentView.getStudentViews()) {
            switch (studentView.getStudentType()) {
                case TYPE1:
                    g.drawImage(images.get("student1").getImage(), studentView.getLocation().getX() * ElementImage.ELEMENT_SIZE, studentView.getLocation().getY() * ElementImage.ELEMENT_SIZE, null);
                    break;
                case TYPE2:
                    g.drawImage(images.get("student2").getImage(), studentView.getLocation().getX() * ElementImage.ELEMENT_SIZE, studentView.getLocation().getY() * ElementImage.ELEMENT_SIZE, null);
                    break;
                case TYPE3:
                    g.drawImage(images.get("student3").getImage(), studentView.getLocation().getX() * ElementImage.ELEMENT_SIZE, studentView.getLocation().getY() * ElementImage.ELEMENT_SIZE, null);
                    break;
                case TYPE4:
                    g.drawImage(images.get("student4").getImage(), studentView.getLocation().getX() * ElementImage.ELEMENT_SIZE, studentView.getLocation().getY() * ElementImage.ELEMENT_SIZE, null);
                    break;
                case TYPE5:
                    g.drawImage(images.get("student5").getImage(), studentView.getLocation().getX() * ElementImage.ELEMENT_SIZE, studentView.getLocation().getY() * ElementImage.ELEMENT_SIZE, null);
                    break;
                case TYPE6:
                    g.drawImage(images.get("student6").getImage(), studentView.getLocation().getX() * ElementImage.ELEMENT_SIZE, studentView.getLocation().getY() * ElementImage.ELEMENT_SIZE, null);
                    break;
                default:
                    g.drawImage(images.get("student7").getImage(), studentView.getLocation().getX() * ElementImage.ELEMENT_SIZE, studentView.getLocation().getY() * ElementImage.ELEMENT_SIZE, null);
                    break;

            }

        }
    }

    /**
     * Metoda tworzy tlo - obrazek ze wszystkimi nieruchomymi elementami
     * @param environmentView widok srodowiska
     * @return
     */
    private BufferedImage createConstantElementsImage(EnvironmentView environmentView) {
        BufferedImage bi = new BufferedImage(environmentView.getWidth() * ElementImage.ELEMENT_SIZE, environmentView.getHeight() * ElementImage.ELEMENT_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = bi.createGraphics();
        // rysowanie elementow stalych
        for (ElementView elementView : environmentView.getAllElementViews()) {
            switch (elementView.getElementType()) {
                case BOOKSHELF:
                    g.drawImage(images.get("bookshelf").getImage(), elementView.getLocation().getX() * ElementImage.ELEMENT_SIZE, elementView.getLocation().getY() * ElementImage.ELEMENT_SIZE, null);
                    break;
                case DESK:
                    g.drawImage(images.get("desk").getImage(), elementView.getLocation().getX() * ElementImage.ELEMENT_SIZE, elementView.getLocation().getY() * ElementImage.ELEMENT_SIZE, null);
                    break;
                case OBSTRUCTION:
                    g.drawImage(images.get("obstruction").getImage(), elementView.getLocation().getX() * ElementImage.ELEMENT_SIZE, elementView.getLocation().getY() * ElementImage.ELEMENT_SIZE, null);
                    break;
            }
        }

        return bi;
    }

    /**
     * Metoda tworzy tlo - obrazek ze wszystkimi nieruchomymi elementami
     * @param environmentView widok srodowiska
     * @return
     */
    private BufferedImage createMarkersImage(EnvironmentView environmentView) {
        BufferedImage bi = new BufferedImage(environmentView.getWidth() * ElementImage.ELEMENT_SIZE, environmentView.getHeight() * ElementImage.ELEMENT_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = bi.createGraphics();
        // rysowanie elementow stalych
        for (ElementView elementView : environmentView.getAllElementViews()) {
            switch (elementView.getElementType()) {
                case STUDENT_ENTRY:
                    g.drawImage(images.get("entry").getImage(), elementView.getLocation().getX() * ElementImage.ELEMENT_SIZE, elementView.getLocation().getY() * ElementImage.ELEMENT_SIZE, null);
                    break;
                case STUDENT_EXIT:
                    g.drawImage(images.get("exit").getImage(), elementView.getLocation().getX() * ElementImage.ELEMENT_SIZE, elementView.getLocation().getY() * ElementImage.ELEMENT_SIZE, null);
                    break;
                case STUDENT_AREA:
                    g.drawImage(images.get("marker1").getImage(), elementView.getLocation().getX() * ElementImage.ELEMENT_SIZE, elementView.getLocation().getY() * ElementImage.ELEMENT_SIZE, null);
                    break;
                case ROBOT_AREA:
                    g.drawImage(images.get("marker2").getImage(), elementView.getLocation().getX() * ElementImage.ELEMENT_SIZE, elementView.getLocation().getY() * ElementImage.ELEMENT_SIZE, null);
                    break;
            }
        }

        return bi;
    }

    /**
     * Metoda ustawia wielkosc srodowiska
     * @param width
     * @param heigth
     */
    private void setEnvironmentSize(int width, int heigth) {

        Dimension d = new Dimension(width * ElementImage.ELEMENT_SIZE, heigth * ElementImage.ELEMENT_SIZE);
        setMinimumSize(d);
        setPreferredSize(d);
        setMaximumSize(d);
        setBackground(Color.white);
    }
}
