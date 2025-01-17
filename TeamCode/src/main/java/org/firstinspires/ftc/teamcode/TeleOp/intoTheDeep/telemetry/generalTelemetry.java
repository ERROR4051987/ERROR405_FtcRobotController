package org.firstinspires.ftc.teamcode.TeleOp.intoTheDeep.telemetry;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@Config
@TeleOp(name= "generalTelemetry", group="testing")
public class generalTelemetry extends OpMode {
    public DcMotorEx arm = null;

    int backPos = 0;
    int frontPos = 0;

    @Override
    public void init () {
        arm = hardwareMap.get(DcMotorEx.class, "arm");

        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }
    @Override
    public void loop() {

        telemetry.addData("current pos", arm.getCurrentPosition());
        telemetry.addData("origin", backPos);
        telemetry.addData("frontBoundary", frontPos);
        telemetry.update();

        if (gamepad2.a) {
            backPos = arm.getCurrentPosition();
        } else if (gamepad2.b) {
            frontPos = arm.getCurrentPosition();
        }

    }
}