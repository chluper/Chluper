/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.utp.chluper.nvironment;

import pl.edu.utp.chluper.environment.view.ChangingEnvironmentView;
import pl.edu.utp.chluper.environment.view.ElementView;
import pl.edu.utp.chluper.environment.view.RobotEnvironmentView;
import pl.edu.utp.chluper.environment.view.StudentEnvironmentView;
import pl.edu.utp.chluper.environment.view.FullEnvironmentView;
import pl.edu.utp.chluper.environment.element.Location;
import pl.edu.utp.chluper.environment.element.Student;
import pl.edu.utp.chluper.environment.element.ElementType;
import pl.edu.utp.chluper.environment.element.Bookshelf;
import pl.edu.utp.chluper.environment.element.Element;
import pl.edu.utp.chluper.environment.element.Desk;
import pl.edu.utp.chluper.environment.element.Robot;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import pl.edu.utp.chluper.environment.element.Marker;
import pl.edu.utp.chluper.environment.element.Obstruction;
import pl.edu.utp.chluper.environment.view.BookshelfView;
import pl.edu.utp.chluper.environment.view.DeskView;
import pl.edu.utp.chluper.environment.view.MarkerView;
import pl.edu.utp.chluper.environment.view.ObstructionView;
import pl.edu.utp.chluper.environment.view.RobotView;
import pl.edu.utp.chluper.environment.view.StudentView;
import pl.edu.utp.chluper.simulation.logging.LoggerGroup;
import pl.edu.utp.chluper.simulation.logging.LoggingAgent;

/**
 *
 * @author damian
 */
public class Environment {

    // szerokosc
    private final int width;
    // dlugosc
    private final int height;
    // polki
    private final Set<Bookshelf> bookshelfs = new HashSet<Bookshelf>();
    // biorka
    private final Set<Desk> desks = new HashSet<Desk>();
    // roboty
    private final Set<Robot> robots = new HashSet<Robot>();
    // studenci
    private final Set<Student> students = new HashSet<Student>();
    // przeszkody
    private final Set<Obstruction> obstructions = new HashSet<Obstruction>();
    // znaczniki
    private final Set<Marker> markers = new HashSet<Marker>();

    /**
     * Tworzy srodowisko
     * @param width
     * @param height
     * @param elements elementy srodowiska
     */
    public Environment(int width, int height, Collection<Element> elements) {
        this.width = width;
        this.height = height;

        // umieszczanie elementow we wlasciwych kolekcjach
        for (Element element : elements) {
            switch (element.getElementType()) {
                case BOOKSHELF:
                    this.bookshelfs.add((Bookshelf) element);
                    break;
                case DESK:
                    this.desks.add((Desk) element);
                    break;
                case OBSTRUCTION:
                    this.obstructions.add((Obstruction) element);
                    break;
                case ROBOT:
                    this.robots.add((Robot) element);
                    break;
                case ROBOT_AREA:
                case ROBOT_START_POINT:
                case STUDENT_AREA:
                case STUDENT_ENTRY:
                case STUDENT_EXIT:
                    this.markers.add((Marker) element);
                    break;
                default:
                    throw new EnvinronmentException("Element srodowiska: " + element + "nie powinien sie tu znalezc");
            }
        }
    }

    /**
     * Konstruktor tworzy domyslne srodowisko
     */
    public Environment() {
        width = 16;
        height = 16;
    }

    /**
     * Zwraca wysokosc srodowiska
     * @return
     */
    public int getWidth() {
        return width;
    }

    /**
     * Zwraca wysokosc (ilosc elementow) srodowiska
     * @return
     */
    public int getHeight() {
        return height;
    }

    /**
     * Zwraca polki na ksiazki
     * @return
     */
    public Set<Bookshelf> getBookshelfs() {
        return bookshelfs;
    }

    /**
     * Zwraca widoki polek
     * @return
     */
    public Collection<BookshelfView> getBookshelfViews() {
        Set<BookshelfView> set = new HashSet<BookshelfView>();
        for (Bookshelf bookshelf : bookshelfs) {
            set.add(bookshelf.getElementView());
        }
        return set;
    }

