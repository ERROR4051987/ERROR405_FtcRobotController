package org.firstinspires.ftc.teamcode.Auto.oldScripts;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.tfod.TfodProcessor;

import java.util.List;
@Disabled
@Autonomous(name= "Webcam_Testing", group = "Webcam_Autonomous")

public class Webcam_Testing extends LinearOpMode {

    private DcMotor Bl = null;
    private DcMotor Fl = null;
    private DcMotor Fr = null;
    private DcMotor Br = null;
    private DcMotor Intake = null;
    private ElapsedTime runtime = new ElapsedTime();
    private static final boolean USE_WEBCAM = true;
    private TfodProcessor tfod;
    private VisionPortal visionPortal;
    private boolean pixelDetected = false;
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

        initTfod();

        telemetry.addData("DS preview on/off", "3 dots, Camera Stream");
        telemetry.addData(">", "Touch Play to start OpMode");
        telemetry.update();

        waitForStart();
        runtime.reset();

        while (opModeIsActive() && !pixelDetected) {

            telemetryTfod();

            // Push telemetry to the Driver Station.
            telemetry.update();

            drive(0.2, 0.2, 0.2, 0);

            if (tfod != null) {
                List<Recognition> currentRecognitions = tfod.getRecognitions();
                telemetry.addData("# Objects Detected", currentRecognitions.size());

                // Step through the list of recognitions and display info for each one.
                for (Recognition recognition : currentRecognitions) {
                    double x = (recognition.getLeft() + recognition.getRight()) / 2;
                    double y = (recognition.getTop() + recognition.getBottom()) / 2;

                    telemetry.addData("", " ");
                    telemetry.addData("Image", "%s (%.0f %% Conf.)", recognition.getLabel(), recognition.getConfidence() * 100);
                    telemetry.addData("- Position", "%.0f / %.0f", x, y);
                    telemetry.addData("- Size", "%.0f x %.0f", recognition.getWidth(), recognition.getHeight());
                    boolean pixelDetected = true;
                }   // end for() loop
            } else {
                boolean pixelDetected = false;
            }

            if (pixelDetected) {

            }
        }
    }
    private void initTfod() {

        // Create the TensorFlow processor the easy way.
        tfod = TfodProcessor.easyCreateWithDefaults();

        // Create the vision portal the easy way.
        if (USE_WEBCAM) {
            visionPortal = VisionPortal.easyCreateWithDefaults(
                    hardwareMap.get(WebcamName.class, "Webcam"), tfod);
            tfod.setMinResultConfidence((float) 0.50);
        } else {
            visionPortal = VisionPortal.easyCreateWithDefaults(
                    BuiltinCameraDirection.BACK, tfod);
            tfod.setMinResultConfidence((float) 0.50);
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
    private void drive(double leftSpeed, double rightSpeed, double time, double intakePower) {
        Bl.setPower(leftSpeed);
        Fl.setPower(leftSpeed);
        Fr.setPower(rightSpeed);
        Br.setPower(rightSpeed);

        Intake.setPower(intakePower);

        while ((opModeIsActive() && (runtime.seconds() <= time))) {
            idle();
        } runtime.reset();
        return;
    }
}