package robot.commands.autonomous.commandgroup;

import robot.commands.DriveDistanceCommand;
import robot.commands.DriveToteElevatorCommand;
import robot.commands.ResetGyroCommand;
import robot.commands.TeleopDriveCommand;
import robot.commands.autonomous.ActuateContainerArmCommand;
import robot.commands.autonomous.AutoIntakeTimeoutCommand;
import robot.commands.autonomous.AutonomousDelayCommand;
import robot.subsystems.ChassisSubsystem.DriveMode;
import robot.subsystems.ToteElevatorSubsystem.ToteElevatorLevel;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class SingleContainerCommandGroup extends CommandGroup {
	public SingleContainerCommandGroup() {
		
		addSequential(new ResetGyroCommand(180));
		
		addSequential(new AutonomousDelayCommand(6000));
		
		addSequential(new DriveToteElevatorCommand(ToteElevatorLevel.HALF, false));
		
		addParallel(new ActuateContainerArmCommand(true));
		addParallel(new DriveDistanceCommand(0.3, 180, 180, 22, DriveMode.FIELD_RELATIVE));
		addSequential(new AutoIntakeTimeoutCommand(2.0));
		addParallel(new TeleopDriveCommand());

		addParallel(new ActuateContainerArmCommand(false));
		addSequential(new AutonomousDelayCommand(250));
		
		addSequential(new DriveToteElevatorCommand(ToteElevatorLevel.TWO, false));
		
		addSequential(new DriveDistanceCommand(0.5, 0, 180, 120, DriveMode.FIELD_RELATIVE));
		addSequential(new TeleopDriveCommand());
		addSequential(new DriveToteElevatorCommand(ToteElevatorLevel.THREE, false));
	}
}
