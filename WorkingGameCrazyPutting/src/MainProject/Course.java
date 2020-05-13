package MainProject;

import javafx.scene.paint.*;
import javafx.scene.shape.*;

import javafx.scene.AmbientLight;
import javafx.scene.Group;

public class Course {
    private final TriangleMesh field = new TriangleMesh();
    private final TriangleMesh water = new TriangleMesh();

    private final int resolution = 100;
    private final int area = 5;

    private final Group course = new Group();
    private final PuttingSimulator PS;

    public Course(PuttingSimulator PS) {
        this.PS = PS;
    }

    public Group get() {
        setCourse();
        return course;
    }

    private void setCourse() {
        generateSurface();
        addTextures();
        addFaces();
        getMaterials();

        course.setRotate(180);
    }

    private void addTextures() {
        addTextureMesh(field);
        addTextureMesh(water);
    }

    private void addTextureMesh(TriangleMesh mesh) {
        for (float x = 0; x < resolution - 1; x++) {
            for (float y = 0; y < resolution - 1; y++) {
                float x0 = x / (float) resolution;
                float y0 = y / (float) resolution;
                float x1 = (x + 1) / (float) resolution;
                float y1 = (y + 1) / (float) resolution;

                mesh.getTexCoords().addAll(
                        x1, y1,
                        x1, y0,
                        x0, y1,
                        x0, y0
                );
            }
        }
    }

    private void addFaces() {
        addFacesMesh(field);
        addFacesMesh(water);
    }

    private void addFacesMesh(TriangleMesh mesh) {
        for (int x = 0; x < resolution - 1; x++) {
            for (int z = 0; z < resolution - 1; z++) {
                int p0 = x * resolution + z;
                int p1 = x * resolution + z + 1;
                int p2 = (x + 1) * resolution + z;
                int p3 = (x + 1) * resolution + z + 1;

                mesh.getFaces().addAll(p2, 0, p1, 0, p0, 0);
                mesh.getFaces().addAll(p2, 0, p3, 0, p1, 0);
            }
        }
    }

    private void generateSurface() {
        double stepSize = ((area * 2)) / ((float) (resolution));

        for (double x = -area; x <= area; x += stepSize) {
            for (double y = -area; y <= area; y += stepSize) {

                double z = PS.getCourse().get_height().evaluate(new Vector2d(x, y));
                addPointsToMeshes(x, y, z);
            }
        }
    }

    private void addPointsToMeshes(double x, double y, double z) {
        if (z > 0.01) {
            field.getPoints().addAll(
                    (int) (x * 100),
                    (int) (z * 200),
                    (int) (y * 100));
        } else {
            field.getPoints().addAll(
                    (int) (x * 100),
                    (int) (-0.01),
                    (int) (y * 100));
        }

        water.getPoints().addAll(
                (int) (x * 100),
                (int) (0),
                (int) (y * 100));
    }

    private void getMaterials() {
        Color grassColor = Color.GREEN;
        Color waterColor = Color.BLUE;

        PhongMaterial fieldMaterial = getMaterialWithColor(grassColor);
        PhongMaterial waterMaterial = getMaterialWithColor(waterColor);

        MeshView waterView = getMeshView(water, waterMaterial);
        MeshView meshView = getMeshView(field, fieldMaterial);

        AmbientLight ambientLight = basicAmbientLight();

        course.getChildren().add(ambientLight);
        course.getChildren().add(meshView);
        course.getChildren().add(waterView);
    }

    private AmbientLight basicAmbientLight() {
        AmbientLight ambientLight = new AmbientLight();
        ambientLight.setTranslateY(-1000);

        return ambientLight;
    }

    private PhongMaterial getMaterialWithColor(Color color) {
        PhongMaterial material = new PhongMaterial();
        material.setSpecularColor(color);
        material.setDiffuseColor(color);

        return material;
    }

    private MeshView getMeshView(TriangleMesh surface, PhongMaterial material) {
        MeshView meshView = new MeshView(surface);
        meshView.setMaterial(material);
        meshView.setCullFace(CullFace.NONE);
        meshView.setDrawMode(DrawMode.FILL);
        return meshView;
    }
}
