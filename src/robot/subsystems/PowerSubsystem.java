package robot.subsystems;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
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
    
	@Override
	public void initSubsystem() {}
    
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
    
    public void updateDashboard() {
    	
    	// Update the dashboard with the current on all channels of the Power Distribution Panel.
    	for (int i=0; i < PowerDistributionPanel.kPDPChannels; i++) {
        	SmartDashboard.putNumber("PDP output current [" + i + "]", powerDistributionPanel.getCurrent(i));
    	}
    	
    }

}

