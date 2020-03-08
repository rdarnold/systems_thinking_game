package gos;

import java.util.ArrayList;

public class SystemSnapshot {
    Values values = new Values();
    int currentTurnNumber = -1;
    int maxTurns = -1;
    int selectedShapeIndex = 0; // Which shape was selected when this snap was taken?

    public ArrayList<SysShape> shapes = new ArrayList<SysShape>();
    public ArrayList<Raindrop> drops = new ArrayList<Raindrop>();
    public ArrayList<Spike> spikes = new ArrayList<Spike>();
    public ArrayList<Earthpatch> patches = new ArrayList<Earthpatch>();
    public ArrayList<GravityWell> wells = new ArrayList<GravityWell>();

    /*public ArrayList<SysShape> shapes; 
    public ArrayList<Raindrop> drops; 
    public ArrayList<Spike> spikes; 
    public ArrayList<Earthpatch> patches;*/

    public SystemSnapshot() { }
    public SystemSnapshot(Simulator sim) {
        snap(sim);
    }

    public ArrayList<SysShape> getShapes() { return shapes; }
    public ArrayList<Raindrop> getDrops() { return drops; }
    public ArrayList<Spike> getSpikes() { return spikes; }
    public ArrayList<Earthpatch> getPatches() { return patches; }
    public ArrayList<GravityWell> getGravityWells() { return wells; }
    public Values getValues() { return values; }

    // Only used in data analyzer when we're loading up textual data
    public void setValuesTo(Values newValues) { values = newValues; } 

    public void clear() {
        shapes.clear();
        drops.clear();
        spikes.clear();
        patches.clear();
        wells.clear();
    }
    
    public void snap(Simulator sim) {
        clear();

        // Allocate them with exactly the right sizes from the get-go
        /*shapes = new ArrayList<SysShape>(sim.shapes.size());
        drops = new ArrayList<Raindrop>(sim.rainDrops.size());
        spikes = new ArrayList<Spike>(sim.spikes.size());
        patches = new ArrayList<Earthpatch>(sim.patches.size());*/

        //long time = System.currentTimeMillis();
        //Utils.log("Before " + time);
        // So we take a snapshot of the state of everything in the sim,
        // allowing us to restore it to an earlier state.
        // These have to be deep copies.
        //for (SysShape item : sim.shapes) {
        for (int i = 0; i < sim.shapes.size(); i++) {
            SysShape item = sim.shapes.get(i); 
            SysShape newItem = new SysShape(item);
            shapes.add(newItem);
        }
        //Utils.log("Shapes done " + (System.currentTimeMillis() - time));
        
        //for (Raindrop item : sim.rainDrops) {
        for (int i = 0; i < sim.rainDrops.size(); i++) {
            Raindrop item = sim.rainDrops.get(i); 
            Raindrop newItem = new Raindrop(item);
            drops.add(newItem);
        }
        //Utils.log("Drops done " + (System.currentTimeMillis() - time));

        //for (Spike item : sim.spikes) {
        for (int i = 0; i < sim.spikes.size(); i++) {
            Spike item = sim.spikes.get(i); 
            Spike newItem = new Spike(item);
            spikes.add(newItem);
        }
        //Utils.log("Spikes done " + (System.currentTimeMillis() - time));

        //for (Earthpatch item : sim.patches) {
        for (int i = 0; i < sim.patches.size(); i++) {
            Earthpatch item = sim.patches.get(i); 
            Earthpatch newItem = new Earthpatch(item);
            patches.add(newItem);
        }
        //Utils.log("Patches done " + (System.currentTimeMillis() - time));
        
        for (int i = 0; i < sim.wells.size(); i++) {
            GravityWell item = sim.wells.get(i); 
            GravityWell newItem = new GravityWell(item);
            wells.add(newItem);
        }

        currentTurnNumber = Player.getCurrentTurnNumber();
        maxTurns = Player.getMaxTurns();
        selectedShapeIndex = sim.getShapeIndex(Player.getSelectedShape());
        
        values.setTo(Data.currentValues);
        //Utils.log("All done " + (System.currentTimeMillis() - time));
    }

