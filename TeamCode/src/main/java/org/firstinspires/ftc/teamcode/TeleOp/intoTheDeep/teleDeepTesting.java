package org.firstinspires.ftc.teamcode.TeleOp.intoTheDeep;

import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp(name="teleDeepTesting", group="intoTheDeepTesting")

public class teleDeepTesting extends LinearOpMode {

    // declare drivetrain motors
    private DcMotor bl = null;
    private DcMotor br = null;
    private DcMotor fl = null;
    private DcMotor fr = null;

    // declare secondary motors
    private DcMotorEx wrist = null;
    private DcMotorEx elbow = null;
    private CRServo intake = null;

    @Override
    public void runOpMode() {

        // init and set up motors
        bl = hardwareMap.get(DcMotor.class, "backLeft");
        br = hardwareMap.get(DcMotor.class, "backRight");
        fl = hardwareMap.get(DcMotor.class, "frontLeft");
        fr = hardwareMap.get(DcMotor.class, "frontRight");
        wrist = hardwareMap.get(DcMotorEx.class, "wrist");
        elbow = hardwareMap.get(DcMotorEx.class, "elbow");

        // init and set up servos
        intake = hardwareMap.get(CRServo.class, "intake");

        // change properties of the wrist & elbow motor
        wrist.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        wrist.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        wrist.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        elbow.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        elbow.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        elbow.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // declare variables (mutable)
        double leftPower;
        double rightPower;
        double leftStrafe;
        double rightStrafe;
        double lWristPower;
        double rWristPower;
        String wristMode = "unlocked";
        String elbowMode = "unlocked";
        int pos;

        // declare speed constants (immutable)
        final double diagonalStrafePower = 0.5;
        final double intakePower = 1.0;
        final double elbowVel = 1000;
        final double wristVel = 200;

        waitForStart();

        while (opModeIsActive()) {

            telemetry.addData("wristMode", wristMode);
            telemetry.addData("elbowMode", elbowMode);
            telemetry.update();


            // these speed variables are mutabable
            leftPower = gamepad1.left_stick_y;
            rightPower = -gamepad1.right_stick_y;
            leftStrafe = gamepad1.left_trigger;
            rightStrafe = gamepad1.right_trigger;
            rWristPower = gamepad2.right_trigger;
            lWristPower = -gamepad2.left_trigger;

            bl.setPower(leftPower);
            fl.setPower(leftPower);

            br.setPower(rightPower);
            fr.setPower(rightPower);

            
            if (gamepad1.left_trigger > 0) {
                // strafe left
                bl.setPower(-leftStrafe);
                fl.setPower(leftStrafe);

                br.setPower(-leftStrafe);
                fr.setPower(leftStrafe);

            } else if (gamepad1.right_trigger > 0) {
                // strafe right
                bl.setPower(rightStrafe);
                fl.setPower(-rightStrafe);

                br.setPower(rightStrafe);
                fr.setPower(-rightStrafe);
            }

            if (gamepad1.dpad_up && gamepad1.left_bumper) {
                 // strafe upLeft
                 bl.setPower(-diagonalStrafePower);
                fl.setPower(0);

                br.setPower(0);
                fr.setPower(diagonalStrafePower);

            } else if (gamepad1.dpad_down && gamepad1.left_bumper) {
                // strafe downLeft
                bl.setPower(0);
                fl.setPower(diagonalStrafePower);

                br.setPower(-diagonalStrafePower);
                fr.setPower(0);

            } else if (gamepad1.dpad_up && gamepad1.right_bumper) {
                // strafe upRight
                bl.setPower(0);
                fl.setPower(-diagonalStrafePower);

                br.setPower(diagonalStrafePower);
                fr.setPower(0);

            } else if (gamepad1.dpad_down && gamepad1.right_bumper) {
                // strafe downRight
                bl.setPower(diagonalStrafePower);
                fl.setPower(0);

                br.setPower(0);
                fr.setPower(-diagonalStrafePower);
            }

            // wrist code
            switch (wristMode) {

                case "unlocked":
                    wrist.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    if (gamepad2.left_trigger > 0) {
                        wrist.setVelocity(-wristVel);
                    } else if (gamepad2.right_trigger > 0) {
                        wrist.setVelocity(wristVel);
                    } else {
                        wrist.setPower(0);
                    }

                    break;

                case "locked":
                    pos = wrist.getCurrentPosition();
                    wrist.setTargetPosition(pos);
                    wrist.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    wrist.setPositionPIDFCoefficients(0.01);
                    break;

            }

            // elbow code
            switch (elbowMode) {

                case "unlocked":
                    elbow.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    if (gamepad2.left_bumper) {
                        elbow.setVelocity(-elbowVel);
                    } else if (gamepad2.right_bumper) {
                        elbow.setVelocity(elbowVel);
                    } else {
                        elbow.setVelocity(0);
                    }
                    break;

                case "locked":
                    pos = elbow.getCurrentPosition();
                    elbow.setTargetPosition(pos);
                    elbow.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    elbow.setVelocity(5000);
                    break;

            }
            // intake code
            if (gamepad2.a) {
                // when player 2 presses "a"
                // suck up 1 sample
                intake.setPower(-intakePower);
                
            } else if (gamepad2.b) {
                // when player 2 presses "b"
                // release the sample 
                intake.setPower(intakePower);

            } else {
                intake.setPower(0);
            }

            if (gamepad2.dpad_up) {
                do {
                    // once "up" is pressed on the dpad
                    // wristMode will stay locked until 
                    // "down" is pressed
                    wristMode = "locked";

                } while (gamepad2.dpad_up);

            } else if (gamepad2.dpad_down) {
                do {
                    // once "down" is pressed on the dpad
                    // wristMode will stay unlocked until 
                    // "up" is pressed
                    wristMode = "unlocked";

                } while (gamepad2.dpad_down);
            }

            if (gamepad2.dpad_left) {
                do {
                    // once "left" is pressed on the dpad
                    // elbowMode will stay locked until 
                    // "right" is pressed
                    elbowMode = "locked";

                } while (gamepad2.dpad_left);

            } else if (gamepad2.dpad_right) {
                do {
                    // once "right" is pressed on the dpad
                    // elbowMode will stay unlocked until 
                    // "left" is pressed
                    elbowMode = "unlocked";

                } while (gamepad2.dpad_right);
            }

        }
    }
}
