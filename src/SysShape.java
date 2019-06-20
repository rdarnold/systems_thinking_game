package gos;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import java.util.List;
import javafx.geometry.Point2D;
import java.util.ArrayList;

import javafx.scene.input.MouseButton;

public class SysShape extends MovablePolygon {

    public enum RangeTypes {
        TakeRange, GiveRange, ShareRange;
    }

    public static int DEFAULT_MAX_SIZE = 75;

    protected boolean m_bDrawRange = false;

    public boolean getDrawRangeIsOn() {
        return m_bDrawRange;
    }

    private final double giveTakeRangeBaseRadius = 50;
    // private final double takeRangeBaseRadius = 50;
    // private final double giveRangeBaseRadius = 50;
    private final double shareRangeBaseRadius = 100;

    public double giveTakeRange = 0;
    // public double takeRange = 0;
    // public double giveRange = 0;
    public double shareRange = 0;

    ///////////////////////
    // These vars must all be deep copied
    ///////////////////////
    int success = 0;
    boolean dead = false;
    double minSpinSpeed = 1;
    double maxSpinSpeed = 4;
    double spinSpeed = 1; // 1 degree per frame
    boolean spinRight = true;
    double armor = 0;
    double maxArmor = 25;
    int totalSpikeHits = 0;
    double growArmorDesire = 0;

    // Increased this to make it more significant
    double spikeDamageMultiplier = 1.5; // 1.0; //0.2;
    double armorGrowthRate = 2.0;

    // How much of spinSpeed per frame?
    double giveRate = 0.02;
    double takeRate = 0.02;
    double deteriorationRate = 0; // 0.02;

    // Track some numbers
    double sizeStolen = 0;
    double sizeStolenFrom = 0;
    double sizeGiven = 0;
    double sizeGivenTo = 0;
    ///////////////////////
    // End deep copy vars
    ///////////////////////

    Circle giveTakeRangeCircle;
    // Circle takeRangeCircle;
    // Circle giveRangeCircle;
    Circle shareRangeCircle; // For cooperative mode

    public Circle getGiveTakeRangeCircle() {
        return giveTakeRangeCircle;
    }

    // public Circle getTakeRangeCircle() { return takeRangeCircle; }
    // public Circle getGiveRangeCircle() { return giveRangeCircle; }
    public Circle getShareRangeCircle() {
        return shareRangeCircle;
    }

    public double getSizeStolen() {
        return sizeStolen;
    }

    public double getSizeStolenFrom() {
        return sizeStolenFrom;
    }

    public double getSizeGiven() {
        return sizeGiven;
    }

    public double getSizeGivenTo() {
        return sizeGivenTo;
    }

    public void addSizeStolen(double amt) {
        sizeStolen += amt;
    }

    public void addSizeStolenFrom(double amt) {
        sizeStolenFrom += amt;
    }

    public void addSizeGiven(double amt) {
        sizeGiven += amt;
    }

    public void addSizeGivenTo(double amt) {
        sizeGivenTo += amt;
    }

    // These four variables all depend on the number of corners; they
    // can be viewed almost like "cultural traits"
    // But these have all been minimized to have less effect
    // 2019-06-16: Removed the effects entirely to simplify the game
    public double getStealRate() {
        /*
         * if (getNumCorners() == 3) { return 1.2; }
         */
        return 1.0;
    }

    // This only affects earth patches and rain.
    public double getGrowthRate() {
        /*
         * if (getNumCorners() == 4) { return 1.2; }
         */
        return 1.0;
    }

    public double getMoveRate() {
        /*
         * if (getNumCorners() == 5) { return 1.2; }
         */
        return 1.0;
    }

    public double getSpikeDefense() {
        /*
         * if (getNumCorners() == 6) { return 1.2; }
         */
        return 1.0;
    }

    // If we are dead, how long until we reduce to zero.
    private double destructionGrowthRate = 10;

    public void setSpinSpeed(double newSpeed) {
        spinSpeed = Utils.clamp(newSpeed, minSpinSpeed, maxSpinSpeed);
    }

