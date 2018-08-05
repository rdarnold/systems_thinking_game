package gos;

import java.util.ArrayList;
import java.util.EnumSet;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.IntegerProperty;

// A turnset is a list of turns along with basically some indeces in that
// list to tell us where in the set we are.  It is used for observations
// and could be used for more things as well.
public class TurnSet {
    // Should probably use the properties instead of these.
    private int m_nCurrentFrameNumber = 0;
    private ArrayList<Turn> turns = new ArrayList<Turn>();

    // We have X number of turns in our "turns" array - this is the index
    // into that array to tell us where we are in there currently.  It's
    // a propery so we can bind to it.
    private final IntegerProperty m_npTurnNumber = new SimpleIntegerProperty(0);
    public IntegerProperty turnNumberProperty() { return m_npTurnNumber; }
    public int getTurnNumber() { return m_npTurnNumber.get(); }
    public void setTurnNumber(int num) { 
        m_npTurnNumber.set(num); 
        
        // If we are changing the turn number, we should always reset the frame
        // to zero, since it is an index into the current turn.  If we change
        // current turn, frame becomes meaningless/invalid
        m_nCurrentFrameNumber = 0;
    }
    public void resetTurnNumber() { setTurnNumber(0); }
    public void addTurnNumber() { addTurnNumber(1); }
    public void addTurnNumber(int num) {  
        setTurnNumber(m_npTurnNumber.get() + num); 
        if (m_npTurnNumber.get() > m_npMaxTurns.get()-1) {  
            setTurnNumber(m_npMaxTurns.get()-1);  
        }
    }
    public void subTurnNumber() { subTurnNumber(1); }
    public void subTurnNumber(int num) {
        setTurnNumber(m_npTurnNumber.get() - num);
        if (m_npTurnNumber.get() < 0) {  
            setTurnNumber(0);  
        }
    }

    // The max number of turns that are allowed for this observation.
    private static final int m_nDefaultMaxTurns = 3;
    private final IntegerProperty m_npMaxTurns = 
        new SimpleIntegerProperty(m_nDefaultMaxTurns);
    public IntegerProperty maxTurnsProperty() { return m_npMaxTurns; }
    public int getMaxTurns() { return m_npMaxTurns.get(); }
    public void setMaxTurns(int num) { m_npMaxTurns.set(num); }
    public void resetMaxTurns() { setMaxTurns(m_nDefaultMaxTurns); }
    public void addMaxTurns() { addMaxTurns(1); }
    public void addMaxTurns(int num) {  setMaxTurns(m_npMaxTurns.get() + num); }
    public void subMaxTurns() { subMaxTurns(1); }
    public void subMaxTurns(int num) {
        setMaxTurns(m_npMaxTurns.get() - num);
        if (m_npMaxTurns.get() < 0) {  setMaxTurns(0);  }
    }

    public ArrayList<Turn> getTurns() { return turns; }

    public int getNumTurns() {
        if (turns == null) 
            return 0;
        return turns.size();
    }

    public SystemSnapshot getCurrentSnap() {
        if (turns == null || turns.size() == 0) 
            return null;
        Turn turn = getCurrentTurn();
        if (turn == null)
            return null;
        // This does check boundaries so we are good to do this
        return turn.getCurrentFrame();
    }

    public Turn getCurrentTurn() { 
        return getTurn(getTurnNumber()); 
    }

    public Turn getTurn(int num) {
        if (turns.size() <= 0) {
            return null;
        }
        if (num < 0 || num >= turns.size()) {
            return null;
        }
        return turns.get(num); 
    }

    public void addTurn(Turn turn) {
        // If we are adding a turn, we know we want to add to the end,
        // so we know we wanna trim.  So we are adding at the index of
        // current turn number, so we trim all turns past current,
        // then set the index to the new number.
        // So increase the index, trim that index, then move on.
        // If we want to trim from within this turn we'd have to move
        // the index down by one before we called addTurn.
        addTurnNumber();
        trim();
        turns.add(turn);
        setTurnNumber(turns.size() - 1);
        //Utils.log(turns.size());
    }

    public SystemSnapshot getCurrentFrame() { 
        Turn turn = getCurrentTurn();
        if (turn == null) {
            return null;
        }
        return turn.getCurrentFrame();
    }

    public boolean onLastTurn() {
        return (getTurnNumber() >= (getMaxTurns()-1));
    }

    public boolean onFirstTurn() {
        return (getTurnNumber() == 0);
    }

    public void reset() {
        //m_nCurrentFrameNumber = 0;
        resetTurnNumber();
        turns.clear();
    }
    
    public void trim() {
        // We trim right up to the actual turn, the idea being that we're going to add
        // a new turn right now for the current turn number; we haven't simulated it yet.
        for (int i = turns.size() - 1; i >= getTurnNumber(); i--) {
            turns.remove(i);
        }
    }
}