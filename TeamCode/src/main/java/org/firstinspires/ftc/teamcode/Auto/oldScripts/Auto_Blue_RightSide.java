package org.firstinspires.ftc.teamcode.Auto.oldScripts;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous (name= "Auto_Blue_RightSide", group = "Autonomous")
@Disabled

public class Auto_Blue_RightSide extends LinearOpMode {

    private DcMotor Bl = null;
    private DcMotor Fl = null;
    private DcMotor Fr = null;
    private DcMotor Br = null;
    private DcMotor Intake = null;

    private ElapsedTime     runtime = new ElapsedTime();

    @Override
    public void runOpMode() {

        Bl  = hardwareMap.get(DcMotor.class, "BackLeftMotor");
        Fl  = hardwareMap.get(DcMotor.class, "FrontLeftMotor");
        Fr  = hardwareMap.get(DcMotor.class, "FrontRightMotor");
        Br  = hardwareMap.get(DcMotor.class, "BackRightMotor");
        Intake  = hardwareMap.get(DcMotor.class, "Intake");

        Bl.setDirection(DcMotor.Direction.REVERSE);
        Br.setDirection(DcMotor.Direction.FORWARD);
        Fr.setDirection(DcMotor.Direction.FORWARD);
        Fl.setDirection(DcMotor.Direction.FORWARD);

        boolean drive_Ready = true;

        waitForStart();
        runtime.reset();
        while (opModeIsActive()) {
            boolean webcam_Ready = false;
            boolean targetFound = false;
            boolean targetLeft = false;
            boolean targetCenter = false;
            boolean targetRight = false;
            //Boolean variables that are true or false by what the webcam sees.


            if (drive_Ready == true) {
                //          Bl    Fl   Fr    Br  Time  Intake
                stop(15.0);

                strafe(-0.3, 0.3, -0.3, 0.3, 0.3, 0);
                //       left right time intake

                drive(-0.2, 0.2, 0.05, 0);

                drive(0.6, 0.6, 1.9, 0);
                //          Bl    Fl   Fr    Br  Time  Intake
                //       left right time intake
                drive(0.6, 0.6, 1.45, 0);
                //     left right time intake
                drive(0,  0,   0.8,  -0.7);
                //       left right time intake
                drive(0, 0, 0, 0);
                drive_Ready = false;

                if (webcam_Ready == true && targetFound == true) {

                    if (targetFound == true && targetLeft == true) {

                    } else if (targetFound == true && targetCenter == true) {

                    } else if (targetFound == true && targetRight == true) {

                    } }
            }}}
    private void drive(double leftSpeed, double rightSpeed, double time, double intakePower) {
        Bl.setPower(leftSpeed);
        Fl.setPower(leftSpeed);
        Fr.setPower(rightSpeed);
        Br.setPower(rightSpeed);

        Intake.setPower(intakePower);

        while ((opModeIsActive() && (runtime.seconds() <= time))) {
            idle();
        } runtime.reset();

    } /* Function that allows the programmer to input the left and right drivetrain power, the intake's power,
        a delay before the next call of the function, and how long they want the robot to move.*/
    public void strafe(double blSpeed, double flSpeed, double frSpeed, double brSpeed, double time, double intakePower) {
        Bl.setPower(blSpeed);
        Fl.setPower(flSpeed);
        Fr.setPower(frSpeed);
        Br.setPower(brSpeed);

        Intake.setPower(intakePower);

        while (opModeIsActive() && (runtime.seconds() <= time)) {
            idle();
        } runtime.reset();
    } // Function that allows the programmer to input separate power to each motor, allowing the robot to strafe. They may also add a delay, power the intake, and determine how long they want the robot to move.

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
}
