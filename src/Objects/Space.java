package Objects;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static Mechanic.Variables.*;

public class Space extends JPanel {
    public Space(double d, double g) {
        density = d;
        this.g = g;
    }

    public double density; //kg per m3
    public double g; //m per s2
    public final double light_speed = 299792458; //m per s

    public ArrayList<Cube> Cubes = new ArrayList<>();
    public Platform platform;
    public Liquid liquid;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.cyan);
        g.fillRect(0, (int) (liquid.y*pixelsPerMeter), (int) Math.pow(10, 5), (int) Math.pow(10, 5));

        g.setColor(Color.green);
        g.fillRect(0, (int) (platform.y*pixelsPerMeter), (int) Math.pow(10, 5), (int) Math.pow(10, 5));

        g.setColor(Color.gray);
        for (Cube cube : Cubes) {
            g.fillRect((int) (cube.xMassCenter*pixelsPerMeter-cube.length/2*pixelsPerMeter), (int) (cube.yMassCenter*pixelsPerMeter-cube.length*pixelsPerMeter/2), (int) (cube.length*pixelsPerMeter), (int) (cube.length*pixelsPerMeter));
        }
    }
}
