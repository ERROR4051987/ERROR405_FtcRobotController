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
    private DcMotorEx wrist = null;
    private DcMotorEx elbow = null;

    private CRServo lGrip = null;
    private CRServo rGrip = null;

    // declare sensors
    private RevColorSensorV3 color = null;

    private PIDController wPIDF;
    private PIDController ePIDF;

    public static double wP = -0.005, wI = 0, wD = 0.00002;
    public static double wF = 0.07;

    // elbow pid coefficients
    public static double eP = 0.01, eI = 0, eD = 0.00001;
    public static double eF = 0.02;

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
        lGrip = hardwareMap.get(CRServo.class, "lGrip");
        rGrip = hardwareMap.get(CRServo.class, "rGrip");

        color = hardwareMap.get(RevColorSensorV3.class, "colorLeft");

        // change properties of the wrist & elbow motor
        wrist.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        wrist.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        elbow.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        elbow.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        elbow.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        wPIDF = new PIDController(wP, wI, wD);
        ePIDF = new PIDController(eP, eI, eD);

        // declare variables (mutable)
        double leftPower;
        double rightPower;
        double leftStrafe;
        double rightStrafe;
        int wristPower = 0;
        int elbowPower = 0;
        double wristTarget = 0;
        double elbowTarget = 0;
        double wristPidPower = 0;
        double elbowPidPower = 0;
        double poop = 0;
        String twinTowerMode = "unlocked";

        // declare speed constants (immutable)
        final double diagonalStrafePower = 0.7;
        final double strafeScalar = 0.95;

        final double elbowVel = 1000;
        final double wristVel = 0.3;

        final int pidWristSlow = 15;
        final int pidWristNormal = 40;
        final int pidWristFast = 80;

        final int pidElbowSlow = 15;
        final int pidElbowFast = 80;

        final double driveTrainScalar = 0.85;

        int elbowPos;

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

            lGrip.setPower(gamepad2.left_stick_y);
            rGrip.setPower(-gamepad2.right_stick_y);


            if (gamepad2.right_bumper) {
                elbow.setVelocity(elbowVel);
            } else if (gamepad2.left_bumper) {
                elbow.setVelocity(-elbowVel);
            } else {
                elbow.setPower(0);
            }

            if (gamepad2.right_trigger > 0) {
                wrist.setPower(-wristVel);
            } else if (gamepad2.left_trigger > 0) {
                wrist.setPower(wristVel);
            } else {
                wrist.setPower(0);

            }
        }

    }
}


