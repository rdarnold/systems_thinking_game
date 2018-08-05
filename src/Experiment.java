package gos;

import java.util.List;
import java.util.ArrayList;

public class Experiment {
    private SystemSnapshot snap;

    public SystemSnapshot getSnap() { return snap; }

    // Snag it from this time
    public void setStartingState(Simulator sim) {
        snap = new SystemSnapshot(sim);
    }

    public void play(Simulator sim) {
        if (snap != null) {
            snap.restore(sim);
            Gos.simRunner.runOneTurn(false);
        }
    }
}