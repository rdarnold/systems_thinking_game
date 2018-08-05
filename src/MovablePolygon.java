package gos;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import java.util.List;
import javafx.geometry.Point2D;
import java.util.ArrayList;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.DoubleProperty;

public class MovablePolygon extends Polygon {
    
    public static enum PolyType {
        Shape, Spike;
    }
    
    // So we can referende various lists from the simulator.
    public Simulator sim;

    public void setSim(Simulator s) { sim = s; }

    //public boolean drawBounds = false;

    protected PolyType polyType = PolyType.Shape;

    protected double maxSize = 30;
    protected double minSize = 1;
    protected double targetSize = 30;
    protected double growthRate = 0.5;

    // How fat should we be?
    // Make it a property so we can bind to it and do various things with it
    private DoubleProperty size = new SimpleDoubleProperty(maxSize);
    public DoubleProperty sizeProperty() { return size; }
    public double getSize() { return size.get(); }

    // Keep track of radius for collision purposes; actually it's the same as
    // just size / 2, but since we use it so often, we track it separately 
    // to avoid doing the division calculation constantly.  This is actually
    // easy to track since we call a setSize anyway which controls the positions
    // of the polygon points, so we just plunk this in there.
    private double m_fRadius = maxSize / 2; 
    public double getRadius() { return m_fRadius; }

    // Min and max size parameters.
    public double getMaxSize() { return maxSize; }
    public double getMinSize() { return minSize; }
    public void setMaxSize(double newSize) { maxSize = newSize; }
    public void setMinSize(double newSize) { minSize = newSize; }

    // The radius is the assumed radius size of the bounding circle,
    // this is more efficient than using an actual Circle object for the
    // boundary and works just as well.  Radius is always just
    // half of size.  Size is literally the diameter.
    //public double getRadius() {
      //  return (getSize() / 2);
    //}

    public boolean setSize(double newSize) {
        if (newSize < minSize) {
            size.set(minSize);
            updatePoints();
            return false;
        }
        size.set(newSize);
        // Update the radius, we could just calculate
        // it on the fly but this works just as well and saves us
        // the calculation time
        m_fRadius = getSize() / 2;
        updatePoints();
        return true;
    }
    public void setPrevSize() { prevSize = getSize(); }
    public void setSizeBack() { setSize(prevSize); }
    public double getPrevSize() { return prevSize; }

    private boolean selected = false;
    public boolean getSelected() { return selected; }
    public void setSelected(boolean sel) { 
        selected = sel; 

        // Hopefully if we are setting selected, this is not the case.
        // Though we could potentially link to the scene graph from this
        // shape, along with the shape's index in the graph, and add the
        // circle now if it hadn't been added earlier. Too complicated...?
        if (selectedCircle == null)
            return;

        if (selected == true && sim.isUsingSuccess() == true) {
            selectedCircle.setVisible(true);
            //selectedCircle.setFill(Color.rgb(255, 255, 255, 0));
            //selectedCircle.setStroke(Color.rgb(255, 0, 0, .5));
            updateSelectedCircle();
        }
        else {
            selectedCircle.setVisible(false);
            //selectedCircle.setFill(Color.rgb(255, 255, 255, 0));
            //selectedCircle.setStroke(Color.rgb(255, 255, 255, 0));
        }
    }
    
    //public void setSize(double newSize) { prevSize = size; size = newSize; updatePoints(); }
    //public void setSizeBack() { size = prevSize; updatePoints(); }

    //protected double size = 50;
    protected double prevSize = maxSize;
    protected double m_fAngleInDegrees = 0;
    public void setAngleDegrees(double deg) { m_fAngleInDegrees = deg; }
    public double getAngleDegrees() {return m_fAngleInDegrees; }

    protected double m_fPrevCenterX = 0;
    protected double m_fPrevCenterY = 0;
    protected double m_fCenterX = 0;
    protected double m_fCenterY = 0;
    public double getCenterX() { return m_fCenterX; }
    public double getCenterY() { return m_fCenterY; }

    protected double xSpeed = 0;
    protected double ySpeed = 0;
    public double getXSpeed() { return xSpeed; }
    public double getYSpeed() { return ySpeed; }

    // For more efficient collision checking
    //private Circle boundingCircle;
    private Circle selectedCircle = null;
    protected Text shapeText = null;  // If we want to display any text...

