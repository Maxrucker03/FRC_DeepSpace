/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;


import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Slide;
import frc.robot.subsystems.ClimbSubsystem;
import frc.robot.subsystems.LedSubsystem;

import edu.wpi.first.cameraserver.CameraServer;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoSink;
import edu.wpi.cscore.VideoMode.PixelFormat;

import frc.robot.subsystems.*;
/*
	
	JAGBOTS 2019 DEEP SPACE CODE
	
 */



/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  public static OI m_oi;
  public static RobotMap m_map = new RobotMap();
  public static PowerDistributionPanel m_pdp = new PowerDistributionPanel();
  public static PressureSensor m_pressursensor = new PressureSensor(0);
  public static LedSubsystem m_ledSubsystem = new LedSubsystem();
  public static ClimbSubsystem m_climbSubsystem = new ClimbSubsystem();
  public static GrabberHandSubsystem m_grabberHand = new GrabberHandSubsystem();
  public static GrabberArmSubsystem m_grabberArm = new GrabberArmSubsystem();
  public static DriveTrain m_drivetrain = new DriveTrain();
  public static LiftSubsystem m_lift = new LiftSubsystem();
  public static Slide m_slide = new Slide();

  

 Command m_autonomousCommand;
 SendableChooser<Command> m_chooser = new SendableChooser<>();

  UsbCamera camera0;  
  UsbCamera camera1;
  VideoSink vidSink;
  boolean previousButton = false;
  int currentCamera = 1;


  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    m_oi = new OI();
    m_drivetrain.initialize();
    m_climbSubsystem.initialize();
    m_lift.initialize();
    m_grabberArm.initialize();
    m_grabberHand.initialize();
    m_slide.initialize();
    // m_chooser.setDefaultOption("Default Auto", new GrabberOff()); 
    // chooser.addOption("My Auto", new MyAutoCommand());
    
    

    camera0 = CameraServer.getInstance().startAutomaticCapture(0);
    camera0.setVideoMode(PixelFormat.kMJPEG, 460,340, 15);

    
    camera1 = CameraServer.getInstance().startAutomaticCapture(1);
    camera1.setVideoMode(PixelFormat.kMJPEG, 460,340, 15);
   
    vidSink = CameraServer.getInstance().getServer();

    m_ledSubsystem.LED(true);

  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    SmartDashboard.putData(Scheduler.getInstance());
    SmartDashboard.putNumber("PSI", Robot.m_pressursensor.getAirPressurePsi());
    Robot.m_ledSubsystem.blindCommand();
  }

  /**
   * This function is called once each time the robot enters Disabled mode.
   * You can use it to reset any subsystem information you want to clear when
   * the robot is disabled.
   */
  @Override
  public void disabledInit() {
    
  }

  @Override
  public void disabledPeriodic() {
    Scheduler.getInstance().run();
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString code to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional commands to the
   * chooser code above (like the commented example) or additional comparisons
   * to the switch structure below with additional strings & commands.
   */
  @Override
  public void autonomousInit() {
    m_autonomousCommand = m_chooser.getSelected();

    /*
     * String autoSelected = SmartDashboard.getString("Auto Selector",
     * "Default"); switch(autoSelected) { case "My Auto": autonomousCommand
     * = new MyAutoCommand(); break; case "Default Auto": default:
     * autonomousCommand = new SolenoidCommand(); break; }
     */

    // schedule the autonomous command (example)
    if (m_autonomousCommand != null) {
      m_autonomousCommand.start();
    }
    //m_lift.autonomousInit();
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    Scheduler.getInstance().run();

    boolean currentButton = m_oi.joy.getRawButton(m_oi.CAMERA_BUTTON) == true;
    if ((!currentButton) && previousButton) {
      if (currentCamera == 0) {
        vidSink.setSource(camera1);
        System.out.println(1);
        currentCamera = 1;
      } else {
        vidSink.setSource(camera0);
        System.out.println(0);
        currentCamera = 0;
      }
    }
    previousButton = currentButton;
    
  }

  @Override
  public void teleopInit() {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
    //m_lift.teleopInit();
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    Scheduler.getInstance().run();

    boolean currentButton = m_oi.joy.getRawButton(m_oi.CAMERA_BUTTON) == true;
    if ((!currentButton) && previousButton) {
      if (currentCamera == 0) {
        vidSink.setSource(camera1);
        System.out.println(1);
        currentCamera = 1;
      } else {
        vidSink.setSource(camera0);
        System.out.println(0);
        currentCamera = 0;
      }
    }
    previousButton = currentButton;
    
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }

}
