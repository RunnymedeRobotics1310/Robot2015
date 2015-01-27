package robot.subsystems;

import edu.wpi.first.wpilibj.CameraServer;

/**
 *
 */
public class VisionSubsystem extends RunnymedeSubsystem {
    
	CameraServer server;

    @Override
	public void disableSubsystem() { }

    @Override
	public void enableSubsystem() { }
    
    public void initDefaultCommand() {}

	@Override
    public void initSubsystem() {
    	
	    server = CameraServer.getInstance();
	    server.setQuality(50);
	    server.startAutomaticCapture("cam0");

    }

	@Override
    public void updateDashboard() { }
}

