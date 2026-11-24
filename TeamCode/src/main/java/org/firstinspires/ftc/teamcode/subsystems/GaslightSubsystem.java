package org.firstinspires.ftc.teamcode.subsystems;

import static com.pedropathing.ivy.commands.Commands.instant;

import com.pedropathing.ivy.CommandBuilder;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.List;

public class GaslightSubsystem {
    private final Limelight3A limelight;

    public GaslightSubsystem(HardwareMap hardwareMap)
    {
        limelight = hardwareMap.get(Limelight3A.class, "limelight");

        limelight.pipelineSwitch(0);
        limelight.setPollRateHz(30);
        limelight.start();
    }

    public void start()
    {
        limelight.start();
    }

    public void stop()
    {
        limelight.stop();
    }

    public boolean checkApriltag()
    {
        LLResult result = limelight.getLatestResult();

        if (result != null && result.isValid()) {
            List<LLResultTypes.FiducialResult> fiducials = result.getFiducialResults();

            return !fiducials.isEmpty();
        }

        return false;
    }

    public CommandBuilder checkApriltagCommand = instant(this::checkApriltag);
}
