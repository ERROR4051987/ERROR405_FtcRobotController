package org.firstinspires.ftc.teamcode.TeleOp.oldScripts;

import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior;

@Disabled
@TeleOp (name="TeleOp_Tank", group="old_teleOP")

public class TeleOp_Tank extends LinearOpMode {

    private DcMotor Bl = null;
    private DcMotor Br = null;
    private DcMotor Fl = null;
    private DcMotor Fr = null;
    private DcMotor Arm = null;
    private DcMotorEx Slide = null;
    private DcMotorEx PAL = null;
    private DcMotor Intake = null;
    private RevColorSensorV3 color = null;
    private RevColorSensorV3 color2 = null;
    @Override
    public void runOpMode() {

        // Init and set up motors.
        Bl = hardwareMap.get(DcMotor.class, "BackLeftMotor");
        Br = hardwareMap.get(DcMotor.class, "BackRightMotor");
        Fl = hardwareMap.get(DcMotor.class, "FrontLeftMotor");
        Fr = hardwareMap.get(DcMotor.class, "FrontRightMotor");
        Arm = hardwareMap.get(DcMotor.class, "Arm");
        Slide = hardwareMap.get(DcMotorEx.class, "Slide");
        PAL = hardwareMap.get(DcMotorEx.class, "PAL");
        Intake = hardwareMap.get(DcMotor.class, "Intake");

        color2 = hardwareMap.get(RevColorSensorV3.class, "Color2");
        color = hardwareMap.get(RevColorSensorV3.class, "Color");

        Arm.setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);
        Slide.setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);

        Bl.setDirection(DcMotor.Direction.FORWARD);
        Br.setDirection(DcMotor.Direction.FORWARD);
        Fr.setDirection(DcMotor.Direction.FORWARD);
        Fl.setDirection(DcMotor.Direction.REVERSE);

        Slide.setDirection(DcMotor.Direction.REVERSE);
        Slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Slide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        PAL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Create speed variables.

        final double diagonalStrafePower = 1.0;
        final double PAL_Velocity = -1450;
        final double slidePower = 0.6;
        final double intakePower = 0.5;

        waitForStart();

        while (opModeIsActive()) {

            color2.enableLed(false);
            color.enableLed(false);

            float rightpower  = gamepad1.right_stick_y;
            float leftpower = -gamepad1.left_stick_y;
            float leftstrafe = gamepad1.left_trigger;
            float rightstrafe = gamepad1.right_trigger;

            boolean Hang_Locked = gamepad2.x && gamepad2.a;

            Bl.setPower(-leftpower);
            Fl.setPower(-leftpower);

            Br.setPower(-rightpower);
            Fr.setPower(-rightpower);


            if (gamepad1.left_trigger > 0) {

                Bl.setPower(-leftstrafe);
                Fl.setPower(leftstrafe);

                Br.setPower(-leftstrafe);
                Fr.setPower(leftstrafe);

            } else if (gamepad1.right_trigger > 0) {

                Bl.setPower(rightstrafe);
                Fl.setPower(-rightstrafe);

                Br.setPower(rightstrafe);
                Fr.setPower(-rightstrafe);
            }

            if (gamepad1.dpad_up && gamepad1.left_bumper) {

                Bl.setPower(-diagonalStrafePower);
                Fl.setPower(0);

                Br.setPower(0);
                Fr.setPower(diagonalStrafePower);
                // Makes the Robot strafe UpLeft.

            } else if (gamepad1.dpad_down && gamepad1.left_bumper) {

                Bl.setPower(0);
                Fl.setPower(diagonalStrafePower);

                Br.setPower(-diagonalStrafePower);
                Fr.setPower(0);
                // Makes the Robot strafe DownLeft.

            } else if (gamepad1.dpad_up && gamepad1.right_bumper) {


                Bl.setPower(0);
                Fl.setPower(-diagonalStrafePower);

                Br.setPower(diagonalStrafePower);
                Fr.setPower(0);
                // Makes the Robot strafe UpRight.

            } else if (gamepad1.dpad_down && gamepad1.right_bumper) {

                Bl.setPower(diagonalStrafePower);
                Fl.setPower(0);

                Br.setPower(0);
                Fr.setPower(-diagonalStrafePower);

                // Makes the Robot strafe DownRight.

            }

            if (gamepad2.right_trigger > 0) {

                Arm.setPower(0.3);

            } else if (gamepad2.left_trigger > 0) {

                Arm.setPower(-0.4);

            } else {

                Arm.setPower(0);
            }

            /*Player 2's Left trigger brings the arm down,
            Right trigger raises the arm*/

            if (gamepad2.y) {

                PAL.setVelocity(PAL_Velocity);

            } else {

                PAL.setPower(0);
            }

            //When Player 2 holds "Y", The Paper Airplane Launcher spins.

            if (gamepad2.right_bumper) {

                Slide.setPower(slidePower);

            } else if (gamepad2.left_bumper) {

                Slide.setPower(-slidePower);

            } else if (Hang_Locked) {

                Slide.setTargetPosition(100);
                Slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                Slide.setVelocity(1900);
                sleep(30000);

            } else {

                Slide.setPower(0);
            }

            /*When Player 2 holds the right bumper, the slide will extend,
              holding left bumper will bring the slide back.
              They can press A & X to lock the slide in place.*/

            if(gamepad2.dpad_up) {

                Intake.setPower(intakePower);

            } else if (gamepad2.dpad_down) {

                Intake.setPower(-intakePower);

            } else {

                Intake.setPower(0);
            }
        }
    }
}