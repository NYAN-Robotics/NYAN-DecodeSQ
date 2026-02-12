package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.utilities.math.linearalgebra.Pose;
import org.firstinspires.ftc.teamcode.utilities.robot.RobotEx;
import org.firstinspires.ftc.teamcode.utilities.robot.command.framework.commandtypes.SequentialCommandGroup;
import org.firstinspires.ftc.teamcode.utilities.robot.command.movement.MovementCommand;
import org.firstinspires.ftc.teamcode.utilities.robot.movement.MovementConstants;

/**
 * Example teleop code for a basic mecanum drive
 */
@Autonomous(name = "Strafe Auto")
public class StrafeAuto extends LinearOpMode {

    // Create new Instance of the robot
    RobotEx robot = RobotEx.getInstance();

    @Override
    public void runOpMode() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        telemetry.setMsTransmissionInterval(500);

        // Initialize the robot
        robot.init(this, telemetry);
        robot.theLocalizer.setPose(new Pose(0, 0, Math.PI / 2));

        SequentialCommandGroup commands = new SequentialCommandGroup(
                //new ParallelCommandGroup(
                new MovementCommand(
                        new Pose(0, 0, Math.PI / 2),
                        new Pose(40, 0, Math.PI / 2),
                        new MovementConstants(10)
                )//,
//                new MovementCommand(
//                        new Pose(0, 40, Math.PI / 2),
//                        new Pose(40, 40, Math.PI / 2),
//                        new MovementConstants(3)
//                ),
//                new MovementCommand(
//                        new Pose(40, 40, Math.PI / 2),
//                        new Pose(20, 40, 0),
//                        new MovementConstants(10)
//                ),
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
