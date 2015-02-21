package robot;

import edu.wpi.first.wpilibj.Gyro;

public class OffsetableGyro extends Gyro {

	private double offset;
	private double lastRawAngle;
	
	public OffsetableGyro(int channel) {
		super(channel);
		offset = 0.0;
		lastRawAngle = 0.0d;
	}

	public void setOffset(double offset) {
		super.reset();
		lastRawAngle = 0.0d;
		this.offset = offset;
	}

	@Override
	public double getRate() {
		return -super.getRate();
	}
	
	@Override
	public double getAngle() {

		// Watch for gyro values that are messed up and 
		// discard them.
		double rawAngle = super.getAngle();

		if (Math.abs(rawAngle - lastRawAngle) > 360) {
			rawAngle = lastRawAngle;
		}
		
		lastRawAngle = rawAngle;
		
		// Adjust the angle for the offset.
		
		double angle = rawAngle + offset;
		
		angle = angle % 360.0d;
		
		if (angle < 0) { angle += 360; }

		return angle;
	}
	
	@Override
	public void reset() {
		super.reset();
		offset = 0.0;
	}

}
