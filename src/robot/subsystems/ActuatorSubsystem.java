package robot.subsystems;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

/**
 *
 */
public class ActuatorSubsystem extends RunnymedeSubsystem {
	
	Relay spike =  new Relay(0);
	Servo servo =  new Servo(3);

	// @Override
    public void initSubsystem() {
    	
    	// Add all sensors and motors to the LiveWindow
    	
    	LiveWindow.addSensor  ("Actuator", "Spike", spike);
    	LiveWindow.addActuator("Actuator", "Servo", servo);
    	
    	// Start Live Window for all sensors, PIDS and motors that implement LiveWindowSendable
    	
    	servo.startLiveWindowMode();
    	spike.startLiveWindowMode();
    	
    }
    
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
    // @Override
    public void updateDashboard() {
    	
    	// Call updateTable on all inputs that implement LiveWindowSendable
    	servo.updateTable();
    	spike.updateTable();
    	
    }
}