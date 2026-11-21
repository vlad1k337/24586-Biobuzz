package org.firstinspires.ftc.teamcode.autos;

import static com.pedropathing.api.Paths.curve;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import org.firstinspires.ftc.teamcode.opmodes.OpModeStorage;
import org.firstinspires.ftc.teamcode.opmodes.PoseVault;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.misc.Alliance;
import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;

/*
 Honorary Autonomous mode for usage with team 7229 BzBots on November 21, 2026
 */

public class BzAuto extends OpMode {
    private final Alliance alliance;

    private Follower follower;
    private Shooter shooter;
    private Intake intake;

    private PoseVault poses;

    public BzAuto(Alliance alliance)
    {
        this.alliance = alliance;
    }

    private Path park()
    {
        return curve(poses.startPose, poses.parkControl, poses.endPose).linear(poses.startPose, poses.endPose);
    }

    private Command shoot4()
    {
        return sequential(
                shooter.startPollenCommand(),
                intake.openGateCommand(),
                waitMs(1000),
                intake.startCommand(),

                waitMs(3500),
                shooter.stopPollenCommand(),

                intake.stopCommand(),
                intake.closeGateCommand()
        );
    }

    private Command autoRoutine()
    {
        return sequential(
                shoot4(),

                intake.startCommand(),
                follow(follower, poses.pickGarden),
                waitMs(1500),
                intake.stopCommand(),

                follow(follower, poses.comeBackGarden),
                shoot4(),

                follow(follower, park())
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
