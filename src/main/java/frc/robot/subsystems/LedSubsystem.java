/** 
/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;
import frc.robot.Robot;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.commands.*;
import java.lang.Math;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

// TODO-DW : create methods to reflect status, joystick, button, team.

/**
 * An example subsystem.  You can replace me with your own Subsystem.
 */

public class LedSubsystem extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.

  

  class Pixel {
    byte m_r;
    byte m_g;
    byte m_b;
    
    public Pixel() {
      m_r = 0;
      m_g = 0;
      m_b  = 0;
    }

    public void copy(Pixel other) {
      m_r = other.m_r;
      m_g = other.m_g;
      m_b = other.m_b;
    }

    public void set(byte r, byte g, byte b) {
      m_r = r;
      m_g = g;
      m_b = b;
    }
  };

  SPI m_spi;
  byte m_power;
  
  static final int NUM_PIXELS = 60;
  static final int MAX_SPI_WRITE = 127;
  Pixel m_pixel[];
  final byte m_frame[] = new byte[NUM_PIXELS*4 + 8];
  byte m_writeBuf[] = new byte[MAX_SPI_WRITE];

  public LedSubsystem() {
    m_power = 1;
    m_pixel = new Pixel[NUM_PIXELS];
    for (int n = 0; n < NUM_PIXELS; n++) {
      m_pixel[n] = new Pixel();
    }

    // Init SPI port
    m_spi = new SPI(SPI.Port.kOnboardCS0);
    m_spi.setClockRate(512000);
    m_spi.setClockActiveLow();
    m_spi.setChipSelectActiveLow();
    m_spi.setSampleDataOnRising();
    m_spi.setMSBFirst();

    blank();
    updateLeds();
  }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }

  @Override 
  public void periodic() {
    /*for (int n = NUM_PIXELS-1; n > 0; n--) {
      // shift all pixels by one
      m_pixel[n].copy(m_pixel[n-1]);
      // write one new pixel
      m_pixel[0].set((byte)0, (byte)80, (byte)0); // ___ r _____
    }
*/
//System.out.printf("The output is %d %n", Robot.m_lift.m_motorup1.getOutputCurrent());
    updateLeds();
  

  }

  private void blank() {
    for (int n = 0; n < NUM_PIXELS; n++) {
      m_pixel[n].set((byte)0, (byte)0, (byte)0);
    }
  }

  private void sendFrame() {
    // SPI device can send a max of 128 bytes and that's not enough for a frame
    // of LED data.  So we write it in chunks.
    int cursor = 0;

    for (int n = 0; n < m_frame.length; n++) {
      m_writeBuf[cursor] = m_frame[n];
      cursor += 1;
      if (cursor == MAX_SPI_WRITE) {
        // we have a full write buff, send it now.
        m_spi.write(m_writeBuf, cursor);
        cursor = 0;
      }
    }

    // now write the last part, if any
    if (cursor > 0) {
      m_spi.write(m_writeBuf, cursor);
      cursor = 0;
    }
  }

  private void updateLeds() {
    int wrote = 0;

    // start sequence
    int cursor = 0;
    m_frame[cursor++] = (byte)0;
    m_frame[cursor++] = (byte)0;
    m_frame[cursor++] = (byte)0;
    m_frame[cursor++] = (byte)0;

    // Pixel data
    for (int n = 0; n < NUM_PIXELS; n++) {
      // power multiplier
      m_frame[cursor++] = (byte) (m_power + 0xE0);
      
     
      if (n % 10 == 0) {
        m_frame[cursor++] = (byte) 230;
        m_frame[cursor++] = (byte) 230;
        m_frame[cursor++] = (byte) 230;
      } else {
        m_frame[cursor++] = m_pixel[n].m_r;
        m_frame[cursor++] = m_pixel[n].m_b;
        m_frame[cursor++] = m_pixel[n].m_g;
      }
    }

    // stop sequence
    m_frame[cursor++] = (byte) 0xFF;
    m_frame[cursor++] = (byte) 0xFF;
    m_frame[cursor++] = (byte) 0xFF;
    m_frame[cursor++] = (byte) 0xFF;

    // send it out the SPI port
    sendFrame();
  }

