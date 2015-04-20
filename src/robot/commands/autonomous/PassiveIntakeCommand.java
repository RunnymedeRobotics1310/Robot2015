package robot.commands.autonomous;

import robot.Robot;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;

public class PassiveIntakeCommand extends Command {

	double speed;
	boolean actuateEyebrows;
	
	public PassiveIntakeCommand(double speed, boolean actuateEyebrows) {
		requires(Robot.toteIntakeSubsystem);
		this.speed = speed;
		this.actuateEyebrows = actuateEyebrows;
	}
	
	@Override
	protected void initialize() {
	}

	@Override
	protected void execute() {
		Robot.toteIntakeSubsystem.actuateEyebrows(actuateEyebrows);
		Robot.toteIntakeSubsystem.rollInnerWheels(speed);
		Robot.toteIntakeSubsystem.rollOuterWheels(speed);
	}

	@Override
	protected boolean isFinished() {
		return !DriverStation.getInstance().isAutonomous();
	}

	@Override
	protected void end() {
		Robot.toteIntakeSubsystem.rollInnerWheels(0.0);
	}

	@Override
	protected void interrupted() {
		Robot.toteIntakeSubsystem.rollInnerWheels(0.0);
	}

}
