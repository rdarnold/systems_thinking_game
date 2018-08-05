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
            if (oldShape.getFill() != newShape.getFill())
                sb.append(" E" + newShape.getFill());
        }

        return sb.toString();
    }
}