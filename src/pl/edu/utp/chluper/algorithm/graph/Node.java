/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.edu.utp.chluper.algorithm.graph;

import pl.edu.utp.chluper.environment.element.Location;

/**
 * wezel grafu
 * @author damian
 */
public class Node {

    // polozenie wezla
    private final Location location;
	// odleglosc od poczatku
	private int dist = Integer.MAX_VALUE;
	// oznaczenie poczatkowego
	private boolean source = false;
	// poprzednik 
	private Node pred = null;
	// odwiedzony
	private boolean visited = false;

    public Node(Location location) {
        this.location = location;
    }

	public Location getLocation() {
		return location;
	}

	public int getDist() {
		return dist;
	}

	public void setDist(int dist) {
		this.dist = dist;
	}

	public Node getPred() {
		return pred;
	}

	public void setPred(Node pred) {
		this.pred = pred;
	}

	public boolean isSource() {
		return source;
	}

	public void setSource(boolean source) {
		this.source = source;
	}

	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}

	


}
