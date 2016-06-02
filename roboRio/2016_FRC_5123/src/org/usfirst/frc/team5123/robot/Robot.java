package org.usfirst.frc.team5123.robot;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.livewindow.LiveWindow; 

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	RobotDrive myRobot;
	DriverStation ds;
	Joystick mlgStick;
	DigitalOutput red, blue, auto;
	Talon liftyStick;
	int autoLoopCounter, loopCounter, loopCounterTwo;
	double leftY, rightY, lift;
	boolean shoot, receive, extend, retract, toggle, toggleyButton,
	controllyThing0, controllyThing1, controllyThing2, aSwitch, bSwitch, Cswitch;							// **
	Jaguar topShooter, bottomShooter;
	DoubleSolenoid pokeyStick, gearShift;
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
 
    	myRobot = new RobotDrive(0,1);
    	mlgStick = new Joystick(0);
    	liftyStick = new Talon(2);
    	topShooter = new Jaguar(3);
    	bottomShooter = new Jaguar(4);
    	gearShift = new DoubleSolenoid (0,1);
    	pokeyStick = new DoubleSolenoid(2,3);
    	red = new DigitalOutput(0);
    	blue = new DigitalOutput(1);
    	auto = new DigitalOutput(2);
    	
    	ds = DriverStation.getInstance();
    	DriverStation.Alliance color = ds.getAlliance();
    	
    	if (isDisabled()){
    		red.set(false);
    		blue.set(false);
    	}
    	else if(isEnabled()){
	    	if (color == DriverStation.Alliance.Blue){
	    		red.set(false);
	    		blue.set(true);
	    	}
	    	else if (color == DriverStation.Alliance.Red){
	    		red.set(true);
	    		blue.set(false);
	    	}
    	}
    	
    	CameraServer server = CameraServer.getInstance();
    	server.setQuality(50);
    	server.startAutomaticCapture("cam0");
    }
    /**
     * This function is run once each time the robot enters autonomous mode
     */
    public void autonomousInit() {
    	autoLoopCounter = 0;
    	auto.set(true);
    }

    /**operated mode
     */
    public void teleopInit(){
    	loopCounter = 0;
     	toggle = false;
     	auto.set(false);
     	controllyThing0 = true;
     	controllyThing1 = false;
     	controllyThing2 = false;
     	retract = false;
     	extend = false;
    }
    
    public void autonomousPeriodic() {
    	if(autoLoopCounter < 200) //Check if we've completed 200 loops (approximately 4 seconds)
		{
			myRobot.drive(0.5, 0.5); 	// drive forwards half speed
			autoLoopCounter++;
			} else {
			myRobot.drive(0.0, 0.0); 	// stop robot
		}
    }
    /**
     * This function is called once each time the robot enters tele-
    }
    /**
     * This function is called periodically during operator control
     */
    
    public void teleopPeriodic() {
    	ds = DriverStation.getInstance();
    	DriverStation.Alliance color = ds.getAlliance();
    	
    	if (isDisabled()){
    		red.set(false);
    		blue.set(false);
    	}
    	else if(isEnabled()){
	    	if (color == DriverStation.Alliance.Blue){
	    		red.set(false);
	    		blue.set(true);
	    	}
	    	else if (color == DriverStation.Alliance.Red){
	    		red.set(true);
	    		blue.set(false);
	    	}
    	}
    	leftY = mlgStick.getRawAxis(1);
    	rightY = mlgStick.getRawAxis(5);
    	aSwitch = mlgStick.getRawButton(12); 
    	bSwitch = mlgStick.getRawButton(11); 
    	if (controllyThing0 == true){
    		myRobot.tankDrive(leftY, rightY);
    	}
    	else if (controllyThing1 == true){
        	myRobot.tankDrive(leftY - 0.2, rightY - 0.2);
        	}
    	else if (controllyThing2 == true){
    		myRobot.tankDrive(leftY - 0.4, rightY - 0.4);
    	    	}
    	if (aSwitch == true && bSwitch == false){
    		controllyThing1 = true;
    		controllyThing2 = false;
    		controllyThing0 = false;
    	}
    	if (bSwitch == true && aSwitch == false){
    		controllyThing2 = true;
    		controllyThing1 = false;
    		controllyThing0 = false;
    	}
    	if (aSwitch == true && bSwitch == true){
    		controllyThing0 = true;
    		controllyThing1 = false;
    		controllyThing2 = false;
    	}
    	
    	lift = mlgStick.getRawAxis(3) - mlgStick.getRawAxis(2);
    	liftyStick.set(lift);
    	
    	//led.setRaw(255);
    	
    	shoot = mlgStick.getRawButton(1);
    	receive = mlgStick.getRawButton(4);           
    ///	extend = mlgStick.getRawButton(5);
    ///	retract = mlgStick.getRawButton(6);
    	
    	
        if (shoot == true){
        	bottomShooter.set(1);
        	topShooter.set(-1);
        }
        else if (receive == true) { 				
        	bottomShooter.set(-0.75);
         	topShooter.set(-0.75);
        }
        else {
        	bottomShooter.set(0);
        	topShooter.set(0);
        }            
        if (shoot == true){
        	if (loopCounter < 150) {
        		loopCounter++;
        	}
        	else {
        		 pokeyStick.set(DoubleSolenoid.Value.kForward);
        	}
        }
        else if (extend == true) {
        	pokeyStick.set(DoubleSolenoid.Value.kForward);
        }
        else if (receive == true) {
        	pokeyStick.set(DoubleSolenoid.Value.kReverse);
        }
        else if (retract == true){
        	pokeyStick.set(DoubleSolenoid.Value.kReverse);
        }
        else {
        	 pokeyStick.set(DoubleSolenoid.Value.kOff);
        	 loopCounter = 0;
        }
        
        toggleyButton = mlgStick.getRawButton(3);
        
        if (toggleyButton == true){
        	if (toggle == false){
        		gearShift.set(DoubleSolenoid.Value.kForward);
        		//.set(DoubleSolenoid.Value.kForward);
        	}
        	else if (toggle == true){
        		loopCounterTwo=0;
        		gearShift.set(DoubleSolenoid.Value.kReverse);
        		//.set(DoubleSolenoid.Value.kReverse);
        	}
        	else { 
        		gearShift.set(DoubleSolenoid.Value.kOff);
        		///.set(DoubleSolenoid.Value.kOff);
        	}
        	
        }
        else if (toggleyButton == false) {
        	if (toggle == false) {
        		toggle = true;
        	}
        	else if (toggle == true) {
        		toggle = false;
        	}
        }
        
    }	
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    	LiveWindow.run();
    }
    
    public void disabledPeriodic(){
    	ds = DriverStation.getInstance();
    	DriverStation.Alliance color = ds.getAlliance();
    	
    	if (isDisabled()){
    		red.set(false);
    		blue.set(false);
    	}
    	else if(isEnabled()){
	    	if (color == DriverStation.Alliance.Blue){
	    		red.set(false);
	    		blue.set(true);
	    	}
	    	else if (color == DriverStation.Alliance.Red){
	    		red.set(true);
	    		blue.set(false);
	    	}
    	}
    }
}










