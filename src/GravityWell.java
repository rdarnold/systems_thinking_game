package gos;

import java.util.ArrayList;
import java.util.EnumSet;
import javafx.scene.paint.Color;

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
    
    public boolean isMaxSize() { return (getSize() == maxSize); }

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
        Color color = Color.rgb(0, 0, 0);
        setColor(color);
        //setEffect(Utils.createBorderGlow(color));
        
        // This is arbitrary
        maxSize = 300;
        if (from != null) {
            deepCopy(from);
            return;
        }
        setSize(30);
    }

    public int getGravityPull() {
        // We'll just make it a constant right now
        return 50;
    }

    public void centerInSim() {
        int x1 = Constants.SIM_CENTER_X; //- (int)getSize()/2;
        int y1 = Constants.SIM_CENTER_Y; // - (int)getSize()/2;
        
        moveTo(x1, y1);
    }

    // Make everything equal
    public void deepCopy(GravityWell from) {
        super.deepCopy(from);
        // Nothing specific for the GravityWell but we could put something 
        // if we were to have multiple wells
    }

    public void update() {
        
    }
}