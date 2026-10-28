package org.firstinspires.ftc.teamcode.opmodes;


import static com.pedropathing.utils.Angle.normalize;

import com.pedropathing.controllers.Controller;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.pedro.Constants;

@TeleOp
public class HeadingPIDTuner extends OpMode {
    private Follower follower;
    private Controller controller;
    private double kP, kI, kD;

    private double prevHeading = 0;

    public double angleWrap(double radians) {

        while (radians > Math.PI) {
            radians -= 2 * Math.PI;
        }
        while (radians < -Math.PI) {
            radians += 2 * Math.PI;
        }

        // keep in mind that the result is in radians
        return radians;
    }

    @Override
    public void init()
    {
        follower = Constants.create(hardwareMap);
        follower.setPose(new Pose(0, 0, 0));

        kP = 1.5;
        kI = 0;
        kD = 0.1;

        controller = Controller.pid(kP, kI, kD);
    }

    @Override
    public void loop() {
        if (gamepad1.yWasPressed()) {
            kI += 0.0001;
        }

        if (gamepad1.aWasPressed()) {
            kI -= 0.0001;
        }

        if (gamepad1.dpadUpWasPressed()) {
            kD += 0.0005;
        }

        if (gamepad1.dpadDownWasPressed()) {
            kD -= 0.0005;
        }
        controller = Controller.pid(kP, kI, kD);

        double heading = follower.pose().heading();
        double error = AngleUnit.normalizeRadians(0 - heading);
        double deriv = AngleUnit.normalizeRadians(heading - prevHeading);
        double control = controller.calculate(0, error, deriv);
        prevHeading = heading;

        if (Math.abs(error) < Math.toRadians(5))
        {
            follower.manual(0, 0, 0);
        } else {
            follower.manual(0, 0, control);
        }

        follower.update();

        telemetry.addData("kP", kP);
        telemetry.addData("kI", kI);
        telemetry.addData("kD", kD);
        telemetry.addData("Error", error);
        telemetry.addData("Deriv", deriv);
        telemetry.addData("Heading", heading);
        telemetry.addData("Control", control);

        telemetry.update();
    }
}
