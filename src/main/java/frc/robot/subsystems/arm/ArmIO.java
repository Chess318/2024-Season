package frc.robot.subsystems.arm;

public interface ArmIO {
    public double getAbsoluteEncoderPosition();
    public double getEncoderPosition();
    public double getEncoderSpeed();
    public void setSpeed(double speed);
    public void setPosition(double position);
    public void periodicUpdate();
    public double getArmCurrent();
}



