package robot.commands;

import robot.Robot;
import robot.subsystems.ToteElevatorSubsystem.ElevatorMode;
import robot.subsystems.ToteElevatorSubsystem.ToteElevatorLevel;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;

public class DriveToteElevatorCommand extends Command {
	
	ToteElevatorLevel level;
	boolean actuateEyebrows;
	boolean requenstPulse = false;
	
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
		this.requenstPulse = requestPulse;
	}
	
	@Override
	protected void initialize() {
		if(!DriverStation.getInstance().isAutonomous()) {
			Robot.toteElevatorSubsystem.setArm(false);
		}
		Robot.toteIntakeSubsystem.driveIntakeMotors(0.0, false);
		Robot.toteIntakeSubsystem.actuateEyebrows(actuateEyebrows);
		if(Robot.oi.getOperatorPOV() != -1 && level == ToteElevatorLevel.ONE) {
			Robot.toteElevatorSubsystem.initDriveToLevel(ToteElevatorLevel.FIVE);
		} else {
			Robot.toteElevatorSubsystem.initDriveToLevel(level);
		}
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
		if(requenstPulse) {
			Robot.passiveContainerArmSubsystem.requestPulse();
		}
		Robot.toteElevatorSubsystem.disableSubsystem();
	}

	@Override
	protected void interrupted() {
		Robot.toteElevatorSubsystem.disableSubsystem();
	}

}
