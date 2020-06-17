package MainProject;

import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import javafx.scene.Group;
import javafx.scene.PointLight;
import javafx.scene.paint.Color;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;

import java.net.URL;

class DefaultBlenderObjects {
    public Group get() {
        Group trees = getTrees();
        Group arrow = getArrow();

        Group blenderObjects = new Group();
        blenderObjects.getChildren().add(arrow);
        blenderObjects.getChildren().add(trees);

        Group[] grassArray = getGrassArray();

        for (Group grassElement: grassArray) {
            blenderObjects.getChildren().add(grassElement);
        }

        PointLight pointLight = basicPointLight();

        blenderObjects.getChildren().add(pointLight);
        return blenderObjects;
    }

    private Group getTrees() {
        ObjectType treesType = ObjectType.TREES;
        treesType.setTranslate(-18, 1, 13);

        return new BlenderModel(treesType).get();
    }

    private Group getArrow() {
        ObjectType arrowType = ObjectType.ARROW;
        arrowType.setTranslate(0, -40, 3.3);
        Group arrow = new BlenderModel(arrowType).get();
        arrow.getTransforms().add(new Rotate(180, Rotate.X_AXIS));

        return arrow;
    }


    private Group[] getGrassArray() {
        Group grass = getGrass(-30, 0, 50);
        Group grass2 = getGrass(-60, 0, 50);
        Group grass3 = getGrass(-30, 0, 70);
        Group grass4 = getGrass(-45, 0, 40);
        Group grass5 = getGrass(-10, 0, 50);
        Group grass6 = getGrass(-40, 0, 50);
        Group grass7 = getGrass(-35, 0, 40);

        Group[] grassArray = {grass, grass2, grass3, grass4, grass5, grass6, grass7};

        return grassArray;
    }

    private Group getGrass(double x, double y, double z) {
        double translateX = 60 + x;
        double translateY =  y;
        double translateZ = 70 + z;

        ObjectType grassType = ObjectType.GRASS;
        grassType.setTranslate(translateX, translateY, translateZ);

        return new BlenderModel(grassType).get();
    }

    private PointLight basicPointLight() {
        PointLight pointLight = new PointLight();
        pointLight.setColor(Color.GRAY);
        pointLight.setTranslateY(pointLight.getTranslateY() - 100);
        pointLight.setOpacity(0.4);

        return pointLight;
    }
}

class BlenderModel {
    private final ObjectType objectType;

    public BlenderModel(ObjectType objectType) {
        this.objectType = objectType;
    }

    public Group get() {
        String pathName = objectType.getPathName();
        Group object = loadModel(getClass().getResource("Objects/" + pathName + ".obj"));

        double scaling = objectType.getScalingFactor();
        object.getTransforms().add(new Scale(scaling, scaling, scaling));

        object.getTransforms().add(new Rotate(0, Rotate.Y_AXIS));
        object.getTransforms().add(objectType.getTranslate());

        return object;
    }

    private Group loadModel(URL url) {
        Group modelRoot = new Group();

        ObjModelImporter importer = new ObjModelImporter();
        importer.read(url);

        for (MeshView view : importer.getImport()) {
            modelRoot.getChildren().add(view);
        }

        return modelRoot;
    }
}