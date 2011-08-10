/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.utp.chluper.simulation.scenario;

import java.util.ArrayList;
import java.util.List;
import pl.edu.utp.chluper.environment.element.Book;
import pl.edu.utp.chluper.environment.element.Student;

/**
 *
 * @author damian
 */
public class FixedRateScenario implements Scenario {

    // okres co jaki maja wychodic studenci
    private final int period;
	// licznik tykniec
	private int tickCounter = 0;
	// kolejne numery studentow
	private int studentNo = 0;

    /**
     * Konstruktor
     * @param period co ile taktow ma sie pojawiac student
     */
    public FixedRateScenario(int period) {
        this.period = period;
    }

    /**
     * Metoda wywolywana co takt
     * @return
     */
    public Student next() {
        Student student = createStudent();
        tickCounter++;
        return student;
    }

    /**
     * Metoda pobierajaca studenta
     * @return
     */
    private Student createStudent() {
        // sprawdzanie czy teraz sie powinno wygenerowac studenta
        if (tickCounter % period == 0) {
			// zwiekszanie licznika
			studentNo++;
            List<Book> booksToReturn = new ArrayList<Book>();
            List<Integer> lentList = new ArrayList<Integer>();
            // student oddajacy wszystko
            if ((tickCounter / period) % 5 == 0) {
                booksToReturn.add(new Book(tickCounter * 11));
                booksToReturn.add(new Book(tickCounter * 23));
                booksToReturn.add(new Book(tickCounter * 41));
                return new Student(studentNo, booksToReturn, lentList, Student.StudentType.TYPE1);
            }
            // student wypozyczajacy 1
            if ((tickCounter / period) % 5 == 1) {
                lentList.add(tickCounter * 59);
                return new Student(studentNo, booksToReturn, lentList, Student.StudentType.TYPE2);
            }
            // student oddajacy i wypozyczajacy
            if ((tickCounter / period) % 5 == 2) {
                booksToReturn.add(new Book(tickCounter * 73));
                booksToReturn.add(new Book(tickCounter * 97));
                lentList.add(tickCounter * 13);
                lentList.add(tickCounter * 29);
                return new Student(studentNo, booksToReturn, lentList, Student.StudentType.TYPE3);
            }
            // student wypozyczajacy duzo
            if ((tickCounter / period) % 5 == 3) {
                lentList.add(tickCounter * 43);
                lentList.add(tickCounter * 61);
                lentList.add(tickCounter * 79);
                return new Student(studentNo, booksToReturn, lentList, Student.StudentType.TYPE4);
            }
            // student oddajacy 1
            if ((tickCounter / period) % 5 == 4) {
                booksToReturn.add(new Book(tickCounter * 3));
                return new Student(studentNo, booksToReturn, lentList, Student.StudentType.TYPE5);
            }
        }
        return null;
    }
}