    /*private double m_fTopX;
    private double m_fTopY;

    // The leftmost X and Y coordinates
    private double m_fLeftX;
    private double m_fLeftY;*/

    /*public double getTopX() { return m_fTopX; }
    public double getTopY() { return m_fTopY; }

    public double getLeftX() { return m_fLeftX; }
    public double getLeftY() { return m_fLeftY; }*/

    /*private double xPoints[];
    private double yPoints[];

    public double[] getXPoints() { return xPoints; }
    public double[] getYPoints() { return yPoints; }
    
    public double getXPoint(int slot) { return xPoints[slot]; }
    public double getYPoint(int slot) { return yPoints[slot]; }*/

    public MovablePolygon(Simulator s) {    
        super();
        sim = s;
        //boundingCircle = new Circle(getSize());
        //boundingCircle.setFill(Color.rgb(100, 100, 100, 0.5));

       /* selectedCircle = new Circle(getSize());
        selectedCircle.setFill(Color.rgb(255, 255, 255, 0));
        selectedCircle.setStroke(Color.rgb(255, 0, 0, 0.5));
        selectedCircle.setVisible(false);*/

        //shapeText = new Text();
        //shapeText.setText("");
    }

    public void deepCopy(MovablePolygon from) {
        getPoints().addAll(from.getPoints());
        setSize(from.getSize());
        //setColor((Color)from.getFill());
        setFill(from.getFill());
        setAngleDegrees(from.getAngleDegrees());
        prevSize = from.getPrevSize();
        matchSpeed(from);
        maxSize = from.maxSize;
        minSize = from.minSize;
        targetSize = from.targetSize;
        growthRate = from.growthRate;
        moveTo(from.getCenterX(), from.getCenterY());
    }

    //public Circle getBoundingCircle() { return boundingCircle; }
    public Circle getSelectedCircle() { return selectedCircle; }
    public Text getShapeText() { return shapeText; }
    public void setShapeText(String str) {
        if (shapeText == null) {
            shapeText = new Text(str);
        }
        else {
            shapeText.setText(str);
        }
    }
    // This just gets us on the map, so we can get the shape texts
    // on the scene graph up front even if we aren't using it for
    // anything yet.
    public void setUseShapeText() {
        shapeText = new Text();
    }

    // Call this if we want to use a selected circle to display selection
    public void setUseSelectedCircle() {
        selectedCircle = new Circle(getSize());
        selectedCircle.setFill(Color.rgb(255, 255, 255, 0));
        selectedCircle.setStroke(Color.rgb(255, 0, 0, 0.5));
        selectedCircle.setVisible(false);
    }

    public void matchSpeed(MovablePolygon other) {
        setSpeed(other.getXSpeed(), other.getYSpeed());
    }

    public void zeroSpeed() {
        setSpeed(0, 0);
    }
    
    public void setSpeed(double x, double y) {
        xSpeed = x;
        ySpeed = y;
    }

    protected void addPoint(Point2D point) {
        addPoint(point.getX(), point.getY());
    }

    protected void addPoint(int x, int y) {
        addPoint((double)x, (double)y);
    }

    protected void addPoint(double x, double y) {
        getPoints().addAll(x, y);
    }

    protected void updatePoint(int index, Point2D point) {
        updatePoint(index, point.getX(), point.getY());
    }

    protected void updatePoint(int index, double x, double y) {
        // The polygon stores them all as one huge array 
        int polyIndex = index * 2;
        getPoints().set(polyIndex, x);
        getPoints().set(polyIndex + 1, y);
    }

    protected void updatePoints() {
        switch (polyType) {
            case Shape:
                updatePointsForShape();
                break;
            case Spike:
                updatePointsForSpike();
                break;
        }
        updateMetaData();
    }

    private void updatePointsForShape() {
        int numCorners = getNumCorners();
        List<Double> points = getPoints();
        double anglePerCorner = 360 / numCorners;
        double cornerAngle = m_fAngleInDegrees;
        double x = 0;
        double y = 0;
        for (int i = 0; i < numCorners; i++) {
            // Calc from straight line going up
            x = m_fCenterX;
            y = m_fCenterY - (getSize()/2);
            Point2D point = Utils.calcRotatedPoint(m_fCenterX, m_fCenterY, x, y, cornerAngle);
            updatePoint(i, point.getX(), point.getY());
            cornerAngle += anglePerCorner;
            cornerAngle = Utils.normalizeAngle(cornerAngle);
        }
    }

