
package robot;


import java.util.ArrayList;
import java.util.List;

import robot.commands.ExampleCommand;
import robot.subsystems.ActuatorSubsystem;
import robot.subsystems.ChassisSubsystem;
import robot.subsystems.PowerSubsystem;
import robot.subsystems.RunnymedeSubsystem;
import robot.subsystems.SensorSubsystem;
import robot.subsystems.VisionSubsystem;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	private boolean testRobot = false;
	
	private List<RunnymedeSubsystem> subsystemLs = new ArrayList<RunnymedeSubsystem>();
	
	public static OI oi;

	public static       ActuatorSubsystem actuatorSubsystem;
	public static       SensorSubsystem   sensorSubsystem; 
	public static final VisionSubsystem   visionSubsystem   = new VisionSubsystem();
	public static final PowerSubsystem    powerSubsystem    = new PowerSubsystem();
	public static       ChassisSubsystem  chassisSubsystem;
	
    Command autonomousCommand;
    
    // Default constructor.
    
    public Robot () { 
    	
    	// Initialize the different subsytems depending on the mode because
    	// the subsystems use overlapping outputs which results in the 
    	// robot not being able to be instantiated.  When an I/O Port is used 
    	// in more than one subsystem, the driver station will display an 
    	// unable to instatiate class message.
    	if (testRobot) {
    		actuatorSubsystem = new ActuatorSubsystem();
    		sensorSubsystem   = new SensorSubsystem();
    		chassisSubsystem  = null;
    	} else {
    		actuatorSubsystem = null;
    		sensorSubsystem   = null;
    		chassisSubsystem  = new ChassisSubsystem();
    	}
    }

    @Override
    public void autonomousInit() {
        // schedule the autonomous command (example)
        if (autonomousCommand != null) autonomousCommand.start();
        
        enableSubsystems();
    }
	
	/**
     * This function is called periodically during autonomous
     */
    @Override
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
        updateDashboard();
    }

    /**
     * This function is called when the disabled button is hit.
     * You can use it to reset subsystems before shutting down.
     */
    @Override
    public void disabledInit() {
    	disableSubsystems();
    }

    @Override
    public void disabledPeriodic() {
		Scheduler.getInstance().run();
		updateDashboard();
	}

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    @Override
    public void robotInit() {
		oi = new OI();
		
		if (actuatorSubsystem != null)  { subsystemLs.add(actuatorSubsystem); }
    	if (sensorSubsystem != null)    { subsystemLs.add(sensorSubsystem); }
    	subsystemLs.add(visionSubsystem);
    	subsystemLs.add(powerSubsystem);
    	if (chassisSubsystem != null)   { subsystemLs.add(chassisSubsystem); }
    	
    	// Initialize all subsystems.
    	for (RunnymedeSubsystem subsystem: subsystemLs) {
    		subsystem.initSubsystem();
    	}

    	// instantiate the command used for the autonomous period
        autonomousCommand = new ExampleCommand();
    }

    @Override
    public void teleopInit() {
		// This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to 
        // continue until interrupted by another command, remove
        // this line or comment it out.
        if (autonomousCommand != null) autonomousCommand.cancel();
        
        enableSubsystems();
    }

    /**
     * This function is called periodically during operator control
     */
    @Override
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
        oi.periodic();
        updateDashboard();
    }
    
    /**
     * This function is called periodically during test mode
     */
    @Override
    public void testPeriodic() {
        LiveWindow.run();
    }
    
    private void disableSubsystems() { 
    	for (RunnymedeSubsystem subsystem: subsystemLs) {
    		subsystem.disableSubsystem();
    	}
    }
    
    private void enableSubsystems() { 
    	for (RunnymedeSubsystem subsystem: subsystemLs) {
    		subsystem.enableSubsystem();
    	}
    }

    private void updateDashboard() {
    	for (RunnymedeSubsystem subsystem: subsystemLs) {
    		subsystem.updateDashboard();
    	}
    	oi.updateDashboard();
    }
}