    /**
     * Zwraca przeszkody
     * @return
     */
    public Set<Obstruction> getObstructions() {
        return obstructions;
    }

    /**
     * Zwraca widoki przeszkod
     * @return
     */
    public Collection<ObstructionView> getObstructionViews() {
        Set<ObstructionView> set = new HashSet<ObstructionView>();
        for (Obstruction obstruction : obstructions) {
            set.add(obstruction.getElementView());
        }
        return set;
    }

    /**
     * zwraca biurka
     * @return
     */
    public Set<Desk> getDesks() {
        return desks;
    }

    /**
     * Zwraca widoki biurek
     * @return
     */
    public Collection<DeskView> getDeskViews() {
        Set<DeskView> set = new HashSet<DeskView>();
        for (Desk desk : desks) {
            set.add(desk.getElementView());
        }
        return set;
    }

    /**
     * Zwraca znaczniki
     * @return
     */
    public Set<Marker> getMarkers() {
        return markers;
    }

    /**
     * Zwraca widoki markerow
     * @return
     */
    public Collection<MarkerView> getMarkerViews() {
        Set<MarkerView> set = new HashSet<MarkerView>();
        for (Marker marker : markers) {
            set.add(marker.getElementView());
        }
        return set;
    }

    /**
     * Zwraca widoki markerow okreslonego typu
     * @return
     */
    public Collection<MarkerView> getMarkerViewsByType(ElementType type) {
        Set<MarkerView> set = new HashSet<MarkerView>();
        for (Marker marker : markers) {
            if (marker.getElementType() == type) {
                set.add(marker.getElementView());
            }
        }
        return set;
    }

    /**
     * Zwraca roboty
     * @return
     */
    public Set<Robot> getRobots() {
        return robots;
    }

    /**
     * Zwraca widoki biurek
     * @return
     */
    public Collection<RobotView> getRobotViews() {
        Set<RobotView> set = new HashSet<RobotView>();
        for (Robot robot : robots) {
            set.add(robot.getElementView());
        }
        return set;
    }

    /**
     * Zwraca studentow
     * @return
     */
    public Set<Student> getStudents() {
        return students;
    }

    /**
     * Zwraca widoki biurek
     * @return
     */
    public Collection<StudentView> getStudentViews() {
        Set<StudentView> set = new HashSet<StudentView>();
        for (Student student : students) {
            set.add(student.getElementView());
        }
        return set;
    }

    @Override
    public String toString() {
        return "[" + this.getClass().getSimpleName() + "|" + width + "|" + height + "]";
    }

    /**
     * Metoda wyluskuje polke na podstawie isbn ksiazki
     * @param isbn
     * @return
     */
    public Bookshelf getBookshelfByIsbn(int isbn) {
        for (Bookshelf bookshelf : bookshelfs) {
            if (bookshelf.isValidIsbn(isbn)) {
                return bookshelf;
            }
        }
        throw new EnvinronmentException("Nie odlaleziono polki z ksiazka o ISBN: " + isbn);
    }

    /**
     * Metoda wyluskuje polke na podstawie isbn ksiazki
     * @param isbn
     * @return
     */
    public Desk getDeskByNumber(int number) {
        for (Desk desk : desks) {
            if (desk.getNumber() == number) {
                return desk;
            }
        }
        throw new EnvinronmentException("Nie odlaleziono biurka o okreslonym numerze: " + number);
    }

    /**
     * @author Kinga
     * Zwracanie robota o określonej nazwie
     * @param name
     * @return 
     */
    public Robot getRobotByName(String name) {
        for (Robot robot : robots) {
            if (robot.getName().equals(name)) {
                return robot;
            }
        }
        throw new EnvinronmentException("Nie odlaleziono robota o podanej nazwie: " + name);
    }

    /**
     * Dodaje robota do srodowiska i ustala polozenie na punkcie startowym
     * @param robot
     */
    public void putRobot(Robot robot) {
        // wyszukiwanie lokalizacji
        Location location = null;
        for (Element element : markers) {
            if (element.getElementType().equals(ElementType.ROBOT_START_POINT)) {
                location = element.getLocation();
            }
        }
        // jesli to null to cos nie tak
        if (location == null) {
            throw new EnvinronmentException("Brak miejsca startowego dla robota: " + robot);
        }
        // zapis robota
        robot.setLocation(location);
        robots.add(robot);
    }

