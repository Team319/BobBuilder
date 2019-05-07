/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team319.robot;

import org.usfirst.frc.team319.models.RobotMode;
import org.usfirst.frc.team319.robot.subsystems.Drivetrain;
import org.usfirst.frc.team319.robot.subsystems.Limelight;
import org.usfirst.frc.team319.robot.subsystems.Pneumatics;
import org.usfirst.frc.team319.robot.subsystems.SampleSubsystem;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in the
 * project.
 */

public class Robot extends TimedRobot {

	Command autonomousCommand;
	public static final SampleSubsystem sampleSubsystem = new SampleSubsystem();
	public static final Limelight limelight = new Limelight();
	public static final Pneumatics pneumatics = new Pneumatics();
	public static final Drivetrain drivetrain = new Drivetrain();

	public static OI oi;
	public static RobotMode mode = RobotMode.Normal;

	// SendableChooser<Command> m_chooser = new SendableChooser<>();

	@Override
	public void robotInit() {
		oi = new OI();
		limelight.setStreamType();// sets secondary as the main camera feed.
		Robot.drivetrain.setDrivetrainPositionToZero();

		// CameraServer.getInstance().startAutomaticCapture();
	}

	@Override
	public void disabledInit() {

	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
		limelight.setStreamType();// sets secondary as the main camera feed.
		limelight.setLedModeOff();
	}

	@Override
	public void autonomousInit() {
		limelight.setStreamType();
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
		limelight.setStreamType();// sets secondary as the main camera feed.

	}

	@Override
	public void teleopInit() {

		if (autonomousCommand != null) {
			autonomousCommand.cancel();
		}
		limelight.setStreamType();// sets secondary as the main camera feed.
	}

	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
		limelight.setStreamType();// sets secondary as the main camera feed.

	}

	@Override
	public void testPeriodic() {
	}

}
