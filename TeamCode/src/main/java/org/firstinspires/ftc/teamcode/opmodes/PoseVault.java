package org.firstinspires.ftc.teamcode.opmodes;

import static com.pedropathing.api.Paths.curve;
import static com.pedropathing.api.Paths.line;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;

import org.firstinspires.ftc.teamcode.misc.Alliance;

public class PoseVault {
    private final PoseFactory poseFactory;

    public Pose startPose;
    public Pose endPose;

    public Pose startPosePark;
    public Pose parkControl;
    public Pose endPosePark;

    public Pose garden;
    public Pose shootFar;

    public Path pickGarden;
    public Path comeBackGarden;
    public Path moveToShootGarden;
    public Path parkFromGarden;

    public PoseVault(Alliance alliance) {
        if (alliance == Alliance.RED) {
            poseFactory = PoseFactory.degrees();
        } else {
            poseFactory = PoseFactory.degrees().mirrorX(70.75).mirrorY(70.75);
        }

        startPose = poseFactory.of(58, 8, 270);
        endPose = poseFactory.of(13, 93, 0);

        startPosePark = poseFactory.of(36, 133, 270);
        parkControl = poseFactory.of(30, 30, 0);
        endPosePark = poseFactory.of(15, 120, 0);

        garden = poseFactory.of(8, 8, 270);
        shootFar = poseFactory.of(57, 128, 90);

        pickGarden = curve(startPose, poseFactory.of(25, 51, 0), garden).linear(startPose, garden);
        comeBackGarden = curve(garden, poseFactory.of(25, 51, 0), startPose).linear(garden, startPose);
        moveToShootGarden = curve(garden, poseFactory.of(28, 107, 0), shootFar).linear(garden, shootFar);
        parkFromGarden = line(shootFar, endPose).linear(shootFar, endPose);
    }

}
