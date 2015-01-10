
package robot.commands;

import robot.Robot;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ExampleCommand extends Command {

    public ExampleCommand() {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.exampleSubsystem);
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }
}
