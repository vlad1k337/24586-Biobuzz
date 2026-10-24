package org.firstinspires.ftc.teamcode.subsystems;

import static com.pedropathing.ivy.commands.Commands.instant;

import com.pedropathing.ivy.Command;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

public class Intake {
    private final DcMotorEx firstMotor;
    private final CRServo servo;

    final double firstMotorPower = 1.0;
    final double servoPower      = 1.0;

    public Intake(HardwareMap hardwareMap)
    {
        firstMotor = hardwareMap.get(DcMotorEx.class, "MotorIntake");
        MotorConfigurationType secondMotorConfig = firstMotor.getMotorType().clone();
        secondMotorConfig.setAchieveableMaxRPMFraction(1.0);
        firstMotor.setMotorType(secondMotorConfig);

        servo = hardwareMap.get(CRServo.class, "ServoIntake");
        servo.setDirection(DcMotorSimple.Direction.REVERSE);
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
    }

    public void updateTelemetry(Telemetry telemetry)
    {
        telemetry.addData("Intake Current (A)", firstMotor.getCurrent(CurrentUnit.AMPS));
    }

    public void start()
    {
        firstMotor.setPower(firstMotorPower);
        servo.setPower(servoPower);
    }

    public void reverse()
    {
        firstMotor.setPower(-firstMotorPower / 2);
        servo.setPower(-servoPower / 2);
    }

    public void stop()
    {
        firstMotor.setPower(0);
        servo.setPower(0);
    }

    public Command startCommand()
    {
        return instant(this::start);
    }

    public Command stopCommand()
    {
        return instant(this::stop);
    }
}
