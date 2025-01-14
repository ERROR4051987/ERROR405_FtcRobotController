package org.firstinspires.ftc.teamcode.TeleOp.intoTheDeep;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="teleDeepStable", group="intoTheDeep")

public class teleDeepStable extends LinearOpMode {
    // ni-ce kool aid person
    // declare drivetrain motors
    private DcMotor bl = null;
    private DcMotor br = null;
    private DcMotor fl = null;
    private DcMotor fr = null;

    // declare secondary motors
    private DcMotorEx lHanger = null;
    private DcMotorEx rHanger = null;
    private DcMotorEx arm = null;
    private DcMotorEx slide = null;

    // declare servos
    private Servo lGripper = null;
    private Servo rGripper = null;

    @Override
    public void runOpMode() throws InterruptedException {

        // init and set up motors
        bl = hardwareMap.get(DcMotor.class, "backLeft");
        br = hardwareMap.get(DcMotor.class, "backRight");
        fl = hardwareMap.get(DcMotor.class, "frontLeft");
        fr = hardwareMap.get(DcMotor.class, "frontRight");
        arm = hardwareMap.get(DcMotorEx.class, "arm");
        slide = hardwareMap.get(DcMotorEx.class, "slide");
        lHanger = hardwareMap.get(DcMotorEx.class, "leftHanger");
        rHanger = hardwareMap.get(DcMotorEx.class, "rightHanger");

        // init and set up servos
        lGripper = hardwareMap.get(Servo.class, "lGrip");
        rGripper = hardwareMap.get(Servo.class, "rGrip");

        // customize motor zero power behavior
        bl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        br.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        fl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        fr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rHanger.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lHanger.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // customize motor modes
        slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slide.setTargetPosition(0);
        slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lHanger.setDirection(DcMotorSimple.Direction.REVERSE);
        lHanger.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lHanger.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rHanger.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rHanger.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // declare speed variables (mutable)
        double leftPower;
        double rightPower;
        double leftStrafe;
        double rightStrafe;
        double armLowerPower;
        double armRaisePower;

        // declare speed constants (immutable)
        final double diagonalStrafePower = 0.7;
        final double strafeScalar = 0.95;
        final double driveTrainScalar = 0.85;
        final double slideExtend = 0.7;
        final double slideRetract = -500;
        final double armVelocityScalar = 600;

        // declare position constants
        final int hangMax = 8500;
        final int hangMaxSpeed = 4500;
        final int hangMin = 100;
        final int hangMinSpeed = 4000;
        final double lGripClose = 0.5;
        final double lGripOpen = 0.0;
        final double rGripClose = 0.5;
        final double rGripOpen = 1.0;

        //carter quit looking at this
        waitForStart();

        while (opModeIsActive()) {

            telemetry.addData("arm pid", arm.getPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER));
            telemetry.addLine("change setVelocityPIDFCoefficients to use the same PID, but raise the F value");
            telemetry.update();

            // these speed variables are mutabable
            leftPower = gamepad1.left_stick_y;
            rightPower = -gamepad1.right_stick_y;
            leftStrafe = gamepad1.left_trigger;
            rightStrafe = gamepad1.right_trigger;

            armLowerPower = gamepad2.left_trigger;
            armRaisePower = gamepad2.right_trigger;

            // player 2 booleans
            boolean hangStart = gamepad2.a && !gamepad2.y;
            boolean hanging = gamepad2.a && gamepad2.y;

            if (!hanging) {
                bl.setPower(leftPower * driveTrainScalar);
                fl.setPower(leftPower * driveTrainScalar);

                br.setPower(rightPower * driveTrainScalar);
                fr.setPower(rightPower * driveTrainScalar);
            }

            // strafe left and right
            if (gamepad1.left_trigger > 0 && !hanging) {

                bl.setPower(-leftStrafe * strafeScalar);
                fl.setPower(leftStrafe * strafeScalar);

                br.setPower(-leftStrafe * strafeScalar);
                fr.setPower(leftStrafe * strafeScalar);

            } else if (gamepad1.right_trigger > 0 && !hanging) {

                bl.setPower(rightStrafe * strafeScalar);
                fl.setPower(-rightStrafe * strafeScalar);

                br.setPower(rightStrafe * strafeScalar);
                fr.setPower(-rightStrafe * strafeScalar);
            }

            if (gamepad1.dpad_up && gamepad1.left_bumper && !hanging) {

                bl.setPower(-diagonalStrafePower);
                fl.setPower(0);

                br.setPower(0);
                fr.setPower(diagonalStrafePower);
                // upLeft

            } else if (gamepad1.dpad_down && gamepad1.left_bumper && !hanging) {

                bl.setPower(0);
                fl.setPower(diagonalStrafePower);

                br.setPower(-diagonalStrafePower);
                fr.setPower(0);
                // downLeft

            } else if (gamepad1.dpad_up && gamepad1.right_bumper && !hanging) {

                //skibidi
                bl.setPower(0);
                fl.setPower(-diagonalStrafePower);

                br.setPower(diagonalStrafePower);
                fr.setPower(0);
                // upRight

            } else if (gamepad1.dpad_down && gamepad1.right_bumper && !hanging) {

                bl.setPower(diagonalStrafePower);
                fl.setPower(0);

                br.setPower(0);
                fr.setPower(-diagonalStrafePower);
                // downRight

            }

//            if (gamepad2.b) {
//                slide.setTargetPosition(262);
//                slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//                slide.setVelocity(500);
//                sleep(1000);
//                lGripper.setPosition(lGripOpen);
//                rGripper.setPosition(rGripOpen);
//            }

            if (hangStart) {
                lHanger.setTargetPosition(hangMax);
                lHanger.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                lHanger.setVelocity(hangMaxSpeed);
                rHanger.setTargetPosition(hangMax);
                rHanger.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                rHanger.setVelocity(hangMaxSpeed);
            } else if (hanging) {
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

//            arm.setPower(armRaisePower - armLowerPower);

//            if (gamepad2.start) {
//                arm.setPower(0.8);
//            }
            if (gamepad2.right_trigger > 0) {
                arm.setVelocityPIDFCoefficients(10.0, 3.0, 0, 0.31);
                arm.setVelocity(armVelocityScalar * armRaisePower);
            } else if (gamepad2.left_trigger > 0) {
                arm.setVelocityPIDFCoefficients(10.0, 3.0, 0, 0.31);
                arm.setVelocity(-armVelocityScalar * armLowerPower);
            } else {
                arm.setVelocityPIDFCoefficients(10.0, 3.0, 0, 0.31);
                arm.setVelocity(0.85);
            }

            if (gamepad2.right_bumper) {
                slide.setTargetPosition(292);
                slide.setVelocity(800);
            } else if (gamepad2.left_bumper) {
                slide.setTargetPosition(10);
                slide.setVelocity(slideRetract);
            } else {
                slide.setVelocity(0.5);
            }

            if (gamepad2.dpad_down) {
                lGripper.setPosition(lGripClose);
                rGripper.setPosition(rGripClose);
            } else if (gamepad2.dpad_up) {
                lGripper.setPosition(lGripOpen);
                rGripper.setPosition(rGripOpen);
            }

        }
    }
}
