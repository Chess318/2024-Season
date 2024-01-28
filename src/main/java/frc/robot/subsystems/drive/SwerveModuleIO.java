package frc.robot.subsystems.drive;

public interface SwerveModuleIO {
    public static class SwerveModuleIOInputs {
        public double drivePositionMeters = 0.0;
        public double driveVelocityMPS = 0.0;
        public double driveAppliedVolts = 0.0;
    
        public double turnPositionRad = 0.0;
        public double turnAppliedVolts = 0.0;
    }

    public void updateInputs(SwerveModuleIOInputs inputs);
    
    public void setDriveEncoderPosition(double position);
    public double getDriveEncoderPosition();

    public void setDesiredDriveSpeedMPS(double speed);
    public double getDriveEncoderSpeedMPS();
       
    public double getTurnEncoderPosition();
    public void setDesiredTurnAngle(double angle);
    public double getDriveVolts();
    public double getDriveOutput();
    public double getTurnVolts(); 
}