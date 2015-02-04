package robot;

import edu.wpi.first.wpilibj.Gyro;

public class OffsetableGyro extends Gyro {

	private double offset;
	
	public OffsetableGyro(int channel) {
		super(channel);
		offset = 0.0;
	}

	public void setOffset(double offset) {
		super.reset();
		this.offset = offset;
	}
	
	@Override
	public double getAngle() {
		double angle = super.getAngle() + offset;
		
		while (angle < 0)    { angle += 360; }
		while (angle >= 360) { angle -= 360; }

		return angle;
	}
	
	@Override
	public void reset() {
		super.reset();
		offset = 0.0;
	}

}
