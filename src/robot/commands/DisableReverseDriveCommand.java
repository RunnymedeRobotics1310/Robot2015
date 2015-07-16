package robot.commands;

import robot.PolarCoordinate;
import robot.Robot;
import robot.subsystems.ChassisSubsystem.PIDEnable;
import edu.wpi.first.wpilibj.command.Command;

public class DisableReverseDriveCommand extends Command {

	boolean end;
	
	public DisableReverseDriveCommand(boolean end) {
		requires(Robot.chassisSubsystem);
		this.end = end;
	}
	
	@Override
	protected void initialize() {
	}

	@Override
	protected void execute() {

		Robot.toteIntakeSubsystem.driveIntakeMotors(0.75, true);

		PolarCoordinate drivePolarCoordinate = Robot.oi.getDriverPolarCoordinate();
		
		if(drivePolarCoordinate.getTheta() >= 315 || drivePolarCoordinate.getTheta() <= 45) {
		
		Robot.chassisSubsystem.driveJoystick(drivePolarCoordinate,
				Robot.oi.getDriverRotation(), Robot.oi.getDriveMode(), PIDEnable.ENABLED, PIDEnable.ENABLED);
		} else {
			Robot.chassisSubsystem.driveJoystick(new PolarCoordinate(),
					Robot.oi.getDriverRotation(), Robot.oi.getDriveMode(), PIDEnable.ENABLED, PIDEnable.ENABLED);
		}

	}

	@Override
	protected boolean isFinished() {
		return end;
	}

	@Override
	protected void end() {
	}

	@Override
	protected void interrupted() {
	}
	
}
