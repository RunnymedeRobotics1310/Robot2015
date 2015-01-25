package robot.subsystems;

import robot.commands.ActuatorCommand;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Relay.Direction;
import edu.wpi.first.wpilibj.Relay.Value;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class ActuatorSubsystem extends RunnymedeSubsystem {
	
	Relay spike =  new Relay(0);
	Servo servo =  new Servo(3);

	// @Override
    public void initSubsystem() {
    	
    	// Initialize sensors and motors
    	spike.setDirection(Direction.kBoth);
    	spike.set(Value.kOff);
    	
    	// Add all sensors and motors to the LiveWindow
    	
    	LiveWindow.addSensor  ("Actuator", "Spike", spike);
    	LiveWindow.addActuator("Actuator", "Servo", servo);
    	
    	// Start Live Window for all sensors, PIDS and motors that implement LiveWindowSendable
    	SmartDashboard.putData("Servo", servo);
    	SmartDashboard.putData("Relay", spike);
    	
    	servo.startLiveWindowMode();
    	spike.startLiveWindowMode();
        
    }
    
    public void initDefaultCommand() {
    	setDefaultCommand(new ActuatorCommand());
    }

    /**
     * Set the relay to one of the values Value.kForward, kReverse, or kOn, or kOff.
     * @param value - kForward, kReverse, kOn, kOff
     */
    public void setRelay(Value value) {
    	spike.set(value);
    	spike.updateTable();
    }
    
    /**
     * Set the servo setpoint.
     * @param setpoint - kForward, kReverse, kOn, kOff
     */
    public void setServoSetpoint(double setpoint) {
    	servo.set(setpoint);
    	servo.updateTable();
    }
    
    // @Override
    public void updateDashboard() {
    	
    	// Call updateTable on all inputs that implement LiveWindowSendable
    	servo.updateTable();
    	spike.updateTable();

    	SmartDashboard.putData("Servo", servo);
    	SmartDashboard.putData("Relay", spike);
    }
}