package org.firstinspires.ftc.teamcode.misc;

public class MathUtils {
    public static double smooth(double input, double p)
    {
        // don't question cuh
        return Math.signum(input)*((Math.exp(p*Math.abs(input)) - 1)/(Math.exp(p) - 1));
    }
}