    /**
     * Dodawanie studenta
     * @param student
     */
    public void addStudent(Student student) {
        students.add(student);
    }

    /**
     * Usuwanie studenta
     * @param student
     */
    public void removeStudent(Student student) {
        students.remove(student);
    }

    /**
     * Metoda zwraca pelen widok srodowiska
     * @return
     */
    public FullEnvironmentView getFullEnvironmentView() {
        final Set<ElementView> elementViews = new HashSet<ElementView>();
        elementViews.addAll(getBookshelfViews());
        elementViews.addAll(getDeskViews());
        elementViews.addAll(getObstructionViews());
        elementViews.addAll(getMarkerViews());
        elementViews.addAll(getRobotViews());
        elementViews.addAll(getStudentViews());

        return new FullEnvironmentView() {

            public int getWidth() {
                return width;
            }

            public int getHeight() {
                return height;
            }

            public Collection<ElementView> getAllElementViews() {
                return elementViews;
            }
        };
    }

    /**
     * Metoda zwraca widok zmieniajacych sie elementow srodowiska
     * @return
     */
    public ChangingEnvironmentView getChangingEnvironmentView() {
        final Environment environment = this;
        return new ChangingEnvironmentView() {

            // zatrzaskiwanie
            private final Collection<RobotView> robotViewsCopy = environment.getRobotViews();
            private final Collection<StudentView> studentViewsCopy = environment.getStudentViews();
            private final Collection<DeskView> deskViewsCopy = environment.getDeskViews();

            public Collection<StudentView> getStudentViews() {
                return studentViewsCopy;
            }

            public Collection<RobotView> getRobotViews() {
                return robotViewsCopy;
            }

            public Collection<DeskView> getDeskViews() {
                return deskViewsCopy;
            }

            public int getWidth() {
                return width;
            }

            public int getHeight() {
                return height;
            }

            public Collection<ElementView> getAllElementViews() {
                Set<ElementView> set = new HashSet<ElementView>();
                set.addAll(robotViewsCopy);
                set.addAll(studentViewsCopy);
                return set;
            }
        };
    }

    /**
     * Metoda zwraca widok przeznaczny dla robota
     * @return
     */
    public RobotEnvironmentView getRobotEnvironmentView() {
        final Environment environment = this;
        return new RobotEnvironmentView() {

            // zatrzaskiwanie
            private final Collection<RobotView> robotViewsCopy = environment.getRobotViews();
            private final Collection<StudentView> studentViewsCopy = environment.getStudentViews();
            private final Collection<BookshelfView> bookshelfViewsCopy = environment.getBookshelfViews();
            private final Collection<DeskView> deskViewsCopy = environment.getDeskViews();
            private final Collection<MarkerView> robotAreaMarkerViewsCopy = environment.getMarkerViewsByType(ElementType.ROBOT_AREA);

            public Collection<BookshelfView> getBookshelfViews() {
                return bookshelfViewsCopy;
            }

            public Collection<DeskView> getDeskViews() {
                return deskViewsCopy;
            }

            public Collection<MarkerView> getRobotAreaMarkerViews() {
                return robotAreaMarkerViewsCopy;
            }

            public Collection<RobotView> getRobotViews() {
                return robotViewsCopy;
            }

            public BookshelfView getBookshelfViewByIsbn(int isbn) {
                for (BookshelfView bookshelfView : bookshelfViewsCopy) {
                    if (bookshelfView.isValidIsbn(isbn)) {
                        return bookshelfView;
                    }
                }
                throw new EnvinronmentException("Nie odlaleziono polki ktora by zawierala ksiazke o isbn: " + isbn);
            }

            public DeskView getDeskViewByNumber(int number) {
                for (DeskView deskView : deskViewsCopy) {
                    if (deskView.getNumber() == number) {
                        return deskView;
                    }
                }
                throw new EnvinronmentException("Nie odlaleziono biurka o okreslonym numerze: " + number);
            }

            /**
             * @author Kinga
             * Zwracanie robota o określonej nazwie
             */
            public RobotView getRobotViewByName(String name) {
                for (RobotView robotView : robotViewsCopy) {
                    if (robotView.getName().equals(name)) {
                        return robotView;
                    }
                }
                throw new EnvinronmentException("Nie odlaleziono robota o okreslonej nazwie: " + name);
            }

            public int getWidth() {
                return width;
            }

            public int getHeight() {
                return height;
            }

            public Collection<ElementView> getAllElementViews() {
                Set<ElementView> set = new HashSet<ElementView>();
                set.addAll(robotViewsCopy);
                set.addAll(studentViewsCopy);
                set.addAll(bookshelfViewsCopy);
                set.addAll(deskViewsCopy);
                set.addAll(robotAreaMarkerViewsCopy);
                return set;
            }
        };
    }