    private void updatePointsForSpike() {
        // This is a little more interesting.  It looks like a star.
        int numSpikes = 6;
        List<Double> points = getPoints();
        double anglePerSpike = 360 / numSpikes;
        double angle = m_fAngleInDegrees;
        double x = 0;
        double y = 0;
        for (int i = 0; i < numSpikes; i++) {
            // Calc from straight line going up
            // Make a spike, which requires three points.
            // Point 1.
            x = m_fCenterX;
            y = m_fCenterY - (getSize()/8);
            Point2D point = Utils.calcRotatedPoint(m_fCenterX, m_fCenterY, x, y, angle);
            updatePoint((i*3), point.getX(), point.getY());
            angle += anglePerSpike / 3;
            angle = Utils.normalizeAngle(angle);

            // Point 2, the highest point.
            x = m_fCenterX;
            y = m_fCenterY - (getSize()/2);
            point = Utils.calcRotatedPoint(m_fCenterX, m_fCenterY, x, y, angle);
            updatePoint((i*3)+1, point.getX(), point.getY());
            angle += anglePerSpike / 3;
            angle = Utils.normalizeAngle(angle);

            // Point 3, back down.
            x = m_fCenterX;
            y = m_fCenterY - (getSize()/8);
            point = Utils.calcRotatedPoint(m_fCenterX, m_fCenterY, x, y, angle);
            updatePoint((i*3)+2, point.getX(), point.getY());
            angle += anglePerSpike / 3;
            angle = Utils.normalizeAngle(angle);
        }
    }

    // Same as number of sides
    public int getNumCorners() {
        return (getPoints().size() / 2);
    }

    public int getNumSides() {
        // The number of sides is equal to the number of coordinate pairs in the polygon
        //   (so half the number of points in the array)
        return (getPoints().size() / 2);
    }

    public void setRandomColor() {
        int r = Utils.number(0, 255);
        int g = Utils.number(0, 255);
        int b = Utils.number(0, 255);
        setColor(Color.rgb(r, g, b));
    }

    public void setColor(Color color) {
        //setStroke(Color.AQUA), 
        setFill(color);
    }

    // Simply move along velocity path
    public boolean move() {
        if (xSpeed == 0 && ySpeed == 0) {
            return false;
        }
        moveBy(xSpeed, ySpeed);
        return true;
    }

    public void moveTo(double newCenterX, double newCenterY) {
        m_fCenterX = newCenterX;
        m_fCenterY = newCenterY;
        updateMetaData();
        updatePoints();
    }

    public void moveBack() {
        moveTo(m_fPrevCenterX, m_fPrevCenterY);
    }

    public void moveBackX() {
        moveTo(m_fPrevCenterX, m_fCenterY);
    }

    public void moveBackY() {
        moveTo(m_fCenterX, m_fPrevCenterY);
    }

    /*private void updateBoundingCircle() {
        boundingCircle.setCenterX(m_fCenterX);
        boundingCircle.setCenterY(m_fCenterY);
        boundingCircle.setRadius(getSize()/2);
    }*/

    private void updateSelectedCircle() {
        // Actually we don't even need to bother unless we are selected.
        if (selected == true && selectedCircle != null) {
            selectedCircle.setCenterX(m_fCenterX);
            selectedCircle.setCenterY(m_fCenterY);
            selectedCircle.setRadius((getSize()/2) + 5);
        }
    }

    public void updateShapeText() {
        if (shapeText != null) {
            shapeText.setX(m_fCenterX - (shapeText.getText().length() * 4));
            shapeText.setY(m_fCenterY + 6);
        }
    }

    public void updateMetaData() {
        //updateLeftXY();
        //updateTopXY();
        //updateBoundingCircle();
        updateSelectedCircle();
        updateShapeText();
    }
    
    public void moveBy(double mX, double mY) {
        double moveX = mX * Data.currentValues.globalMoveRate;
        double moveY = mY * Data.currentValues.globalMoveRate;
        m_fPrevCenterX = m_fCenterX;
        m_fPrevCenterY = m_fCenterY;
        m_fCenterX += moveX;
        m_fCenterY += moveY;
        updateMetaData();
        List<Double> points = getPoints();
        for (int i = 0; i < points.size(); i+=2) {
            points.set(i, points.get(i) + moveX);
        }
        for (int i = 1; i < points.size(); i+=2) {
            points.set(i, points.get(i) + moveY);
        }
    }
    
