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
public class Earthpatch extends MovableCircle { 
    
    boolean turningToSpike = false;
    boolean hit = false;

    // If we have just spawned, we have to wait a few frames
    // before spawning again just to prevent craziness.
    static int defaultSpawnTimer = 20;
    int spawnTimer = defaultSpawnTimer;

    public void setTurningToSpike() { turningToSpike = true; hit = true; }
    public boolean getTurningToSpike() { return turningToSpike; }
    public void setHit() { hit = true; }
    public boolean getHit() { return hit; }
    public boolean isMaxSize() { return (getSize() == maxSize); }

    public Earthpatch(Simulator s) {
        super(s);
        init(null);
    }
    
    public Earthpatch(Earthpatch from) {
        super(from.sim);
        init(from);
    }

    public Earthpatch(Simulator s, String strFrom) {
        super(s);
        init(null);
        setFromString(strFrom);
    }

    public void init(Earthpatch from) {
        // setRandomColor();
        Color color = Color.rgb(
            Data.currentValues.movingDotR, 
            Data.currentValues.movingDotG, 
            Data.currentValues.movingDotB);
        setColor(color);
        //setEffect(Utils.createBorderGlow(color));
        
        maxSize = Data.currentValues.movingDotSize;
        growthRate = 0.75;
        if (from != null) {
            deepCopy(from);
            return;
        }
        prepareNewSpawn();

        //setOnMouseClicked(t -> Utils.log("EARTHPATCH"));
        //setFill(pattern);
    }
    

    /*static Image hatch = createHatch();
    static ImagePattern pattern = new ImagePattern(hatch, 0.2, 0.2, 0.4, 0.4, true); 
    private static Image createHatch() {
        Pane pane = new Pane();
        pane.setPrefSize(5, 5);
        Line fw = new Line(0, 0, 5, 5);
        Line bw = new Line(0, 5, 5, 0);
        fw.setStroke(Color.SKYBLUE);
        bw.setStroke(Color.SKYBLUE);
        fw.setStrokeWidth(1);
        bw.setStrokeWidth(1);
        Rectangle rect = new Rectangle(0, 0, 5, 5);
        rect.setFill(Color.RED);
        pane.getChildren().addAll(rect, fw, bw);
        new Scene(pane);
        return pane.snapshot(null, null);
    }*/

    // Make everything equal
    public void deepCopy(Earthpatch from) {
        super.deepCopy(from);
        hit = from.hit;
        turningToSpike = from.turningToSpike;
        spawnTimer = from.spawnTimer;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(Constants.PATCH_KEY_STRING); // @E
        sb.append(" x:" + (int)getCenterX());
        sb.append(" y:" + (int)getCenterY());
        sb.append(" vx:" + (int)getXSpeed());
        sb.append(" vy:" + (int)getYSpeed());
        sb.append(" s:" + (int)getSize());
        sb.append(" i:" + spawnTimer);
        sb.append(" h:" + (hit ? 1 : 0));
        sb.append(" t:" + (turningToSpike  ? 1 : 0));
        
        return sb.toString();
    }

    // The parallel to the above toString
    public boolean setFromString(String str) {
        int var = 0;

        int x = Utils.getIntFromKey(str, "x:");
        int y = Utils.getIntFromKey(str, "y:");
        moveTo(x, y);

        int vx = Utils.getIntFromKey(str, "vx:");
        int vy = Utils.getIntFromKey(str, "vy:");
        if (vx >= 0 && vy >= 0) {
            setSpeed(vx, vy);
        }

        var = Utils.getIntFromKey(str, "s:");
        if (var >= 0) { 
            setSize(var); 
        }
        
        var = Utils.getIntFromKey(str, "i:");
        spawnTimer = (var > 0 ? var : 0);

        var = Utils.getIntFromKey(str, "h:");
        hit = (var == 1); 

        var = Utils.getIntFromKey(str, "t:");
        turningToSpike = (var == 1); 

        return true;
    }

    public void prepareNewSpawn() {
        hit = false;
        turningToSpike = false;
        spawnTimer = defaultSpawnTimer;
        setSize(6);
        setSpeed(Utils.random(-2, 2), Utils.random(-2, 2));
    }

