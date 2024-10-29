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

    private DcMotorEx wrist = null;
    private DcMotorEx elbow = null;

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

        wrist = hardwareMap.get(DcMotorEx.class, "wrist");
        wrist.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    @Override
    public void loop() {
        controller.setPID(p, i, d);
        int wristPos = wrist.getCurrentPosition();
        double pid = controller.calculate(wristPos, target);
        double ff = Math.cos(Math.toRadians(target / ticksInDegree)) * f;

        double power = pid + ff;

        wrist.setPower(power);

        telemetry.addData("pos", wristPos);
        telemetry.addData("target", target);
        telemetry.update();
    }
}

