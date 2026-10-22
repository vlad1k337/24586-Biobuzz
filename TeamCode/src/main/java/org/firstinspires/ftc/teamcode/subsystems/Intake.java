package org.firstinspires.ftc.teamcode.subsystems;

import com.pedropathing.ivy.Command;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;

public class Intake {
    private final DcMotorEx firstMotor;
    private final CRServo servo;

    final double firstMotorPower = 0.8;
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

    public void update(Gamepad gamepad1)
    {
        if(gamepad1.aWasPressed())
        {
            start();
        }

        if(gamepad1.bWasPressed())
        {
            stop();
        }
    }

    public void start()
    {
        firstMotor.setPower(firstMotorPower);
        servo.setPower(servoPower);
    }

    public void stop()
    {
        firstMotor.setPower(0);
        servo.setPower(0);
    }

    public Command startCommand()
    {
        return Command.build()
                .setExecute(this::start);
    }

    public Command stopCommand()
    {
        return Command.build()
                .setExecute(this::stop);
    }
}
