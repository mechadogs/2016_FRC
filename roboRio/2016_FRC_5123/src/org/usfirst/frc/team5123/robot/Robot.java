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
	boolean shoot, receive, extend, retract, toggle, toggleyButton;							// **
	Jaguar topShooter, bottomShooter;
	DoubleSolenoid pokeyStick, gearShift;
	DriverStation.Alliance color;
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
    	color = ds.getAlliance();
    	
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
    	if (color == DriverStation.Alliance.Blue){
	    	red.set(false);
	    	blue.set(true);
    	}
    	else if (color == DriverStation.Alliance.Red){
    		red.set(true);
    		blue.set(false);
    	}
    }

    /**operated mode
     */
    public void teleopInit(){
    	if (color == DriverStation.Alliance.Blue){
	    	red.set(false);
	    	blue.set(true);
    	}
    	else if (color == DriverStation.Alliance.Red){
    		red.set(true);
    		blue.set(false);
    	}
    	loopCounter = 0;
     	toggle = false;
     	auto.set(false);
    }
    
    public void autonomousPeriodic() {
    	if(autoLoopCounter < 100) //Check if we've completed 100 loops (approximately 2 seconds)
		{
			myRobot.drive(-0.5, 0.0); 	// drive forwards half speed
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
    	leftY = mlgStick.getRawAxis(1);
    	rightY = mlgStick.getRawAxis(5);
    	myRobot.tankDrive(leftY, rightY);
    	
    	lift = mlgStick.getRawAxis(3) - mlgStick.getRawAxis(2);
    	liftyStick.set(lift);
    	
    	//led.setRaw(255);
    	
    	shoot = mlgStick.getRawButton(1);
    	receive = mlgStick.getRawButton(4);           
    	extend = mlgStick.getRawButton(5);
    	retract = mlgStick.getRawButton(6);
    	
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
    
    public void disabledInit() {
    	red.set(false);
		blue.set(false);
    }
}
