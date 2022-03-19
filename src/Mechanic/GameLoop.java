package Mechanic;

import Objects.Cube;
import Physics.Force;

import java.util.Map;

import static Mechanic.Formulas.*;
import static Mechanic.Variables.*;
import static java.lang.Math.*;

public class GameLoop extends Thread {
    public long lastGameStatsUpdate = System.nanoTime(); //nanoseconds

    public void run() {
        long previous = System.nanoTime(); //nanoseconds
        long steps = 0; //nanoseconds
        while (true) {
            long loopStartTime = System.nanoTime(); //nanoseconds
            long elapsed = loopStartTime - previous; //nanoseconds
            previous = System.nanoTime();
            steps += elapsed;

            handleInput();

            while (steps >= nanosecondsPerFrame) {
                updateGameStats();
                steps -= nanosecondsPerFrame;
            }

            frame.repaint();

            double loopSlot = nanosecondsPerFrame; //nanoseconds
            double endTime = loopStartTime + loopSlot; //nanoseconds
            while (System.nanoTime() < endTime) {
                try {
                    sleep(1);
                } catch (InterruptedException ignored) {}
            }
            if (stopGameLoop) {
                stopGameLoop = false;
                break;
            }
        }
    }

    public void handleInput() {

    }
    public void updateGameStats() {
        long previous = lastGameStatsUpdate;  //nanoseconds
        for (long x = (System.nanoTime() - previous) / 1000000; x > 0; x--) {                                           // Update for every millisecond
            for (Cube cube : space.Cubes) {                                                                             // For each cube
                if (space.platform.y <= cube.yMassCenter + cube.length / 2) {                                           // If the cube is either on the platform or inside a platform
                    cube.ActingForces.put(Force.TYPE_WEIGHT, new Force(Force.TYPE_WEIGHT, force_weight(cube.length, cube.density, space.g), Force.DIRECTION_VERTICAL,
                            cube.length / 2, cube.length / 2));                                                 // Creating a new Weight value according to the formula
                    cube.ActingForces.put(Force.TYPE_AIR_RESISTANCE, new Force(Force.TYPE_AIR_RESISTANCE, -0, Force.DIRECTION_VERTICAL,
                            cube.length / 2, cube.length / 2));                                                 // Creating a new Air resistance value 0
                } else {                                                                                                // Or if the cube is above the platform
                    if (cube.ActingForces.containsKey(Force.TYPE_WEIGHT)) {                                             // Updating the Weight value without updating the starting time
                        cube.ActingForces.get(Force.TYPE_WEIGHT).magnitude = force_weight(cube.length, cube.density, space.g);
                    } else {
                        cube.ActingForces.put(Force.TYPE_WEIGHT, new Force(Force.TYPE_WEIGHT,
                                force_weight(cube.length, cube.density, space.g),
                                Force.DIRECTION_VERTICAL, cube.length / 2, cube.length / 2));
                    }
                    cube.ActingForces.put(Force.TYPE_AIR_RESISTANCE, new Force(Force.TYPE_AIR_RESISTANCE,               // Creating a new Air resistance value according to the formula
                            force_air_resistance(cube.length, space.density, cube.velocity, cube.dragCoefficient),
                            Force.DIRECTION_VERTICAL, cube.length / 2, cube.length / 2));
                }
                double buoyant_magnitude;
                if (space.liquid.y < cube.yMassCenter + cube.length / 2) {                                              // If the cube is inside a liquid
                    double height_in_liquid = Math.min((cube.yMassCenter + cube.length / 2) - space.liquid.y, cube.length);  // Find the height of the part of the cube in the liquid
                    buoyant_magnitude = force_buoyant(cube.length, height_in_liquid, space.liquid.density, space.g) +   // Calculate and sum two Buoyant forces acting on the cube
                            force_buoyant(cube.length, cube.length-height_in_liquid, space.density, space.g);
                } else {                                                                                                // Or if the cube is outside or touching the liquid
                    buoyant_magnitude = force_buoyant(cube.length, cube.length, space.density, space.g);                // Calculate the single Buoyant force acting on the cube
                }
                if (cube.ActingForces.containsKey(Force.TYPE_BUOYANT)) {                                                // Updating the Buoyant force value without updating the starting time
                    cube.ActingForces.get(Force.TYPE_BUOYANT).magnitude = buoyant_magnitude;
                } else {
                    cube.ActingForces.put(Force.TYPE_BUOYANT, new Force(Force.TYPE_BUOYANT, buoyant_magnitude,
                            Force.DIRECTION_VERTICAL,cube.length / 2, cube.length / 2));
                }

                int totalVertical = 0;
                int totalHorizontal = 0;
                for (Map.Entry<String, Force> forceSet : cube.ActingForces.entrySet()) {                                // Summing up every acting force magnitude (+Weight, -Air resistance, -Buoyancy)
                    Force force = forceSet.getValue();
                    if (force.direction.equals(Force.DIRECTION_VERTICAL))
                        totalVertical += force.magnitude;
                    else if (force.direction.equals(Force.DIRECTION_HORIZONTAL)) {
                        totalHorizontal += force.magnitude;
                    }
                }
                System.out.println("x:"+cube.xMassCenter+", y:"+cube.yMassCenter+", v:"+totalVertical + ", h:" + totalHorizontal);

                if (totalVertical > 0) {                                                                                // If the cube is falling
                    cube.ActingForces.remove(Force.TYPE_BUOYANT);                                                       // Update Buoyant force starting time
                    double speed = space.g * (System.nanoTime() / Math.pow(10, 9) - cube.ActingForces.get(Force.TYPE_WEIGHT).timeForceStarted);
                    double terminalVelocity = terminal_velocity(cube.density, cube.length, space.g, space.density, cube.dragCoefficient);
                    if (abs(speed) > abs(terminalVelocity)) {
                        cube.velocity = new double[]{terminalVelocity, 0};
                    } else
                        cube.velocity = new double[]{speed, 0};
                } else if (totalVertical < 0) {                                                                         // Or if the cube is rising
                    Force buoyant = cube.ActingForces.get(Force.TYPE_BUOYANT);
                    double speed = buoyant.magnitude*(System.nanoTime() / Math.pow(10, 9) - buoyant.timeForceStarted) / mass(cube.length, cube.density);
                    if (abs(speed) > space.light_speed) {
                        cube.velocity = new double[]{space.light_speed, 0};
                    } else {
                        cube.velocity = new double[]{speed, 0};
                    }
                }
            }

            for (Cube cube : space.Cubes) {  // Moving cubes according to their velocity
                if (cube.velocity[0] < 0) {                                                                             // If the cube is rising
                    cube.yMassCenter += cube.velocity[0];                                                               // Move the cube upwards according to the velocity value
                } else if (cube.velocity[0] > 0) {                                                                      // If the cube is falling
                    cube.yMassCenter += Math.min(space.platform.y - (cube.yMassCenter + cube.length / 2), cube.velocity[0]/1000); //Processing the case of going into the platform
                }
            }
        }
        lastGameStatsUpdate = System.nanoTime();
    }
}
