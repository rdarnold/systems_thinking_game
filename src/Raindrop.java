package gos;

import java.util.ArrayList;
import java.util.EnumSet;

// A single drop of rain.  Woot.
public class Raindrop extends MovableCircle { 

    // How fast the drops fall down the screen
    double fallingRate = 1;
    Constants.Dir fallingDirection = Constants.Dir.Bottom;

    public Raindrop(Simulator s) {
        super(s);
        init(null);
    }
    
    public Raindrop(Raindrop from) {
        super(from.sim);
        init(from);
    }

    public void init(Raindrop from) {
        // Create a sort of small polygon thing I guess.  Could just use
        // circles too.
        if (from != null) {
            deepCopy(from);
            return;
        }
        setSize(3);
    }

    public void setDir(Constants.Dir dir) {
        fallingDirection = dir;
    }

    public double getFallingRate() { return fallingRate; }
    public Constants.Dir getDir() { return fallingDirection; }

    public void setFall(Constants.Dir dir, double rate) { 
        setDir(dir);
        fallingRate = rate; 
        switch (fallingDirection) {
            case Bottom:
                setSpeed(0, fallingRate);
                break;
            case Top:
                setSpeed(0, fallingRate * -1);
                break;
            case Left:
                setSpeed(fallingRate * -1, 0);
                break;
            case Right:
                setSpeed(fallingRate, 0);
                break;
        }
    }

    public boolean update() {
        // Move a bit
        if (move() == false) {
            // Somehow we ran out of velocity, so just self-terminate.
            reachEnd();
            return false;
        }
        switch (fallingDirection) {
            case Bottom:
                if (getCenterY() > sim.height) {
                    reachEnd();
                    return false;
                }
                break;
            case Top:
                if (getCenterY() < 0) {
                    reachEnd();
                    return false;
                }
                break;
            case Left:
                if (getCenterX() < 0) {
                    reachEnd();
                    return false;
                }
                break;
            case Right:
                if (getCenterX() > sim.width) {
                    reachEnd();
                    return false;
                }
                break;
        }
        return true;
    }

    // Make everything equal
    public void deepCopy(Raindrop from) {
        super.deepCopy(from);
        setFall(from.getDir(), from.getFallingRate());
    }

    public void reachEnd() {
        // Let's try spawning a thing.
        spawnEarthpatch();
    }

    public void spawnEarthpatch() {
        double x1 = getCenterX();
        double y1 = getCenterY();

        double x2 = getCenterX();
        double y2 = getCenterY();

        double minXSpeed = 0;
        double maxXSpeed = 0;
        double minYSpeed = 0;
        double maxYSpeed = 0;
        double var = getSize() * 2;
        switch (fallingDirection) {
            case Bottom:
                x1 -= var;
                x2 += var;
                y1 -= var;
                y2 -= var;
                minXSpeed = -2;
                maxXSpeed = 2;
                minYSpeed = -2;
                maxYSpeed = -1;
                break;
            case Top:
                x1 -= var;
                x2 += var;
                y1 += var;
                y2 += var;
                minXSpeed = -2;
                maxXSpeed = 2;
                minYSpeed = 1;
                maxYSpeed = 2;
                break;
            case Left:
                x1 += var;
                x2 += var;
                y1 += var;
                y2 -= var;
                minXSpeed = 1;
                maxXSpeed = 2;
                minYSpeed = -2;
                maxYSpeed = 2;
                break;
            case Right:
                x1 -= var;
                x2 -= var;
                y1 += var;
                y2 -= var;
                xSpeed = Utils.random(-2, -1);
                ySpeed = Utils.random(-2, 2);
                minXSpeed = -2;
                maxXSpeed = -1;
                minYSpeed = -2;
                maxYSpeed = 2;
                break;
        }

        // Now adjust according to moving dot speed, basically we just
        // multiply it by the rate, so make it "more or less significant"
        if (Data.currentValues.movingDotSpeedRate != 1) {
            minXSpeed *= Data.currentValues.movingDotSpeedRate;
            maxXSpeed *= Data.currentValues.movingDotSpeedRate;
            minYSpeed *= Data.currentValues.movingDotSpeedRate;
            maxYSpeed *= Data.currentValues.movingDotSpeedRate;
        }

        Earthpatch newPatch = new Earthpatch(sim);
        newPatch.moveTo(x1, y1);
        newPatch.setSpeed(Utils.random(minXSpeed, maxXSpeed), Utils.random(minYSpeed, maxYSpeed));
        sim.addEarthpatch(newPatch);

        newPatch = new Earthpatch(sim);
        newPatch.moveTo(x2, y2);
        newPatch.setSpeed(Utils.random(minXSpeed, maxXSpeed), Utils.random(minYSpeed, maxYSpeed));
        sim.addEarthpatch(newPatch);
    }
}