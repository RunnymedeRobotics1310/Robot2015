package robot.commands;

import robot.subsystems.ToteElevatorSubsystem.Level;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class ElevatorCommandGroup extends CommandGroup {

	public ElevatorCommandGroup() {
		addSequential(new DriveElevatorCommand(Level.FLOOR));
		addSequential(new DriveElevatorCommand(Level.TWO));
	}
	
}
