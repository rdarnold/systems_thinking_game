package gos;

import java.util.ArrayList;
import java.util.EnumSet;

// Basically, a Turn is a list of snapshots.  You can play it forwards or backwards.
// It also has a little bit of meta-data about the turn itself so even if we aren't
// recording snapshots we still need a new one every turn.
public class Turn {
    int m_nCurrentFrameNumber = 0;
    int m_nMaxFrames = 0;

    // Initialize to 300 because there are 300 frames max in a turn, generally.
    ArrayList<SystemSnapshot> frames = new ArrayList<SystemSnapshot>();
    
    public boolean recordFrames = false;
    public void setMaxFrames(int num) {m_nMaxFrames = num; }
    public int getMaxFrames() { return m_nMaxFrames; }
    public int getCurrentFrameNumber() { return m_nCurrentFrameNumber; }
    public void advanceFrameNumber() { m_nCurrentFrameNumber++; }
    public void setCurrentFrameNumber(int num) { m_nCurrentFrameNumber = num; }
    public ArrayList<SystemSnapshot> getFrames() { return frames; };

    public void addFrame(Simulator sim) {
        SystemSnapshot snap = new SystemSnapshot(sim);
        frames.add(snap);
    }

    // Just remove all but the first one.
    public void clearFrames() {
        // THIS IS THE PROBLEM:
        //  The garbage collector doesn't have to return memory to the OS after it collects.
        //   So YES this is being garbage collected within the application but the memory
        //   is NOT being returned to the HEAP because the GC doesn't like to return memory
        //   to the heap.  To fix, I used Xmx to set max memory size to 512 MB which makes
        //   it return memory to the heap when it reaches that point.
        SystemSnapshot frame = null;
        if (frames.size() > 0) {
            frame = frames.get(0);
        }
        // Shouldn't need to do this but maybe it'll help with memory issues...
        for (int i = 1; i < frames.size(); i++) {
            frames.get(i).clear();
        }
        frames.clear();
        frames = new ArrayList<SystemSnapshot>();
        if (frame != null) {
            frames.add(frame);
        }
        m_nCurrentFrameNumber = 0;
        m_nMaxFrames = 1;
    }

    // Clear all frames after frameNum
    public void trimFrames(int frameNum) {
        if (frames == null || frameNum > frames.size())
            return;
        frames.subList(frameNum, frames.size()).clear();
    }
    public void trimAfterCurrent() {
        trimFrames(m_nCurrentFrameNumber);
    }

    public SystemSnapshot getFrame(int num) {
        if (frames == null || frames.size() <= 0) {
            return null;
        }
        if (num < 0 || num >= frames.size()) {
            return null;
        }
        return frames.get(num);
    }

    public SystemSnapshot getCurrentFrame() {
        if (frames == null || frames.size() <= 0 ||
            m_nCurrentFrameNumber >= frames.size()) {
            return null;
        }
        return frames.get(m_nCurrentFrameNumber);
    }

    public SystemSnapshot getNext() {
        if (m_nCurrentFrameNumber >= frames.size()) {
            m_nCurrentFrameNumber = 0;
            return null;
        }
        m_nCurrentFrameNumber++;
        return frames.get(m_nCurrentFrameNumber - 1);
    }

    public SystemSnapshot getLastFrame() {
        if (frames == null || frames.size() <= 0)
            return null;
        return frames.get(frames.size()-1);
    }

    public int getLastFrameNumber() {
        if (frames == null || frames.size() <= 0)
            return 0;
        return (frames.size()-1);
    }

    public boolean onLastFrame() {
        return (getCurrentFrameNumber() == getLastFrameNumber());
    }

    public boolean onMaxFrame() {
        return (getCurrentFrameNumber() >= (getMaxFrames()-1));
    }

    // Do we have a full turn - i.e. are all frames included?
    public boolean isFull() {
        return (getNumFrames() >= getMaxFrames());
    }

    public void setToLastFrame() {
        setCurrentFrameNumber(getLastFrameNumber());
    }

    public int getNumFrames() {
        return frames.size();
    }

    // How many total items in all snapshots in this turn?
    public int getNumItems() {
        int num = 0;
        for (SystemSnapshot snap : frames) {
            num += snap.getNumItems();
        }
        return num;
    }
    
    // OK so save it out in some semi-efficient fashion
    public void save() {
        // We should probably save some meta data about the turn, then go ahead
        // and save each snapshot using its own save function (yet to be implemented)
    }

    // Load up what we saved.
    public void load() {

    }
}