package MainProject;

import javafx.scene.Node;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import javafx.animation.*;

public class IntroTransition {
    Object cam;
    Object rotatingObject;

    public IntroTransition(Object rotatingObject, Object cam) {
        this.cam = cam;
        this.rotatingObject = rotatingObject;
    }

    public void play() {
        doDescendingIntroTransition();
        doRotateIntroTransition();
    }

    private void doRotateIntroTransition() {
        RotateTransition rotateTransition = new RotateTransition();
        rotateTransition.setAxis(Rotate.Y_AXIS);
        rotateTransition.setFromAngle(45);
        rotateTransition.setToAngle(110);
        rotateTransition.setDuration(Duration.seconds(5));
        rotateTransition.setCycleCount(1);
        rotateTransition.setNode((Node) rotatingObject);
        rotateTransition.play();
    }

    private void doDescendingIntroTransition() {
        TranslateTransition descendingIntroTransition = new TranslateTransition();
        descendingIntroTransition.setDuration(Duration.seconds(3.6));
        descendingIntroTransition.setFromY(800);
        descendingIntroTransition.setToY(1600);
        descendingIntroTransition.setCycleCount(1);
        descendingIntroTransition.setNode((Node) cam);
        descendingIntroTransition.play();
    }
}