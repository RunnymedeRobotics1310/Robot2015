package robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *  This class is used as the basis for all Runnymede subsystems.  It implements
 *  the abstract classes required by the Runnymede robot.
 */
public abstract class RunnymedeSubsystem extends Subsystem {
    
	/**
	 * Disable the subsystem. 
	 * <p>
	 * This method is called to disable the robot subsystem.  It is called when the robot is disabled.
	 */
	public abstract void disableSubsystem();
	
	/**
	 * Enable the subsystem. 
	 * <p>
	 * This method is called to enable the subsystems in Auto and Teleop modes.
	 */
	public abstract void enableSubsystem();
	
	/**
	 * Initialize the subsystem. 
	 * <p>
	 * This method is called from the Robot init after the subsystem instances are created.  Each of the 
	 * subsystems should use this method to initialize and objects that are required for the Smartdashboard.
	 */
	public abstract void initSubsystem();
	
	/**
	 * Update the Smartdashboard values for all sensors, PIDs, and motors used in this subsystem.
	 * 
	 * Any object that implements LiveWindowSendable can use the updateTable() method to perform the 
	 * update.  The LiveWindowSendable objects should call the startLiveWindowMode() in the init() method
	 * of this subsystem.
	 */
	public abstract void updateDashboard();
}

