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
    private CRServo intake = null;
    private CRServo wrist = null;

    // declare sensors
    private RevColorSensorV3 color = null;

    private PIDController controller;

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

        // customize motor modes
        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        color = hardwareMap.get(RevColorSensorV3.class, "colorLeft");

        intake = hardwareMap.get(CRServo.class, "intake");
        wrist = hardwareMap.get(CRServo.class, "wrist");


        // declare speed variables (mutable)
        double leftPower;
        double rightPower;
        double leftStrafe;
        double rightStrafe;
        double wristPower;

        // declare speed constants (immutable)
        final double diagonalStrafePower = 0.7;
        final double strafeScalar = 0.95;
        final double driveTrainScalar = 0.85;
        final double slideVelocity = 1000;
        final double intakePower = 1.0;

        color.initialize();

        waitForStart();

        while (opModeIsActive()) {
            telemetry.addData("speed", slide.getVelocity());
            telemetry.update();

            // these speed variables are mutabable
            leftPower = gamepad1.left_stick_y;
            rightPower = -gamepad1.right_stick_y;
            leftStrafe = gamepad1.left_trigger;
            rightStrafe = gamepad1.right_trigger;

            wristPower = gamepad2.right_stick_y;

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

            wrist.setPower(wristPower);

            if (gamepad2.right_trigger > 0) {
                arm.setPower(gamepad2.right_trigger);
            } else if (gamepad2.left_trigger > 0) {
                arm.setPower(-gamepad2.left_trigger);
            } else {
                arm.setPower(0);
            }

            if (gamepad2.dpad_up) {
                intake.setPower(intakePower);
            } else if (gamepad2.dpad_down) {
                intake.setPower(-intakePower);
            } else {
                intake.setPower(0);
            }

            if (gamepad2.right_bumper) {
                slide.setVelocity(-slideVelocity);
            } else if (gamepad2.left_bumper) {
                slide.setVelocity(slideVelocity);
            } else {
                slide.setPower(0);
            }

//jonah snuck into the code
            if (!gamepad2.a) {
                    double p = 0.01, i = 0, d = 0;
                    double f = 0.1;

                    final double ticksInDegree = 537.7;
                    controller = new PIDController(p, i, d);

                    controller.setPID(p, i, d);
                    int armPos = arm.getCurrentPosition();
                    int target = armPos += (gamepad2.left_stick_y * 20);

                    double pid = controller.calculate(armPos, target);
                    double ff = Math.cos(Math.toRadians(target / ticksInDegree)) * f;

                    double power = pid + ff;

                    arm.setPower(power);
                    telemetry.addData("TARGET", target);
                    telemetry.update();

            }
        }
    }

}



