package robot.commands;

import robot.Robot;
import robot.commands.autonomous.ActuateHolderCommand;
import robot.subsystems.ToteElevatorSubsystem.ToteElevatorLevel;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class ToteElevatorWithoutIntakeCommandGroup extends CommandGroup {
	
	static boolean autoExpired = false;
	
	public static void setAutoExpired(boolean state) {
		autoExpired = state;
	}
	
	public ToteElevatorWithoutIntakeCommandGroup() {
		requires(Robot.sensorSubsystem);
		requires(Robot.passiveContainerArmSubsystem);
		if(!autoExpired) {
			addParallel(new DisableReverseDriveCommand(false));
			addSequential(new DriveToteElevatorCommand(ToteElevatorLevel.AUTO_PICKUP_LEVEL, false, true));
			addSequential(new DriveToteElevatorCommand(ToteElevatorLevel.ONE, false));
			addParallel(new DisableReverseDriveCommand(true));
		} else {
			addSequential(new DriveToteElevatorCommand(ToteElevatorLevel.AUTO_PICKUP_LEVEL, false, false));
			addSequential(new DriveToteElevatorCommand(ToteElevatorLevel.FIVE, false, false));
			addParallel(new ActuateHolderCommand(false));
			addSequential(new DriveToteElevatorCommand(ToteElevatorLevel.ONE, false, false));
			autoExpired = false;
		}
	}
	
}
