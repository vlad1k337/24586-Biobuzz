package org.firstinspires.ftc.teamcode.subsystems;

import static com.pedropathing.ivy.commands.Commands.instant;
import static com.pedropathing.ivy.groups.Groups.sequential;

import com.pedropathing.ivy.Command;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

public class Shooter {
    private final DcMotorEx flywheelPollen;
    private final double kS = 0.055, kV = 0.0003937, kP = 0.005;

    public final static double pollenSetVelocity = 1460;

    private double pollenTargetVelocity = 0;

    public Shooter(HardwareMap hardwareMap)
    {
        flywheelPollen = hardwareMap.get(DcMotorEx.class, "FlywheelPollen");
        MotorConfigurationType flywheelConfig = flywheelPollen.getMotorType().clone();
        flywheelConfig.setAchieveableMaxRPMFraction(1.0);
        flywheelPollen.setMotorType(flywheelConfig);
        flywheelPollen.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void update(Gamepad gamepad)
    {
        updateFeedforward();
        if(gamepad.yWasPressed())
        {
            pollenTargetVelocity = pollenSetVelocity;
        }

        if(gamepad.xWasPressed())
        {
            pollenTargetVelocity = 0;
        }

        if(gamepad.rightBumperWasPressed())
        {
            pollenTargetVelocity += 20;
        }

        if(gamepad.leftBumperWasPressed())
        {
            pollenTargetVelocity -= 20;
        }
    }

    public void updateFeedforward()
    {
        flywheelPollen.setPower((kV * pollenTargetVelocity) + (kP * (pollenTargetVelocity - flywheelPollen.getVelocity())) + kS);
    }

    public void updateTelemetry(Telemetry telemetry)
    {
        telemetry.addData("Flywheel Actual", flywheelPollen.getVelocity());
        telemetry.addData("Flywheel Target", pollenTargetVelocity);
        telemetry.addData("Flywheel Current (A)", flywheelPollen.getCurrent(CurrentUnit.AMPS));
    }

    public void setPollenTargetVelocity(double newVelocity)
    {
        pollenTargetVelocity = newVelocity;
    }

    public Command startPollenCommand()
    {
        return instant(() -> setPollenTargetVelocity(pollenSetVelocity));
    }

    public Command stopPollenCommand()
    {
        return instant(() -> setPollenTargetVelocity(0));
    }
}
