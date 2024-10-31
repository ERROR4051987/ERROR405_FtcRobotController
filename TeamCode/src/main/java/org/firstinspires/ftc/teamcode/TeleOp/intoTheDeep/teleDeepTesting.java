package org.firstinspires.ftc.teamcode.TeleOp.intoTheDeep;

import com.arcrobotics.ftclib.controller.PIDController;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@TeleOp(name="teleDeepTesting", group="intoTheDeepTesting")

public class teleDeepTesting extends LinearOpMode {

    // declare drivetrain motors
    private DcMotor bl = null;
    private DcMotor br = null;
    private DcMotor fl = null;
    private DcMotor fr = null;

    // declare secondary motors
    private DcMotorEx wrist = null;
    private DcMotorEx elbow = null;

    // declare servos
    private CRServo lGrip = null;
    private CRServo rGrip = null;

    // declare sensors
    private RevColorSensorV3 lColor = null;
    private RevColorSensorV3 rColor = null;

    // make pid
    private PIDController ePIDF;
    private PIDController wPIDF;
    // wrist pid coeffecients
    public static double p = -0.005, i = 0, d = 0.00002;
    public static double f = 0.07;

    // elbow pid coefficients
    public static double eP = 0.01, eI = 0, eD = 0.00001;
    public static double eF = 0.02;

    public final double ticksInDegree = 1425.1;

    enum wristSpeed {
        SLOW,
        NORMAL,
        FAST
    }

