package robot.commands.autonomous.commandgroup;

import robot.commands.DriveDistanceCommand;
import robot.commands.ResetGyroCommand;
import robot.subsystems.ChassisSubsystem.DriveMode;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutonomousTestCommandGroup extends CommandGroup {

	public AutonomousTestCommandGroup() {
		addSequential(new ResetGyroCommand(0));
		addSequential(new DriveDistanceCommand(.4, 90, 0.0, 36, DriveMode.FIELD_RELATIVE));
		addSequential(new DriveDistanceCommand(.4, 0, 0.0, 36, DriveMode.FIELD_RELATIVE));
		addSequential(new DriveDistanceCommand(.4, 270, 0.0, 36, DriveMode.FIELD_RELATIVE));
		addSequential(new DriveDistanceCommand(.4, 180, 0.0, 36, DriveMode.FIELD_RELATIVE));
	}

}