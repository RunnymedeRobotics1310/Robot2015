package robot.subsystems;

import robot.MockSpeedController;
import robot.RobotMap;
import robot.commands.DriveContainerElevatorCommand;
import robot.commands.TeleopContainerElevatorCommand;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ContainerElevatorSubsystem extends RunnymedeSubsystem {

	public enum ContainerElevatorLevel {
		FLOOR(0), 
		ONE(190),
		TWO(270),
		THREE(330),
		FOUR(390),
		FIVE(470),
		SIX(570);

		public double encoderSetpoint;

		ContainerElevatorLevel(double encoderSetpoint) {
			this.encoderSetpoint = encoderSetpoint;
		}

	}
	
	double difference = 0.0;
	ContainerElevatorLevel level = ContainerElevatorLevel.FLOOR;
	DigitalInput containerSensor = new DigitalInput(RobotMap.CONTAINER_SENSOR_PORT);
	Talon intakeMotorLeft = new Talon(RobotMap.LEFT_CONTAINER_MOTOR_PORT);
	Talon intakeMotorRight = new Talon(RobotMap.RIGHT_CONTAINER_MOTOR_PORT);
	Solenoid containerClamp = new Solenoid(RobotMap.CONTAINER_PINCHER_PORT);
	DoubleSolenoid containerDeploy = new DoubleSolenoid(RobotMap.CONTAINER_DEPLOY_PORT_ONE, RobotMap.CONTAINER_DEPLOY_PORT_TWO);
	MockSpeedController holdLevelPIDOutput = new MockSpeedController();
	
	Encoder encoder = new Encoder(RobotMap.CONTAINER_ELEVATOR_ENCODER_ONE,
			RobotMap.CONTAINER_ELEVATOR_ENCODER_TWO, true);
	
	Talon elevatorMotor = new Talon(RobotMap.CONTAINER_ELEVATOR_MOTOR);

	PIDController elevatorRatePID = new PIDController(0.4, 0.0, 0.0, 0.002 * RobotMap.CONTAINER_ELEVATOR_MAX_ELEVATOR_ENCODER_RATE,
		new PIDSource() {
			@Override
			public double pidGet() {
				return encoder.getRate() / RobotMap.CONTAINER_ELEVATOR_MAX_ELEVATOR_ENCODER_RATE;
			}
	}, elevatorMotor);
	
	PIDController holdLevelPID = new PIDController(0.1, 0.0, 0.0, 0.0, new PIDSource() {
		public double pidGet() {
			return encoder.getDistance();
		}
	}, holdLevelPIDOutput);

	
	public void initDefaultCommand() {
		setDefaultCommand(new TeleopContainerElevatorCommand());
	}

	public boolean onTarget() {
		return Math.abs(difference) < 7.5;
	}

	public void driveToLevel(ContainerElevatorLevel level) {
		difference = encoder.getDistance() - level.encoderSetpoint;
		
		enableRatePID();
		disableHoldLevelPID();
		
		if (difference > 0) {
			elevatorRatePID.setSetpoint(-1.0);
		} else {
			elevatorRatePID.setSetpoint(1.0);
		}
		
		this.level = level;
		
	}
	private void enableRatePID() {
		if(!elevatorRatePID.isEnable()) {
			elevatorRatePID.enable();
		}
	}
	private void enableHoldLevelPID() {
		if(!holdLevelPID.isEnable()) {
			holdLevelPID.enable();
		}
	}
	private void disableHoldLevelPID() {
		if(holdLevelPID.isEnable()) {
			holdLevelPID.disable();
		}
	}
	private void disableRatePID() {
		if(elevatorRatePID.isEnable()) {
			elevatorRatePID.disable();
		}
	}

	public void holdLevel() {

		enableHoldLevelPID();
		enableRatePID();
		
		holdLevelPID.setSetpoint(level.encoderSetpoint);
		elevatorRatePID.setSetpoint(holdLevelPIDOutput.get());
		
	}
	
	public void updatePickup(boolean clampState, boolean deployState) {
		if(clampState && containerSensor.get()) {
			intakeMotorLeft.set(-0.5);
			intakeMotorRight.set(0.5);
		} else {
			intakeMotorLeft.set(0.0);
			intakeMotorRight.set(0.0);
		}
		
		if(deployState) {
			if(level != ContainerElevatorLevel.TWO) {
				Scheduler.getInstance().add(new DriveContainerElevatorCommand(ContainerElevatorLevel.TWO));
			} else {
				containerDeploy.set(DoubleSolenoid.Value.kForward);
			}
			} else {
			containerDeploy.set(DoubleSolenoid.Value.kReverse);
		}
		
		containerClamp.set(!clampState);
	}

	@Override
	public void disableSubsystem() {
		disableRatePID();
		elevatorRatePID.setSetpoint(0.0);
	}

	@Override
	public void enableSubsystem() {
		enableRatePID();
		elevatorRatePID.setSetpoint(0.0);
	}

	@Override
	public void initSubsystem() {
		elevatorRatePID.setInputRange(-1.0, 1.0);
		elevatorRatePID.setOutputRange(-1.0, 1.0);
		
		holdLevelPID.setOutputRange(-1.0, 1.0);
	}

	@Override
	public void updateDashboard() {
		SmartDashboard.putData("Container Elevator Encoder", encoder);
		SmartDashboard.putData("Container Elevator PID", elevatorRatePID);
		SmartDashboard.putData("Container Elevator Talon", elevatorMotor);
		
		SmartDashboard.putData("Hold Level PID", holdLevelPID);

	}

}