    // Takes a percentage from 0 to 100
    public void setSpinSpeedPercent(double perc) {
        double range = maxSpinSpeed - minSpinSpeed;
        setSpinSpeed(minSpinSpeed + (range * (perc / 100f)));
    }

    public void setSpinRight() {
        spinRight = true;
    }

    public void setSpinLeft() {
        spinRight = false;
    }

    public void setSpin(boolean spin) {
        spinRight = spin;
    }

    public void flipSpin() {
        spinRight = !spinRight;
    }

    public void setRandomSpinSpeed() {
        setSpinSpeedPercent((double) Utils.number(0, 100)); // Utils.random(0.0, 100.0));
    }

    public void setRandomSize() {
        setSize(Utils.number((int) getMinSize(), (int) getMaxSize()));
    }

    public double getSpinSpeedPercent() {
        double range = maxSpinSpeed - minSpinSpeed;
        double speed = spinSpeed - minSpinSpeed;
        return ((speed / range) * 100f);
    }

    public void setSizePercent(double perc) {
        double range = getMaxSize() - getMinSize();
        setSize(getMinSize() + (range * (perc / 100f)));
    }

    public double getSizePercent() {
        double range = getMaxSize() - getMinSize();
        double sz = getSize() - getMinSize();
        return ((sz / range) * 100f);
    }

    public double getSpinSpeed() {
        return spinSpeed;
    }

    public boolean getSpin() {
        return spinRight;
    }

    public boolean getSpinRight() {
        return spinRight;
    }

    public boolean getSpinLeft() {
        return (!spinRight);
    }

    public boolean isDead() {
        return dead;
    }

    public double getArmor() {
        return armor;
    }

    public void setArmor(double val) {
        armor = val;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int val) {
        success = val;
        if (success <= 0) {
            success = 0;
            super.setShapeText("");
        } else {
            super.setShapeText("" + success);
        }
    }

    public void addSuccess() {
        setSuccess(success + 1);
    }

    public void subSuccess() {
        setSuccess(success - 1);
    }

    public SysShape(Simulator s) {
        super(s);
        init(null);
    }

    public SysShape(SysShape from) {
        super(from.sim);
        init(from);
    }

    public void init(SysShape from) {
        // We need to get this up front so that we can add the
        // Text object to the scene graph even though we may not be using
        // it right now.
        setUseShapeText();
        setUseSelectedCircle();

        setOnMouseClicked(t -> {
            //Utils.log("SHAPE"); 
            Gos.gos.onClickShape((SysShape) t.getSource());
        });
 
        maxSize = SysShape.DEFAULT_MAX_SIZE;

        polyType = MovablePolygon.PolyType.Shape;

        // Only create these if we need visualization.  Otherwise
        // we can do all our calculations just with numbers so that
        // we don't have to allocate extra memory for no reason.
        if (m_bDrawRange == true) {
            giveTakeRangeCircle = new Circle();
            //takeRangeCircle = new Circle();
            //giveRangeCircle = new Circle();
            shareRangeCircle = new Circle();
        }
        setRandomColor();

        // Since I'm not using armor now, I'm outlining all
        // shapes to make them easier to see.
        setStroke(Color.BLACK);
        setStrokeWidth(1);

        if (from != null) {
            // Deep copy everything.
            deepCopy(from);
            return;
        }
        prepareNewSpawn();
    }

    // Make everything equal
    public void deepCopy(SysShape from) {
        super.deepCopy(from);
        success = from.success;
        dead = from.dead;
        spinSpeed = from.spinSpeed;
        spinRight = from.spinRight;
        updateArmor(from.armor); // This updates the graphics correctly too
        maxArmor = from.maxArmor;
        totalSpikeHits = from.totalSpikeHits;
        growArmorDesire = from.growArmorDesire;
        spikeDamageMultiplier = from.spikeDamageMultiplier;
    	armorGrowthRate = from.armorGrowthRate;
    	giveRate = from.giveRate;
    	takeRate = from.takeRate;
    	shareRangeCircle = from.shareRangeCircle;
        deteriorationRate = from.deteriorationRate;
        sizeStolen = from.sizeStolen;
        sizeStolenFrom = from.sizeStolenFrom;
        sizeGiven = from.sizeGiven;
        sizeGivenTo = from.sizeGivenTo;
    }
    
