package robot.subsystems;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

/**
 *
 */
public class SensorSubsystem extends RunnymedeSubsystem {
    
	// Initialize sensor inputs
	
	DigitalInput toggleSwitch    = new DigitalInput(0);
	DigitalInput proximitySwitch = new DigitalInput(1);
	
	AnalogPotentiometer pot = new AnalogPotentiometer(0);

	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
    // @Override
    public void initSubsystem() {
    	
		LiveWindow.addSensor("DigitalIO", "Toggle",    toggleSwitch);
		LiveWindow.addSensor("DigitalIO", "Proximity", proximitySwitch);
		LiveWindow.addSensor("AnalogIO",  "Pot",       pot);

    }
    
    
    // @Override
    public void updateDashboard() {
    	
    	// Call the updateTable method for all sensors that implement LiveWindowSendable;
    	toggleSwitch.updateTable();
    	proximitySwitch.updateTable();
    }
}

