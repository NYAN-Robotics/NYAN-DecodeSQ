package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.utilities.math.linearalgebra.Pose;
import org.firstinspires.ftc.teamcode.utilities.robot.Alliance;
import org.firstinspires.ftc.teamcode.utilities.robot.RobotEx;
import org.firstinspires.ftc.teamcode.utilities.robot.command.framework.commandtypes.DelayCommand;
import org.firstinspires.ftc.teamcode.utilities.robot.command.framework.commandtypes.OneTimeCommand;
import org.firstinspires.ftc.teamcode.utilities.robot.command.framework.commandtypes.ParallelCommandGroup;
import org.firstinspires.ftc.teamcode.utilities.robot.command.framework.commandtypes.SequentialCommandGroup;
import org.firstinspires.ftc.teamcode.utilities.robot.command.framework.commandtypes.YieldCommand;
import org.firstinspires.ftc.teamcode.utilities.robot.command.movement.MovementCommand;
import org.firstinspires.ftc.teamcode.utilities.robot.movement.MovementConstants;

/**
 * Example teleop code for a basic mecanum drive
 */
@Autonomous(name = "High Red Test Auto")
public class HighRedTest extends LinearOpMode {

    // Create new Instance of the robot
    RobotEx robot = RobotEx.getInstance();



    @Override
    public void runOpMode() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        telemetry.setMsTransmissionInterval(500);

        // Initialize the robot
        robot.init(this, telemetry);
        robot.theLocalizer.setPose(new Pose(0, 0, Math.PI / 2));

        Servo leftTurretServo = hardwareMap.get(Servo .class, "leftTurretServo");
        Servo rightTurretServo = hardwareMap.get(Servo.class, "rightTurretServo");
        Servo rightPivotServo = hardwareMap.get(Servo.class, "rightPivotServo");
        Servo leftPivotServo = hardwareMap.get(Servo.class, "leftPivotServo");

        leftTurretServo.setDirection(Servo.Direction.REVERSE);
        rightTurretServo.setDirection(Servo.Direction.REVERSE);
        rightPivotServo.setDirection(Servo.Direction.FORWARD);
        leftPivotServo.setDirection(Servo.Direction.REVERSE);

        rightPivotServo.setPosition(0.9);
        leftPivotServo.setPosition(0.9);

        leftTurretServo.setPosition(0.55);
        rightTurretServo.setPosition(0.55);
        double outtakePower = 0.85 * robot.getPowerMultiple();

        Alliance currentAlliance = Alliance.RED;

