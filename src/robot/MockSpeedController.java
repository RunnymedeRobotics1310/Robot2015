package robot;

import edu.wpi.first.wpilibj.SpeedController;

public class MockSpeedController implements SpeedController {

	private double setSpeed = 0.0d;
	
	@Override
	public void pidWrite(double output) {
		set(output);
	}

	@Override
	public double get() {
		return setSpeed;
	}

	@Override
	@Deprecated
	public void set(double speed, byte syncGroup) {	
		set(speed);
	}

	@Override
	public void set(double speed) {	
		setSpeed = speed;
	}

	@Override
	public void disable() {
		setSpeed = 0.0d;
	}
}
