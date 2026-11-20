package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.misc.Alliance;
import org.firstinspires.ftc.teamcode.teleops.Tele2P;

@TeleOp
public class BlueTele2P extends Tele2P {
    public BlueTele2P()
    {
        super(Alliance.BLUE);
    }
}
