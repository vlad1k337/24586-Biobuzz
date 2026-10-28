package org.firstinspires.ftc.teamcode.subsystems;

import static com.pedropathing.ivy.commands.Commands.instant;

import com.pedropathing.ivy.Command;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

public class Shooter {
    private final DcMotorEx flywheelPollen, flywheelNectar;
    private final double kS = 0.055, kV = 0.0003937, kP = 0.005;

    public final static double pollenSetVelocity = 1310;
    public final static double nectarSetVelocity = 1310;

    private double pollenTargetVelocity = 0;
    private double nectarTargetVelocity = 0;

    public Shooter(HardwareMap hardwareMap)
    {
        flywheelPollen = hardwareMap.get(DcMotorEx.class, "FlywheelPollen");
        MotorConfigurationType pollenConfig = flywheelPollen.getMotorType().clone();
        pollenConfig.setAchieveableMaxRPMFraction(1.0);
        flywheelPollen.setMotorType(pollenConfig);
        flywheelPollen.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);

        flywheelNectar = hardwareMap.get(DcMotorEx.class, "FlywheelNectar");
        MotorConfigurationType nectarConfig = flywheelNectar.getMotorType().clone();
        nectarConfig.setAchieveableMaxRPMFraction(1.0);
        flywheelNectar.setMotorType(nectarConfig);
        flywheelNectar.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        flywheelNectar.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void update(Gamepad gamepad1, Gamepad gamepad2)
    {
        updateFeedforward();
        if(gamepad1.yWasPressed() || gamepad2.yWasPressed())
        {
            pollenTargetVelocity = pollenSetVelocity;
            nectarTargetVelocity = nectarSetVelocity;
        }

        if(gamepad1.xWasPressed() || gamepad2.xWasPressed())
        {
            pollenTargetVelocity = 0;
            nectarTargetVelocity = 0;
        }

        if(gamepad1.rightBumperWasPressed() || gamepad2.rightBumperWasPressed())
        {
            pollenTargetVelocity += 20;
        }

        if(gamepad1.leftBumperWasPressed() || gamepad2.leftBumperWasPressed())
        {
            pollenTargetVelocity -= 20;
        }
    }

    public void updateFeedforward()
    {
        flywheelPollen.setPower((kV * pollenTargetVelocity) + (kP * (pollenTargetVelocity - flywheelPollen.getVelocity())) + kS);
        flywheelNectar.setPower((kV * nectarTargetVelocity) + (kP * (nectarTargetVelocity - flywheelNectar.getVelocity())) + kS);
    }

    public void updateTelemetry(Telemetry telemetry)
    {
        telemetry.addData("Pollen Actual", flywheelPollen.getVelocity());
        telemetry.addData("Pollen Target", pollenTargetVelocity);
        telemetry.addData("Pollen Current (A)", flywheelPollen.getCurrent(CurrentUnit.AMPS));
    }

    public void setPollenTargetVelocity(double newVelocity)
    {
        pollenTargetVelocity = newVelocity;
    }

    public void setNectarTargetVelocity(double newVelocity) { nectarTargetVelocity = newVelocity; }

    public Command startPollenCommand()
    {
        return instant(() -> setPollenTargetVelocity(pollenSetVelocity));
    }

    public Command startNectarCommand()
    {
        return instant(() -> setPollenTargetVelocity(pollenSetVelocity));
    }

    public Command stopPollenCommand()
    {
        return instant(() -> setPollenTargetVelocity(0));
    }

    public Command stopNectarCommand()
    {
        return instant(() -> setNectarTargetVelocity(0));
    }
}
