package org.firstinspires.ftc.teamcode.opmodes;

import static java.lang.Math.pow;

import com.pedropathing.follower.Follower;
import com.pedropathing.follower.ManualDrive;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;

@TeleOp
public class Tele1P extends OpMode {
    private Follower follower;
    private Intake intake;
    private Shooter shooter;

    @Override
    public void init()
    {
        follower = Constants.create(hardwareMap);

        intake  = new Intake(hardwareMap);
        shooter = new Shooter(hardwareMap);
    }

    @Override
    public void loop()
    {
        double forward = smooth(-gamepad1.left_stick_y);
        double lateral = smooth(-gamepad1.left_stick_x);
        double turn = smooth(-gamepad1.right_stick_x);

        ManualDrive.driveOrHold(
                follower,
                forward,
                lateral,
                turn
        );

        intake.update(gamepad1);
        shooter.update(gamepad1);
        shooter.updateTelemetry(telemetry);

        follower.update();
        telemetry.update();
    }

    private double smooth(double input)
    {
        final double power = 3;

        if(input > 0)
        {
            return pow(input, power);
        } else {
            return -pow(-input, power);
        }
    }
}
