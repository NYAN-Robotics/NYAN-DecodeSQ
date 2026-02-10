package org.firstinspires.ftc.teamcode.utilities.robot.subsystems;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.utilities.robot.Alliance;
import org.firstinspires.ftc.teamcode.utilities.robot.RobotEx;

import java.util.List;

public class Turret implements Subsystem {
    private Limelight3A limelight = null;
    private Servo leftTurretServo = null;
    private Servo rightTurretServo = null;
    int id;
    private double tx;

    public void center(Alliance currentAlliance, MultipleTelemetry telemetry) {
        LLResult result = limelight.getLatestResult();
        if (result != null && result.isValid()) {
            List<LLResultTypes.FiducialResult> fiducials = result.getFiducialResults();
            for (LLResultTypes.FiducialResult fiducial : fiducials) {
                id = fiducial.getFiducialId();
            }
            if ((id == 20 && currentAlliance == Alliance.BLUE) || (id == 24 && currentAlliance == Alliance.RED)) {
                tx = result.getTx();
                telemetry.addData("tx", tx);
            } else {
                tx = 0.0;
                telemetry.addData("The thingy doesn't see the right tag", "fix it.");
            }
        } else {
            tx = 0.0;
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
    }
    @Override
    public void onInit(HardwareMap hardwareMap, Telemetry telemetry) {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        telemetry.setMsTransmissionInterval(500);

        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.pipelineSwitch(0);

        leftTurretServo = hardwareMap.get(Servo.class, "leftTurretServo");
        rightTurretServo = hardwareMap.get(Servo.class, "rightTurretServo");

        leftTurretServo.setDirection(Servo.Direction.REVERSE);
        rightTurretServo.setDirection(Servo.Direction.REVERSE);
    }

    @Override
    public void onOpmodeStarted() {

    }

    @Override
    public void onCyclePassed() {

    }
}
