package robot.subsystems;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

/**
 *
 */
public class SensorSubsystem extends Subsystem {
    
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	
	DigitalInput toggleSwitch    = new DigitalInput(0);
	DigitalInput proximitySwitch = new DigitalInput(1);
	
	AnalogPotentiometer pot = new AnalogPotentiometer(0);

	public SensorSubsystem() {
		LiveWindow.addSensor("DigitalIO", "Toggle",    toggleSwitch);
		LiveWindow.addSensor("DigitalIO", "Proximity", proximitySwitch);
		LiveWindow.addSensor("AnalogIO",  "Pot",       pot);
	}
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
    public void init() {
    	
    }
}

