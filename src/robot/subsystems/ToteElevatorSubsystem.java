package robot.subsystems;

import robot.RobotMap;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ToteElevatorSubsystem extends RunnymedeSubsystem {

	public enum Level {
		FLOOR(0), 
		ONE(-1 * RobotMap.ENCODER_COUNTS_PER_ELEVATOR_LEVEL), 
		TWO(-2 * RobotMap.ENCODER_COUNTS_PER_ELEVATOR_LEVEL), 
		THREE(-3 * RobotMap.ENCODER_COUNTS_PER_ELEVATOR_LEVEL), 
		FOUR(-4 * RobotMap.ENCODER_COUNTS_PER_ELEVATOR_LEVEL), 
		FIVE(-5 * RobotMap.ENCODER_COUNTS_PER_ELEVATOR_LEVEL);

		public double encoderSetpoint;

		Level(double encoderSetpoint) {
			this.encoderSetpoint = encoderSetpoint;
		}

	}
	
	double difference = 0.0;
	
	Encoder encoder = new Encoder(RobotMap.ELEVATOR_ENCODER_ONE,
			RobotMap.ELEVATOR_ENCODER_TWO) {
		@Override
		public double pidGet() {
			return this.getRate() / RobotMap.MAX_ELEVATOR_ENCODER_RATE;
		}
	};
	
	Talon elevatorMotor = new Talon(RobotMap.ELEVATOR_MOTOR);
	Solenoid brake = new Solenoid(RobotMap.BRAKE_SOLENOID);

	PIDController elevatorRatePID = new PIDController(0.5, 0.0, 0.0, 0.0004 * RobotMap.MAX_ELEVATOR_ENCODER_RATE, encoder, elevatorMotor);

	public void initDefaultCommand() {
		setDefaultCommand(null);
	}

	public boolean onTarget() {
		return Math.abs(difference) < 100;
	}

	public void driveToLevel(Level level) {
		difference = encoder.getDistance() - level.encoderSetpoint;

		disengageBrake();
		
		if (difference > 0) {
			elevatorRatePID.setSetpoint(-1.0);
		} else {
			elevatorRatePID.setSetpoint(1.0);
		}
		
	}

	private void disengageBrake() {
		brake.set(false);
		elevatorRatePID.enable();
	}

	private void engageBrake() {
		brake.set(true);
		elevatorRatePID.disable();
	}
	
	@Override
	public void disableSubsystem() {
		engageBrake();
		elevatorRatePID.setSetpoint(0.0);
	}

	@Override
	public void enableSubsystem() {
		elevatorRatePID.enable();
		elevatorRatePID.setSetpoint(0.0);
	}

	@Override
	public void initSubsystem() {
		elevatorRatePID.setInputRange(-1.0, 1.0);
		elevatorRatePID.setOutputRange(-1.0, 1.0);
	}

	@Override
	public void updateDashboard() {
		SmartDashboard.putData("Elevator Encoder", encoder);
		SmartDashboard.putData("Elevator PID", elevatorRatePID);
		SmartDashboard.putData("Elevator Talon", elevatorMotor);
	}

}
