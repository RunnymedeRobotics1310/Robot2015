package robot.commands;

import robot.CartesianCoordinate;
import robot.PolarCoordinate;
import robot.RobotMap;
import robot.subsystems.ChassisSubsystem.DriveMode;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;

/**
 *
 */
public class DriveToClickCommand extends Command {

	boolean driveDistanceStarted = false;
	
	DriveDistanceCommand driveDistanceCommand;
	
    public DriveToClickCommand(CartesianCoordinate xy) {
    	
    	// Translate the xy coordinate into a distance and an angle to drive to
    	// 32" is the center of the screen 117" is the top of the screen and 11' at the far end
    	
    	// Assume the angle of the screen is 56deg
    	double theta = xy.getX() * 28;
    	while (theta < 0) {
    		theta += 360;
    	}
    	
    	double distance     = 59 * Math.tan(Math.toRadians(49 + 17 * xy.getY())) - 30;
    	
    	double angle = theta;
    	
    	PolarCoordinate p = new PolarCoordinate(.5, theta);
    	
    	driveDistanceCommand = new DriveDistanceCommand(p, angle, distance, DriveMode.ROBOT_RELATIVE);
    	
    }

    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	if (! driveDistanceStarted) {
    		Scheduler.getInstance().add(driveDistanceCommand);
    		driveDistanceStarted = true;
    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return driveDistanceStarted;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
