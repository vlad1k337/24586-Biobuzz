package org.firstinspires.ftc.teamcode.teleops;

import static org.firstinspires.ftc.teamcode.misc.MathUtils.smooth;

import com.pedropathing.controllers.PIDController;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.misc.Alliance;
import org.firstinspires.ftc.teamcode.opmodes.OpModeStorage;
import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.subsystems.GaslightSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;

import java.util.List;

public class Tele2P extends OpMode {
    private final Alliance alliance;

    private Follower follower;
    private Intake intake;
    private Shooter shooter;
    private GaslightSubsystem camera;

    private PIDController headingController;
    private boolean headingLock = false;

    private List<LynxModule> allHubs;

    private Pose hiveClose;
    private Pose hiveFar;

    private ElapsedTime loopTimer;

    public Tele2P(Alliance alliance)
    {
        this.alliance = alliance;
    }

    @Override
    public void init()
    {
        follower = Constants.create(hardwareMap);
        follower.setPose(OpModeStorage.autonomousEndPose);

        headingController = new PIDController(1.5, 0, 0.1);

        intake  = new Intake(hardwareMap);
        shooter = new Shooter(hardwareMap);
        camera  = new GaslightSubsystem(hardwareMap);
        camera.start();

        allHubs = hardwareMap.getAll(LynxModule.class);

        for (LynxModule hub : allHubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
        }

        if(alliance == Alliance.RED)
        {
            hiveClose = new Pose(59, 52.75);
            hiveFar   = new Pose(59, 88.75);
        } else {
            hiveClose = new Pose(83, 52.75);
            hiveFar   = new Pose(83, 88.75);
        }

        loopTimer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
    }

    public void updateDrive()
    {
        if(gamepad1.leftStickButtonWasPressed())
        {
            headingLock = !headingLock;
        }

        double forward = smooth(-gamepad1.left_stick_y, 2.2);
        double lateral = smooth(-gamepad1.left_stick_x, 2.2);
        double turn    = smooth(-0.67 * gamepad1.right_stick_x, 1.5);

        if(headingLock)
        {
            double targetHeading;
            if(follower.pose().y() < 70.75)
            {
                targetHeading = Math.atan2(
                        hiveClose.y() - follower.pose().y(),
                        hiveClose.x() - follower.pose().x()) + Math.PI;
            } else {
                targetHeading = Math.atan2(
                        hiveFar.y() - follower.pose().y(),
                        hiveFar.x() - follower.pose().x()) + Math.PI;

            }
            double headingError  = AngleUnit.normalizeRadians(targetHeading - follower.pose().heading());

            telemetry.addData("Target", Math.toDegrees(targetHeading));
            telemetry.addData("Error", Math.toDegrees(headingError));
            telemetry.addData("Heading", Math.toDegrees(follower.pose().heading()));
            telemetry.addData("Pose", follower.pose());

            follower.manual(
                    forward,
                    lateral,
                    headingController.calculate(targetHeading, headingError)
            );
        } else {
            follower.manual(
                    forward,
                    lateral,
                    turn
            );
        }
    }

    @Override
    public void loop()
    {
        double timeBeginning = loopTimer.milliseconds();

        for (LynxModule hub : allHubs) {
            hub.clearBulkCache();
        }
        
        updateDrive();

        intake.update(gamepad1, gamepad2);
        shooter.update(gamepad1, gamepad2);

        intake.updateTelemetry(telemetry);
        shooter.updateTelemetry(telemetry);

        follower.update();

        telemetry.addData("Heading Lock", headingLock);
        telemetry.addData("X", follower.pose().x());
        telemetry.addData("Y", follower.pose().y());
        telemetry.addData("AprilTag visible", camera.checkApriltag());

        telemetry.addData("Loop Time", loopTimer.milliseconds() - timeBeginning);
        loopTimer.reset();

        telemetry.update();
    }

    @Override
    public void stop()
    {
        OpModeStorage.autonomousEndPose = follower.pose();
    }
}
