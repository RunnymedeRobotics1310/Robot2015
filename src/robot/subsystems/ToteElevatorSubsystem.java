package robot.subsystems;

import robot.MockSpeedController;
import robot.RobotMap;
import robot.SafeTalon;
import robot.commands.DefaultToteElevatorCommand;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ToteElevatorSubsystem extends RunnymedeSubsystem {

	public enum ToteElevatorLevel {
		FLOOR (0),
		AUTO_PICKUP_LEVEL(0),
		ARM_LEVEL(-RobotMap.TOTE_ELEVATOR_ENCODER_COUNTS_AT_FIRST_LEVEL/3),
		HALF (-RobotMap.TOTE_ELEVATOR_ENCODER_COUNTS_AT_FIRST_LEVEL/3),
		ONE   (-RobotMap.TOTE_ELEVATOR_ENCODER_COUNTS_AT_FIRST_LEVEL), 
		TWO   (-RobotMap.TOTE_ELEVATOR_ENCODER_COUNTS_AT_FIRST_LEVEL
				+ (1 * -RobotMap.TOTE_ELEVATOR_ENCODER_COUNTS_PER_ELEVATOR_LEVEL)),
		AUTONOMOUS_CONTAINER_LEVEL((-RobotMap.TOTE_ELEVATOR_ENCODER_COUNTS_AT_FIRST_LEVEL
				+ (1 * -RobotMap.TOTE_ELEVATOR_ENCODER_COUNTS_PER_ELEVATOR_LEVEL)) - 200),
		THREE (-RobotMap.TOTE_ELEVATOR_ENCODER_COUNTS_AT_FIRST_LEVEL
				+ (2 * -RobotMap.TOTE_ELEVATOR_ENCODER_COUNTS_PER_ELEVATOR_LEVEL)), 
		FOUR  ( -RobotMap.TOTE_ELEVATOR_ENCODER_COUNTS_AT_FIRST_LEVEL
				+ 3 * -RobotMap.TOTE_ELEVATOR_ENCODER_COUNTS_PER_ELEVATOR_LEVEL),
		FIVE  ( -2750);

		public double encoderSetpoint;

		ToteElevatorLevel(double encoderSetpoint) {
			this.encoderSetpoint = encoderSetpoint;
		}

	}
	
	int direction = -1;
	double driveSpeed = 0;

	public enum ElevatorMode {
		AUTOMATIC,
		MANUAL;
	}
	
	public boolean armState = false;

	ElevatorMode mode = ElevatorMode.AUTOMATIC;
	double difference = 0.0;
	ToteElevatorLevel level = ToteElevatorLevel.FLOOR;
	ToteElevatorLevel prevLevel = ToteElevatorLevel.FLOOR;
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
	DigitalInput upperSensor = new DigitalInput(RobotMap.TOTE_ELEVATOR_UPPER_LIMIT_SWITCH);

	PIDController elevatorRatePID = new PIDController(0.8, 0.0, 0.0,
			0.0004 * RobotMap.TOTE_ELEVATOR_MAX_ELEVATOR_ENCODER_RATE, encoder,
			elevatorMotor);
	
	MockSpeedController distancePIDOutput = new MockSpeedController();
	
	PIDController elevatorDistanceRampDownPID = new PIDController(0.002, 0.0, 0.0, 0.0, new PIDSource() {
		@Override
		public double pidGet() {
			return encoder.getDistance();
		}
	}, distancePIDOutput);
	PIDController elevatorDistanceRampUpPID = new PIDController(0.002, 0.0, 0.0, 0.0, new PIDSource() {
		@Override
		public double pidGet() {
			return encoder.getDistance();
		}
	}, distancePIDOutput);

	public ToteElevatorSubsystem() {
		// Add the safety elements to the elevator talon
		// Since negative power drives the motor up, the negative limit switch is the elevator upper limit switch
		elevatorMotor.setNegativeLimitSwitch(upperSensor);
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
			if (   (direction == 1 && difference > -65)
					|| (direction == -1 && difference < 65)) {
				return true;
			}
		}

		if (   (elevatorMotor.getState() == SafeTalon.TalonState.POSITIVE_LIMIT_SWITCH) 
				|| (elevatorMotor.getState() == SafeTalon.TalonState.NEGATIVE_LIMIT_SWITCH)) {
			return true;
		}

		return false;
	}

	public ToteElevatorLevel getLevel() { return level; }

	public void driveToLevel() {
		if(mode == ElevatorMode.AUTOMATIC) {
			disengageBrake();
			elevatorRatePIDSetpoint = Math.sqrt(Math.abs(elevatorDistanceRampDownPID.get()) * Math.abs(elevatorDistanceRampUpPID.get()));
			
			if(Math.abs(elevatorRatePIDSetpoint) < 0.4) {
				elevatorRatePIDSetpoint = 0.4;
			}
			
			if(direction == 1) {
				elevatorDistanceRampDownPID.setPID(0.002, 0.0, 0.0);
				elevatorDistanceRampUpPID.setPID(0.002, 0.0, 0.0);
				elevatorRatePID.setSetpoint(direction * elevatorRatePIDSetpoint * driveSpeed);
			} else {
				elevatorDistanceRampDownPID.setPID(0.002, 0.0, 0.0);
				elevatorDistanceRampUpPID.setPID(0.00225, 0.0, 0.0);
				elevatorRatePID.setSetpoint(direction * elevatorRatePIDSetpoint * driveSpeed);
			}
		}
	}

	public void initDriveToLevel(ToteElevatorLevel level) {

		mode = ElevatorMode.AUTOMATIC;

		double difference = encoder.getDistance() - level.encoderSetpoint;

		driveSpeed = 0;

		if(DriverStation.getInstance().isAutonomous()) {
			driveSpeed = 1.0;
		} else if(DriverStation.getInstance().isOperatorControl()) {
			driveSpeed = 1.0;
		}

		if (difference > 0) {
			direction = -1;
		} else {
			direction = 1;
		}
		this.prevLevel = this.level;
		this.level = level;
		elevatorDistanceRampDownPID.setSetpoint(level.encoderSetpoint);

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
		elevatorDistanceRampDownPID.setSetpoint(encoder.getDistance());
		elevatorDistanceRampUpPID.setSetpoint(encoder.getDistance());
	}

	@Override
	public void enableSubsystem() {
		enabled = true;
		elevatorRatePID.enable();
		elevatorRatePID.setSetpoint(0.0);
		
		elevatorDistanceRampDownPID.enable();
		elevatorDistanceRampUpPID.enable();
	}

	@Override
	public void initSubsystem() {
		elevatorRatePID.setInputRange(-1.0, 1.0);
		elevatorRatePID.setOutputRange(-1.0, 1.0);

		elevatorDistanceRampDownPID.setOutputRange(-1.0, 1.0);
		elevatorDistanceRampUpPID.setOutputRange(-1.0, 1.0);
	}

	@Override
	public void updateDashboard() {
		SmartDashboard.putData("Tote Elevator Encoder", encoder);
		SmartDashboard.putData("Tote Elevator PID", elevatorRatePID);
		SmartDashboard.putData("Tote Elevator Talon", elevatorMotor);
		
		SmartDashboard.putData("Tote Elevator Distance Ramp Down PID", elevatorDistanceRampDownPID);
		SmartDashboard.putData("Tote Elevator Distance Ramp Up PID", elevatorDistanceRampUpPID);
		
		SmartDashboard.putNumber("DistanceRampDownPID Output", elevatorDistanceRampDownPID.get());
		SmartDashboard.putNumber("DistanceRampUpPID Output", elevatorDistanceRampUpPID.get());
		
		SmartDashboard.putNumber("Tote Elevator PIDGet()", encoder.pidGet());
		
		SmartDashboard.putString("TOTE ELEVATOR COMMAND", this.getCurrentCommand().toString());
				
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
			elevatorRatePID.setSetpoint(rawAxis);
		} else {
			elevatorRatePID.setSetpoint(0.0);
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
	
	public ToteElevatorLevel getPrevLevel() {
		return prevLevel;
	}
	
	public boolean isMoving() {
		if (this.getCurrentCommand() instanceof DefaultToteElevatorCommand) {
			return false;
		} else {
			return true;
		}
	}

	public boolean getUpperLimit() {
		return upperSensor.get();
	}

}
