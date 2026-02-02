package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.utilities.robot.Alliance;

import java.util.List;

// I hope this works
@TeleOp(name="Test Teleop")
public class TestTeleop extends LinearOpMode {
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
            telemetry.update();

            if (gamepad1.circle) {
                currentAlliance = Alliance.BLUE;
            }
            if (gamepad1.square) {
                currentAlliance = Alliance.RED;
            }
        }

        runtime.reset();
        limelight.start();

        while (opModeIsActive()) {
            int id = 0;
            double tx = 0;
            double ty = 0;
            double max;

            double axial = gamepad1.left_stick_y; // y is inverted to reverse the robot
            double yaw = -gamepad1.left_stick_x;
            double lateral = gamepad1.right_stick_x;

            double leftFrontPower = axial + lateral + yaw;
            double leftBackPower = axial - lateral + yaw;
            double rightFrontPower = axial - lateral - yaw;
            double rightBackPower = axial + lateral - yaw;

            max = Math.max(Math.abs(leftFrontPower), Math.abs(rightFrontPower));
            max = Math.max(max, Math.abs(leftBackPower));
            max = Math.max(max, Math.abs(rightBackPower));

            if (max > 1.0) {
                leftFrontPower /= max;
                leftBackPower /= max;
                rightFrontPower /= max;
                rightBackPower /= max;
            }

            leftFrontMotor.setPower(leftFrontPower);
            leftBackMotor.setPower(leftBackPower);
            rightFrontMotor.setPower(rightFrontPower);
            rightBackMotor.setPower(rightBackPower);

            LLResult result = limelight.getLatestResult();
            if (result != null && result.isValid()) {
                List<LLResultTypes.FiducialResult> fiducials = result.getFiducialResults();
                for (LLResultTypes.FiducialResult fiducial : fiducials) {
                    id = fiducial.getFiducialId();
                }
                if ((id == 20 && currentAlliance == Alliance.BLUE) || (id == 24 && currentAlliance == Alliance.RED)) {
                    tx = result.getTx();
                    ty = result.getTy();
                    telemetry.addData("tx", tx);
                    telemetry.addData("ty", ty);
                }
            } else {
                telemetry.addData("The thingy is broken", "because of the limelight");
            }
            double ltpos = leftTurretServo.getPosition();
            double rtpos = rightTurretServo.getPosition();
            telemetry.addData("ltpos", ltpos);
            telemetry.addData("rtpos", rtpos);
            telemetry.update();
            if (tx <= -2) {
                leftTurretServo.setPosition(ltpos + 0.001);
                rightTurretServo.setPosition(rtpos + 0.001);
            } else if (tx >= 2) {
                leftTurretServo.setPosition(ltpos - 0.001);
                rightTurretServo.setPosition(rtpos - 0.001);
            }

            leftTransferServo.setPosition(0.52);
            rightTransferServo.setPosition(0.52);
            if (gamepad1.dpad_up && turretModeTime.milliseconds() > 500) {
                turretModeTime.reset();
                if (!highTurret) {
                    highTurret = true;
                } else {
                    highTurret = false;
                }
            }
            if (highTurret) {
                rightPivotServo.setPosition(0.25);
                leftPivotServo.setPosition(0.25);
            } else {
                rightPivotServo.setPosition(0.5);
                leftPivotServo.setPosition(0.5);
            }
//            if (gamepad1.circle) {
//                leftFrontMotor.setDirection(DcMotorEx.Direction.REVERSE);
//                leftBackMotor.setDirection(DcMotorEx.Direction.FORWARD);
//                rightFrontMotor.setDirection(DcMotorEx.Direction.FORWARD);
//                rightBackMotor.setDirection(DcMotorEx.Direction.REVERSE);
//            } else if (gamepad1.x) {
//                leftFrontMotor.setDirection(DcMotorEx.Direction.FORWARD);
//                leftBackMotor.setDirection(DcMotorEx.Direction.REVERSE);
//                rightFrontMotor.setDirection(DcMotorEx.Direction.REVERSE);
//                rightBackMotor.setDirection(DcMotorEx.Direction.FORWARD);
//            }
            if (gamepad1.right_trigger >= 0.1) {
                rightIntakeMotor.setPower(1.0);
                leftIntakeMotor.setPower(1.0);
            } else if (gamepad1.right_bumper) {
                rightIntakeMotor.setPower(-1.0);
                leftIntakeMotor.setPower(-1.0);
            } else {
                rightIntakeMotor.setPower(0.0);
                leftIntakeMotor.setPower(0.0);
            }

            if (gamepad1.left_trigger > 0.1) {
                leftOuttakeMotor.setPower(1.0);
                rightOuttakeMotor.setPower(1.0);
            } else {
                rightOuttakeMotor.setPower(0.0);
                leftOuttakeMotor.setPower(0.0);
            }
            if (gamepad1.left_bumper) {
                leftIntakeMotor.setPower(0.0);
                rightIntakeMotor.setPower(0.0);
                leftOuttakeMotor.setPower(1.0);
                rightOuttakeMotor.setPower(1.0);
                transferTime.reset();
                while (transferTime.milliseconds() < 1000) {
                    leftOuttakeMotor.setPower(1.0);
                    rightOuttakeMotor.setPower(1.0);
                }
                while (transferTime.milliseconds() < 1200) {
                    leftTransferServo.setPosition(0.10);
                    rightTransferServo.setPosition(0.10);
                }
                while (transferTime.milliseconds() < 1390) {
                    leftTransferServo.setPosition(0.52);
                    rightTransferServo.setPosition(0.52);
                }
                while (transferTime.milliseconds() < 1600) {
                    rightIntakeMotor.setPower(0.0);
                    leftIntakeMotor.setPower(0.0);
                }
                while (transferTime.milliseconds() < 2000) {
                    rightIntakeMotor.setPower(1.0);
                    leftIntakeMotor.setPower(1.0);
                }
            }

            telemetry.addData("Status", "Run Time:" + runtime.toString());
            telemetry.addData("Front left/Right", "%4.2f, %4.2f", leftFrontPower, rightFrontPower);
            telemetry.addData("Back left/Right", "%4.2f, %4.2f", leftBackPower, rightBackPower);
            telemetry.update();
        }
    }
}
