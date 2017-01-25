package org.usfirst.frc.team1683.test;
import edu.wpi.first.wpilibj.Solenoid;
import org.usfirst.frc.team1683.robot.HWR;
public class SolenoidTest {
	Solenoid solenoid;
	public SolenoidTest(){
		solenoid = new Solenoid(HWR.DEFAULT_MODULE_CHANNEL, HWR.GEAR_PISTON_CHANNEL);
	}
	public void test(){
		solenoid.set(true);
	}
}