//public void LED(int n, boolean state) {
  public void LED(boolean state){
    /*if (){         
      m_pixel[n].set((byte) 50,(byte) 0,(byte) 0);
    } else {
      m_pixel[n].set((byte)0, (byte)0, (byte)0);
    }
    */
    /*if (state){         
      m_pixel[n].set((byte)blue, (byte) red,(byte) green);
    } else {
      m_pixel[n].set((byte)0, (byte)0, (byte)0);
    } Working but need to initialize each LED */
    for (int n = NUM_PIXELS-1; n > 0; n--) {
      // shift all pixels by one
      m_pixel[n].copy(m_pixel[n-1]);

      // write one new pixel
      if(state) {
      m_pixel[1].set((byte)0, (byte)255, (byte)0);
      m_pixel[2].set((byte)0, (byte)255, (byte)127);    //BRG
      m_pixel[3].set((byte)0, (byte)255, (byte)255);
      m_pixel[4].set((byte)0, (byte)0, (byte)255);
      m_pixel[5].set((byte)255, (byte)0, (byte)0);
      m_pixel[6].set((byte)130, (byte)75, (byte)0);
      m_pixel[7].set((byte)255, (byte)143, (byte)0);
      m_pixel[8].set((byte)0, (byte)255, (byte)0);
      m_pixel[9].set((byte)0, (byte)255, (byte)127);    //BRG
      m_pixel[10].set((byte)0, (byte)255, (byte)255);
      m_pixel[11].set((byte)0, (byte)0, (byte)255);
      m_pixel[12].set((byte)255, (byte)0, (byte)0);
      m_pixel[13].set((byte)130, (byte)75, (byte)0);
      m_pixel[14].set((byte)255, (byte)143, (byte)0);
      m_pixel[15].set((byte)0, (byte)255, (byte)0);
      m_pixel[16].set((byte)0, (byte)255, (byte)127);    //BRG
      m_pixel[17].set((byte)0, (byte)255, (byte)255);
      m_pixel[18].set((byte)0, (byte)0, (byte)255);
      m_pixel[19].set((byte)255, (byte)0, (byte)0);
      m_pixel[20].set((byte)130, (byte)75, (byte)0);
      m_pixel[21].set((byte)255, (byte)143, (byte)0);
      m_pixel[22].set((byte)0, (byte)255, (byte)0);
      m_pixel[23].set((byte)0, (byte)255, (byte)127);    //BRG
      m_pixel[24].set((byte)0, (byte)255, (byte)255);
      m_pixel[25].set((byte)0, (byte)0, (byte)255);
      m_pixel[26].set((byte)255, (byte)0, (byte)0);
      m_pixel[27].set((byte)130, (byte)75, (byte)0);
      m_pixel[28].set((byte)255, (byte)143, (byte)0);
      m_pixel[29].set((byte)0, (byte)255, (byte)0);
      m_pixel[30].set((byte)0, (byte)255, (byte)127);    //BRG
      m_pixel[31].set((byte)0, (byte)255, (byte)255);
      m_pixel[32].set((byte)0, (byte)0, (byte)255);
      m_pixel[33].set((byte)255, (byte)0, (byte)0);
      m_pixel[34].set((byte)130, (byte)75, (byte)0);
      m_pixel[35].set((byte)255, (byte)143, (byte)0);
      m_pixel[36].set((byte)0, (byte)255, (byte)0);
      m_pixel[37].set((byte)0, (byte)255, (byte)127);    //BRG
      m_pixel[38].set((byte)0, (byte)255, (byte)255);
      m_pixel[39].set((byte)0, (byte)0, (byte)255);
      m_pixel[40].set((byte)255, (byte)0, (byte)0);
      m_pixel[41].set((byte)130, (byte)75, (byte)0);
      m_pixel[42].set((byte)255, (byte)143, (byte)0);
      m_pixel[43].set((byte)0, (byte)255, (byte)0);
      m_pixel[44].set((byte)0, (byte)255, (byte)127);    //BRG
      m_pixel[45].set((byte)0, (byte)255, (byte)255);
      m_pixel[46].set((byte)0, (byte)0, (byte)255);
      m_pixel[47].set((byte)255, (byte)0, (byte)0);
      m_pixel[48].set((byte)130, (byte)75, (byte)0);
      m_pixel[50].set((byte)0, (byte)255, (byte)0);
      m_pixel[51].set((byte)0, (byte)255, (byte)127);    //BRG
      m_pixel[52].set((byte)0, (byte)255, (byte)255);
      m_pixel[53].set((byte)0, (byte)0, (byte)255);
      m_pixel[54].set((byte)0, (byte)255, (byte)0);
      m_pixel[55].set((byte)0, (byte)255, (byte)127);    //BRG
      m_pixel[56].set((byte)0, (byte)255, (byte)255);
      m_pixel[57].set((byte)0, (byte)0, (byte)255);
      m_pixel[58].set((byte)0, (byte)255, (byte)0);
      m_pixel[59].set((byte)0, (byte)255, (byte)127);



    } else{
      m_pixel[0].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[1].set((byte) 0,(byte) 0,(byte) 0);     //brg
    m_pixel[2].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[3].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[4].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[5].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[6].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[7].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[8].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[9].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[10].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[11].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[12].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[13].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[14].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[15].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[16].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[17].set((byte) 0,(byte) 0,(byte) 0);     //brg
    m_pixel[18].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[19].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[20].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[21].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[22].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[23].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[24].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[25].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[26].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[27].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[28].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[29].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[30].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[31].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[32].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[33].set((byte) 0,(byte) 0,(byte) 0);     //brg
    m_pixel[34].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[35].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[36].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[37].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[38].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[39].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[40].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[41].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[42].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[43].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[44].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[45].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[46].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[47].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[48].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[49].set((byte) 0,(byte) 0,(byte) 0);     //brg
    m_pixel[50].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[51].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[52].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[53].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[54].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[55].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[56].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[57].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[58].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[59].set((byte) 0,(byte) 0,(byte) 0);
    }
  }

    // ___ r _____
    //sendFrame();
    
  }
