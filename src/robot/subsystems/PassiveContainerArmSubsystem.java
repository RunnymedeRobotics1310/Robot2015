package robot.subsystems;

import robot.Robot;
import robot.RobotMap;
import robot.Timer;
import robot.Toggle;
import robot.commands.DefaultPassiveContainerCommand;
import robot.subsystems.ToteElevatorSubsystem.ToteElevatorLevel;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class PassiveContainerArmSubsystem extends RunnymedeSubsystem {

	DoubleSolenoid holderSolenoid = new DoubleSolenoid(RobotMap.HOLDER_SOLENOID_PORT_ONE, RobotMap.HOLDER_SOLENOID_PORT_TWO);
	Solenoid armSolenoid = new Solenoid(RobotMap.CONTAINER_ARM_SOLENOID_PORT);
	
	DigitalInput lightSensor = new DigitalInput(RobotMap.PASSIVE_CONTAINER_ARM_TOTE_SENSOR);
	Toggle lightSensorLatch = new Toggle(false);
	Timer pulseTimer = new Timer(1.0);
	
	int numTotes = 0;
	
	public void update(boolean actuateArm, boolean pickupContainer, boolean deployHolder) {
		if(!DriverStation.getInstance().isAutonomous()) {
			ToteElevatorLevel level = Robot.toteElevatorSubsystem.getLevel();

			if ((level == ToteElevatorLevel.FIVE && deployHolder && !Robot.toteElevatorSubsystem.isMoving()) || (deployHolder && !Robot.toteElevatorSubsystem.getUpperLimit())) {
				holderSolenoid.set(DoubleSolenoid.Value.kForward);
			} else if(!deployHolder) {// && ((level == ToteElevatorLevel.FIVE || level == ToteElevatorLevel.FOUR) && !Robot.toteElevatorSubsystem.isMoving())) {
				holderSolenoid.set(DoubleSolenoid.Value.kReverse);
			}

			if(pickupContainer) {
				Robot.oi.overrideContainerArmToggle(true);
				numTotes = 0;
			}
			
			SmartDashboard.putNumber("number of totes", numTotes);
			SmartDashboard.putBoolean("puse timer isExpired", pulseTimer.isExpired());
			
			if(pulseTimer.isExpired() && !(Robot.toteElevatorSubsystem.getLevel() == ToteElevatorLevel.FLOOR && !Robot.toteElevatorSubsystem.isMoving())) {
				armSolenoid.set(Robot.toteIntakeSubsystem.getPassiveContainerSaftey() || (!pickupContainer && actuateArm));
			} else {
				armSolenoid.set(false);
			}
			
		}
	}
	
	public void requestPulse() {
		if(numTotes < 2) {
			numTotes++;
			armSolenoid.set(false);
			pulseTimer.disable();
			pulseTimer.start();
		}
	}
		
		/*
			lightSensorLatch.update(!lightSensor.get());

			if(numTotes < 2 && lightSensorLatch.getState()) {// &&
				//				!(Robot.toteElevatorSubsystem.getLevel() == ToteElevatorLevel.HALF &&
				//				Robot.toteElevatorSubsystem.getUpperLimit())) {
				numTotes++;
				armSolenoid.set(false);
				pulseTimer.disable();
				pulseTimer.start();
				lightSensorLatch.setState(false);
			}

			SmartDashboard.putNumber("number of totes", numTotes);
			SmartDashboard.putBoolean("puse timer isExpired", pulseTimer.isExpired());
			SmartDashboard.putBoolean("light sensor latch", lightSensorLatch.getState());

			if(pulseTimer.isExpired() && (Robot.toteElevatorSubsystem.getLevel() != ToteElevatorLevel.FLOOR)) {
				armSolenoid.set(Robot.toteIntakeSubsystem.getPassiveContainerSaftey() || (!pickupContainer && actuateArm));
			} else if(pulseTimer.isExpired() && lightSensor.get()){
				armSolenoid.set(false);
			}
		}
	}
*/
	
	public void actuateArmSolenoid(boolean state) {
		armSolenoid.set(state);
	}
	
	public void actuateHolderSolenoid(boolean state) {
		
		if((!Robot.toteElevatorSubsystem.isMoving() && Robot.toteElevatorSubsystem.getLevel() == ToteElevatorLevel.FIVE) || (!Robot.toteElevatorSubsystem.getUpperLimit())) {
			if(state) {
				holderSolenoid.set(DoubleSolenoid.Value.kForward);
			}
		} else if(!state) {
			holderSolenoid.set(DoubleSolenoid.Value.kReverse);
		}
	}

	@Override
	public void disableSubsystem() {
	}

	@Override
	public void enableSubsystem() {
	}

	@Override
	public void initSubsystem() {
		pulseTimer.start();
	}

	@Override
	public void updateDashboard() {
	}

	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new DefaultPassiveContainerCommand());
	}

}
