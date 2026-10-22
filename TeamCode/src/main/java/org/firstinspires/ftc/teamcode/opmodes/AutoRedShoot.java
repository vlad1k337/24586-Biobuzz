package org.firstinspires.ftc.teamcode.opmodes;

import static com.pedropathing.api.Paths.curve;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import static org.firstinspires.ftc.teamcode.opmodes.PoseVault.Red.*;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;

@Autonomous
public class AutoRedShoot extends OpMode {
    private Follower follower;
    private Shooter shooter;
    private Intake intake;

    private Path park()
    {
        return curve(startPose, parkControl, endPose).linear(startPose, endPose);
    }

    private Command shoot4()
    {
        return sequential(
                shooter.startPollenCommand(),
                intake.startCommand(),
                waitMs(3000),
                shooter.stopPollenCommand(),
                intake.stopCommand()
        );
    }

    private Command autoRoutine()
    {
        return sequential(
                shoot4(),
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
        follower.update();
        Scheduler.execute();
    }
}
