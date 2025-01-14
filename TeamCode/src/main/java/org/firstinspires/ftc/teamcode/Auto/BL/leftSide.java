package org.firstinspires.ftc.teamcode.Auto.BL;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Config
@Autonomous (name= "leftSide", group= "autoLeft", preselectTeleOp = "teleDeepStable")
public class leftSide extends LinearOpMode {

    // declare drivetrain motors
    private DcMotorEx bl = null;
    private DcMotorEx br = null;
    private DcMotorEx fl = null;
    private DcMotorEx fr = null;

    // declare secondary motors

    private DcMotorEx arm = null;
    private DcMotorEx slide = null;

    // declare servos
    private Servo lGripper = null;
    private Servo rGripper = null;

    // make time move for stuff
    private ElapsedTime runtime = new ElapsedTime();

    public static int square = 965;
    public static int initPos = 965;
    public static int fullTurn = 2300;
    public double armTps = 500;
    public double slideTps = 1500;

    public int armPos = -130;
    public int slidePos = 350;
    public int slideReturnPos = 400;
    public static int forwardPos = 200;
    public static int armGrabPos = -335;
    public static double armGrabTps = 500;
    public static int grabDrive = 2455;

    @Override
    public void runOpMode() {

        // init and set up motors
        bl = hardwareMap.get(DcMotorEx.class, "backLeft");
        br = hardwareMap.get(DcMotorEx.class, "backRight");
        fl = hardwareMap.get(DcMotorEx.class, "frontLeft");
        fr = hardwareMap.get(DcMotorEx.class, "frontRight");

        arm = hardwareMap.get(DcMotorEx.class, "arm");
        slide = hardwareMap.get(DcMotorEx.class, "slide");

        // set all drivetrain motors' encoders to 0
        bl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        br.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        fl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        fr.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        bl.setDirection(DcMotorSimple.Direction.REVERSE);
        fl.setDirection(DcMotorSimple.Direction.REVERSE);

        // init and set up servos
        lGripper = hardwareMap.get(Servo.class, "lGrip");
        rGripper = hardwareMap.get(Servo.class, "rGrip");

        // position constants
        final double lGripClose = 0.5;
        final double lGripOpen = 0.0;
        final double rGripClose = 0.5;
        final double rGripOpen = 1.0;

        waitForStart();

        runtime.reset();

        while(opModeIsActive()) {

            closeGrippers(lGripClose,rGripClose);

            posForward(900, initPos);

            trueStop("REVERSE");

            score(armPos, armTps, slidePos, slideReturnPos, slideTps, lGripOpen, rGripOpen);

            retract(false);

            posStrafeRight(1400, grabDrive);

            trueStop("LEFT");

            stop(0.5);

            posTurnRight(1000, fullTurn);

            stop(0.4);

            posForward(500, forwardPos);

            grab(armGrabPos, armGrabTps, lGripClose, rGripClose);

            retract(true);

            posReverse(500, forwardPos - 250);

            posTurnLeft(800, fullTurn - 150);

            trueStop("TURNRIGHT");

            posStrafeLeft(1000, 2150);

            posTurnLeft(500, 50);

            score(armPos, armTps, slidePos, slideReturnPos, slideTps, lGripOpen, rGripOpen);

            retract(false);

            posStrafeRight(1500, 2400);

            stop(0.2);

            posReverse(1400, 700);

            closeGrippers(lGripClose, rGripClose);

            requestOpModeStop();
        }

    }
    private void posForward (double tps, int pos) {

        // tps = Ticks Per Second. (ie. position = 1000, tps = 500, will reach target position in 2s)
        bl.setTargetPosition(pos);
        br.setTargetPosition(pos);
        fl.setTargetPosition(pos);
        fr.setTargetPosition(pos);

        enablePos();

        bl.setVelocity(Math.abs(tps));
        br.setVelocity(Math.abs(tps));
        fl.setVelocity(Math.abs(tps));
        fr.setVelocity(Math.abs(tps));

        while (bl.isBusy() && fl.isBusy() && fr.isBusy() && br.isBusy() && opModeIsActive()){
            idle();
        }

        resetMotorsAndTime();
    }
    private void posTurnRight (double tps, int pos) {

        bl.setTargetPosition(pos);
        fl.setTargetPosition(pos);
        fr.setTargetPosition(-pos);
        br.setTargetPosition(-pos);

        enablePos();

        bl.setVelocity(Math.abs(tps));
        fl.setVelocity(Math.abs(tps));
        fr.setVelocity(Math.abs(tps));
        br.setVelocity(Math.abs(tps));

        while (bl.isBusy() && fl.isBusy() && fr.isBusy() && br.isBusy() && opModeIsActive()) {
            idle();
        }

        resetMotorsAndTime();
    }

    private void posTurnLeft (double tps, int pos) {

        bl.setTargetPosition(-pos);
        fl.setTargetPosition(-pos);
        fr.setTargetPosition(pos);
        br.setTargetPosition(pos);

        enablePos();

        bl.setVelocity(Math.abs(tps));
        fl.setVelocity(Math.abs(tps));
        fr.setVelocity(Math.abs(tps));
        br.setVelocity(Math.abs(tps));

        while (bl.isBusy() && fl.isBusy() && fr.isBusy() && br.isBusy() && opModeIsActive()) {
            idle();
        }

        resetMotorsAndTime();
    }

