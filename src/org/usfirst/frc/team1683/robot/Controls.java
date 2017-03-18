package org.usfirst.frc.team1683.robot;

import org.usfirst.frc.team1683.driveTrain.DriveTrain;
import org.usfirst.frc.team1683.driverStation.DriveStation;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;
import org.usfirst.frc.team1683.scoring.GearScore;
import org.usfirst.frc.team1683.scoring.Intake;
import org.usfirst.frc.team1683.scoring.ScoringMotor;
import org.usfirst.frc.team1683.scoring.Winch;
import org.usfirst.frc.team1683.vision.LightRing;
import org.usfirst.frc.team1683.vision.PiVisionReader;

/**
 * 
 * @author Yi Liu
 * 
 */
public class Controls {
	public static boolean[] toggle = new boolean[11];
	public static boolean[][] joystickCheckToggle = new boolean[3][11];

	DriveTrain drive;
	GearScore gearScore;
	Winch winch;
	Intake intake;
	LightRing light;
	PiVisionReader piReader;

	boolean frontMode = true;
	boolean toggleWinch = false;
	boolean visionAidedMovement = false;

	double rSpeed;
	double lSpeed;
	double maxPower = 1.0;

	public final double MAX_JOYSTICK_SPEED = 1.0;
	public final double SECOND_JOYSTICK_SPEED = 0.8;
	
	public InputFilter rightFilter, leftFilter;

	private double p = 0.74;
	private double i = 0.0;
	private double d = 0.0;

	public Controls(DriveTrain drive, LightRing light, PiVisionReader piReader) {
		this.drive = drive;
		this.light = light;
		this.piReader = piReader;

		SmartDashboard.prefDouble("ap", p);
		SmartDashboard.prefDouble("ai", i);
		SmartDashboard.prefDouble("ad", d);

		winch = new Winch(HWR.WINCH1, HWR.WINCH2);
		intake = new Intake(HWR.INTAKE);

		gearScore = new GearScore(drive, 0.2, piReader, p, i, d, "Cont");
		
		rightFilter = new InputFilter(0.86);
		leftFilter = new InputFilter(0.86);
	}

	public void run() {
		// drivetrain
		SmartDashboard.sendData("Front(intake) or Back(gear) mode", frontMode ? "intake" : "gear", true);
		if (DriveStation.rightStick.getRawButton(HWR.BACK_CONTROL))
			frontMode = false;
		else if (DriveStation.rightStick.getRawButton(HWR.FRONT_CONTROL))
			frontMode = true;

		if (checkToggle(HWR.LEFT_JOYSTICK, HWR.TOGGLE_VISION_AID)) {
			visionAidedMovement = !visionAidedMovement;
		}

		SmartDashboard.sendData("Vision Aided", visionAidedMovement, true);
		if (!visionAidedMovement) {
			if (gearScore != null) {
				gearScore.disable();
				gearScore = null;
				getPID();
			}

			SmartDashboard.sendData("Drive Power", maxPower, true);
			if (frontMode) {
				lSpeed = -maxPower * DriveStation.leftStick.getRawAxis(DriveStation.YAxis);
				rSpeed = -maxPower * DriveStation.rightStick.getRawAxis(DriveStation.YAxis);
			} else {
				lSpeed = maxPower * DriveStation.rightStick.getRawAxis(DriveStation.YAxis);
				rSpeed = maxPower * DriveStation.leftStick.getRawAxis(DriveStation.YAxis);
			}
			
			if(maxPower == MAX_JOYSTICK_SPEED){
				lSpeed = leftFilter.filterInput(Math.pow(lSpeed, 3));
				rSpeed = rightFilter.filterInput(Math.pow(rSpeed, 3));
			}
			else if(maxPower == SECOND_JOYSTICK_SPEED){
				lSpeed = leftFilter.filterInput(lSpeed);
				rSpeed = rightFilter.filterInput(rSpeed);
			}
			
			if (DriveStation.rightStick.getRawButton(HWR.FULL_POWER))
				maxPower = MAX_JOYSTICK_SPEED;
			else if (DriveStation.leftStick.getRawButton(HWR.SECOND_POWER))
				maxPower = SECOND_JOYSTICK_SPEED;
			
			drive.driveMode(lSpeed, rSpeed);
		} else {
			if (gearScore == null)
				gearScore = new GearScore(drive, 0.2, piReader, p, i, d, "Cont");

			gearScore.enable();
			gearScore.run();
		}

		// intake
		if (DriveStation.auxStick.getRawButton(HWR.TURN_INTAKE)) {
			intake.turnOn();
		} else
			intake.stop();

		// winch
		toggleMotor(HWR.MAIN_WINCH, winch);
	}

	public void getPID() {
		p = SmartDashboard.getDouble("ap");
		i = SmartDashboard.getDouble("ai");
		d = SmartDashboard.getDouble("ad");
	}

	/*
	 * Checks if a button is pressed to toggle it. Since teleop is periodic,
	 * needs to remember past button state to toggle
	 * 
	 */
	public static void toggleMotor(int button, ScoringMotor motor) {
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
				pressed = DriveStation.auxStick.getRawButton(button);
				break;
			case HWR.RIGHT_JOYSTICK:
				pressed = DriveStation.rightStick.getRawButton(button);
				break;
			case HWR.LEFT_JOYSTICK:
				pressed = DriveStation.leftStick.getRawButton(button);
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
	
	@SuppressWarnings("unused")
	private boolean returnToggle(int joystick, int button){
		return joystickCheckToggle[joystick][button - 1];
	}
}
