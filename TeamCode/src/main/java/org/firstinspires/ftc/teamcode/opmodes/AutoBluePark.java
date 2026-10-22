package org.firstinspires.ftc.teamcode.opmodes;

import static com.pedropathing.api.Paths.curve;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import static org.firstinspires.ftc.teamcode.opmodes.PoseVault.Blue.*;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.pedro.Constants;

@Autonomous
public class AutoBluePark extends OpMode {
    private Follower follower;

    private Path park()
    {
        return curve(startPose, parkControl, endPose).linear(startPose, endPose);
    }

    private Command autoRoutine()
    {
        return sequential(
                follow(follower, park())
        );
    }

    @Override
    public void init()
    {
        Scheduler.reset();

        follower = Constants.create(hardwareMap);
        follower.setPose(startPose);
        follower.update();
    }

    @Override
    public void start()
    {
        schedule(autoRoutine());
    }

    @Override
    public void loop()
    {
        follower.update();
        Scheduler.execute();
    }
}
