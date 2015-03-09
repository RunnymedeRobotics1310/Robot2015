package robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class OldStyleCompressor {
	
	DigitalInput pressureSwitch;
	Relay spike;
	
	boolean enabled = false;
	
	public OldStyleCompressor(int spikePort, int pressureSwitchPort) {
		this.spike = new Relay(spikePort);
		this.pressureSwitch = new DigitalInput(pressureSwitchPort);
	}

	public void update() {
		if(!pressureSwitch.get() && enabled) {
			spike.set(Relay.Value.kForward);
		} else {
			spike.set(Relay.Value.kOff);
		}
		SmartDashboard.putBoolean("Pressure Input Switch" , pressureSwitch.get());
		SmartDashboard.putBoolean("Enabled" , enabled);
	}
	
	public void stop() {
		enabled = false;
		spike.set(Relay.Value.kOff);
	}
	
	public void start() {
		enabled = true;
	}
	
}
