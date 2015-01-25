package robot.subsystems;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class PowerSubsystem extends RunnymedeSubsystem {
	
	PowerDistributionPanel powerDistributionPanel = new PowerDistributionPanel();
	
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
	// @Override
    public void initSubsystem() {
    	
    	// Add all sensors and motors to the LiveWindow
    	LiveWindow.addSensor  ("PowerDistributionPanel", "Current", powerDistributionPanel);
    	
    	// Start Live Window for all sensors, PIDS and motors that implement LiveWindowSendable
    	SmartDashboard.putData("PowerDistributionPanel", powerDistributionPanel);
        
    }
    
    /**
     * Get the current on any channel on the Power Distribution Panel.  
     * @param channel - the channel number on the Power Distribution Panel.  
     * @return double - the current on the channel or 0 if the channel number is invalid.
     */
    public double getCurrent(int channel) {
    	
    	if (channel >= 0 && channel < PowerDistributionPanel.kPDPChannels) {
    		return powerDistributionPanel.getCurrent(channel);
    	}
    	
    	System.out.println("Invalid channel number (" + channel + ") requested for PowerSubsystem.getCurrent()");
    	return 0;
    }

    // @Override
    public void updateDashboard() {

    	powerDistributionPanel.updateTable();
    	
    }

}

