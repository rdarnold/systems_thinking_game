package gos;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.scene.Node;
import javafx.geometry.Bounds;

public class AnimationOperator {
    private Timeline timeline;

    public AnimationOperator() {
        this.timeline = new Timeline(60);
    }

    private double scale = 1.0;
    public void operate(Node node, double factor, double x, double y) {
        // determine scale
        double oldScale = node.getScaleX();
        scale = oldScale * factor;
        double f = (scale / oldScale) - 1;

        // determine offset that we will have to move the node
        Bounds bounds = node.localToScene(node.getBoundsInLocal());
        double dx = (x - (bounds.getWidth() / 2 + bounds.getMinX()));
        double dy = (y - (bounds.getHeight() / 2 + bounds.getMinY()));

        // timeline that scales and moves the node
        timeline.getKeyFrames().clear();

        timeline.getKeyFrames().addAll(
            new KeyFrame(Duration.millis(50), new KeyValue(node.translateXProperty(), node.getTranslateX() - f * dx)),
            new KeyFrame(Duration.millis(50), new KeyValue(node.translateYProperty(), node.getTranslateY() - f * dy)),
            new KeyFrame(Duration.millis(50), new KeyValue(node.scaleXProperty(), scale)),
            new KeyFrame(Duration.millis(50), new KeyValue(node.scaleYProperty(), scale))

            // can call instead, node.setTranslateX and node.setScaleX, etc.
        );
        timeline.play();
    }

    public double getScale() {
        return scale;
    }
}