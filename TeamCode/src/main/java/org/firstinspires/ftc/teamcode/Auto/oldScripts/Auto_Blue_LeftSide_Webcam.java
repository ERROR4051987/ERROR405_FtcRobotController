package org.firstinspires.ftc.teamcode.Auto.oldScripts;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.tfod.TfodProcessor;

import java.util.List;

@Disabled
@Autonomous (name= "Auto_Blue_LeftSide_Webcam", group = "Webcam_Autonomous")

public class Auto_Blue_LeftSide_Webcam extends LinearOpMode {
    private DcMotor Bl = null;
    private DcMotor Fl = null;
    private DcMotor Fr = null;
    private DcMotor Br = null;
    private DcMotor Intake = null;
    private ElapsedTime runtime = new ElapsedTime();
    private static final boolean USE_WEBCAM = true;
    private TfodProcessor tfod;
    private VisionPortal visionPortal;
    private String scanLocation = "";
    private boolean isPixelDetected = false;

    @Override
    public void runOpMode() {

        Bl = hardwareMap.get(DcMotor.class, "BackLeftMotor");
        Fl = hardwareMap.get(DcMotor.class, "FrontLeftMotor");
        Fr = hardwareMap.get(DcMotor.class, "FrontRightMotor");
        Br = hardwareMap.get(DcMotor.class, "BackRightMotor");
        Intake = hardwareMap.get(DcMotor.class, "Intake");

        Bl.setDirection(DcMotor.Direction.REVERSE);
        Br.setDirection(DcMotor.Direction.FORWARD);
        Fr.setDirection(DcMotor.Direction.FORWARD);
        Fl.setDirection(DcMotor.Direction.FORWARD);

        boolean moving = true;

        initTfod();

        telemetry.addData("DS preview on/off", "3 dots, Camera Stream");
        telemetry.addData(">", "Touch Play to start OpMode");
        telemetry.update();

        waitForStart();
        runtime.reset();
        if (opModeIsActive()) {
        while (opModeIsActive()) {

            telemetryTfod();

            // Push telemetry to the Driver Station.
            telemetry.update();

            if (moving) {

                // Begin step 1: deliver yellow pixel to parking spot.

                drive(0.3, 0.3, 0.6, 0);

                strafe(0.6, -0.6, 0.6, -0.6, 1.9, 0);

                stop(1.5);

                // End step 1: deliver yellow pixel to parking spot.

                // Begin step 2: Get into scanning position.

                strafe(-0.6, 0.6, -0.6, 0.6, 1.1, 0);

                drive(0.5, 0.5, 0.8, 0);

                stop(1.5);

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

                stop(0.1);


                /* The "detectPixel()" function takes in location input as a String.
                   Now, we will scan right. */

                detectPixel("RIGHT");

                /* End Step 4: A pixel was not detected on the left.
                   Now, let's move over to the right spikemark and scan. */

                /* Begin Step 5: No pixel was detected on the right,
                   this must mean that it's on the center spikemark.
                   Let's put the purple pixel on the right spikemark, grab the white pixel, and park. */

                visionPortal.close();
                requestOpModeStop();
            }
                visionPortal.close();
                requestOpModeStop();
            }
        }
        visionPortal.close();
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
        return;
    }
    private void initTfod() {

        // Create the TensorFlow processor the easy way.
        tfod = TfodProcessor.easyCreateWithDefaults();

        // Create the vision portal the easy way.
        if (USE_WEBCAM) {
            visionPortal = VisionPortal.easyCreateWithDefaults(
                    hardwareMap.get(WebcamName.class, "Webcam"), tfod);
            tfod.setMinResultConfidence((float) 0.60);

        } else {
            visionPortal = VisionPortal.easyCreateWithDefaults(
                    BuiltinCameraDirection.BACK, tfod);
            tfod.setMinResultConfidence((float) 0.60);
        }

    }
    private void telemetryTfod() {

        List<Recognition> currentRecognitions = tfod.getRecognitions();
        telemetry.addData("# Objects Detected", currentRecognitions.size());

        // Step through the list of recognitions and display info for each one.
        for (Recognition recognition : currentRecognitions) {
            double x = (recognition.getLeft() + recognition.getRight()) / 2 ;
            double y = (recognition.getTop()  + recognition.getBottom()) / 2 ;

            telemetry.addData(""," ");
            telemetry.addData("Image", "%s (%.0f %% Conf.)", recognition.getLabel(), recognition.getConfidence() * 100);
            telemetry.addData("- Position", "%.0f / %.0f", x, y);
            telemetry.addData("- Size", "%.0f x %.0f", recognition.getWidth(), recognition.getHeight());
        }   // end for() loop

    }

    private void detectPixel(String pixelLocation) {

        switch (pixelLocation) {

            case "LEFT":

            if (tfod != null) {
                List<Recognition> currentRecognitions = tfod.getRecognitions();
                sleep(3000);

                if (currentRecognitions != null) {
                    telemetry.addData("# Objects Detected", currentRecognitions.size());

                    for (Recognition recognition : currentRecognitions) {
                        double x = (recognition.getLeft() + recognition.getRight()) / 2;
                        double y = (recognition.getTop() + recognition.getBottom()) / 2;

                        telemetry.addData("", " ");
                        telemetry.addData("Image", "%s (%.0f %% Conf.)", recognition.getLabel(), recognition.getConfidence() * 100);
                        telemetry.addData("- Position", "%.0f / %.0f", x, y);
                        telemetry.addData("- Size", "%.0f x %.0f", recognition.getWidth(), recognition.getHeight());
                        pixelLeft();
                    }
                }
            }
            break;

            case "RIGHT":

                if (tfod != null) {
                    List<Recognition> currentRecognitions = tfod.getRecognitions();
                    sleep(3000);

                    if (currentRecognitions != null) {
                        telemetry.addData("# Objects Detected", currentRecognitions.size());

                        for (Recognition recognition : currentRecognitions) {
                            double x = (recognition.getLeft() + recognition.getRight()) / 2;
                            double y = (recognition.getTop() + recognition.getBottom()) / 2;

                            telemetry.addData("", " ");
                            telemetry.addData("Image", "%s (%.0f %% Conf.)", recognition.getLabel(), recognition.getConfidence() * 100);
                            telemetry.addData("- Position", "%.0f / %.0f", x, y);
                            telemetry.addData("- Size", "%.0f x %.0f", recognition.getWidth(), recognition.getHeight());
                            pixelRight();
                        }
                    }
                }
                break;
        }
    }
    private void pixelLeft() {

        /* Begin step 3a: Left spikemark has a pixel.
           let's put the purple pixel on the center spikemark, suck up the white pixel,
           and take it to the parking spot. */

        drive(0.5, -0.5, 0.5, 0.5);

        drive(0.3, -0.3, 0.3, 0);

        strafe(-0.3, 0.3, -0.3, 0.3, 0.8, 0);

        drive(0.3, -0.3, 0.2, 0);

        stop(0.4);

    }

    private void pixelRight() {

        /* Begin step 4a: Right spikemark has a pixel.
           let's put the purple pixel on the center spikemark, suck up the white pixel,
           and take it to the parking spot. */
    }
}


