package robot.commands.autonomous.commandgroup;

import robot.commands.DriveDistanceCommand;
import robot.commands.DriveToAngleCommand;
import robot.commands.DriveToteElevatorCommand;
import robot.commands.ResetGyroCommand;
import robot.commands.autonomous.AutonomousDelayCommand;
import robot.commands.autonomous.AutonomousPickupToteCommand;
import robot.subsystems.ChassisSubsystem.DriveMode;
import robot.subsystems.ToteElevatorSubsystem.ToteElevatorLevel;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutonomousTestCommandGroup extends CommandGroup {

	public AutonomousTestCommandGroup() {

		addSequential(new ResetGyroCommand(270));

		for(int i = 0; i < 2; i++) {
			addParallel(new DriveToteElevatorCommand(ToteElevatorLevel.HALF));
			addSequential(new DriveToAngleCommand(315, DriveMode.FIELD_RELATIVE));
			addSequential(new DriveDistanceCommand(1.0, 270+30, 315, 12, DriveMode.FIELD_RELATIVE));

			addParallel(new DriveToteElevatorCommand(ToteElevatorLevel.ONE));
			addSequential(new DriveDistanceCommand(1.0, 270-32.5, 270, 40, DriveMode.FIELD_RELATIVE));

			addParallel(new DriveDistanceCommand(1.0, 270, 270, 18, DriveMode.FIELD_RELATIVE));
			addSequential(new AutonomousPickupToteCommand());

			addSequential(new DriveToteElevatorCommand(ToteElevatorLevel.FLOOR));
		}
		
		addSequential(new DriveToteElevatorCommand(ToteElevatorLevel.HALF));
		addParallel(new DriveDistanceCommand(1.0, 0.0, 0.0, 42, DriveMode.FIELD_RELATIVE));
		addSequential(new AutonomousDelayCommand(250));
		addSequential(new DriveToteElevatorCommand(ToteElevatorLevel.FLOOR));

		
		//		addSequential(new ResetGyroCommand(0));
		//		addSequential(new DriveDistanceCommand(.4, 90, 0.0, 36, DriveMode.FIELD_RELATIVE));
		//		addSequential(new DriveDistanceCommand(.4, 0, 0.0, 36, DriveMode.FIELD_RELATIVE));
		//		addSequential(new DriveDistanceCommand(.4, 270, 0.0, 36, DriveMode.FIELD_RELATIVE));
		//		addSequential(new DriveDistanceCommand(.4, 180, 0.0, 36, DriveMode.FIELD_RELATIVE));
	}

}