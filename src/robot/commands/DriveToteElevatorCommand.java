package robot.commands;

import robot.Robot;
import robot.subsystems.ToteElevatorSubsystem.ElevatorMode;
import robot.subsystems.ToteElevatorSubsystem.ToteElevatorLevel;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;

public class DriveToteElevatorCommand extends Command {
	
	ToteElevatorLevel level;
	boolean actuateEyebrows;
	boolean requestPulse = false;
	boolean autoExpired = false;
	
	public DriveToteElevatorCommand(ToteElevatorLevel level, boolean actuateEyebrows) {
		requires(Robot.toteElevatorSubsystem);
		requires(Robot.toteIntakeSubsystem);
		this.level = level;
		this.actuateEyebrows = actuateEyebrows;
	}

	public DriveToteElevatorCommand(ToteElevatorLevel level, boolean actuateEyebrows, boolean requestPulse) {
		requires(Robot.toteElevatorSubsystem);
		requires(Robot.toteIntakeSubsystem);
		this.level = level;
		this.actuateEyebrows = actuateEyebrows;
		this.requestPulse = requestPulse;
	}
	
	public void autoExpired() {
		autoExpired = true;
	}
	
	@Override
	protected void initialize() {
		if(!DriverStation.getInstance().isAutonomous()) {
			Robot.toteElevatorSubsystem.setArm(false);
		}
		Robot.toteIntakeSubsystem.driveIntakeMotors(0.0, false);
		Robot.toteIntakeSubsystem.actuateEyebrows(actuateEyebrows);
		Robot.toteElevatorSubsystem.initDriveToLevel(level);
	}

	@Override
	protected void execute() {
		if(Robot.toteElevatorSubsystem.getEncoderDistance() > -700) {
			Robot.toteIntakeSubsystem.disableSubsystem();
		}
		Robot.toteElevatorSubsystem.driveToLevel();
	}

	@Override
	protected boolean isFinished() {
		return Robot.toteElevatorSubsystem.onTarget() || Robot.toteElevatorSubsystem.getElevatorMode() == ElevatorMode.MANUAL;
	}

	@Override
	protected void end() {
		if(requestPulse) {
			Robot.passiveContainerArmSubsystem.requestPulse();	
		}
		Robot.toteElevatorSubsystem.disableSubsystem();
	}

	@Override
	protected void interrupted() {
		Robot.toteElevatorSubsystem.disableSubsystem();
	}

}