    public void prepareNewSpawn() {
        dead = false;
        totalSpikeHits = 0;
        growArmorDesire = 0;
        setArmor(0);
        maxArmor = 25;
    }

    public void makeShape(int numCorners) {
       makeShape(getCenterX(), getCenterY(), numCorners);
    }

    public void makeShape(double centerX, double centerY, int numCorners) {
        // So we need to take the center point and calculate the distances
        // to all the corners from there.
        getPoints().clear();
        for (int i = 0; i < numCorners; i++) {
            // Just plop them all in the center then move them.
            addPoint(centerX, centerY);
        }
        moveTo(centerX, centerY);
    }

    private void spin() {
        if (spinRight == true) {
            m_fAngleInDegrees += spinSpeed;
        } else {
            m_fAngleInDegrees -= spinSpeed;
        }
        m_fAngleInDegrees = Utils.normalizeAngle(m_fAngleInDegrees);
    }

    // This gets called by the base class as needed.
    @Override
    public void setColor(Color color) {
        setStroke(Color.BLACK); // For armor
        //setStrokeWidth(0);
        super.setColor(color);

        // Extremely strange that I need to multiply by 255 in this case as sometimes
        // I seem to get back the 0-255 value .. but here I get back a 0-1 number.
        if (m_bDrawRange == true) {
            int red = (int)(255.0 * color.getRed());
            int green = (int)(255.0 * color.getGreen());
            int blue = (int)(255.0 * color.getBlue());
            giveTakeRangeCircle.setFill(Color.rgb(red, green, blue, .2));
            //takeRangeCircle.setFill(Color.rgb(red, green, blue, .2));
            //giveRangeCircle.setFill(Color.rgb(red, green, blue, .2));
            shareRangeCircle.setFill(Color.rgb(red, green, blue, .1));
        }

        //setEffect(Utils.createBorderGlow(color));
    }

    // This gets called by the base class as needed.
    @Override
    public void updateMetaData()
    {
        super.updateMetaData();
        giveTakeRange = getRadius() + giveTakeRangeBaseRadius;
        //takeRange = getSize()/2 + takeRangeBaseRadius;
        //giveRange = getSize()/2 + giveRangeBaseRadius;
        shareRange = shareRangeBaseRadius;
        if (m_bDrawRange == true) {
            // They can be different if I want later... people might want 
            // to be able to tweak these things.
            giveTakeRangeCircle.setCenterX(m_fCenterX);
            giveTakeRangeCircle.setCenterY(m_fCenterY);
            giveTakeRangeCircle.setRadius(giveTakeRange);
            
            //takeRangeCircle.setCenterX(m_fCenterX);
            //takeRangeCircle.setCenterY(m_fCenterY);
            //takeRangeCircle.setRadius(takeRange);

            //giveRangeCircle.setCenterX(m_fCenterX);
            //giveRangeCircle.setCenterY(m_fCenterY);
            //giveRangeCircle.setRadius(giveRange);

            shareRangeCircle.setCenterX(m_fCenterX);
            shareRangeCircle.setCenterY(m_fCenterY);
            //shareRangeCircle.setRadius(getSize()/2 + 75);
            shareRangeCircle.setRadius(shareRange);
        }
    }

    public void updateOneFrame(boolean running, Simulator s) {
        sim = s;

        // Do whatever.
        if (running == false) {
            spin();
            updatePoints();
	        return; 
        }
        // Rotate
        spin();

        if (dead == true) {
            shrink(destructionGrowthRate);
            return;
        }

        // Give/take size
        exchangeSize();

        // Apply traits
        applyTraits();

        // Move
        moveOneFrame();

        // Arms race
        tryMatchArmor();

        // Maintenance required
        deteriorate();

        // Update graphics
        updatePoints();
    }

