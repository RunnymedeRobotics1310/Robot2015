package robot;

import edu.wpi.first.wpilibj.Encoder;

public class EncoderCorrection extends Encoder {

	public EncoderCorrection(int aChannel, int bChannel,
			boolean reverseDirection) {
		super(aChannel, bChannel, reverseDirection);
	}

	@Override
	public double pidGet() {
		return getRate() * 1.44;
	}
	
	@Override
	public double getDistance() {
		return super.getDistance() * 1.44;
	}

}
