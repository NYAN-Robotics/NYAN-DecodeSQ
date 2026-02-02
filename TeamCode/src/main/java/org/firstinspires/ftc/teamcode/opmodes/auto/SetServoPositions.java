package org.firstinspires.ftc.teamcode.opmodes.auto;
//package org.firstinspires.ftc.teamcode.opmodes.auto;
//
//import com.acmerobotics.dashboard.FtcDashboard;
//import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//import com.qualcomm.robotcore.eventloop.opmode.Disabled;
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.hardware.Gamepad;
//
//import org.firstinspires.ftc.teamcode.utilities.math.linearalgebra.Pose;
//import org.firstinspires.ftc.teamcode.utilities.robot.RobotEx;
//import org.firstinspires.ftc.teamcode.utilities.robot.command.framework.commandtypes.OneTimeCommand;
//import org.firstinspires.ftc.teamcode.utilities.robot.command.framework.commandtypes.ParallelCommandGroup;
//import org.firstinspires.ftc.teamcode.utilities.robot.command.framework.commandtypes.SequentialCommandGroup;
//import org.firstinspires.ftc.teamcode.utilities.robot.command.framework.commandtypes.YieldCommand;
//import org.firstinspires.ftc.teamcode.utilities.robot.command.movement.MovementCommand;
//import org.firstinspires.ftc.teamcode.utilities.robot.movement.MovementConstants;
//import org.firstinspires.ftc.teamcode.utilities.robot.movement.PIDDrive;
//import org.firstinspires.ftc.teamcode.utilities.robot.subsystems.Outtake;
//
///**
// * Example teleop code for a basic mecanum drive
// */
//@Autonomous(name = "Leave Auto")
//public class LeaveAuto extends LinearOpMode {
//
//    // Create new Instance of the robot
//    RobotEx robot = RobotEx.getInstance();
//
//    @Override
//    public void runOpMode() {
//        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
//        telemetry.setMsTransmissionInterval(500);
//
//        // Initialize the robot
//        robot.init(this, telemetry);
//
//        SequentialCommandGroup commands = new SequentialCommandGroup(
//            new MovementCommand(
//                    new Pose(0, 0, Math.PI / 2),
//                    new Pose(0, 10, Math.PI / 2),
//                    new MovementConstants()
//            )
//        );
//
//        waitForStart();
//
//        // Notify subsystems before loop
//        robot.postStart();
//
//        if (isStopRequested()) return;
//
//        // Initialize variables for loop
//        Gamepad currentFrameGamepad1 = new Gamepad();
//        Gamepad currentFrameGamepad2 = new Gamepad();
//
//        Gamepad previousFrameGamepad1 = new Gamepad();
//        Gamepad previousFrameGamepad2 = new Gamepad();
//
//        PIDDrive drive = new PIDDrive(robot, this, telemetry);
//
//        robot.theLocalizer.setPose(new Pose(0, 0, Math.PI / 2));
//
//        robot.pause(0.5);
//        /*
//        MovementCommandCache initialCommand = new MovementCommandCache(
//                new Pose(0, 0, Math.PI / 2),
//                new Pose(0, 40, Math.PI),
//                new MovementConstants()
//        );
//
//        MovementCommandCache returnCommand = new MovementCommandCache(
//                new Pose(0, 40, Math.PI),
//                new Pose(0, 0, Math.PI / 2),
//                new MovementConstants()
//        );
//
//        drive.gotoPoint(initialCommand);
//        drive.gotoPoint(returnCommand);
//
//         */
//
//
//
//        robot.theCommandScheduler.scheduleCommand(commands);
//
//        while (!isStopRequested()) {
//            robot.update();
//        }
//
//    }
//}

//package org.firstinspires.ftc.teamcode.pedroPathing;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.List;

// I hope this works
@Autonomous(name="Set Servo Positions")
public class SetServoPositions extends LinearOpMode {
    private final ElapsedTime runtime = new ElapsedTime();
    private ElapsedTime transferTime = new ElapsedTime();
    private ElapsedTime turretModeTime = new ElapsedTime();
    private Limelight3A limelight = null;
    private DcMotorEx leftFrontMotor = null;
    private DcMotorEx leftBackMotor = null;
    private DcMotorEx rightFrontMotor = null;
    private DcMotorEx rightBackMotor = null;
    private DcMotorEx rightIntakeMotor = null;
    private DcMotorEx leftIntakeMotor = null;
    private DcMotorEx rightOuttakeMotor = null;
    private DcMotorEx leftOuttakeMotor = null;
    private Servo leftTurretServo = null;
    private Servo rightTurretServo = null;
    private Servo rightPivotServo = null;
    private Servo leftPivotServo = null;
    private Servo rightTransferServo = null;
    private Servo leftTransferServo = null;
    private Servo rightLiftServo = null;
    private Servo leftLiftServo = null;

