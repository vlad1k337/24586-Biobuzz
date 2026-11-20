package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.autos.Park;
import org.firstinspires.ftc.teamcode.misc.Alliance;

@Autonomous
public class RedPark extends Park {
    public RedPark()
    {
        super(Alliance.RED);
    }
}
