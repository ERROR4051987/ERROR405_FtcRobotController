package org.firstinspires.ftc.teamcode.TeleOp.intoTheDeep;

import com.arcrobotics.ftclib.controller.PIDController;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

@TeleOp(name="dhdh", group="intoTheDeepTesting")

public class dumb extends LinearOpMode {

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

    private PIDController wPIDF;

    public static double p = -0.005, i = 0, d = 0.00002;
    public static double f = 0.07;

    public final double ticksInDegree = 1425.1;

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
        wrist.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        wrist.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        elbow.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        elbow.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        elbow.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        wPIDF = new PIDController(p, i, d);

        // declare variables (mutable)
        double leftPower;
        double rightPower;
        double leftStrafe;
        double rightStrafe;
        int wristPower = 0;
        double target = 0;
        double power = 0;
        double poop = 0;
        String twinTowerMode = "unlocked";

        // declare speed constants (immutable)
        final double diagonalStrafePower = 0.7;
        final double intakePower = 1.0;
        final double elbowVel = 1500;
        final double wristVel = 500;
        final int pidWristNormal = 70;
        final int pidWristSlow = 20;

        final double driveTrainScalar = 0.85;
        final double strafeScalar = 0.95;

        int pos;

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

//            if (gamepad2.left_bumper) {
//                elbow.setVelocity(elbowVel);
//            } else if (gamepad2.right_bumper) {
//                elbow.setVelocity(-elbowVel);
//            } else {
//                elbow.setVelocity(0);
//            }

//            if (gamepad2.dpad_up) {
//                wPIDF.setPID(p, i, d);
//                pos = wrist.getCurrentPosition();
//                int target = pos;
//                double pid = wPIDF.calculate(pos, target);
//                double ff = Math.cos(Math.toRadians(target / ticksInDegree)) * f;
//                double power = pid + ff;
//                wrist.setPower(power);
//
//            } else {
////                wrist.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
////                if (gamepad2.left_trigger > 0) {
////                    wrist.setVelocity(wristVel);
////                } else if (gamepad2.right_trigger > 0) {
////                    wrist.setVelocity(-wristVel);
////                } else {
////                    wrist.setPower(0);
////                }
//
//            }
//
//            if (gamepad2.y) {
//                do {
//                    wPIDF.setPID(p, i, d);
//                    pos = wrist.getCurrentPosition();
//                    int target = pos;
//                    double pid = wPIDF.calculate(pos, target);
//                    double ff = Math.cos(Math.toRadians(target / ticksInDegree)) * f;
//                    double power = pid + ff;
//                    wrist.setPower(power);
//
//                } while (gamepad2.y);

            // when p2 'y' is held, pid wrist will move at the slow constant speed
//            if (gamepad2.y) {
//                do {
//                    wPIDF.setPID(p, i, d);
//                    pos = wrist.getCurrentPosition();
//
//                    telemetry.addData("poop", poop);
//                    telemetry.addData("pos", pos);
//                    telemetry.addData("wristPower", wristPower);
//                    telemetry.addData("target", target);
//                    telemetry.addData("pidPower", power);
//                    telemetry.update();
//
//                    if (gamepad2.right_trigger != 0) {
////                        wristPower = 75;
//                        wristPower = 75;
//                    } else if (gamepad2.left_trigger != 0) {
//                        wristPower = -60;
//                    } else {
//                        wristPower = 0;
//                    }
////                    int target = pos + wristPower;
//                    if (wristPower != 0) {
//                        target = wristPower + pos;
//                        double pid = wPIDF.calculate(pos, target);
//                        double ff = Math.cos(Math.toRadians(target / ticksInDegree)) * f;
//                        power = pid + ff;
//                        wrist.setPower(power);
//                    }
//                } while (gamepad2.y);
//            }

            if (gamepad2.y) {
                double wristAdjustment = 0;
                final double SCALE_FACTOR = 20; // Adjust as needed
                final double TRIGGER_DEAD_ZONE = 0.1;

                do {
                    wPIDF.setPID(p, i, d);
                    pos = wrist.getCurrentPosition();

                    double rightTrigger = gamepad2.right_trigger > TRIGGER_DEAD_ZONE ? gamepad2.right_trigger : 0;
                    double leftTrigger = gamepad2.left_trigger > TRIGGER_DEAD_ZONE ? gamepad2.left_trigger : 0;
                    wristAdjustment = (rightTrigger - leftTrigger) * SCALE_FACTOR;

                    target = pos + wristAdjustment; // target is now a double

                    double pid = wPIDF.calculate(pos, target);
                    double ff = Math.cos(Math.toRadians(target / ticksInDegree)) * f;
                    power = pid + ff;
                    wrist.setPower(power);

                } while (gamepad2.y);
            }
            // elbow code



            // intake code
            if (gamepad2.a) {
                intake.setPower(-intakePower);

            } else if (gamepad2.b) {
                intake.setPower(intakePower);

            } else {
                intake.setPower(0);
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
