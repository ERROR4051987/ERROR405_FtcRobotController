package org.firstinspires.ftc.teamcode.Auto.oldScripts;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
@Disabled
@Autonomous(name= "Blue_Left", group = "Chance_Auto")

public class Blue_Left extends LinearOpMode {
    private DcMotor Bl = null;
    private DcMotor Fl = null;
    private DcMotor Fr = null;
    private DcMotor Br = null;
    private DcMotor Intake = null;
    private ElapsedTime runtime = new ElapsedTime();

    public void runOpMode() {

        Bl = hardwareMap.get(DcMotor.class, "BackLeftMotor");
        Fl = hardwareMap.get(DcMotor.class, "FrontLeftMotor");
        Fr = hardwareMap.get(DcMotor.class, "FrontRightMotor");
        Br = hardwareMap.get(DcMotor.class, "BackRightMotor");

        Bl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Fl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Fr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Br.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        Bl.setDirection(DcMotor.Direction.REVERSE);
        Br.setDirection(DcMotor.Direction.FORWARD);
        Fr.setDirection(DcMotor.Direction.FORWARD);
        Fl.setDirection(DcMotor.Direction.FORWARD);

        Intake = hardwareMap.get(DcMotor.class, "Intake");


        waitForStart();
        runtime.reset();
        
        while (opModeIsActive()) {

            // Begin Step 1: Deliver yellow pixel.

            drive(0.7, 0.7, 0.08, 0);

            strafe(0.6, -0.6, 0.6, -0.6, 1.7, 0);

            stop(0.7);

            strafe(-0.6, 0.6, -0.6, 0.6, 0.75, 0);

            turnRight(0.2, 0.2, 0.1);

            stop(0.2);

            // End Step 1: Deliver yellow pixel.

            // Begin step 2: deliver purple to center spikemark.

            drive(0.6, 0.6, 0.8, 0);

            stop(1.5);

            drive(0, 0, 0.3, 1.0);

            drive(-0.5, -0.5, 0.35, 1.0);

            drive(-0.5, -0.5, 0.45, 0);

            stop(0.2);

            strafe(-0.6, 0.6, -0.6, 0.6, 1.1, 0);

            stop(0.2);

            turnLeft(0.2, 0.2, 0.05);

            drive(0.7, 0.7, 1.25, 0);

            turnRight(0.6, 0.6, 1.6);

            stop(0.1);


            drive(-0.2, -0.2, 0.13, 0);

            drive(0, 0, 0.5, -1.0);

            drive(-0.05, -0.05, 0.05, -1.0);

            drive(-0.2, -0.2, 0.5, -1.0);

            drive(-0.2, -0.2, 0.2, 0);

            stop(0.1);

            // End Step 2: deliver purple pixel to yellow spikemark.

            turnRight(0.6, 0.6, 0.65);

            drive(0.6, 0.6, 1.4, 0);

            drive(0,0, 1.1, -1.0);

            requestOpModeStop();

        }
    }

    private void drive(double leftSpeed, double rightSpeed, double time, double intakePower) {
        runtime.reset();
        Bl.setPower(leftSpeed);
        Fl.setPower(leftSpeed);
        Fr.setPower(rightSpeed);
        Br.setPower(rightSpeed);

        Intake.setPower(intakePower);

        while ((opModeIsActive() && (runtime.seconds() <= Math.abs(time)))) {
            idle();
        }
    }
    private void strafe(double blSpeed,
                        double flSpeed,
                        double frSpeed,
                        double brSpeed,
                        double time,
                        double intakePower) {

        runtime.reset();
        Bl.setPower(blSpeed);
        Fl.setPower(flSpeed);
        Fr.setPower(frSpeed);
        Br.setPower(brSpeed);

        Intake.setPower(intakePower);


        while (opModeIsActive() && (runtime.seconds() <= Math.abs(time))) {
            idle();
        }
    }
    private void stop(double time) {
        Bl.setPower(0);
        Fl.setPower(0);
        Fr.setPower(0);
        Br.setPower(0);

        Intake.setPower(0);

        while (opModeIsActive() && (runtime.seconds() <= Math.abs(time))) {
            idle();
        } runtime.reset();

    }

    private void turnLeft (double leftSpeed,
                           double rightSpeed,
                           double time) {

        runtime.reset();
        Bl.setPower(-leftSpeed);
        Fl.setPower(-leftSpeed);
        Fr.setPower(rightSpeed);
        Br.setPower(rightSpeed);


        while ((opModeIsActive() && (runtime.seconds() <= Math.abs(time)))) {
            idle();
        }
    }

    private void turnRight (double leftSpeed,
                            double rightSpeed,
                            double time) {

        runtime.reset();
        Bl.setPower(leftSpeed);
        Fl.setPower(leftSpeed);
        Fr.setPower(-rightSpeed);
        Br.setPower(-rightSpeed);


        while ((opModeIsActive() && (runtime.seconds() <= Math.abs(time)))) {
            idle();
        }
    }
}
