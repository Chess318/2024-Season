// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.XboxController.Axis;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants.AutoConstants;
import frc.robot.Constants.DriveConstants;
import frc.robot.Constants.OIConstants;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SwerveControllerCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;

import java.util.List;
import frc.robot.subsystems.*;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.intake.IntakeIO;
import frc.robot.subsystems.intake.RealIntake;
import frc.robot.subsystems.shooter.RealShooter;
import frc.robot.subsystems.shooter.Shooter;
import frc.robot.subsystems.shooter.ShooterIO;

/*
 * This class is where the bulk of the robot should be declared.  Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls).  Instead, the structure of the robot
 * (including subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems
//   private final DriveSubsystem m_robotDrive = new DriveSubsystem();
//   private static Gyro m_gyro = new Gyro(); 
  public boolean fieldOrientedDrive = false;

  public static Shooter m_shooter;

  public static Intake m_intake;
 

  // The driver's controller
  XboxController m_driverController = new XboxController(OIConstants.kDriverControllerPort);

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    // Configure the button bindings
    SmartDashboard.putNumber("Shoot speed", SmartDashboard.getNumber("Shoot speed", 0));
    setUpShooter();
    setUpIntake();
    configureButtonBindings();

    m_shooter.setDefaultCommand(
        new InstantCommand(
            () -> m_shooter.setSpeed(SmartDashboard.getNumber("Shoot speed", 0)),
            m_shooter));

    m_intake.setDefaultCommand(
        new InstantCommand(
            () -> m_intake.setMotor(0),
            m_intake));

    // Configure default commands
    // m_robotDrive.setDefaultCommand(
    //     // The left stick controls translation of the robot.
    //     // Turning is controlled by the X axis of the right stick.
    //     new RunCommand(
    //         () -> m_robotDrive.drive(
    //             -MathUtil.applyDeadband(m_driverController.getLeftY(), OIConstants.kDriveDeadband),
    //             -MathUtil.applyDeadband(m_driverController.getLeftX(), OIConstants.kDriveDeadband),
    //             -MathUtil.applyDeadband(m_driverController.getRightX(), OIConstants.kDriveDeadband),
    //             fieldOrientedDrive, false),
    //         m_robotDrive));
        // new RunCommand(
        //     () -> m_robotDrive.drive(
        //         -MathUtil.applyDeadband(m_driverController.getLeftY(), OIConstants.kDriveDeadband),
        //         0,
        //         0,
        //         fieldOrientedDrive, false),
        //     m_robotDrive));
  }



  /**
   * Use this method to define your button->command mappings. Buttons can be
   * created by
   * instantiating a {@link edu.wpi.first.wpilibj.GenericHID} or one of its
   * subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then calling
   * passing it to a
   * {@link JoystickButton}.
   */
  private void configureButtonBindings() {
    //right bumper?
    // new JoystickButton(m_driverController, XboxController.Button.kX.value)
    //     .whileTrue(new RunCommand(
    //         () -> m_robotDrive.setX(),
    //         m_robotDrive));
    
    // new JoystickButton(m_driverController, XboxController.Button.kY.value)
    //     .whileTrue(new RunCommand(
    //         () -> m_robotDrive.setZero(),
    //         m_robotDrive));

    new JoystickButton(m_driverController, XboxController.Button.kA.value)
        .onTrue(new InstantCommand(
        () -> fieldOrientedDrive = !fieldOrientedDrive));

    // new JoystickButton(m_driverController, XboxController.Button.kB.value)
    //     .onTrue(new InstantCommand(
    //         () -> m_gyro.resetYaw(), m_gyro));  
    
    new JoystickButton(m_driverController, XboxController.Button.kLeftBumper.value)
        .whileTrue(new InstantCommand(
        () -> m_shooter.setMotor(0)));
    
    new Trigger(() -> m_driverController.getRawAxis(Axis.kRightTrigger.value) > 0.1)
        .whileTrue(new InstantCommand(
        () -> m_intake.setMotor(1)));

    new Trigger(() -> m_driverController.getRawAxis(Axis.kRightTrigger.value) > 0.1)
        .whileTrue(new InstantCommand(
        () -> m_intake.setMotor(-1)));
    }

  private void setUpShooter () {
  
    ShooterIO shooterIO;
       
        shooterIO = new RealShooter();

    m_shooter = new Shooter(shooterIO);
  }

  private void setUpIntake () {

    IntakeIO intakeIO;
       
        intakeIO = new RealIntake();

    m_intake = new Intake(intakeIO);
  }
  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
//   public Command getAutonomousCommand() {
//     // Create config for trajectory
//     TrajectoryConfig config = new TrajectoryConfig(
//         AutoConstants.kMaxSpeedMetersPerSecond,
//         AutoConstants.kMaxAccelerationMetersPerSecondSquared)
//         // Add kinematics to ensure max speed is actually obeyed
//         .setKinematics(DriveConstants.kDriveKinematics);

//     // An example trajectory to follow. All units in meters.
//     Trajectory exampleTrajectory = TrajectoryGenerator.generateTrajectory(
//         // Start at the origin facing the +X direction
//         new Pose2d(0, 0, new Rotation2d(0)),
//         // Pass through these two interior waypoints, making an 's' curve path
//         List.of(new Translation2d(1, 1), new Translation2d(2, -1)),
//         // End 3 meters straight ahead of where we started, facing forward
//         new Pose2d(3, 0, new Rotation2d(0)),
//         config);

//     var thetaController = new ProfiledPIDController(
//         AutoConstants.kPThetaController, 0, 0, AutoConstants.kThetaControllerConstraints);
//     thetaController.enableContinuousInput(-Math.PI, Math.PI);

//     SwerveControllerCommand swerveControllerCommand = new SwerveControllerCommand(
//         exampleTrajectory,
//         m_robotDrive::getPose, // Functional interface to feed supplier
//         DriveConstants.kDriveKinematics,

//         // Position controllers
//         new PIDController(AutoConstants.kPXController, 0, 0),
//         new PIDController(AutoConstants.kPYController, 0, 0),
//         thetaController,
//         m_robotDrive::setModuleStates,
//         m_robotDrive);

//     // Reset odometry to the starting pose of the trajectory.
//     m_robotDrive.resetOdometry(exampleTrajectory.getInitialPose());

//     // Run path following command, then stop at the end.
//     return swerveControllerCommand.andThen(() -> m_robotDrive.drive(0, 0, 0, false, false));
//   }
}
