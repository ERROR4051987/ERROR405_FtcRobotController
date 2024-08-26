package org.firstinspires.ftc.teamcode.Auto.oldScripts;

import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name= "Blue_Left_Pos_1", group = "blueLeft")
@Disabled

public class Blue_Left_Pos_1 extends LinearOpMode {

    private DcMotorEx Bl = null;
    private DcMotorEx Fl = null;
    private DcMotorEx Fr = null;
    private DcMotorEx Br = null;
    private DcMotor Intake = null;
    private DcMotor Arm = null;
    private RevColorSensorV3 color = null;
    private ElapsedTime runtime = new ElapsedTime();
    // test for andres
    @Override
    public void runOpMode() {

        Bl = hardwareMap.get(DcMotorEx.class, "BackLeftMotor");
        Fl = hardwareMap.get(DcMotorEx.class, "FrontLeftMotor");
        Fr = hardwareMap.get(DcMotorEx.class, "FrontRightMotor");
        Br = hardwareMap.get(DcMotorEx.class, "BackRightMotor");

        Arm = hardwareMap.get(DcMotor.class, "Arm");

        Arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        Bl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Fl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Fr.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Br.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        Bl.setDirection(DcMotor.Direction.REVERSE);
        Br.setDirection(DcMotor.Direction.FORWARD);
        Fr.setDirection(DcMotor.Direction.FORWARD);
        Fl.setDirection(DcMotor.Direction.FORWARD);

        Intake = hardwareMap.get(DcMotor.class, "Intake");

        color = hardwareMap.get(RevColorSensorV3.class, "Color");

        color.enableLed(true);

        color.initialize();

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {

            posForward(700, 300);

            posStrafeLeft(500, 350);

            posForward(700, 1000);

            stop(1.0);

            if (color.blue() > color.red() && color.blue() > color.green()) {
                // Blue prop detected on left. time to place purple pixel.

                posStrafeLeft(300, 350);

                intakeRelease();

                posReverse(600, 1500);

                posStrafeLeft(1000, 2000);

                requestOpModeStop();

            } else {
                // Blue prop not detected on left. switching to right spikemark...

                posStrafeRight(800, 300);

                posTurnRight(950, 1200);

                posForward(400, 250);

                stop(1.0);

                if (color.blue() > color.red() && color.blue() > color.green()) {
                    // Blue prop detected on right. time to place purple pixel.

                    posForward(200, 125);

                    intakeRelease();

                    posReverse(800, 1300);

                    posStrafeRight(1100, 950);

                    posReverse(600, 1360);

                    requestOpModeStop();

                } else {

                    posStrafeLeft(500, 250);

                    stop(1.0);

                    if (color.blue() > color.red() && color.blue() > color.green()) {
                        // Blue prop detected on right. time to place purple pixel.

                        posForward(200, 125);

                        intakeRelease();

                        posReverse(800, 1300);

                        posStrafeRight(1100, 1250);

                        posReverse(600, 1360);

                        requestOpModeStop();

                    } else {
                        // Blue prop not on left or right. assuming it's on center...

                        intakeSuck();

                        posReverse(500, 200);

                        posTurnLeft(1200, 1200);

                        intakeRelease();

                        posReverse(900, 1200);

                        stop(0.5);

                        posStrafeLeft(800, 2500);

                        requestOpModeStop();
                    }
                }

                requestOpModeStop();
            }
        }
    }

    private void posForward (double tps, int pos) {

        Bl.setTargetPosition(pos);
        Fl.setTargetPosition(pos);
        Fr.setTargetPosition(pos);
        Br.setTargetPosition(pos);

        Intake.setPower(0);

        Arm.setPower(0);

        Bl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Fl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Fr.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Br.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // tps = Ticks Per Second. (ie. position = 1000, tps = 500, will reach target position in 2s.

        Bl.setVelocity(Math.abs(tps));
        Fl.setVelocity(Math.abs(tps));
        Fr.setVelocity(Math.abs(tps));
        Br.setVelocity(Math.abs(tps));

        while (Bl.isBusy() && Fl.isBusy() && Fr.isBusy() && Br.isBusy()){
            idle();
        }

        Bl.setPower(0);
        Fl.setPower(0);
        Fr.setPower(0);
        Br.setPower(0);

        Bl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Fl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Fr.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Br.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        runtime.reset();
    }

    private void posTurnRight (double tps, int pos) {

        Bl.setTargetPosition(pos);
        Fl.setTargetPosition(pos);
        Fr.setTargetPosition(-pos);
        Br.setTargetPosition(-pos);

        Intake.setPower(0);

        Arm.setPower(0);

        Bl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Fl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Fr.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Br.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // tps = Ticks Per Second. (ie. position = 1000, tps = 500, will reach target position in 2s.

        Bl.setVelocity(Math.abs(tps));
        Fl.setVelocity(Math.abs(tps));
        Fr.setVelocity(Math.abs(tps));
        Br.setVelocity(Math.abs(tps));

        while (Bl.isBusy() && Fl.isBusy() && Fr.isBusy() && Br.isBusy()) {
            idle();
        }

        Bl.setPower(0);
        Fl.setPower(0);
        Fr.setPower(0);
        Br.setPower(0);

        Bl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Fl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Fr.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Br.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        runtime.reset();
    }