    public void deteriorate() {
        // Deteriorate linearly over time.
        shrink(deteriorationRate);
    }

    public void updateArmor(double amount) {
        armor += amount;
        if (armor > maxArmor) {
            armor = maxArmor;
        } 
        else if (armor <= 0) {
            armor = 0;
            //setStrokeWidth(0);
            return;
        }
        // Create our polyline stroke.
        // Always make it at least 2 pixels so we can actually see it.
        setStrokeWidth(1 + Math.ceil(armor / amountPerPatch));
    }

    public void improveArmor(double amount) {
        updateArmor(amount);
    }

    public boolean reduceArmor(double amount) {
        if (armor > 0) {
            updateArmor(amount * -1);
            return true;
        }
        return false;
    }

    public void addGrowArmorDesire(double amount) {
        updateGrowArmorDesire(growArmorDesire + amount);
    }
    public void reduceGrowArmorDesire(double amount) {
        updateGrowArmorDesire(growArmorDesire - amount);
    }

    public void updateGrowArmorDesire(double amount) {
        growArmorDesire = amount;
        if (growArmorDesire < 0) {
            growArmorDesire = 0;
        }
        else if (growArmorDesire > maxArmor) {
            // This doesnt really make sense because its not a target
            // armor level, but then again, why would we want to grow even
            // higher than our max.  Makes no sense.
            growArmorDesire = maxArmor;
        }
        
        // Ok if we are at max, we don't want to grow any more.
        if (armor == maxArmor) {
            growArmorDesire = 0;
        }
    }

    public int damagePerSpike(Spike spike) {
        return (int)((Math.ceil(spike.getSize() * spikeDamageMultiplier) / getSpikeDefense()));
    }

    public void hitSpike(Spike spike) {
        // We wouldnt want an armored thing to take multiple hits
        if (spike.getHit() == true) {
            return;
        }
        spike.setHit();

        int dmg = damagePerSpike(spike);

        // Change this depending on armor.
        if (reduceArmor(dmg) == false) {
            //shrink(getSize()/2);
            shrink(dmg);
        }

        totalSpikeHits++;

        // removing armor for now.
        /*if (Data.currentValues.armorRules != Constants.ArmorRules.NoArmor.getValue()) {
            addGrowArmorDesire(armorDamagePerSpike(spike));
        }*/
    }

    // Doubling this for our experimental version
    private static int amountPerPatch = 12;
    public static int getAmountPerPatch() { return amountPerPatch; }
    public void hitEarth(Earthpatch patch) {
        if (patch.getHit() == true) {
            return;
        }
        patch.setHit();

        // Now what?  How do we represent this resource?
        // We can use this to grow either armor or babies.
        // But how do we show this resource on screen?
        // If we've recently been hit, improve our armor
        /*if (growArmorDesire > 0) {
            growArmorDesire -= amountPerPatch;
            improveArmor(amountPerPatch);
            return;
        }*/

        // If no growth, we don't grow from this.
        if (Data.currentValues.growthRules == Constants.GrowthRules.NoGrowth.getValue())
            return;

        // They should probably start to suck the material from the
        // patches in rather than just grabbing the whole thing.
        grow(amountPerPatch * getGrowthRate());
    }

    public boolean checkSpikeCollisions(ArrayList<Spike> spikes) {
        for (Spike spike : spikes) {
            if (spike.getHit() == true) {
                continue;
            }
            if (intersects(spike) == true) {
                hitSpike(spike);
                return true;
            }
        }
        return false;
    }

    public boolean checkShapeCollisions(ArrayList<SysShape> shapes) {
        for (SysShape otherShape : shapes) {
            if (this.equals(otherShape) == true) {
                continue;
            }

            if (intersects(otherShape) == true) {
                return true;
            }
        }
        return false;
    }

    public boolean checkEarthCollisions(ArrayList<Earthpatch> patches) {
        for (Earthpatch patch : patches) {
            if (patch.getHit() == true) {
                continue;
            }
            if (intersects(patch) == true) {
                hitEarth(patch);
                return true;
            }
        }
        return false;
    }

