package org.firstinspires.ftc.teamcode.TeleOp.intoTheDeep;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name="teleDeep", group="intoTheDeep")

public class teleDeep extends LinearOpMode {

    // declare drivetrain motors
    private DcMotor bl = null;
    private DcMotor br = null;
    private DcMotor fl = null;
    private DcMotor fr = null;

    // declare secondary motors
    private DcMotor s = null;


    @Override
    public void runOpMode() {

        // init and set up motors
        bl = hardwareMap.get(DcMotor.class, "backLeft");
        br = hardwareMap.get(DcMotor.class, "backRight");
        fl = hardwareMap.get(DcMotor.class, "frontLeft");
        fr = hardwareMap.get(DcMotor.class, "frontRight");
        s = hardwareMap.get(DcMotor.class, "s");

        // declare variables
        double leftPower;
        double rightPower;
        double leftStrafe;
        double rightStrafe;
        double sPower = 1.0;

        final double diagonalStrafePower = 1.0;

        waitForStart();

        while (opModeIsActive()) {

            leftPower = gamepad1.left_stick_y;
            rightPower = -gamepad1.right_stick_y;
            leftStrafe = gamepad1.left_trigger;
            rightStrafe = gamepad1.right_trigger;

            bl.setPower(leftPower);
            fl.setPower(leftPower);

            br.setPower(rightPower);
            fr.setPower(rightPower);

            if (gamepad2.left_bumper) {
                s.setPower(sPower);
            } else if (gamepad2.right_bumper) {
                s.setPower(-sPower);
            } else {
                s.setPower(0);
            }

        }
    }
}
