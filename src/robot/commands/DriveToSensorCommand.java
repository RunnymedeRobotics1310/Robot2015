package robot.commands;

import robot.PolarCoordinate;
import robot.Robot;
import robot.subsystems.ChassisSubsystem.DriveMode;
import robot.subsystems.ChassisSubsystem.PIDEnable;
import edu.wpi.first.wpilibj.command.Command;

public class DriveToSensorCommand extends Command {

	PolarCoordinate p;
	double targetAngle;
	double distance;
	DriveMode driveMode;

	public DriveToSensorCommand(double r, double thetaDeg, double targetAngle, DriveMode driveMode) {
		this(new PolarCoordinate(r, thetaDeg), targetAngle, driveMode);
	}
	
	public DriveToSensorCommand(PolarCoordinate p, double targetAngle, DriveMode driveMode) {
		requires(Robot.chassisSubsystem);
		requires(Robot.sensorSubsystem);
		this.p = p;
		this.targetAngle = targetAngle;
		this.driveMode = driveMode;
	}

	@Override
	protected void initialize() {
		Robot.chassisSubsystem.resetEncoders();
	}

	@Override
	protected void execute() {
		// Y axis is inverted on controller, the robot drive compensates for this, so a negative is needed here
		//Robot.chassisSubsystem.drive(r * Math.cos(thetaRad), -r * Math.sin(thetaRad), rotation, true);
		Robot.chassisSubsystem.driveToAngle(p, targetAngle, driveMode, PIDEnable.ENABLED, PIDEnable.ENABLED);
	}

	@Override
	protected boolean isFinished() {
		return Robot.sensorSubsystem.getDistanceSensor();
	}

	@Override
	protected void end() {
		Robot.chassisSubsystem.driveJoystick(new PolarCoordinate(), 0.0, DriveMode.ROBOT_RELATIVE, PIDEnable.ENABLED, PIDEnable.ENABLED);
	}

	@Override
	protected void interrupted() {
		Robot.chassisSubsystem.driveJoystick(new PolarCoordinate(), 0.0, DriveMode.ROBOT_RELATIVE, PIDEnable.ENABLED, PIDEnable.ENABLED);
	}

}
