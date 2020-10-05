package gos;

import java.util.ArrayList;
import java.util.EnumSet;

/* 
   A set of changes made either when proceeding to the next turn,
   or when creating an experiment.
   It tells us two things:
     1:  What did we change this time from last time
     2:  What are all the differences between this set and the default

   We may not need this so much if a player can only make one change per turn,
   but if we start to allow more changes it'll be good to have this in place.
*/
public class ChangeSet { 
    private static int nextId = 0;
    private int id;

    public int getId() { return id; }

    // Actually this isnt that hard because we store all our data in the Values
    // class anyway.  So we can just use that.
    private Values oldSet;
    private Values newSet;
    private Values changed;

    private SysShape oldShape;
    private SysShape newShape;

    public Values getNewValues() { return newSet; }
    public SysShape getNewShape() { return newShape; }

    ///////////////////////////////////////////
    ///////////////////////////////////////////
    // Variables ONLY for data loading
    ///////////////////////////////////////////
    ///////////////////////////////////////////
    // This is how we can tell if a shape changed or not even if we
    // don't have a prior shape to compare to at the time of checking
    ///////////////////////////////////////////
    boolean numShapeCornersChanged = false;
    boolean shapeSpinSpeedChanged = false;
    boolean shapeSpinDirChanged = false;
    boolean shapeColorChanged = false;
    
    public boolean getShapeNumCornersChanged() { return numShapeCornersChanged; }
    public boolean getShapeSpinSpeedChanged() { return shapeSpinSpeedChanged; }
    public boolean getShapeSpinDirChanged() { return shapeSpinDirChanged; }
    public boolean getShapeColorChanged() { return shapeColorChanged; }

    public void setShapeNumCornersChanged() { numShapeCornersChanged = true; }
    public void setShapeSpinSpeedChanged() { shapeSpinSpeedChanged = true; }
    public void setShapeSpinDirChanged() { shapeSpinDirChanged = true; }
    public void setShapeColorChanged() { shapeColorChanged = true; }
    ///////////////////////////////////////////
    ///////////////////////////////////////////
    // End Variables ONLY for data loading
    ///////////////////////////////////////////
    ///////////////////////////////////////////
    
    public ChangeSet() {
        // We would only want to use this constructor when we're loading
        // for data analysis    
    }

    public ChangeSet(Values oldv, SysShape olds) {
        // Just constantly increment the change sets.  But provide ids, so that
        // we can reference them if necessary from different parts of our log
        // file.
        id = nextId;
        nextId++;

        // Create objects to just keep track of before/after.
        oldSet = new Values(oldv);

        // The player might have lost all shapes for some reason.  If no shape is
        // selected we can't record any changes for it because none happened.
        oldShape = null;
        if (olds != null) {
            oldShape = new SysShape(olds);
        }
    }

    public void setNew(Values newv, SysShape news) {
        // Create objects to just keep track of before/after.
        newSet = new Values(newv);

        // And record our changed values.
        changed = new Values(newSet);
        changed.setChanged(oldSet);

        newShape = null;
        if (news != null) {
            newShape = new SysShape(news);
        }
    }

    public String toString() {

        StringBuilder sb = new StringBuilder();
        // OK print this out to a string, lets keep it super lean.
        sb.append("!C"); // Changeset starter
        sb.append("\r\n");

        // One line for values
        sb.append(changed.toString());
        sb.append("\r\n");

        // One line for shape changes, might be nice to put these
        // into some other class like I did with the Values changes
        // If we don't have an old or new shape, changes cannot have occurred
        // by definition since there was nothing to change from/to
        if (oldShape != null && newShape != null) {
            sb.append("S");
            if (oldShape.getNumCorners() != newShape.getNumCorners())
                sb.append(" A" + newShape.getNumCorners());
            if (oldShape.getSize() != newShape.getSize())
                sb.append(" B" + newShape.getSize());
            if (oldShape.getSpinSpeed() != newShape.getSpinSpeed())
                sb.append(" C" + newShape.getSpinSpeed());
            if (oldShape.getSpin() != newShape.getSpin())
                sb.append(" D" + newShape.getSpin());
            if (oldShape.getFill() != newShape.getFill()) {
                /*Color c = (Color)newShape.getFill();
                int r = (int)(c.getRed() * 255);
                int g = (int)(c.getGreen() * 255);
                int b = (int)(c.getBlue() * 255);*/
                sb.append(" E" + newShape.getFill());
                // We want the color showing as rgb, not hex, so we can
                // easily tell what it is - not that it actually matters
                //sb.append(" E" + r + "," + g + "," + b);
            }
            sb.append("\r\n");
        }

        return sb.toString();
    }
    
    // Loading from our data sets generated by players
    public void fromString(String loadedStr) {
        // Looks something like this:
        /* 
            !C
            V C1.0 P2 R12 S12
            S A3 B25.0 C4.0 Dtrue E0x76db4eff
        */

        // When we're loading, we only know what changed
        String lines[] = loadedStr.split("\\r?\\n");
        for (String line : lines) {
            if (line.charAt(0) == '!') {
                continue;
            }
            if (line.charAt(0) == 'V') {
                // easy peesy
                changed = new Values();
                changed.setFromString(line);
            }
            if (line.charAt(0) == 'S') {
                setShapeChangedFromStr(line);
            }
        }
    }
    
    private void setShapeChangedFromStr(String str) {
        //  S A3 B25.0 C4.0 Dtrue E0x76db4eff
        
        // We don't even care what the changes were.. just whether or not the change occurred
        String lines[] = str.split(" ");
        for (String line : lines) {
            char start = line.charAt(0);
            if (start == 'A') {
                setShapeNumCornersChanged();
            }
            // Don't care about size being changed.. these are things the player
            // changed
            /*else if (start == 'B') {
                setSizeChanged(boolean change);
            }*/
            else if (start == 'C') {
                setShapeSpinSpeedChanged();
            }
            else if (start == 'D') {
                setShapeSpinDirChanged();
            }
            else if (start == 'E') {
                setShapeColorChanged();
            }
        }
    }

    // Check if we have a change for the passed in VariableType
    public boolean wasChanged(Constants.VariableType varType) {
        switch (varType) {
            case RainRate:
                return (changed.rainRate != null);
            case GravityWellLocation:
                return (changed.gravityWellCenterX != null || changed.gravityWellCenterY != null);
            case GravityDirection:
                return (changed.gravityRules != null);
            case Growth:
                return (changed.growthRules != null);
            case Paradigm:
                return (changed.paradigm != null);
            case ShapeSpinSpeed:
                return getShapeSpinSpeedChanged();
            case ShapeSpinDirection:
                return getShapeSpinDirChanged();
            case ShapeType:
                return getShapeNumCornersChanged();
            case ShapeColor:
                return getShapeColorChanged();
        }
        return false;
    }
}