package gos;

import java.util.ArrayList;
import java.util.EnumSet;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod; 
import javafx.scene.paint.RadialGradient;  
import javafx.scene.paint.Stop;
import javafx.scene.input.MouseButton;

// All this stuff just to make our personal color for our selected shape
/*import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;*/

// Could extend from a MovableCircle class instead.
public class GravityWell extends MovableCircle {

    public boolean isMaxSize() {
        return (getSize() == maxSize);
    }

    public static int DEFAULT_SIZE = 30;
    public static int DEFAULT_GRAVITY_PULL = 50;

    private int dragDeltaX;
    private int dragDeltaY;

    public GravityWell(Simulator s) {
        super(s);
        init(null);
    }

    public GravityWell(GravityWell from) {
        super(from.sim);
        init(from);
    }

    public void init(GravityWell from) {
        // setRandomColor();
        /*
         * Color color = Color.BEIGE; //Color.rgb(0, 0, 0); setColor(color);
         * setStroke(Color.BLACK); setStrokeWidth(8);
         */
        // setEffect(Utils.createBorderGlow(color));

        setColor(Color.BLACK);

        /*
         * Stop[] stops = new Stop[] { new Stop(0.0, Color.WHITE), new Stop(0.3,
         * Color.RED), new Stop(1.0, Color.DARKRED) }; RadialGradient radialGradient =
         * new RadialGradient(0, 0, 300, 178, 60, false, CycleMethod.NO_CYCLE, stops);
         * //Setting the radial gradient to the circle and text setFill(radialGradient);
         */

        // Add mouse event handlers for the source
        setOnMousePressed(event -> {
            //Utils.log("GRAVITY");
            GravityWell well = (GravityWell) event.getSource();
            // event.setDragDetect(true);
            dragDeltaX = (int) well.getCenterX() - (int) event.getScreenX();
            dragDeltaY = (int) well.getCenterY() - (int) event.getScreenY();
            // setCursor(Cursor.MOVE);
        });

        //setOnMouseReleased(event -> { });

        setOnMouseDragged(event -> {
            if (Gos.playerCanChangeSystem() == false) {
                return;
            }
            moveTo(event.getScreenX() + dragDeltaX, event.getScreenY() + dragDeltaY);
        });

        //setOnDragDetected(event -> { });

        if (from != null) {
            deepCopy(from);
            return;
        }

        // This is arbitrary
        maxSize = 300;
        setSize(GravityWell.DEFAULT_SIZE);
    }

    public int getGravityPull() {
        // We'll just make it a constant right now
        return GravityWell.DEFAULT_GRAVITY_PULL;
    }

    public void setPosToDefaults() {
        int x1 = Data.startingValues.gravityWellCenterX;
        int y1 = Data.startingValues.gravityWellCenterY;
        
        moveTo(x1, y1);
    }

    public void centerInSim() {
        int x1 = Constants.SIM_CENTER_X; //- (int)getSize()/2;
        int y1 = Constants.SIM_CENTER_Y; // - (int)getSize()/2;
        
        moveTo(x1, y1);
    }

    // Make everything equal
    public void deepCopy(GravityWell from) {
        super.deepCopy(from);
        // Nothing specific for the GravityWell 
    }

    public void update() {
        
    }
}