package robot.commands.autonomous.commandgroup;

import robot.commands.DriveDistanceCommand;
import robot.commands.ResetGyroCommand;
import robot.subsystems.ChassisSubsystem.DriveMode;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutonomousTestCommandGroup extends CommandGroup {

	public AutonomousTestCommandGroup() {
		addSequential(new ResetGyroCommand(0));
		addSequential(new DriveDistanceCommand(0.75, 0, 0.0, 100, DriveMode.FIELD_RELATIVE));
	}

}