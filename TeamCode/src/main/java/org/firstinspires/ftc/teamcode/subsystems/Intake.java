package org.firstinspires.ftc.teamcode.subsystems;

import static com.pedropathing.ivy.commands.Commands.instant;

import com.pedropathing.ivy.Command;
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
    private final DcMotorEx intakeMotor;
    private final CRServo servo;
    private final Servo gate;

    final double intakeMotorPower = 0.8;
    final double servoPower       = 1.0;

    public Intake(HardwareMap hardwareMap)
    {
        intakeMotor = hardwareMap.get(DcMotorEx.class, "MotorIntake");
        MotorConfigurationType secondMotorConfig = intakeMotor.getMotorType().clone();
        secondMotorConfig.setAchieveableMaxRPMFraction(1.0);
        intakeMotor.setMotorType(secondMotorConfig);

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

        if(gamepad1.dpadUpWasPressed() || gamepad2.dpadDownWasPressed())
        {
            openGate();
        }
    }

    public void updateTelemetry(Telemetry telemetry)
    {
        telemetry.addData("Intake Current (A)", intakeMotor.getCurrent(CurrentUnit.AMPS));
    }

    public void start()
    {
        intakeMotor.setPower(intakeMotorPower);
        servo.setPower(servoPower);
    }

    public void reverse()
    {
        intakeMotor.setPower(-intakeMotorPower / 2);
        servo.setPower(-servoPower / 2);
    }

    public void stop()
    {
        intakeMotor.setPower(0);
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

    public Command startCommand()
    {
        return instant(this::start);
    }

    public Command stopCommand()
    {
        return instant(this::stop);
    }

    public Command openGateCommand() { return instant(this::openGate); }

    public Command closeGateCommand() { return instant(this::closeGate); }
}