    public void moveOneFrame() {
        if (move() == true) {
            // Attempt to move along velocity path,
            // determined by gravity pull
            /*if (checkShapeCollisions(sim.shapes) == true) {
                moveBack();
            }*/

            // If it hit a wall we don't move through it even though that is
            // kinda cool as it goes "outside the bounds" of a normal system.
            // Maybe it demonstrates out of the box thinking.  Which I think
            // would be great, but maybe that's better served for a different
            // type of app so I can just get my damned PhD.
            if (getCenterX() - getSize()/2 < 0) {
                moveBackX();
            }
            else if (getCenterX() + getSize()/2 > sim.width) {
                moveBackX();
            }
            if (getCenterY() - getSize()/2 < 0) {
                moveBackY();
            }
            else if (getCenterY() + getSize()/2 > sim.height) {
                moveBackY();
            }
        }

        // Actually it is more efficient for us to just check collisions
        // with only the shapes, since there are less of them generally than
        // patches and spikes.  Well not really, I mean, it's shapes times
        // spikes plus shapes times patches, same either way...
        //Utils.log(sim.shapes.size() * sim.spikes.size() + 
          //  sim.shapes.size() * sim.patches.size()); 
        checkSpikeCollisions(sim.spikes);
        checkEarthCollisions(sim.patches);

        // Also check if we have gone too far out of bounds, if so, stop.
        // Actually we can set the area to bounded or not; in future iterations
        // we could remove or potentially decreases this boundary.  People might
        // be like, whoa, what the heck?  When shapes start moving across behind
        // their buttons and such, could be an interesting way to see how they
        // react - the system isn't "bounded" by conventional wisdom.  Hell I could
        // even have an exercise where they need to get their shape as far left
        // as possible, and most people think it's the edge of the box but it's 
        // actually not, you can remove the boundary and move even further left.
        // Literally thinking "out of the box"


        // Now we reset their velocities to zero.  It was just
        // the speed for one single round, we arent accelerating.
        zeroSpeed();
    }

    private static int amountPerRain = 2;
    public static int getAmountPerRain() { return amountPerRain; }
    public void absorbRain(Raindrop drop, Simulator s) {
        if (Data.currentValues.growthRules == Constants.GrowthRules.NoGrowth.getValue())
            return;
        sim = s;
        // But if we are too big.. revert.  The sim does that for us.
        grow(amountPerRain * getGrowthRate()); // Grow a little bit more from rain...
    }

    public void spawn(double x, double y) {
        SysShape shape = new SysShape(this);
        shape.prepareNewSpawn();
        // Should they conserve volume or not?
        //shape.setSize(getSize() / 2);
        // Actually let's have them not.  They lose volume in a split.
        shape.setSize(getSize() / 3);
        // They should conserve armor though
        shape.updateArmor(this.getArmor());
        shape.moveTo(x, y);
        sim.addShape(shape);
    }

    public void split() {
        // split into two smaller shapes with the same (?) traits,
        // although this will probably make these types of shapes always
        // come out on top.
        double x = getCenterX();
        double y = getCenterY();

        x -= (getSize() / 3);
        spawn(x, y);

        x = getCenterX();
        x += (getSize() / 3);
        spawn(x, y);
    }

    // Keep a list of shapes that we can use to track which ones are in
    // range.
    private ArrayList<SysShape> inRange = new ArrayList<SysShape>();
    public double share(double amount) {
        double amt = amount;
        double count = 1 + inRange.size();

        // First count how many in our immediate area then divide evenly
        // between them.
        inRange.clear();
        for (int i = sim.shapes.size() - 1; i >= 0; i--) {
            SysShape otherShape = sim.shapes.get(i);
            if (this.equals(otherShape) == true) {
                continue;
            }
            if (otherShape.isDead() == true) {
                continue;
            }

            //if (rangeIntersects(shareRangeCircle, otherShape) == true) {
            if (rangeIntersects(RangeTypes.ShareRange, otherShape) == true) {
                count++;
                inRange.add(otherShape);
            }
        }

        amt /= count;

        for (SysShape shape : inRange) {
            shape.grow(amt, true);
        }
        // Post-clear so we don't somehow leak this memory if the shape itself is 
        // cleared 
        inRange.clear();

        return amt;
    }

