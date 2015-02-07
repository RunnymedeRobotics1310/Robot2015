package robot;

public class PolarCoordinate {
	
	private double r;
	
	private double theta;

	public PolarCoordinate(double r, double theta) {
		this.r = r;
		this.theta = theta;
	}
	
	public PolarCoordinate() {
		this.r = 0.0;
		this.theta = 0.0;
	}

	/**
	 * Get a new PolarCoordinate that is the equivalent of the passed in 
	 * CartesianCoordinate.
	 * <p>
	 * The r value will be limited to a maximum of 1.0 and will be rounded to 
	 * two decimal places.
	 * <br>
	 * The theta value will be between 0 and 360 degrees and will be rounded to
	 * zero decimal places.
	 * @param xy
	 * @return
	 */
	public static PolarCoordinate valueOf(CartesianCoordinate xy) {

		PolarCoordinate p = new PolarCoordinate();
		
		// the magnitude is the root of the sum of squares.
		p.setR(Math.sqrt(xy.getX() * xy.getX() 
				       + xy.getY() * xy.getY()));

		// The polar direction, theta, is the arcTan of the polar x and y coordinates.
		// The radians will be between -PI and PI
		double radians = Math.atan2(xy.getX(), xy.getY());
		
		// Convert theta to degrees.
		double degrees = (radians * 180.0)/Math.PI;

		// Allow the setter to round theta and translate it to a value between
		// 0 and 360 degrees.
		p.setTheta(degrees);
		
		return p;
	}
	
	/**
	 * Add the given angle to this PolarCoordinate.
	 * <p>
	 * The new value of theta will be an angle between 0 and 360 degrees, and will be rounded 
	 * to zero decimal places.
	 * 
	 * @param thetaOffset - the number of degrees by which to change the theta value of this
	 * polar coordinate.
	 */
	public void addAngle(double thetaOffset) { setTheta(this.theta + thetaOffset); }

	/**
	 * Get the r value of the (r, theta) coordinate.
	 * @return r - a value between 0 and 1.0.
	 */
	public double getR() { return this.r; }
	
	/**
	 * Get the theta value of the (r, theta) coordinate.
	 * @return theta - a value between 0 and 360 degrees.
	 */
	public double getTheta() { return this.theta; }
	
	/**
	 * Set the Polar Coordinate to the same value as the passed in PolarCoordinate.
	 * <p>
	 * @param p - the PolarCoordinate (r, theta) used to set this object.
	 */
	public void set(PolarCoordinate p) {
		this.r     = p.r;
		this.theta = p.theta;
	}
	
	/**
	 * Set the r value of the polar coordinate r, theta.
	 * <p>
	 * This routine will take the absolute value of the number entered, round it to an upper
	 * bound of 1.0, and round the value to 2 decimal places.
	 * <p>
	 * @param r - the magnitude of the (r, theta) coordinate.
	 */
	public void setR(double r) {
		this.r = Math.round(Math.abs(r) * 100.0) / 100.0; 
		if (this.r > 1.0) {
			this.r = 1.0;
		}
	}
	
	/**
	 * Set the theta value of the Polar Coordinate.
	 * <p>
	 * This routine will take theta in degrees and make the angle between 0 and 360 degrees.  
	 * The angle will be rounded to 0 decimal places.
	 * 
	 * @param theta - the angle of the (r, theta) coordinate.
	 */
	public void setTheta(double theta) {
		this.theta = Math.round(theta);
		
		while (this.theta < 0.0) {
			this.theta += 360;
		}
		
		while (this.theta >= 360.0) {
			this.theta -= 360;
		}
	}
	
	/**
	 * Square the r value in the Polar Coordinate.
	 * <p>
	 * This convenience method can be used to reduce the sensitivity of the joystick.
	 * 
	 * @return this - pointer to this Polar Coordinate.
	 */
	public PolarCoordinate square() { 
		setR(this.r*this.r);
		return this;
	}
	

	// @Override
	public String toString() {

		return "(" + this.r + ", " + this.theta + ")";

	}
}
