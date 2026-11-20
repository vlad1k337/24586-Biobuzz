package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.misc.Alliance;
import org.firstinspires.ftc.teamcode.teleops.Tele2P;

@TeleOp
public class RedTele2P extends Tele2P {
    public RedTele2P()
    {
        super(Alliance.RED);
    }
}
