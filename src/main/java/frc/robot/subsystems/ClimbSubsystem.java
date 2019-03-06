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

import java.util.EnumMap;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;



//Add your docs here.
 
public class ClimbSubsystem extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.

  enum ClimbState {
    IDLE,
    DESCEND_S1, DESCEND_S2, DESCEND_S3, DESCEND_S4, DESCEND_S5,
    CLIMB_L2_S1, CLIMB_L2_S2, CLIMB_L2_S3, CLIMB_L2_S4, CLIMB_L2_S5, CLIMB_L2_S6,
    CLIMB_L3_S1, CLIMB_L3_S2, CLIMB_L3_S3, CLIMB_L3_S4, CLIMB_L3_S5,
  }
  private ClimbState m_climbState = ClimbState.IDLE;

  private static final EnumMap<ClimbState, ClimbState> nextStageMap = new EnumMap<>(ClimbState.class);
  private static final EnumMap<ClimbState, ClimbState> prevStageMap = new EnumMap<>(ClimbState.class);

  private double m_timeLeft_sec;

  
  private DoubleSolenoid Solenoid_1;
  private DoubleSolenoid Solenoid_2;
  private DoubleSolenoid Solenoid_3;
  private DoubleSolenoid Solenoid_4;
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



  public ClimbSubsystem() {
  
    nextStageMap.put(ClimbState.IDLE, ClimbState.IDLE);

    nextStageMap.put(ClimbState.DESCEND_S1, ClimbState.DESCEND_S2);
    nextStageMap.put(ClimbState.DESCEND_S2, ClimbState.DESCEND_S3);
    nextStageMap.put(ClimbState.DESCEND_S3, ClimbState.DESCEND_S4);
    nextStageMap.put(ClimbState.DESCEND_S4, ClimbState.DESCEND_S5);
    nextStageMap.put(ClimbState.DESCEND_S5, ClimbState.IDLE);

    nextStageMap.put(ClimbState.CLIMB_L2_S1, ClimbState.CLIMB_L2_S2);
    nextStageMap.put(ClimbState.CLIMB_L2_S2, ClimbState.CLIMB_L2_S3);
    nextStageMap.put(ClimbState.CLIMB_L2_S3, ClimbState.CLIMB_L2_S4);
    nextStageMap.put(ClimbState.CLIMB_L2_S4, ClimbState.CLIMB_L2_S5);
    nextStageMap.put(ClimbState.CLIMB_L2_S5, ClimbState.CLIMB_L2_S6);
    nextStageMap.put(ClimbState.CLIMB_L2_S6, ClimbState.IDLE);

    nextStageMap.put(ClimbState.CLIMB_L3_S1, ClimbState.CLIMB_L3_S2);
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

    prevStageMap.put(ClimbState.CLIMB_L2_S1, ClimbState.IDLE);
    prevStageMap.put(ClimbState.CLIMB_L2_S2, ClimbState.CLIMB_L2_S1);
    prevStageMap.put(ClimbState.CLIMB_L2_S3, ClimbState.CLIMB_L2_S2);
    prevStageMap.put(ClimbState.CLIMB_L2_S4, ClimbState.CLIMB_L2_S3);
    prevStageMap.put(ClimbState.CLIMB_L2_S5, ClimbState.CLIMB_L2_S4);
    prevStageMap.put(ClimbState.CLIMB_L2_S6, ClimbState.CLIMB_L2_S5);

    prevStageMap.put(ClimbState.CLIMB_L3_S1, ClimbState.IDLE);
    prevStageMap.put(ClimbState.CLIMB_L3_S2, ClimbState.CLIMB_L3_S1);
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
    initActuators();
    setActuators();
  }
  public void initActuators(){
    int frontLeftClimbCanID = Robot.m_map.getId(MapKeys.FRONTLEFTCLIMBWHEEL);
    int frontRightClimbCanID = Robot.m_map.getId(MapKeys.FRONTRIGHTCLIMBWHEEL);
    if ((frontLeftClimbCanID != 0) && (frontRightClimbCanID != 0)){
      frontleftwheel = new WPI_TalonSRX(frontLeftClimbCanID);  
      frontleftwheel.setInverted(true); 
      frontrightwheel = new WPI_TalonSRX(frontRightClimbCanID);
      frontrightwheel.setInverted(false);
      climbwheels = new SpeedControllerGroup(frontleftwheel, frontrightwheel);
    }


    final int PCM_1_CAN_ID = Robot.m_map.getId(MapKeys.PCM_CLIMBCANID);
    final int PCM_2_CAN_ID = Robot.m_map.getId(MapKeys.PCM_CLIMBCANID2);
    if ((PCM_1_CAN_ID != 0) && (PCM_2_CAN_ID != 0)){
      Solenoid_1 = new DoubleSolenoid(
        PCM_1_CAN_ID,
        Robot.m_map.getId(MapKeys.SOLENOID_FRONTEXTEND),
        Robot.m_map.getId(MapKeys.SOLENOID_FRONTRETRACT)
      );
      Solenoid_1.set(DoubleSolenoid.Value.kOff);

      Solenoid_2 = new DoubleSolenoid(
        PCM_1_CAN_ID,
        Robot.m_map.getId(MapKeys.SOLENOID_BACKEXTEND),
        Robot.m_map.getId(MapKeys.SOLENOID_BACKRETRACT)
      );
      Solenoid_2.set(DoubleSolenoid.Value.kOff);
    

      
    /*  Solenoid_3 = new DoubleSolenoid(
        PCM_1_CAN_ID,
        Robot.m_map.getId(MapKeys.SOLENOID_BACKLEFTEXTEND),
        Robot.m_map.getId(MapKeys.SOLENOID_BACKLEFTRETRACT)
        );
      Solenoid_3.set(DoubleSolenoid.Value.kOff);

      Solenoid_4 = new DoubleSolenoid(
        PCM_1_CAN_ID,
      Robot.m_map.getId(MapKeys.SOLENOID_BACKRIGHTEXTEND),
      Robot.m_map.getId(MapKeys.SOLENOID_BACKRIGHTRETRACT)
      );
      Solenoid_4.set(DoubleSolenoid.Value.kOff);
      */
    

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

    m_climbState = ClimbState.CLIMB_L2_S1;
    setActuators();
  }

  public void startL3Ascend() {

    m_climbState = ClimbState.CLIMB_L3_S1;
    setActuators();

  }

  public void nextStage() {

    m_climbState = nextStageMap.get(m_climbState);
    setActuators();

  }

  public void prevStage() {

    m_climbState = prevStageMap.get(m_climbState);
    setActuators();
  }

  private void setActuators() {
    Preferences prefs = Preferences.getInstance();
    switch(m_climbState) {

      case IDLE:
        ascendFront(false);
        ascendBack(false);
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
        ascendFront(false);
        ascendBack(false);
        descendAssistBack(false);
        descendAssistFront(false);
        ascendAssistBack(false);
        m_auxDrive = prefs.getDouble("Descend_S1_AuxDrive", 0.0);
        m_mainDrive = prefs.getDouble("Descend_S1_MainDrive", 0.5);
        m_timeLeft_sec = prefs.getDouble("Descend_S1_TimeLeft", 0.0);
        m_LEDRedValue = 0;
        m_LEDBlueValue = 255;
        m_LEDGreenValue = 0;
        System.out.print("Descend Stage 1\n");
        m_autoDescend = true;
        m_autoL2Ascend = false;
        m_autoL3Ascend = false;
        break;

      case DESCEND_S2:
        ascendFront(false);
        ascendBack(false);
        descendAssistBack(false);
        descendAssistFront(true);
        ascendAssistBack(false);
        m_auxDrive = prefs.getDouble("Descend_S2_AuxDrive", 0.0);
        m_mainDrive = prefs.getDouble("Descend_S2_MainDrive", 0.5);
        m_timeLeft_sec = prefs.getDouble("Descend_S2_TimeLeft", 0.0);
        m_LEDRedValue = 0;
        m_LEDBlueValue = 255;
        m_LEDGreenValue = 0;
        System.out.print("Descend Stage 2\n");
        m_autoDescend = true;
        m_autoL2Ascend = false;
        m_autoL3Ascend = false;
        break;

      case DESCEND_S3:
        ascendFront(false);
        ascendBack(false);
        descendAssistBack(true);
        descendAssistFront(true);
        ascendAssistBack(false);
        m_auxDrive = prefs.getDouble("Descend_S3_AuxDrive", 0.0);
        m_mainDrive = prefs.getDouble("Descend_S3_MainDrive", 0.5);
        m_timeLeft_sec = prefs.getDouble("Descend_S3_TimeLeft", 0.0);
        m_LEDRedValue = 0;
        m_LEDBlueValue = 255;
        m_LEDGreenValue = 0;
        System.out.print("Descend Stage 3\n");
        m_autoDescend = true;
        m_autoL2Ascend = false;
        m_autoL3Ascend = false;
        break;

      case DESCEND_S4:
        ascendFront(false);
        ascendBack(false);
        descendAssistBack(true);
        descendAssistFront(false);
        ascendAssistBack(false);
        m_auxDrive = prefs.getDouble("Descend_S4_AuxDrive", 0.0);
        m_mainDrive = prefs.getDouble("Descend_S4_MainDrive", 0.5);
        m_timeLeft_sec = prefs.getDouble("Descend_S4_TimeLeft", 0.0);
        m_LEDRedValue = 0;
        m_LEDBlueValue = 255;
        m_LEDGreenValue = 0;
        System.out.print("Descend Stage 4\n");
        m_autoDescend = true;
        m_autoL2Ascend = false;
        m_autoL3Ascend = false;
        break;

      case DESCEND_S5:
        ascendFront(false);
        ascendBack(false);
        descendAssistBack(false);
        descendAssistFront(false);
        ascendAssistBack(false);
        m_auxDrive = prefs.getDouble("Descend_S5_AuxDrive", 0.0);
        m_mainDrive = prefs.getDouble("Descend_S5_MainDrive", 0.5);
        m_timeLeft_sec = prefs.getDouble("Descend_S5_TimeLeft", 0.0);
        m_LEDRedValue = 0;
        m_LEDBlueValue = 255;
        m_LEDGreenValue = 0;
        System.out.print("Descend Stage 5\n");
        m_autoDescend = true;
        m_autoL2Ascend = false;
        m_autoL3Ascend = false;
        break;

      case CLIMB_L2_S1:
        ascendFront(true);
        ascendBack(true);
        descendAssistBack(false);
        descendAssistFront(false);
        ascendAssistBack(false);
        m_auxDrive = prefs.getDouble("Climb_L2_S1_AuxDrive", 0.0);
        m_mainDrive = prefs.getDouble("Climb_L2_S1_MainDrive", 0.0);
        m_timeLeft_sec = prefs.getDouble("Climb_L2_S1_TimeLeft", 2.0);
        m_LEDRedValue = 0;
        m_LEDBlueValue = 0;
        m_LEDGreenValue = 0;
        System.out.print("Climb Level 2 Stage 1\n");
        m_autoDescend = false;
        m_autoL2Ascend = true;
        m_autoL3Ascend = false;
        break;

      case CLIMB_L2_S2:
        ascendFront(true);
        ascendBack(true);
        descendAssistBack(false);
        descendAssistFront(false);
        ascendAssistBack(false);
        m_auxDrive = prefs.getDouble("Climb_L2_S2_AuxDrive", 0.5);
        m_mainDrive = prefs.getDouble("Climb_L2_S2_MainDrive", 0.5);
        m_timeLeft_sec = prefs.getDouble("Climb_L2_S2_TimeLeft", 2.0);
        m_LEDRedValue = 0;
        m_LEDBlueValue = 0;
        m_LEDGreenValue = 0;
        System.out.print("Climb Level 2 Stage 2\n");
        m_autoDescend = false;
        m_autoL2Ascend = true;
        m_autoL3Ascend = false;
        break;

      case CLIMB_L2_S3:
        ascendFront(true);
        ascendBack(true);
        descendAssistBack(false);
        descendAssistFront(false);
        ascendAssistBack(true);
        m_auxDrive = prefs.getDouble("Climb_L2_S3_AuxDrive", 0.5);
        m_mainDrive = prefs.getDouble("Climb_L2_S3_MainDrive", 0.5);
        m_timeLeft_sec = prefs.getDouble("Climb_L2_S3_TimeLeft", 2.0);
        m_LEDRedValue = 0;
        m_LEDBlueValue = 0;
        m_LEDGreenValue = 0;
        System.out.print("Climb Level 2 Stage 3\n");
        m_autoDescend = false;
        m_autoL2Ascend = true;
        m_autoL3Ascend = false;
        break;

      case CLIMB_L2_S4:
        ascendFront(false);
        ascendBack(false);
        descendAssistBack(false);
        descendAssistFront(false);
        ascendAssistBack(true);
        m_auxDrive = prefs.getDouble("Climb_L2_S4_AuxDrive", 0.0);
        m_mainDrive = prefs.getDouble("Climb_L2_S4_MainDrive", 0.5);
        m_timeLeft_sec = prefs.getDouble("Climb_L2_S4_TimeLeft", 2.0);
        m_LEDRedValue = 0;
        m_LEDBlueValue = 0;
        m_LEDGreenValue = 0;
        System.out.print("Climb Level 2 Stage 4\n");
        m_autoDescend = false;
        m_autoL2Ascend = true;
        m_autoL3Ascend = false;
        break;

      case CLIMB_L2_S5:
        ascendFront(false);
        ascendBack(false);
        descendAssistBack(false);
        descendAssistFront(false);
        ascendAssistBack(false);
        m_auxDrive = prefs.getDouble("Climb_L2_S5_AuxDrive", 0.0);
        m_mainDrive = prefs.getDouble("Climb_L2_S5_MainDrive", 0.5);
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
        ascendFront(false);
        ascendBack(false);
        descendAssistBack(false);
        descendAssistFront(false);
        ascendAssistBack(false);
        m_auxDrive = prefs.getDouble("Climb_L2_S6_AuxDrive", 0.0);
        m_mainDrive = prefs.getDouble("Climb_L2_S6_MainDrive", 0.0);
        m_timeLeft_sec = prefs.getDouble("Climb_L2_S6_TimeLeft", 2.0);
        m_LEDRedValue = 0;
        m_LEDBlueValue = 0;
        m_LEDGreenValue = 0;
        System.out.print("Climb Level 2 Stage 6\n");
        m_autoDescend = false;
        m_autoL2Ascend = true;
        m_autoL3Ascend = false;
        break;

      case CLIMB_L3_S1:
        ascendFront(true); 
        ascendBack(true);
        descendAssistBack(false);
        descendAssistFront(false);
        ascendAssistBack(false);
        m_auxDrive = prefs.getDouble("Climb_L3_S1_AuxDrive", 0.0);
        m_mainDrive = prefs.getDouble("Climb_L3_S1_MainDrive", 0.0);
        m_timeLeft_sec = prefs.getDouble("Climb_L3_S1_TimeLeft", 2.0);
        m_LEDRedValue = 0;
        m_LEDBlueValue = 0;
        m_LEDGreenValue = 0;
        System.out.print("Climb Level 3 Stage 1\n");
        m_autoDescend = false;
        m_autoL2Ascend = false;
        m_autoL3Ascend = true;
        break;

      case CLIMB_L3_S2:
        ascendFront(true);
        ascendBack(true);
        descendAssistBack(false);
        descendAssistFront(false);
        ascendAssistBack(false);
        m_auxDrive = prefs.getDouble("Climb_L3_S2_AuxDrive", 0.5);
        m_mainDrive = prefs.getDouble("Climb_L3_S2_MainDrive", 0.5);
        m_timeLeft_sec = prefs.getDouble("Climb_L3_S2_TimeLeft", 2.0);
        m_LEDRedValue = 0;
        m_LEDBlueValue = 0;
        m_LEDGreenValue = 0;
        System.out.print("Climb Level 3 Stage 2\n");
        m_autoDescend = false;
        m_autoL2Ascend = false;
        m_autoL3Ascend = true;
        break;

      case CLIMB_L3_S3:
        ascendFront(false);
        ascendBack(true);
        descendAssistBack(false);
        descendAssistFront(false);
        ascendAssistBack(false);
        m_auxDrive = prefs.getDouble("Climb_L3_S3_AuxDrive", 0.5);
        m_mainDrive = prefs.getDouble("Climb_L3_S3_MainDrive", 0.5);
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
        ascendFront(false);
        ascendBack(true);
        descendAssistBack(false);
        descendAssistFront(false);
        ascendAssistBack(false);
        m_auxDrive = prefs.getDouble("Climb_L3_S4_AuxDrive", 0.0);
        m_mainDrive = prefs.getDouble("Climb_L3_S4_MainDrive", 0.5);
        m_timeLeft_sec = prefs.getDouble("Climb_L3_S4_TimeLeft", 2.0);
        m_LEDRedValue = 0;
        m_LEDBlueValue = 0;
        m_LEDGreenValue = 0;
        System.out.print("Climb Level 3 Stage 4\n");
        m_autoDescend = false;
        m_autoL2Ascend = false;
        m_autoL3Ascend = true;
        break;

        case CLIMB_L3_S5:
        ascendFront(false);
        ascendBack(false);
        descendAssistBack(false);
        descendAssistFront(false);
        ascendAssistBack(false);
        m_auxDrive = prefs.getDouble("Climb_L3_S5_AuxDrive", 0.0);
        m_mainDrive = prefs.getDouble("Climb_L3_S5_MainDrive", 0.5);
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
  




  public void ascendFront(boolean state) {
    if (!m_configured) {
      return;
    }
    if (state) {
      System.out.print("Acscend Front Activated\n");
    Solenoid_1.set(DoubleSolenoid.Value.kForward);
  
  }
  else {
    Solenoid_1.set(DoubleSolenoid.Value.kReverse);
    
  }
}

public void ascendBack(boolean state) {
  if (!m_configured) {
    return;
  }
  if (state) {
    System.out.print("Acscend Back Activated\n");
    Solenoid_2.set(DoubleSolenoid.Value.kForward);
  }
  else{
    Solenoid_2.set(DoubleSolenoid.Value.kReverse);
  }
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

