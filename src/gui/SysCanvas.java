package gos.gui;

import java.io.*;
import java.util.*;
import java.util.ArrayList;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.canvas.*;
import javafx.scene.effect.DropShadow;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.Node;
import javafx.geometry.Pos;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.ListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ListChangeListener;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Priority;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Circle;

import gos.*;

public class SysCanvas extends Canvas {

    Simulator sim = null;
    GravityWell draggingWell = null; // Keep track of a well if we're dragging it

    public SysCanvas(Simulator s, double wid, double hgt) {
        super(wid, hgt);
        sim = s;
        create();
    }

    public void create() {
        /*VBox.setVgrow(this, Priority.ALWAYS);

        // Add our outline to the playing area
        Line line = new Line(Constants.SIM_WIDTH, 0, Constants.SIM_WIDTH, Constants.SIM_HEIGHT);
        line.setStrokeWidth(4);
        getChildren().add(line);
        
        line = new Line(0, Constants.SIM_HEIGHT, Constants.SIM_WIDTH, Constants.SIM_HEIGHT);
        line.setStrokeWidth(4);
        getChildren().add(line);

        // And beyond the lines I guess I'll just add like a big gray shape or something... kind of
        // weird but should work.
        Rectangle rec = new Rectangle();
        rec.setFill(Color.LIGHTGRAY);
        rec.setStroke(Color.LIGHTGRAY);
        rec.setX(Constants.SIM_WIDTH);
        rec.setY(0);
        rec.setWidth(1200);
        rec.setHeight(1200);
        getChildren().add(rec);

        rec = new Rectangle();
        rec.setFill(Color.LIGHTGRAY);
        rec.setStroke(Color.LIGHTGRAY);
        rec.setX(0);
        rec.setY(Constants.SIM_HEIGHT);
        rec.setWidth(1200);
        rec.setHeight(1200);
        getChildren().add(rec);*/
        
        EventHandler filter2 = 
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        /*sim.updateCanvasMouseCoordinates(event.getX(), event.getY());
                        sim.screenMouseX = event.getScreenX();
                        sim.screenMouseY = event.getScreenY();
                        sim.sceneMouseX = event.getSceneX();
                        sim.sceneMouseY = event.getSceneY();*/
                        //System.out.println("H: " + spCanvasContainer.getHvalue() + " Y:" + spCanvasContainer.getVvalue());
                        //System.out.println("Hm: " + spCanvasContainer.getHmax() + " Ym:" + spCanvasContainer.getVmax());
                        //System.out.println("Move: " + canvasMouseX + " Y:" + canvasMouseY);
                    }
                };
        this.addEventFilter(MouseEvent.ANY, filter2);

        this.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                onMouseClick(event.getX(), event.getY());
                /*if (event.getButton() == MouseButton.PRIMARY) {
                    //scenario.leftClick(event.getX(), event.getY());
                    event.consume();
                }
                else if (event.getButton() == MouseButton.SECONDARY) {
                    //scenario.rightClick(event.getX(), event.getY());
                }*/
            }
        });

        this.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                onMousePressed(event.getX(), event.getY());
                /*if (event.getButton() == MouseButton.PRIMARY) {
                    //scenario.leftClick(event.getX(), event.getY());
                    event.consume();
                }
                else if (event.getButton() == MouseButton.SECONDARY) {
                }*/
            }
        });

        this.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                onMouseDragged(event.getX(), event.getY());
                /*if (event.isPrimaryButtonDown()) {
                   // scenario.onMouseDrag(event.getX(), event.getY());
                    event.consume();
                }
                else if (event.isSecondaryButtonDown()) {
                }*/
            }
        });

        this.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                draggingWell = null;
                //onMouseDragged(event.getX(), event.getY());
                /*if (event.isPrimaryButtonDown()) {
                   // scenario.onMouseDrag(event.getX(), event.getY());
                    event.consume();
                }
                else if (event.isSecondaryButtonDown()) {
                }*/
            }
        });

        /*this.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //scenario.onMouseMove(event.getX(), event.getY());
                event.consume();
            }
        });*/
    }

    private boolean withinClick(double x, double y, MovablePolygon poly) {
        // Was the shape clicked on?  We'll just do a box around
        // each shape to keep it simple
        int leftX = (int)poly.getCenterX() - (int)poly.getRadius();
        int rightX = (int)poly.getCenterX() + (int)poly.getRadius();
        int topY = (int)poly.getCenterY() - (int)poly.getRadius();
        int bottomY = (int)poly.getCenterY() + (int)poly.getRadius();

        if (x >= leftX && x <= rightX && y >= topY && y <= bottomY) {
            return true;
        }
        return false;
    }

    private boolean withinClick(double x, double y, MovableCircle circle) {
        // Was the shape clicked on?  We'll just do a box around
        // the circle to keep it simple
        int leftX = (int)circle.getCenterX() - (int)circle.getRadius();
        int rightX = (int)circle.getCenterX() + (int)circle.getRadius();
        int topY = (int)circle.getCenterY() - (int)circle.getRadius();
        int bottomY = (int)circle.getCenterY() + (int)circle.getRadius();

        if (x >= leftX && x <= rightX && y >= topY && y <= bottomY) {
            return true;
        }
        return false;
    }

    private void onMouseClick(double x, double y) {
        // See if we clicked on a shape
        if (sim == null) {
            return;
        }
        for (SysShape shape : sim.getShapes()) {
            if (withinClick(x, y, shape) == true) {
                Gos.gos.onClickShape(shape);
                return;
            }
        }
    }

    // Mouse pressed right now is just for the a gravity well
    private void onMousePressed(double x, double y) {
        // See if we clicked on a gravity well
        if (sim == null) {
            return;
        }
        for (GravityWell well : sim.getGravityWells()) {
            if (withinClick(x, y, well) == true) {
                draggingWell = well;
                well.onMousePressed((int)x, (int)y);
                return;
            }
        }
    }
    
    private void onMouseDragged(double x, double y) {
        if (sim == null) {
            return;
        }
        if (draggingWell != null) {
            draggingWell.onMouseDragged(x, y);
        }
    }

    /*
    private void drawShapes(GraphicsContext gc) {
        gc.setFill(Color.GREEN);
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(5);
        gc.strokeLine(40, 10, 10, 40);
        gc.fillOval(10, 60, 30, 30);
        gc.strokeOval(60, 60, 30, 30);
        gc.fillRoundRect(110, 60, 30, 30, 10, 10);
        gc.strokeRoundRect(160, 60, 30, 30, 10, 10);
        gc.fillArc(10, 110, 30, 30, 45, 240, ArcType.OPEN);
        gc.fillArc(60, 110, 30, 30, 45, 240, ArcType.CHORD);
        gc.fillArc(110, 110, 30, 30, 45, 240, ArcType.ROUND);
        gc.strokeArc(10, 160, 30, 30, 45, 240, ArcType.OPEN);
        gc.strokeArc(60, 160, 30, 30, 45, 240, ArcType.CHORD);
        gc.strokeArc(110, 160, 30, 30, 45, 240, ArcType.ROUND);
        gc.fillPolygon(new double[]{10, 40, 10, 40},
                       new double[]{210, 210, 240, 240}, 4);
        gc.strokePolygon(new double[]{60, 90, 60, 90},
                         new double[]{210, 210, 240, 240}, 4);
        gc.strokePolyline(new double[]{110, 140, 110, 140},
                          new double[]{210, 210, 240, 240}, 4);
    }*/

    
    public void updateOneFrame(boolean running, Turn currentTurn) {
        sim = Gos.sim;
        draw();
    }

    public void draw() {
        if (sim == null) {
            return;
        }
        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());

        // Draw a border around the canvas
        drawBorder(gc);

        // And now just draw everything directly from the simulator
        for (Raindrop item : sim.getDrops()) {
            drawMovableCircle(gc, item);
        }
        for (Earthpatch item : sim.getPatches()) {
            drawMovableCircle(gc, item);
        }
        for (SysShape item : sim.getShapes()) {
            drawMovablePolygon(gc, item);
        }
        for (Spike item : sim.getSpikes()) {
            drawMovablePolygon(gc, item);
        }
        for (GravityWell item : sim.getGravityWells()) {
            // And the dropshadow
            if (item.getDropShadow() != null) {
                gc.setEffect(item.getDropShadow());
            }
            drawMovableCircle(gc, item);
            gc.setEffect(null);
        }
    }

    /*private void drawRectangle(GraphicsContext gc, Rectangle rect) {
        gc.setFill(Color.WHITESMOKE);
        gc.fillRect(rect.getX(),      
                    rect.getY(), 
                    rect.getWidth(), 
                    rect.getHeight());
        gc.setFill(Color.GREEN);
        gc.setStroke(Color.BLUE);
    }*/

    private void drawMovableCircle(GraphicsContext gc, MovableCircle circle) {
        gc.setFill(circle.getFill());
        //gc.setStroke(circle.getStroke());
        gc.fillOval(circle.getCenterX()-circle.getRadius(), circle.getCenterY()-circle.getRadius(), circle.getRadius()*2, circle.getRadius()*2);
        //gc.strokeOval(circle.getCenterX()-circle.getRadius(), circle.getCenterY()-circle.getRadius(), circle.getRadius()*2, circle.getRadius()*2);
    }

    private void drawMovablePolygon(GraphicsContext gc, MovablePolygon poly) {
        //poly.getPoints
        //getPoints().set(0);
        //getPoints().set(1);
        gc.setFill(poly.getFill());
        gc.setStroke(poly.getStroke());
        gc.setLineWidth(poly.getStrokeWidth());
        gc.fillPolygon(poly.getXPoints(), poly.getYPoints(), poly.getNumPoints());
        gc.strokePolygon(poly.getXPoints(), poly.getYPoints(), poly.getNumPoints());

        gc.setStroke(Color.BLACK);
        gc.setFill(Color.BLACK);
        if (poly.getShapeText() != null && poly.getShapeText().getText() != null && poly.getShapeText().getText().equals("") == false) {
            gc.fillText(poly.getShapeText().getText(), poly.getCenterX() - 3, poly.getCenterY() + 5);
        }

        Circle circle = poly.getSelectedCircle();
        if (circle != null && circle.isVisible() == true) {
            gc.setStroke(Color.RED);
            gc.setLineWidth(1);
            gc.strokeOval(circle.getCenterX()-circle.getRadius(), circle.getCenterY()-circle.getRadius(), circle.getRadius()*2, circle.getRadius()*2);
        }
    }
    
    private void drawBorder(GraphicsContext gc) {
        final double canvasWidth = getWidth();
        final double canvasHeight = getHeight();
    
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(4);
        gc.strokeRect(0, 0, canvasWidth, canvasHeight);
        gc.setLineWidth(1);
    }
}