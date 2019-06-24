package gos;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import java.util.List;
import javafx.geometry.Point2D;
import java.util.ArrayList;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.DoubleProperty;

public class MovableCircle extends Circle {
    
    // So we can referende various lists from the simulator.
    public Simulator sim;

    public void setSim(Simulator s) { sim = s; }

    protected double maxSize = 30;
    protected double targetSize = 30;
    protected double growthRate = 0.5;

    // How fat should we be?
    // Make it a property so we can bind to it and do various things with it
    private DoubleProperty size = new SimpleDoubleProperty(50.0);
    public DoubleProperty sizeProperty() { return size; }
    public double getSize() {  return size.get();  }

    public boolean setSize(double newSize) {
        if (newSize <= 0) {
            size.set(0);
            setRadius(getSize()/2);
            return false;
        }
        size.set(newSize);
        setRadius(getSize()/2);
        return true;
    }
    public void setPrevSize() { prevSize = getSize(); }
    public void setSizeBack() { setSize(prevSize); }
    public double getPrevSize() { return prevSize; }

    //protected double size = 50;
    protected double prevSize = 0;

    protected double m_fPrevCenterX = 0;
    protected double m_fPrevCenterY = 0;
    protected double xSpeed = 0;
    protected double ySpeed = 0;

    public double getPrevCenterX() { return m_fPrevCenterX; }
    public double getPrevCenterY() { return m_fPrevCenterY; }
    public double getXSpeed() { return xSpeed; }
    public double getYSpeed() { return ySpeed; }

    public MovableCircle(Simulator s) {    
        super();
        sim = s;
    }

    public void deepCopy(MovableCircle from) {
        setSize(from.getSize());
        setColor((Color)from.getFill());
        prevSize = from.getPrevSize();
        matchSpeed(from);
        maxSize = from.maxSize;
        targetSize = from.targetSize;
        growthRate = from.growthRate;
        moveTo(from.getCenterX(), from.getCenterY());
    }

    public void matchSpeed(MovableCircle other) {
        setSpeed(other.getXSpeed(), other.getYSpeed());
    }

    public void zeroSpeed() {
        setSpeed(0, 0);
    }
    
    public void setSpeed(double x, double y) {
        xSpeed = x;
        ySpeed = y;
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
        setCenterX(newCenterX);
        setCenterY(newCenterY);
    }

    public void moveBack() {
        moveTo(m_fPrevCenterX, m_fPrevCenterY);
    }
    
    public void moveBy(double moveX, double moveY) {
        m_fPrevCenterX = getCenterX();
        m_fPrevCenterY = getCenterY();
        setCenterX(m_fPrevCenterX + moveX * Data.currentValues.globalMoveRate);
        setCenterY(m_fPrevCenterY + moveY * Data.currentValues.globalMoveRate);
    }

    public boolean intersects(MovablePolygon otherShape) {
        return intersects(otherShape.getCenterX(), otherShape.getCenterY(), otherShape.getRadius());
        //return intersects(otherShape.getBoundingCircle());
    }

    // Right now it's just a bounding circle intersect and that should be sufficient.
    public boolean intersects(Circle otherCircle) {
        return intersects(otherCircle.getCenterX(), otherCircle.getCenterY(), otherCircle.getRadius());
        /*double distanceX = getCenterX() - otherCircle.getCenterX();
        double distanceY = getCenterY() - otherCircle.getCenterY();
        double radiusSum = otherCircle.getRadius() + getRadius();
        return distanceX * distanceX + distanceY * distanceY <= radiusSum * radiusSum;*/
    }

    public boolean intersects(double otherCenterX, double otherCenterY, double otherRadius) {
        double distanceX = getCenterX() - otherCenterX;
        double distanceY = getCenterY() - otherCenterY;
        double radiusSum = otherRadius + getRadius();
        return distanceX * distanceX + distanceY * distanceY <= radiusSum * radiusSum;
    }
    
    public boolean checkPolygonCollisions(ArrayList<MovablePolygon> polygons) {
        for (MovablePolygon poly : polygons) {
            if (intersects(poly) == true) {
                return true;
            }
        }
        return false;
    }
    
    public boolean checkCircleCollisions(ArrayList<MovableCircle> circles) {
        for (MovableCircle circle : circles) {
            if (intersects(circle) == true) {
                return true;
            }
        }
        return false;
    }

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