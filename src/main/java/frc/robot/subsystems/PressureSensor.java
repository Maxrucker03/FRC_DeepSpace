package frc.robot.subsystems;

import edu.wpi.first.wpilibj.AnalogInput;

public class PressureSensor {
    private final AnalogInput mAnalogInput;

    public PressureSensor(int analogInputNumber) {
        mAnalogInput = new AnalogInput(analogInputNumber);
    }

    public double getAirPressurePsi() {
        // taken from the datasheet
        return 250.0 * mAnalogInput.getVoltage() / 5.0 - 24.5; //was - 25
    }
    

}