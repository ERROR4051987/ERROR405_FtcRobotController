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

    // declare sensors
    private RevColorSensorV3 color = null;

    @Override
    public void runOpMode() {

        // init and set up drivetrain motors
        bl = hardwareMap.get(DcMotor.class, "backLeft");
        br = hardwareMap.get(DcMotor.class, "backRight");
        fl = hardwareMap.get(DcMotor.class, "frontLeft");
        fr = hardwareMap.get(DcMotor.class, "frontRight");

        bl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        br.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        fl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        fr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // init and set up secondary motors
        wrist = hardwareMap.get(DcMotorEx.class, "wrist");
        elbow = hardwareMap.get(DcMotorEx.class, "elbow");

        // init and set up servos
        intake = hardwareMap.get(CRServo.class, "intake");

        color = hardwareMap.get(RevColorSensorV3.class, "colorLeft");

        // change properties of the wrist & elbow motor
        wrist.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        wrist.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        wrist.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        elbow.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        elbow.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        elbow.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        elbow.setPositionPIDFCoefficients(100);

        // declare variables (mutable)
        double leftPower;
        double rightPower;
        double leftStrafe;
        double rightStrafe;
        double lWristPower;
        double rWristPower;
        String wristMode = "unlocked";
        String twinTowerMode = "unlocked";

        // declare speed constants (immutable)
        final double diagonalStrafePower = 0.5;
        final double intakePower = 1.0;
        final double elbowVel = 1500;
        final double wristVel = 500;

        int pos;

        color.initialize();

        waitForStart();

        while (opModeIsActive()) {

            telemetry.addData("wristMode", wristMode);
            telemetry.addData("twinTowerMode", twinTowerMode);
            telemetry.addData("elbow pidf", elbow.getPIDFCoefficients(DcMotor.RunMode.RUN_TO_POSITION));
            telemetry.addData("wrist pidf", wrist.getPIDFCoefficients(DcMotor.RunMode.RUN_TO_POSITION));
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

            // strafe left and right
            if (gamepad1.left_trigger > 0) {

                bl.setPower(-leftStrafe);
                fl.setPower(leftStrafe);

                br.setPower(-leftStrafe);
                fr.setPower(leftStrafe);

            } else if (gamepad1.right_trigger > 0) {

                bl.setPower(rightStrafe);
                fl.setPower(-rightStrafe);

                br.setPower(rightStrafe);
                fr.setPower(-rightStrafe);
            }

            if (gamepad1.dpad_up && gamepad1.left_bumper) {

                bl.setPower(-diagonalStrafePower);
                fl.setPower(0);

                br.setPower(0);
                fr.setPower(diagonalStrafePower);
                // upLeft

            } else if (gamepad1.dpad_down && gamepad1.left_bumper) {

                bl.setPower(0);
                fl.setPower(diagonalStrafePower);

                br.setPower(-diagonalStrafePower);
                fr.setPower(0);
                // downLeft

            } else if (gamepad1.dpad_up && gamepad1.right_bumper) {


                bl.setPower(0);
                fl.setPower(-diagonalStrafePower);

                br.setPower(diagonalStrafePower);
                fr.setPower(0);
                // upRight

            } else if (gamepad1.dpad_down && gamepad1.right_bumper) {

                bl.setPower(diagonalStrafePower);
                fl.setPower(0);

                br.setPower(0);
                fr.setPower(-diagonalStrafePower);

                // downRight

            }

            // wrist code
            switch (wristMode) {

                case "unlocked":
                    wrist.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    if (gamepad2.left_trigger > 0) {
                        wrist.setVelocity(wristVel);
                    } else if (gamepad2.right_trigger > 0) {
                        wrist.setVelocity(-wristVel);
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
            switch (twinTowerMode) {

                case "unlocked":
                    elbow.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    if (gamepad2.left_bumper) {
                        elbow.setVelocity(elbowVel);
                    } else if (gamepad2.right_bumper) {
                        elbow.setVelocity(-elbowVel);
                    } else {
                        elbow.setVelocity(0);
                    }
                    break;

                case "locked":
                    elbow.setVelocityPIDFCoefficients(100, 5, 2, 0);
                    pos = elbow.getCurrentPosition();
                    elbow.setTargetPosition(pos);
                    elbow.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    elbow.setVelocity(5000);
                    break;

            }
            // intake code
            if (gamepad2.a) {
                intake.setPower(-intakePower);

            } else if (gamepad2.b) {
                intake.setPower(intakePower);

            } else {
                intake.setPower(0);
            }


            if (gamepad2.dpad_up) {
                do {
                    wristMode = "locked";

                } while (gamepad2.dpad_up);

            } else if (gamepad2.dpad_down) {
                do {
                    wristMode = "unlocked";

                } while (gamepad2.dpad_down);
            }

            if (gamepad2.dpad_left) {
                do {
                    twinTowerMode = "locked";

                } while (gamepad2.dpad_left);

            } else if (gamepad2.dpad_right) {
                do {
                    twinTowerMode = "unlocked";

                } while (gamepad2.dpad_right);
            }

        }
    }
}
