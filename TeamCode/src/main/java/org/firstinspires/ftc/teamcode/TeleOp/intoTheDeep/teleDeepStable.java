package org.firstinspires.ftc.teamcode.TeleOp.intoTheDeep;

import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp(name="teleDeepStable", group="intoTheDeep")

public class teleDeepStable extends LinearOpMode {

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

        // declare variables (mutable)
        double leftPower;
        double rightPower;
        double leftStrafe;
        double rightStrafe;
        double lWristPower;
        double rWristPower;
        String wristMode = "unlocked";
        String twinTowerMode = "unlocked";
        int pos;

        // declare speed constants (immutable)
        final double diagonalStrafePower = 0.5;
        final double intakePower = 1.0;
        final double elbowVel = 1000;
        final double wristVel = 200;
        final double driveTrainScalar = 0.85;

        color.initialize();

        waitForStart();

        while (opModeIsActive()) {


            telemetry.addData("wristMode", wristMode);
            telemetry.addData("twinTowerMode", twinTowerMode);
            telemetry.update();


            // these speed variables are mutabable
            leftPower = gamepad1.left_stick_y;
            rightPower = -gamepad1.right_stick_y;
            leftStrafe = gamepad1.left_trigger;
            rightStrafe = gamepad1.right_trigger;
            rWristPower = gamepad2.right_trigger;
            lWristPower = -gamepad2.left_trigger;

            bl.setPower(leftPower * driveTrainScalar);
            fl.setPower(leftPower * driveTrainScalar);

            br.setPower(rightPower * driveTrainScalar);
            fr.setPower(rightPower * driveTrainScalar);

            // strafe left and right
            if (gamepad1.left_trigger > 0) {

                bl.setPower(-leftStrafe * driveTrainScalar);
                fl.setPower(leftStrafe * driveTrainScalar);

                br.setPower(-leftStrafe * driveTrainScalar);
                fr.setPower(leftStrafe * driveTrainScalar);

            } else if (gamepad1.right_trigger > 0) {

                bl.setPower(rightStrafe * driveTrainScalar);
                fl.setPower(-rightStrafe * driveTrainScalar);

                br.setPower(rightStrafe * driveTrainScalar);
                fr.setPower(-rightStrafe * driveTrainScalar);
            }

            if (gamepad1.dpad_up && gamepad1.left_bumper) {

                bl.setPower(-diagonalStrafePower * driveTrainScalar);
                fl.setPower(0);

                br.setPower(0);
                fr.setPower(diagonalStrafePower * driveTrainScalar);
                // upLeft

            } else if (gamepad1.dpad_down && gamepad1.left_bumper) {

                bl.setPower(0);
                fl.setPower(diagonalStrafePower * driveTrainScalar);

                br.setPower(-diagonalStrafePower * driveTrainScalar);
                fr.setPower(0);
                // downLeft

            } else if (gamepad1.dpad_up && gamepad1.right_bumper) {


                bl.setPower(0);
                fl.setPower(-diagonalStrafePower * driveTrainScalar);

                br.setPower(diagonalStrafePower * driveTrainScalar);
                fr.setPower(0);
                // upRight

            } else if (gamepad1.dpad_down && gamepad1.right_bumper) {

                bl.setPower(diagonalStrafePower * driveTrainScalar);
                fl.setPower(0);

                br.setPower(0);
                fr.setPower(-diagonalStrafePower * driveTrainScalar);

                // downRight

            }

            switch (wristMode) {

                case "unlocked":
                    wrist.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    if (gamepad2.left_trigger > 0) {
                        wrist.setVelocity(-wristVel);
                    } else if (gamepad2.right_trigger > 0) {
                        wrist.setVelocity(wristVel);
                    } else {
                        wrist.setVelocity(0);
                    }

                    break;

                case "locked":
                    pos = wrist.getCurrentPosition();
                    wrist.setTargetPosition(pos);
                    wrist.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    wrist.setVelocity(5000);
                    while (opModeIsActive() && !gamepad2.dpad_down) {
                        sleep(1);
                    }
                    break;

            }

            switch (twinTowerMode) {

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
                    while (opModeIsActive() && !gamepad2.dpad_right) {
                        sleep(1);
                    }
                    break;

            }

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
