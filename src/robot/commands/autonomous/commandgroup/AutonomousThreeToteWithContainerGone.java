package robot.commands.autonomous.commandgroup;

import robot.commands.DriveDistanceCommand;
import robot.commands.DriveToAngleCommand;
import robot.commands.DriveToteElevatorCommand;
import robot.commands.ResetGyroCommand;
import robot.commands.autonomous.AutonomousPickupToteCommand;
import robot.subsystems.ChassisSubsystem.DriveMode;
import robot.subsystems.ToteElevatorSubsystem.ToteElevatorLevel;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutonomousThreeToteWithContainerGone  extends CommandGroup {

	public AutonomousThreeToteWithContainerGone() {
		addSequential(new ResetGyroCommand(270));

		addParallel(new DriveToteElevatorCommand(ToteElevatorLevel.HALF, false));
		addSequential(new DriveToAngleCommand(235, DriveMode.FIELD_RELATIVE));
		addSequential(new DriveDistanceCommand(0.8, 270-30, 235, 12, DriveMode.FIELD_RELATIVE));

		addParallel(new DriveToteElevatorCommand(ToteElevatorLevel.ONE, false));
		addSequential(new DriveDistanceCommand(0.8, 270+28, 270, 48, DriveMode.FIELD_RELATIVE));

		addParallel(new DriveDistanceCommand(0.8, 270, 270, 18, DriveMode.FIELD_RELATIVE));
		addSequential(new AutonomousPickupToteCommand());

		addSequential(new DriveToteElevatorCommand(ToteElevatorLevel.FLOOR, true));

		addSequential(new DriveToteElevatorCommand(ToteElevatorLevel.HALF, false));
		addParallel(new DriveToteElevatorCommand(ToteElevatorLevel.ONE, false));

		addSequential(new DriveDistanceCommand(0.8, 270-10, 270, 70, DriveMode.FIELD_RELATIVE));
		addParallel(new DriveDistanceCommand(0.8, 270, 270, 18, DriveMode.FIELD_RELATIVE));
		addSequential(new AutonomousPickupToteCommand());

		addSequential(new DriveToteElevatorCommand(ToteElevatorLevel.FLOOR, true));
		addSequential(new DriveToteElevatorCommand(ToteElevatorLevel.HALF, false));

		addSequential(new DriveDistanceCommand(0.8, 0.0, 270, 42*3, DriveMode.FIELD_RELATIVE));
		addSequential(new DriveToteElevatorCommand(ToteElevatorLevel.FLOOR, false));

		addSequential(new DriveDistanceCommand(0.8, 90, 270, 48, DriveMode.FIELD_RELATIVE));

	}
}