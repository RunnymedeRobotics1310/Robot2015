package robot.commands.autonomous.commandgroup;

import robot.commands.DriveDistanceCommand;
import robot.commands.DriveToAngleCommand;
import robot.commands.DriveToSensorCommand;
import robot.commands.DriveToteElevatorCommand;
import robot.commands.ResetGyroCommand;
import robot.commands.TeleopDriveCommand;
import robot.commands.ToteElevatorWithoutIntakeCommandGroup;
import robot.commands.autonomous.ActuateContainerArmCommand;
import robot.commands.autonomous.ActuateElevatorArmCommand;
import robot.commands.autonomous.AutonomousContainerDragCommand;
import robot.commands.autonomous.AutonomousDelayCommand;
import robot.commands.autonomous.ActuateHolderCommand;
import robot.commands.autonomous.AutonomousPickupEyebrowsCommand;
import robot.commands.autonomous.AutonomousPickupToteCommand;
import robot.commands.autonomous.PassiveIntakeCommand;
import robot.subsystems.ChassisSubsystem.DriveMode;
import robot.subsystems.ToteElevatorSubsystem.ToteElevatorLevel;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoStartWithContainer extends CommandGroup {
	public AutoStartWithContainer() {
		
		ToteElevatorWithoutIntakeCommandGroup.setAutoExpired(true);
		
			addSequential(new ResetGyroCommand(270));
			addSequential(new AutonomousContainerDragCommand(false));
			addParallel(new ActuateContainerArmCommand(true));

			{
				addSequential(new DriveToteElevatorCommand(ToteElevatorLevel.HALF, false));
				addParallel(new AutonomousPickupEyebrowsCommand(true));
				addSequential(new DriveToAngleCommand(305, 0.5, DriveMode.FIELD_RELATIVE));
				addSequential(new DriveDistanceCommand(0.70, 270+30, 310, 16, DriveMode.FIELD_RELATIVE));

				addParallel(new DriveToteElevatorCommand(ToteElevatorLevel.ONE, false));
				addSequential(new DriveDistanceCommand(0.50, 270-42, 270, 59, DriveMode.FIELD_RELATIVE));
				
				//addSequential(new DriveDistanceCommand(0.3, 270, 270, 20, DriveMode.FIELD_RELATIVE));

				addParallel(new PassiveIntakeCommand(0.75, false));
				
				addSequential(new DriveToSensorCommand(0.2, 270, 270, DriveMode.FIELD_RELATIVE));
//				addParallel(new DriveDistanceCommand(0.60, 270, 270, 10, DriveMode.FIELD_RELATIVE));
				addParallel(new TeleopDriveCommand());
				addSequential(new AutonomousPickupToteCommand());
//				addParallel(new TeleopDriveCommand());
				addParallel(new PassiveIntakeCommand(0.25, false));
				addSequential(new DriveToteElevatorCommand(ToteElevatorLevel.FLOOR, false));
				addParallel(new PassiveIntakeCommand(0.0, false));
			}

			{
				addParallel(new DriveToteElevatorCommand(ToteElevatorLevel.FOUR, false));
				addParallel(new ActuateHolderCommand(true));
				
				addSequential(new DriveToAngleCommand(305, 0.5, DriveMode.FIELD_RELATIVE));
				addSequential(new DriveDistanceCommand(0.70, 270+30, 310, 16, DriveMode.FIELD_RELATIVE));
				
				addSequential(new DriveDistanceCommand(0.50, 270-42.5, 270, 59, DriveMode.FIELD_RELATIVE));

				//addSequential(new DriveDistanceCommand(0.3, 270, 270, 20, DriveMode.FIELD_RELATIVE));

				addParallel(new PassiveIntakeCommand(0.75, false));
				
				addSequential(new DriveToSensorCommand(0.2, 270, 270, DriveMode.FIELD_RELATIVE));
//				addParallel(new DriveDistanceCommand(0.60, 270, 270, 14, DriveMode.FIELD_RELATIVE));
				addParallel(new TeleopDriveCommand());
				addSequential(new AutonomousPickupToteCommand());
//				addParallel(new TeleopDriveCommand());
				addParallel(new DriveToteElevatorCommand(ToteElevatorLevel.ONE, false));

			}
			
			addSequential(new DriveToAngleCommand(330, 0.4, DriveMode.FIELD_RELATIVE));
			addSequential(new DriveDistanceCommand(1.0, 330, 330, 90, DriveMode.FIELD_RELATIVE));
			addSequential(new DriveToteElevatorCommand(ToteElevatorLevel.HALF, false));
			addParallel(new ActuateElevatorArmCommand(false));

			addSequential(new AutonomousDelayCommand(250));

			addParallel(new PassiveIntakeCommand(-0.20, false));
			addSequential(new DriveDistanceCommand(1.0, 150, 330, 36, DriveMode.FIELD_RELATIVE));
			addParallel(new PassiveIntakeCommand(0.0, false));
			
			addParallel(new ActuateElevatorArmCommand(true));
			addSequential(new AutonomousDelayCommand(50));

			addParallel(new DriveToteElevatorCommand(ToteElevatorLevel.FLOOR, false));
			//addSequential(new DriveToAngleCommand(180, DriveMode.FIELD_RELATIVE));
		
	}
}
				/*
				addSequential(new DriveToteElevatorCommand(ToteElevatorLevel.FLOOR, true));
			}
		
		addSequential(new DriveToteElevatorCommand(ToteElevatorLevel.HALF, false));
		addSequential(new DriveDistanceCommand(1.0, 360-15, 270, 42*3 + 16, DriveMode.FIELD_RELATIVE));
		addSequential(new DriveToteElevatorCommand(ToteElevatorLevel.FLOOR, false));

		addSequential(new AutonomousDelayCommand(250));

		addSequential(new DriveDistanceCommand(1.0, 90, 270, 36, DriveMode.FIELD_RELATIVE));
		addParallel(new TeleopDriveCommand());
		addSequential(new DriveToteElevatorCommand(ToteElevatorLevel.HALF, false));
		
		addSequential(new AutonomousContainerDragCommand(false));
		

	}
	
}*/
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
