package robot.commands.autonomous.commandgroup;

import robot.commands.DriveDistanceCommand;
import robot.commands.DriveToAngleCommand;
import robot.commands.DriveToSensorCommand;
import robot.commands.DriveToteElevatorCommand;
import robot.commands.ResetGyroCommand;
import robot.commands.TeleopDriveCommand;
import robot.commands.autonomous.ActuateContainerArmCommand;
import robot.commands.autonomous.ActuateElevatorArmCommand;
import robot.commands.autonomous.AutonomousContainerDragCommand;
import robot.commands.autonomous.AutonomousDelayCommand;
import robot.commands.autonomous.ActuateHolderCommand;
import robot.commands.autonomous.AutonomousPickupToteCommand;
import robot.commands.autonomous.PassiveIntakeCommand;
import robot.subsystems.ChassisSubsystem.DriveMode;
import robot.subsystems.ToteElevatorSubsystem.ToteElevatorLevel;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class Autonomous2056CommandGroup extends CommandGroup {
	
	public Autonomous2056CommandGroup() {

		addSequential(new ResetGyroCommand(90));

		addSequential(new ActuateContainerArmCommand(true));
		addSequential(new DriveToteElevatorCommand(ToteElevatorLevel.ONE, false));

		addSequential(new DriveDistanceCommand(0.5, 90, 90, 14, DriveMode.FIELD_RELATIVE));

		addParallel(new TeleopDriveCommand());
		addSequential(new AutonomousPickupToteCommand());
		addSequential(new DriveToteElevatorCommand(ToteElevatorLevel.FLOOR, false));

		addSequential(new DriveToteElevatorCommand(ToteElevatorLevel.HALF, false));

		addParallel(new DriveToteElevatorCommand(ToteElevatorLevel.ONE, false));
		
		addSequential(new DriveToAngleCommand(270, DriveMode.FIELD_RELATIVE, -1));
		addSequential(new DriveDistanceCommand(0.75, 180, 270, 9, DriveMode.FIELD_RELATIVE));
		
		// COPY PASTED CODE STARTS HERE
		
		{
			addParallel(new PassiveIntakeCommand(0.75, false));

			addSequential(new DriveToSensorCommand(0.3, 270, 270, DriveMode.FIELD_RELATIVE));
			// addParallel(new DriveDistanceCommand(0.60, 270, 270, 10, DriveMode.FIELD_RELATIVE));
			addParallel(new TeleopDriveCommand());
			addSequential(new AutonomousPickupToteCommand());
			// addParallel(new TeleopDriveCommand());

			addSequential(new DriveToteElevatorCommand(ToteElevatorLevel.FLOOR, true));
		}

		{
			addParallel(new DriveToteElevatorCommand(ToteElevatorLevel.FIVE, false));
			addParallel(new ActuateHolderCommand(true));

			//addParallel(new AutonomousPickupEyebrowsCommand(true));
			addSequential(new DriveToAngleCommand(305, 0.5, DriveMode.FIELD_RELATIVE));
			addSequential(new DriveDistanceCommand(0.70, 270+30, 310, 16, DriveMode.FIELD_RELATIVE));

			addSequential(new DriveDistanceCommand(0.70, 270-36, 270, 45, DriveMode.FIELD_RELATIVE));

			addSequential(new DriveDistanceCommand(0.5, 270, 270, 20, DriveMode.FIELD_RELATIVE));

			addParallel(new PassiveIntakeCommand(0.75, false));

			addSequential(new DriveToSensorCommand(0.2, 270, 270, DriveMode.FIELD_RELATIVE));
			// addParallel(new DriveDistanceCommand(0.60, 270, 270, 14, DriveMode.FIELD_RELATIVE));
			addParallel(new TeleopDriveCommand());
			addSequential(new AutonomousPickupToteCommand());
			// addParallel(new TeleopDriveCommand());
			
			addParallel(new DriveToteElevatorCommand(ToteElevatorLevel.ONE, false));

		}
		
		addParallel(new PassiveIntakeCommand(0.25, false));
		
		addSequential(new DriveToAngleCommand(330, 0.4, DriveMode.FIELD_RELATIVE));
		addSequential(new DriveDistanceCommand(1.0, 330, 330, 75, DriveMode.FIELD_RELATIVE));
		addSequential(new DriveToteElevatorCommand(ToteElevatorLevel.HALF, false));
		addParallel(new ActuateElevatorArmCommand(false));

		addSequential(new AutonomousDelayCommand(250));

		addParallel(new PassiveIntakeCommand(-0.20, false));
		addSequential(new DriveDistanceCommand(1.0, 150, 330, 36, DriveMode.FIELD_RELATIVE));
		addParallel(new PassiveIntakeCommand(0.0, false));
		
		addParallel(new ActuateElevatorArmCommand(true));
		addSequential(new AutonomousDelayCommand(50));

		addParallel(new DriveToteElevatorCommand(ToteElevatorLevel.FLOOR, false));
		addSequential(new DriveToAngleCommand(180, DriveMode.FIELD_RELATIVE));
	}
}
	
	/*
	public Autonomous2056CommandGroup() {

		addSequential(new ResetGyroCommand(90));

		addSequential(new AutoContainerIntakeCommand(true));
		addSequential(new DriveToteElevatorCommand(ToteElevatorLevel.ONE, false));

		addSequential(new DriveDistanceCommand(0.75, 90, 90, 10, DriveMode.FIELD_RELATIVE));

		addParallel(new TeleopDriveCommand());
		addSequential(new AutonomousPickupToteCommand());
		addSequential(new DriveToteElevatorCommand(ToteElevatorLevel.FLOOR, false));

		addSequential(new DriveToteElevatorCommand(ToteElevatorLevel.HALF, false));

		addParallel(new DriveToteElevatorCommand(ToteElevatorLevel.ONE, false));
		
		addSequential(new DriveToAngleCommand(270, DriveMode.FIELD_RELATIVE, -1));
		
		// COPY PASTED CODE STARTS HERE
		
		{
			addParallel(new PassiveIntakeCommand(0.75));

			addSequential(new DriveToSensorCommand(0.3, 270, 270, DriveMode.FIELD_RELATIVE));
			// addParallel(new DriveDistanceCommand(0.60, 270, 270, 10, DriveMode.FIELD_RELATIVE));
			addParallel(new TeleopDriveCommand());
			addSequential(new AutonomousPickupToteCommand());
			// addParallel(new TeleopDriveCommand());

			addSequential(new DriveToteElevatorCommand(ToteElevatorLevel.FLOOR, true));
		}

		{
			addSequential(new DriveToteElevatorCommand(ToteElevatorLevel.HALF, false));
			addParallel(new AutonomousPickupEyebrowsCommand(true));
			addSequential(new DriveToAngleCommand(305, 0.5, DriveMode.FIELD_RELATIVE));
			addSequential(new DriveDistanceCommand(0.70, 270+30, 310, 16, DriveMode.FIELD_RELATIVE));

			addParallel(new DriveToteElevatorCommand(ToteElevatorLevel.ONE, false));
			addSequential(new DriveDistanceCommand(0.70, 270-36, 270, 45, DriveMode.FIELD_RELATIVE));

			addSequential(new DriveDistanceCommand(0.2, 270, 270, 6, DriveMode.FIELD_RELATIVE));

			addParallel(new PassiveIntakeCommand(0.75));

			addSequential(new DriveToSensorCommand(0.2, 270, 270, DriveMode.FIELD_RELATIVE));
			// addParallel(new DriveDistanceCommand(0.60, 270, 270, 14, DriveMode.FIELD_RELATIVE));
			addParallel(new TeleopDriveCommand());
			addSequential(new AutonomousPickupToteCommand());
			// addParallel(new TeleopDriveCommand());

			addSequential(new DriveToteElevatorCommand(ToteElevatorLevel.FLOOR, true));
			addSequential(new DriveToteElevatorCommand(ToteElevatorLevel.HALF, true));

		}

		addParallel(new DriveToteElevatorCommand(ToteElevatorLevel.FIVE, false));

		addParallel(new AutonomousHoldCommand(true));
		
		addSequential(new DriveDistanceCommand(1.0, 360-15, 270, 42*3 + 16, DriveMode.FIELD_RELATIVE));
		addSequential(new DriveToteElevatorCommand(ToteElevatorLevel.FLOOR, false));

		addSequential(new AutonomousDelayCommand(250));

		addSequential(new DriveDistanceCommand(1.0, 90, 270, 36, DriveMode.FIELD_RELATIVE));
		addParallel(new TeleopDriveCommand());
		addSequential(new DriveToteElevatorCommand(ToteElevatorLevel.FIVE, false));
				
		addParallel(new AutonomousHoldCommand(false));

		addSequential(new AutonomousContainerDragCommand(false));

	}
}*/