package robot.commands;

import robot.subsystems.ElevatorSubsystem.Levels;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class ElevatorCommandGroup extends CommandGroup {

	public ElevatorCommandGroup() {
		addSequential(new DriveElevatorCommand(Levels.TWO.encoderSetpoint));
		addSequential(new DriveElevatorCommand(Levels.FLOOR.encoderSetpoint));
	}
	
}
