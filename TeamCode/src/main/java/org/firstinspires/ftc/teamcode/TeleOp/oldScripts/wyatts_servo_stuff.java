package org.firstinspires.ftc.teamcode.TeleOp.oldScripts;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;

@TeleOp(name="wyatts_servo_stuff", group="misc")

public class wyatts_servo_stuff extends LinearOpMode {

    private CRServo wServo = null;

    double wPower;

    @Override
    public void runOpMode() {

        wServo = hardwareMap.get(CRServo.class, "wServo");

        waitForStart();
        while (opModeIsActive()) {

            if (gamepad2.left_bumper) {
                // if wyatt presses LB, move servo DOWN
                wPower = -0.45;
            } else if (gamepad2.right_bumper) {
                // if wyatt presses RB, move servo UP
                wPower = 0.45;
            } else {
                wPower = 0;
            }

            wServo.setPower(wPower);
        }
    }
}
