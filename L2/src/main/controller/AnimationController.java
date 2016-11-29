package main.controller;


import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

/**
 * Created by a1 on 02.11.16.
 */
class AnimationController {

    private int i = 100;

    void create(AnchorPane pane) {
        Canvas canvas = new Canvas(pane.getWidth(), pane.getHeight());
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        drawShapes(graphicsContext);

        pane.getChildren().add(canvas);
    }

    private void drawShapes(GraphicsContext graphicsContext) {
        graphicsContext.setStroke(Color.BLUE);
        graphicsContext.setLineWidth(5);

        /* TODO draw cicrcle with single line */
        new Thread(() -> {
            while (true) {
                graphicsContext.strokeLine(i, 100, 10, 40);
                try {
                    Thread.sleep(250);
                    ++i;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private void resetCanvas(GraphicsContext graphicsContext) {
        Canvas canvas = graphicsContext.getCanvas();
        graphicsContext.setFill(Color.web("#F6F6F6"));

        graphicsContext.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }
}