public void LiftDrive(){
  if ((Robot.m_lift.m_motorup1 != null) && (Robot.m_lift.m_motorup2 != null)){
    if (Math.abs(Robot.m_lift.m_motorup1.getMotorOutputVoltage()) > 0){         
      m_pixel[49].set((byte) 255,(byte) 171,(byte) 0);
      m_pixel[50].set((byte) 255,(byte) 171,(byte) 0);
      m_pixel[51].set((byte) 255,(byte) 171,(byte) 0);
      m_pixel[52].set((byte) 255,(byte) 171,(byte) 0);
      m_pixel[53].set((byte) 255,(byte) 171,(byte) 0);
      m_pixel[54].set((byte) 255,(byte) 171,(byte) 0);
      m_pixel[55].set((byte) 255,(byte) 171,(byte) 0);
      m_pixel[56].set((byte) 255,(byte) 171,(byte) 0);
      m_pixel[57].set((byte) 255,(byte) 171,(byte) 0);
      m_pixel[58].set((byte) 255,(byte) 171,(byte) 0);

      
    } else {
      m_pixel[49].set((byte) 0,(byte) 0,(byte) 0);
      m_pixel[50].set((byte) 0,(byte) 0,(byte) 0);
      m_pixel[51].set((byte) 0,(byte) 0,(byte) 0);
      m_pixel[52].set((byte) 0,(byte) 0,(byte) 0);
      m_pixel[53].set((byte) 0,(byte) 0,(byte) 0);
      m_pixel[54].set((byte) 0,(byte) 0,(byte) 0);
      m_pixel[55].set((byte) 0,(byte) 0,(byte) 0);
      m_pixel[56].set((byte) 0,(byte) 0,(byte) 0);
      m_pixel[57].set((byte) 0,(byte) 0,(byte) 0);
      m_pixel[58].set((byte) 0,(byte) 0,(byte) 0);
    }
  }
}

 public void climbDescend(){
  if (Robot.m_climbSubsystem.m_autoDescend == true){
    m_pixel[0].set((byte) 0,(byte) 255,(byte) 165);
    m_pixel[1].set((byte) 0,(byte) 255,(byte) 165);     //brg
    m_pixel[2].set((byte) 0,(byte) 255,(byte) 165);
    m_pixel[3].set((byte) 0,(byte) 255,(byte) 165);
    m_pixel[4].set((byte) 0,(byte) 255,(byte) 165);
    m_pixel[5].set((byte) 0,(byte) 255,(byte) 165);
    m_pixel[6].set((byte) 0,(byte) 255,(byte) 165);
    m_pixel[7].set((byte) 0,(byte) 255,(byte) 165);
    m_pixel[8].set((byte) 0,(byte) 255,(byte) 165);
    m_pixel[9].set((byte) 0,(byte) 255,(byte) 165);
    m_pixel[10].set((byte) 0,(byte) 255,(byte) 165);
    m_pixel[11].set((byte) 0,(byte) 255,(byte) 165);
    m_pixel[12].set((byte) 0,(byte) 255,(byte) 165);
    m_pixel[13].set((byte) 0,(byte) 255,(byte) 165);
    m_pixel[14].set((byte) 0,(byte) 255,(byte) 165);
    m_pixel[15].set((byte) 0,(byte) 255,(byte) 165);
  } else {
    m_pixel[0].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[1].set((byte) 0,(byte) 0,(byte) 0);     //brg
    m_pixel[2].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[3].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[4].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[5].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[6].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[7].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[8].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[9].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[10].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[11].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[12].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[13].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[14].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[15].set((byte) 0,(byte) 0,(byte) 0);

  }
} 
public void climbL2(){
  if (Robot.m_climbSubsystem.m_autoL2Ascend == true){
    m_pixel[19].set((byte) 0,(byte) 255,(byte) 165);
    m_pixel[20].set((byte) 0,(byte) 255,(byte) 165);     //brg
    m_pixel[21].set((byte) 0,(byte) 255,(byte) 165);
    m_pixel[22].set((byte) 0,(byte) 255,(byte) 165);
    m_pixel[23].set((byte) 0,(byte) 255,(byte) 165);
    m_pixel[24].set((byte) 0,(byte) 255,(byte) 165);
    m_pixel[25].set((byte) 0,(byte) 255,(byte) 165);
    m_pixel[26].set((byte) 0,(byte) 255,(byte) 165);
    m_pixel[27].set((byte) 0,(byte) 255,(byte) 165);
    m_pixel[28].set((byte) 0,(byte) 255,(byte) 165);
    m_pixel[29].set((byte) 0,(byte) 255,(byte) 165);
    m_pixel[30].set((byte) 0,(byte) 255,(byte) 165);
    m_pixel[31].set((byte) 0,(byte) 255,(byte) 165);
    m_pixel[32].set((byte) 0,(byte) 255,(byte) 165);
    m_pixel[33].set((byte) 0,(byte) 255,(byte) 165);
    m_pixel[34].set((byte) 0,(byte) 255,(byte) 165);
    m_pixel[35].set((byte) 0,(byte) 255,(byte) 165);    
  } else {
    m_pixel[19].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[20].set((byte) 0,(byte) 0,(byte) 0);     //brg
    m_pixel[21].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[22].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[23].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[24].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[25].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[26].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[27].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[28].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[29].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[30].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[31].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[32].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[33].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[34].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[35].set((byte) 0,(byte) 0,(byte) 0);
  }

}
public void climbL3(){
  if (Robot.m_climbSubsystem.m_autoL3Ascend == true){
    m_pixel[39].set((byte) 0,(byte) 255,(byte) 165);
    m_pixel[40].set((byte) 0,(byte) 255,(byte) 165);     //brg
    m_pixel[41].set((byte) 0,(byte) 255,(byte) 165);
    m_pixel[42].set((byte) 0,(byte) 255,(byte) 165);
    m_pixel[43].set((byte) 0,(byte) 255,(byte) 165);
    m_pixel[44].set((byte) 0,(byte) 255,(byte) 165);
    m_pixel[45].set((byte) 0,(byte) 255,(byte) 165);
    m_pixel[46].set((byte) 0,(byte) 255,(byte) 165);
    m_pixel[47].set((byte) 0,(byte) 255,(byte) 165);
    m_pixel[48].set((byte) 0,(byte) 255,(byte) 165);
    m_pixel[49].set((byte) 0,(byte) 255,(byte) 165);
    m_pixel[50].set((byte) 0,(byte) 255,(byte) 165);
    m_pixel[51].set((byte) 0,(byte) 255,(byte) 165);
    m_pixel[52].set((byte) 0,(byte) 255,(byte) 165);
    m_pixel[53].set((byte) 0,(byte) 255,(byte) 165);
    m_pixel[54].set((byte) 0,(byte) 255,(byte) 165);
    m_pixel[55].set((byte) 0,(byte) 255,(byte) 165);    
  } else {
    m_pixel[39].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[40].set((byte) 0,(byte) 0,(byte) 0);     //brg
    m_pixel[41].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[42].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[43].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[44].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[45].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[46].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[47].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[48].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[49].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[50].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[51].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[52].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[53].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[54].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[55].set((byte) 0,(byte) 0,(byte) 0);
  }
}
public void DriveLeft(){
  double ledValue = 0;
  if (Robot.m_drivetrain.m_frontleft != null){
    ledValue = Math.abs(Math.min(Robot.m_drivetrain.m_frontleft.getOutputCurrent() * 0.1, 1) * 255);
  }
  //if (Math.abs(Robot.m_drivetrain.m_backleft.getMotorOutputVoltage()) > 0 && Math.abs(Robot.m_drivetrain.m_frontleft.getMotorOutputVoltage()) > 0) {
  if (true) {
      m_pixel[1].set((byte) ledValue,(byte) 0,(byte) 0);
      m_pixel[2].set((byte) ledValue,(byte) 0,(byte) 0);
      m_pixel[3].set((byte) ledValue,(byte) 0,(byte) 0);
      m_pixel[4].set((byte) ledValue,(byte) 0,(byte) 0);
      m_pixel[5].set((byte) ledValue,(byte) 0,(byte) 0);
      m_pixel[6].set((byte) ledValue,(byte) 0,(byte) 0);
      m_pixel[7].set((byte) ledValue,(byte) 0,(byte) 0);
      m_pixel[8].set((byte) ledValue,(byte) 0,(byte) 0);
      m_pixel[9].set((byte) ledValue,(byte) 0,(byte) 0);
      m_pixel[10].set((byte) ledValue,(byte) 0,(byte) 0);
      m_pixel[11].set((byte) ledValue,(byte) 0,(byte) 0);
      m_pixel[12].set((byte) ledValue,(byte) 0,(byte) 0);
      m_pixel[13].set((byte) ledValue,(byte) 0,(byte) 0);
      m_pixel[14].set((byte) ledValue,(byte) 0,(byte) 0);
      m_pixel[15].set((byte) ledValue,(byte) 0,(byte) 0);
      m_pixel[16].set((byte) ledValue,(byte) 0,(byte) 0);
      m_pixel[17].set((byte) ledValue,(byte) 0,(byte) 0);
      m_pixel[18].set((byte) ledValue,(byte) 0,(byte) 0);
      m_pixel[19].set((byte) ledValue,(byte) 0,(byte) 0);
      m_pixel[20].set((byte) ledValue,(byte) 0,(byte) 0);
      m_pixel[21].set((byte) ledValue,(byte) 0,(byte) 0);
      m_pixel[22].set((byte) ledValue,(byte) 0,(byte) 0);
      m_pixel[23].set((byte) ledValue,(byte) 0,(byte) 0);
      m_pixel[24].set((byte) ledValue,(byte) 0,(byte) 0);
      m_pixel[25].set((byte) ledValue,(byte) 0,(byte) 0);
      m_pixel[26].set((byte) ledValue,(byte) 0,(byte) 0);
      m_pixel[27].set((byte) ledValue,(byte) 0,(byte) 0);
      m_pixel[28].set((byte) ledValue,(byte) 0,(byte) 0);
      m_pixel[29].set((byte) ledValue,(byte) 0,(byte) 0);
      
    } 
    
  }


