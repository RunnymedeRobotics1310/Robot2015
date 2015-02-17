package robot.subsystems;

import robot.RobotMap;
import robot.commands.SensorCommand;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SensorSubsystem extends RunnymedeSubsystem {

	DigitalInput toteSensor = new DigitalInput(RobotMap.TOTE_SENSOR_PORT);
	
	public boolean getToteSensor() {
		return false;//!toteSensor.get();
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
