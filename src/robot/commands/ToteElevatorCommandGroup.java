package robot.commands;

import robot.Robot;
import robot.subsystems.ToteElevatorSubsystem.ToteElevatorLevel;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class ToteElevatorCommandGroup extends CommandGroup {

	public ToteElevatorCommandGroup() {
		requires(Robot.sensorSubsystem);
		addParallel(new DisableReverseDriveCommand(false));
		addSequential(new DriveToteElevatorCommand(ToteElevatorLevel.AUTO_PICKUP_LEVEL, true, true));
		addSequential(new DriveToteElevatorCommand(ToteElevatorLevel.ONE, true));
		addParallel(new DisableReverseDriveCommand(true));
	}
	
}
