/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.Robot;
import frc.robot.RobotMap.MapKeys;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import java.util.EnumMap;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;




//Add your docs here.
 
public class ClimbSubsystem extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.

  enum ClimbState {
    IDLE,
    DESCEND_S1, DESCEND_S2, DESCEND_S3, DESCEND_S4, DESCEND_S5,
    CLIMB_L2_S0, CLIMB_L2_S1, CLIMB_L2_S2, CLIMB_L2_S3, CLIMB_L2_S4, CLIMB_L2_S5, CLIMB_L2_S6,
    CLIMB_L3_S1A, CLIMB_L3_S1B,CLIMB_L3_S2, CLIMB_L3_S3, CLIMB_L3_S4, CLIMB_L3_S5,
  }
  private ClimbState m_climbState = ClimbState.IDLE;

  private static final EnumMap<ClimbState, ClimbState> nextStageMap = new EnumMap<>(ClimbState.class);
  private static final EnumMap<ClimbState, ClimbState> prevStageMap = new EnumMap<>(ClimbState.class);

  private double m_timeLeft_sec;

  
  //private DoubleSolenoid Solenoid_1;
  //private DoubleSolenoid Solenoid_2;
  //private DoubleSolenoid Solenoid_3;
  //private DoubleSolenoid Solenoid_4;
  private DoubleSolenoid Solenoid_5;
  private DoubleSolenoid Solenoid_6;
  private DoubleSolenoid Solenoid_7;
  private DoubleSolenoid Solenoid_8;

  WPI_TalonSRX frontleftwheel;
  WPI_TalonSRX frontrightwheel;
  public SpeedControllerGroup climbwheels;

  public double m_mainDrive;

  public double m_auxDrive;

  public int m_LEDRedValue;

  public int m_LEDBlueValue;

  public int m_LEDGreenValue;
  
  public boolean m_autoDescend;
  public boolean m_autoL2Ascend;
  public boolean m_autoL3Ascend;

  private boolean m_configured = false;

  private double m_velocity;

  private double m_DriveTime;

  private int m_tickcounter;
  
  
  public CANSparkMax m_climbfront;
  public CANSparkMax m_climbback;
  private CANPIDController m_pidControllerFront;
  private CANPIDController m_pidControllerBack;
  private CANEncoder m_encoderFront;
  private CANEncoder m_encoderBack;
  public double kP, kI, kD, kIz, kFF, kMaxOutput, kMinOutput;
  private static final double GEAR_RATIO = 20;
  private static final double GEAR_DIAM = 2.0;
  public static final double ROTATIONS_PER_INCH = GEAR_RATIO/(Math.PI*GEAR_DIAM); // 2 inch pulley TODO: check this
  private int m_maxAmps = 20;
  private final double L3_POS_IN = 20; // inches for L3 Climb 
  private final double L2_POS_IN = 8;  // inches for L2 Climb
  private final boolean m_closedloop = true;
  private final double SPEED_IDLE_IPS = 0.0;
  private final double SPEED_EXTEND_FRONT_IPS = 10.0;
  private final double SPEED_RETRACT_FRONT_IPS = 10.0;
  private final double SPEED_EXTEND_BACK_IPS = 10.0;
  private final double SPEED_RETRACT_BACK_IPS = 10.0;
  private double m_frontPos_in = 0.0;
  private double m_backPos_in = 0.0;
  private double m_fronttargetPos_in = 0.0;
  private double m_backtargetPos_in = 0.0;
  
  
  


  public ClimbSubsystem() {
  
    nextStageMap.put(ClimbState.IDLE, ClimbState.IDLE);

    nextStageMap.put(ClimbState.DESCEND_S1, ClimbState.DESCEND_S2);
    nextStageMap.put(ClimbState.DESCEND_S2, ClimbState.DESCEND_S3);
    nextStageMap.put(ClimbState.DESCEND_S3, ClimbState.DESCEND_S4);
    nextStageMap.put(ClimbState.DESCEND_S4, ClimbState.DESCEND_S5);
    nextStageMap.put(ClimbState.DESCEND_S5, ClimbState.IDLE);

    nextStageMap.put(ClimbState.CLIMB_L2_S0, ClimbState.CLIMB_L2_S1);
    nextStageMap.put(ClimbState.CLIMB_L2_S1, ClimbState.CLIMB_L2_S2);
    nextStageMap.put(ClimbState.CLIMB_L2_S2, ClimbState.CLIMB_L2_S3);
    nextStageMap.put(ClimbState.CLIMB_L2_S3, ClimbState.CLIMB_L2_S4);
    nextStageMap.put(ClimbState.CLIMB_L2_S4, ClimbState.CLIMB_L2_S5);
    nextStageMap.put(ClimbState.CLIMB_L2_S5, ClimbState.CLIMB_L2_S6);
    nextStageMap.put(ClimbState.CLIMB_L2_S6, ClimbState.IDLE);

    nextStageMap.put(ClimbState.CLIMB_L3_S1A, ClimbState.CLIMB_L3_S1B);
    nextStageMap.put(ClimbState.CLIMB_L3_S1B, ClimbState.CLIMB_L3_S2);
    nextStageMap.put(ClimbState.CLIMB_L3_S2, ClimbState.CLIMB_L3_S3);
    nextStageMap.put(ClimbState.CLIMB_L3_S3, ClimbState.CLIMB_L3_S4);
    nextStageMap.put(ClimbState.CLIMB_L3_S4, ClimbState.CLIMB_L3_S5);
    nextStageMap.put(ClimbState.CLIMB_L3_S5, ClimbState.IDLE);

    prevStageMap.put(ClimbState.IDLE, ClimbState.IDLE);

    prevStageMap.put(ClimbState.DESCEND_S1, ClimbState.IDLE);
    prevStageMap.put(ClimbState.DESCEND_S2, ClimbState.DESCEND_S1);
    prevStageMap.put(ClimbState.DESCEND_S3, ClimbState.DESCEND_S2);
    prevStageMap.put(ClimbState.DESCEND_S4, ClimbState.DESCEND_S3);
    prevStageMap.put(ClimbState.DESCEND_S5, ClimbState.DESCEND_S4);

    prevStageMap.put(ClimbState.CLIMB_L2_S0, ClimbState.IDLE);
    prevStageMap.put(ClimbState.CLIMB_L2_S1, ClimbState.CLIMB_L2_S0);
    prevStageMap.put(ClimbState.CLIMB_L2_S2, ClimbState.CLIMB_L2_S1);
    prevStageMap.put(ClimbState.CLIMB_L2_S3, ClimbState.CLIMB_L2_S2);
    prevStageMap.put(ClimbState.CLIMB_L2_S4, ClimbState.CLIMB_L2_S3);
    prevStageMap.put(ClimbState.CLIMB_L2_S5, ClimbState.CLIMB_L2_S4);
    prevStageMap.put(ClimbState.CLIMB_L2_S6, ClimbState.CLIMB_L2_S5);

    prevStageMap.put(ClimbState.CLIMB_L3_S1A, ClimbState.IDLE);
    prevStageMap.put(ClimbState.CLIMB_L3_S1B, ClimbState.CLIMB_L3_S1A);
    prevStageMap.put(ClimbState.CLIMB_L3_S2, ClimbState.CLIMB_L3_S1B);
    prevStageMap.put(ClimbState.CLIMB_L3_S3, ClimbState.CLIMB_L3_S2);
    prevStageMap.put(ClimbState.CLIMB_L3_S4, ClimbState.CLIMB_L3_S3);
    prevStageMap.put(ClimbState.CLIMB_L3_S5, ClimbState.CLIMB_L3_S4);

    m_mainDrive = 0.0;
    
    m_auxDrive = 0.0;

    m_LEDRedValue = 0;
    m_LEDBlueValue = 0;
    m_LEDGreenValue = 0;

  }

  public void initialize() {

    //TODO-CALCULATE VELOCITY
    m_velocity = 28.24; //Inches per Second

    initActuators();
    setActuators();
  }

  public void initActuators(){
    int frontLeftClimbCanID = Robot.m_map.getId(MapKeys.FRONTLEFTCLIMBWHEEL);
    int frontRightClimbCanID = Robot.m_map.getId(MapKeys.FRONTRIGHTCLIMBWHEEL);
    

    int ClimbFront = Robot.m_map.getId(MapKeys.CLIMBFRONT);
    int ClimbBack = Robot.m_map.getId(MapKeys.CLIMBBACK);

    m_climbfront = new CANSparkMax(ClimbFront, MotorType.kBrushless);
    m_climbfront.setIdleMode(IdleMode.kBrake);
    m_climbfront.setSmartCurrentLimit(m_maxAmps);

    m_climbback = new CANSparkMax(ClimbBack, MotorType.kBrushless);
    m_climbback.setIdleMode(IdleMode.kBrake);
    m_climbback.setSmartCurrentLimit(m_maxAmps);
    
    m_pidControllerFront = m_climbfront.getPIDController();
    m_pidControllerBack = m_climbback.getPIDController();  
    
    m_encoderFront = m_climbfront.getEncoder();
    m_encoderBack = m_climbback.getEncoder();


    kP = 1.0; 
    kI = 0.0;
    kD = 0.0; 
    kIz = 0.0; 
    kFF = 0.0; 
    kMaxOutput = 1; 
    kMinOutput = -1;


    m_pidControllerFront.setP(kP);
    m_pidControllerFront.setI(kI);
    m_pidControllerFront.setD(kD);
    m_pidControllerFront.setIZone(kIz);
    m_pidControllerFront.setFF(kFF);
    m_pidControllerFront.setOutputRange(kMinOutput, kMaxOutput);

    m_pidControllerBack.setP(kP);
    m_pidControllerBack.setI(kI);
    m_pidControllerBack.setD(kD);
    m_pidControllerBack.setIZone(kIz);
    m_pidControllerBack.setFF(kFF);
    m_pidControllerBack.setOutputRange(kMinOutput, kMaxOutput);

    // display PID coefficients on SmartDashboard
    // SmartDashboard.putNumber("P Gain", kP);
    // SmartDashboard.putNumber("I Gain", kI);
    // SmartDashboard.putNumber("D Gain", kD);
    // SmartDashboard.putNumber("I Zone", kIz);
    // SmartDashboard.putNumber("Feed Forward", kFF);
    // SmartDashboard.putNumber("Max Output", kMaxOutput);
    // SmartDashboard.putNumber("Min Output", kMinOutput);
    // SmartDashboard.putNumber("Set Rotations", 0);
  
    if ((frontLeftClimbCanID != 0) && (frontRightClimbCanID != 0)) {
      frontleftwheel = new WPI_TalonSRX(frontLeftClimbCanID);  
      frontleftwheel.setInverted(false); 
      frontrightwheel = new WPI_TalonSRX(frontRightClimbCanID);
      frontrightwheel.setInverted(false);
      climbwheels = new SpeedControllerGroup(frontleftwheel, frontrightwheel);
    }


    
  
      
  

    final int PCM_1_CAN_ID = Robot.m_map.getId(MapKeys.PCM_CLIMBCANID);
    final int PCM_2_CAN_ID = Robot.m_map.getId(MapKeys.PCM_CLIMBCANID2);
    if ((PCM_1_CAN_ID != 0) && (PCM_2_CAN_ID != 0)){
      
      
     

      Solenoid_5 = new DoubleSolenoid(
        PCM_1_CAN_ID,
        Robot.m_map.getId(MapKeys.SOLENOID_LOWERFRONTEXTEND),
        Robot.m_map.getId(MapKeys.SOLENOID_LOWERFRONTRETRACT)
      );
      Solenoid_5.set(DoubleSolenoid.Value.kOff);

      Solenoid_6 = new DoubleSolenoid(
        PCM_1_CAN_ID,
        Robot.m_map.getId(MapKeys.SOLENOID_LOWERBACKEXTEND),
        Robot.m_map.getId(MapKeys.SOLENOID_LOWERBACKRETRACT)
      );
      Solenoid_6.set(DoubleSolenoid.Value.kOff); 
      
      Solenoid_7 = new DoubleSolenoid(
        PCM_2_CAN_ID,
        Robot.m_map.getId(MapKeys.SOLENOID_ASCENDASSISTBACKLEFTEXTEND),
        Robot.m_map.getId(MapKeys.SOLENOID_ASCENDASSISTBACKLEFTRETRACT)
      );
      Solenoid_7.set(DoubleSolenoid.Value.kOff);

      Solenoid_8 = new DoubleSolenoid(
        PCM_2_CAN_ID,
        Robot.m_map.getId(MapKeys.SOLENOID_ASCENDASSISTBACKRIGHTEXTEND),
        Robot.m_map.getId(MapKeys.SOLENOID_ASCENDASSISTBACKRIGHTRETRACT)
      );
      Solenoid_8.set(DoubleSolenoid.Value.kOff);
    }

    if ((frontLeftClimbCanID != 0) && (frontRightClimbCanID != 0) && 
        (PCM_1_CAN_ID != 0) && (PCM_2_CAN_ID != 0)){
      m_configured = true;
    }
  }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }

  public double getDescecndTimeleft(double m_Distance){

    m_DriveTime = (m_Distance)/(m_velocity);
    
    return m_DriveTime;
  }

  public boolean stateDescend(){
    return !m_autoDescend;
  } 
  public boolean stateL2Ascend(){
    return !m_autoL2Ascend;
  }
  public boolean stateL3Ascend(){
    return !m_autoL3Ascend;
  }
  public void startDescend() {

    m_climbState = ClimbState.DESCEND_S1;
    setActuators();
  }

  public void startL2Ascend() {

    m_climbState = ClimbState.CLIMB_L2_S0;
    setActuators();
  }

  public void startL3Ascend() {

    m_climbState = ClimbState.CLIMB_L3_S1A;
    setActuators();

  }

  public void nextStage() {

    m_climbState = nextStageMap.get(m_climbState);
    if (m_climbState == ClimbState.CLIMB_L3_S1B) {
      m_tickcounter = 0;
    }
    setActuators();

  }

  public void prevStage() {

    m_climbState = prevStageMap.get(m_climbState);

    if (m_climbState == ClimbState.CLIMB_L3_S1B) {
      m_tickcounter = 0;
    }
    
    setActuators();
  }

  @Override 
  public void periodic() {
   if (m_fronttargetPos_in > m_frontPos_in) {
     // increase front position
     double dist = m_fronttargetPos_in - m_frontPos_in;
     double dh = SPEED_EXTEND_FRONT_IPS * 0.02; // 0.02 seconds per tick
     if (dist <= dh) {
       m_frontPos_in = m_fronttargetPos_in; // we're there
     }
     else {
       m_frontPos_in += dh; // incremental move
     }
   }
   else if (m_fronttargetPos_in < m_frontPos_in) {
     // decrease front position
     double dist = m_frontPos_in - m_fronttargetPos_in;
     double dh = SPEED_RETRACT_FRONT_IPS * 0.02; // 0.02 seconds per tick
     if (dist <= dh) {
       m_frontPos_in = m_fronttargetPos_in; // we're there
     }
     else {
       m_frontPos_in -= dh; // incremental move
     }
   }
   if (m_backtargetPos_in > m_backPos_in) {
     // increase back position
     double dist = m_backtargetPos_in - m_backPos_in;
     double dh = SPEED_EXTEND_BACK_IPS * 0.02; // 0.02 seconds per tick
     if (dist <= dh) {
       m_backPos_in = m_backtargetPos_in; // we're there
     }
     else {
       m_backPos_in += dh; // incremental move
     }
  }
  else if (m_backtargetPos_in < m_backPos_in) {
     // decrease back position
     double dist = m_backtargetPos_in - m_backPos_in;
     double dh = SPEED_RETRACT_BACK_IPS * 0.02; // 0.02 seconds per tick
     if (dist <= dh) {
       m_backPos_in = m_backtargetPos_in; // we're there
     }
     else {
       m_backPos_in -= dh; // incremental move
     }
  }

    m_pidControllerBack.setReference(ROTATIONS_PER_INCH * m_backPos_in, ControlType.kPosition);
    m_pidControllerFront.setReference(ROTATIONS_PER_INCH * m_frontPos_in, ControlType.kPosition);
  }

  private void setActuators() {
    Preferences prefs = Preferences.getInstance();
    switch(m_climbState) {

      case IDLE:
        if (m_closedloop) {
          ascendFrontPOS(0);
          ascendBackPOS(0);
        }
        else {
          ascendFrontPT(SPEED_IDLE_IPS);
          ascendBackPT(SPEED_IDLE_IPS);
        }
        descendAssistBack(false);
        descendAssistFront(false);
        ascendAssistBack(false);
        m_auxDrive = 0.0;
        m_mainDrive = 0.0;
        m_timeLeft_sec = 0.0; 
        m_LEDRedValue = 255;
        m_LEDBlueValue = 0;
        m_LEDGreenValue = 0;
        System.out.print("Robot is in stage IDLE\n");
        m_autoDescend = false;
        m_autoL2Ascend = false;
        m_autoL3Ascend = false;
        break;

        case DESCEND_S1:
        if (m_closedloop) {
          ascendFrontPOS(0);
          ascendBackPOS(0);
        }
        else {
          ascendFrontPT(SPEED_IDLE_IPS);
          ascendBackPT(SPEED_IDLE_IPS);
        }
        descendAssistBack(false);
        descendAssistFront(true);
        ascendAssistBack(false);
        m_auxDrive = prefs.getDouble("Descend_S1_AuxDrive", 0.0);
        m_mainDrive = prefs.getDouble("Descend_S1_MainDrive", 0.4);
        m_timeLeft_sec = getDescecndTimeleft(38.0);
        //m_timeLeft_sec = prefs.getDouble("Descend_S1_TimeLeft", 0.5);
        m_LEDRedValue = 0;
        m_LEDBlueValue = 255;
        m_LEDGreenValue = 0;
        System.out.print("Descend Stage 1\n");
        m_autoDescend = true;
        m_autoL2Ascend = false;
        m_autoL3Ascend = false;
        break;

      case DESCEND_S2:
      if (m_closedloop) {
        ascendFrontPOS(0);
        ascendBackPOS(0);
      }
      else {
        ascendFrontPT(SPEED_IDLE_IPS);
        ascendBackPT(SPEED_IDLE_IPS);
      }
        descendAssistBack(false);
        descendAssistFront(false);
        ascendAssistBack(false);
        m_auxDrive = prefs.getDouble("Descend_S2_AuxDrive", 0.0);
        m_mainDrive = prefs.getDouble("Descend_S2_MainDrive", 0.4);
        m_timeLeft_sec = getDescecndTimeleft(2.0); //Was 8.0
        //m_timeLeft_sec = prefs.getDouble("Descend_S2_TimeLeft", 2);
        m_LEDRedValue = 0;
        m_LEDBlueValue = 255;
        m_LEDGreenValue = 0;
        System.out.print("Descend Stage 2\n");
        m_autoDescend = true;
        m_autoL2Ascend = false;
        m_autoL3Ascend = false;
        break;

      case DESCEND_S3:
      if (m_closedloop) {
        ascendFrontPOS(0);
        ascendBackPOS(0);
      }
      else {
        ascendFrontPT(SPEED_IDLE_IPS);
        ascendBackPT(SPEED_IDLE_IPS);
      }
        descendAssistBack(false);
        descendAssistFront(false);
        ascendAssistBack(true);
        m_auxDrive = prefs.getDouble("Descend_S3_AuxDrive", 0.0);
        m_mainDrive = prefs.getDouble("Descend_S3_MainDrive", 0.4);
        m_timeLeft_sec = getDescecndTimeleft(8.0);
        //m_timeLeft_sec = prefs.getDouble("Descend_S3_TimeLeft", 0.75);
        m_LEDRedValue = 0;
        m_LEDBlueValue = 255;
        m_LEDGreenValue = 0;
        System.out.print("Descend Stage 3\n");
        m_autoDescend = true;
        m_autoL2Ascend = false;
        m_autoL3Ascend = false;
        break;

      case DESCEND_S4:
      if (m_closedloop) {
        ascendFrontPOS(0);
        ascendBackPOS(0);
      }
      else {
        ascendFrontPT(SPEED_IDLE_IPS);
        ascendBackPT(SPEED_IDLE_IPS);
      }
        descendAssistBack(false);
        descendAssistFront(false);
        ascendAssistBack(false);
        m_auxDrive = prefs.getDouble("Descend_S4_AuxDrive", 0.0);
        m_mainDrive = prefs.getDouble("Descend_S4_MainDrive", 0.4);
        m_timeLeft_sec = getDescecndTimeleft(0.0);
        //m_timeLeft_sec = prefs.getDouble("Descend_S4_TimeLeft", 0.75);
        m_LEDRedValue = 0;
        m_LEDBlueValue = 255;
        m_LEDGreenValue = 0;
        System.out.print("Descend Stage 4\n");
        m_autoDescend = true;
        m_autoL2Ascend = false;
        m_autoL3Ascend = false;
        break;

      case DESCEND_S5:
      if (m_closedloop) {
        ascendFrontPOS(0);
        ascendBackPOS(0);
      }
      else {
        ascendFrontPT(SPEED_IDLE_IPS);
        ascendBackPT(SPEED_IDLE_IPS);
      }
        descendAssistBack(false);
        descendAssistFront(false);
        ascendAssistBack(false);
        m_auxDrive = prefs.getDouble("Descend_S5_AuxDrive", 0.0);
        m_mainDrive = prefs.getDouble("Descend_S5_MainDrive", 0.0);
        m_timeLeft_sec = getDescecndTimeleft(0.0);
        //m_timeLeft_sec = prefs.getDouble("Descend_S5_TimeLeft", 0.0);
        m_LEDRedValue = 0;
        m_LEDBlueValue = 255;
        m_LEDGreenValue = 0;
        System.out.print("Descend Stage 5\n");
        m_autoDescend = true;
        m_autoL2Ascend = false;
        m_autoL3Ascend = false;
        break;

      case CLIMB_L2_S0:
      if (m_closedloop) {
        ascendFrontPOS(0);
        ascendBackPOS(0);
      }
      else {
        ascendFrontPT(SPEED_IDLE_IPS);
        ascendBackPT(SPEED_IDLE_IPS);
      }
        descendAssistBack(false);
        descendAssistFront(false);
        ascendAssistBack(false);
        m_auxDrive = prefs.getDouble("Climb_L2_S0_AuxDrive", 0.0);
        m_mainDrive = prefs.getDouble("Climb_L2_S0_MainDrive", -0.3);
        m_timeLeft_sec = prefs.getDouble("Climb_L2_S0_TimeLeft", 0.25);
        m_LEDRedValue = 0;
        m_LEDBlueValue = 0;
        m_LEDGreenValue = 0;
        System.out.print("Climb Level 2 Stage 0\n");
        m_autoDescend = false;
        m_autoL2Ascend = true;
        m_autoL3Ascend = false;
        break;

      case CLIMB_L2_S1:
      if (m_closedloop) {
        ascendFrontPOS(L2_POS_IN);
        ascendBackPOS(L2_POS_IN);
      }
      else {
        ascendFrontPT(SPEED_IDLE_IPS);
        ascendBackPT(SPEED_IDLE_IPS);
      }
        descendAssistBack(false);
        descendAssistFront(false);
        ascendAssistBack(false);
        m_auxDrive = prefs.getDouble("Climb_L2_S1_AuxDrive", 0.0);
        m_mainDrive = prefs.getDouble("Climb_L2_S1_MainDrive", 0.0);
        m_timeLeft_sec = prefs.getDouble("Climb_L2_S1_TimeLeft", 1.0);
        m_LEDRedValue = 0;
        m_LEDBlueValue = 0;
        m_LEDGreenValue = 0;
        System.out.print("Climb Level 2 Stage 1\n");
        m_autoDescend = false;
        m_autoL2Ascend = true;
        m_autoL3Ascend = false;
        break;

      case CLIMB_L2_S2:
      if (m_closedloop) {
        ascendFrontPOS(L2_POS_IN);
        ascendBackPOS(L2_POS_IN);
      }
      else {
        ascendFrontPT(SPEED_IDLE_IPS);
        ascendBackPT(SPEED_IDLE_IPS);
      }
        descendAssistBack(false);
        descendAssistFront(false);
        ascendAssistBack(false);
        m_auxDrive = prefs.getDouble("Climb_L2_S2_AuxDrive", 0.5);
        m_mainDrive = prefs.getDouble("Climb_L2_S2_MainDrive", 0.5);
        m_timeLeft_sec = prefs.getDouble("Climb_L2_S2_TimeLeft", 0.8);
        m_LEDRedValue = 0;
        m_LEDBlueValue = 0;
        m_LEDGreenValue = 0;
        System.out.print("Climb Level 2 Stage 2\n");
        m_autoDescend = false;
        m_autoL2Ascend = true;
        m_autoL3Ascend = false;
        break;

      case CLIMB_L2_S3:
      if (m_closedloop) {
        ascendFrontPOS(0);
        ascendBackPOS(L2_POS_IN);
      }
      else {
        ascendFrontPT(SPEED_IDLE_IPS);
        ascendBackPT(SPEED_IDLE_IPS);
      }
        descendAssistBack(false);
        descendAssistFront(false);
        ascendAssistBack(false);
        m_auxDrive = prefs.getDouble("Climb_L2_S3_AuxDrive", 0.0);
        m_mainDrive = prefs.getDouble("Climb_L2_S3_MainDrive", 0.0);
        m_timeLeft_sec = prefs.getDouble("Climb_L2_S3_TimeLeft", 1.2);
        m_LEDRedValue = 0;
        m_LEDBlueValue = 0;
        m_LEDGreenValue = 0;
        System.out.print("Climb Level 2 Stage 3\n");
        m_autoDescend = false;
        m_autoL2Ascend = true;
        m_autoL3Ascend = false;
        break;

      case CLIMB_L2_S4:
      if (m_closedloop) {
        ascendFrontPOS(0);
        ascendBackPOS(L2_POS_IN);
      }
      else {
        ascendFrontPT(SPEED_IDLE_IPS);
        ascendBackPT(SPEED_IDLE_IPS);
      }
        descendAssistBack(false);
        descendAssistFront(false);
        ascendAssistBack(true);
        m_auxDrive = prefs.getDouble("Climb_L2_S4_AuxDrive", 0.25);
        m_mainDrive = prefs.getDouble("Climb_L2_S4_MainDrive", 0.3);
        m_timeLeft_sec = prefs.getDouble("Climb_L2_S4_TimeLeft", 2.0); // too long
        m_LEDRedValue = 0;
        m_LEDBlueValue = 0;
        m_LEDGreenValue = 0;
        System.out.print("Climb Level 2 Stage 4\n");
        m_autoDescend = false;
        m_autoL2Ascend = true;
        m_autoL3Ascend = false;
        break;

      case CLIMB_L2_S5:
      if (m_closedloop) {
        ascendFrontPOS(0);
        ascendBackPOS(0);
      }
      else {
        ascendFrontPT(SPEED_IDLE_IPS);
        ascendBackPT(SPEED_IDLE_IPS);
      }
        descendAssistBack(false);
        descendAssistFront(false);
        ascendAssistBack(true);
        m_auxDrive = prefs.getDouble("Climb_L2_S5_AuxDrive", 0.0);
        m_mainDrive = prefs.getDouble("Climb_L2_S5_MainDrive", 0.0);
        m_timeLeft_sec = prefs.getDouble("Climb_L2_S5_TimeLeft", 2.0);
        m_LEDRedValue = 0;
        m_LEDBlueValue = 0;
        m_LEDGreenValue = 0;
        System.out.print("Climb Level 2 Stage 5\n");
        m_autoDescend = false;
        m_autoL2Ascend = true;
        m_autoL3Ascend = false;
        break;

      case CLIMB_L2_S6:
      if (m_closedloop) {
        ascendFrontPOS(0);
        ascendBackPOS(0);
      }
      else {
        ascendFrontPT(SPEED_IDLE_IPS);
        ascendBackPT(SPEED_IDLE_IPS);
      }
        descendAssistBack(false);
        descendAssistFront(false);
        ascendAssistBack(false);
        m_auxDrive = prefs.getDouble("Climb_L2_S6_AuxDrive", 0.0);
        m_mainDrive = prefs.getDouble("Climb_L2_S6_MainDrive", 0.0);
        m_timeLeft_sec = prefs.getDouble("Climb_L2_S6_TimeLeft", 0.0);
        m_LEDRedValue = 0;
        m_LEDBlueValue = 0;
        m_LEDGreenValue = 0;
        System.out.print("Climb Level 2 Stage 6\n");
        m_autoDescend = false;
        m_autoL2Ascend = true;
        m_autoL3Ascend = false;
        break;

      case CLIMB_L3_S1A:
        if (m_closedloop) {
          ascendFrontPOS(L3_POS_IN);
          ascendBackPOS(L3_POS_IN);
        }
        else {
          ascendFrontPT(SPEED_IDLE_IPS);
          ascendBackPT(SPEED_IDLE_IPS);
        }
        descendAssistBack(false);
        descendAssistFront(false);
        ascendAssistBack(false);
        m_auxDrive = prefs.getDouble("Climb_L3_S1_AuxDrive", 0.15);
        m_mainDrive = prefs.getDouble("Climb_L3_S1_MainDrive", 0.0);
        m_timeLeft_sec = prefs.getDouble("Climb_L3_S1_TimeLeft", 2.0);
        m_LEDRedValue = 0;
        m_LEDBlueValue = 0;
        m_LEDGreenValue = 0;
        System.out.print("Climb Level 3 Stage 1A\n");
        m_autoDescend = false;
        m_autoL2Ascend = false;
        m_autoL3Ascend = true;
        break;

        case CLIMB_L3_S1B:
          if (m_closedloop) {
            ascendFrontPOS(L3_POS_IN);
            ascendBackPOS(L3_POS_IN);
          }
          else {
            ascendFrontPT(SPEED_IDLE_IPS);
            ascendBackPT(SPEED_IDLE_IPS);
          }
        descendAssistBack(false);
        descendAssistFront(false);
        ascendAssistBack(false);
        m_auxDrive = prefs.getDouble("Climb_L3_S1_AuxDrive", 0.15);
        m_mainDrive = prefs.getDouble("Climb_L3_S1_MainDrive", 0.0);
        m_timeLeft_sec = prefs.getDouble("Climb_L3_S1B_TimeLeft", 0.0);
        m_LEDRedValue = 0;
        m_LEDBlueValue = 0;
        m_LEDGreenValue = 0;
        System.out.print("Climb Level 3 Stage 1B\n");
        m_autoDescend = false;
        m_autoL2Ascend = false;
        m_autoL3Ascend = true;
        break;

      case CLIMB_L3_S2:
        if (m_closedloop) {
          ascendFrontPOS(L3_POS_IN);
          ascendBackPOS(L3_POS_IN);
        }
        else {
          ascendFrontPT(SPEED_IDLE_IPS);
          ascendBackPT(SPEED_IDLE_IPS);
        }
        descendAssistBack(false);
        descendAssistFront(false);
        ascendAssistBack(false);
        m_auxDrive = prefs.getDouble("Climb_L3_S2_AuxDrive", 0.5);
        m_mainDrive = prefs.getDouble("Climb_L3_S2_MainDrive", 0.5);
        m_timeLeft_sec = prefs.getDouble("Climb_L3_S2_TimeLeft", 2.0); //too long
        m_LEDRedValue = 0;
        m_LEDBlueValue = 0;
        m_LEDGreenValue = 0;
        System.out.print("Climb Level 3 Stage 2\n");
        m_autoDescend = false;
        m_autoL2Ascend = false;
        m_autoL3Ascend = true;
        break;

      case CLIMB_L3_S3:
        if (m_closedloop) {
          ascendFrontPOS(0);
          ascendBackPOS(L3_POS_IN);
        }
        else {
          ascendFrontPT(SPEED_IDLE_IPS);
          ascendBackPT(SPEED_IDLE_IPS);
        }
        descendAssistBack(false);
        descendAssistFront(false);
        ascendAssistBack(false);
        m_auxDrive = prefs.getDouble("Climb_L3_S3_AuxDrive", 0.0);
        m_mainDrive = prefs.getDouble("Climb_L3_S3_MainDrive", 0.0);
        m_timeLeft_sec = prefs.getDouble("Climb_L3_S3_TimeLeft", 2.0);
        m_LEDRedValue = 0;
        m_LEDBlueValue = 0;
        m_LEDGreenValue = 0;
        System.out.print("Climb Level 3 Stage 3\n");
        m_autoDescend = false;
        m_autoL2Ascend = false;
        m_autoL3Ascend = true;
        break;

      case CLIMB_L3_S4:
        if (m_closedloop) {
          ascendFrontPOS(0);
          ascendBackPOS(L3_POS_IN);
        }
        else {
          ascendFrontPT(SPEED_IDLE_IPS);
          ascendBackPT(SPEED_IDLE_IPS);
        }
        descendAssistBack(false);
        descendAssistFront(false);
        ascendAssistBack(false);
        m_auxDrive = prefs.getDouble("Climb_L3_S4_AuxDrive", 0.5);
        m_mainDrive = prefs.getDouble("Climb_L3_S4_MainDrive", 0.5);
        m_timeLeft_sec = prefs.getDouble("Climb_L3_S4_TimeLeft", 2.0); // too long
        m_LEDRedValue = 0;
        m_LEDBlueValue = 0;
        m_LEDGreenValue = 0;
        System.out.print("Climb Level 3 Stage 4\n");
        m_autoDescend = false;
        m_autoL2Ascend = false;
        m_autoL3Ascend = true;
        break;

        case CLIMB_L3_S5:
          if (m_closedloop) {
            ascendFrontPOS(0);
            ascendBackPOS(0);
          }
          else {
            ascendFrontPT(SPEED_IDLE_IPS);
            ascendBackPT(SPEED_IDLE_IPS);
          }
        descendAssistBack(false);
        descendAssistFront(false);
        ascendAssistBack(false);
        m_auxDrive = prefs.getDouble("Climb_L3_S5_AuxDrive", 0.0);
        m_mainDrive = prefs.getDouble("Climb_L3_S5_MainDrive", 0.0);
        m_timeLeft_sec = prefs.getDouble("Climb_L3_S5_TimeLeft", 2.0);
        m_LEDRedValue = 0;
        m_LEDBlueValue = 0;
        m_LEDGreenValue = 0;
        System.out.print("Climb Level 3 Stage 5\n");
        m_autoDescend = false;
        m_autoL2Ascend = false;
        m_autoL3Ascend = true;
        break;

    }

  }

  public double getMainDrive() {

    return m_mainDrive;

  }
  public int getRedValue(){
    return m_LEDRedValue;
  }

  public int getBlueValue(){
    return m_LEDBlueValue;
  }

  public int getGreenValue(){
    return m_LEDGreenValue;
  }

  public double getTimeLeft() {

    return m_timeLeft_sec;

  }

  public double getauxDrive() {
    return m_auxDrive;
  }

  public void setauxDrive() {
    climbwheels.set(m_auxDrive);
  }
  




  public void ascendFrontPOS(double position_in) {
    if (!m_configured) {
      return;
    }
   
      System.out.print("Acscend Front positioned\n");
      m_fronttargetPos_in = position_in;
     // m_pidControllerFront.setReference(Rotations_per_inch * position_in, ControlType.kPosition);
  }

