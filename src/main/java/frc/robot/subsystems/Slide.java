/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.Robot;
import frc.robot.RobotMap.MapKeys;
import frc.robot.commands.SlideCommand;
/**
 * Add your docs here.
 */
public class Slide extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  private WPI_TalonSRX m_slide;
  private double m_current;
  public Slide(){
    
  }

  public void initalize(){

    m_slide = new WPI_TalonSRX(Robot.m_map.getId(MapKeys.SLIDE));
    m_current = 0;//Robot.m_pdp.getCurrent(14);

  } 

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    setDefaultCommand(new SlideCommand());
  }
  
  public void update(double x){
    if (m_current > 10) {
      slideStop();
    } else {
      m_slide.set(ControlMode.PercentOutput, x);
    }
    
  }

  public void slideStop(){
    m_slide.set(0);
  }
  
}