    @Override
    public void runOpMode() {

        // init and set up drivetrain motors
        bl = hardwareMap.get(DcMotor.class, "backLeft");
        br = hardwareMap.get(DcMotor.class, "backRight");
        fl = hardwareMap.get(DcMotor.class, "frontLeft");
        fr = hardwareMap.get(DcMotor.class, "frontRight");

        bl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        br.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        fl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        fr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // init and set up secondary motors
        wrist = hardwareMap.get(DcMotorEx.class, "wrist");
        elbow = hardwareMap.get(DcMotorEx.class, "elbow");

        // init and set up servos
        lGrip = hardwareMap.get(CRServo.class, "lGrip");
        rGrip = hardwareMap.get(CRServo.class, "rGrip");

        // change properties of the wrist & elbow motor
        wrist.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        wrist.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        elbow.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        elbow.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        lColor = hardwareMap.get(RevColorSensorV3.class, "colorLeft");
        rColor = hardwareMap.get(RevColorSensorV3.class, "colorRight");

        lColor.initialize();
        rColor.initialize();

        lColor.enableLed(false);
        rColor.enableLed(false);

        // make pidf vontrollers
        ePIDF = new PIDController(eP, eI, eD);
        wPIDF = new PIDController(p, i, d);

        // declare variables (mutable)
        double leftPower;
        double rightPower;
        double leftStrafe;
        double rightStrafe;
        int wristPower = 0;
        int elbowPower = 0;
        double target = 0;
        double eTarget = 0;
        double power = 0;
        double elbowPidPower = 0;
        double poop = 0;
        double lGripSpeed;
        double rGripSpeed;
        String twinTowerMode = "unlocked";

        // declare speed constants (immutable)
        final double diagonalStrafePower = 0.5;
        final int pidWristFast = 95;
        final int pidWristNormal = 70;
        final int pidWristSlow = 15;
        final int pidElbowFast = 70;
        final int pidElbowNormal = 90;
        final int pidElbowSlow = 20;
        final double driveTrainScalar = 0.85;

        int pos;
        int ePos;

        waitForStart();

        while (opModeIsActive()) {

            // p2 button booleans
            boolean hangerMode = gamepad2.a && gamepad2.b && gamepad2.x && gamepad2.y;
            boolean slowWristMode = gamepad2.y;
            boolean fastWristMode = gamepad2.x;

            // these speed variables are mutabable
            leftPower = gamepad1.left_stick_y;
            rightPower = -gamepad1.right_stick_y;
            leftStrafe = gamepad1.left_trigger;
            rightStrafe = gamepad1.right_trigger;
            lGripSpeed = gamepad2.left_stick_y;
            rGripSpeed = -gamepad2.right_stick_y;

            bl.setPower(leftPower * driveTrainScalar);
            fl.setPower(leftPower * driveTrainScalar);

            br.setPower(rightPower * driveTrainScalar);
            fr.setPower(rightPower * driveTrainScalar);

            // strafe left and right
            if (gamepad1.left_trigger > 0) {

                bl.setPower(-leftStrafe * driveTrainScalar);
                fl.setPower(leftStrafe * driveTrainScalar);

                br.setPower(-leftStrafe * driveTrainScalar);
                fr.setPower(leftStrafe * driveTrainScalar);

            } else if (gamepad1.right_trigger > 0) {

                bl.setPower(rightStrafe * driveTrainScalar);
                fl.setPower(-rightStrafe * driveTrainScalar);

                br.setPower(rightStrafe * driveTrainScalar);
                fr.setPower(-rightStrafe * driveTrainScalar);
            }

            if (gamepad1.dpad_up && gamepad1.left_bumper) {

                bl.setPower(-diagonalStrafePower * driveTrainScalar);
                fl.setPower(0);

                br.setPower(0);
                fr.setPower(diagonalStrafePower * driveTrainScalar);
                // upLeft

            } else if (gamepad1.dpad_down && gamepad1.left_bumper) {

                bl.setPower(0);
                fl.setPower(diagonalStrafePower * driveTrainScalar);

                br.setPower(-diagonalStrafePower * driveTrainScalar);
                fr.setPower(0);
                // downLeft

            } else if (gamepad1.dpad_up && gamepad1.right_bumper) {

                bl.setPower(0);
                fl.setPower(-diagonalStrafePower * driveTrainScalar);

                br.setPower(diagonalStrafePower * driveTrainScalar);
                fr.setPower(0);
                // upRight

            } else if (gamepad1.dpad_down && gamepad1.right_bumper) {

                bl.setPower(diagonalStrafePower * driveTrainScalar);
                fl.setPower(0);


                br.setPower(0);
                fr.setPower(-diagonalStrafePower * driveTrainScalar);

                // downRight

            }

            lGrip.setPower(lGripSpeed);
            rGrip.setPower(rGripSpeed);

            if (hangerMode) {

                lColor.getDistance(DistanceUnit.CM);
                rColor.getDistance(DistanceUnit.CM);
                telemetry.addData("hanging", hangerMode);
                telemetry.addData("lColor", lColor.getDistance(DistanceUnit.CM));
                telemetry.addData("rColor", rColor.getDistance(DistanceUnit.CM));
                telemetry.update();
            }
//

            // p2 pid enums are used for a switch. this changes the speed used for the pid
            wristSpeed currentWristSpeed = null;
            if ((gamepad2.left_trigger > 0 || gamepad2.right_trigger > 0) && slowWristMode && !fastWristMode) {

                currentWristSpeed = wristSpeed.SLOW;

            } else if ((gamepad2.left_trigger > 0 || gamepad2.right_trigger > 0) && !slowWristMode && !fastWristMode) {

                currentWristSpeed = wristSpeed.NORMAL;

            } else if ((gamepad2.left_trigger > 0 || gamepad2.right_trigger > 0) && !slowWristMode && fastWristMode) {

                currentWristSpeed = wristSpeed.FAST;

            }

            switch (currentWristSpeed) {
                case SLOW:
                    do {
                        wPIDF.setPID(p, i, d);
                        pos = wrist.getCurrentPosition();

                        if (gamepad2.right_trigger != 0) {
                            wristPower = pidWristSlow;
                        } else if (gamepad2.left_trigger != 0) {
                            wristPower = -pidWristSlow;
                        } else {
                            wristPower = 0;
                        }

                        if (wristPower != 0) {
                            target = wristPower + pos;
                            double pid = wPIDF.calculate(pos, target);
                            double ff = Math.cos(Math.toRadians(target / ticksInDegree)) * f;
                            power = pid + ff;
                            wrist.setPower(power);
                        }

                    } while (currentWristSpeed == wristSpeed.SLOW);
                    break;

                case NORMAL:
                    do {
                    wPIDF.setPID(p, i, d);
                    pos = wrist.getCurrentPosition();

                    if (gamepad2.right_trigger != 0) {
                        wristPower = pidWristNormal;
                    } else if (gamepad2.left_trigger != 0) {
                        wristPower = -pidWristNormal;
                    } else {
                        wristPower = 0;
                    }

                    if (wristPower != 0) {
                        target = wristPower + pos;
                        double pid = wPIDF.calculate(pos, target);
                        double ff = Math.cos(Math.toRadians(target / ticksInDegree)) * f;
                        power = pid + ff;
                        wrist.setPower(power);
                    }
                    double pid = wPIDF.calculate(pos, target);
                    double ff = Math.cos(Math.toRadians(target / ticksInDegree)) * f;
                    power = pid + ff;
                    wrist.setPower(power);
                } while (gamepad2.left_trigger > 0 || gamepad2.right_trigger > 0 && !gamepad2.y);

                    break;
                case FAST:
                    do {
                    wPIDF.setPID(p, i, d);
                    pos = wrist.getCurrentPosition();

                    if (gamepad2.right_trigger != 0) {
                        wristPower = pidWristFast;
                    } else if (gamepad2.left_trigger != 0) {
                        wristPower = -pidWristFast;
                    } else {
                        wristPower = 0;
                    }

                    if (wristPower != 0) {
                        target = wristPower + pos;
                        double pid = wPIDF.calculate(pos, target);
                        double ff = Math.cos(Math.toRadians(target / ticksInDegree)) * f;
                        power = pid + ff;
                        wrist.setPower(power);
                    }
                    double pid = wPIDF.calculate(pos, target);
                    double ff = Math.cos(Math.toRadians(target / ticksInDegree)) * f;
                    power = pid + ff;
                    wrist.setPower(power);
                } while (gamepad2.left_trigger > 0 || gamepad2.right_trigger > 0 && !gamepad2.y);
            }
        }
//            // when p2 'y' is held, pid wrist will move at the slow constant speed
//            if (gamepad2.left_trigger > 0 || gamepad2.right_trigger > 0 && slowWristMode) {
//                do {
//                    wPIDF.setPID(p, i, d);
//                    pos = wrist.getCurrentPosition();
//
//                    telemetry.addData("poop", poop);
//                    telemetry.addData("pos", pos);
//                    telemetry.addData("wristPower", wristPower);
//                    telemetry.addData("target", target);
//                    telemetry.addData("pidPower", power);
//                    telemetry.update();
//
//                    if (gamepad2.right_trigger != 0) {
//                        wristPower = pidWristSlow;
//                    } else if (gamepad2.left_trigger != 0) {
//                        wristPower = -pidWristSlow;
//                    } else {
//                        wristPower = 0;
//                    }
//
//                    if (wristPower != 0) {
//                        target = wristPower + pos;
//                        double pid = wPIDF.calculate(pos, target);
//                        double ff = Math.cos(Math.toRadians(target / ticksInDegree)) * f;
//                        power = pid + ff;
//                        wrist.setPower(power);
//                    }
//                    double pid = wPIDF.calculate(pos, target);
//                    double ff = Math.cos(Math.toRadians(target / ticksInDegree)) * f;
//                    power = pid + ff;
//                    wrist.setPower(power);
//                } while (gamepad2.left_trigger > 0 || gamepad2.right_trigger > 0 && gamepad2.y);
//
//                // when p2 'y' is not held, pid wrist will move at the fast constant speed
//            } else if (gamepad2.left_trigger > 0 || gamepad2.right_trigger > 0 && !gamepad2.y) {
//                do {
//                    wPIDF.setPID(p, i, d);
//                    pos = wrist.getCurrentPosition();
//
//                    telemetry.addData("poop", poop);
//                    telemetry.addData("pos", pos);
//                    telemetry.addData("wristPower", wristPower);
//                    telemetry.addData("target", target);
//                    telemetry.addData("pidPower", power);
//                    telemetry.update();
//
//                    if (gamepad2.right_trigger != 0) {
//                        wristPower = pidWristFast;
//                    } else if (gamepad2.left_trigger != 0) {
//                        wristPower = -pidWristFast;
//                    } else {
//                        wristPower = 0;
//                    }
//
//                    if (wristPower != 0) {
//                        target = wristPower + pos;
//                        double pid = wPIDF.calculate(pos, target);
//                        double ff = Math.cos(Math.toRadians(target / ticksInDegree)) * f;
//                        power = pid + ff;
//                        wrist.setPower(power);
//                    }
//                    double pid = wPIDF.calculate(pos, target);
//                    double ff = Math.cos(Math.toRadians(target / ticksInDegree)) * f;
//                    power = pid + ff;
//                    wrist.setPower(power);
//                } while (gamepad2.left_trigger > 0 || gamepad2.right_trigger > 0 && !gamepad2.y);
//            }
//
//            // when p2 'y' is held and 'x' is not held, pid elbow will move at the slow constant speed
//            if (p2LB || p2LB && p2Y && !p2X) {
//                do {
//                    ePIDF.setPID(eP, eI, eD);
//                    ePos = elbow.getCurrentPosition();
//
//                    if (p2LB) {
//                        elbowPower = -pidElbowSlow;
//                    } else if (p2LB) {
//                        elbowPower = pidElbowSlow;
//                    } else {
//                        elbowPower = 0;
//                    }
//
//                    if (elbowPower != 0) {
//                        eTarget = elbowPower + ePos;
//                        double ePid = ePIDF.calculate(ePos, eTarget);
//                        double eFf = Math.cos(Math.toRadians(eTarget / ticksInDegree)) * eF;
//                        elbowPidPower = ePid + eFf;
//                        elbow.setPower(elbowPidPower);
//                    }
//
//                    eTarget = elbowPower + ePos;
//                    double ePid = ePIDF.calculate(ePos, eTarget);
//                    double eFf = Math.cos(Math.toRadians(eTarget / ticksInDegree)) * eF;
//                    elbowPidPower = ePid + eFf;
//                    elbow.setPower(elbowPidPower);
//
//                } while (p2LB || p2LB && p2Y && !p2X);
//
//                // when p2 'y' and 'x' is not held, pid elbow will move at the normal constant speed
//            } else if (p2LB || p2LB && !p2Y && !p2X) {
//                do {
//                    ePIDF.setPID(eP, eI, eD);
//                    ePos = elbow.getCurrentPosition();
//
//                    if (p2LB) {
//                        elbowPower = -pidElbowNormal;
//                    } else if (p2LB) {
//                        elbowPower = pidElbowNormal;
//                    } else {
//                        elbowPower = 0;
//                    }
//
//                    if (elbowPower != 0) {
//                        eTarget = elbowPower + ePos;
//                        double ePid = ePIDF.calculate(ePos, eTarget);
//                        double eFf = Math.cos(Math.toRadians(eTarget / ticksInDegree)) * eF;
//                        elbowPidPower = ePid + eFf;
//                        elbow.setPower(elbowPidPower);
//                    }
//
//                    eTarget = elbowPower + ePos;
//                    double ePid = ePIDF.calculate(ePos, eTarget);
//                    double eFf = Math.cos(Math.toRadians(eTarget / ticksInDegree)) * eF;
//                    elbowPidPower = ePid + eFf;
//                    elbow.setPower(elbowPidPower);
//
//                } while (p2LB || p2LB && !p2Y && !p2X);
//
//                // when p2 'x' is held and 'y' is not held, pid elbow will move at the fast constant speed
//            } else if (p2LB || p2LB && !p2Y && p2X) {
//                do {
//                    ePIDF.setPID(eP, eI, eD);
//                    ePos = elbow.getCurrentPosition();
//
//                    if (p2LB) {
//                        elbowPower = -pidElbowFast;
//                    } else if (p2LB) {
//                        elbowPower = pidElbowFast;
//                    } else {
//                        elbowPower = 0;
//                    }
//
//                    if (elbowPower != 0) {
//                        eTarget = elbowPower + ePos;
//                        double ePid = ePIDF.calculate(ePos, eTarget);
//                        double eFf = Math.cos(Math.toRadians(eTarget / ticksInDegree)) * eF;
//                        elbowPidPower = ePid + eFf;
//                        elbow.setPower(elbowPidPower);
//                    }
//
//                    eTarget = elbowPower + ePos;
//                    double ePid = ePIDF.calculate(ePos, eTarget);
//                    double eFf = Math.cos(Math.toRadians(eTarget / ticksInDegree)) * eF;
//                    elbowPidPower = ePid + eFf;
//                    elbow.setPower(elbowPidPower);
//
//                } while (p2LB || p2RB && !p2Y && p2X);
        }
        }


//}