    private void posTurnLeft (double tps, int pos) {

        Bl.setTargetPosition(-pos);
        Fl.setTargetPosition(-pos);
        Fr.setTargetPosition(pos);
        Br.setTargetPosition(pos);

        Intake.setPower(0);

        Arm.setPower(0);

        Bl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Fl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Fr.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Br.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // tps = Ticks Per Second. (ie. position = 1000, tps = 500, will reach target position in 2s.

        Bl.setVelocity(Math.abs(tps));
        Fl.setVelocity(Math.abs(tps));
        Fr.setVelocity(Math.abs(tps));
        Br.setVelocity(Math.abs(tps));

        while (Bl.isBusy() && Fl.isBusy() && Fr.isBusy() && Br.isBusy()) {
            idle();
        }

        Bl.setPower(0);
        Fl.setPower(0);
        Fr.setPower(0);
        Br.setPower(0);

        Bl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Fl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Fr.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Br.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        runtime.reset();
    }

    private void posReverse (double tps, int pos) {

        Bl.setTargetPosition(-pos);
        Fl.setTargetPosition(-pos);
        Fr.setTargetPosition(-pos);
        Br.setTargetPosition(-pos);

        Intake.setPower(0);

        Arm.setPower(0);

        Bl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Fl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Fr.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Br.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // tps = Ticks Per Second. (ie. position = 1000, tps = 500, will reach target position in 2s.

        Bl.setVelocity(Math.abs(tps));
        Fl.setVelocity(Math.abs(tps));
        Fr.setVelocity(Math.abs(tps));
        Br.setVelocity(Math.abs(tps));

        while (Bl.isBusy() && Fl.isBusy() && Fr.isBusy() && Br.isBusy()){
            idle();
        }

        Bl.setPower(0);
        Fl.setPower(0);
        Fr.setPower(0);
        Br.setPower(0);

        Bl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Fl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Fr.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Br.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        runtime.reset();
    }
    private void posStrafeLeft (double tps, int pos) {

        Bl.setTargetPosition(pos);
        Fl.setTargetPosition(-pos);
        Fr.setTargetPosition(pos);
        Br.setTargetPosition(-pos);

        Intake.setPower(0);

        Arm.setPower(0);

        Bl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Fl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Fr.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Br.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // tps = Ticks Per Second. (ie. position = 1000, tps = 500, will reach target position in 2s.

        Bl.setVelocity(Math.abs(tps));
        Fl.setVelocity(Math.abs(tps));
        Fr.setVelocity(Math.abs(tps));
        Br.setVelocity(Math.abs(tps));

        while (Bl.isBusy() && Fl.isBusy() && Fr.isBusy() && Br.isBusy()){
            idle();
        }

        Bl.setPower(0);
        Fl.setPower(0);
        Fr.setPower(0);
        Br.setPower(0);

        Bl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Fl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Fr.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Br.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        runtime.reset();
    }

    private void posStrafeRight (double tps, int pos) {

        Bl.setTargetPosition(-pos);
        Fl.setTargetPosition(pos);
        Fr.setTargetPosition(-pos);
        Br.setTargetPosition(pos);

        Intake.setPower(0);

        Arm.setPower(0);

        Bl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Fl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Fr.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Br.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // tps = Ticks Per Second. (ie. position = 1000, tps = 500, will reach target position in 2s.

        Bl.setVelocity(Math.abs(tps));
        Fl.setVelocity(Math.abs(tps));
        Fr.setVelocity(Math.abs(tps));
        Br.setVelocity(Math.abs(tps));

        while (Bl.isBusy() && Fl.isBusy() && Fr.isBusy() && Br.isBusy()){
            idle();
        }

        Bl.setPower(0);
        Fl.setPower(0);
        Fr.setPower(0);
        Br.setPower(0);

        Bl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Fl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Fr.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Br.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        runtime.reset();
    }

    private void stop(double time) {
        Bl.setPower(0);
        Fl.setPower(0);
        Fr.setPower(0);
        Br.setPower(0);

        Arm.setPower(0);

        Intake.setPower(0);

        while (opModeIsActive() && (runtime.seconds() <= Math.abs(time))) {
            idle();
        } runtime.reset();

    }

    private void intakeRelease () {

        Intake.setPower(-1.0);

        while (opModeIsActive() && (runtime.seconds() <= 1.0)) {
            idle();
        } runtime.reset();
    }

    private void intakeSuck () {

        Intake.setPower(1.0);

        while (opModeIsActive() && (runtime.seconds() <= 1.0)) {
            idle();
        } runtime.reset();
    }
}
