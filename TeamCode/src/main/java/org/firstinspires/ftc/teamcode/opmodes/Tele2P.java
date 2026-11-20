package org.firstinspires.ftc.teamcode.opmodes;

import static org.firstinspires.ftc.teamcode.misc.MathUtils.smooth;
import static java.lang.Math.pow;

import com.pedropathing.controllers.Controller;
import com.pedropathing.controllers.PIDController;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.ManualDrive;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;

import java.util.List;

@TeleOp
public class Tele2P extends OpMode {
    private Follower follower;
    private Intake intake;
    private Shooter shooter;

    private List<LynxModule> allHubs;

    private ElapsedTime timer;

    @Override
    public void init()
    {
        follower = Constants.create(hardwareMap);
        follower.setPose(OpModeStorage.autonomousEndPose);

        intake  = new Intake(hardwareMap);
        shooter = new Shooter(hardwareMap);

        allHubs = hardwareMap.getAll(LynxModule.class);

        for (LynxModule hub : allHubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
        }

        timer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
    }

    @Override
    public void loop()
    {
        for (LynxModule hub : allHubs) {
            hub.clearBulkCache();
        }

        double forward = smooth(-gamepad1.left_stick_y, 2.5);
        double lateral = smooth(-0.8 * gamepad1.left_stick_x, 2);
        double turn    = smooth(-0.8 * gamepad1.right_stick_x, 1);

        ManualDrive.driveOrHold(
                follower,
                forward,
                lateral,
                turn
        );

        intake.update(gamepad1, gamepad2);
        shooter.update(gamepad1, gamepad2);

        intake.updateTelemetry(telemetry);
        shooter.updateTelemetry(telemetry);

        follower.update();

        telemetry.addData("Loop ms", timer.milliseconds());
        timer.reset();

        telemetry.addData("X", follower.pose().x());
        telemetry.addData("Y", follower.pose().y());

        telemetry.update();
    }
}
