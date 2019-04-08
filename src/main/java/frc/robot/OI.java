package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import frc.robot.commands.*;
import java.lang.Math;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
  //// CREATING BUTTONS
  // One type of button is a joystick button which is any button on a
  //// joystick.
  // You create one by telling it which joystick it's on and which button
  // number it is.
  // Joystick stick = new Joystick(port);
  // Button button = new JoystickButton(stick, buttonNumber);
  // There are a few additional built in buttons you can use. Additionally,
  // by subclassing Button you can create custom triggers and bind those to
  // commands the same as any other Button.

  //// TRIGGERING COMMANDS WITH BUTTONS
  // Once you have a button, it's trivial to bind it to a button in one of
  // three ways:

  // Start the command when the button is pressed and let it run the command
  // until it is finished as determined by it's isFinished method.
  // button.whenPressed(new ExampleCommand());

  // Run the command while the button is being held down and interrupt it once
  // the button is released.
  // button.whileHeld(new ExampleCommand());

  // Start the command when the button is released and let it run the command
  // until it is finished as determined by it's isFinished method.
  // button.whenReleased(new ExampleCommand());

  Joystick joy = new Joystick(0);

  Preferences prefs = Preferences.getInstance();
  
//  private double TURNMODIFIER;
//  private double STRAIGHMODIFIER;
  private double LIFTMODIFIER;
  public double alphaD = 0.85;
  public double alphaT = 0.65;
  public double betaD = 0.60; //was .45, 55
  public double betaT = 0.35;
  public double gammaD = 0.9;
  public double gammaT = 0.75;
  private final double TD2_ALPHA = 0.4;
  private final double TD2_BETA = 0.6;
  private final double TT2_ALPHA = 0.4;
  private final double TT2_BETA = 0.4;
  private final double TS_ALPHA = 0.4;
  private final double TS_BETA = 0.6;
  private final double DEADBAND = 0.05;
  

  public final int CAMERA_BUTTON = 2;
  private final int Lvl2DESC_BUTTON = 7;
  private final int Lvl2CLIMB_BUTTON = 8;
  private final int Lvl3CLIMB_BUTTON = 3;
  private final int CLIMBNEXT_BUTTON = 6;
  private final int CLIMBPREV_BUTTON = 5;

  public JoystickButton bButton1 = new JoystickButton(joy, CAMERA_BUTTON);
	public JoystickButton backButton1 = new JoystickButton(joy, Lvl2DESC_BUTTON);
	public JoystickButton startButton1 = new JoystickButton(joy, Lvl2CLIMB_BUTTON);
  public JoystickButton leftBumper1 = new JoystickButton(joy, CLIMBPREV_BUTTON);
  public JoystickButton rightBumper1 = new JoystickButton(joy, CLIMBNEXT_BUTTON);
  public JoystickButton xButton1 = new JoystickButton(joy, Lvl3CLIMB_BUTTON);

  Joystick joy2 = new Joystick(1);


  private final int ARM_BUTTON = 2;
  private final int HIGHHATCH_BUTTON = 3;
  private final int MIDHATCH_BUTTON = 8; 
  private final int LOWHATCH_BUTTON = 7;
  private final int CLAMP_BUTTON = 4;
  private final int BLIND = 6;
  //TODO-ADD private final int SLIDERESET_BUTTON = 0;
  public JoystickButton yButton2 = new JoystickButton(joy2, ARM_BUTTON);
  public JoystickButton xButton2 = new JoystickButton(joy2, HIGHHATCH_BUTTON);
  public JoystickButton startButton2 = new JoystickButton(joy2, MIDHATCH_BUTTON);
  public JoystickButton backButton2 = new JoystickButton(joy2, LOWHATCH_BUTTON);
  public JoystickButton bButton2 = new JoystickButton(joy2, CLAMP_BUTTON);
  public JoystickButton leftBumper2 = new JoystickButton(joy2, BLIND);
  //TODO-ADD public POVButton dpadUp = new POVButton(joys, SLIDERESET_BUTTON);
 //public jous



  public OI () {

    //bButton1.toggleWhenPressed(new CameraToggle);
    
    backButton1.whenPressed(new DescendCommandGroup()); //initiate lvl 2 descent
    startButton1.whenPressed(new L2AscendCommandGroup()); //initiate lvl 2 climb
    xButton1.whenPressed(new startL3AscendCommand()); // intiate lvl 3 climb
    
    leftBumper2.whenPressed(new BlindCommand());
    
    leftBumper1.whenPressed(new ClimbPrevCommand()); //press to go to the previous climb state in the sequence
    rightBumper1.whenPressed(new ClimbNextCommand()); //press to go to the next climb state in the sequence
    
    
    yButton2.whenPressed(new PneumaticArmExtend()); //Press for arm extend
    yButton2.whenReleased(new PneumaticArmRetract()); //release for arm retract

    xButton2.whenPressed(new LiftHighCommand()); //move lift to mid hatch position
    startButton2.whenPressed(new LiftMidCommand()); //move hatch to 
    backButton2.whenPressed(new LiftLowCommand()); //move lift to low hatch position
    
    bButton2.whenPressed(new PneumaticExtendCommand()); //press for clamp
    bButton2.whenReleased(new PneumaticRetractCommand()); //release for clamp

    

    
    

    
  }
 


        //REGULAR DRIVE
  /*public double getForwardValue() {
    STRAIGHMODIFIER = prefs.getDouble("Drive_Straight_Modifier", -0.9);
    if (Math.abs(joy.getRawAxis(1)) < .1) {
      return 0;
    } else {
      return joy.getRawAxis(1) * STRAIGHMODIFIER;
    }
  

  }
  public double getTurnValue() {
    TURNMODIFIER = prefs.getDouble("Drive_Turn_Modifier", 0.6);
    if (Math.abs(joy.getRawAxis(4)) < .1) {
      return 0;
    } else {
      return joy.getRawAxis(4) * TURNMODIFIER;
    }
  }
  */

          //TURBO MODE
  public double turbomodeDrive() {
    if (Math.abs(joy.getRawAxis(1)) < DEADBAND) { // was 0.1
      return 0;
      //deadband
    }
    else if (Math.abs(joy.getRawAxis(1)) < alphaD) {
      return -(joy.getRawAxis(1) * (betaD/alphaD));
    }
    else {
      return -(Math.signum(joy.getRawAxis(1)) * ((betaD) + ((gammaD-betaD)/(1-alphaD)) * (Math.abs(joy.getRawAxis(1))-alphaD)));
    }
  }

  public double turbomodeDrive2() {
    double x = Math.abs(joy.getRawAxis(1));
    double sign = Math.signum(joy.getRawAxis(1));
    if (x < DEADBAND) { // was 0.1
      return 0;
      //deadband
    }
    else {
      x -= DEADBAND;
      double x2 = x*x;
      return -(sign *(TD2_ALPHA*x + TD2_BETA*x2));
    }
  }
  

  public double turbomodeTurn() {
    if (Math.abs(joy.getRawAxis(4)) < DEADBAND) { // was 0.1
      return 0;
      //deadband
    }
    else if (Math.abs(joy.getRawAxis(4)) < alphaT) {
      return joy.getRawAxis(4) * (betaT/alphaT);
    }
    else {
      return (Math.signum(joy.getRawAxis(4)) * ((betaT) + ((gammaT-betaT)/(1-alphaT)) * (Math.abs(joy.getRawAxis(4))-alphaT)));
  
    }
  }

  public double turbomodeTurn2() {
    double x = Math.abs(joy.getRawAxis(4));
    double sign = Math.signum(joy.getRawAxis(4));
    if (x < DEADBAND) { // was 0.1
      return 0;
      //deadband
    }
    else {
      x -= DEADBAND;
      double x2 = x*x;
      return sign *(TT2_ALPHA*x + TT2_BETA*x2);
    }
  }
  

  public double getLiftValue() {
    LIFTMODIFIER = prefs.getDouble("Lift_Modifier", -0.8);
    if (Math.abs(joy2.getRawAxis(1)) < 0.1) {
      return 0;
    } else {
      return joy2.getRawAxis(1) * LIFTMODIFIER;
    }
    
  }

  public double getSlideValue() {
    if (Math.abs(joy2.getRawAxis(2) - joy2.getRawAxis(3)) < 0.1){
      return 0;
    } else {
      return joy2.getRawAxis(2) - joy2.getRawAxis(3);
    }
 
  }

  public double turbomodeSlide() {
    double x = Math.abs(joy2.getRawAxis(2) - joy2.getRawAxis(3));
    double sign = Math.signum(joy2.getRawAxis(2) - joy2.getRawAxis(3));
    if (x < DEADBAND) { // was 0.1
      return 0;
      //deadband
    }
    else {
      x -= DEADBAND;
      double x2 = x*x;
      return sign *(TS_ALPHA*x + TS_BETA*x2);
    }
  }


  


}
