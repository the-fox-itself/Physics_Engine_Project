package Objects;

import Physics.Force;

import java.util.LinkedHashMap;

public class Cube extends GameObject {
    public Cube(double xMC, double yMC, double l, double d) {
        xMassCenter = xMC;
        yMassCenter = yMC;
        length = l;
        density = d;
    }

    public double xMassCenter; //m
    public double yMassCenter; //m

    public double length; //m
    public double density; //kg per m3

//    public static final int DIRECTION_REST = 0;
//    public static final int DIRECTION_NORTH = 1;
//    public static final int DIRECTION_SOUTH = 2;
//    public static final int DIRECTION_WEST = 3;
//    public static final int DIRECTION_EAST = 4;
    public double[] velocity = new double[]{0, 0}; //m per s (+ down, - up); m per s (+ right, - left)

    public final double dragCoefficient = 0.95;  // 1.05 or 0.95

    public LinkedHashMap<String, Force> ActingForces = new LinkedHashMap<>();
}
