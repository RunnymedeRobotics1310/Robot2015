package robot.commands.autonomous.commandgroup;

import robot.commands.DriveDistanceCommand;
import robot.commands.ResetGyroCommand;
import robot.commands.autonomous.AutonomousDelayCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutonomousThreeToteAngledCommandGroup extends CommandGroup {

	public AutonomousThreeToteAngledCommandGroup() {
		addSequential(new ResetGyroCommand(-90));
		
		addSequential(new DriveDistanceCommand(0.65, 0.0, -135, 21));
		addSequential(new AutonomousDelayCommand(500));
		addSequential(new DriveDistanceCommand(0.65, -90, -135, 48));
		addSequential(new AutonomousDelayCommand(500));
		// addSequential(new DriveDistanceCommand(0.65, 90, 0.0, 21));

		// addSequential(new DriveDistanceCommand(0.65, -90, 0.0, 21));
		addSequential(new DriveDistanceCommand(0.65, -90, -135, 48));
		addSequential(new AutonomousDelayCommand(500));
		// addSequential(new DriveDistanceCommand(0.65, 90, 0.0, 21));

		addSequential(new DriveDistanceCommand(0.65, 0.0, 0, 42));
	}
}
