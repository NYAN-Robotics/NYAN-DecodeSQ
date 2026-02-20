package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
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
import org.firstinspires.ftc.teamcode.utilities.robot.Alliance;
import org.firstinspires.ftc.teamcode.utilities.robot.RobotEx;

import java.util.List;

// I hope this works
@Autonomous(name="High Blue Auto")
public class HighBlueAuto extends LinearOpMode {
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

    @Override
    public void runOpMode() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        telemetry.setMsTransmissionInterval(500);

        RobotEx robot = RobotEx.getInstance();
        robot.init(this, telemetry);

        limelight = hardwareMap.get(Limelight3A.class, "limelight");

        limelight.pipelineSwitch(0);

        leftFrontMotor = hardwareMap.get(DcMotorEx.class, "leftFrontMotor");
        leftBackMotor = hardwareMap.get(DcMotorEx.class, "leftBackMotor");
        rightFrontMotor = hardwareMap.get(DcMotorEx.class, "rightFrontMotor");
        rightBackMotor = hardwareMap.get(DcMotorEx.class, "rightBackMotor");

        leftFrontMotor.setDirection(DcMotorEx.Direction.REVERSE);
        leftBackMotor.setDirection(DcMotorEx.Direction.REVERSE);
        rightFrontMotor.setDirection(DcMotorEx.Direction.FORWARD);
        rightBackMotor.setDirection(DcMotorEx.Direction.FORWARD);

        leftFrontMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        leftBackMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        rightFrontMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        rightBackMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

        leftTurretServo = hardwareMap.get(Servo.class, "leftTurretServo");
        rightTurretServo = hardwareMap.get(Servo.class, "rightTurretServo");
        rightPivotServo = hardwareMap.get(Servo.class, "rightPivotServo");
        leftPivotServo = hardwareMap.get(Servo.class, "leftPivotServo");
        rightTransferServo = hardwareMap.get(Servo.class, "rightTransferServo");
        leftTransferServo = hardwareMap.get(Servo.class, "leftTransferServo");

        leftTurretServo.setDirection(Servo.Direction.REVERSE);
        rightTurretServo.setDirection(Servo.Direction.REVERSE);
        rightPivotServo.setDirection(Servo.Direction.FORWARD);
        leftPivotServo.setDirection(Servo.Direction.REVERSE);
        rightTransferServo.setDirection(Servo.Direction.FORWARD);
        leftTransferServo.setDirection(Servo.Direction.REVERSE);

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
        boolean highTurret = false;

        Alliance currentAlliance = Alliance.BLUE;

        while (opModeInInit()) {
            telemetry.addData("Alliance", currentAlliance.name());
            telemetry.addData("Circle for blue", "Square for red");
            telemetry.update();

            if (gamepad1.circle) {
                currentAlliance = Alliance.BLUE;
            }
            if (gamepad1.square) {
                currentAlliance = Alliance.RED;
            }
            leftTurretServo.setPosition(0.7);
            rightTurretServo.setPosition(0.7);

            rightPivotServo.setPosition(1.0);
            leftPivotServo.setPosition(1.0);
        }

        runtime.reset();
        limelight.start();

        while (opModeIsActive()) {
            int id = 0;
            double tx = 0;
            double ty = 0;

            robot.theTurret.center(currentAlliance, (MultipleTelemetry) telemetry);

            rightPivotServo.setPosition(1.0);
            leftPivotServo.setPosition(1.0);

            if (isStopRequested()) return;

            if (runtime.milliseconds() < 500) {
                    leftFrontMotor.setPower(0.5);
                    leftBackMotor.setPower(0.5);
                    rightFrontMotor.setPower(0.5);
                    rightBackMotor.setPower(0.5);
            }
            else if (runtime.milliseconds() < 2000) {
                leftFrontMotor.setPower(0);
                leftBackMotor.setPower(0);
                rightFrontMotor.setPower(0);
                rightBackMotor.setPower(0);
                robot.theOuttake.outtake(0.85 * robot.getPowerMultiple());
            } else if (runtime.milliseconds() < 12000) {
                for (int i = 0; i < 4; i++) {
                    robot.theOuttake.transfer(0.85 * robot.getPowerMultiple(), currentAlliance);
                    transferTime.reset();
                    while (transferTime.milliseconds() < 250) {
                        robot.theIntake.intakeReverse();
                        robot.theTurret.center(currentAlliance, (MultipleTelemetry) telemetry);
                    }
                    while (transferTime.milliseconds() < 500) {
                        robot.theIntake.intakeForward();
                        robot.theTurret.center(currentAlliance, (MultipleTelemetry) telemetry);
                    }
                    robot.theIntake.stopIntake();
                }
            } else if (runtime.milliseconds() < 13000) {
                robot.theOuttake.stopOuttake();
                robot.theIntake.stopIntake();
                leftFrontMotor.setPower(0.5);
                leftBackMotor.setPower(-0.5);
                rightFrontMotor.setPower(-0.5);
                rightBackMotor.setPower(0.5);
            } else if (runtime.milliseconds() < 29000){
                leftFrontMotor.setPower(0);
                leftBackMotor.setPower(0);
                rightFrontMotor.setPower(0);
                rightBackMotor.setPower(0);
            } else {
                return;
            }
        }
    }
}