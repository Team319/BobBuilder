package org.usfirst.frc.team319.robot.subsystems;

import java.util.ArrayList;

import org.usfirst.frc.team319.models.BobTalonSRX;
import org.usfirst.frc.team319.models.DriveSignal;
import org.usfirst.frc.team319.models.LeaderBobTalonSRX;
import org.usfirst.frc.team319.models.SRXGains;
import org.usfirst.frc.team319.robot.commands.drivetrain.BobDrive;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.RemoteSensorSource;
import com.ctre.phoenix.motorcontrol.StatusFrame;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.sensors.PigeonIMU;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Drivetrain extends Subsystem {

	private boolean isHighGear = true;

	public static int LOW_GEAR_PROFILE = 2;
	public static int HIGH_GEAR_PROFILE = 0;
	public static int ROTATION_PROFILE = 1;


	private SRXGains lowGearGains = new SRXGains(LOW_GEAR_PROFILE, 0.0, 0.0, 0.0, 0.0, 0); //
	private SRXGains highGearGains = new SRXGains(HIGH_GEAR_PROFILE, 0.0, 0.0, 0.0, 0.0, 0); // 
	private SRXGains rotationGains = new SRXGains(ROTATION_PROFILE, 0.0, 0.00, 0.0, 0.0, 0); // 

	private BobTalonSRX leftFollower = new BobTalonSRX(0);
	private BobTalonSRX rightFollower = new BobTalonSRX(0);
	public LeaderBobTalonSRX leftLead = new LeaderBobTalonSRX(0, leftFollower); // 8
	public LeaderBobTalonSRX rightLead = new LeaderBobTalonSRX(0, rightFollower); // 1

	private PigeonIMU pigeon = new PigeonIMU(leftFollower);

	public Drivetrain() {

		// These Values will be different for every Robot :)
		leftLead.setInverted(true);
		leftLead.configPrimaryFeedbackDevice(FeedbackDevice.CTRE_MagEncoder_Relative);
		leftLead.setSensorPhase(false);

		rightLead.setInverted(false);
		rightLead.configPrimaryFeedbackDevice(FeedbackDevice.CTRE_MagEncoder_Relative);
		rightLead.setSensorPhase(false);

		leftLead.enableCurrentLimit(false);
		leftLead.configContinuousCurrentLimit(0);
		rightLead.enableCurrentLimit(false);
		rightLead.configContinuousCurrentLimit(0);

		leftLead.configOpenloopRamp(0.0);
		rightLead.configOpenloopRamp(0.0);

		setNeutralMode(NeutralMode.Coast);

		configGains(highGearGains);
		configGains(lowGearGains);
		configGains(rotationGains);

		// configure distance sensor
		// Remote 0 will be the other side's Talon
		rightLead.configRemoteSensor0(leftLead.getDeviceID(), RemoteSensorSource.TalonSRX_SelectedSensor);
		rightLead.configSensorSum(FeedbackDevice.RemoteSensor0, FeedbackDevice.CTRE_MagEncoder_Relative);
		rightLead.configPrimaryFeedbackDevice(FeedbackDevice.SensorSum, 0.5); // distances from left and right are
																				// summed, so average them
		rightLead.configMaxIntegralAccumulator(ROTATION_PROFILE, 0);

		// configure angle sensor
		// Remote 1 will be a pigeon
		rightLead.configRemoteSensor1(leftFollower.getDeviceID(), RemoteSensorSource.GadgeteerPigeon_Yaw);
		rightLead.configSecondaryFeedbackDevice(FeedbackDevice.RemoteSensor1, (0.0 / 0.0)); // Coefficient for
																									// Pigeon to

		// convert to 360
		leftLead.setStatusFramePeriod(StatusFrame.Status_2_Feedback0, 5, 0);
		rightLead.configAuxPIDPolarity(false, 0);
	}

	public void initDefaultCommand() {
		setDefaultCommand(new BobDrive());
	}

	public void configGains(SRXGains gains) {
		this.leftLead.setGains(gains);
		this.rightLead.setGains(gains);
	}

	public void drive(ControlMode controlMode, double left, double right) {
		this.leftLead.set(controlMode, left);
		this.rightLead.set(controlMode, right);
	} 

	public void drive(ControlMode controlMode, DriveSignal driveSignal) {
		this.drive(controlMode, driveSignal.getLeft(), driveSignal.getRight());
	}

	public double getLeftDriveLeadDistance() {
		return this.leftLead.getSelectedSensorPosition();
	}

	public double getRightDriveLeadDistance() {
		return this.rightLead.getSelectedSensorPosition();
	}

	public double getLeftDriveLeadVelocity() {
		return this.leftLead.getSelectedSensorVelocity();
	}

	public double getRightDriveLeadVelocity() {
		return this.rightLead.getSelectedSensorVelocity();
	}

	public void setDrivetrainPositionToZero() {
		this.leftLead.setSelectedSensorPosition(0);
		this.rightLead.setSelectedSensorPosition(0);
	}

	public double getLeftLeadVoltage() {
		return this.leftLead.getMotorOutputVoltage();
	}

	public double getRightLeadVoltage() {
		return this.rightLead.getMotorOutputVoltage();
	}

	public double getLeftClosedLoopError() {
		return this.leftLead.getClosedLoopError();
	}

	public double getRightClosedLoopError() {
		return this.rightLead.getClosedLoopError();
	}

	public TalonSRX getLeftLeadTalon() {
		return this.getLeftLeadTalon();
	}

	public TalonSRX getRightLeadTalon() {
		return this.rightLead;
	}

	public void setNeutralMode(NeutralMode neutralMode) {
		this.leftLead.setNeutralMode(neutralMode);
		this.rightLead.setNeutralMode(neutralMode);
	}

	public boolean isHighGear() {
		return isHighGear;
	}

	public void setIsHighGear(boolean isHighGear) {
		this.isHighGear = isHighGear;
	}

	public double getAngle() {
		double[] ypr = new double[3];
		pigeon.getYawPitchRoll(ypr);
		return ypr[0];
	}

	public double getDistance() {
		return rightLead.getPrimarySensorPosition();
	}

	public double getVelocity() {
		return rightLead.getPrimarySensorVelocity();
	}

	@Override
	public void periodic() {
		SmartDashboard.putBoolean("Drivetrain High Gear", isHighGear);
		SmartDashboard.putNumber("Drivetrain Angle", getAngle());
		SmartDashboard.putNumber("Angle Error", rightLead.getClosedLoopError(1));
		SmartDashboard.putNumber("Drivetrain Velocity", getVelocity());
		SmartDashboard.putNumber("Drivetrain Distance", getDistance());
		SmartDashboard.putNumber("Left Lead Current", leftLead.getOutputCurrent());
		SmartDashboard.putNumber("Left Follower Current", leftFollower.getOutputCurrent());
	}
}
