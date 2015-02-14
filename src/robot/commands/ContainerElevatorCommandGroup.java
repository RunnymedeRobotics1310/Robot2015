package robot.commands;

import robot.subsystems.ContainerElevatorSubsystem.ContainerElevatorLevel;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class ContainerElevatorCommandGroup extends CommandGroup {

	public ContainerElevatorCommandGroup() {
		addSequential(new DriveContainerElevatorCommand(ContainerElevatorLevel.FLOOR));
		addSequential(new DriveContainerElevatorCommand(ContainerElevatorLevel.TOP));
	}
	
}
