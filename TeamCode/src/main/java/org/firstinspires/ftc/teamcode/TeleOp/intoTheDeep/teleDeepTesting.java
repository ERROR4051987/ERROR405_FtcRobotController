package org.firstinspires.ftc.teamcode.TeleOp.intoTheDeep;

import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name="teleDeepTesting", group="intoTheDeepTesting")

public class teleDeepTesting extends LinearOpMode {
// ni-ce kool aid person
    // declare drivetrain motors
    private DcMotor bl = null;
    private DcMotor br = null;
    private DcMotor fl = null;
    private DcMotor fr = null;

    // declare secondary motors
    private DcMotorEx lHanger = null;
    private DcMotorEx rHanger = null;
    private DcMotorEx lift = null;

    // declare servos
    private PIDController controller;

    @Override
    public void runOpMode() {

        // init and set up motors
        bl = hardwareMap.get(DcMotor.class, "backLeft");
        br = hardwareMap.get(DcMotor.class, "backRight");
        fl = hardwareMap.get(DcMotor.class, "frontLeft");
        fr = hardwareMap.get(DcMotor.class, "frontRight");
        lift = hardwareMap.get(DcMotorEx.class, "lift");
        lHanger = hardwareMap.get(DcMotorEx.class, "leftHanger");
        rHanger = hardwareMap.get(DcMotorEx.class, "rightHanger");

        // customize motor zero power behavior
        bl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        br.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        fl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        fr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lHanger.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rHanger.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rHanger.setDirection(DcMotorSimple.Direction.REVERSE);

        // customize motor modes
        lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lHanger.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lHanger.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rHanger.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rHanger.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // declare speed variables (mutable)
        double leftPower;
        double rightPower;
        double leftStrafe;
        double rightStrafe;

        // declare speed constants (immutable)
        final double diagonalStrafePower = 0.7;
        final double strafeScalar = 0.95;
        final double driveTrainScalar = 0.85;

        // declare position constants
        final int hangMax = 8000;
        final int hangMaxSpeed = 4500;
        final int hangMin = 100;
        final int hangMinSpeed = 4000;

        //carter quit looking at this
        waitForStart();

        while (opModeIsActive()) {

            // these speed variables are mutabable
            leftPower = gamepad1.left_stick_y;
            rightPower = -gamepad1.right_stick_y;
            leftStrafe = gamepad1.left_trigger;
            rightStrafe = gamepad1.right_trigger;

            // player 2 booleans
            boolean hangReady = gamepad2.a && !gamepad2.x;
            boolean hang = gamepad2.a && gamepad2.x;

            bl.setPower(leftPower * driveTrainScalar);
            fl.setPower(leftPower * driveTrainScalar);

            br.setPower(rightPower * driveTrainScalar);
            fr.setPower(rightPower * driveTrainScalar);

            // strafe left and right
            if (gamepad1.left_trigger > 0) {

                bl.setPower(-leftStrafe * strafeScalar);
                fl.setPower(leftStrafe * strafeScalar);

                br.setPower(-leftStrafe * strafeScalar);
                fr.setPower(leftStrafe * strafeScalar);

            } else if (gamepad1.right_trigger > 0) {

                bl.setPower(rightStrafe * strafeScalar);
                fl.setPower(-rightStrafe * strafeScalar);

                br.setPower(rightStrafe * strafeScalar);
                fr.setPower(-rightStrafe * strafeScalar);
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

                //skibidi
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

            if (hangReady) {
                lHanger.setTargetPosition(hangMax);
                lHanger.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                lHanger.setVelocity(hangMaxSpeed);
                rHanger.setTargetPosition(hangMax);
                rHanger.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                rHanger.setVelocity(hangMaxSpeed);
            } else if (hang) {
                lHanger.setTargetPosition(hangMin);
                lHanger.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                lHanger.setVelocity(hangMinSpeed);
                rHanger.setTargetPosition(hangMin);
                rHanger.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                rHanger.setVelocity(hangMinSpeed);
                sleep(30000);
            } else {
                lHanger.setPower(0);
                rHanger.setPower(0);
            }
        }
    }
//ion like watewrmelon chicken shadow people
}
