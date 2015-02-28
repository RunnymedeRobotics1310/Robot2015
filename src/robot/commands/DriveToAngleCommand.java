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
	Timer settlingTimer = new Timer();
	long startTime = 0;
	
	public DriveToAngleCommand(int targetAngle, DriveMode driveMode) {
		this.targetAngle = targetAngle;
		this.driveMode = driveMode;
		requires(Robot.chassisSubsystem);
		this.setInterruptible(true);
		startTime = 0;
	}

	@Override
	protected void initialize() {
	}

	@Override
	protected void execute() {
		
		// Until the angle is within 18 degrees, use a rotation drive, and then switch to the 
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
		
		if (Math.abs(angleDifference) > 15) {
			// Set the rotation value to .8 in the direction of the angle difference 
			double rotation = .8 * Math.signum(angleDifference);
			
			Robot.chassisSubsystem.driveJoystick(Robot.oi.getDriverPolarCoordinate(),
					rotation, driveMode, Robot.oi.getRotationPIDEnable(), Robot.oi.getMotorPIDEnable());
		} else {
			// Wait for a timer to expire in the targeting before looking
			// for the isFinished.  This is done because the angle is controlled by
			// a PID controller and the PID may need time to settle.
			if (startTime == 0) {
				startTime = System.currentTimeMillis();
			}
			Robot.chassisSubsystem.driveToAngle(Robot.oi.getDriverPolarCoordinate(),
					targetAngle, driveMode, Robot.oi.getRotationPIDEnable(), Robot.oi.getMotorPIDEnable());
		}
		
	}

	@Override
	protected boolean isFinished() {
		
		// If not within 18 degrees, then not finished.
//		if (startTime == 0) { return false; }
		
		// Wait at least 1.5 seconds after the timer starts to end the command.
//		long endTime = System.currentTimeMillis();
//		if (endTime - startTime < 1500) { return false; }
		
		// Check if the angle is on target.
		// The PID call to on target does not work for continuous targets like the angle where 360 = 0.
		if (Math.abs(targetAngle - Robot.chassisSubsystem.getGyroAngle()) < ChassisSubsystem.ANGLE_PID_ABSOLUTE_TOLERANCE) {
			startTime = 0;
			return true; 
		}

		if (Math.abs(targetAngle - (Robot.chassisSubsystem.getGyroAngle()-360)) < ChassisSubsystem.ANGLE_PID_ABSOLUTE_TOLERANCE) {
			startTime = 0;
			return true; 
		}
		
		// No matter what happens, always expire this command after 5 seconds of getting close to the angle.
//		if (endTime - startTime > 5000) {
//			startTime = 0;
//			return true;
//		}
		
		return false;
	}

	@Override
	protected void end() {
		Robot.chassisSubsystem.driveJoystick(new PolarCoordinate(), 0.0, DriveMode.ROBOT_RELATIVE, PIDEnable.DISABLED, PIDEnable.DISABLED);
	}

	@Override
	protected void interrupted() {
		Robot.chassisSubsystem.driveJoystick(new PolarCoordinate(), 0.0, DriveMode.ROBOT_RELATIVE, PIDEnable.DISABLED, PIDEnable.DISABLED);
	}
}
