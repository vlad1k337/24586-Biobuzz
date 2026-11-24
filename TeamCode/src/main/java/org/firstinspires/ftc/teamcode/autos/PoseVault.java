package org.firstinspires.ftc.teamcode.autos;

import static com.pedropathing.api.Paths.curve;
import static com.pedropathing.api.Paths.line;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;

import org.firstinspires.ftc.teamcode.misc.Alliance;

public class PoseVault {

    public Pose startPose;
    public Pose endPose;

    public Pose startPosePark;
    public Pose parkControl;
    public Pose endPosePark;

    public Pose garden;
    public Pose shootFar;

    public Path parkFromStart;
    public Path pickGarden;
    public Path comeBackGarden;
    public Path gardenToShootFar;
    public Path moveToShootFar;
    public Path parkFromFar;

    public PoseVault(Alliance alliance) {
        PoseFactory poseFactory;

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
        shootFar = poseFactory.of(58, 128, 90);

        parkFromStart = curve(startPose, parkControl, endPose).linear(startPose, endPose);
        pickGarden = curve(startPose, poseFactory.of(25, 51, 0), garden).linear(startPose, garden);
        comeBackGarden = curve(garden, poseFactory.of(25, 51, 0), startPose).linear(garden, startPose);

        gardenToShootFar = curve(garden, poseFactory.of(28, 107, 0), shootFar).linear(garden, shootFar);
        moveToShootFar = curve(startPose, poseFactory.of(71, 71, 0), shootFar).linear(startPose, shootFar);

        parkFromFar = line(shootFar, endPose).linear(shootFar, endPose);
    }

}
