package org.firstinspires.ftc.teamcode.subsystems;

import static com.pedropathing.ivy.commands.Commands.instant;

import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.CommandBuilder;
import com.pedropathing.revhub.drivetrains.CachedMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

public class Shooter {
    private final CachedMotor flywheelPollen, flywheelNectar;
    private final double kS = 0.054, kV = 0.0003937, kP = 0.003;

    public final static double POLLEN_VELOCITY = 1280;
    public final static double NECTAR_VELOCITY = 1410;

    private double pollenTargetVelocity = 0;
    private double nectarTargetVelocity = 0;

    public Shooter(HardwareMap hardwareMap)
    {
        flywheelPollen = new CachedMotor(hardwareMap.get(DcMotorEx.class, "FlywheelPollen"), 0.001);
        MotorConfigurationType pollenConfig = flywheelPollen.raw().getMotorType().clone();
        pollenConfig.setAchieveableMaxRPMFraction(1.0);
        flywheelPollen.raw().setMotorType(pollenConfig);
        flywheelPollen.raw().setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);

        flywheelNectar = new CachedMotor(hardwareMap.get(DcMotorEx.class, "FlywheelNectar"), 0.001);
        MotorConfigurationType nectarConfig = flywheelNectar.raw().getMotorType().clone();
        nectarConfig.setAchieveableMaxRPMFraction(1.0);
        flywheelNectar.raw().setMotorType(nectarConfig);
        flywheelNectar.raw().setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        flywheelNectar.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void update(Gamepad gamepad1, Gamepad gamepad2)
    {
        updateFeedforward();

        if(gamepad1.yWasPressed() || gamepad2.yWasPressed())
        {
            pollenTargetVelocity = POLLEN_VELOCITY;
            nectarTargetVelocity = NECTAR_VELOCITY;
        }

        if(gamepad1.xWasPressed() || gamepad2.xWasPressed())
        {
            pollenTargetVelocity = 0;
            nectarTargetVelocity = 0;
        }

        if(gamepad1.rightBumperWasPressed() || gamepad2.rightBumperWasPressed())
        {
            pollenTargetVelocity += 20;
            nectarTargetVelocity += 20;
        }

        if(gamepad1.leftBumperWasPressed() || gamepad2.leftBumperWasPressed())
        {
            pollenTargetVelocity -= 20;
            nectarTargetVelocity -= 20;
        }
    }

    public void updateFeedforward()
    {
        flywheelPollen.setPower((kV * pollenTargetVelocity) + (kP * (pollenTargetVelocity - flywheelPollen.raw().getVelocity())) + kS);
        flywheelNectar.setPower((kV * nectarTargetVelocity) + (kP * (nectarTargetVelocity - flywheelNectar.raw().getVelocity())) + kS);
    }

    public void updateTelemetry(Telemetry telemetry)
    {
        telemetry.addData("Pollen Actual", flywheelPollen.raw().getVelocity());
        telemetry.addData("Nectar Actual", flywheelNectar.raw().getVelocity());

        telemetry.addData("Pollen Target", pollenTargetVelocity);
        telemetry.addData("Nectar Target", nectarTargetVelocity);

        telemetry.addData("Pollen Current (A)", flywheelPollen.raw().getCurrent(CurrentUnit.AMPS));
        telemetry.addData("Nectar Current (A)", flywheelNectar.raw().getCurrent(CurrentUnit.AMPS));
    }

    public void setPollenTargetVelocity(double newVelocity)
    {
        pollenTargetVelocity = newVelocity;
    }

    public void setNectarTargetVelocity(double newVelocity) { nectarTargetVelocity = newVelocity; }

    public CommandBuilder startPollenCommand = instant(() -> setPollenTargetVelocity(POLLEN_VELOCITY));
    public CommandBuilder startNectarCommand = instant(() -> setNectarTargetVelocity(NECTAR_VELOCITY));

    public CommandBuilder stopPollenCommand = instant(() -> setPollenTargetVelocity(0));
    public CommandBuilder stopNectarCommand = instant(() -> setNectarTargetVelocity(0));
}
