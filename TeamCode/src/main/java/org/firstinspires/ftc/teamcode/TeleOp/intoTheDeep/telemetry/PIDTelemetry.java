package org.firstinspires.ftc.teamcode.TeleOp.intoTheDeep.telemetry;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@Config
@TeleOp(name="PIDTelemetry", group="testing")

public class PIDTelemetry extends OpMode {

    private DcMotorEx lift = null;

    // make funny pid
    private PIDController controller;

    // pid variables
    public static double p = 0, i = 0, d = 0;
    public static double f = 0;

    public static int target = 0;
    public final double ticksInDegree = 537.7;

    @Override
    public void init() {
        controller = new PIDController(p, i, d);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        lift = hardwareMap.get(DcMotorEx.class, "arm");
        lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    @Override
    public void loop() {
        controller.setPID(p, i, d);
        int liftPos = lift.getCurrentPosition();
        double pid = controller.calculate(liftPos, target);
        double ff = Math.cos(Math.toRadians(target / ticksInDegree)) * f;

        double power = pid + ff;

        lift.setPower(power);

        telemetry.addData("pos", liftPos);
        telemetry.addData("target", target);
        telemetry.addData("on?", lift.isMotorEnabled());
        telemetry.update();
    }
}

