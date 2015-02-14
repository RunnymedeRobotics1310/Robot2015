package robot.subsystems;

import robot.RobotMap;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ContainerElevatorSubsystem extends RunnymedeSubsystem {

	public enum ContainerElevatorLevel {
		FLOOR(0), 
		TOP(1000);

		public double encoderSetpoint;

		ContainerElevatorLevel(double encoderSetpoint) {
			this.encoderSetpoint = encoderSetpoint;
		}

	}
	
	double difference = 0.0;
	
	Encoder encoder = new Encoder(RobotMap.CONTAINER_ELEVATOR_ENCODER_ONE,
			RobotMap.CONTAINER_ELEVATOR_ENCODER_TWO, true) {
		@Override
		public double pidGet() {
			return this.getRate() / RobotMap.CONTAINER_ELEVATOR_MAX_ELEVATOR_ENCODER_RATE;
		}
	};
	
	Talon elevatorMotor = new Talon(RobotMap.CONTAINER_ELEVATOR_MOTOR);

	PIDController elevatorRatePID = new PIDController(0.8, 0.0, 0.0, 0.002 * RobotMap.CONTAINER_ELEVATOR_MAX_ELEVATOR_ENCODER_RATE, encoder, elevatorMotor);

	public void initDefaultCommand() {
		setDefaultCommand(null);
	}

	public boolean onTarget() {
		return Math.abs(difference) < 25;
	}

	public void driveToLevel(ContainerElevatorLevel level) {
		difference = encoder.getDistance() - level.encoderSetpoint;
		
		if(!elevatorRatePID.isEnable()) {
			elevatorRatePID.enable();
		}
		
		if (difference > 0) {
			elevatorRatePID.setSetpoint(-0.5);
		} else {
			elevatorRatePID.setSetpoint(1.0);
		}
		
	}

	@Override
	public void disableSubsystem() {
		elevatorRatePID.disable();
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
		SmartDashboard.putData("Container Elevator Encoder", encoder);
		SmartDashboard.putData("Container Elevator PID", elevatorRatePID);
		SmartDashboard.putData("Container Elevator Talon", elevatorMotor);
		
		SmartDashboard.putNumber("DEBUG", elevatorRatePID.get());
	}

}
