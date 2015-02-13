package robot.subsystems;

import robot.MockSpeedController;
import robot.RobotMap;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSource.PIDSourceParameter;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ToteElevatorSubsystem extends RunnymedeSubsystem {

	public enum Level {
		FLOOR(0), 
		TWO(1 * RobotMap.ENCODER_COUNTS_PER_ELEVATOR_LEVEL), 
		THREE(2 * RobotMap.ENCODER_COUNTS_PER_ELEVATOR_LEVEL), 
		FOUR(3 * RobotMap.ENCODER_COUNTS_PER_ELEVATOR_LEVEL), 
		FIVE(4 * RobotMap.ENCODER_COUNTS_PER_ELEVATOR_LEVEL), 
		SIX(5 * RobotMap.ENCODER_COUNTS_PER_ELEVATOR_LEVEL);

		public double encoderSetpoint;

		Level(double encoderSetpoint) {
			this.encoderSetpoint = encoderSetpoint;
		}

	}
	
	double difference = 0.0;
	
	Encoder encoder = new Encoder(RobotMap.ELEVATOR_ENCODER_ONE,
			RobotMap.ELEVATOR_ENCODER_TWO);
	
	Talon elevatorMotor = new Talon(RobotMap.ELEVATOR_MOTOR);
	Solenoid brake = new Solenoid(RobotMap.BRAKE_SOLENOID);

	PIDController elevatorRatePID = new PIDController(0.1, 0.0, 0.0, 0.0, encoder, elevatorMotor);

	public void initDefaultCommand() {
		setDefaultCommand(null);
	}

	public boolean onTarget() {
		return Math.abs(difference) < 200;
	}

	public void driveToLevel(Level level) {
		difference = encoder.getDistance() - level.encoderSetpoint;

		disengageBrake();
		elevatorRatePID.setSetpoint(-0.3);

		// direction if statement
		// else if (difference < 0) {
		// elevatorMotor.set(0.3);
		// } else {
		// elevatorMotor.set(-0.3);
		// }
	}

	private void disengageBrake() {
		brake.set(true);
	}

	private void engageBrake() {
		brake.set(false);
		elevatorRatePID.disable();
	}
	
	@Override
	public void disableSubsystem() {
		engageBrake();
		elevatorRatePID.disable();
	}

	@Override
	public void enableSubsystem() {
		elevatorRatePID.enable();
	}

	@Override
	public void initSubsystem() {
		// FIXME 0,0 input range(measure max encoder counts)
		elevatorRatePID.setInputRange(-100, 100);
		elevatorRatePID.setOutputRange(-0.3, 0.3);

		encoder.setPIDSourceParameter(PIDSourceParameter.kRate);
	}

	@Override
	public void updateDashboard() {
		SmartDashboard.putData("Elevator Encoder", encoder);
	}

}
