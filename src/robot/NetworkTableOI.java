package robot;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class NetworkTableOI {

	private static final String MOUSE_WINDOW_TABLE     = "mouseWindow";
	private static final String MOUSE_WINDOW_X         = "mouseX";
	private static final String MOUSE_WINDOW_Y         = "mouseY";
	private static final String MOUSE_WINDOW_NEW_EVENT = "newMouseEvent";
	
	NetworkTable mouseWindow;
	
	NetworkTableOI() {

		mouseWindow = NetworkTable.getTable(MOUSE_WINDOW_TABLE);
		
		// Initialize the table variables
		mouseWindow.putBoolean(MOUSE_WINDOW_NEW_EVENT, false);
		mouseWindow.putNumber(MOUSE_WINDOW_X, 0.0);
		mouseWindow.putNumber(MOUSE_WINDOW_Y, 0.0);
	}
	
	public boolean isNewMouseEvent() {
		
		return mouseWindow.getBoolean(MOUSE_WINDOW_NEW_EVENT);
	}
	
	public CartesianCoordinate getMouseEvent() {
		
		// Return the coordinate
		CartesianCoordinate xy = new CartesianCoordinate();
		xy.setX(mouseWindow.getNumber(MOUSE_WINDOW_X));
		xy.setY(mouseWindow.getNumber(MOUSE_WINDOW_Y));
		
		// Set the coordinate to false to indicate to the 
		// drivers that the robot has accepted this command.
		mouseWindow.putBoolean(MOUSE_WINDOW_NEW_EVENT, false);
		
		return xy;
	}
	
	public void updateDashboard() {
		
		// Return the coordinate
		CartesianCoordinate xy = new CartesianCoordinate();
		xy.setX(mouseWindow.getNumber(MOUSE_WINDOW_X));
		xy.setY(mouseWindow.getNumber(MOUSE_WINDOW_Y));
		
		SmartDashboard.putString("Network OI", 
				"mouse" + xy + " " + isNewMouseEvent()); 

	}
}