    private void posReverse (double tps, int pos) {

        bl.setTargetPosition(-pos);
        fl.setTargetPosition(-pos);
        fr.setTargetPosition(-pos);
        br.setTargetPosition(-pos);

        enablePos();

        bl.setVelocity(Math.abs(tps));
        fl.setVelocity(Math.abs(tps));
        fr.setVelocity(Math.abs(tps));
        br.setVelocity(Math.abs(tps));

        while (bl.isBusy() && fl.isBusy() && fr.isBusy() && br.isBusy() && opModeIsActive()){
            idle();
        }

        resetMotorsAndTime();
    }
    private void posStrafeLeft (double tps, int pos) {

        bl.setTargetPosition(pos);
        fl.setTargetPosition(-pos);
        fr.setTargetPosition(pos);
        br.setTargetPosition(-pos);

        enablePos();

        bl.setVelocity(Math.abs(tps));
        fl.setVelocity(Math.abs(tps));
        fr.setVelocity(Math.abs(tps));
        br.setVelocity(Math.abs(tps));

        while (bl.isBusy() && fl.isBusy() && fr.isBusy() && br.isBusy() && opModeIsActive()){
            idle();
        }

        resetMotorsAndTime();
    }

    private void posStrafeRight (double tps, int pos) {

        bl.setTargetPosition(-pos);
        fl.setTargetPosition(pos);
        fr.setTargetPosition(-pos);
        br.setTargetPosition(pos);

        enablePos();

        bl.setVelocity(Math.abs(tps));
        fl.setVelocity(Math.abs(tps));
        fr.setVelocity(Math.abs(tps));
        br.setVelocity(Math.abs(tps));

        while (bl.isBusy() && fl.isBusy() && fr.isBusy() && br.isBusy() && opModeIsActive()) {
            idle();
        }

        resetMotorsAndTime();

        
    }

    public void retract(boolean grabbed) {
        if (grabbed) {
            arm.setTargetPosition(0);
            arm.setVelocity(400);
            while (opModeIsActive() && arm.isBusy()) {
                idle();
            }
            slide.setTargetPosition(0);
            slide.setVelocity(500);
            while (opModeIsActive() && slide.isBusy()) {
                idle();
            }
        } else {
            lGripper.setPosition(0.44);
            rGripper.setPosition(0.375);
            arm.setTargetPosition(0);
            arm.setVelocity(400);
            while (opModeIsActive() && arm.isBusy()) {
                idle();
            }
            slide.setTargetPosition(0);
            slide.setVelocity(500);
            while (opModeIsActive() && slide.isBusy()) {
                idle();
            }
            lGripper.setPosition(0.0);
            rGripper.setPosition(1.0);
            runtime.reset();
        }
    }
    public void score(int armPos, double armTps, int slidePos, int slideReturnPos, double slideTps, double lOpen, double rOpen) {
        slide.setTargetPosition(slidePos);
        slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slide.setVelocity(slideTps);
        arm.setTargetPosition(armPos);
        arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        arm.setVelocity(armTps);
        while (opModeIsActive() && slide.isBusy() && arm.isBusy()) {
            idle();
        }
//        sleep(1500);
        slide.setTargetPosition(177);
        slide.setVelocity(460);
        while (opModeIsActive() && slide.isBusy()) {
            idle();
        }
//        sleep(1300);
        lGripper.setPosition(lOpen);
        rGripper.setPosition(rOpen);
        sleep(1000);
        runtime.reset();
    }

    private void grab (int armGrabPos, double armGrabTps, double lGrip, double rGrip) {
        arm.setTargetPosition(armGrabPos);
        arm.setVelocity(armGrabTps);
        while (arm.isBusy() && opModeIsActive()) {
            idle();
        }
        lGripper.setPosition(lGrip);
        rGripper.setPosition(rGrip);
        sleep(1000);
        runtime.reset();
    }

    private void stop(double time) {

        bl.setPower(0);
        fl.setPower(0);
        fr.setPower(0);
        br.setPower(0);

        while (opModeIsActive() && (runtime.seconds() <= Math.abs(time))) {
            idle();
        } runtime.reset();

    }

    public void trueStop (String mode) {
        switch (mode) {
            case "REVERSE":
                bl.setPower(-0.15);
                fl.setPower(-0.15);
                br.setPower(-0.15);
                fr.setPower(-0.15);

                while ((opModeIsActive() && (runtime.seconds() <= 0.2))) {
                    idle();
                }
                runtime.reset();
                break;

            case "LEFT":
                bl.setPower(0.25);
                fl.setPower(-0.25);
                fr.setPower(0.25);
                br.setPower(-0.25);
                while ((opModeIsActive() && (runtime.seconds() <= 0.15))) {
                    idle();
                }
                break;

            case "TURNRIGHT":
                bl.setPower(0.1);
                fl.setPower(0.1);
                fr.setPower(-0.1);
                br.setPower(-0.1);

                while ((opModeIsActive() && (runtime.seconds() <= 0.2))) {
                    idle();
                }
        }
    }

    private void resetMotorsAndTime() {

        bl.setPower(0);
        fl.setPower(0);
        fr.setPower(0);
        br.setPower(0);

        bl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        fl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        fr.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        br.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        runtime.reset();
    }

    private void enablePos() {

        bl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        br.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        fl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        fr.setMode(DcMotor.RunMode.RUN_TO_POSITION);

    }

    private void closeGrippers (double lGripPos, double rGripPos) {
        lGripper.setPosition(lGripPos);
        rGripper.setPosition(rGripPos);
    }

}
