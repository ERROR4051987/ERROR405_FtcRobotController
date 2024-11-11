package org.firstinspires.ftc.teamcode.TeleOp.intoTheDeep;

import com.arcrobotics.ftclib.controller.PIDController;
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
    private DcMotorEx slide = null;
    private DcMotorEx arm = null;

    // declare servos
    private CRServo lWrist = null;
    private CRServo rWrist = null;

    // declare sensors
    private RevColorSensorV3 color = null;

    @Override
    public void runOpMode() {

        // init and set up motors
        bl = hardwareMap.get(DcMotor.class, "backLeft");
        br = hardwareMap.get(DcMotor.class, "backRight");
        fl = hardwareMap.get(DcMotor.class, "frontLeft");
        fr = hardwareMap.get(DcMotor.class, "frontRight");
        slide = hardwareMap.get(DcMotorEx.class, "slide");
        arm = hardwareMap.get(DcMotorEx.class, "arm");

        // customize motor zero power behavior
        bl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        br.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        fl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        fr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // customize motor modes
        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        color = hardwareMap.get(RevColorSensorV3.class, "colorLeft");

        // declare variables (mutable)
        double leftPower;
        double rightPower;
        double leftStrafe;
        double rightStrafe;

        // declare speed constants (immutable)
        final double diagonalStrafePower = 0.7;
        final double strafeScalar = 0.95;

        final double driveTrainScalar = 0.85;

        color.initialize();

        waitForStart();

        while (opModeIsActive()) {


            // these speed variables are mutabable
            leftPower = gamepad1.left_stick_y;
            rightPower = -gamepad1.right_stick_y;
            leftStrafe = gamepad1.left_trigger;
            rightStrafe = gamepad1.right_trigger;

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

            }
        }

    }



