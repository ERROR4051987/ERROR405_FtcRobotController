package org.firstinspires.ftc.teamcode.TeleOp.intoTheDeep;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="teleDeep", group="intoTheDeep")

public class teleDeep extends LinearOpMode {

    // declare drivetrain motors
    private DcMotor bl = null;
    private DcMotor br = null;
    private DcMotor fl = null;
    private DcMotor fr = null;

    private CRServo intake = null;



    // declare secondary motors
    private DcMotor elbow = null;


    @Override
    public void runOpMode() {

        // init and set up motors
        bl = hardwareMap.get(DcMotor.class, "backLeft");
        br = hardwareMap.get(DcMotor.class, "backRight");
        fl = hardwareMap.get(DcMotor.class, "frontLeft");
        fr = hardwareMap.get(DcMotor.class, "frontRight");
        elbow = hardwareMap.get(DcMotor.class, "elbow");
        intake = hardwareMap.get(CRServo.class, "intake1");

        // declare variables
        double leftPower;
        double rightPower;
        double leftStrafe;
        double rightStrafe;
        double intakePower;

        // declare speed constants
        final double ePower = 1.0;
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

                elbow.setPower(ePower);

            } else if (gamepad2.right_bumper) {

                elbow.setPower(-ePower);

            } else {

                elbow.setPower(0);
            }

            if (gamepad2.dpad_left) {

                intakePower = -1.0;

            } else if (gamepad2.dpad_right) {

                intakePower = 1.0;

            } else {

                intakePower = 0;

            }

            intake.setPower(intakePower);

        }
    }
}
