package org.firstinspires.ftc.teamcode.autos;

import static com.pedropathing.api.Paths.line;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import org.firstinspires.ftc.teamcode.misc.Alliance;
import org.firstinspires.ftc.teamcode.opmodes.OpModeStorage;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.pedro.Constants;

public class Park extends OpMode {
    private final Alliance alliance;

    private Follower follower;
    private PoseVault poses;

    public Park(Alliance alliance)
    {
        this.alliance = alliance;
    }

    private Path park()
    {
        return line(poses.startPosePark, poses.endPosePark).linear(poses.startPosePark, poses.endPosePark);
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

        poses = new PoseVault(alliance);

        follower = Constants.create(hardwareMap);
        follower.setPose(poses.startPosePark);
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

    @Override
    public void stop()
    {
        OpModeStorage.autonomousEndPose = follower.pose();
    }
}
