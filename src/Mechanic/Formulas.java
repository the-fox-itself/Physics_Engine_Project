package Mechanic;

import static java.lang.Math.*;

public class Formulas {
    public static double volume(double length) {
        return pow(length, 3);
    }

    public static double mass(double length, double object_density) {
        return volume(length) * object_density;
    }

    public static double force_weight(double length, double object_density, double g) {
        return mass(length, object_density) * g;
    }

    public static double force_air_resistance(double length, double space_density, double[] velocity, double dragCoefficient) {
        return -(0.5 * space_density * velocity[0] * velocity[0] * dragCoefficient * pow(length, 2));
    }

    public static double force_buoyant(double length, double height, double space_density, double g) {
        return -(space_density * g * length*length*height);
    }

    public static double terminal_velocity(double object_density, double length, double g, double space_density, double dragCoefficient) {
        return sqrt((2 * force_weight(length, object_density, g)) / (space_density * pow(length, 2) * dragCoefficient));
    }
}
