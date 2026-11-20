package org.firstinspires.ftc.teamcode.opmodes;

import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import static org.firstinspires.ftc.teamcode.opmodes.PoseVault.Red.*;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Intake;

@Autonomous
public class AutoRedTwoTip extends OpMode {
    private Follower follower;
    private Intake intake;


    private Command autoRoutine()
    {
        return sequential(
                intake.startCommand(),
                follow(follower, pickGarden),
                waitMs(1000),
                intake.stopCommand(),
                follow(follower, moveToShootGarden),
                waitMs(1000),
                follow(follower, parkFromGarden)
        );
    }

    @Override
    public void init()
    {
        Scheduler.reset();

        follower = Constants.create(hardwareMap);
        follower.setPose(startPose);
        follower.update();

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

    @Override
    public void stop()
    {
        OpModeStorage.autonomousEndPose = follower.pose();
    }
}