    /*public double findLeftX() {
        double left = 0;
        List<Double> points = getPoints();
        if (points == null || points.size() < 1) {
            return 0;
        }
        left = points.get(0);
        for (int i = 2; i < points.size(); i+=2) {
            if (points.get(i) < left) {
                left = points.get(i);
            }
        }
        return left;
    }

    public double findLeftY() {
        double left = 0;
        List<Double> points = getPoints();
        if (points == null || points.size() < 1) {
            return 0;
        }
        left = points.get(1);
        for (int i = 3; i < points.size(); i+=2) {
            if (points.get(i) < left) {
                left = points.get(i);
            }
        }
        return left;
    }*/

    /*private void updateLeftXY() {
        List<Double> points = getPoints();
        if (points == null || points.size() < 1) {
            m_fLeftX = 0;
            m_fLeftY = 0;
            return;
        }
        m_fLeftX = points.get(0);
        m_fLeftY = points.get(1);
        for (int i = 2; i < points.size(); i+=2) {
            if (points.get(i) < m_fLeftX) {
                m_fLeftX = points.get(i);
                m_fLeftY = points.get(i + 1);
            }
        }
    }

    private void updateTopXY() {
        List<Double> points = getPoints();
        if (points == null || points.size() < 1) {
            m_fTopX = 0;
            m_fTopY = 0;
            return;
        }
        m_fTopX = points.get(0);
        m_fTopY = points.get(1);
        for (int i = 3; i < points.size(); i+=2) {
            if (points.get(i) < m_fTopY) {
                m_fTopX = points.get(i-1);
                m_fTopY = points.get(i);
            }
        }
    }*/

    /*public boolean intersects(MovableCircle otherCircle) {
        double distanceX = boundingCircle.getCenterX() - otherCircle.getCenterX();
        double distanceY = boundingCircle.getCenterY() - otherCircle.getCenterY();
        double radiusSum = otherCircle.getRadius() + boundingCircle.getRadius();
        return distanceX * distanceX + distanceY * distanceY <= radiusSum * radiusSum;
    }

    // Right now it's just a bounding circle intersect and that should be sufficient.
    public boolean intersects(MovablePolygon otherShape) {
        Circle otherCircle = otherShape.boundingCircle;
        double distanceX = boundingCircle.getCenterX() - otherCircle.getCenterX();
        double distanceY = boundingCircle.getCenterY() - otherCircle.getCenterY();
        double radiusSum = otherCircle.getRadius() + boundingCircle.getRadius();
        return distanceX * distanceX + distanceY * distanceY <= radiusSum * radiusSum;
    }*/

    // Right now it's just a bounding circle intersect and that should be sufficient.
    public boolean intersects(MovablePolygon otherShape) {
        return intersects(otherShape.getCenterX(), otherShape.getCenterY(), otherShape.getRadius());
    }

    public boolean intersects(Circle otherCircle) {
        return intersects(otherCircle.getCenterX(), otherCircle.getCenterY(), otherCircle.getRadius());
    }

    public boolean intersects(double otherCenterX, double otherCenterY, double otherRadius) {
        double distanceX = getCenterX() - otherCenterX;
        double distanceY = getCenterY() - otherCenterY;
        double radiusSum = getRadius() + otherRadius;
        return distanceX * distanceX + distanceY * distanceY <= radiusSum * radiusSum;
    }

    /*
    public boolean checkCollisions(ArrayList<MovablePolygon> polygons) {
        for (MovablePolygon poly : polygons) {
            if (this.equals(poly) == true) {
                continue;
            }
            if (intersects(poly) == true) {
                return true;
            }
        }
        return false;
    }*/
    
    // Return false if we hit zero or below.
    public boolean growTowardsTargetSize() {
        // Handle various types of growth
        if (getSize() < targetSize) {
            setSize(getSize() + growthRate);
        } 
        else if (getSize() > targetSize) {
            setSize(getSize() - growthRate);
        }
        
        if (getSize() >= maxSize) {
            setSize(maxSize);
        }
        else if (getSize() <= 0) {
            setSize(0);
            return false;
        }
        return true;
    }
}