    /**
     * Metoda zwraca widok przeznaczny dla robota
     * @return
     */
    public StudentEnvironmentView getStudentEnvironmentView() {
        final Environment environment = this;
        return new StudentEnvironmentView() {

            // zatrzaskiwanie
            private final Collection<StudentView> studentViewsCopy = environment.getStudentViews();
            private final Collection<DeskView> deskViewsCopy = environment.getDeskViews();
            private final Collection<MarkerView> studentAreaMarkerViewsCopy = environment.getMarkerViewsByType(ElementType.STUDENT_AREA);
            private final Collection<MarkerView> studentEntryMarkerViewsCopy = environment.getMarkerViewsByType(ElementType.STUDENT_ENTRY);
            private final Collection<MarkerView> studentExitMarkerViewsCopy = environment.getMarkerViewsByType(ElementType.STUDENT_EXIT);

            public Collection<DeskView> getDeskViews() {
                return deskViewsCopy;
            }

            public Collection<MarkerView> getStudentAreaMarkerViews() {
                return studentAreaMarkerViewsCopy;
            }

            public Collection<StudentView> getStudentViews() {
                return studentViewsCopy;
            }

            public Collection<MarkerView> getStudentEntryMarkerViews() {
                return studentEntryMarkerViewsCopy;
            }

            public Collection<MarkerView> getStudentExitMarkerViews() {
                return studentExitMarkerViewsCopy;
            }

            public DeskView getDeskViewByNumber(int number) {
                for (DeskView deskView : deskViewsCopy) {
                    if (deskView.getNumber() == number) {
                        return deskView;
                    }
                }
                throw new EnvinronmentException("Nie odlaleziono biurka o okreslonym numerze: " + number);
            }

            public int getWidth() {
                return width;
            }

            public int getHeight() {
                return height;
            }

            public Collection<ElementView> getAllElementViews() {
                Set<ElementView> set = new HashSet<ElementView>();
                set.addAll(studentViewsCopy);
                set.addAll(deskViewsCopy);
                set.addAll(studentAreaMarkerViewsCopy);
                set.addAll(studentEntryMarkerViewsCopy);
                set.addAll(studentExitMarkerViewsCopy);
                return set;
            }
        };
    }

    /**
     * Ustawia elementom srodowiska ktore tego potrzebuja obiekt logowania
     * @param collector
     */
    public void setLoggingAgent(LoggingAgent collector) {
        // ustawianie loggera polkom
        for (Bookshelf bookshelf : bookshelfs) {
            String id = "Bookshelf-" + bookshelf.getNumber();
            bookshelf.setLogger(collector.createLoggingHandler(LoggerGroup.BOOKSHELF, id));
        }
        // ustawianie loggera biurkom
        for (Desk desk : desks) {
            String id = "Desk-" + desk.getNumber();
            desk.setLogger(collector.createLoggingHandler(LoggerGroup.DESK, id));
        }
        // ustawianie loggera robotom
        for (Robot robot : robots) {
            String id = "Robot-" + robot.getName();
            String algorithmId = "Algorithm-" + robot.getName();
            robot.setLogger(collector.createLoggingHandler(LoggerGroup.ROBOT, id));
            robot.setAlgorithmLogger(collector.createLoggingHandler(LoggerGroup.ALGORITHM, algorithmId));
        }
    }
}
