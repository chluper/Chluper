/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.edu.utp.chluper.simulation;

import pl.edu.utp.chluper.environment.element.Student;

/**
 * Klasa odpowiedzialna za statystyki
 * @author damian
 */
public class Statistics {

	// ilosc studntow
	private int studentNumber = 0;
	// suma czasow obslugi
	private long serviceTimeSum = 0;
	// suma czasow oczekiwania
	private long waitingTimeSum = 0;
	// suma calkowitego czasu
	private long totalTimeSum = 0;
	
	/**
	 * Dodaje obsluzonego studenta do statystyk
	 * @param student
	 */
	public void add(Student student) {
		studentNumber++;
		serviceTimeSum += student.getExitTick() - student.getEntryTick();
		waitingTimeSum += student.getEntryTick() - student.getCreationTick();
		totalTimeSum += student.getExitTick() - student.getCreationTick();
	}

	/**
	 * Metoda zwraca sredni czas obslugi studentow
	 * @return
	 */
	public double getAverageServeceTime() {
		if (studentNumber == 0) {
			return 0;
		}
		return ((double) serviceTimeSum ) / studentNumber;
	}

	/**
	 * Metoda zwraca sredni czas oczekiwania studentow
	 * @return
	 */
	public double getAverageWaitingTime() {
		if (studentNumber == 0) {
			return 0;
		}
		return ((double) waitingTimeSum ) / studentNumber;
	}

	/**
	 * Metoda zwraca sredni czas oczekiwania studentow
	 * @return
	 */
	public double getAverageTotalTime() {
		if (studentNumber == 0) {
			return 0;
		}
		return ((double) totalTimeSum ) / studentNumber;
	}

    public int getStudentNumber() {
        return studentNumber;
    }

        
	@Override
	public String toString() {
		return "[" + this.getClass().getSimpleName() + "|"+ getStudentNumber() + "|" + getAverageWaitingTime() + "|" + getAverageServeceTime() + "|" + getAverageTotalTime() + "]";
                
	}


}
