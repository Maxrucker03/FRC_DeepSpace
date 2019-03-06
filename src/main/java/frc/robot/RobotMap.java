/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import java.util.EnumMap;

import edu.wpi.first.wpilibj.Preferences;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
  // For example to map the left and right motors, you could define the
  // following variables to use with your drivetrain subsystem.
  // public static int leftMotor = 1;
  // public static int rightMotor = 2;

  // If you are using multiple modules, make sure to define both the port
  // number and the module. For example you with a rangefinder:
  // public static int rangefinderPort = 1;
  // public static int rangefinderModule = 1;

  public enum MapKeys {
    DRIVE_FRONTLEFT, DRIVE_FRONTRIGHT, DRIVE_BACKLEFT, DRIVE_BACKRIGHT,
    SLIDE, LIFT_LEFT, LIFT_RIGHT, 
    PCM_CLIMBCANID, 
    SOLENOID_FRONTEXTEND, SOLENOID_FRONTRETRACT, SOLENOID_BACKEXTEND, SOLENOID_BACKRETRACT, SOLENOID_FRONTRIGHTEXTEND, SOLENOID_FRONTRIGHTRETRACT, SOLENOID_BACKRIGHTEXTEND, SOLENOID_BACKRIGHTRETRACT,
    PCM_CLIMBCANID2,
    SOLENOID_LOWERFRONTEXTEND, SOLENOID_LOWERFRONTRETRACT, SOLENOID_LOWERBACKEXTEND, SOLENOID_LOWERBACKRETRACT, SOLENOID_ASCENDASSISTBACKLEFTEXTEND, SOLENOID_ASCENDASSISTBACKLEFTRETRACT, SOLENOID_ASCENDASSISTBACKRIGHTEXTEND, SOLENOID_ASCENDASSISTBACKRIGHTRETRACT,
    PCM_ARMCANID,
    SOLENOID_PUSHERPUSH, SOLENOID_PUSHERRETRACT, SOLENOID_ARMFORWARD, SOLENOID_ARMREVERSE, FRONTLEFTCLIMBWHEEL, FRONTRIGHTCLIMBWHEEL

  }

  public enum RobotTypes {
    ALBERT, BRIEFCASE, DEEPSPACE_ROBOT, DEADPIXEL, DASHBOARD
  }

  public EnumMap<MapKeys, Integer> briefcase = new EnumMap<MapKeys, Integer>(MapKeys.class);

  public EnumMap<MapKeys, Integer> albert = new EnumMap<MapKeys, Integer>(MapKeys.class);

  public EnumMap<MapKeys, Integer> deadpixel = new EnumMap<MapKeys, Integer>(MapKeys.class);

  public EnumMap<MapKeys, Integer> dashboard = new EnumMap<MapKeys, Integer>(MapKeys.class);

  boolean m_Initialized = false;

  public RobotMap() {

    m_Initialized = false;

    // BRIEFCASE IDs BEGIN HERE #########################################

    briefcase.put(MapKeys.DRIVE_FRONTLEFT, 0);
    briefcase.put(MapKeys.DRIVE_FRONTRIGHT, 0);
    briefcase.put(MapKeys.DRIVE_BACKLEFT, 0);
    briefcase.put(MapKeys.DRIVE_BACKRIGHT, 0);
    briefcase.put(MapKeys.FRONTLEFTCLIMBWHEEL, 0);
    briefcase.put(MapKeys.FRONTLEFTCLIMBWHEEL, 0);

    briefcase.put(MapKeys.SLIDE, 0);
    briefcase.put(MapKeys.LIFT_LEFT, 8);
    briefcase.put(MapKeys.LIFT_RIGHT, 4);

    briefcase.put(MapKeys.PCM_CLIMBCANID, 0);
    briefcase.put(MapKeys.SOLENOID_FRONTEXTEND, 0);
    briefcase.put(MapKeys.SOLENOID_FRONTRETRACT, 0);
    briefcase.put(MapKeys.SOLENOID_BACKEXTEND, 0);
    briefcase.put(MapKeys.SOLENOID_BACKRETRACT, 0);
    briefcase.put(MapKeys.SOLENOID_FRONTRIGHTEXTEND, 0);
    briefcase.put(MapKeys.SOLENOID_FRONTRIGHTRETRACT, 0);
    briefcase.put(MapKeys.SOLENOID_BACKRIGHTEXTEND, 6);
    briefcase.put(MapKeys.SOLENOID_BACKRIGHTRETRACT, 7);

    briefcase.put(MapKeys.PCM_CLIMBCANID2, 0);
    briefcase.put(MapKeys.SOLENOID_LOWERFRONTEXTEND, 0);
    briefcase.put(MapKeys.SOLENOID_LOWERFRONTRETRACT, 1);
    briefcase.put(MapKeys.SOLENOID_LOWERBACKEXTEND, 2);
    briefcase.put(MapKeys.SOLENOID_LOWERBACKRETRACT, 3);
    briefcase.put(MapKeys.SOLENOID_ASCENDASSISTBACKLEFTEXTEND, 4);
    briefcase.put(MapKeys.SOLENOID_ASCENDASSISTBACKLEFTRETRACT, 5);
    briefcase.put(MapKeys.SOLENOID_ASCENDASSISTBACKRIGHTEXTEND, 6);
    briefcase.put(MapKeys.SOLENOID_ASCENDASSISTBACKRIGHTRETRACT, 7);

    briefcase.put(MapKeys.PCM_ARMCANID, 0);
    briefcase.put(MapKeys.SOLENOID_PUSHERPUSH, 0);
    briefcase.put(MapKeys.SOLENOID_PUSHERRETRACT, 1);
    briefcase.put(MapKeys.SOLENOID_ARMFORWARD, 2);
    briefcase.put(MapKeys.SOLENOID_ARMREVERSE, 3);

    // ALBERT IDs BEGIN HERE #########################################

    albert.put(MapKeys.DRIVE_FRONTLEFT, 20);
    albert.put(MapKeys.DRIVE_FRONTRIGHT, 11);
    albert.put(MapKeys.DRIVE_BACKLEFT, 21);
    albert.put(MapKeys.DRIVE_BACKRIGHT, 10);

    albert.put(MapKeys.FRONTLEFTCLIMBWHEEL, 2);
    albert.put(MapKeys.FRONTLEFTCLIMBWHEEL, 3);

    albert.put(MapKeys.SLIDE, 4);
    
    albert.put(MapKeys.LIFT_LEFT, 0);
    albert.put(MapKeys.LIFT_RIGHT, 0);

    albert.put(MapKeys.PCM_CLIMBCANID, 5);
    albert.put(MapKeys.SOLENOID_FRONTEXTEND, 0);
    albert.put(MapKeys.SOLENOID_FRONTRETRACT, 1);
    albert.put(MapKeys.SOLENOID_BACKEXTEND, 2);
    albert.put(MapKeys.SOLENOID_BACKRETRACT, 3);
    albert.put(MapKeys.SOLENOID_FRONTRIGHTEXTEND,6);
    albert.put(MapKeys.SOLENOID_FRONTRIGHTRETRACT, 7);
    albert.put(MapKeys.SOLENOID_BACKRIGHTEXTEND, 4);
    albert.put(MapKeys.SOLENOID_BACKRIGHTRETRACT, 5);

    albert.put(MapKeys.PCM_CLIMBCANID2, 6);
    albert.put(MapKeys.SOLENOID_LOWERFRONTEXTEND, 0);
    albert.put(MapKeys.SOLENOID_LOWERFRONTRETRACT, 1);
    albert.put(MapKeys.SOLENOID_LOWERBACKEXTEND, 2);
    albert.put(MapKeys.SOLENOID_LOWERBACKRETRACT, 3);
    albert.put(MapKeys.SOLENOID_ASCENDASSISTBACKLEFTEXTEND, 4);
    albert.put(MapKeys.SOLENOID_ASCENDASSISTBACKLEFTRETRACT, 5);
    albert.put(MapKeys.SOLENOID_ASCENDASSISTBACKRIGHTEXTEND, 6);
    albert.put(MapKeys.SOLENOID_ASCENDASSISTBACKRIGHTRETRACT, 7);

    albert.put(MapKeys.PCM_ARMCANID, 7);
    albert.put(MapKeys.SOLENOID_PUSHERPUSH, 0);
    albert.put(MapKeys.SOLENOID_PUSHERRETRACT, 1);
    albert.put(MapKeys.SOLENOID_ARMFORWARD, 2);
    albert.put(MapKeys.SOLENOID_ARMREVERSE, 3);

    // DEADPIXEL IDs BEGIN HERE #########################################

    deadpixel.put(MapKeys.DRIVE_FRONTLEFT, 0);
    deadpixel.put(MapKeys.DRIVE_FRONTRIGHT, 0);
    deadpixel.put(MapKeys.DRIVE_BACKLEFT, 0);
    deadpixel.put(MapKeys.DRIVE_BACKRIGHT, 0);

    deadpixel.put(MapKeys.SLIDE, 0);
    deadpixel.put(MapKeys.LIFT_LEFT, 0);
    deadpixel.put(MapKeys.LIFT_RIGHT, 0);

    deadpixel.put(MapKeys.PCM_CLIMBCANID, 5);
    deadpixel.put(MapKeys.SOLENOID_FRONTEXTEND, 0);
    deadpixel.put(MapKeys.SOLENOID_FRONTRETRACT, 1);
    deadpixel.put(MapKeys.SOLENOID_BACKEXTEND, 2);
    deadpixel.put(MapKeys.SOLENOID_BACKRETRACT, 3);
    deadpixel.put(MapKeys.SOLENOID_FRONTRIGHTEXTEND, 4);
    deadpixel.put(MapKeys.SOLENOID_FRONTRIGHTRETRACT, 5);
    deadpixel.put(MapKeys.SOLENOID_BACKRIGHTEXTEND, 6);
    deadpixel.put(MapKeys.SOLENOID_BACKRIGHTRETRACT, 7);

    deadpixel.put(MapKeys.PCM_CLIMBCANID2, 0);
    deadpixel.put(MapKeys.SOLENOID_LOWERFRONTEXTEND, 0);
    deadpixel.put(MapKeys.SOLENOID_LOWERFRONTRETRACT, 0);
    deadpixel.put(MapKeys.SOLENOID_LOWERBACKEXTEND, 0);
    deadpixel.put(MapKeys.SOLENOID_LOWERBACKRETRACT, 0);
    deadpixel.put(MapKeys.SOLENOID_ASCENDASSISTBACKLEFTEXTEND, 0);
    deadpixel.put(MapKeys.SOLENOID_ASCENDASSISTBACKLEFTRETRACT, 0);
    deadpixel.put(MapKeys.SOLENOID_ASCENDASSISTBACKRIGHTEXTEND, 0);
    deadpixel.put(MapKeys.SOLENOID_ASCENDASSISTBACKRIGHTRETRACT, 0);

    deadpixel.put(MapKeys.PCM_ARMCANID, 0);
    deadpixel.put(MapKeys.SOLENOID_PUSHERPUSH, 0);
    deadpixel.put(MapKeys.SOLENOID_PUSHERRETRACT, 0);
    deadpixel.put(MapKeys.SOLENOID_ARMFORWARD, 0);
    deadpixel.put(MapKeys.SOLENOID_ARMREVERSE, 0);

  }

  public void initAllMaps() {
    initDashboardMap();
    m_Initialized = true;

  }

  public void initDashboardMap() {

    Preferences prefs = Preferences.getInstance();

    //DASHBOARD IDs BEGIN HERE ##########################################
    //TODO-MR Put real Can Ids
    int CanID =  prefs.getInt("CanID_Test", 20);
    System.out.printf("------------- Got CanID %d \n", CanID);
    dashboard.put(MapKeys.DRIVE_FRONTLEFT, prefs.getInt("CanID_DriveFrontLeft", 2));
    dashboard.put(MapKeys.DRIVE_FRONTRIGHT, prefs.getInt("CanID_DriveFrontRight", 10));
    dashboard.put(MapKeys.DRIVE_BACKLEFT, prefs.getInt("CanID_DriveBackLeft", 1));
    dashboard.put(MapKeys.DRIVE_BACKRIGHT, prefs.getInt("CanID_DriveBackRight", 11));

    dashboard.put(MapKeys.FRONTRIGHTCLIMBWHEEL, prefs.getInt("CanID_FrontRightClimbWheel", 9));
    dashboard.put(MapKeys.FRONTLEFTCLIMBWHEEL, prefs.getInt("CanID_FrontLeftClimbWheel", 12));

    dashboard.put(MapKeys.SLIDE, prefs.getInt("CanID_Slide", 3));
    dashboard.put(MapKeys.LIFT_LEFT, prefs.getInt("CanID_LiftLeft", 4));
    dashboard.put(MapKeys.LIFT_RIGHT, prefs.getInt("CanID_LiftRight", 8));

    dashboard.put(MapKeys.PCM_CLIMBCANID, prefs.getInt("CanID_PCM_ClimbID1", 5));
    dashboard.put(MapKeys.SOLENOID_FRONTEXTEND, prefs.getInt("CanID_FrontExtend", 0));
    dashboard.put(MapKeys.SOLENOID_FRONTRETRACT, prefs.getInt("CanID_FrontRetract", 1));
    dashboard.put(MapKeys.SOLENOID_BACKEXTEND, prefs.getInt("CanID_BackExtend", 2));
    dashboard.put(MapKeys.SOLENOID_BACKRETRACT, prefs.getInt("CanID_BackRetract", 3));
    dashboard.put(MapKeys.SOLENOID_LOWERFRONTEXTEND, prefs.getInt("CanID_LowerFrontExtend", 4));
    dashboard.put(MapKeys.SOLENOID_LOWERFRONTRETRACT, prefs.getInt("CanID_LowerFrontRetract", 5));
    dashboard.put(MapKeys.SOLENOID_LOWERBACKEXTEND, prefs.getInt("CanID_LowerBackExtend", 6));
    dashboard.put(MapKeys.SOLENOID_LOWERBACKRETRACT, prefs.getInt("CanID_LowerBackRetract", 7));
    /*
    dashboard.put(MapKeys.SOLENOID_FRONTRIGHTEXTEND, prefs.getInt("CanID_FrontRightExtend", 6));
    dashboard.put(MapKeys.SOLENOID_FRONTRIGHTRETRACT, prefs.getInt("CanID_FrontRightRetract", 7));
    dashboard.put(MapKeys.SOLENOID_BACKRIGHTEXTEND, prefs.getInt("CanID_BackRightExtend", 4));
    dashboard.put(MapKeys.SOLENOID_BACKRIGHTRETRACT, prefs.getInt("CanID_BackRightRetract", 5));
    */

    dashboard.put(MapKeys.PCM_CLIMBCANID2, prefs.getInt("CanID_PCM_ClimbID2", 6));
    dashboard.put(MapKeys.SOLENOID_ASCENDASSISTBACKLEFTEXTEND, prefs.getInt("CanID_AscendAssistBackLeftExtend", 0));
    dashboard.put(MapKeys.SOLENOID_ASCENDASSISTBACKLEFTRETRACT, prefs.getInt("CanID_AscendAssistBackLeftRetract", 1));
    dashboard.put(MapKeys.SOLENOID_ASCENDASSISTBACKRIGHTEXTEND, prefs.getInt("CanID_AscendAssistBackRightExtend", 2));
    dashboard.put(MapKeys.SOLENOID_ASCENDASSISTBACKRIGHTRETRACT, prefs.getInt("CanID_AScendAssistBackRightRetract", 3));
    dashboard.put(MapKeys.SOLENOID_PUSHERPUSH, prefs.getInt("CanID_GrabberExtend", 4));
    dashboard.put(MapKeys.SOLENOID_PUSHERRETRACT, prefs.getInt("CanID_GrabberRetract", 5));
    dashboard.put(MapKeys.SOLENOID_ARMFORWARD, prefs.getInt("CanID_ArmExtend", 6));
    dashboard.put(MapKeys.SOLENOID_ARMREVERSE, prefs.getInt("CanID_ArmRetract", 7));

    dashboard.put(MapKeys.PCM_ARMCANID, prefs.getInt("CanID_PCM_ArmID", 7));
  }



  public RobotTypes activeRobot = RobotTypes.DASHBOARD;

  public int getId(MapKeys key) {
    System.out.printf("In Get ID. %s\n", key.toString());
    if(!m_Initialized) {
      initAllMaps();
      System.out.printf("Initialized all maps.\n");
    }

    if(activeRobot == RobotTypes.DASHBOARD) {
      System.out.printf("Getting Dashboard key %d.\n", dashboard.get(key));
      return dashboard.get(key);
    }

    else if(activeRobot == RobotTypes.BRIEFCASE) {
      System.out.printf("Getting Briefcase key %d.\n", briefcase.get(key));
      return briefcase.get(key);
    }
    else if(activeRobot == RobotTypes.DEADPIXEL) {
      System.out.printf("Getting Deadpixel key %d.\n", deadpixel.get(key));
      return deadpixel.get(key);
    }
    else {
      System.out.printf("Getting Albert key %d.\n", albert.get(key));
      return albert.get(key);
    }
  
  }

 

  


}