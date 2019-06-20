package gos;

import java.util.ArrayList;
import java.util.EnumSet;

// A single drop of rain.  Woot.
public class Spike extends MovablePolygon { 

    boolean hit = false;
    
    public void setHit() { hit = true; }
    public boolean getHit() { return hit; }

    public Spike(Simulator s) {
        super(s);
        init(null);
    }
    
    public Spike(Spike from) {
        super(from.sim);
        init(from);
    }

    public void init(Spike from) {
        maxSize = 150;
        targetSize = 25;
        polyType = MovablePolygon.PolyType.Spike;
        // Create a sort of small polygon thing I guess.  Could just use
        // circles too.
        if (from != null) {
            deepCopy(from);
            return;
        }

        // A six point star basically
        for (int i = 0; i < 18; i++) {
            // Just plop them all in the center then move them.
            addPoint(0, 0);
        }
        setAngleDegrees(Utils.number(0, 359));
        setSize(5);

        //setOnMouseClicked(t -> Utils.log("SPIKE"));
    }

    // Make everything equal
    public void deepCopy(Spike from) {
        super.deepCopy(from);
        hit = from.hit;
    }

    double sizeIncrement = 4;
    public void absorbRain(Raindrop drop) {
        targetSize += sizeIncrement;
        if (targetSize > maxSize) {
            targetSize = maxSize;
        }
        /*targetSize -= sizeIncrement;
        if (targetSize <= 0) {
            targetSize = 0;
        }*/
    }
    
    public void update() {
        if (hit == true) {
            setSize(getSize() - growthRate * 2);
            if (getSize() < 0) {
                setSize(0);
            }
            return;
        }

        moveOneFrame();
        
        /*if (getSize() >= maxSize) {
            setSize(maxSize);
            return;
        }*/

        // Handle various types of growth
        growTowardsTargetSize();
    }

    public void hitSpike(Spike spike) {
        // Largest one kills smaller
        if (getSize() >= spike.getSize()) {
            targetSize -= spike.getSize() / 2;
            spike.setHit();
        }
        else {
            spike.targetSize -= getSize() / 2;
            setHit();
        }

        if (targetSize <= 0) {
            setHit();
        }
        if (spike.targetSize <= 0) {
            spike.setHit();
        }


        // The largest one wins!
        /*targetSize -= spike.getSize();
        spike.targetSize -= getSize();

        if (targetSize <= 0) {
            setHit();
        }
        else {
            spike.setHit();
        }*/
    }

    // Earthpatches damage spikes too although
    // they don't tend to destroy them.
    public void hitPatch(Earthpatch patch) {
        // Actually no, let's have spikes just
        // tear through patches.
        /*targetSize -= patch.getSize();
        if (targetSize <= 0) {
            setHit();
        }*/
    }
    
    public boolean checkSpikeCollisions(ArrayList<Spike> spikes) {
        for (Spike spike : spikes) {
            if (this.equals(spike) == true) {
                continue;
            }
            if (spike.hit == true) {
                continue;
            }
            if (intersects(spike) == true) {
                hitSpike(spike);
                return true;
            }
        }
        return false;
    }
    
    public boolean checkEarthCollisions(ArrayList<Earthpatch> patches) {
        if (hit == true) {
            return false;
        }

        for (Earthpatch patch : patches) {
            if (patch.hit == true) {
                continue;
            }
            if (intersects(patch) == true) {
                patch.hitSpike(this);
                hitPatch(patch);
                return true;
            }
        }
        return false;
    }

    public void moveOneFrame() {
        if (move() == true) {
            checkEarthCollisions(sim.patches);
            checkSpikeCollisions(sim.spikes);

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