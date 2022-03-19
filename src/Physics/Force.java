package Physics;

public class Force {
    public Force(String t, double m, String d, double xA, double yA) {
        type = t;
        magnitude = m;
        direction = d;
        xApplied = xA;
        yApplied = yA;
        timeForceStarted = System.nanoTime()/Math.pow(10, 9);
    }

    public static final String TYPE_AIR_RESISTANCE = "air resistance";
    public static final String TYPE_REACTION_FORCE = "reaction force";
    public static final String TYPE_WEIGHT = "weight";
    public static final String TYPE_BUOYANT = "buoyant force";
    public String type;

    public double magnitude; //N

    public static final String DIRECTION_VERTICAL = "Vertical";
    public static final String DIRECTION_HORIZONTAL = "Horizontal";
    public String direction = "";

    public double xApplied; //m
    public double yApplied; //m

    public double timeForceStarted; //s
}
