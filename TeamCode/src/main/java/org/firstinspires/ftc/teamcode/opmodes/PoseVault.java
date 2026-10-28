package org.firstinspires.ftc.teamcode.opmodes;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;

public class PoseVault {
    final static PoseFactory poseFactory = PoseFactory.degrees();
    public static class Blue {
        static Pose startPose = poseFactory.of(84, 132, 90);
        static Pose startPosePark = poseFactory.of(105, 8, 90);

        static Pose parkControl = poseFactory.of(120, 117, 0);
        static Pose endPose = poseFactory.of(130, 45, 180);
        static Pose endPosePark = poseFactory.of(125, 25, 180);
    }

    public static class Red {
        static Pose startPose = poseFactory.of(58, 8, 270);
        static Pose startPosePark = poseFactory.of(33, 133, 270);

        static Pose parkControl = poseFactory.of(30, 30, 0);
        static Pose endPose = poseFactory.of(12, 95, 0);
        static Pose endPosePark = poseFactory.of(15, 120, 0);
    }
}
