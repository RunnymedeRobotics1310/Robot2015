package robot.commands;

import robot.PolarCoordinate;
import robot.Robot;
import robot.RobotMap;
import robot.subsystems.ChassisSubsystem.DriveMode;
import robot.subsystems.ChassisSubsystem.PIDEnable;
import edu.wpi.first.wpilibj.command.Command;

public class DriveDistanceCommand extends Command {

	PolarCoordinate p;
	double targetAngle;
	double distance;
	DriveMode driveMode;

	public DriveDistanceCommand(double r, double thetaDeg, double targetAngle, double distance, DriveMode driveMode) {
		this(new PolarCoordinate(r, thetaDeg), targetAngle, distance, driveMode);
	}
	
	public DriveDistanceCommand(PolarCoordinate p, double targetAngle, double distance, DriveMode driveMode) {
		requires(Robot.chassisSubsystem);
		this.p = p;
		this.targetAngle = targetAngle;
		this.distance = distance * RobotMap.ENCODER_COUNTS_PER_INCH;
	}

	@Override
	protected void initialize() {
		Robot.chassisSubsystem.resetEncoders();
	}

	@Override
	protected void execute() {
		// Y axis is inverted on controller, the robot drive compensates for this, so a negative is needed here
		//Robot.chassisSubsystem.drive(r * Math.cos(thetaRad), -r * Math.sin(thetaRad), rotation, true);
		Robot.chassisSubsystem.driveDistance(p, targetAngle, distance, driveMode);
		
		
	}

	@Override
	protected boolean isFinished() {
		return Robot.chassisSubsystem.distanceOnTarget() && Robot.chassisSubsystem.angleOnTarget();//(Robot.chassisSubsystem.getEncoderCounts() - distance) >= 0;
	}

	@Override
	protected void end() {
		Robot.chassisSubsystem.disablePIDs();
		Robot.chassisSubsystem.driveJoystick(new PolarCoordinate(), 0.0, DriveMode.ROBOT_RELATIVE, PIDEnable.DISABLED, PIDEnable.DISABLED);
	}

	@Override
	protected void interrupted() {
		Robot.chassisSubsystem.disablePIDs();
		Robot.chassisSubsystem.driveJoystick(new PolarCoordinate(), 0.0, DriveMode.ROBOT_RELATIVE, PIDEnable.DISABLED, PIDEnable.DISABLED);
	}

}
