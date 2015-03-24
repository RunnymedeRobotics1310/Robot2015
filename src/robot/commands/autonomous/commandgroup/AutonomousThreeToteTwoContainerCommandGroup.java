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

public class AutonomousThreeToteTwoContainerCommandGroup extends CommandGroup {
	public AutonomousThreeToteTwoContainerCommandGroup() {
		addSequential(new ResetGyroCommand(270));
		addSequential(new AutonomousContainerDragCommand(true));

		{
			addParallel(new DriveToteElevatorCommand(ToteElevatorLevel.HALF, false));
			addParallel(new AutonomousContainerShiftCommand(0.75));
			addSequential(new DriveToAngleCommand(235, 0.5, DriveMode.FIELD_RELATIVE));
			addSequential(new DriveDistanceCommand(0.70, 270-30, 230, 12, DriveMode.FIELD_RELATIVE));

			addParallel(new DriveToteElevatorCommand(ToteElevatorLevel.ONE, false));
			addSequential(new DriveDistanceCommand(0.70, 270+23, 270, 60, DriveMode.FIELD_RELATIVE));

			addParallel(new DriveDistanceCommand(0.60, 270, 270, 6, DriveMode.FIELD_RELATIVE));
			addSequential(new AutonomousPickupToteCommand());

			addSequential(new DriveToteElevatorCommand(ToteElevatorLevel.FLOOR, true));
		}

		{
			addParallel(new DriveToteElevatorCommand(ToteElevatorLevel.HALF, false));
			addParallel(new AutonomousContainerShiftCommand(0.75));
			addSequential(new DriveToAngleCommand(235, 0.5, DriveMode.FIELD_RELATIVE));
			addSequential(new DriveDistanceCommand(0.70, 270-30, 230, 16, DriveMode.FIELD_RELATIVE));

			addParallel(new DriveToteElevatorCommand(ToteElevatorLevel.ONE, false));
			addSequential(new DriveDistanceCommand(0.70, 270+19, 270, 55, DriveMode.FIELD_RELATIVE));

			addSequential(new DriveDistanceCommand(0.70, 270, 270, 12, DriveMode.FIELD_RELATIVE));
			
			addParallel(new DriveDistanceCommand(0.6, 270, 270, 8, DriveMode.FIELD_RELATIVE));
			addSequential(new AutonomousPickupToteCommand());

			addSequential(new DriveToteElevatorCommand(ToteElevatorLevel.FLOOR, true));
		}
		
		addSequential(new DriveToteElevatorCommand(ToteElevatorLevel.HALF, false));
		addSequential(new DriveDistanceCommand(1.0, 360 - 15, 270, 42*3 + 20, DriveMode.FIELD_RELATIVE));
		addSequential(new DriveToteElevatorCommand(ToteElevatorLevel.FLOOR, false));

		addSequential(new DriveDistanceCommand(1.0, 90, 270, 36, DriveMode.FIELD_RELATIVE));
		addSequential(new DriveToteElevatorCommand(ToteElevatorLevel.THREE, false));
		
		addSequential(new AutonomousContainerDragCommand(false));
		
	}
}