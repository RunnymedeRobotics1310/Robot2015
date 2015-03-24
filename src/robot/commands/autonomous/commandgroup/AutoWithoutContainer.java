package robot.commands.autonomous.commandgroup;

import robot.commands.DriveDistanceCommand;
import robot.commands.DriveToAngleCommand;
import robot.commands.DriveToSensorCommand;
import robot.commands.DriveToteElevatorCommand;
import robot.commands.ResetGyroCommand;
import robot.commands.TeleopDriveCommand;
import robot.commands.autonomous.AutonomousContainerDragCommand;
import robot.commands.autonomous.AutonomousDelayCommand;
import robot.commands.autonomous.AutonomousPickupEyebrowsCommand;
import robot.commands.autonomous.AutonomousPickupToteCommand;
import robot.commands.autonomous.SecondaryIntakeCommand;
import robot.subsystems.ChassisSubsystem.DriveMode;
import robot.subsystems.ToteElevatorSubsystem.ToteElevatorLevel;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoWithoutContainer extends CommandGroup {
	public AutoWithoutContainer() {
			addSequential(new ResetGyroCommand(270));
			addSequential(new AutonomousContainerDragCommand(false));

			{
				addSequential(new DriveToteElevatorCommand(ToteElevatorLevel.HALF, false));
				addParallel(new AutonomousPickupEyebrowsCommand(true));
				addSequential(new DriveToAngleCommand(305, 0.5, DriveMode.FIELD_RELATIVE));
				addSequential(new DriveDistanceCommand(0.70, 270+30, 310, 16, DriveMode.FIELD_RELATIVE));

				addParallel(new DriveToteElevatorCommand(ToteElevatorLevel.ONE, false));
				addSequential(new DriveDistanceCommand(0.70, 270-34, 270, 45, DriveMode.FIELD_RELATIVE));

				addParallel(new SecondaryIntakeCommand(0.75));
				addSequential(new DriveToSensorCommand(0.2, 270, 270, DriveMode.FIELD_RELATIVE));
//				addParallel(new DriveDistanceCommand(0.60, 270, 270, 10, DriveMode.FIELD_RELATIVE));
				addParallel(new TeleopDriveCommand());
				addSequential(new AutonomousPickupToteCommand());
//				addParallel(new TeleopDriveCommand());

				addSequential(new DriveToteElevatorCommand(ToteElevatorLevel.FLOOR, true));
			}

			{
				addSequential(new DriveToteElevatorCommand(ToteElevatorLevel.HALF, false));
				addParallel(new AutonomousPickupEyebrowsCommand(true));
				addSequential(new DriveToAngleCommand(305, 0.5, DriveMode.FIELD_RELATIVE));
				addSequential(new DriveDistanceCommand(0.70, 270+30, 310, 16, DriveMode.FIELD_RELATIVE));

				addParallel(new DriveToteElevatorCommand(ToteElevatorLevel.ONE, false));
				addSequential(new DriveDistanceCommand(0.70, 270-34, 270, 45, DriveMode.FIELD_RELATIVE));

				addParallel(new SecondaryIntakeCommand(0.75));
				addSequential(new DriveToSensorCommand(0.2, 270, 270, DriveMode.FIELD_RELATIVE));
//				addParallel(new DriveDistanceCommand(0.60, 270, 270, 14, DriveMode.FIELD_RELATIVE));
				addParallel(new TeleopDriveCommand());
				addSequential(new AutonomousPickupToteCommand());
//				addParallel(new TeleopDriveCommand());

				addSequential(new DriveToteElevatorCommand(ToteElevatorLevel.FLOOR, true));
			}
		
		addSequential(new DriveToteElevatorCommand(ToteElevatorLevel.HALF, false));
		addSequential(new DriveDistanceCommand(1.0, 360-15, 270, 42*3 + 16, DriveMode.FIELD_RELATIVE));
		addSequential(new DriveToteElevatorCommand(ToteElevatorLevel.FLOOR, false));

		addSequential(new AutonomousDelayCommand(250));

		addSequential(new DriveDistanceCommand(1.0, 90, 270, 36, DriveMode.FIELD_RELATIVE));
		addParallel(new TeleopDriveCommand());
		addSequential(new DriveToteElevatorCommand(ToteElevatorLevel.FIVE, false));
		
		addSequential(new AutonomousContainerDragCommand(false));
		

	}
	
}
	/*public AutoWithoutContainer() {
		addSequential(new ResetGyroCommand(270));
		addSequential(new AutonomousContainerDragCommand(false));

		{
			addParallel(new DriveToteElevatorCommand(ToteElevatorLevel.HALF, false));
			addParallel(new AutonomousContainerShiftCommand(0.75));
			addSequential(new DriveToAngleCommand(235, 0.5, DriveMode.FIELD_RELATIVE));
			addSequential(new DriveDistanceCommand(0.70, 270-30, 230, 12, DriveMode.FIELD_RELATIVE));

			addParallel(new DriveToteElevatorCommand(ToteElevatorLevel.ONE, false));
			addSequential(new DriveDistanceCommand(0.70, 270+24, 270, 60, DriveMode.FIELD_RELATIVE));

			addParallel(new DriveDistanceCommand(0.60, 270, 270, 10, DriveMode.FIELD_RELATIVE));
			addSequential(new AutonomousPickupToteCommand());

			addSequential(new DriveToteElevatorCommand(ToteElevatorLevel.FLOOR, true));
		}

		{
			addParallel(new DriveToteElevatorCommand(ToteElevatorLevel.HALF, false));
			addParallel(new AutonomousContainerShiftCommand(0.75));
			addSequential(new DriveToAngleCommand(235, 0.5, DriveMode.FIELD_RELATIVE));
			addSequential(new DriveDistanceCommand(0.70, 270-30, 230, 12, DriveMode.FIELD_RELATIVE));

			addParallel(new DriveToteElevatorCommand(ToteElevatorLevel.ONE, false));
			addSequential(new DriveDistanceCommand(0.70, 270+26, 270, 60, DriveMode.FIELD_RELATIVE));

			addParallel(new DriveDistanceCommand(0.60, 270, 270, 14, DriveMode.FIELD_RELATIVE));
			addSequential(new AutonomousPickupToteCommand());

			addSequential(new DriveToteElevatorCommand(ToteElevatorLevel.FLOOR, true));
		}
	
	addSequential(new DriveToteElevatorCommand(ToteElevatorLevel.HALF, false));
	addSequential(new DriveDistanceCommand(1.0, 360-15, 270, 42*3 + 16, DriveMode.FIELD_RELATIVE));
	addSequential(new DriveToteElevatorCommand(ToteElevatorLevel.FLOOR, false));

	addSequential(new DriveDistanceCommand(1.0, 90, 270, 36, DriveMode.FIELD_RELATIVE));
	addSequential(new DriveToteElevatorCommand(ToteElevatorLevel.THREE, false));
	
	addSequential(new AutonomousContainerDragCommand(false));
	
}
}*/
