package org.firstinspires.ftc.teamcode.subsystems;

import static com.pedropathing.ivy.commands.Commands.instant;

import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.CommandBuilder;
import com.pedropathing.revhub.drivetrains.CachedMotor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

public class Intake {
    private final CachedMotor motor;
    private final CRServo servo;
    private final Servo gate;

    final double MOTOR_POWER = 0.9, REVERSE_MOTOR_POWER = -0.5;
    final double SERVO_POWER = 1.0, REVERSE_SERVO_POWER = -1.0;

    public Intake(HardwareMap hardwareMap)
    {
        motor = new CachedMotor(hardwareMap.get(DcMotorEx.class, "MotorIntake"), 0.01);
        MotorConfigurationType secondMotorConfig = motor.raw().getMotorType().clone();
        secondMotorConfig.setAchieveableMaxRPMFraction(1.0);
        motor.raw().setMotorType(secondMotorConfig);

        servo = hardwareMap.get(CRServo.class, "ServoIntake");
        servo.setDirection(DcMotorSimple.Direction.REVERSE);

        gate = hardwareMap.get(Servo.class, "PollenGate");
    }

    public void update(Gamepad gamepad1, Gamepad gamepad2)
    {
        if(gamepad1.aWasPressed() || gamepad2.aWasPressed())
        {
            start();
        }

        if(gamepad1.bWasPressed() || gamepad2.bWasPressed())
        {
            stop();
        }

        if(gamepad1.rightStickButtonWasPressed() || gamepad2.leftStickButtonWasPressed())
        {
            reverse();
        }

        if(gamepad1.dpadDownWasPressed() || gamepad2.dpadDownWasPressed())
        {
            closeGate();
        }

        if(gamepad1.dpadUpWasPressed() || gamepad2.dpadUpWasPressed())
        {
            openGate();
        }
    }

    public void updateTelemetry(Telemetry telemetry)
    {
        telemetry.addData("Intake Current (A)", motor.raw().getCurrent(CurrentUnit.AMPS));
    }

    public void start()
    {
        motor.setPower(MOTOR_POWER);
        servo.setPower(SERVO_POWER);
    }

    public void reverse()
    {
        motor.setPower(REVERSE_MOTOR_POWER);
        servo.setPower(REVERSE_SERVO_POWER);
    }

    public void stop()
    {
        motor.setPower(0);
        servo.setPower(0);
    }

    public void openGate()
    {
        gate.setPosition(1.0);
    }

    public void closeGate()
    {
        gate.setPosition(0.0);
    }

    public CommandBuilder startCommand = instant(this::start);
    public CommandBuilder stopCommand  = instant(this::stop);

    public CommandBuilder openGateCommand = instant(this::openGate);
    public CommandBuilder closeGateCommand = instant(this::closeGate);
}
