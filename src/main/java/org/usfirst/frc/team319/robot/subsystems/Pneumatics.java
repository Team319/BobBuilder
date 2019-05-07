package org.usfirst.frc.team319.robot.subsystems;

import org.usfirst.frc.team319.models.RobotMode;
import org.usfirst.frc.team319.robot.Robot;
import org.usfirst.frc.team319.robot.commands.pneumatics.CompressorRun;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Pneumatics extends Subsystem {

	private Compressor compressor = new Compressor(0);

	private DoubleSolenoid sampleSolenoid = new DoubleSolenoid(0, 1);

	public void initDefaultCommand() {
		setDefaultCommand(new CompressorRun());
	}

	public void compressorRun() {
		if (Robot.mode == RobotMode.Climb) {
			compressor.setClosedLoopControl(false);
			compressor.stop();
		} else {
			compressor.setClosedLoopControl(true);
		}
	}

	public void sampleSolenoidExtend() {
		this.sampleSolenoid.set(DoubleSolenoid.Value.kForward);
		Robot.sampleSubsystem.setSampleSolenoidExtended(true);
	}

	public void sampleSOlenoidRetract() {
		this.sampleSolenoid.set(DoubleSolenoid.Value.kReverse);
		Robot.sampleSubsystem.setSampleSolenoidExtended(false);
	}
}
