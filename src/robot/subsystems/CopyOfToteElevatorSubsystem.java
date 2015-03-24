package robot.subsystems;

import robot.RobotMap;
import robot.SafeTalon;
import robot.commands.DefaultToteElevatorCommand;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class CopyOfToteElevatorSubsystem extends RunnymedeSubsystem {

	public enum ToteElevatorLevel {
		FLOOR (0),
		ARM_LEVEL(-RobotMap.TOTE_ELEVATOR_ENCODER_COUNTS_AT_FIRST_LEVEL/3),
		HALF (-RobotMap.TOTE_ELEVATOR_ENCODER_COUNTS_AT_FIRST_LEVEL/2),
		ONE   (-RobotMap.TOTE_ELEVATOR_ENCODER_COUNTS_AT_FIRST_LEVEL), 
		TWO   (-RobotMap.TOTE_ELEVATOR_ENCODER_COUNTS_AT_FIRST_LEVEL
				+ (1 * -RobotMap.TOTE_ELEVATOR_ENCODER_COUNTS_PER_ELEVATOR_LEVEL)),
		AUTONOMOUS_CONTAINER_LEVEL((-RobotMap.TOTE_ELEVATOR_ENCODER_COUNTS_AT_FIRST_LEVEL
				+ (1 * -RobotMap.TOTE_ELEVATOR_ENCODER_COUNTS_PER_ELEVATOR_LEVEL)) - 200),
		THREE (-RobotMap.TOTE_ELEVATOR_ENCODER_COUNTS_AT_FIRST_LEVEL
				+ (2 * -RobotMap.TOTE_ELEVATOR_ENCODER_COUNTS_PER_ELEVATOR_LEVEL)), 
		FOUR  ( -RobotMap.TOTE_ELEVATOR_ENCODER_COUNTS_AT_FIRST_LEVEL
				+ 3 * -RobotMap.TOTE_ELEVATOR_ENCODER_COUNTS_PER_ELEVATOR_LEVEL),
		FIVE  ( -RobotMap.TOTE_ELEVATOR_ENCODER_COUNTS_AT_FIRST_LEVEL
				+ 4 * -RobotMap.TOTE_ELEVATOR_ENCODER_COUNTS_PER_ELEVATOR_LEVEL);

		public double encoderSetpoint;

		ToteElevatorLevel(double encoderSetpoint) {
			this.encoderSetpoint = encoderSetpoint;
		}

	}

	public enum ElevatorMode {
		AUTOMATIC,
		MANUAL;
	}
	
	public boolean armState = false;

	ElevatorMode mode = ElevatorMode.AUTOMATIC;
	double difference = 0.0;
	ToteElevatorLevel level = null;
	double elevatorRatePIDSetpoint = 0.0d;
	boolean enabled = false;

	Encoder encoder = new Encoder(RobotMap.TOTE_ELEVATOR_ENCODER_ONE,
			RobotMap.TOTE_ELEVATOR_ENCODER_TWO) {
		@Override
		public double pidGet() {
			return this.getRate()
					/ RobotMap.TOTE_ELEVATOR_MAX_ELEVATOR_ENCODER_RATE;
		}
	};

	SafeTalon elevatorMotor = new SafeTalon(RobotMap.TOTE_ELEVATOR_MOTOR);
	Solenoid brake = new Solenoid(RobotMap.BRAKE_SOLENOID);
	Solenoid arm = new Solenoid(RobotMap.ELEVATOR_ARM_SOLENOID_PORT);

	DigitalInput floorSensor = new DigitalInput(RobotMap.TOTE_ELEVATOR_LOWER_LIMIT_SWITCH);

	PIDController elevatorRatePID = new PIDController(0.8, 0.0, 0.0,
			0.0004 * RobotMap.TOTE_ELEVATOR_MAX_ELEVATOR_ENCODER_RATE, encoder,
			elevatorMotor);

	public CopyOfToteElevatorSubsystem() {
		// Add the safety elements to the elevator talon
		// Since negative power drives the motor up, the negative limit switch is the elevator upper limit switch
		elevatorMotor.setNegativeLimitSwitch(new DigitalInput(RobotMap.TOTE_ELEVATOR_UPPER_LIMIT_SWITCH));
		elevatorMotor.setPositiveLimitSwitch(floorSensor);
		elevatorMotor.setOverCurrentFuse(RobotMap.TOTE_ELEVATOR_POWER_DISTRIBUTION_PORT, SafeTalon.CURRENT_NO_LIMIT, 0);
	}

	public void initDefaultCommand() {
		setDefaultCommand(new DefaultToteElevatorCommand());
	}

	public boolean onTarget() {


		double difference = encoder.getDistance() - level.encoderSetpoint;

		// Drive down until the floor sensor is activated when floor is pressed
		if (level == ToteElevatorLevel.FLOOR) {
			// The floor sensor is normally closed, so the elevator has hit the limit when the switch is open
			if (!floorSensor.get()) {
				resetEncoders();
			}
			return !floorSensor.get();
		}  else {
			if (   (elevatorRatePIDSetpoint > 0 && difference > -65)
					|| (elevatorRatePIDSetpoint < 0 && difference < 65)) {
				return true;
			}
		}

		if (   (elevatorMotor.getState() == SafeTalon.TalonState.POSITIVE_LIMIT_SWITCH && level == ToteElevatorLevel.FLOOR) 
				|| (elevatorMotor.getState() == SafeTalon.TalonState.NEGATIVE_LIMIT_SWITCH && level == ToteElevatorLevel.FIVE)) {
			return true;
		}

		return false;
	}

	public ToteElevatorLevel getLevel() { return level; }

	public void driveToLevel() {
		if(mode == ElevatorMode.AUTOMATIC) {
			disengageBrake();
			elevatorRatePID.setSetpoint(elevatorRatePIDSetpoint);
		}
	}

	public void initDriveToLevel(ToteElevatorLevel level) {

		mode = ElevatorMode.AUTOMATIC;

		double difference = encoder.getDistance() - level.encoderSetpoint;

		double driveSpeed = 0;

		if(DriverStation.getInstance().isAutonomous()) {
			driveSpeed = 1.0;
		} else if(DriverStation.getInstance().isOperatorControl()) {
			driveSpeed = 0.75;
		}

		if (difference > 0) {
			elevatorRatePIDSetpoint = -driveSpeed;
		} else {
			elevatorRatePIDSetpoint = driveSpeed*0.75;
		}

		this.level = level;

		enableSubsystem();
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
		enabled = false;
		engageBrake();
		elevatorRatePID.setSetpoint(0.0);
	}

	@Override
	public void enableSubsystem() {
		enabled = true;
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
		SmartDashboard.putData("Tote Elevator Encoder", encoder);
		SmartDashboard.putData("Tote Elevator PID", elevatorRatePID);
		SmartDashboard.putData("Tote Elevator Talon", elevatorMotor);
		
		SmartDashboard.putNumber("Tote Elevator PIDGet()", encoder.pidGet());

		elevatorMotor.updateTable();
	}

	public void resetEncoders() {
		encoder.reset();
	}

	public double getEncoderDistance() {
		return encoder.getDistance();
	}

	public boolean isEnabled() {
		return enabled;
	}

	public ElevatorMode getElevatorMode() {
		return mode;
	}

	public void override(double rawAxis) {
		mode = ElevatorMode.MANUAL;
		if(Math.abs(rawAxis) > 0.05) {
			disengageBrake();
			elevatorMotor.set(rawAxis);
		} else {
			elevatorMotor.set(0.0);
			engageBrake();
		}
	}

	public void setArm(boolean state) {
		if(level != ToteElevatorLevel.FLOOR) {
			arm.set(state);
			armState = state;
		} else {
			arm.set(false);
			armState = false;
		}
	}
	
	public boolean isMoving() {
		if (this.getCurrentCommand() instanceof DefaultToteElevatorCommand) {
			return false;
		} else {
			return true;
		}
	}

}
