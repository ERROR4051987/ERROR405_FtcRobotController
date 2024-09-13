package org.firstinspires.ftc.teamcode.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous (name= "autoDeep", group= "intoTheDeep")
public class autoDeep extends LinearOpMode {

    // declare drivetrain motors
    private DcMotorEx bl = null;
    private DcMotorEx br = null;
    private DcMotorEx fl = null;
    private DcMotorEx fr = null;

    // make time move for stuff
    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {

        // init and set up motors
        bl = hardwareMap.get(DcMotorEx.class, "backLeft");
        br = hardwareMap.get(DcMotorEx.class, "backRight");
        fl = hardwareMap.get(DcMotorEx.class, "frontLeft");
        fr = hardwareMap.get(DcMotorEx.class, "frontRight");
        
        bl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        br.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        fl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        fr.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        
        waitForStart();
        runtime.reset();

        while(opModeIsActive()) {

            posForward(500, 1000);
        }

    }
    private void posForward (double tps, int pos) {

        bl.setTargetPosition(pos);
        br.setTargetPosition(pos);
        fl.setTargetPosition(pos);
        fr.setTargetPosition(pos);

        bl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        br.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        fl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        fr.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // tps = Ticks Per Second. (ie. position = 1000, tps = 500, will reach target position in 2s.

        bl.setVelocity(Math.abs(tps));
        br.setVelocity(Math.abs(tps));
        fl.setVelocity(Math.abs(tps));
        fr.setVelocity(Math.abs(tps));

        while (bl.isBusy() && fl.isBusy() && fr.isBusy() && br.isBusy()){
            idle();
        }

        bl.setPower(0);
        fl.setPower(0);
        fr.setPower(0);
        br.setPower(0);

        bl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        fl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        fr.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        br.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        runtime.reset();
    }
}
