package robot.subsystems;

import robot.RobotMap;
import robot.commands.SensorCommand;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SensorSubsystem extends RunnymedeSubsystem {

	DigitalInput toteSensorOne = new DigitalInput(RobotMap.TOTE_SENSOR_PORT_ONE);
	DigitalInput toteSensorTwo = new DigitalInput(RobotMap.TOTE_SENSOR_PORT_TWO);
	
	DigitalInput distanceSensorLeft  = new DigitalInput(RobotMap.DISTANCE_SENSOR_LEFT);
	DigitalInput distanceSensorRight = new DigitalInput(RobotMap.DISTANCE_SENSOR_RIGHT);
	
	public boolean getToteSensor() {
		return !toteSensorOne.get() && !toteSensorTwo.get();
	}
	
	// The distance sensors are normally true, and go false when the 
	// distance is reached.
	public boolean getDistanceSensor() {
		return !distanceSensorLeft.get();
	}
	
	@Override
	public void disableSubsystem() {
	}

	@Override
	public void enableSubsystem() {
	}

	@Override
	public void initSubsystem() {
	}

	@Override
	public void updateDashboard() {
	}

	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new SensorCommand());
	}

}
