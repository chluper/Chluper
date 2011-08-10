/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.utp.chluper.algorithm.graph;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import pl.edu.utp.chluper.algorithm.AlgorithmException;
import pl.edu.utp.chluper.environment.element.Location;
import pl.edu.utp.chluper.environment.element.Turn;
import pl.edu.utp.chluper.environment.view.ElementView;

/**
 * Klasa reprezentuje graf
 * @author damian
 */
public class Graph {

	private static final int WEIGHT = 1;
	private final int environmentWidth;
	private final int environmentHeigth;
	private final Node[][] nodeMatrix;
	private final Set<Node> nodes = new HashSet<Node>();

	/**
	 * Metoda tworzy graf
	 * @param width szerokosc srodowiska
	 * @param height wysokosc srodowiska
	 * @param nodeLocations wezly po ktorych mozna sie poruszac
	 * @param source wezel poczatkowy
	 */
	public Graph(int environmentWidth, int environmentHeigth, Collection<Location> nodeLocations, Location source) {
		this.environmentWidth = environmentWidth;
		this.environmentHeigth = environmentHeigth;
		nodeMatrix = new Node[environmentWidth][environmentHeigth];
		// tworzenie wezlow i uzupelnianie grafu
		for (Location location : nodeLocations) {
			Node node = new Node(location);
			nodes.add(node);
			if (nodeMatrix[location.getX()][location.getY()] != null) {
				throw new AlgorithmException("Powtorzana wartosc lokalizacji:" + location);
			}
			nodeMatrix[location.getX()][location.getY()] = node;
		}
		// oznaczanie poczatku
		Node src = nodeMatrix[source.getX()][source.getY()];
		if (nodeMatrix[source.getX()][source.getY()] == null) {
			throw new AlgorithmException("Nie ma takiego wezla:" + source);
		}
		src.setSource(true);
		src.setDist(0);
		// algorytm dijkstry
		dijkstra();
	}

	/**
	 * Metoda tworzy graf
	 * @param width szerokosc srodowiska
	 * @param height wysokosc srodowiska
	 * @param elements  elementy po ktorych mozna sie poruszac
	 * @param source wezel poczatkowy
	 */
	public Graph(int environmentWidth, int environmentHeigth, Location source, Collection<? extends ElementView> elements) {
		this(environmentWidth, environmentHeigth, getLocations(elements), source);
	}

	/**
	 * Metoda zwraca sciezke (liste lokalizacji) od zrodla do miejsca docelowego
	 * (wlacznie ze zrodlem i miejscem docelowym)
	 * @param target polozenie koncowe
	 * @return lista kolejnych lokalizacji
	 */
	public List<Location> getPathToLocation(Location target) {
		LinkedList<Location> path = new LinkedList<Location>();
		if (nodeMatrix[target.getX()][target.getY()] == null) {
			throw new AlgorithmException("Nie ma takiego wezla:" + target);
		}
		Node current = nodeMatrix[target.getX()][target.getY()];
		// wyluskiwanie od konca
		for (;;) {
			path.addFirst(current.getLocation());
			// poprzednik
			current = current.getPred();
			// jesli nie ma dalej poprzednika to koniec
			if (current == null) {
				break;
			}
		}
		return path;
	}

	/**
	 * Metoda zwraca odleglosc do konkretnego polozenia
	 * @param target cel
	 * @return ilosc krokow do celu
	 */
	public int getDistToLocation(Location target) {
		if (nodeMatrix[target.getX()][target.getY()] == null) {
			throw new AlgorithmException("Nie ma takiego wezla:" + target);
		}
		Node node = nodeMatrix[target.getX()][target.getY()];
		return node.getDist();
	}

	/**
	 * Realizacja algorytmu Dijkstry
	 */
	private void dijkstra() {

		// odnajdywanie nieodwiedzonego wezla o najkrutszej drodze
		Node next = null;
		while ((next = getMinDistNode()) != null) {
			next.setVisited(true);

			// dla wszystkich sasiadow
			for (Node neighbour : getNeighbours(next)) {
				final int dist = next.getDist() + WEIGHT;

				// jesli koszt jest mniejszy od aktualnego kosztu sasiada
				if (dist < neighbour.getDist()) {
					// aktualizacja kosztu
					neighbour.setDist(dist);
					// ustawianie poprzedniego jako obowiazujacego
					neighbour.setPred(next);
				}

			}
		}
	}

	/**
	 * Metoda zwraca nieodwiedzony wezel o najkrutszej drodze
	 * @return wezel lub null jesli takiego nie ma
	 */
	private Node getMinDistNode() {
		int minDist = Integer.MAX_VALUE;
		Node minDistNode = null;
		for (Node node : nodes) {
			if (!node.isVisited() && node.getDist() < minDist) {
				minDistNode = node;
				minDist = node.getDist();
			}
		}
		return minDistNode;
	}

	/**
	 * Metoda zwraca sasiadow danego wezla
	 * @param node
	 * @return
	 */
	private Set<Node> getNeighbours(Node node) {
		Set<Node> neighbours = new HashSet<Node>();

		for (Turn turn : Turn.values()) {
			Location nl = turn.move(node.getLocation(), 1);
			// sprawdzanie czy nie wychodzi poza tablice
			if (nl.getX() >= 0 && nl.getY() >= 0 && nl.getX() < environmentWidth && nl.getY() < environmentHeigth) {
				// jesli na danym polu cos jest
				if (nodeMatrix[nl.getX()][nl.getY()] != null) {
					neighbours.add(nodeMatrix[nl.getX()][nl.getY()]);
				}
			}
		}

		return neighbours;
	}

	/**
	 * Wyluskuje lokalizacje wszystkich elementow
	 * @param elements
	 * @return
	 */
	private static Collection<Location> getLocations(Collection<? extends ElementView> elements) {
		final Set<Location> set = new HashSet<Location>();
		for (ElementView element : elements) {
			set.add(element.getLocation());
		}
		return set;
	}
}