        SequentialCommandGroup commands = new SequentialCommandGroup(
                new ParallelCommandGroup(
                        new MovementCommand(
                                new Pose(0, 0, 0),
                                new Pose(12, 0, 0),
                                new MovementConstants(0)
                        ),
                        new OneTimeCommand(() -> robot.theOuttake.outtake(outtakePower))
                ),
                new YieldCommand(100),
                new ParallelCommandGroup(
                        new SequentialCommandGroup(
                                new OneTimeCommand(() -> robot.theOuttake.transfer(outtakePower, currentAlliance)),
                                new YieldCommand(500),
                                new ParallelCommandGroup(
                                        new OneTimeCommand(() -> leftTurretServo.setPosition(0.55)),
                                        new OneTimeCommand(() -> rightTurretServo.setPosition(0.55))
                                ),
                                new OneTimeCommand(() -> robot.theOuttake.transfer(outtakePower, currentAlliance)),
                                new YieldCommand(500),
                                new ParallelCommandGroup(
                                        new OneTimeCommand(() -> robot.theIntake.intakeReverse()),
                                        new YieldCommand(250)
                                ),
                                new ParallelCommandGroup(
                                        new OneTimeCommand(() -> robot.theIntake.intakeForward()),
                                        new YieldCommand(250)
                                ),
                                new ParallelCommandGroup(
                                        new OneTimeCommand(() -> leftTurretServo.setPosition(0.55)),
                                        new OneTimeCommand(() -> rightTurretServo.setPosition(0.55))
                                ),
                                new OneTimeCommand(() -> robot.theOuttake.transfer(outtakePower, currentAlliance)),
                                new ParallelCommandGroup(
                                        new OneTimeCommand(() -> robot.theIntake.intakeReverse()),
                                        new YieldCommand(250)
                                ),
                                new ParallelCommandGroup(
                                        new OneTimeCommand(() -> robot.theIntake.intakeForward()),
                                        new YieldCommand(250)
                                ),
                                new YieldCommand(100),
                                new ParallelCommandGroup(
                                        new OneTimeCommand(() -> leftTurretServo.setPosition(0.55)),
                                        new OneTimeCommand(() -> rightTurretServo.setPosition(0.55))
                                )
                        ),
                        new YieldCommand(1000)
                ),
                new OneTimeCommand(() -> robot.theOuttake.stopOuttake()),
                new MovementCommand(
                        new Pose(12, 0, 0),
                        new Pose(31, -25, Math.PI / 2),
                        new MovementConstants(0)
                ),
                new YieldCommand(500),
                new MovementCommand(
                        new Pose(31, -25, Math.PI / 2),
                        new Pose(31, -60, Math.PI / 2),
                        new MovementConstants(20, 20, 0, 0.013, 0.0065)
                ),
                new YieldCommand(500),
                new ParallelCommandGroup(
                        new MovementCommand(
                                new Pose(31, -60, Math.PI / 2),
                                new Pose(8, -15, 0),
                                new MovementConstants(0)
                        ),
                        new OneTimeCommand(() -> robot.theIntake.stopIntake())
                ),
                new OneTimeCommand(() -> robot.theOuttake.outtake(outtakePower)),
                new YieldCommand(1000),
                new ParallelCommandGroup(
                        new OneTimeCommand(() -> leftTurretServo.setPosition(0.55)),
                        new OneTimeCommand(() -> rightTurretServo.setPosition(0.55))
                ),
                new ParallelCommandGroup(
                        new SequentialCommandGroup(
                                new OneTimeCommand(() -> robot.theOuttake.transfer(outtakePower, currentAlliance)),
                                new YieldCommand(500),
                                new ParallelCommandGroup(
                                        new OneTimeCommand(() -> leftTurretServo.setPosition(0.55)),
                                        new OneTimeCommand(() -> rightTurretServo.setPosition(0.55))
                                ),
                                new OneTimeCommand(() -> robot.theOuttake.transfer(outtakePower, currentAlliance)),
                                new YieldCommand(500),
                                new ParallelCommandGroup(
                                        new OneTimeCommand(() -> robot.theIntake.intakeReverse()),
                                        new YieldCommand(250)
                                ),
                                new ParallelCommandGroup(
                                        new OneTimeCommand(() -> robot.theIntake.intakeForward()),
                                        new YieldCommand(250)
                                ),
                                new ParallelCommandGroup(
                                        new OneTimeCommand(() -> leftTurretServo.setPosition(0.55)),
                                        new OneTimeCommand(() -> rightTurretServo.setPosition(0.55))
                                ),
                                new OneTimeCommand(() -> robot.theOuttake.transfer(outtakePower, currentAlliance)),
                                new ParallelCommandGroup(
                                        new OneTimeCommand(() -> robot.theIntake.intakeReverse()),
                                        new YieldCommand(250)
                                ),
                                new ParallelCommandGroup(
                                        new OneTimeCommand(() -> robot.theIntake.intakeForward()),
                                        new YieldCommand(250)
                                ),
                                new YieldCommand(100),
                                new ParallelCommandGroup(
                                        new OneTimeCommand(() -> leftTurretServo.setPosition(0.55)),
                                        new OneTimeCommand(() -> rightTurretServo.setPosition(0.55))
                                )
                        ),
                        new YieldCommand(1000)
                ),
                new MovementCommand(
                        new Pose(8, -15, 0),
                        new Pose(20, -20, 0),
                        new MovementConstants()
                )

                //new ParallelCommandGroup(
//                new MovementCommand(
//                        new Pose(0, 0, 0),
//                        new Pose(40, 0, 0),
//                        new MovementConstants(0)
//                ),
//                new MovementCommand(
//                        new Pose(40, 0, 0),
//                        new Pose(40, 40, 0),
//                        new MovementConstants(0)
//                ),
//                new MovementCommand(
//                        new Pose(40, 40, 0),
//                        new Pose(40, 20, Math.PI / 2),
//                        new MovementConstants(0)
//                ),
//                new MovementCommand(
//                        new Pose(40, 20, Math.PI / 2),
//                        new Pose(0, 0, 0),
//                        new MovementConstants(0)
//                )
//                        new SequentialCommandGroup(
//                                new YieldCommand(500),
//                                new OneTimeCommand(() -> robot.theOuttake.outtake()),
//                                new YieldCommand(500),
//                                new OneTimeCommand(() -> robot.theOuttake.transfer()),
//                                new YieldCommand(500),
//                                new OneTimeCommand(() -> robot.theOuttake.stopOuttake())
//                        )
                //),
                //new ParallelCommandGroup(
//                        new MovementCommand(
//                                new Pose(0, 40, Math.PI / 2),
//                                new Pose(0, 40, Math.PI),
//                                new MovementConstants(10)
//                        ),
//                        new SequentialCommandGroup(
//                                new YieldCommand(500),
//                                new OneTimeCommand(() -> robot.theIntake.intakeForward()),
//                                new YieldCommand(500),
//                                new OneTimeCommand(()-> robot.theIntake.intakeReverse()),
//                                new YieldCommand(500),
//                                new OneTimeCommand(() -> robot.theIntake.stopIntake())
//                        )
//
                //)
        );

        waitForStart();

        // Notify subsystems before loop
        robot.postStart();

        if (isStopRequested()) return;

        robot.pause(0.5);
        /*
        MovementCommandCache initialCommand = new MovementCommandCache(
                new Pose(0, 0, Math.PI / 2),
                new Pose(0, 40, Math.PI),
                new MovementConstants()
        );

        MovementCommandCache returnCommand = new MovementCommandCache(
                new Pose(0, 40, Math.PI),
                new Pose(0, 0, Math.PI / 2),
                new MovementConstants()
        );

        drive.gotoPoint(initialCommand);
        drive.gotoPoint(returnCommand);

         */



        robot.theCommandScheduler.scheduleCommand(commands);

        while (!isStopRequested()) {
            robot.update();
        }

    }
}