    public void grow(double amount) {
        grow(amount, false);
    }

    // Shared means we got this from a distribution from a cooperative nearby
    // shape, which means we don't want to continue to redistribute.
    public void grow(double amount, boolean shared) {
        double amt = amount;

        // If we want armor, grow that instead.
        if (growArmorDesire > 0 && getArmor() < maxArmor) {
            amt = amount * armorGrowthRate;
            reduceGrowArmorDesire(amt);

            // Actually though, armor doesnt grow as fast as our size,
            // its kind of hard to grow.
            improveArmor(amt);
            return;
        }

        setPrevSize();

        // So, if the mindset is cooperation, we attempt to distribute any
        // growth we receive evenly across our immediate area.  Beware that
        // received size from cooperation distributions shouldn't continue to redistribute
        // because it'll be endless.  So growth from cooperation sharing should
        // not trigger this.
        if (shared == false && 
            Data.currentValues.paradigm == Constants.Paradigms.Cooperative.getValue()) {
            // And actually we're going to say that the sum is more than the addition
            // of the parts, so when we share we actually improve growth more.  So we
            // multiply by 2.
            amt = share(amt * 2);
        }

        // We could do a transition graphic here to make this look better.
        setSize(getSize() + amt);

        /*if (checkShapeCollisions(sim.shapes) == true) {
            setSizeBack();
        }*/
        // Even though we really should be doing this, for performance reasons
        // I'm going to remove it.  It should be generally fine, as when the spikes
        // and patches update, they'll see these collisions.  These updates get
        // really slow as we have a lot of shapes on screen so I need to optimize
        // somehow.
        //checkSpikeCollisions(sim.spikes);
        //checkEarthCollisions(sim.patches);

        // Maybe this shouldn't always happen, some things should have other
        // types of behavior.
        if (getSize() > maxSize) {
            // If the sim is in success mode we don't split, we add success.
            if (sim.isUsingSuccess() == true) {
                addSuccess();
                setSize(getMinSize()); // Go back to the smallest size but with more success
            }
            else {
                // Actually we need to be dead first so that we can calculate the nubmer
                // of living shapes to transmit to whatever classes are listening.
                // It's ok because the shapes set dead to false when they prepare new spawn
                // anyway.
                dead = true;
                split();
            }
        }
    }
    
    public double shrink(double amount) {
        setPrevSize();
        // We could do a transition graphic here to make this look better.
        if (setSize(getSize() - amount) == false) {
            // If my size is zero, I'm ded!  But only if my success is zero.
            if (getSuccess() == 0) {
                // Well, if we dont allow death and this guy is selected, don't die.
                if (sim.noPlayerDeath() == true && Player.getSelectedShape() == this) {
                    setSize(getMinSize());
                }
                else {
                    dead = true;
                }
            }
            else {
                // If not, I lose one success and go back to max size.
                subSuccess();
                setSize(getMaxSize());
            }
        }
        return (getPrevSize() - getSize());
    }

    // RDA 2019-06-16: Removing the old gravity rules and using a new concept called Gravity Well
    public void receiveGravity(MovablePolygon from, double amount) {}
    public void receiveGravity(MovableCircle from, double amount) {}

    public void receiveGravityFromWells(ArrayList<GravityWell> wells) {
        for (GravityWell well : wells) {
            receiveGravity(well.getCenterX(), well.getCenterY(), well.getGravityPull());
        }
    }

