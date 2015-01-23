package robot.subsystems;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class VisionSubsystem extends Subsystem {
    
	CameraServer server;

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

	public VisionSubsystem() {

	    server = CameraServer.getInstance();
	    server.setQuality(50);
	    //the camera name (ex "cam0") can be found through the roborio web interface
	    server.startAutomaticCapture("cam0");

	}
	
    public void initDefaultCommand() {
    }

}

