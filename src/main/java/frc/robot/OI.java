/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import frc.robot.commands.PositionCommand;
import frc.robot.commands.PositionUpdateCommand;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
  private static final double MAX_POS_DEG = 90.0;
  private static final int POS_HOLD_AXIS = 0;
  private static final int POS_HOLD_TOGGLE_BUTTON = 1;
  private static final int POS_HOLD_UPDATE_BUTTON = 2;

  private static final Joystick m_controller = new Joystick(0);
  private static final JoystickButton m_x = new JoystickButton(m_controller, POS_HOLD_TOGGLE_BUTTON);
  private static final JoystickButton m_y = new JoystickButton(m_controller, POS_HOLD_UPDATE_BUTTON);
  
  public OI(){
    m_x.whenPressed(new PositionCommand());
    m_y.whenPressed(new PositionUpdateCommand());
  }
  
  public double readPositionDeg() {
    double input = m_controller.getRawAxis(POS_HOLD_AXIS);
    double holdPos = input * MAX_POS_DEG;

    return holdPos;
	}
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
}
