package org.firstinspires.ftc.teamcode.TeleOp.intoTheDeep.telemetry;

import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@TeleOp(name="distance", group="testing")

public class distance extends LinearOpMode {
    // ni-ce kool aid person
    // declare drivetrain motors
    private RevColorSensorV3 distance = null;

    @Override
    public void runOpMode() throws InterruptedException {

        distance = hardwareMap.get(RevColorSensorV3.class, "distance");

        waitForStart();

        while (opModeIsActive()) {

            telemetry.addData("distance", distance.getDistance(DistanceUnit.INCH));
            telemetry.update();

        }
    }
}

