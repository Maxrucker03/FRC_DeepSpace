/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import frc.robot.Robot;
import frc.robot.RobotMap.MapKeys;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
/**
 * Add your docs here.
 */
public class GrabberArmSubsystem extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  private DoubleSolenoid m_grabberArm;
  private int  PCM_ARMCANID = 0;
  public boolean armGrabberLED;
  public GrabberArmSubsystem() {
    
  }
  public void initialize(){
    PCM_ARMCANID = Robot.m_map.getId(MapKeys.PCM_CLIMBCANID2);
    if (PCM_ARMCANID != 0){
      m_grabberArm = new DoubleSolenoid(
        Robot.m_map.getId(MapKeys.PCM_CLIMBCANID2),
        Robot.m_map.getId(MapKeys.SOLENOID_ARMFORWARD),
        Robot.m_map.getId(MapKeys.SOLENOID_ARMREVERSE)
        );
      m_grabberArm.set(DoubleSolenoid.Value.kOff);
      armGrabberLED = false;
    }
  }

   public void armTurnOff() {
     if (m_grabberArm != null){
      m_grabberArm.set(DoubleSolenoid.Value.kOff);
    }
    armGrabberLED = false;
  }
  public void armExtend() {
    if (m_grabberArm != null){
      m_grabberArm.set(DoubleSolenoid.Value.kForward);
    }
    armGrabberLED= true;
  }
  public void armRetract() {
    if (m_grabberArm != null){
      m_grabberArm.set(DoubleSolenoid.Value.kReverse);
    }
    armGrabberLED = true;
  }

  @Override
  protected void initDefaultCommand() {

  }
} 