OR









package org.usfirst.frc.team5123.robot;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

public class RobotV2 extends IterativeRobot{
	RobotDrive myRobot;
	DriverStation ds;
	Joystick mlgStick;
	DigitalOutput red, blue, auto;
	Talon liftyStick;
	int autoLoopCounter, loopCounter, loopCounterTwo;
	double leftY, rightY, lift;
	boolean shoot, receive, extend, retract, toggle, toggleyButton,
	controllyThing0, controllyThing1, controllyThing2, aSwitch, bSwitch, Cswitch;							// **
	Jaguar topShooter, bottomShooter;
	DoubleSolenoid pokeyStick, gearShift;
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
 
    	myRobot = new RobotDrive(0,1);
    	mlgStick = new Joystick(0);
    	liftyStick = new Talon(2);
    	topShooter = new Jaguar(3);
    	bottomShooter = new Jaguar(4);
    	gearShift = new DoubleSolenoid (0,1);
    	pokeyStick = new DoubleSolenoid(2,3);
    	red = new DigitalOutput(0);
    	blue = new DigitalOutput(1);
    	auto = new DigitalOutput(2);
    	
    	ds = DriverStation.getInstance();
    	DriverStation.Alliance color = ds.getAlliance();
    	
    	if (isDisabled()){
    		red.set(false);
    		blue.set(false);
    	}
    	else if(isEnabled()){
	    	if (color == DriverStation.Alliance.Blue){
	    		red.set(false);
	    		blue.set(true);
	    	}
	    	else if (color == DriverStation.Alliance.Red){
	    		red.set(true);
	    		blue.set(false);
	    	}
    	}
    	
