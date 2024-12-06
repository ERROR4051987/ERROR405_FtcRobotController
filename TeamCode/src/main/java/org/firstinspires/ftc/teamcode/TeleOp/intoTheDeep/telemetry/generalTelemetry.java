package org.firstinspires.ftc.teamcode.TeleOp.intoTheDeep.telemetry;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@Config
@TeleOp(name= "generalTelemetry", group="testing")
public class generalTelemetry extends OpMode {
    public DcMotorEx slide = null;

    int backPos = 0;
    int frontPos = 0;

    @Override
    public void init () {
        slide = hardwareMap.get(DcMotorEx.class, "slide");

        slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }
    @Override
    public void loop() {

        telemetry.addData("current pos", slide.getCurrentPosition());
        telemetry.addData("backBoundary", backPos);
        telemetry.addData("frontBoundary", frontPos);
        telemetry.update();

        if (gamepad2.a) {
            backPos = slide.getCurrentPosition();
        } else if (gamepad2.b) {
            frontPos = slide.getCurrentPosition();
        }

        if (gamepad2.x) {
            slide.setTargetPosition(backPos);
            slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            slide.setVelocity(500);
        } else if (gamepad2.y) {
            slide.setTargetPosition(frontPos);
            slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            slide.setVelocity(500);
        }
    }
}