    /*
    public void receiveGravity(MovablePolygon from, double amount) {
        // if you're already colliding, stop applying gravity.
        if (Data.currentValues.gravityRules == Constants.GravityRules.Normal.getValue() &&
            intersects(from) == true) 
        {
            return;
        }
        // Based on size, 
        // Should it be based on proximity too?
        receiveGravity(from.getCenterX(), from.getCenterY(), amount);
    }

    public void receiveGravity(MovableCircle from, double amount) {
        // if you're already colliding, stop applying gravity, well only
        // if gravity is normal.  If reversed it should not stop applying
        // if you are colliding, you should be able to push things away.
        if (Data.currentValues.gravityRules == Constants.GravityRules.Normal.getValue() &&
            intersects(from) == true) 
        {
            return;
        }
        // Based on size, 
        // Should it be based on proximity too?
        receiveGravity(from.getCenterX(), from.getCenterY(), amount);
    }*/

    int numPulls = 0;
    double totalXPull = 0;
    double totalYPull = 0;
    public void receiveGravity(double fromX, double fromY, double amount) {

        // Update our velocity to tend towards this thing,
        // based on size.  
        // This is literally for every single frame so we need to make this slow
        double strength = amount / 50;

        // Actually what we want to do is, add up all the X pulls, average them,
        // add up all the Y pulls, average them.

        // To calculate the X pull, we take the angle from this center to the
        // other center, and use the velocity as the hypotenuse   

        double radians = Utils.getAngleRadians(getCenterX(), getCenterY(), fromX, fromY);
        double xPull = strength * Math.cos(radians);
        double yPull = strength * Math.sin(radians);

        numPulls++;
        totalXPull += xPull;
        totalYPull += yPull;
    }

    public void applyGravity() {
        if (numPulls == 0) {
            return;
        }
        if (Data.currentValues.gravityRules == Constants.GravityRules.Off.getValue()) {
            return;
        }
        // Go through our gravity array and average all the gravitation pulls
        // to figure out where we are actually going.
        double xPull = totalXPull / numPulls;
        double yPull = totalYPull / numPulls;

        if (Data.currentValues.gravityRules == Constants.GravityRules.Normal.getValue()) {
            xSpeed += (xPull * Data.currentValues.gravityRate * getMoveRate());
            ySpeed += (yPull * Data.currentValues.gravityRate * getMoveRate());
        } 
        else {
            // Reverse gravity - repel instead of attracting.
            xSpeed -= (xPull * Data.currentValues.gravityRate * getMoveRate());
            ySpeed -= (yPull * Data.currentValues.gravityRate);
        }

        numPulls = 0;
        totalXPull = 0;
        totalYPull = 0;
    }

    public void tryMatchArmor() {
        if (Data.currentValues.armorRules != Constants.ArmorRules.Normal.getValue()) {
            return;
        }
        double max = getMaxNearbyShapeArmor();
        // Only add desire if we dont already have any.  Remember its not
        // target armor level, its just how much we want to grow our armor.
        if (max > 0 && growArmorDesire == 0 && getArmor() <= max) {
            // We just want slightly more... it just keeps inceasing.  Sort of like
            // an "envy race" as its not really an arms race because they aren't
            // competing with each other through the armor.
            addGrowArmorDesire(1);
        }   
    }

    public double getMaxNearbyShapeArmor()
    {
        double max = 0;
        for (SysShape otherShape : sim.shapes) {
            if (this.equals(otherShape) == true) {
                continue;
            }
            if (otherShape.isDead() == true) {
                continue;
            }
            //if (rangeIntersects(giveRangeCircle, otherShape) == true) {
            if (rangeIntersects(RangeTypes.GiveRange, otherShape) == true) {
                if (otherShape.getArmor() > max) {
                    max = otherShape.getArmor();
                }
            }
        } 
        return max;
    }
    
    public void applyTraits() {
        /*for (SysShape otherShape : sim.shapes) {
            // Whatever traits we have, apply them appropriately
            // Maybe we attract or avoid other shapes or other types of shapes.
        }*/
    }

    public void exchangeSize() {
        if (dead == true) {
            return;
        }
        // If we aren't competitive we don't give and take size from each other
        if (Data.currentValues.paradigm == Constants.Paradigms.Competitive.getValue()) {
            if (spinRight == true) {
                takeSize();
            } else {
                giveSize();
            }
        }
    }
    
