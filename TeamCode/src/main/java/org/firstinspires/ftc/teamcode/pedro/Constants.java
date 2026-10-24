package org.firstinspires.ftc.teamcode.pedro;

import com.pedropathing.algorithm.Foresight;
import com.pedropathing.algorithm.ForesightConfig;
import com.pedropathing.controllers.Controller;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Matrix;
import com.pedropathing.math.Vector2D;
import com.pedropathing.revhub.drivetrains.Mecanum;
import com.pedropathing.revhub.drivetrains.MecanumConfig;
import com.pedropathing.revhub.localizers.PinpointConfig;
import com.pedropathing.revhub.localizers.PinpointLocalizer;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Constants {
    public enum Color {
        RED,
        BLUE
    };

    public static MecanumConfig driveConfig = new MecanumConfig(c -> {
        c.frontLeftName.set("FL");
        c.frontRightName.set("FR");
        c.backLeftName.set("BL");
        c.backRightName.set("BR");
        c.frontLeftDirection.set(DcMotorSimple.Direction.FORWARD);
        c.frontRightDirection.set(DcMotorSimple.Direction.REVERSE);
        c.backLeftDirection.set(DcMotorSimple.Direction.FORWARD);
        c.backRightDirection.set(DcMotorSimple.Direction.REVERSE);
    });

    public static PinpointConfig localizerConfig = new PinpointConfig(c -> {
        c.name.set("pinpoint");
        c.podType.set(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        c.xPodOffset.set(-0.29367360543078325);
        c.yPodOffset.set(-8.652612881397637);
        c.xPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
        c.yPodDirection.set(GoBildaPinpointDriver.EncoderDirection.REVERSED);
        c.globalDistanceUnit.set(DistanceUnit.INCH);
        c.offsetUnits.set(DistanceUnit.INCH);
    });

    public static ForesightConfig foresightConfig = new ForesightConfig(
            c -> {
                Controller primaryTranslationalForward = Controller.proportional(0.2567289181601455);
                Controller secondaryTranslationalForward = Controller.proportional(0.09485447533713952);
                Controller primaryTranslationalLateral = Controller.proportional(0.3570032217162877);
                Controller secondaryTranslationalLateral = Controller.proportional(0.13190315112239623);

                c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
                c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

                c.coast.set(Controller.proportionalFeedforward(0.011570325788197326));
                c.brake.set(Controller.proportionalFeedforward(0.009834776919967727));

                c.headingFeedback.set(Controller.proportional(4.084729357112927));
                c.headingBrakeCoefficients.set(Vector2D.cartesian(0.038565798931447036, 0.007228310716168373));

                c.linearBrakeCoefficients.set(Matrix.diag(0.09375962814999743, -4.908689109757405E-4));
                c.quadraticBrakeCoefficients.set(Matrix.diag(0.0012549060270682518, 0.003302965384398944));

                c.maxAchievableForwardVelocity.set(83.03160784863196);
                c.maxAchievableStrafeVelocity.set(67.2245418922149);
                c.naturalForwardDeceleration.set(29.687934885310874);
                c.naturalStrafeDeceleration.set(63.34242507254066);
            }
    );
    public static Follower create(HardwareMap h) {
        return new Follower(
                new PinpointLocalizer(h, localizerConfig),
                new Mecanum(h, driveConfig),
                new Foresight(foresightConfig)
        );
    }
}