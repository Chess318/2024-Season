package frc.robot.subsystems.intake;

import com.revrobotics.CANSparkLowLevel;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;

import frc.robot.Constants;
import frc.robot.Constants.IntakeConstants;

public class RealIntake implements IntakeIO {

    public static CANSparkMax leftCenteringIntakeMotorController;
    public static CANSparkMax rightCenteringIntakeMotorController;
    public static CANSparkMax intakeMotorController;
    public static RelativeEncoder leftCenteringIntakeEncoder;
    public static RelativeEncoder rightCenteringIntakeEncoder;
    public static RelativeEncoder intakeEncoder;
    public static SparkPIDController intakePIDController;
    private double slewRate = 0.0;
    private double FEEDFORWARD = 0.01;
    private double PVALUE = 0.01;

    public RealIntake()
    {
        leftCenteringIntakeMotorController = new CANSparkMax(IntakeConstants.LEFT_CENTERING_MOTOR_ID,MotorType.kBrushless);
        leftCenteringIntakeMotorController.restoreFactoryDefaults();
        leftCenteringIntakeMotorController.setSmartCurrentLimit(Constants.NEO550_CURRENT_LIMIT);
        leftCenteringIntakeMotorController.setIdleMode(CANSparkMax.IdleMode.kBrake);
        // built in slew rate for spark max
        leftCenteringIntakeMotorController.setOpenLoopRampRate(slewRate);

        rightCenteringIntakeMotorController = new CANSparkMax(IntakeConstants.RIGHT_CENTERING_MOTOR_ID, MotorType.kBrushless);
        rightCenteringIntakeMotorController.restoreFactoryDefaults();
        rightCenteringIntakeMotorController.setSmartCurrentLimit(Constants.NEO550_CURRENT_LIMIT);
        rightCenteringIntakeMotorController.setIdleMode(CANSparkMax.IdleMode.kBrake);
        rightCenteringIntakeMotorController.setOpenLoopRampRate(slewRate);

        intakeMotorController = new CANSparkMax(IntakeConstants.INTAKE_CENTERING_ID, MotorType.kBrushless);
        intakeMotorController.restoreFactoryDefaults();
        intakeMotorController.setSmartCurrentLimit(Constants.NEO550_CURRENT_LIMIT);
        intakeMotorController.setIdleMode(CANSparkMax.IdleMode.kBrake);
        intakeMotorController.setOpenLoopRampRate(slewRate);

        // initialize motor encoder
        leftCenteringIntakeEncoder = leftCenteringIntakeMotorController.getEncoder();
        rightCenteringIntakeEncoder = rightCenteringIntakeMotorController.getEncoder();
        intakeEncoder = intakeMotorController.getEncoder();
        
        intakeEncoder.setVelocityConversionFactor(1/60.0); //convert to rps

        intakePIDController = intakeMotorController.getPIDController();
        intakePIDController.setFeedbackDevice(intakeEncoder);
        
        intakePIDController.setFF(FEEDFORWARD);
        intakePIDController.setP(PVALUE);

        // to reduce CANBus utilization
        leftCenteringIntakeMotorController.setPeriodicFramePeriod(CANSparkLowLevel.PeriodicFrame.kStatus2, 32767);
        leftCenteringIntakeMotorController.setPeriodicFramePeriod(CANSparkLowLevel.PeriodicFrame.kStatus3, 32767);
        leftCenteringIntakeMotorController.setPeriodicFramePeriod(CANSparkLowLevel.PeriodicFrame.kStatus4, 32767);
        leftCenteringIntakeMotorController.setPeriodicFramePeriod(CANSparkLowLevel.PeriodicFrame.kStatus5, 32767);
        leftCenteringIntakeMotorController.setPeriodicFramePeriod(CANSparkLowLevel.PeriodicFrame.kStatus6, 32767);
    }

    @Override
    public void setMotor(double percentOutput) {
        leftCenteringIntakeMotorController.set(percentOutput);
        rightCenteringIntakeMotorController.set(percentOutput);
        intakeMotorController.set(percentOutput);
    }

    public double getLeftCurrent()
    {
        return leftCenteringIntakeMotorController.getOutputCurrent();
    }

    public double getRightCurrent()
    {
        return rightCenteringIntakeMotorController.getOutputCurrent();
    }

    @Override
    public double getLeftVelocity() {
        return leftCenteringIntakeEncoder.getVelocity();
    }

    @Override
    public double getRightVelocity() {
        return rightCenteringIntakeEncoder.getVelocity();
    }

    public double getIntakeVelocity() {
        return intakeEncoder.getVelocity();
    }

    @Override
    public double getLeftEncoderPosition() {
        return leftCenteringIntakeEncoder.getPosition();
    }

    @Override
    public double getRightEncoderPosition() {
        return rightCenteringIntakeEncoder.getPosition();
    }

    @Override
    public void setLeftCurrentLimit(int current) {
        leftCenteringIntakeMotorController.setSmartCurrentLimit(current);        
    }

    @Override
    public void setRightCurrentLimit(int current) {
        rightCenteringIntakeMotorController.setSmartCurrentLimit(current);        
    }

    @Override
    public void periodicUpdate() {
    }
}