    	CameraServer server = CameraServer.getInstance();
    	server.setQuality(50);
    	server.startAutomaticCapture("cam0");
    }
    /**
     * This function is run once each time the robot enters autonomous mode
     */
    public void autonomousInit() {
    	autoLoopCounter = 0;
    	auto.set(true);
    }

    /**operated mode
     */
    public void teleopInit(){
    	loopCounter = 0;
     	toggle = false;
     	auto.set(false);
     	controllyThing0 = true;
     	controllyThing1 = false;
     	controllyThing2 = false;
     	retract = false;
     	extend = false;
    }
    
    public void autonomousPeriodic() {
    	if(autoLoopCounter < 200) //Check if we've completed 200 loops (approximately 4 seconds)
		{
			myRobot.drive(0.5, 0.5); 	// drive forwards half speed
			autoLoopCounter++;
			} else {
			myRobot.drive(0.0, 0.0); 	// stop robot
		}
    }
    /**
     * This function is called once each time the robot enters tele-
    }
    /**
     * This function is called periodically during operator control
     */
    
    public void teleopPeriodic() {
    	ds = DriverStation.getInstance();
    	DriverStation.Alliance color = ds.getAlliance();
    	
    	if (isDisabled()){
    		red.set(false);
    		blue.set(false);
    	}
    	else if(isEnabled()){
	    	if (color == DriverStation.Alliance.Blue){
	    		red.set(false);
	    		blue.set(true);
	    	}
	    	else if (color == DriverStation.Alliance.Red){
	    		red.set(true);
	    		blue.set(false);
	    	}
    	}
    	leftY = mlgStick.getRawAxis(1);
    	rightY = mlgStick.getRawAxis(5);
    	aSwitch = mlgStick.getRawButton(11);   
    	bSwitch = mlgStick.getRawButton(12);   
    	if (controllyThing0 == true){
    		myRobot.tankDrive(leftY, rightY);
    	}
    	else if (controllyThing1 == true){
        	myRobot.tankDrive(leftY - 0.2, rightY - 0.2);
        	}
    	else if (controllyThing2 == true){
    		myRobot.tankDrive(leftY - 0.4, rightY - 0.4);
    	    	}
    	if (aSwitch == true && bSwitch == false){
    		controllyThing1 = true;
    		controllyThing2 = false;
    		controllyThing0 = false;
    	}
    	if (bSwitch == true && aSwitch == false){
    		controllyThing2 = true;
    		controllyThing1 = false;
    		controllyThing0 = false;
    	}
    	if (aSwitch == true && bSwitch == true){
    		controllyThing0 = true;
    		controllyThing1 = false;
    		controllyThing2 = false;
    	}
    	
    	lift = mlgStick.getRawAxis(3) - mlgStick.getRawAxis(2);
    	liftyStick.set(lift);
    	
    	//led.setRaw(255);
    	
    	shoot = mlgStick.getRawButton(1);
    	receive = mlgStick.getRawButton(4);           
    ///	extend = mlgStick.getRawButton(5);
    ///	retract = mlgStick.getRawButton(6);
    	
    	
        if (shoot == true){
        	bottomShooter.set(1);
        	topShooter.set(-1);
        }
        else if (receive == true) { 				
        	bottomShooter.set(-0.75);
         	topShooter.set(-0.75);
        }
        else {
        	bottomShooter.set(0);
        	topShooter.set(0);
        }            
        if (shoot == true){
        	if (loopCounter < 150) {
        		loopCounter++;
        	}
        	else {
        		 pokeyStick.set(DoubleSolenoid.Value.kForward);
        	}
        }
        else if (extend == true) {
        	pokeyStick.set(DoubleSolenoid.Value.kForward);
        }
        else if (receive == true) {
        	pokeyStick.set(DoubleSolenoid.Value.kReverse);
        }
        else if (retract == true){
        	pokeyStick.set(DoubleSolenoid.Value.kReverse);
        }
        else {
        	 pokeyStick.set(DoubleSolenoid.Value.kOff);
        	 loopCounter = 0;
        }
        
        toggleyButton = mlgStick.getRawButton(3);
        
        if (toggleyButton == true){
        	if (toggle == false){
        		gearShift.set(DoubleSolenoid.Value.kForward);
        		//.set(DoubleSolenoid.Value.kForward);
        	}
        	else if (toggle == true){
        		loopCounterTwo=0;
        		gearShift.set(DoubleSolenoid.Value.kReverse);
        		//.set(DoubleSolenoid.Value.kReverse);
        	}
        	else { 
        		gearShift.set(DoubleSolenoid.Value.kOff);
        		///.set(DoubleSolenoid.Value.kOff);
        	}
        	
        }
        else if (toggleyButton == false) {
        	if (toggle == false) {
        		toggle = true;
        	}
        	else if (toggle == true) {
        		toggle = false;
        	}
        }
        
    }	
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    	LiveWindow.run();
    }
    
    public void disabledPeriodic(){
    	ds = DriverStation.getInstance();
    	DriverStation.Alliance color = ds.getAlliance();
    	
    	if (isDisabled()){
    		red.set(false);
    		blue.set(false);
    	}
    	else if(isEnabled()){
	    	if (color == DriverStation.Alliance.Blue){
	    		red.set(false);
	    		blue.set(true);
	    	}
	    	else if (color == DriverStation.Alliance.Red){
	    		red.set(true);
	    		blue.set(false);
	    	}
    	}
    }
}
