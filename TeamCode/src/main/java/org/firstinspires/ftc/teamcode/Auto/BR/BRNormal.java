package org.firstinspires.ftc.teamcode.Auto.BR;

import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous (name= "BRNormal", group= "autoBR", preselectTeleOp = "teleDeep")
public class BRNormal extends LinearOpMode {

    // declare drivetrain motors
    private DcMotorEx bl = null;
    private DcMotorEx br = null;
    private DcMotorEx fl = null;
    private DcMotorEx fr = null;

    // declare secondary motors
    private DcMotorEx wrist = null;
    private DcMotorEx elbow = null;

    // declare servos
    private CRServo intake = null;

    // declare sensors
    private RevColorSensorV3 colorLeft = null;

    // make time move for stuff
    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {

        // init and set up motors
        bl = hardwareMap.get(DcMotorEx.class, "backLeft");
        br = hardwareMap.get(DcMotorEx.class, "backRight");
        fl = hardwareMap.get(DcMotorEx.class, "frontLeft");
        fr = hardwareMap.get(DcMotorEx.class, "frontRight");

        // set all drivetrain motors' encoders to 0
        bl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        br.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        fl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        fr.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        bl.setDirection(DcMotorSimple.Direction.REVERSE);
        fl.setDirection(DcMotorSimple.Direction.REVERSE);

        // init and set up secondary motors
        wrist = hardwareMap.get(DcMotorEx.class, "wrist");
        elbow = hardwareMap.get(DcMotorEx.class, "elbow");

        // set all secondary motors' encoders to 0
        wrist.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        elbow.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // set all secondary motors' mode to brake
        wrist.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        elbow.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // init and set up servos
        intake = hardwareMap.get(CRServo.class, "intake");

        // init and set up sensors
        colorLeft = hardwareMap.get(RevColorSensorV3.class, "colorLeft");

        // position constants
        final int elbowHalf = -1214;
        final int elbowReset = 0;
        final int wristVertical = 500;
        final int wristReset = 0;

        //tps and speed constants
        final double brake = 0;

        waitForStart();
        runtime.reset();

        while(opModeIsActive()) {

            posForward(400, 200);

            posStrafeRight(700, 2000);

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

        while (bl.isBusy() && fl.isBusy() && fr.isBusy() && br.isBusy()){
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

        while (bl.isBusy() && fl.isBusy() && fr.isBusy() && br.isBusy()) {
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

        while (bl.isBusy() && fl.isBusy() && fr.isBusy() && br.isBusy()) {
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

        while (bl.isBusy() && fl.isBusy() && fr.isBusy() && br.isBusy()){
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

        while (bl.isBusy() && fl.isBusy() && fr.isBusy() && br.isBusy()){
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

        while (bl.isBusy() && fl.isBusy() && fr.isBusy() && br.isBusy()){
            idle();
        }

        resetMotorsAndTime();
    }

    private void posElbow (double tps, int pos) {
        elbow.setTargetPosition(pos);
        elbow.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        elbow.setVelocity(tps);
        while (elbow.isBusy()) {
            idle();
        } runtime.reset();
        elbow.setPower(0);
    }

    private void posWrist(double tps, int pos) {
        wrist.setTargetPosition(pos);
        wrist.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        wrist.setVelocity(tps);
        while (wrist.isBusy()) {
            idle();
        } runtime.reset();
        wrist.setPower(0);
    }

    private void posElbowAndWrist(double elbowTps, int elbowPos,
                                  double wristTps, int wristPos) {

        elbow.setTargetPosition(elbowPos);
        elbow.setVelocity(elbowTps);
        wrist.setTargetPosition(wristPos);
        wrist.setVelocity(wristTps);

        while (wrist.isBusy() && elbow.isBusy()) {
            idle();
        } runtime.reset();
        wrist.setPower(0);
        elbow.setPower(0);
    }

    private void sampleSuck() {

        intake.setPower(-1.0);
        while (opModeIsActive() && (runtime.seconds() <= Math.abs(1))) {
            idle();
        } runtime.reset();
    }

    private void sampleSpit() {

        intake.setPower(1.0);
        while (opModeIsActive() && (runtime.seconds() <= Math.abs(3.0))) {
            idle();
        } runtime.reset();
    }

    private void colorScan (String location) {
        switch (location) {
            case "Right":
                if (colorLeft.red() > colorLeft.blue() && colorLeft.red() > colorLeft.green()) {

                }
                break;

            case "Middle":
                if (colorLeft.red() > colorLeft.blue() && colorLeft.red() > colorLeft.green()) {

                }
                break;

            case "Left":
                if (colorLeft.red() > colorLeft.blue() && colorLeft.red() > colorLeft.green()) {

                }
                break;
        }


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

    private void unloadSampleintoBasket (String location) {

    }

    private void putPreloadIntoBasket (int elbowPos, int wristPos) {
        posForward(500,400);
        posStrafeLeft(500, 700);
        posTurnRight(500, 500);
        posElbow(1500, elbowPos);
        posElbowAndWrist(1000, elbowPos * 2, 600, wristPos);
        sampleSpit();
    }

    private void grabNeutralSamples () {
        posWrist(500,0);
        posElbow(200,0);
        posForward(500, 200);
        posTurnLeft(300, 500);
        posForward(300, 100);


    }

    private void hangPreloadSpecimen () {

    }
}
