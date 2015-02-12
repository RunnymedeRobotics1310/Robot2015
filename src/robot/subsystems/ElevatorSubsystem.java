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

public class ElevatorSubsystem extends RunnymedeSubsystem {

	public enum Levels {
		FLOOR(0), TWO(1 * RobotMap.ENCODER_COUNTS_PER_ELEVATOR_LEVEL), THREE(
				2 * RobotMap.ENCODER_COUNTS_PER_ELEVATOR_LEVEL), FOUR(
				3 * RobotMap.ENCODER_COUNTS_PER_ELEVATOR_LEVEL), FIVE(
				4 * RobotMap.ENCODER_COUNTS_PER_ELEVATOR_LEVEL), SIX(
				5 * RobotMap.ENCODER_COUNTS_PER_ELEVATOR_LEVEL);

		public double encoderSetpoint;

		Levels(double encoderSetpoint) {
			this.encoderSetpoint = encoderSetpoint;
		}

	}

	class MockEncoder implements PIDSource {
		
		Encoder encoder;
		
		MockEncoder(Encoder encoder) {
			this.encoder = encoder;
		}
		
		@Override
		public double pidGet() {
			return encoder.getRate();
		}
	}
	
	double difference = 0.0;
	
	Encoder encoder = new Encoder(RobotMap.ELEVATOR_ENCODER_ONE,
			RobotMap.ELEVATOR_ENCODER_TWO);
	MockEncoder rateEncoder = new MockEncoder(encoder);
	
	Talon elevatorMotor = new Talon(RobotMap.ELEVATOR_MOTOR);
	MockSpeedController distancePIDOutput = new MockSpeedController();
	Solenoid brake = new Solenoid(RobotMap.BRAKE_SOLENOID);

	PIDController elevatorDistancePID = new PIDController(0.1, 0.0, 0.0, 0.0,
			encoder, distancePIDOutput);
	PIDController elevatorRatePID = new PIDController(0.1, 0.0, 0.0, 0.0,
			rateEncoder, elevatorMotor);

	public void initDefaultCommand() {
		setDefaultCommand(null);
	}

	public boolean onTarget() {
		return Math.abs(difference) < 200;
	}

	public void driveToLevel(double setpoint) {
		difference = encoder.getDistance() - setpoint;

		disengageBrake();
		elevatorDistancePID.setSetpoint(setpoint);
		elevatorRatePID.setSetpoint(-0.3 * elevatorDistancePID.get());

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
	}

	@Override
	public void disableSubsystem() {
		engageBrake();
		elevatorDistancePID.disable();
		elevatorRatePID.disable();
	}

	@Override
	public void enableSubsystem() {
	}

	@Override
	public void initSubsystem() {
		elevatorDistancePID.setAbsoluteTolerance(10);
		elevatorDistancePID.setOutputRange(-0.3, 0.3);
		// FIXME 0,0 input range(measure max encoder counts)
		elevatorRatePID.setInputRange(0, 0);
		elevatorRatePID.setOutputRange(-0.3, 0.3);

		encoder.setPIDSourceParameter(PIDSourceParameter.kDistance);
	}

	@Override
	public void updateDashboard() {
		SmartDashboard.putData("Elevator Encoder", encoder);
	}

}
