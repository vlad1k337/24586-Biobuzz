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
        double forward = smooth(-gamepad1.left_stick_y, 2.5);
        double lateral = smooth(-0.8 * gamepad1.left_stick_x, 2);
        double turn = smooth(-0.8 * gamepad1.right_stick_x, 2);

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
        telemetry.update();
    }

    private double smooth(double input, double power)
    {
        if(input > 0)
        {
            return pow(input, power);
        } else {
            return -pow(-input, power);
        }
    }
}
