package robot;

public class CartesianCoordinate {
	
	private double x;
	private double y;
	
	/**
	 * Get the x value of this Cartesian Coordinate (x, y).
	 * @return double - a value between -1.0 and 1.0
	 */
	public double getX() { return this.x; }

	/**
	 * Get the y value of this Cartesian Coordinate (x, y).
	 * @return double - a value between -1.0 and 1.0
	 */
	public double getY() { return this.y; }

	/**
	 * Set the x value of the Cartesian Coordinate (x, y).
	 * <p>
	 * The value of x will be truncated to be a number between -1.0 and 1.0 rounded to 2 decimal places.
	 * 
	 * @param x
	 */
	public void setX(double x) { this.x = roundAndTruncate(x); }
	
	/**
	 * Set the y value of the Cartesian Coordinate (x, y).
	 * <p>
	 * The value of y will be truncated to be a number between -1.0 and 1.0 rounded to 2 decimal places.
	 * 
	 * @param y
	 */
	public void setY(double y) { this.y = roundAndTruncate(y); }
	
	/**
	 * Square each of the values in the cartesian coordinate x,y.
	 * <p>
	 * This convenience method can be used to reduce the sensitivity of the joystick.
	 * When squaring the sensitivity, preserve the sign of the coordinates.
	 * 
	 * @return this - pointer to this cartesian coordinate after squaring.
	 */
	public CartesianCoordinate square() { 
		setX(this.x * this.x * (this.x < 0 ? -1.0 : 1.0));
		setY(this.y * this.y * (this.y < 0 ? -1.0 : 1.0));
		return this;
	}
	
	// @Override
	public String toString() {

		return "(" + this.x + ", " + this.y + ")";

	}
	
	private double roundAndTruncate(double a) {

		if (a > 1.0)  { return  1.0; }
		if (a < -1.0) { return -1.0; }
		
		return Math.round(a * 100.0) / 100.0;
	}

}
