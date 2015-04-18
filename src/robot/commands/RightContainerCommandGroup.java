package robot.commands;

import robot.Robot;
import robot.subsystems.ChassisSubsystem.DriveMode;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class RightContainerCommandGroup extends CommandGroup{

	public RightContainerCommandGroup() {
		
		addParallel(new RightContainerElevatorCommand());
		addSequential(new DriveDistanceCommand(0.175, Robot.chassisSubsystem.getGyroAngle(), Robot.chassisSubsystem.getGyroAngle(), 25, DriveMode.FIELD_RELATIVE));
		
	}
	
}
