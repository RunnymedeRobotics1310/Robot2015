package robot.commands.autonomous.commandgroup;

import robot.commands.DriveDistanceCommand;
import robot.commands.DriveToAngleCommand;
import robot.commands.DriveToteElevatorCommand;
import robot.commands.ResetGyroCommand;
import robot.commands.ToteElevatorCommandGroup;
import robot.commands.autonomous.AutonomousContainerShiftCommand;
import robot.commands.autonomous.AutonomousDeployIntakeCommand;
import robot.commands.autonomous.AutonomousPickupEyebrowsCommand;
import robot.commands.autonomous.AutonomousPickupToteCommand;
import robot.subsystems.ChassisSubsystem.DriveMode;
import robot.subsystems.ToteElevatorSubsystem.ToteElevatorLevel;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutonomousThreeToteStackCommandGroup extends CommandGroup{

	public AutonomousThreeToteStackCommandGroup() {

		addSequential(new ResetGyroCommand(245));

		for(int i = 0; i < 2; i++) {
			addParallel(new DriveToteElevatorCommand(ToteElevatorLevel.HALF, false));
			addSequential(new DriveToAngleCommand(315, DriveMode.FIELD_RELATIVE));
			addSequential(new DriveDistanceCommand(1.0, 270+30, 315, 12, DriveMode.FIELD_RELATIVE));

			addParallel(new DriveToteElevatorCommand(ToteElevatorLevel.ONE, false));
			addSequential(new DriveDistanceCommand(1.0, 270-32, 270, 48, DriveMode.FIELD_RELATIVE));

			addParallel(new DriveDistanceCommand(1.0, 270, 270, 18, DriveMode.FIELD_RELATIVE));
			addSequential(new AutonomousPickupToteCommand());

			addSequential(new DriveToteElevatorCommand(ToteElevatorLevel.FLOOR, true));
		}
		
		addSequential(new DriveToteElevatorCommand(ToteElevatorLevel.HALF, false));
		addSequential(new DriveDistanceCommand(1.0, 0.0, 270, 42*3 - 8, DriveMode.FIELD_RELATIVE));
		addSequential(new DriveToteElevatorCommand(ToteElevatorLevel.FLOOR, false));
		
		addSequential(new DriveDistanceCommand(1.0, 90, 270, 24, DriveMode.FIELD_RELATIVE));

	}
	
}
