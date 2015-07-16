package robot.commands.autonomous.commandgroup;

import robot.commands.DriveDistanceCommand;
import robot.commands.DriveToSensorCommand;
import robot.commands.ResetGyroCommand;
import robot.commands.TeleopDriveCommand;
import robot.subsystems.ChassisSubsystem.DriveMode;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutonomousDriveForwardCommandGroup extends CommandGroup {

	public AutonomousDriveForwardCommandGroup() {
		addSequential(new ResetGyroCommand(270));
		addSequential(new DriveDistanceCommand(0.75, 180, 270, 50, DriveMode.FIELD_RELATIVE));
		addParallel(new TeleopDriveCommand());
	}
	
}
