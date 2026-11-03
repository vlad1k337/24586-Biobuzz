package org.firstinspires.ftc.teamcode.opmodes;

import com.pedropathing.controllers.PIDController;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.pedro.Constants;

import java.util.List;

@TeleOp
public class LimelightShenenigan extends OpMode {
    private Limelight3A limelight;

    private Follower follower;

    private PIDController headingController;
    private boolean headingLock = false;

    private double fx = 1209;
    private double pollenWidth = 2.8; // inches

    private boolean wasDetected = false;

    private double getPollenTx()
    {
        LLResult result = limelight.getLatestResult();
        if(result != null && result.isValid()) {
            List<LLResultTypes.DetectorResult> detections = result.getDetectorResults();
            LLResultTypes.DetectorResult detection = detections.get(0);

            String className = detection.getClassName();
            double x = detection.getTargetXDegrees();
            double y = detection.getTargetYDegrees();

            List<List<Double>> corners = detection.getTargetCorners();

            double x0 = corners.get(0).get(0);
            double y0 = corners.get(0).get(1);
            double x1 = corners.get(1).get(0);
            double y1 = corners.get(1).get(1);

            telemetry.addData("x0", x0);
            telemetry.addData("y0", y0);
            telemetry.addData("x1", x1);
            telemetry.addData("y1", y1);

            // Z = f/(Wactual/Wpixel);
            double distance = fx * (pollenWidth / Math.abs(x0 - x1));

            telemetry.addData("Confidence", detection.getConfidence());
            telemetry.addData("Distance", distance);
            telemetry.addData(className, "at (" + x + ", " + y + ") degrees");

            wasDetected = true;

            if(detection.getConfidence() < 0.3)
            {
                return 0;
            } else {
                return x;
            }
        }

        wasDetected = false;
        return 0;
    }

    @Override
    public void init()
    {
        follower = Constants.create(hardwareMap);
        follower.setPose(new Pose(0, 0,0));

        headingController = new PIDController(1, 0, 0.1);

        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.setPollRateHz(120);
        limelight.start();
    }

    private void updateDrive(double tx)
    {
        if(gamepad1.leftStickButtonWasPressed())
        {
            headingLock = !headingLock;
        }

        double forward = -gamepad1.left_stick_y;
        double lateral = -0.8 * gamepad1.left_stick_x;
        double turn    = -0.8 * gamepad1.right_stick_x;

        if(headingLock)
        {
            double targetHeading = follower.pose().heading() - tx;
            double headingError  = AngleUnit.normalizeRadians(targetHeading - follower.pose().heading());

            if(Math.abs(headingError) < Math.toRadians(7))
            {
                headingError = 0;
            }

            telemetry.addData("Target", Math.toDegrees(targetHeading));
            telemetry.addData("Error", Math.toDegrees(headingError));
            telemetry.addData("Heading", Math.toDegrees(follower.pose().heading()));

            follower.manual(
                    forward + (wasDetected ? 0.3 : 0),
                    lateral,
                    0.75 * headingController.calculate(targetHeading, headingError)
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
        double tx = Math.toRadians(getPollenTx());

        updateDrive(tx);

        follower.update();

        telemetry.update();
    }
}
