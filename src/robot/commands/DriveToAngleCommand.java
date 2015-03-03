package robot.commands;

import robot.PolarCoordinate;
import robot.Robot;
import robot.Timer;
import robot.subsystems.ChassisSubsystem;
import robot.subsystems.ChassisSubsystem.DriveMode;
import robot.subsystems.ChassisSubsystem.PIDEnable;
import edu.wpi.first.wpilibj.command.Command;


public class DriveToAngleCommand extends Command {

	private final int targetAngle;
	private final DriveMode driveMode;
	private double timeout = -1.0;
	private Timer timeoutTimer = new Timer();
	
	public DriveToAngleCommand(int targetAngle, DriveMode driveMode) {
		this(targetAngle, -1.0d, driveMode);
	}

	public DriveToAngleCommand(int targetAngle, double timeout, DriveMode driveMode) {
		requires(Robot.chassisSubsystem);
		this.targetAngle = targetAngle;
		this.driveMode = driveMode;
		this.setInterruptible(true);
		this.timeout = timeout;
		timeoutTimer.disable();
	}

	@Override
	protected void initialize() {
	}

	@Override
	protected void execute() {
		
		if (timeout >= 0) {
			timeoutTimer.start(timeout);
		}
		
		// Until the angle is within 20 degrees, use a rotation drive, and then switch to the 
		// driveToAngle command to finish off when within 18 degrees.

		// Get the difference in angle
		double currentAngle = Robot.chassisSubsystem.getGyroAngle();
		double angleDifference = targetAngle - currentAngle;
		
		if (angleDifference > 180) {
			angleDifference -= 360;
		}
		if (angleDifference < -180) {
			angleDifference += 360;
		}
		
		if (Math.abs(angleDifference) > 25) {
			// Set the rotation value to .8 in the direction of the angle difference 
			double rotation = .8 * Math.signum(angleDifference);
			
			Robot.chassisSubsystem.enableAnglePID();
			Robot.chassisSubsystem.driveJoystick(Robot.oi.getDriverPolarCoordinate(),
					rotation, driveMode, Robot.oi.getRotationPIDEnable(), Robot.oi.getMotorPIDEnable());
		} else {
			// Wait for a timer to expire in the targeting before looking
			// for the isFinished.  This is done because the angle is controlled by
			// a PID controller and the PID may need time to settle.
			Robot.chassisSubsystem.driveToAngle(Robot.oi.getDriverPolarCoordinate(),
					targetAngle, driveMode, Robot.oi.getRotationPIDEnable(), Robot.oi.getMotorPIDEnable());
		}
		
	}

	@Override
	protected boolean isFinished() {

		if (timeout >= 0) {
			if (timeoutTimer.isExpired()) { return true; }
		}
		
		// Check if the angle is on target.
		// The PID call to on target does not work for continuous targets like the angle where 360 = 0.
		if (Math.abs(targetAngle - Robot.chassisSubsystem.getGyroAngle()) < ChassisSubsystem.ANGLE_PID_ABSOLUTE_TOLERANCE) {
			return true; 
		}

		if (Math.abs(targetAngle - (Robot.chassisSubsystem.getGyroAngle()-360)) < ChassisSubsystem.ANGLE_PID_ABSOLUTE_TOLERANCE) {
			return true; 
		}
		
		return false;
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