    @Override
    public void runOpMode() {
        limelight = hardwareMap.get(Limelight3A.class, "limelight");

        limelight.pipelineSwitch(0);

        leftFrontMotor = hardwareMap.get(DcMotorEx.class, "leftFrontMotor");
        leftBackMotor = hardwareMap.get(DcMotorEx.class, "leftBackMotor");
        rightFrontMotor = hardwareMap.get(DcMotorEx.class, "rightFrontMotor");
        rightBackMotor = hardwareMap.get(DcMotorEx.class, "rightBackMotor");

        leftFrontMotor.setDirection(DcMotorEx.Direction.REVERSE);
        leftBackMotor.setDirection(DcMotorEx.Direction.FORWARD);
        rightFrontMotor.setDirection(DcMotorEx.Direction.FORWARD);
        rightBackMotor.setDirection(DcMotorEx.Direction.REVERSE);

        leftTurretServo = hardwareMap.get(Servo.class, "leftTurretServo");
        rightTurretServo = hardwareMap.get(Servo.class, "rightTurretServo");
        rightPivotServo = hardwareMap.get(Servo.class, "rightPivotServo");
        leftPivotServo = hardwareMap.get(Servo.class, "leftPivotServo");
        rightTransferServo = hardwareMap.get(Servo.class, "rightTransferServo");
        leftTransferServo = hardwareMap.get(Servo.class, "leftTransferServo");
        rightLiftServo = hardwareMap.get(Servo.class, "rightLiftServo");
        leftLiftServo = hardwareMap.get(Servo.class, "leftLiftServo");

        leftTurretServo.setDirection(Servo.Direction.REVERSE);
        rightTurretServo.setDirection(Servo.Direction.REVERSE);
        rightPivotServo.setDirection(Servo.Direction.FORWARD);
        leftPivotServo.setDirection(Servo.Direction.REVERSE);
        rightTransferServo.setDirection(Servo.Direction.FORWARD);
        leftTransferServo.setDirection(Servo.Direction.REVERSE);
        rightLiftServo.setDirection(Servo.Direction.FORWARD);
        leftLiftServo.setDirection(Servo.Direction.REVERSE);

        rightIntakeMotor = hardwareMap.get(DcMotorEx.class, "rightIntakeMotor");
        leftIntakeMotor = hardwareMap.get(DcMotorEx.class, "leftIntakeMotor");
        rightOuttakeMotor = hardwareMap.get(DcMotorEx.class, "rightOuttakeMotor");
        leftOuttakeMotor = hardwareMap.get(DcMotorEx.class, "leftOuttakeMotor");

        rightIntakeMotor.setDirection(DcMotorEx.Direction.REVERSE);
        leftIntakeMotor.setDirection(DcMotorEx.Direction.FORWARD);
        rightOuttakeMotor.setDirection(DcMotorEx.Direction.FORWARD);
        leftOuttakeMotor.setDirection(DcMotorEx.Direction.REVERSE);

        telemetry.addData("Status", "Initialized");
        telemetry.update();
//        boolean highTurret = false;
//
//        Telemetry.Line controlLine = telemetry.addLine("Controls:\no - blue\n[] - red");
//        Enums.Alliance currentAlliance = Enums.Alliance.BLUE;
//
//        while (opModeInInit()) {
//            telemetry.addData("Alliance", currentAlliance.name());
//            telemetry.update();
//
//            if (gamepad1.circle) {
//                currentAlliance = Enums.Alliance.BLUE;
//            }
//            if (gamepad1.square) {
//                currentAlliance = Enums.Alliance.RED;
//            }
////        }
//
//        telemetry.removeLine(controlLine);

        waitForStart();
        runtime.reset();
//        limelight.start();

        while (opModeIsActive()) {

            //pivot 0.5, transfer 0.5, pto 0.5 turret 0,

            rightPivotServo.setPosition(0.5);
            leftPivotServo.setPosition(0.5);
            rightTransferServo.setPosition(0.5);
            leftTransferServo.setPosition(0.5);
            rightTurretServo.setPosition(0);
            leftTurretServo.setPosition(0);
            rightLiftServo.setPosition(0.5);
            leftLiftServo.setPosition(0.5);
//            int id = 0;
//            double tx = 0;
//            double ty = 0;
//
//            LLResult result = limelight.getLatestResult();
//            if (result != null && result.isValid()) {
//                List<LLResultTypes.FiducialResult> fiducials = result.getFiducialResults();
//                for (LLResultTypes.FiducialResult fiducial : fiducials) {
//                    id = fiducial.getFiducialId();
//                }
//                if ((id == 20 && currentAlliance == Enums.Alliance.BLUE) || (id == 24 && currentAlliance == Enums.Alliance.RED)) {
//                    tx = result.getTx();
//                    ty = result.getTy();
//                    telemetry.addData("tx", tx);
//                    telemetry.addData("ty", ty);
//                }
//            } else {
//                telemetry.addData("The thingy is broken", "because of the limelight");
//            }
//            double ltpos = leftTurretServo.getPosition();
//            double rtpos = rightTurretServo.getPosition();
//            telemetry.addData("ltpos", ltpos);
//            telemetry.addData("rtpos", rtpos);
//            telemetry.update();
//            if (tx <= -2) {
//                leftTurretServo.setPosition(ltpos + 0.001);
//                rightTurretServo.setPosition(rtpos + 0.001);
//            } else if (tx >= 2) {
//                leftTurretServo.setPosition(ltpos - 0.001);
//                rightTurretServo.setPosition(rtpos - 0.001);
//            }
//
//            rightPivotServo.setPosition(0.5);
//            leftPivotServo.setPosition(0.5);

//            if (runtime.milliseconds() < 1000) {
//                leftFrontMotor.setPower(0);
//                leftBackMotor.setPower(0);
//                rightFrontMotor.setPower(0);
//                rightBackMotor.setPower(0);
//            } else if (runtime.milliseconds() < 2000) {
//                leftFrontMotor.setPower(0.5);
//                leftBackMotor.setPower(0.5);
//                rightFrontMotor.setPower(0.5);
//                rightBackMotor.setPower(0.5);
//            } else {
//                leftFrontMotor.setPower(0);
//                leftBackMotor.setPower(0);
//                rightFrontMotor.setPower(0);
//                rightBackMotor.setPower(0);
//            }

//            else if (runtime.milliseconds() < 4000) {
//                leftFrontMotor.setPower(0);
//                leftBackMotor.setPower(0);
//                rightFrontMotor.setPower(0);
//                rightBackMotor.setPower(0);
//            } else if (runtime.milliseconds() < 10000) {
//                for (int i = 0; i < 3; i++) {
//                    leftIntakeMotor.setPower(0.0);
//                    rightIntakeMotor.setPower(0.0);
//                    transferTime.reset();
//                    while (transferTime.milliseconds() < 200) {
//                        leftTransferServo.setPosition(0.10);
//                        rightTransferServo.setPosition(0.10);
//                    }
//                    while (transferTime.milliseconds() < 390) {
//                        leftTransferServo.setPosition(0.52);
//                        rightTransferServo.setPosition(0.52);
//                    }
//                    while (transferTime.milliseconds() < 600) {
//                        rightIntakeMotor.setPower(0.0);
//                        leftIntakeMotor.setPower(0.0);
//                    }
//                    while (transferTime.milliseconds() < 1000) {
//                        rightIntakeMotor.setPower(1.0);
//                        leftIntakeMotor.setPower(1.0);
//                    }
//                    while (transferTime.milliseconds() < 2000) {
//                        leftIntakeMotor.setPower(0.0);
//                        rightIntakeMotor.setPower(0.0);
//                    }
//                }
//            } else if (runtime.milliseconds() < 11000) {
//                leftFrontMotor.setPower(-1.0);
//                leftBackMotor.setPower(1.0);
//                rightFrontMotor.setPower(-1.0);
//                rightBackMotor.setPower(1.0);
//            } else {
//                leftFrontMotor.setPower(0);
//                leftBackMotor.setPower(0);
//                rightFrontMotor.setPower(0);
//                rightBackMotor.setPower(0);
//            }
        }
    }
}