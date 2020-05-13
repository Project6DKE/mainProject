package MainProject;

import javafx.scene.transform.Translate;

public enum ObjectType {
    GRASS("bunchOfGrass", 3),

    TREES("trees", 25),

    ARROW("arrow", 5),

    FLAG("flag", 30);

    private final double scalingFactor;
    private final String pathName;

    private Translate translationLocation;

    ObjectType(String pathName, double scalingFactor) {
        this.pathName = pathName;
        this.scalingFactor = scalingFactor;
    }

    public String getPathName() {
        return pathName;
    }

    public double getScalingFactor() {
        return scalingFactor;
    }

    public void setTranslate(double x, double y, double z) {
        translationLocation = new Translate(x, y, z);
    }

    public Translate getTranslate() {
        return translationLocation;
    }
}