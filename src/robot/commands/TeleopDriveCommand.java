package robot.commands;

import robot.PolarCoordinate;
import robot.Robot;
import robot.RobotMap;
import robot.Toggle;
import robot.subsystems.ChassisSubsystem.PIDEnable;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class TeleopDriveCommand extends Command {

	Toggle togglePID = new Toggle(true);
	double prevDriveSpeed = 0.0d;

	// The approximate loop speed is 20ms, so this means 50 iterations per second.
	double MAX_DRIVE_INCREMENT = RobotMap.MAX_TELEOP_DRIVE_ACCELERATION/50;

	public TeleopDriveCommand() {
		// Use requires() here to declare subsystem dependencies
		requires(Robot.chassisSubsystem);
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		
		PolarCoordinate drivePolarCoordinate = Robot.oi.getDriverPolarCoordinate();
		
		if (! Robot.oi.getAccelerationOverride()) {
			// Always ramp the r-value from the Polar Drive between the last r value and the current r value.
			double driveIncrement = drivePolarCoordinate.getR() - prevDriveSpeed;
			
			if (Math.abs(driveIncrement) > MAX_DRIVE_INCREMENT) {
				driveIncrement = MAX_DRIVE_INCREMENT * Math.signum(driveIncrement);
			}
			
			double newDriveSpeed = prevDriveSpeed + driveIncrement;
			
			prevDriveSpeed = newDriveSpeed;
			
			drivePolarCoordinate = new PolarCoordinate(newDriveSpeed, drivePolarCoordinate.getTheta());
		}

		Robot.chassisSubsystem.driveJoystick(drivePolarCoordinate,
				Robot.oi.getDriverRotation(), Robot.oi.getDriveMode(), PIDEnable.ENABLED, PIDEnable.ENABLED);

	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return false;
	}

	// Called once after isFinished returns true
	protected void end() {
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
	}
}
