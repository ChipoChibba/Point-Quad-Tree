import java.util.ArrayList;
import java.util.List;

/**
 * @author Chikwanda Chikwanda
 * @author Chipo Chibbamulilo
 * A point quadtree: stores an element at a 2D position, 
 * with children at the subdivided quadrants.
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Spring 2015.
 * @author CBK, Spring 2016, explicit rectangle.
 * @author CBK, Fall 2016, generic with Point2D interface.
 */
public class PointQuadtree<E extends Point2D> {
	private E point;							// the point anchoring this node
	private int x1, y1;							// upper-left corner of the region
	private int x2, y2;							// bottom-right corner of the region
	private PointQuadtree<E> c1, c2, c3, c4;	// children

	/**
	 * Initializes a leaf quadtree, holding the point in the rectangle
	 */
	public PointQuadtree(E point, int x1, int y1, int x2, int y2) {
		this.point = point;
		this.x1 = x1; this.y1 = y1; this.x2 = x2; this.y2 = y2;
	}

	// Getters
	
	public E getPoint() {
		return point;
	}

	public int getX1() {
		return x1;
	}

	public int getY1() {
		return y1;
	}

	public int getX2() {
		return x2;
	}

	public int getY2() {
		return y2;
	}

	/**
	 * Returns the child (if any) at the given quadrant, 1-4
	 * @param quadrant	1 through 4
	 */
	public PointQuadtree<E> getChild(int quadrant) {
		if (quadrant==1) return c1;
		if (quadrant==2) return c2;
		if (quadrant==3) return c3;
		if (quadrant==4) return c4;
		return null;
	}

	/**
	 * Returns whether or not there is a child at the given quadrant, 1-4
	 * @param quadrant	1 through 4
	 */
	public boolean hasChild(int quadrant) {
		return (quadrant==1 && c1!=null) || (quadrant==2 && c2!=null) || (quadrant==3 && c3!=null) || (quadrant==4 && c4!=null);
	}

	/**
	 * Inserts the point into the tree
	 */
	public void insert(E p2) {
		// TODO: YOUR CODE HERE
		int quadrant = findQuadrant(p2);
		if (getChild(quadrant) != null) { // check if quadrant is not null
			getChild(quadrant).insert(p2);
		} else{
			if (quadrant == 1) {
				c1 = new PointQuadtree<>(p2, (int) point.getX(), y1, x2, (int) point.getY());
			}
			if (quadrant == 2) {
				c2 = new PointQuadtree<>(p2, x1, y1, (int) point.getX(), (int) point.getY());
			}
			if (quadrant == 3) {
				c3 = new PointQuadtree<>(p2, x1, (int) point.getY(), (int) point.getX(), y2);
			}
			if (quadrant == 4) {
				c4 = new PointQuadtree<>(p2, (int) point.getX(), (int) point.getY(), x2, y2);
			}
		}


	}
	
	/**
	 * Finds the number of points in the quadtree (including its descendants)
	 */
	public int size() {
		// TODO: YOUR CODE HERE
		int count = 1; // starts at 1 to include the current node
		// increase size of each child
		if (hasChild(1)) count += c1.size();
		if (hasChild(2)) count += c2.size();
		if (hasChild(3)) count += c3.size();
		if (hasChild(4)) count += c4.size();
		return count;
	}
	
	/**
	 * Builds a list of all the points in the quadtree (including its descendants)
	 */
	public List<E> allPoints() {
		// TODO: YOUR CODE HERE
		List<E> treePoints = new ArrayList<>();
		pointsAdd(treePoints); // recursive call
		return treePoints;
	}

	/**
	 * Uses the quadtree to find all points within the circle
	 * @param cx	circle center x
	 * @param cy  	circle center y
	 * @param cr  	circle radius
	 * @return    	the points in the circle (and the qt's rectangle)
	 */
	public List<E> findInCircle(double cx, double cy, double cr) {
		// TODO: YOUR CODE HERE
		List<E> inCircle = new ArrayList<>();
		findInCircle0(cx, cy, cr, inCircle);
		return inCircle;
	}

	public void findInCircle0(double cx, double cy, double cr, List<E> inCircle){
		if (Geometry.circleIntersectsRectangle(cx, cy, cr, x1, y1, x2, y2)) { //check if circle intersects with rectangle
			if (Geometry.pointInCircle(point.getX(), point.getY(), cx, cy, cr)) { //check if point is within circle
				inCircle.add(point); // add point to list
			}
			for (int i = 1; i <= 4; i++){
				if (hasChild(i)) {getChild(i).findInCircle0(cx, cy, cr, inCircle);} //checks if a quadrant has a child and recursively calls findInCircle
			}
		}
	}

	// TODO: YOUR CODE HERE for any helper methods.
	/**
	 * check which quadrant a point will be in
	 * @param p which is the blob at a certain point
	 */
	public int findQuadrant(E p) {
		boolean isRight = p.getX() >= point.getX();
		boolean isTop = p.getY() <= point.getY();
		if (isRight) {
			if (isTop) {
				return 1; // top-right
			} else {
				return 4; // bottom-left
			}
		}
		else { // if is left
			if (isTop) {
				return 2; // top-left
			} else {
				return 3; // bottom-left
			}
		}
	}

	/**
	 * adds points to the list treePoints
	 * @param treePoints is the list of points in a tree
	 */
	private void pointsAdd(List<E> treePoints) {
		// add point to treePoints and do a recursive call on a child.
		treePoints.add(point);
		if (hasChild(1) )c1.pointsAdd(treePoints);
		if (hasChild(2) )c2.pointsAdd(treePoints);
		if (hasChild(3) )c3.pointsAdd(treePoints);
		if (hasChild(4) )c4.pointsAdd(treePoints);

	}
}
