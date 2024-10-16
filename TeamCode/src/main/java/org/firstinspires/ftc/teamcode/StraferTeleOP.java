package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="Strafer Tele Op", group="Starter Code")
public class StraferTeleOP extends LinearOpMode{
    @Override
    public void runOpMode() {
        //Get the Motors
        DcMotor motorFrontLeft = hardwareMap.dcMotor.get("fl");
        DcMotor motorFrontRight = hardwareMap.dcMotor.get("fr");
        DcMotor motorBackLeft = hardwareMap.dcMotor.get("bl");
        DcMotor motorBackRight = hardwareMap.dcMotor.get("br");
        DcMotor armMotor = hardwareMap.dcMotor.get("arm");
        Servo clawLeftMotor = hardwareMap.servo.get("vl");
        Servo clawRightMotor = hardwareMap.servo.get("vr");

        //Set the Direction of the Motors
        motorBackLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        motorFrontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        motorFrontRight.setDirection(DcMotorSimple.Direction.FORWARD);
        motorBackRight.setDirection(DcMotorSimple.Direction.FORWARD);
        armMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        armMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        clawRightMotor.setDirection(Servo.Direction.REVERSE);
        clawLeftMotor.setDirection(Servo.Direction.FORWARD);
        waitForStart();

        if(isStopRequested()) return;

        //Run the OpMode
        while(opModeIsActive()) {
            //Get the Joystick Values
            double y = -gamepad1.left_stick_y;
            double x = gamepad1.left_stick_x;
            double rx = gamepad1.right_stick_x;

            //Arm Control
            double armPower = gamepad2.right_stick_y;

            //Arm Positions
            int lowerPosition = 0;
            int middlePosition = 500;
            int upperPosition = 1000;
            double armLimit = 0.9;

            //Arm Control
            if (gamepad1.a) {
                armMotor.setTargetPosition(lowerPosition);
                armMotor.setPower(0.5);
            } else if (gamepad1.b) {
                armMotor.setTargetPosition(middlePosition);
                armMotor.setPower(0.5);
            } else if (gamepad1.y) {
                armMotor.setTargetPosition(upperPosition);
                armMotor.setPower(0.5);
            }

            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double frontLeftPower = (y + x + rx) / denominator * 0.85;
            double frontRightPower = (y - x - rx) / denominator * 0.85;
            double backLeftPower = (y - x + rx) / denominator * 0.85;
            double backRightPower = (y + x - rx) / denominator * 0.85;

            if (gamepad2.a) {
                clawLeftMotor.setPosition(clawLeftMotor.getPosition() + 0.1);
                clawRightMotor.setPosition(clawRightMotor.getPosition() + 0.1);
            }
            else if (gamepad2.b) {
                clawLeftMotor.setPosition(clawLeftMotor.getPosition() - 0.1);
                clawRightMotor.setPosition(clawRightMotor.getPosition() - 0.1);
            }

            //SecuritySwitch
            if (gamepad2.left_trigger > 0.1 || gamepad2.right_trigger > 0.1 || gamepad1.left_trigger > 0.1 || gamepad1.right_trigger > 0.1) {
                motorFrontLeft.setPower(0);
                motorFrontRight.setPower(0);
                motorBackLeft.setPower(0);
                motorBackRight.setPower(0);
            } else {
                motorFrontLeft.setPower(frontLeftPower);
                motorFrontRight.setPower(frontRightPower);
                motorBackLeft.setPower(backLeftPower);
                motorBackRight.setPower(backRightPower);
                armMotor.setPower(armPower*armLimit);
            }
        }
    }
}