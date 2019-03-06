package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.Robot;
import frc.robot.RobotMap.MapKeys;
import frc.robot.commands.LiftCommand;



/**
 * Add your docs here.
 */
public class LiftSubsystem extends Subsystem {
  public WPI_TalonSRX m_motorup1;
  public WPI_TalonSRX m_motorup2;
  private SpeedControllerGroup m_lift;
  private double m_current1;
  private double m_current2;

  public LiftSubsystem(){
     
  }

  public void initialize(){

    m_motorup1 = new WPI_TalonSRX(Robot.m_map.getId(MapKeys.LIFT_LEFT));
    m_motorup1.set(ControlMode.PercentOutput, 0.0);
    m_motorup2 = new WPI_TalonSRX(Robot.m_map.getId(MapKeys.LIFT_RIGHT));
    m_motorup2.set(ControlMode.PercentOutput,0.0);
  
    m_lift = new SpeedControllerGroup(m_motorup1, m_motorup2);
    
  }


  // Put methods for controlling this subsystem
  // here. Call these from Commands.

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    setDefaultCommand(new LiftCommand());
     
  }

  public void update(double x) {
    if (m_motorup1 !=null && m_motorup2 !=null) {
      m_lift.set(x);
    }
    
  }

  
} 