    public void restore(Simulator sim) {
        sim.reset();
        sim.signalReset();

        // We shouldn't need to do these clears because we already cleared in the sim.reset
        sim.shapes.clear();
        sim.rainDrops.clear();
        sim.spikes.clear();
        sim.patches.clear();
        sim.wells.clear();

        // Now we re-build from the snapshot
        
        //for (SysShape item : shapes) {
        for (int i = 0; i < shapes.size(); i++) {
            SysShape newItem = new SysShape(shapes.get(i));
            sim.addShape(newItem);
            if (newItem.getWasSelected() == true) {
                Gos.selectShape(newItem);
            }
        }
        
        //for (Raindrop item : drops) {
        for (int i = 0; i < drops.size(); i++) {
            Raindrop newItem = new Raindrop(drops.get(i));
            sim.addRaindrop(newItem);
        }

        //for (Spike item : spikes) {
        for (int i = 0; i < spikes.size(); i++) {
            Spike newItem = new Spike(spikes.get(i));
            sim.addSpike(newItem);
        }

        for (int i = 0; i < patches.size(); i++) {
            Earthpatch newItem = new Earthpatch(patches.get(i));
            sim.addEarthpatch(newItem);
        }

        for (int i = 0; i < wells.size(); i++) {
            GravityWell newItem = new GravityWell(wells.get(i));
            sim.addGravityWell(newItem);
        }

        if (selectedShapeIndex == -1) {
            // Do nothing; we should have already selected
        }
        else if (selectedShapeIndex < sim.shapes.size()) {
            Gos.selectShape(sim.shapes.get(selectedShapeIndex));
        } 
        else {
            // Maybe we took a snapshot before we even had a system ready?
            // Check to make sure we even had shapes here.
            if (sim.shapes.size() > 0) {
                // Should not be possible but just in case.
                Gos.selectShape(sim.shapes.get(0));
            }
        }

        Data.currentValues.setTo(values);
        
        // BUT REMEMBER when we set the values, we also need to update the GravityWell
        // position.  Although theoretically it's already in the correct position just from
        // being saved out in its regular array.  
        if (sim.wells.size() > 0) {
            GravityWell well = sim.wells.get(0);
            well.moveTo(values.gravityWellCenterX, values.gravityWellCenterY);
        }
    }

    // Write out everything to a string
    public String toString() {
        StringBuilder sb = new StringBuilder();

        // Sandwich the state in these strings so we can embed it in other data and easily find it.
        sb.append("!START_SNAPSHOT\r\n"); // System state starter

        // First the system
        for (int i = 0; i < shapes.size(); i++) {
            sb.append(shapes.get(i).toString());
            sb.append("\r\n");
        }
        
        //for (Raindrop item : drops) {
        for (int i = 0; i < drops.size(); i++) {
            sb.append(drops.get(i).toString());
            sb.append("\r\n");
        }

        //for (Spike item : spikes) {
        for (int i = 0; i < spikes.size(); i++) {
            sb.append(spikes.get(i).toString());
            sb.append("\r\n");
        }

        for (int i = 0; i < patches.size(); i++) {
            sb.append(patches.get(i).toString());
            sb.append("\r\n");
        }

        for (int i = 0; i < wells.size(); i++) {
            sb.append(wells.get(i).toString());
            sb.append("\r\n");
        }

        // Then the values (you might think we save this anyway in the playerdata but not really; we do,
        // but only the values that have changed since the last turn.  This requires us to actually know
        // what came "before."  For this method, we want this sytem state to be able to be restored from
        // the string in a context-free environment using JUST this one string.  So we save all the values here.
        sb.append(values.toString() + "\r\n"); 

        // And we're done
        sb.append("!END_SNAPSHOT\r\n"); // System state ender
        return sb.toString();
    }

    // Easy enough; if a line is something that should be in the system,
    // we parse it and created said obj, otherwise just ignore and move on
    private void parseOneLine(Simulator sim, String line) {
        // Shape, drop, spike, patch, well, or nothing?
        String key = line.substring(0, 2);
        switch (key) {
            case Constants.SHAPE_KEY_STRING: {
                SysShape newItem = new SysShape(sim, line);
                shapes.add(newItem);
                break;
            }
            case Constants.DROP_KEY_STRING: {
                Raindrop newItem = new Raindrop(sim, line);
                drops.add(newItem);
                break;
            }
            case Constants.SPIKE_KEY_STRING: {
                Spike newItem = new Spike(sim, line);
                spikes.add(newItem);
                break;
            }
            case Constants.PATCH_KEY_STRING: {
                Earthpatch newItem = new Earthpatch(sim, line);
                patches.add(newItem);
                break;
            }
            case Constants.WELL_KEY_STRING: {
                GravityWell newItem = new GravityWell(sim, line);
                wells.add(newItem);
                break;
            }
        }
    }

    // Read in a string and restore a snap from that string
    public void restoreFromString(Simulator sim, String str) {
        clear();

        // When we saved out, we saved the 'selected' state onto the shapes,
        // so we can restore that here instead of using the index, which
        // was not saved (just because maybe the array isn't in the same order now?)
        selectedShapeIndex = -1;

        // So we first load up the string as an array of lines
        String lines[] = str.split("\\r?\\n");

        // First find the beginning to make sure we're actually at the snapshot
        int i = 0;
        String line = lines[i];
        while (line != null) {
            if (i >= lines.length) {
                break;
            }
            line = lines[i];
            parseOneLine(sim, line);
            i++;
        }
        //String curLine = // get the SysShape string from the str
        //if (curLine == isSysShape whatever) {
        //  SysShape newItem = new SysShape(sim, curLine);
        //  shapes.add(newItem);
        //}

        // Then we restore it to the sim
        restore(sim);
    }

    // How much "stuff" is in this snapshot?
    public int getNumItems() {
        return shapes.size() + drops.size() + spikes.size() + patches.size() + wells.size();
    }
}