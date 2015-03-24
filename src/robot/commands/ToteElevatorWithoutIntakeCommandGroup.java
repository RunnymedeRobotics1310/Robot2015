package robot.commands;

import robot.Robot;
import robot.subsystems.ToteElevatorSubsystem.ToteElevatorLevel;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class ToteElevatorWithoutIntakeCommandGroup extends CommandGroup {

	public ToteElevatorWithoutIntakeCommandGroup() {
		requires(Robot.sensorSubsystem);
		addSequential(new DriveToteElevatorCommand(ToteElevatorLevel.AUTO_PICKUP_LEVEL, false));
		addSequential(new DriveToteElevatorCommand(ToteElevatorLevel.ONE, false));
	}
	
}
