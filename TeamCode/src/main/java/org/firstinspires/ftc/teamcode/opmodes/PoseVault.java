package org.firstinspires.ftc.teamcode.opmodes;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;

public class PoseVault {
    final static PoseFactory poseFactory = PoseFactory.degrees();
    public static class Blue {
        static Pose startPose = poseFactory.of(84, 132, 90);
        static Pose parkControl = poseFactory.of(120, 117, 0);
        static Pose endPose = poseFactory.of(130, 36, 180);
    }

    public static class Red {
        static Pose startPose = poseFactory.of(58, 8, 270);
        static Pose parkControl = poseFactory.of(30, 30, 0);
        static Pose endPose = poseFactory.of(12, 105, 0);
    }
}
