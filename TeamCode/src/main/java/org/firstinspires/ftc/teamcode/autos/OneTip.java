package org.firstinspires.ftc.teamcode.autos;

import static com.pedropathing.api.Paths.curve;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import org.firstinspires.ftc.teamcode.opmodes.OpModeStorage;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.misc.Alliance;
import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;

public class OneTip extends OpMode {
    private final Alliance alliance;

    private Follower follower;
    private Shooter shooter;
    private Intake intake;

    private PoseVault poses;

    public OneTip(Alliance alliance)
    {
        this.alliance = alliance;
    }

    private Command shoot4()
    {
        return sequential(
                shooter.startPollenCommand,
                intake.openGateCommand,
                waitMs(1000),
                intake.startCommand,

                waitMs(4000),
                shooter.stopPollenCommand,

                intake.stopCommand,
                intake.closeGateCommand
        );
    }

    private Command autoRoutine()
    {
        return sequential(
                shoot4(),
                follow(follower, poses.parkFromStart)
        );
    }

    @Override
    public void init()
    {
        Scheduler.reset();

        poses = new PoseVault(alliance);

        follower = Constants.create(hardwareMap);
        follower.setPose(poses.startPose);
        follower.update();

        shooter = new Shooter(hardwareMap);
        intake  = new Intake(hardwareMap);
    }

    @Override
    public void start()
    {
        schedule(autoRoutine());
    }

    @Override
    public void loop()
    {
        shooter.updateFeedforward();
        follower.update();

        Scheduler.execute();
    }

    @Override
    public void stop()
    {
        OpModeStorage.autonomousEndPose = follower.pose();
    }
}
