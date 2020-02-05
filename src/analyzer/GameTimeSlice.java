package gos.analyzer;

// So all this is, is just a slice of time in the game when something occurred.
// The idea is that we can build a sequence of when general activities were occurring
// during the game.  For example, the player did a Tutorial for 3 minutes, during that time
// she was observing for 1 minute and playing for 2 minutes.  Maybe during the Four Shapes
// scenario, the player experimented for 5 minutes, spent 3 minutes on round 1, 2 minutes on
// round 2, then observed for a minute, then finished round 2 in another 5 minutes, then spent
// 20 minutes on round 3.  We can build up general timetables of what the player was doing by
// compiling lists of time slices.
public class GameTimeSlice {
    public GameTimeSlice() { }
    public GameTimeSlice(Segment seg) { sliceSeg = seg; }

    public enum Type {
        Exp, Obs, Play, Ques, Scen;  // Scen is a span for an entire scenario, like the entire Tutorial 
    }

    public enum Segment {
        Demographic, Tutorial, FourShapes, FourShapesReplay, Chaos, ChaosReplay, SelfAssess;
    }

    public int turn = 0; // This is only used during "Play" type for the turn number
    public int getTurn() { return turn; }

    public Type sliceType = Type.Play;
    public Segment sliceSeg = Segment.Tutorial;
    public long startTimeMS = 0;
    public long endTimeMS = 0; 
    public long getStartTimeMS() { return startTimeMS; }
    public long getEndTimeMS() { return endTimeMS; }

    public long getTotalTimeMS() { return endTimeMS - startTimeMS; }
}