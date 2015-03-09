package robot.commands.autonomous.commandgroup;

import robot.commands.DriveDistanceCommand;
import robot.commands.DriveToAngleCommand;
import robot.commands.DriveToteElevatorCommand;
import robot.commands.ResetGyroCommand;
import robot.commands.autonomous.AutonomousContainerDragCommand;
import robot.commands.autonomous.AutonomousContainerShiftCommand;
import robot.commands.autonomous.AutonomousPickupToteCommand;
import robot.subsystems.ChassisSubsystem.DriveMode;
import robot.subsystems.ToteElevatorSubsystem.ToteElevatorLevel;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutonomousTestCommandGroup extends CommandGroup {

	public AutonomousTestCommandGroup() {
		addSequential(new ResetGyroCommand(270));
		addSequential(new AutonomousContainerDragCommand(true));

		{
			addParallel(new DriveToteElevatorCommand(ToteElevatorLevel.HALF));
			addParallel(new AutonomousContainerShiftCommand(0.75));
			addSequential(new DriveToAngleCommand(235, 0.5, DriveMode.FIELD_RELATIVE));
			addSequential(new DriveDistanceCommand(0.70, 270-30, 230, 16, DriveMode.FIELD_RELATIVE));

			addParallel(new DriveToteElevatorCommand(ToteElevatorLevel.ONE));
			addSequential(new DriveDistanceCommand(0.70, 270+20, 270, 50, DriveMode.FIELD_RELATIVE));

			addParallel(new DriveDistanceCommand(0.6, 270, 270, 14, DriveMode.FIELD_RELATIVE));
			addSequential(new AutonomousPickupToteCommand());

			addSequential(new DriveToteElevatorCommand(ToteElevatorLevel.FLOOR));
		}

		{
			addParallel(new DriveToteElevatorCommand(ToteElevatorLevel.HALF));
			addParallel(new AutonomousContainerShiftCommand(0.75));
			addSequential(new DriveToAngleCommand(235, 0.5, DriveMode.FIELD_RELATIVE));
			addSequential(new DriveDistanceCommand(0.70, 270-30, 230, 16, DriveMode.FIELD_RELATIVE));

			addParallel(new DriveToteElevatorCommand(ToteElevatorLevel.ONE));
			addSequential(new DriveDistanceCommand(0.70, 270+20, 270, 50, DriveMode.FIELD_RELATIVE));

			addParallel(new DriveDistanceCommand(0.6, 270, 270, 14, DriveMode.FIELD_RELATIVE));
			addSequential(new AutonomousPickupToteCommand());

			addSequential(new DriveToteElevatorCommand(ToteElevatorLevel.FLOOR));
		}

		addSequential(new DriveToteElevatorCommand(ToteElevatorLevel.HALF));
		
		addSequential(new DriveToAngleCommand(235, 0.5, DriveMode.FIELD_RELATIVE));

		addSequential(new DriveDistanceCommand(1.0, 270-30, 230, 26, DriveMode.FIELD_RELATIVE));
				
		addSequential(new DriveDistanceCommand(1.0, 270+30, 270+20, 65, DriveMode.FIELD_RELATIVE));
		
		addSequential(new DriveDistanceCommand(1.0, 360, 270, 42*3 + 16, DriveMode.FIELD_RELATIVE));
		addSequential(new DriveToteElevatorCommand(ToteElevatorLevel.FLOOR));

		addParallel(new AutonomousContainerDragCommand(false));
		addSequential(new DriveDistanceCommand(1.0, 90, 270, 36, DriveMode.FIELD_RELATIVE));

		addSequential(new DriveToteElevatorCommand(ToteElevatorLevel.THREE));

	}
}



