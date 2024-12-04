package org.firstinspires.ftc.teamcode.TeleOp.intoTheDeep.telemetry;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;
import org.openftc.easyopencv.OpenCvWebcam;
@Disabled
@Config
@TeleOp(name= "cvTelemetry", group="testing")
public class cvTelemetry extends OpMode {
    private OpenCvWebcam camera;

    @Override
    public void init() {
        WebcamName webcamName = hardwareMap.get(WebcamName.class, "camera");
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        camera = OpenCvCameraFactory.getInstance().createWebcam(webcamName, cameraMonitorViewId);

        camera.setPipeline(new specimenDetect());
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                camera.startStreaming(1280, 720, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {

            }
        });
    }

    @Override
    public void loop() {

    }
    class specimenDetect extends OpenCvPipeline {
        Mat YCbCr = new Mat();
        Mat leftCrop;
        Mat centerCrop;
        Mat rightCrop;
        double leftAverage;
        double centerAverage;
        double rightAverage;
        final double colorThreshold = 120;
        Mat output = new Mat();
        Scalar leftColor = new Scalar(255, 0, 0);
        Scalar centerColor = new Scalar(0, 255, 0);
        Scalar rightColor = new Scalar(0, 0, 255);


        public Mat processFrame(Mat input) {
            Imgproc.cvtColor(input, YCbCr, Imgproc.COLOR_RGB2YCrCb);
            telemetry.addLine("pipeline running");


            // Define regions for the 3 borders
            Rect rightRect = new Rect(1, 1, 427, 719);
            Rect centerRect = new Rect(428, 0, 427, 719);
            Rect leftRect = new Rect(850, 1, 427, 719);

            input.copyTo(output);

            // Draw the borders
            Imgproc.rectangle(output, rightRect, leftColor, 2);
            Imgproc.rectangle(output, centerRect, centerColor, 2);
            Imgproc.rectangle(output, leftRect, rightColor, 2);

            rightCrop = YCbCr.submat(rightRect);
            centerCrop = YCbCr.submat(centerRect);
            leftCrop = YCbCr.submat(leftRect);

            Core.extractChannel(leftCrop, leftCrop, 2);
            Core.extractChannel(centerCrop, centerCrop, 2);
            Core.extractChannel(rightCrop, rightCrop, 2);

            Scalar rightAvg = Core.mean(rightCrop);
            Scalar centerAvg = Core.mean(centerCrop);
            Scalar leftAvg = Core.mean(leftCrop);

            rightAverage = rightAvg.val[0];
            centerAverage = centerAvg.val[0];
            leftAverage = leftAvg.val[0];

            if (rightAverage > colorThreshold && rightAverage > centerAverage && rightAverage > leftAverage) {
                // right has the most red
                // assume that the red block is on the right
                telemetry.addLine("detected on right");
                telemetry.addData("leftColor", leftAverage);
                telemetry.addData("centerColor", centerAverage);
                telemetry.addData("rightColor", rightAverage);
                telemetry.update();
            } else if (centerAverage > colorThreshold && centerAverage > rightAverage && centerAverage > leftAverage) {
                // center has the most red
                // assume that the red block is in the center
                telemetry.addLine("detected on center");
                telemetry.addData("leftColor", leftAverage);
                telemetry.addData("centerColor", centerAverage);
                telemetry.addData("rightColor", rightAverage);
                telemetry.update();
            } else if (leftAverage > colorThreshold && leftAverage > rightAverage && leftAverage > centerAverage) {
                // left has the most red
                // assume that the red block is on the left
                telemetry.addLine("detected on left");
                telemetry.addData("leftColor", leftAverage);
                telemetry.addData("centerColor", centerAverage);
                telemetry.addData("rightColor", rightAverage);
                telemetry.update();
            }
//            if ((rightAverage > colorThreshold) || (leftAverage > colorThreshold)) {
//                if (rightAverage > colorThreshold) {
//                    telemetry.addLine("right");
//                    telemetry.addData("leftColor", leftAverage);
//                    telemetry.addData("centerColor", centerAverage);
//                    telemetry.addData("rightColor", rightAverage);
//                    telemetry.update();
//                } else {
//                    telemetry.addLine("left");
//                    telemetry.addData("leftColor", leftAverage);
//                    telemetry.addData("centerColor", centerAverage);
//                    telemetry.addData("rightColor", rightAverage);
//                    telemetry.update();
//                }
//            } else {
//                telemetry.addLine("center");
//                telemetry.addData("leftColor", leftAverage);
//                telemetry.addData("centerColor", centerAverage);
//                telemetry.addData("rightColor", rightAverage);
//                telemetry.update();
//            }

            return (output);
        }
    }
//    class specimenDetect extends OpenCvPipeline {
//        Mat YCbCr = new Mat();
//        Mat leftCrop;
//        Mat rightCrop;
//        double leftAverage;
//        double rightAverage;
//        Mat output = new Mat();
//        Scalar recColor = new Scalar(255, 0, 0);
//
//        public Mat processFrame(Mat input) {
//
//            Imgproc.cvtColor(input, YCbCr, Imgproc.COLOR_RGB2YCrCb);
//            telemetry.addLine("pipeline running");
//
//            Rect leftRect = new Rect(1, 1, 639, 719);
//            Rect rightRect = new Rect(320, 1, 639, 719);
//
//            input.copyTo(output);
//            Imgproc.rectangle(output, leftRect, recColor, 2);
//            Imgproc.rectangle(output, rightRect, recColor, 2);
//
//            leftCrop = YCbCr.submat(leftRect);
//            rightCrop = YCbCr.submat(rightRect);
//
//            Core.extractChannel(leftCrop, leftCrop, 2);
//            Core.extractChannel(rightCrop, rightCrop, 2);
//
//            Scalar leftAvg = Core.mean(leftCrop);
//            Scalar rightAvg = Core.mean(rightCrop);
//
//            leftAverage = leftAvg.val[0];
//            rightAverage = rightAvg.val[0];
//
//            if (leftAverage > rightAverage) {
//                telemetry.addLine("left");
//            } else {
//                telemetry.addLine("right");
//            }
//
//            return (output);
//        }
//    }
}
