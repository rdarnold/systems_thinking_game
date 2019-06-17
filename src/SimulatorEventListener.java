package gos;

public interface SimulatorEventListener {
    void onGuiUpdateRequired();
    void updateOneFrame(boolean running, Turn currentTurn);
    void onSimReset();
    void onStartTurn();
    void onEndTurn();
    void onPause();
    void onShapeAdded(SysShape shape);
    void onRaindropAdded(Raindrop drop);
    void onShapeRemoved(SysShape shape);
    void onRaindropRemoved(Raindrop drop);
    void onSpikeAdded(Spike spike);
    void onSpikeRemoved(Spike spike);
    void onEarthpatchAdded(Earthpatch patch);
    void onEarthpatchRemoved(Earthpatch patch);
    void onGravityWellAdded(GravityWell item);
    void onGravityWellRemoved(GravityWell item);
    void onNumberOfShapesChanged(int numberShapes);
}