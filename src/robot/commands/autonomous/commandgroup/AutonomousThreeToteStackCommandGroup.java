package robot.commands.autonomous.commandgroup;

import robot.commands.DriveDistanceCommand;
import robot.commands.DriveToAngleCommand;
import robot.commands.DriveToteElevatorCommand;
import robot.commands.ResetGyroCommand;
import robot.commands.ToteElevatorCommandGroup;
import robot.commands.autonomous.AutonomousContainerShiftCommand;
import robot.commands.autonomous.AutonomousDeployIntakeCommand;
import robot.commands.autonomous.AutonomousPickupToteCommand;
import robot.subsystems.ChassisSubsystem.DriveMode;
import robot.subsystems.ToteElevatorSubsystem.ToteElevatorLevel;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutonomousThreeToteStackCommandGroup extends CommandGroup{

	public AutonomousThreeToteStackCommandGroup() {
		addSequential(new ResetGyroCommand(270));
		
		addSequential(new AutonomousDeployIntakeCommand());
		
		addSequential(new AutonomousPickupToteCommand());
		addSequential(new ToteElevatorCommandGroup());
		
//		addSequential(new DriveDistanceCommand(1.0, 0.0, 270, 40, DriveMode.FIELD_RELATIVE));
//		addSequential(new DriveDistanceCommand(1.0, 270, 270, 81 - 34, DriveMode.FIELD_RELATIVE));
//		addSequential(new DriveDistanceCommand(1.0, 180, 270, 40, DriveMode.FIELD_RELATIVE));
//		addParallel(new DriveDistanceCommand(0.5, 270, 270, 34, DriveMode.FIELD_RELATIVE));
		addParallel(new AutonomousContainerShiftCommand());
		addSequential(new DriveDistanceCommand(0.4, 270, 270-10, 81 - 34, DriveMode.FIELD_RELATIVE));
		addParallel(new DriveDistanceCommand(0.5, 270, 270, 34, DriveMode.FIELD_RELATIVE));
		addSequential(new DriveToteElevatorCommand(ToteElevatorLevel.ONE));
		addSequential(new AutonomousPickupToteCommand());
		
		addSequential(new ToteElevatorCommandGroup());
		
//		addSequential(new DriveDistanceCommand(1.0, 0.0, 270, 40, DriveMode.FIELD_RELATIVE));
//		addSequential(new DriveDistanceCommand(1.0, 270, 270, 81 - 34, DriveMode.FIELD_RELATIVE));
//		addSequential(new DriveDistanceCommand(1.0, 180, 270, 40, DriveMode.FIELD_RELATIVE));
//		addParallel(new DriveDistanceCommand(0.5, 270, 270, 34, DriveMode.FIELD_RELATIVE));
		addParallel(new AutonomousContainerShiftCommand());
		addSequential(new DriveDistanceCommand(0.4, 270, 270-10, 81 - 34, DriveMode.FIELD_RELATIVE));
		addParallel(new DriveDistanceCommand(0.5, 270, 270, 34, DriveMode.FIELD_RELATIVE));
		addSequential(new DriveToteElevatorCommand(ToteElevatorLevel.ONE));
		addSequential(new AutonomousPickupToteCommand());

		addSequential(new DriveToteElevatorCommand(ToteElevatorLevel.FLOOR));
		addParallel(new DriveToteElevatorCommand(ToteElevatorLevel.HALF));
		
		addSequential(new DriveDistanceCommand(1.0, 0.0, 0.0, 42, DriveMode.FIELD_RELATIVE));
	}
	
}
