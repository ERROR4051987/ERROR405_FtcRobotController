package org.firstinspires.ftc.teamcode.TeleOp.intoTheDeep;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Config
@TeleOp(name="telemetry", group="testing")

public class telemetry extends OpMode {

    private DcMotorEx elbow = null;
    private DcMotorEx wrist = null;

    // make funny pid
    private PIDController controller;

    // pid variables
    public static double p = 0, i = 0, d = 0;
    public static double f = 0;

    public static int target = 0;
    // TODO: change ticks to actual ticks
    public final double ticksInDegree = 1425.1;

    @Override
    public void init() {
        controller = new PIDController(p, i, d);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        elbow = hardwareMap.get(DcMotorEx.class, "wrist");
        elbow.setDirection(DcMotorSimple.Direction.REVERSE);
        elbow.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        elbow.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    @Override
    public void loop() {
        controller.setPID(p, i, d);
        int elbowPos = elbow.getCurrentPosition();
        double pid = controller.calculate(elbowPos, target);
        double ff = Math.cos(Math.toRadians(target / ticksInDegree)) * f;

        double power = pid + ff;

        elbow.setPower(power);

        telemetry.addData("pos", elbowPos);
        telemetry.addData("target", target);
        telemetry.update();
    }
}