    public void takeSize() {
        if (dead == true) {
            return;
        }
        if (Data.currentValues.noTakeSize == true) {
            return;
        }
        // Try to grab size from our neighbors, based on our rate
        // and our proximity.  The proximity isn't important except
        // that they have to be within range.
        for (int i = sim.shapes.size() - 1; i >= 0; i--) {
            if (dead == true) {
                // We can die during this
                return;
            }
            SysShape otherShape = sim.shapes.get(i);
            if (this.equals(otherShape) == true) {
                continue;
            }
            if (otherShape.isDead() == true) {
                continue;
            }
            // If we have the same number of corners, we are considered allies
            // and thus we dont take each others' size.  
           // if (getNumCorners() == otherShape.getNumCorners()) {
            //    continue;
            //}
            //if (rangeIntersects(takeRangeCircle, otherShape) == true) {
            if (rangeIntersects(RangeTypes.TakeRange, otherShape) == true) {
                // Raaahhh take their shit
                double amount = spinSpeed * takeRate;
                amount *= getStealRate();
                otherShape.shrink(amount);
                grow(amount);
                otherShape.addSizeStolenFrom(amount);
                addSizeStolen(amount);
            }
        }
    }

    public void giveSize() {
        if (dead == true) {
            return;
        }
        if (Data.currentValues.noGiveSize == true) {
            return;
        }
        // Try to grab size from our neighbors, based on our rate
        // and our proximity.  The proximity isn't important except
        // that they have to be within range.
        for (int i = sim.shapes.size() - 1; i >= 0; i--) {
            if (dead == true) {
                // We can die during this
                return;
            }
            SysShape otherShape = sim.shapes.get(i);
            if (this.equals(otherShape) == true) {
                continue;
            }
            if (otherShape.isDead() == true) {
                continue;
            }
            // If we have the same number of corners, we are considered allies
            // and thus we dont take each others' size.  
           // if (getNumCorners() == otherShape.getNumCorners()) {
             //   continue;
            //}
            //if (rangeIntersects(giveRangeCircle, otherShape) == true) {
            if (rangeIntersects(RangeTypes.GiveRange, otherShape) == true) {
                // Raaahhh give them shit
                double amount = spinSpeed * giveRate;
                shrink(amount);
                otherShape.grow(amount);
                otherShape.addSizeGivenTo(amount);
                addSizeGiven(amount);
            }
        }
    }

    public boolean rangeIntersects(Circle range, MovablePolygon otherShape) {
        double distanceX = range.getCenterX() - otherShape.getCenterX();
        double distanceY = range.getCenterY() - otherShape.getCenterY();
        double radiusSum = otherShape.getRadius() + range.getRadius();
        return distanceX * distanceX + distanceY * distanceY <= radiusSum * radiusSum;
        /*Circle otherCircle = otherShape.getBoundingCircle();
        double distanceX = range.getCenterX() - otherCircle.getCenterX();
        double distanceY = range.getCenterY() - otherCircle.getCenterY();
        double radiusSum = otherCircle.getRadius() + range.getRadius();
        return distanceX * distanceX + distanceY * distanceY <= radiusSum * radiusSum;*/
    }

    public boolean rangeIntersects(RangeTypes rangeType, SysShape otherShape) {
        double radius = 0;
        double otherRadius = 0;
        switch (rangeType) {
            case TakeRange:
                radius = giveTakeRange; 
                otherRadius = otherShape.giveTakeRange;
                break; 
            case GiveRange:
                radius = giveTakeRange; 
                otherRadius = otherShape.giveTakeRange;
                break; 
            case ShareRange:
                radius = shareRange; 
                otherRadius = otherShape.shareRange;
                break; 
            default:
                radius = giveTakeRange; 
                otherRadius = otherShape.giveTakeRange;
                break; 
        }
        //Circle otherCircle = otherShape.getBoundingCircle();
        double distanceX = getCenterX() - otherShape.getCenterX();
        double distanceY = getCenterY() - otherShape.getCenterY();
        double radiusSum = otherRadius + radius;
        return distanceX * distanceX + distanceY * distanceY <= radiusSum * radiusSum;
    }
}