public void ascendBackPOS(double position_in) {
  if (!m_configured) {
    return;
  }
    System.out.print("Acscend Back positioned\n");
    m_backtargetPos_in = position_in;
    // m_pidControllerBack.setReference(Rotations_per_inch * position_in, ControlType.kPosition);
}


public void ascendFrontPT(double speed) {
  if (!m_configured) {
    return;
  }
  m_climbfront.set(speed);
}

public void ascendBackPT(double speed) {
  if (!m_configured) {
    return;
  }
  m_climbback.set(speed);
}


public void descendAssistFront(boolean state) {
  if (!m_configured) {
    return;
  }
  if (state) {
    System.out.print("Descend Front Activated\n");
    Solenoid_5.set(DoubleSolenoid.Value.kForward);
  }
  else {
    Solenoid_5.set(DoubleSolenoid.Value.kReverse);
  }

}

public void descendAssistBack(boolean state) {
  if (!m_configured) {
    return;
  }
  if (state) {
    System.out.print("Descend Back Activated\n");
    Solenoid_6.set(DoubleSolenoid.Value.kForward);
  }
  else {
    Solenoid_6.set(DoubleSolenoid.Value.kReverse);
  }
}

public void ascendAssistBack(boolean state) {
  if (!m_configured) {
    return;
  }
  if (state) {
    System.out.print("Acscend Assist Back Activated\n");
    Solenoid_7.set(DoubleSolenoid.Value.kForward);
    Solenoid_8.set(DoubleSolenoid.Value.kForward);
  }
  else {
    Solenoid_7.set(DoubleSolenoid.Value.kReverse);
    Solenoid_8.set(DoubleSolenoid.Value.kReverse);
  }
}

}
