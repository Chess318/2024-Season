// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.util.Optional;

import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the
 * name of this class or
 * the package after creating this project, you must also update the
 * build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private Command m_autonomousCommand;

  private RobotContainer m_robotContainer;

  public enum RobotType {
    SIMULATION,
    KORG,
    COMP_BOT
  }

  public static RobotType robotType;

  /**
   * This function is run when the robot is first started up and should be used
   * for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    DriverStation.silenceJoystickConnectionWarning(true);
    // Instantiate our RobotContainer. This will perform all our button bindings,
    // and put our
    // autonomous chooser on the dashboard.

    DataLogManager.start();
    DriverStation.startDataLog(DataLogManager.getLog());
    SmartDashboard.putString("branch and date", MyVersion.GIT_BRANCH + " " + MyVersion.GIT_DATE);
    Shuffleboard.getTab("Driver").add("robot/branch info",
        MyVersion.GIT_BRANCH + " " + MyVersion.BUILD_DATE + " " + MyVersion.GIT_SHA);

    if (RobotBase.isSimulation()) {
      robotType = RobotType.SIMULATION;
    } else {
      robotType = RobotType.KORG;
    }

    m_robotContainer = new RobotContainer();

    CommandScheduler.getInstance().onCommandInitialize(cmd -> DataLogManager.log(cmd.getName() + ": Init"));
    CommandScheduler.getInstance().onCommandInterrupt(cmd -> DataLogManager.log(cmd.getName() + ": Interrupted"));
    CommandScheduler.getInstance().onCommandFinish(cmd -> DataLogManager.log(cmd.getName() + ": End"));

  }

  /**
   * This function is called every 20 ms, no matter the mode. Use this for items
   * like diagnostics
   * that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>
   * This runs after the mode specific periodic functions, but before LiveWindow
   * and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    // Runs the Scheduler. This is responsible for polling buttons, adding
    // newly-scheduled
    // commands, running already-scheduled commands, removing finished or
    // interrupted commands,
    // and running subsystem periodic() methods. This must be called from the
    // robot's periodic
    // block in order for anything in the Command-based framework to work.
    CommandScheduler.getInstance().run();
  }

  /** This function is called once each time the robot enters Disabled mode. */
  @Override
  public void disabledInit() {

    // Disables arm and elevator PID loops so it won't remember/try to get to the
    // last setpoint
    // Otherwise, if the arm fell after disabling, it would go up really quickly on
    // enabling
    // Also disables gravity compensation b/c no command with gravity compensation
    // running after disable
    RobotContainer.arm.disable();
  }

  @Override
  public void disabledPeriodic() {
  }

  /**
   * This autonomous runs the autonomous command selected by your
   * {@link RobotContainer} class.
   */
  @Override
  public void autonomousInit() {
    // Optional<Alliance> ally = DriverStation.getAlliance();
    // m_robotContainer.m_vision.assignAprilTags(ally);
    RobotContainer.arm.setEncoderPosition(RobotContainer.arm.getAbsoluteEncoderPosition());

    m_autonomousCommand = m_robotContainer.getAutonomousCommand();
    RobotContainer.makeSetPositionCommand(RobotContainer.arm, 0.335);

    // schedule the autonomous command
    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }

  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopInit() {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    Optional<Alliance> ally = DriverStation.getAlliance();
    m_robotContainer.led.turnTeleop();
    m_robotContainer.vision.assignAprilTags(ally);
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
  }

  @Override
  public void testInit() {
    // Cancels all running commands at the start of test mode.
    CommandScheduler.getInstance().cancelAll();
  }

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {
  }
}
