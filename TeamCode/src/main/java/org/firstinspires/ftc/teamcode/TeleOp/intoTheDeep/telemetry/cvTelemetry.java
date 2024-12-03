package org.firstinspires.ftc.teamcode.TeleOp.intoTheDeep.telemetry;

import com.acmerobotics.dashboard.config.Config;
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
        final double redThreshold = 100;
        Mat output = new Mat();
        Scalar recColor = new Scalar(255, 0, 0);
        Scalar otherColor = new Scalar(0, 255, 0);
        Scalar coolColor = new Scalar(0, 0, 255);


        public Mat processFrame(Mat input) {
            Imgproc.cvtColor(input, YCbCr, Imgproc.COLOR_RGB2YCrCb);
            telemetry.addLine("pipeline running");


            // Define regions for the 3 borders
            Rect leftRect = new Rect(1, 1, 427, 719);
            Rect centerRect = new Rect(428, 0, 427, 719);
            Rect rightRect = new Rect(850, 1, 427, 719);

            input.copyTo(output);

            // Draw the borders
            Imgproc.rectangle(output, leftRect, recColor, 2);
            Imgproc.rectangle(output, centerRect, otherColor, 2);
            Imgproc.rectangle(output, rightRect, coolColor, 2);

            leftCrop = YCbCr.submat(leftRect);
            centerCrop = YCbCr.submat(centerRect);
            rightCrop = YCbCr.submat(rightRect);

            Core.extractChannel(leftCrop, leftCrop, 2);
            Core.extractChannel(centerCrop, centerCrop, 2);
            Core.extractChannel(rightCrop, rightCrop, 2);

            Scalar leftAvg = Core.mean(leftCrop);
            Scalar centerAvg = Core.mean(centerCrop);
            Scalar rightAvg = Core.mean(rightCrop);

            leftAverage = leftAvg.val[0];
            centerAverage = centerAvg.val[0];
            rightAverage = rightAvg.val[0];

            if (leftAverage > redThreshold) {
                telemetry.addLine("left");
            } else if (centerAverage > redThreshold) {
                telemetry.addLine("center");
            } else if (rightAverage > redThreshold ){
                telemetry.addLine("right");
            }

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
