package robot.commands.autonomous.commandgroup;

import robot.commands.DriveDistanceCommand;
import robot.commands.DriveToAngleCommand;
import robot.commands.DriveToteElevatorCommand;
import robot.commands.ResetGyroCommand;
import robot.commands.autonomous.AutonomousContainerShiftCommand;
import robot.commands.autonomous.AutonomousPickupToteCommand;
import robot.subsystems.ChassisSubsystem.DriveMode;
import robot.subsystems.ToteElevatorSubsystem.ToteElevatorLevel;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutonomousTestCommandGroup extends CommandGroup {

	public AutonomousTestCommandGroup() {


		addSequential(new ResetGyroCommand(270));

		{
			addParallel(new DriveToteElevatorCommand(ToteElevatorLevel.HALF));
			addSequential(new DriveToAngleCommand(235, DriveMode.FIELD_RELATIVE));
			addSequential(new DriveDistanceCommand(1.0, 270-30, 230, 12, DriveMode.FIELD_RELATIVE));

			addParallel(new DriveToteElevatorCommand(ToteElevatorLevel.ONE));
			addSequential(new DriveDistanceCommand(1.0, 270+24.5, 270, 52, DriveMode.FIELD_RELATIVE));

			addParallel(new DriveDistanceCommand(1.0, 270, 270, 16, DriveMode.FIELD_RELATIVE));
			addSequential(new AutonomousPickupToteCommand());

			addSequential(new DriveToteElevatorCommand(ToteElevatorLevel.FLOOR));
		}

		{
			addParallel(new DriveToteElevatorCommand(ToteElevatorLevel.HALF));
			addSequential(new DriveToAngleCommand(235, DriveMode.FIELD_RELATIVE));
			addSequential(new DriveDistanceCommand(1.0, 270-30, 230, 16, DriveMode.FIELD_RELATIVE));

			addParallel(new DriveToteElevatorCommand(ToteElevatorLevel.ONE));
			addSequential(new DriveDistanceCommand(1.0, 270+24.5, 270, 52, DriveMode.FIELD_RELATIVE));

			addParallel(new DriveDistanceCommand(1.0, 270, 270, 16, DriveMode.FIELD_RELATIVE));
			addSequential(new AutonomousPickupToteCommand());

			addSequential(new DriveToteElevatorCommand(ToteElevatorLevel.FLOOR));
		}

		addSequential(new DriveToteElevatorCommand(ToteElevatorLevel.HALF));
		addSequential(new DriveToAngleCommand(235, DriveMode.FIELD_RELATIVE));
		addSequential(new DriveDistanceCommand(1.0, 270-30, 230, 20, DriveMode.FIELD_RELATIVE));
		addSequential(new DriveDistanceCommand(1.0, 0.0-15, 270, 60, DriveMode.FIELD_RELATIVE));
		addSequential(new DriveToteElevatorCommand(ToteElevatorLevel.FLOOR));

		addSequential(new DriveDistanceCommand(1.0, 90, 270, 36, DriveMode.FIELD_RELATIVE));

	}

}




//
//addSequential(new ResetGyroCommand(270));
//
//addParallel(new DriveToteElevatorCommand(ToteElevatorLevel.HALF));
//addSequential(new DriveToAngleCommand(235, DriveMode.FIELD_RELATIVE));
//addSequential(new DriveDistanceCommand(1.0, 270-30, 235, 12, DriveMode.FIELD_RELATIVE));
//
//addParallel(new DriveToteElevatorCommand(ToteElevatorLevel.ONE));
//addSequential(new DriveDistanceCommand(1.0, 270+28, 270, 48, DriveMode.FIELD_RELATIVE));
//
//addParallel(new DriveDistanceCommand(1.0, 270, 270, 18, DriveMode.FIELD_RELATIVE));
//addSequential(new AutonomousPickupToteCommand());
//
//addSequential(new DriveToteElevatorCommand(ToteElevatorLevel.FLOOR));
//
//addSequential(new DriveToteElevatorCommand(ToteElevatorLevel.HALF));
//addParallel(new DriveToteElevatorCommand(ToteElevatorLevel.ONE));
//
//addSequential(new DriveDistanceCommand(1.0, 270-10, 270, 70, DriveMode.FIELD_RELATIVE));
//addParallel(new DriveDistanceCommand(1.0, 270, 270, 18, DriveMode.FIELD_RELATIVE));
//addSequential(new AutonomousPickupToteCommand());
//
//addSequential(new DriveToteElevatorCommand(ToteElevatorLevel.FLOOR));
//addSequential(new DriveToteElevatorCommand(ToteElevatorLevel.HALF));
//
//addSequential(new DriveDistanceCommand(1.0, 0.0, 270, 42*3, DriveMode.FIELD_RELATIVE));
//addSequential(new DriveToteElevatorCommand(ToteElevatorLevel.FLOOR));
//
//addSequential(new DriveDistanceCommand(1.0, 90, 270, 48, DriveMode.FIELD_RELATIVE));
