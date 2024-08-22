package org.firstinspires.ftc.teamcode.Auto.oldScripts;

import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

@Disabled
@Autonomous (name= "Blue_Left_Color", group = "Color_Autonomous")

public class Blue_Left_Color extends LinearOpMode {
    private DcMotor Bl = null;
    private DcMotor Fl = null;
    private DcMotor Fr = null;
    private DcMotor Br = null;
    private DcMotor Intake = null;
    private RevColorSensorV3 color;

    // Init Hardware
    private ElapsedTime runtime = new ElapsedTime();

    boolean moving = true;
    @Override
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

        color = (RevColorSensorV3) hardwareMap.get(ColorSensor.class, "color");
        color.initialize();
        color.enableLed(true);


        if (color.isLightOn()) {
            telemetry.addLine("Color sensor is ready.\n");
            telemetry.update();
        }

        waitForStart();
        runtime.reset();
        if (opModeIsActive()) {
            while (opModeIsActive()) {

                colorTelemetry();



                if (moving) {

                    // Begin step 1: deliver yellow pixel to parking spot.

                    drive(0.3, 0.3, 0.6, 0);

                    strafe(0.6, -0.6, 0.6, -0.6, 1.9, 0);

                    stop(1.5);


                    // End step 1: deliver yellow pixel to parking spot.

                    // Begin step 2: Get into scanning position.

                    strafe(-0.6, 0.6, -0.6, 0.6, 1.1, 0);

                    drive(0.5, 0.5, 0.8, 0);

                    stop(2.0);

                    drive(-0.5, -0.5, 0.5, 0);

                    strafe(-0.5, 0.5, -0.5, 0.5, 0.8, 0);

                    drive(0.5, 0.5, 0.1, 0);

                    drive(0.2, -0.2, 0.5, 0);

                    stop(0.4);

                    // End step 2: Get into scanning position.

                /* Begin step 3: Scan left spikemark and do something depending on whether a pixel
                   is detected or not. */

                /* The "detectPixel()" function takes in location input as a String.
                   First, we will scan left. */
                    detectPixel("LEFT");

                /* End step 3: Scan left spikemark and do something depending on whether a pixel
                   is detected or not. */

                /* Begin Step 4: A pixel was not detected on the left.
                   Now, let's move over to the right spikemark and scan. */

                    drive(-0.4, -0.4, 0.4, 0);

                    strafe(-0.4, 0.4, -0.4, 0.4, 0.4, 0);

                    drive(0.4, 0.4, 0.3, 0);

                    drive(0.4, -0.4, 0.3, 0);

                    stop(0.2);


                /* The "detectPixel()" function takes in location input as a String.
                   Now, we will scan right. */

                    detectPixel("RIGHT");

                /* End Step 4: A pixel was not detected on the left.
                   Now, let's move over to the right spikemark and scan. */

                /* Begin Step 5: No pixel was detected on the right,
                   this must mean that it's on the center spikemark.
                   Let's put the purple pixel on the right spikemark, grab the white pixel, and park. */


                    requestOpModeStop();
                }

                requestOpModeStop();
            }
        }

        requestOpModeStop();
    }
    private void drive(double leftSpeed, double rightSpeed, double time, double intakePower) {
        runtime.reset();
        Bl.setPower(leftSpeed);
        Fl.setPower(leftSpeed);
        Fr.setPower(rightSpeed);
        Br.setPower(rightSpeed);

        Intake.setPower(intakePower);

        while ((opModeIsActive() && (runtime.seconds() <= time))) {
            idle();
        }
    }
    /* Function that allows the programmer to input the left and right drivetrain power, the intake's power,
        a delay before the next call of the function, and how long they want the robot to move. */
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


        while (opModeIsActive() && (runtime.seconds() <= time)) {
            idle();
        }
    }
    /* Function that allows the programmer to input separate power to each motor,
    allowing the robot to strafe. They may also add a delay, power the intake,
    and determine how long they want the robot to move. */
    private void stop(double time) {
        Bl.setPower(0);
        Fl.setPower(0);
        Fr.setPower(0);
        Br.setPower(0);

        Intake.setPower(0);

        while (opModeIsActive() && (runtime.seconds() <= time)) {
            idle();
        } runtime.reset();

    }
    private void detectPixel(String pixelLocation) {

        runtime.reset();

        switch (pixelLocation) {
            case "LEFT":


//                while (opModeIsActive()) {
//                    if (color.red() >= 55 && color.blue() >= 55 && color.green() >= 55) {
//                        telemetry.addData("leftPixel", "TRUE");
//                        telemetry.update();
//
//                        returnSlide();
//                        pixelLeft();
//                        break;
//                    }
//
//                    if (runtime.seconds() == 1.5 && !Slide.isBusy()) {
//                        telemetry.addData("leftPixel", "FALSE");
//                        telemetry.update();
//
//                        returnSlide();
//                        break;
//                    }
//                }

            case "RIGHT":

//                Slide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//                Slide.setPower(0.3);
                sleep(1500);

//                while (opModeIsActive() && Slide.isBusy()) {
//                    if (color.red() >= 55 && color.blue() >= 55 && color.green() >= 55) {
//                        telemetry.addData("rightPixel", "TRUE");
//                        telemetry.update();
//
//                        returnSlide();
//                        pixelRight();
//                        break;
//                    } else if (runtime.seconds() == 1.5 && !Slide.isBusy()) {
//                        telemetry.addData("rightPixel", "FALSE");
//                        telemetry.update();
//
//                        returnSlide();
//                        break;
//                    }
//                }
        }
    }
    private void pixelLeft() {

        /* Begin step 3a: Left spikemark has a pixel.
           let's put the purple pixel on the center spikemark, suck up the white pixel,
           and take it to the parking spot. */

        strafe(-0.4, 0.4, -0.4, 0.4, 0.4, 0);

        drive(0.3, 0.3, 0.4, 0);

        stop(0.2);

        drive(-0.1, -0.1, 0.3, 1.0);

        stop(0.1);

    }
    private void pixelRight() {

        /* Begin step 4a: Right spikemark has a pixel.
           let's put the purple pixel on the center spikemark, suck up the white pixel,
           and take it to the parking spot. */
    }
    private void colorTelemetry() {
        telemetry.addData("redValue", color.red());
        telemetry.addData("greenValue", color.green());
        telemetry.addData("blueValue", color.blue());
        telemetry.update();
    }
}

