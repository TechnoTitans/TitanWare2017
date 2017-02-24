package org.usfirst.frc.team1683.robot;

import org.usfirst.frc.team1683.driveTrain.DriveTrain;
import org.usfirst.frc.team1683.driverStation.DriverStation;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;
import org.usfirst.frc.team1683.scoring.Agitator;
import org.usfirst.frc.team1683.scoring.GearScore;
import org.usfirst.frc.team1683.scoring.Intake;
import org.usfirst.frc.team1683.scoring.ScoringMotor;
import org.usfirst.frc.team1683.scoring.Shooter;
import org.usfirst.frc.team1683.scoring.Winch;
import org.usfirst.frc.team1683.vision.LightRing;
import org.usfirst.frc.team1683.vision.PiVisionReader;

public class Controls {
	public static boolean[] toggle = new boolean[11];
	public static boolean[][] joystickCheckToggle = new boolean[3][11];

	DriveTrain drive;
	GearScore gearScore;
	Winch winch;
	Shooter shooter;
	Intake intake;
	Agitator agitator;
	LightRing light;

	boolean frontMode;
	boolean toggleWinch;
	// boolean autoShooter;
	boolean fullPowerMode;
	boolean visionAidedMovement;

	double rSpeed;
	double lSpeed;
	double maxPower;

	double brightness;
	public final double MAX_JOYSTICK_SPEED = 1.0;
	public final double SECOND_JOYSTICK_SPEED = 0.8;

	public Controls(DriveTrain drive, LightRing light, PiVisionReader piReader) {
		this.drive = drive;
		this.light = light;
		
		winch = new Winch(HWR.WINCH1, HWR.WINCH2);
		intake = new Intake(HWR.INTAKE);

		gearScore = new GearScore(drive, 0.2, piReader);

		frontMode = true;
		toggleWinch = false;
		fullPowerMode = false;
		visionAidedMovement = false;

		maxPower = 1.0;
		brightness = 1.0;
		SmartDashboard.prefDouble("brightness", brightness);
	}

	public void run() {
		// drivetrain
		SmartDashboard.sendData("front(intake) or back(gear) mode", frontMode ? "intake" : "gear");
		if (DriverStation.rightStick.getRawButton(HWR.BACK_CONTROL)) {
			frontMode = false;
		} else if (DriverStation.rightStick.getRawButton(HWR.FRONT_CONTROL)) {
			frontMode = true;
		}

		if (checkToggle(HWR.LEFT_JOYSTICK, HWR.TOGGLE_VISION_AID)) {
			visionAidedMovement = !visionAidedMovement;
		}
		SmartDashboard.sendData("Vision Aided", visionAidedMovement);
		if (!visionAidedMovement) {
			gearScore.disable();
			
			if (DriverStation.rightStick.getRawButton(HWR.FULL_POWER))
				maxPower = MAX_JOYSTICK_SPEED;
			else if (DriverStation.rightStick.getRawButton(HWR.SECOND_POWER))
				maxPower = SECOND_JOYSTICK_SPEED;
			else if (DriverStation.leftStick.getRawButton(HWR.ADD_POWER))
				maxPower += 0.01;
			else if (DriverStation.leftStick.getRawButton(HWR.SUBTRACT_POWER))
				maxPower -= 0.01;
			if (maxPower > 1.0)
				maxPower = 1.0;
			
			SmartDashboard.sendData("Drive Power", maxPower);
			if (frontMode) {
				lSpeed = -maxPower * DriverStation.leftStick.getRawAxis(DriverStation.YAxis);
				rSpeed = -maxPower * DriverStation.rightStick.getRawAxis(DriverStation.YAxis);
			} else {
				lSpeed = maxPower * DriverStation.rightStick.getRawAxis(DriverStation.YAxis);
				rSpeed = maxPower * DriverStation.leftStick.getRawAxis(DriverStation.YAxis);
			}

			drive.driveMode(Math.pow(lSpeed, 3), Math.pow(rSpeed, 3));
		}
		else{
			gearScore.enable();
			gearScore.run();
		}
		

		// light ring
		brightness = SmartDashboard.getDouble("brightness");
		SmartDashboard.sendData("Lightring Brightness", brightness);
		light.setBrightness(brightness);

		// intake
		if (DriverStation.auxStick.getRawButton(HWR.TURN_INTAKE)) {
			intake.turnOn();
		} else
			intake.stop();

		// winch
		toggle(HWR.TOGGLE_WINCH, winch);
	}

	/*
	 * Checks if a button is pressed to toggle it. Since teleop is periodic,
	 * needs to remember past button state to toggle
	 * 
	 */
	public static void toggle(int button, ScoringMotor motor) {
		if (checkToggle(HWR.AUX_JOYSTICK, button)) {
			toggle[button - 1] = !toggle[button - 1];
		}
		if (toggle[button - 1]) {
			motor.turnOn();
		} else {
			motor.stop();
		}
	}

	public static boolean checkToggle(int joystick, int button) {
		boolean pressed = false;

		switch (joystick) {
			case HWR.AUX_JOYSTICK:
				pressed = DriverStation.auxStick.getRawButton(button);
				break;
			case HWR.RIGHT_JOYSTICK:
				pressed = DriverStation.rightStick.getRawButton(button);
				break;
			case HWR.LEFT_JOYSTICK:
				pressed = DriverStation.leftStick.getRawButton(button);
				break;
			default:
				break;
		}

		if (pressed && !joystickCheckToggle[joystick][button - 1]) {
			joystickCheckToggle[joystick][button - 1] = true;
			return true;
		} else if (pressed && joystickCheckToggle[joystick][button - 1]) {
			return false;
		} else {
			joystickCheckToggle[joystick][button - 1] = false;
			return false;
		}
	}
}
