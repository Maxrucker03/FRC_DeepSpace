/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.CANSparkMax.IdleMode;
import frc.robot.Robot;
import frc.robot.RobotMap.MapKeys;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc.robot.commands.*;
import edu.wpi.first.wpilibj.SpeedControllerGroup;

/**

* Add your docs here.
 */
public class DriveTrain extends Subsystem {
  //create CAN Talon SRX objects
  public CANSparkMax m_frontleft;
  public CANSparkMax m_backleft;
  public CANSparkMax m_frontright;
  public CANSparkMax m_backright;
  //create drive train and drive train sides objects
  private SpeedControllerGroup m_left;
  private SpeedControllerGroup m_right;
  private DifferentialDrive m_drive;

<<<<<<< HEAD
  private int m_maxAmps = 10;
  private final int TALON_TIMEOUT_MS = 500;
=======
  private int m_maxAmps = 40;
  private double m_rampRate = 0.75; //seconds from 0 to full throttle
>>>>>>> mixedEncAndPT-MaxFINAL



  public DriveTrain(){
<<<<<<< HEAD
    //initialize + set objects created above
    m_frontleft = new WPI_TalonSRX(Robot.m_map.getId(MapKeys.DRIVE_FRONTLEFT));
    m_frontleft.configContinuousCurrentLimit(m_maxAmps,TALON_TIMEOUT_MS);
    m_frontleft.configPeakCurrentLimit(m_maxAmps,TALON_TIMEOUT_MS);
    m_backleft = new WPI_TalonSRX(Robot.m_map.getId(MapKeys.DRIVE_BACKLEFT));
    m_backleft.configContinuousCurrentLimit(m_maxAmps,TALON_TIMEOUT_MS);
    m_backleft.configPeakCurrentLimit(m_maxAmps,TALON_TIMEOUT_MS);
    m_left = new SpeedControllerGroup(m_frontleft, m_backleft);

    

    m_frontright = new WPI_TalonSRX(Robot.m_map.getId(MapKeys.DRIVE_FRONTRIGHT));
    m_frontright.configContinuousCurrentLimit(m_maxAmps,TALON_TIMEOUT_MS);
    m_frontright.configPeakCurrentLimit(m_maxAmps,TALON_TIMEOUT_MS);
    m_backright = new WPI_TalonSRX(Robot.m_map.getId(MapKeys.DRIVE_BACKRIGHT));
    m_backright.configContinuousCurrentLimit(m_maxAmps,TALON_TIMEOUT_MS);
    m_backright.configPeakCurrentLimit(m_maxAmps,TALON_TIMEOUT_MS);
    m_right = new SpeedControllerGroup(m_frontright, m_backright);
    //m_left.setInverted(true); 
=======
    
  }
  public void initialize(){
    int frontLeftCanID = Robot.m_map.getId(MapKeys.DRIVE_FRONTLEFT);
    int backLeftCanID = Robot.m_map.getId(MapKeys.DRIVE_BACKLEFT);
    int frontRightCanID = Robot.m_map.getId(MapKeys.DRIVE_FRONTRIGHT);
    int backRightCanID = Robot.m_map.getId(MapKeys.DRIVE_BACKRIGHT);

    if ((frontLeftCanID != 0) && (backLeftCanID != 0) && (frontRightCanID != 0) && (backRightCanID != 0)){
        m_frontleft = new CANSparkMax(frontLeftCanID, MotorType.kBrushless);
        m_frontleft.setIdleMode(IdleMode.kBrake);
        m_frontleft.setSmartCurrentLimit(m_maxAmps);
        m_frontleft.setOpenLoopRampRate(m_rampRate);
>>>>>>> mixedEncAndPT-MaxFINAL

        m_frontright = new CANSparkMax(frontRightCanID, MotorType.kBrushless);
        m_frontright.setIdleMode(IdleMode.kBrake);
        m_frontright.setSmartCurrentLimit(m_maxAmps);
        m_frontright.setOpenLoopRampRate(m_rampRate);

        m_backleft = new CANSparkMax(backLeftCanID, MotorType.kBrushless);
        m_backleft.setIdleMode(IdleMode.kBrake);
        m_backleft.setSmartCurrentLimit(m_maxAmps);
        m_backleft.setOpenLoopRampRate(m_rampRate);

        m_backright = new CANSparkMax(backRightCanID, MotorType.kBrushless);
        m_backright.setIdleMode(IdleMode.kBrake);
        m_backright.setSmartCurrentLimit(m_maxAmps);
        m_backright.setOpenLoopRampRate(m_rampRate);

        m_right = new SpeedControllerGroup(m_frontright, m_backright);
        m_left = new SpeedControllerGroup(m_frontleft, m_backleft);
        m_drive = new DifferentialDrive(m_left, m_right);
        
    }
  }
  public void update(double y, double z){
    if (m_drive != null){
      m_drive.arcadeDrive(y, z, true);
    }
  }


  @Override
  public void initDefaultCommand() {
    setDefaultCommand(new DriveCommand());
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
}
