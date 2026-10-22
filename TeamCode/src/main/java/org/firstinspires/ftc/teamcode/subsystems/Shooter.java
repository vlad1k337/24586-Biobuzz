package org.firstinspires.ftc.teamcode.subsystems;

import static com.pedropathing.ivy.groups.Groups.sequential;

import com.pedropathing.ivy.Command;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Shooter {
    private final DcMotorEx flywheelPollen;
    private final double kS = 0.055, kV = 0.0003937, kP = 0.005;

    public final static double pollenSetVelocity = 1450;

    private static double pollenTargetVelocity = 0;

    public Shooter(HardwareMap hardwareMap)
    {
        flywheelPollen = hardwareMap.get(DcMotorEx.class, "FlywheelPollen");
        MotorConfigurationType flywheelConfig = flywheelPollen.getMotorType().clone();
        flywheelConfig.setAchieveableMaxRPMFraction(1.0);
        flywheelPollen.setMotorType(flywheelConfig);
        flywheelPollen.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void update(Gamepad gamepad1)
    {
        updateFeedforward();
        if(gamepad1.yWasPressed())
        {
            pollenTargetVelocity = pollenSetVelocity;
        }

        if(gamepad1.xWasPressed())
        {
            pollenTargetVelocity = 0;
        }

        if(gamepad1.rightBumperWasPressed())
        {
            pollenTargetVelocity += 20;
        }

        if(gamepad1.leftBumperWasPressed())
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
        telemetry.update();
    }

    public void setPollenTargetVelocity(double newVelocity)
    {
        pollenTargetVelocity = newVelocity;
    }

    public Command startPollenCommand()
    {
        return Command.build()
                .setExecute(() -> setPollenTargetVelocity(pollenSetVelocity))
                .setDone(() -> Math.abs(flywheelPollen.getVelocity() - pollenTargetVelocity) < 40)
                .requiring(flywheelPollen);
    }

    public Command stopPollenCommand()
    {
        return Command.build()
                .setExecute(() -> setPollenTargetVelocity(0));
    }
}
