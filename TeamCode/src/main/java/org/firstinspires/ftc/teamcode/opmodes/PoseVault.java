package org.firstinspires.ftc.teamcode.opmodes;

import static com.pedropathing.api.Paths.curve;
import static com.pedropathing.api.Paths.line;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;

public class PoseVault {
    final static PoseFactory poseFactory = PoseFactory.degrees();
    public static class Blue {
        static Pose startPose = poseFactory.of(84, 132, 90);
        static Pose parkControl = poseFactory.of(120, 117, 0);
        static Pose endPose = poseFactory.of(130, 45, 180);

        static Pose startPosePark = poseFactory.of(105, 8, 90);
        static Pose endPosePark = poseFactory.of(125, 25, 180);
    }

    public static class Red {
        static Pose startPose = poseFactory.of(58, 8, 270);
        static Pose parkControl = poseFactory.of(30, 30, 0);
        static Pose endPose = poseFactory.of(12, 93, 0);

        static Pose startPosePark = poseFactory.of(36, 133, 270);
        static Pose endPosePark = poseFactory.of(15, 120, 0);

        static Pose garden = poseFactory.of(8,8, 270);
        static Pose shootFar = poseFactory.of(57,129, 90);

        static Path pickGarden        = curve(startPose, poseFactory.of(25, 51, 0), garden).linear(startPose, garden);
        static Path moveToShootGarden = curve(garden, poseFactory.of(25, 110, 0), shootFar).linear(garden, shootFar);
        static Path parkFromGarden    = line(shootFar, endPose).linear(shootFar, endPose);
    }
}
