package org.firstinspires.ftc.teamcode.TeleOp.intoTheDeep;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="teleDeep", group="intoTheDeep")

public class teleDeep extends LinearOpMode {

    // declare drivetrain motors
    private DcMotor bl = null;
    private DcMotor br = null;
    private DcMotor fl = null;
    private DcMotor fr = null;

    private CRServo intake = null;



    // declare secondary motors
    private DcMotorEx wrist = null;


    @Override
    public void runOpMode() {

        // init and set up motors
        bl = hardwareMap.get(DcMotor.class, "backLeft");
        br = hardwareMap.get(DcMotor.class, "backRight");
        fl = hardwareMap.get(DcMotor.class, "frontLeft");
        fr = hardwareMap.get(DcMotor.class, "frontRight");
        wrist = hardwareMap.get(DcMotorEx.class, "wrist");
        intake = hardwareMap.get(CRServo.class, "intake");

        // change properties of the wrist motor
        wrist.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        wrist.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        wrist.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // declare variables
        double leftPower;
        double rightPower;
        double leftStrafe;
        double rightStrafe;
        double intakePower;
        double lWristPower;
        double rWristPower;
        String mode = "unlocked";

        // declare speed constants
        final double diagonalStrafePower = 1.0;

        waitForStart();

        while (opModeIsActive()) {

            // these speed variables are mutabable
            leftPower = gamepad1.left_stick_y;
            rightPower = -gamepad1.right_stick_y;
            leftStrafe = gamepad1.left_trigger;
            rightStrafe = gamepad1.right_trigger;
            rWristPower = gamepad2.right_trigger;
            lWristPower = -gamepad2.left_trigger;

            bl.setPower(leftPower);
            fl.setPower(leftPower);

            br.setPower(rightPower);
            fr.setPower(rightPower);

            switch (mode) {

                case "unlocked":
                    wrist.setPower(rWristPower);
                    wrist.setPower(lWristPower);
                    break;

                case "locked":
                    int pos = wrist.getCurrentPosition();
                    wrist.setTargetPosition(pos);
                    wrist.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    wrist.setVelocity(1000);
                    while (true) {
                        sleep(30000);
                    }

            }


            if (gamepad2.dpad_left) {
                intakePower = -1.0;

            } else if (gamepad2.dpad_right) {
                intakePower = 1.0;

            } else {
                intakePower = 0;
            }

            if (gamepad2.dpad_up) {
                do {
                    mode = "locked";
                } while (gamepad2.dpad_up);

            } else if (gamepad2.dpad_down) {
                do {
                    mode = "unlocked";
                } while (gamepad2.dpad_down);
            }

            intake.setPower(intakePower);

        }
    }
}
