package Mechanic;

import Objects.Cube;
import Objects.Liquid;
import Objects.Platform;
import Objects.Space;

import javax.swing.*;

import static Mechanic.Variables.*;

public class Mechanic {
    void preparation() {
        frame.setSize(frameSize.width, frameSize.height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(true);
        frame.setLayout(null);
        frame.setVisible(true);

        space = new Space(1.225,9.81); //1.225, 9.81
        frame.add(space);
        space.setBounds(0, 0, (int) Math.pow(10, 5), (int) Math.pow(10, 5));

        space.platform = new Platform(70);
        space.liquid = new Liquid(35, 997);
        space.Cubes.add(new Cube(20, 1, 2, 100));
        space.Cubes.add(new Cube(4, 1, 2, 300));
        space.Cubes.add(new Cube(30, 1, 2, 500));
        space.Cubes.add(new Cube(40, 1, 2, 700));
//        space.Cubes.add(new Cube(3, -1, 1, 500));
//        space.Cubes.add(new Cube(4, -0.5, 1, 500));
//        space.Cubes.add(new Cube(6, -2, 3, 500));

        new GameLoop().start();
    }
}
