package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
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
import org.firstinspires.ftc.teamcode.utilities.math.linearalgebra.Pose;
import org.firstinspires.ftc.teamcode.utilities.robot.Alliance;
import org.firstinspires.ftc.teamcode.utilities.robot.RobotEx;
import org.firstinspires.ftc.teamcode.utilities.robot.subsystems.Outtake;

import java.util.List;

// I hope this works
@TeleOp(name="Field Centric Teleop")
public class FieldCentricDrive extends LinearOpMode {
    private DcMotorEx leftFrontMotor = null;
    private DcMotorEx leftBackMotor = null;
    private DcMotorEx rightFrontMotor = null;
    private DcMotorEx rightBackMotor = null;

    @Override
    public void runOpMode() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        telemetry.setMsTransmissionInterval(500);

        RobotEx robot = RobotEx.getInstance();
        robot.init(this, telemetry);
        robot.theLocalizer.setPose(new Pose(0, 0, Math.PI / 2));

//
//        leftFrontMotor = hardwareMap.get(DcMotorEx.class, "leftFrontMotor");
//        leftBackMotor = hardwareMap.get(DcMotorEx.class, "leftBackMotor");
//        rightFrontMotor = hardwareMap.get(DcMotorEx.class, "rightFrontMotor");
//        rightBackMotor = hardwareMap.get(DcMotorEx.class, "rightBackMotor");
//
//        leftFrontMotor.setDirection(DcMotorEx.Direction.REVERSE);
//        leftBackMotor.setDirection(DcMotorEx.Direction.FORWARD);
//        rightFrontMotor.setDirection(DcMotorEx.Direction.FORWARD);
//        rightBackMotor.setDirection(DcMotorEx.Direction.REVERSE);
//
//        leftFrontMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);
//        leftBackMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);
//        rightFrontMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);
//        rightBackMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

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
        }

        waitForStart();

        // Notify subsystems before loop
        robot.postStart();

        if (isStopRequested()) return;

        robot.pause(0.5);

        while (opModeIsActive()) {

            double axial = gamepad1.left_stick_y; // y is inverted to reverse the robot
            double lateral = -gamepad1.left_stick_x;
            double yaw = gamepad1.right_stick_x;

            robot.theDrivetrain.fieldCentricDriveFromGamepad(axial, lateral, yaw);

            telemetry.addData("Status", "Driving");
            telemetry.update();
        }

        while (!isStopRequested()) {
            robot.update();
        }
    }
}