    // They should need something to spawn.  I have to limit
    // this growth somehow.  Or when they spawn they are smaller.
    public void spawnNewEarthpatch(Earthpatch patch) {
        if (spawnTimer > 0 || patch.spawnTimer > 0) {
            return;
        }
        // The two old ones are eliminated and three new ones 
        // emerge moving at different rates.
        hit = true;
        patch.hit = true;
        double x = (getCenterX() + patch.getCenterX()) / 2;
        double y = (getCenterY() + patch.getCenterY()) / 2;

        // They should all be moving away from center.
        // We should average the colors somehow.
        /*Color thisCol = (Color)getFill();
        Color otherCol = (Color)patch.getFill();

        int red = (int)(thisCol.getRed() + otherCol.getRed()) / 2;
        int green = (int)(thisCol.getGreen() + otherCol.getGreen()) / 2;
        int blue = (int)(thisCol.getBlue() + otherCol.getBlue()) / 2;

        red = Utils.clampColor(red);
        green = Utils.clampColor(green);
        blue = Utils.clampColor(blue);
        Color color = Color.rgb(red, green, blue);*/
    
        int space = 10;
        Earthpatch newPatch;

        newPatch = new Earthpatch(this);
        newPatch.prepareNewSpawn();
        newPatch.moveTo(x - space, y + space);
        //newPatch.setColor(color);
        sim.addEarthpatch(newPatch);

        newPatch = new Earthpatch(this);
        newPatch.prepareNewSpawn();
        newPatch.moveTo(x + space, y + space);
        //newPatch.setColor(color);
        sim.addEarthpatch(newPatch);

        newPatch = new Earthpatch(this);
        newPatch.prepareNewSpawn();
        newPatch.moveTo(x, y - space);
        //newPatch.setColor(color);
        sim.addEarthpatch(newPatch);
    }
 
    public boolean checkEarthCollisions(ArrayList<Earthpatch> patches) {
        if (isMaxSize() == false || hit == true || spawnTimer > 0) {
            return false;
        }

        // If two earthpatches are at full size and are colliding, we spawn a new shape!
        for (Earthpatch patch : patches) {
            if (patch.isMaxSize() == false) {
                continue;
            }
            if (this.equals(patch) == true) {
                continue;
            }
            if (patch.hit == true) {
                continue;
            }
            if (intersects(patch) == true) {
                spawnNewEarthpatch(patch);  
                return true;
            }
        }
        return false;
    }

    public void hitSpike(Spike spike) {
        setHit();
        //spike.setHit();
    }

    public boolean checkSpikeCollisions(ArrayList<Spike> spikes) {
        // If two earthpatches are at full size and are colliding, we spawn a new shape!
        for (Spike spike : spikes) {
            if (spike.getHit() == true) {
                continue;
            }
            if (intersects(spike) == true) {
                // Self-immolate.
                hitSpike(spike);
                return true;
            }
        }
        return false;
    }

    public void update() {
        if (hit == true) {
            setSize(getSize() - growthRate * 2);
            if (getSize() < 0) {
                setSize(0);
            }
            return;
        }

        spawnTimer--;
        if (spawnTimer < 0) {
            spawnTimer = 0;
        }
        moveOneFrame();
        checkSpikeCollisions(sim.spikes);
        if (getSize() >= maxSize) {
            setSize(maxSize);
            //checkEarthCollisions(sim.patches);
            return;
        }
        // Move down a bit
        //moveBy(0, fallingRate);
        setSize(getSize() + growthRate);
        if (getSize() >= maxSize) {
            setSize(maxSize);
        }
    }

    public Spike spawnSpike() {
        Spike spike = new Spike(sim);
        spike.moveTo(getCenterX(), getCenterY());
        spike.setSpeed(xSpeed, ySpeed);
        return spike;
    }

    public void absorbRain(Raindrop drop) {
        // If no growth, we don't make spikes
        if (Data.currentValues.growthRules == Constants.GrowthRules.NoGrowth.getValue())
            return;
        setTurningToSpike();
    }
    
    public void moveOneFrame() {
        if (move() == true) {
            checkEarthCollisions(sim.patches);

            // If it hit a wall we need to bounce.
            if (getCenterX() - getSize()/2 < 0 && xSpeed < 0) {
                xSpeed *= -1;
            }
            if (getCenterX() + getSize()/2 > sim.width && xSpeed > 0) {
                xSpeed *= -1;
            }
            if (getCenterY() - getSize()/2 < 0 && ySpeed < 0) {
                ySpeed *= -1;
            }
            if (getCenterY() + getSize()/2 > sim.height && ySpeed > 0) {
                ySpeed *= -1;
            }
        }
    }
}