public void DriveRight(){
  double ledValue2 = 0;
  if (Robot.m_drivetrain.m_frontright != null){
    ledValue2 = Math.abs(Math.min(Robot.m_drivetrain.m_frontright.getOutputCurrent() * 0.1, 1) * 255);
  }
  if (true){
  //if (Math.abs(Robot.m_drivetrain.m_backright.getMotorOutputVoltage()) > 0 && Math.abs(Robot.m_drivetrain.m_frontright.getMotorOutputVoltage()) > 0) {
    m_pixel[30].set((byte) ledValue2,(byte) 0,(byte) 0);
    m_pixel[31].set((byte) ledValue2,(byte) 0,(byte) 0);
    m_pixel[32].set((byte) ledValue2,(byte) 0,(byte) 0);
    m_pixel[33].set((byte) ledValue2,(byte) 0,(byte) 0);
    m_pixel[34].set((byte) ledValue2,(byte) 0,(byte) 0);
    m_pixel[35].set((byte) ledValue2,(byte) 0,(byte) 0);
    m_pixel[36].set((byte) ledValue2,(byte) 0,(byte) 0);
    m_pixel[37].set((byte) ledValue2,(byte) 0,(byte) 0);
    m_pixel[38].set((byte) ledValue2,(byte) 0,(byte) 0);
    m_pixel[39].set((byte) ledValue2,(byte) 0,(byte) 0);
    m_pixel[40].set((byte) ledValue2,(byte) 0,(byte) 0);
    m_pixel[41].set((byte) ledValue2,(byte) 0,(byte) 0);
    m_pixel[42].set((byte) ledValue2,(byte) 0,(byte) 0);
    m_pixel[43].set((byte) ledValue2,(byte) 0,(byte) 0);
    m_pixel[44].set((byte) ledValue2,(byte) 0,(byte) 0);
    m_pixel[45].set((byte) ledValue2,(byte) 0,(byte) 0);
    m_pixel[46].set((byte) ledValue2,(byte) 0,(byte) 0);
    m_pixel[47].set((byte) ledValue2,(byte) 0,(byte) 0);
    m_pixel[48].set((byte) ledValue2,(byte) 0,(byte) 0);
    m_pixel[49].set((byte) ledValue2,(byte) 0,(byte) 0);
    m_pixel[50].set((byte) ledValue2,(byte) 0,(byte) 0);
    m_pixel[51].set((byte) ledValue2,(byte) 0,(byte) 0);
    m_pixel[52].set((byte) ledValue2,(byte) 0,(byte) 0);
    m_pixel[53].set((byte) ledValue2,(byte) 0,(byte) 0);
    m_pixel[54].set((byte) ledValue2,(byte) 0,(byte) 0);
    m_pixel[55].set((byte) ledValue2,(byte) 0,(byte) 0);
    m_pixel[56].set((byte) ledValue2,(byte) 0,(byte) 0);
    m_pixel[57].set((byte) ledValue2,(byte) 0,(byte) 0);
    m_pixel[58].set((byte) ledValue2,(byte) 0,(byte) 0);
    
  } 
  
}
public void grabberArm(){
  if (Robot.m_grabberArm.armGrabberLED == true){
    m_pixel[1].set((byte) 0,(byte) 255,(byte) 0);
    m_pixel[2].set((byte) 0,(byte) 255,(byte) 0);
    m_pixel[3].set((byte) 0,(byte) 255,(byte) 0);
    m_pixel[4].set((byte) 0,(byte) 255,(byte) 0);
    m_pixel[5].set((byte) 0,(byte) 255,(byte) 0);
    m_pixel[6].set((byte) 0,(byte) 255,(byte) 0);
    m_pixel[7].set((byte) 0,(byte) 255,(byte) 0);
    m_pixel[8].set((byte) 0,(byte) 255,(byte) 0);
    m_pixel[9].set((byte) 0,(byte) 255,(byte) 0);
    m_pixel[10].set((byte) 0,(byte) 255,(byte) 0);
    m_pixel[11].set((byte) 0,(byte) 255,(byte) 0);
    m_pixel[12].set((byte) 0,(byte) 255,(byte) 0);
    m_pixel[13].set((byte) 0,(byte) 255,(byte) 0);
    m_pixel[14].set((byte) 0,(byte) 255,(byte) 0);
    m_pixel[15].set((byte) 0,(byte) 255,(byte) 0);
    m_pixel[16].set((byte) 0,(byte) 255,(byte) 0);
    m_pixel[17].set((byte) 0,(byte) 255,(byte) 0);
    m_pixel[18].set((byte) 0,(byte) 255,(byte) 0);
    m_pixel[19].set((byte) 0,(byte) 255,(byte) 0);
    m_pixel[20].set((byte) 0,(byte) 255,(byte) 0);
    m_pixel[21].set((byte) 0,(byte) 255,(byte) 0);
    m_pixel[22].set((byte) 0,(byte) 255,(byte) 0);
    m_pixel[23].set((byte) 0,(byte) 255,(byte) 0);
    m_pixel[24].set((byte) 0,(byte) 255,(byte) 0);
    m_pixel[25].set((byte) 0,(byte) 255,(byte) 0);
    m_pixel[26].set((byte) 0,(byte) 255,(byte) 0);
    m_pixel[27].set((byte) 0,(byte) 255,(byte) 0);
  } else {
      m_pixel[1].set((byte) 0,(byte) 0,(byte) 0);
      m_pixel[2].set((byte) 0,(byte) 0,(byte) 0);
      m_pixel[3].set((byte) 0,(byte) 0,(byte) 0);
      m_pixel[4].set((byte) 0,(byte) 0,(byte) 0);
      m_pixel[5].set((byte) 0,(byte) 0,(byte) 0);
      m_pixel[6].set((byte) 0,(byte) 0,(byte) 0);
      m_pixel[7].set((byte) 0,(byte) 0,(byte) 0);
      m_pixel[8].set((byte) 0,(byte) 0,(byte) 0);
      m_pixel[9].set((byte) 0,(byte) 0,(byte) 0);
      m_pixel[10].set((byte) 0,(byte) 0,(byte) 0);
      m_pixel[11].set((byte) 0,(byte) 0,(byte) 0);
      m_pixel[12].set((byte) 0,(byte) 0,(byte) 0);
      m_pixel[13].set((byte) 0,(byte) 0,(byte) 0);
      m_pixel[14].set((byte) 0,(byte) 0,(byte) 0);
      m_pixel[15].set((byte) 0,(byte) 0,(byte) 0);
      m_pixel[16].set((byte) 0,(byte) 0,(byte) 0);
      m_pixel[17].set((byte) 0,(byte) 0,(byte) 0);
      m_pixel[18].set((byte) 0,(byte) 0,(byte) 0);
      m_pixel[19].set((byte) 0,(byte) 0,(byte) 0);
      m_pixel[20].set((byte) 0,(byte) 0,(byte) 0);
      m_pixel[21].set((byte) 0,(byte) 0,(byte) 0);
      m_pixel[22].set((byte) 0,(byte) 0,(byte) 0);
      m_pixel[23].set((byte) 0,(byte) 0,(byte) 0);
      m_pixel[24].set((byte) 0,(byte) 0,(byte) 0);
      m_pixel[25].set((byte) 0,(byte) 0,(byte) 0);
      m_pixel[26].set((byte) 0,(byte) 0,(byte) 0);
      m_pixel[27].set((byte) 0,(byte) 0,(byte) 0);
  }
}
public void grabberHand(){
  if(Robot.m_grabberHand.grabberHandLED == true){
    m_pixel[27].set((byte) 0,(byte) 255,(byte) 0);
    m_pixel[28].set((byte) 0,(byte) 255,(byte) 0);
    m_pixel[29].set((byte) 0,(byte) 255,(byte) 0);
    m_pixel[30].set((byte) 0,(byte) 255,(byte) 0);
    m_pixel[31].set((byte) 0,(byte) 255,(byte) 0);
    m_pixel[32].set((byte) 0,(byte) 255,(byte) 0);
    m_pixel[33].set((byte) 0,(byte) 255,(byte) 0);
    m_pixel[34].set((byte) 0,(byte) 255,(byte) 0);
    m_pixel[35].set((byte) 0,(byte) 255,(byte) 0);
    m_pixel[36].set((byte) 0,(byte) 255,(byte) 0);
    m_pixel[37].set((byte) 0,(byte) 255,(byte) 0);
    m_pixel[38].set((byte) 0,(byte) 255,(byte) 0);
    m_pixel[39].set((byte) 0,(byte) 255,(byte) 0);
    m_pixel[40].set((byte) 0,(byte) 255,(byte) 0);
    m_pixel[41].set((byte) 0,(byte) 255,(byte) 0);
    m_pixel[42].set((byte) 0,(byte) 255,(byte) 0);
    m_pixel[43].set((byte) 0,(byte) 255,(byte) 0);
    m_pixel[44].set((byte) 0,(byte) 255,(byte) 0);
    m_pixel[45].set((byte) 0,(byte) 255,(byte) 0);
    m_pixel[46].set((byte) 0,(byte) 255,(byte) 0);
    m_pixel[47].set((byte) 0,(byte) 255,(byte) 0);
    m_pixel[48].set((byte) 0,(byte) 255,(byte) 0);
    m_pixel[49].set((byte) 0,(byte) 255,(byte) 0);
    m_pixel[50].set((byte) 0,(byte) 255,(byte) 0);
    m_pixel[51].set((byte) 0,(byte) 255,(byte) 0);
    m_pixel[52].set((byte) 0,(byte) 255,(byte) 0);
    m_pixel[53].set((byte) 0,(byte) 255,(byte) 0);
  } else {
      m_pixel[27].set((byte) 0,(byte) 0,(byte) 0);
      m_pixel[28].set((byte) 0,(byte) 0,(byte) 0);
      m_pixel[29].set((byte) 0,(byte) 0,(byte) 0);
      m_pixel[30].set((byte) 0,(byte) 0,(byte) 0);
      m_pixel[31].set((byte) 0,(byte) 0,(byte) 0);
      m_pixel[32].set((byte) 0,(byte) 0,(byte) 0);
      m_pixel[33].set((byte) 0,(byte) 0,(byte) 0);
      m_pixel[34].set((byte) 0,(byte) 0,(byte) 0);
      m_pixel[35].set((byte) 0,(byte) 0,(byte) 0);
      m_pixel[36].set((byte) 0,(byte) 0,(byte) 0);
      m_pixel[37].set((byte) 0,(byte) 0,(byte) 0);
      m_pixel[38].set((byte) 0,(byte) 0,(byte) 0);
      m_pixel[39].set((byte) 0,(byte) 0,(byte) 0);
      m_pixel[40].set((byte) 0,(byte) 0,(byte) 0);
      m_pixel[41].set((byte) 0,(byte) 0,(byte) 0);
      m_pixel[42].set((byte) 0,(byte) 0,(byte) 0);
      m_pixel[43].set((byte) 0,(byte) 0,(byte) 0);
      m_pixel[44].set((byte) 0,(byte) 0,(byte) 0);
      m_pixel[45].set((byte) 0,(byte) 0,(byte) 0);
      m_pixel[46].set((byte) 0,(byte) 0,(byte) 0);
      m_pixel[47].set((byte) 0,(byte) 0,(byte) 0);
      m_pixel[48].set((byte) 0,(byte) 0,(byte) 0);
      m_pixel[49].set((byte) 0,(byte) 0,(byte) 0);
      m_pixel[50].set((byte) 0,(byte) 0,(byte) 0);
      m_pixel[51].set((byte) 0,(byte) 0,(byte) 0);
      m_pixel[52].set((byte) 0,(byte) 0,(byte) 0);
      m_pixel[53].set((byte) 0,(byte) 0,(byte) 0);
  }
}
public void blindCommand(){
  if(Robot.m_pressursensor.getAirPressurePsi() < 65) {
    m_pixel[1].set((byte)255, (byte)255, (byte)255);
    m_pixel[2].set((byte)255, (byte)255, (byte)255);    //BRG
    m_pixel[3].set((byte)255, (byte)255, (byte)255);
    m_pixel[4].set((byte)255, (byte)255, (byte)255);
    m_pixel[5].set((byte)255, (byte)255, (byte)255);
    m_pixel[6].set((byte)255 , (byte)255, (byte)255);
    m_pixel[7].set((byte)255, (byte)255, (byte)255);
    m_pixel[8].set((byte)255, (byte)255, (byte)255);
    m_pixel[9].set((byte)255, (byte)255, (byte)255);    //BRG
    m_pixel[10].set((byte)255, (byte)255, (byte)255);
    m_pixel[11].set((byte)255, (byte)255, (byte)255);
    m_pixel[12].set((byte)255, (byte)255, (byte)255);
    m_pixel[13].set((byte)255, (byte)255, (byte)255);
    m_pixel[14].set((byte)255, (byte)255, (byte)255);
    m_pixel[15].set((byte)255, (byte)255, (byte)255);
    m_pixel[16].set((byte)255, (byte)255, (byte)127);    //BRG
    m_pixel[17].set((byte)255, (byte)255, (byte)255);
    m_pixel[18].set((byte)255, (byte)255, (byte)255);
    m_pixel[19].set((byte)255, (byte)255, (byte)255);
    m_pixel[20].set((byte)255, (byte)255, (byte)255);
    m_pixel[21].set((byte)255, (byte)255, (byte)255);
    m_pixel[22].set((byte)255, (byte)255, (byte)255);
    m_pixel[23].set((byte)255, (byte)255, (byte)127);    //BRG
    m_pixel[24].set((byte)255, (byte)255, (byte)255);
    m_pixel[25].set((byte)255, (byte)255, (byte)255);
    m_pixel[26].set((byte)255, (byte)255, (byte)255);
    m_pixel[27].set((byte)255, (byte)255, (byte)255);
    m_pixel[28].set((byte)255, (byte)255, (byte)255);
    m_pixel[29].set((byte)255, (byte)255, (byte)255);
    m_pixel[30].set((byte)255, (byte)255, (byte)255);
    m_pixel[31].set((byte)255, (byte)255, (byte)255);    //BRG
    m_pixel[32].set((byte)255, (byte)255, (byte)255);
    m_pixel[33].set((byte)255, (byte)255, (byte)255);
    m_pixel[34].set((byte)255, (byte)255, (byte)255);
    m_pixel[35].set((byte)255 , (byte)255, (byte)255);
    m_pixel[36].set((byte)255, (byte)255, (byte)255);
    m_pixel[37].set((byte)255, (byte)255, (byte)255);
    m_pixel[38].set((byte)255, (byte)255, (byte)255);    //BRG
    m_pixel[39].set((byte)255, (byte)255, (byte)255);
    m_pixel[40].set((byte)255, (byte)255, (byte)255);
    m_pixel[41].set((byte)255, (byte)255, (byte)255);    //BRG
    m_pixel[42].set((byte)255, (byte)255, (byte)255);
    m_pixel[43].set((byte)255, (byte)255, (byte)255);
    m_pixel[44].set((byte)255, (byte)255, (byte)255);
    m_pixel[45].set((byte)255 , (byte)255, (byte)255);
    m_pixel[46].set((byte)255, (byte)255, (byte)255);
    m_pixel[47].set((byte)255, (byte)255, (byte)255);
    m_pixel[48].set((byte)255, (byte)255, (byte)255);    //BRG
    m_pixel[49].set((byte)255, (byte)255, (byte)255);
    m_pixel[50].set((byte)255, (byte)255, (byte)255);
    m_pixel[51].set((byte)255, (byte)255, (byte)255);    //BRG
    m_pixel[52].set((byte)255, (byte)255, (byte)255);
    m_pixel[53].set((byte)255, (byte)255, (byte)255);
    m_pixel[54].set((byte)255, (byte)255, (byte)255);
    m_pixel[55].set((byte)255 , (byte)255, (byte)255);
    m_pixel[56].set((byte)255, (byte)255, (byte)255);
    m_pixel[57].set((byte)255, (byte)255, (byte)255);
    m_pixel[58].set((byte)255, (byte)255, (byte)255);    //BRG
    m_pixel[59].set((byte)255, (byte)255, (byte)255);

  } else{
    m_pixel[0].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[1].set((byte) 0,(byte) 0,(byte) 0);     //brg
    m_pixel[2].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[3].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[4].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[5].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[6].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[7].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[8].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[9].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[10].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[11].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[12].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[13].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[14].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[15].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[16].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[17].set((byte) 0,(byte) 0,(byte) 0);     //brg
    m_pixel[18].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[19].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[20].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[21].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[22].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[23].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[24].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[25].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[26].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[27].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[28].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[29].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[30].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[31].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[32].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[33].set((byte) 0,(byte) 0,(byte) 0);     //brg
    m_pixel[34].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[35].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[36].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[37].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[38].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[39].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[40].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[41].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[42].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[43].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[44].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[45].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[46].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[47].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[48].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[49].set((byte) 0,(byte) 0,(byte) 0);     //brg
    m_pixel[50].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[51].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[52].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[53].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[54].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[55].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[56].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[57].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[58].set((byte) 0,(byte) 0,(byte) 0);
    m_pixel[59].set((byte) 0,(byte) 0,(byte) 0);
    }
}


}