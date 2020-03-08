package gos;

import java.util.ArrayList;

// All this stuff just to make our personal color for our selected shape
/*import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;*/

// Try to keep this decoupled from the UI
public class ScenarioMaker {
    Simulator sim;

    public ScenarioMaker(Simulator theSim) {
        sim = theSim;
    }
    
    // Depending on what task, we may need to generate a new scenario.
    public void prepareExercise(Exercise exercise) {
        if (exercise == null) {
            prepareRandomSystem();
        }
        else if (exercise.getId() == 0) {
            // Demographic survey
            return;
        }
        else if (exercise.getId() == 1) {
            // Career survey
            return;
        }
        else if (exercise.getId() == 2) {
            // Practice run
            prepareSystem0();
        }
        else if (exercise.getId() == 3) {
            prepareSystem1();
        }
        else if (exercise.getId() == 4) {
            prepareSystem3();
        }
        /*else if (exercise.getId() == 5) {
            prepareSystem3();
        }*/
        else if (exercise.getId() == 5) {
            // Post-assessment survey
            return;
        }
        else if (exercise.getId() == 6) {
            // Self-assessment
            return;
        }
        else {
            prepareRandomSystem();
        }

        // And grab a base snapshot
        sim.snapBase();

        // And a current
        sim.snapCurrent();
    }

    // The generic stuff that's common for all systems in the game
    public void prePrepareSystem() {
        GravityWell item = new GravityWell(sim);
        //item.centerInSim();
        item.setPosToDefaults();
        //item.moveTo(10, 10);
        sim.addGravityWell(item);
    }

    // This class prepares a variety of different scenarios
    // for us based on the task at hand.
    public void prepareRandomSystem() {
        sim.reset();
        setupRandomShapes();
        setupRandomPatches();
    }
    
    public void setupRandomShapes() {
        sim.shapes.clear();
    
        // Let's do a bunch of random shapes for fun.
        int num = Utils.number(6, 8);
        for (int i = 0; i < num; i++) {
            int x = Utils.number(50, sim.width - 50);
            int y = Utils.number(50, sim.height - 50);

            SysShape shape = sim.spawnRandomSysShape(x, y);
            while (shape.checkShapeCollisions(sim.shapes) == true) {
                x = Utils.number(50, sim.width - 50);
                y = Utils.number(50, sim.height - 50);
                shape.moveTo(x, y);
            }
        }
    }

    public void setupRandomPatches() {
        sim.patches.clear();
        
        // Let's do a bunch of random patches for fun.
        int num = Utils.number(6, 8);
        for (int i = 0; i < num; i++) {
            int x = Utils.number(50, sim.width - 50);
            int y = Utils.number(50, sim.height - 50);
            Earthpatch patch = new Earthpatch(sim);
            sim.addEarthpatch(patch);
            patch.setSize(Utils.number(5, 25));
            patch.moveTo(x, y);
        }
    }

    public void prepareSystem0() {
        sim.reset();
        prePrepareSystem();

        // System 0 is six different types.
        SysShape shape;
        int x = 145;
        int y = 145;
        
        shape = sim.addShape(x, y, 6);
        shape.setSize(55);
        shape.setSpinSpeed(2);
        shape.setSpinLeft();

        shape = sim.addShape(x + 300, y, 4);
        shape.setSize(45);
        shape.setSpinSpeed(3);
        shape.setSpinRight();

        shape = sim.addShape(x, y + 250, 5);
        shape.setSize(35);
        shape.setSpinSpeed(1);
        shape.setSpinLeft();

        shape = sim.addShape(x + 300, y + 250, 3);
        shape.setSize(25);
        shape.setSpinSpeed(4);
        shape.setSpinRight();

        shape = sim.addShape(x + 150, y - 50, 5);
        shape.setSize(35);
        shape.setSpinSpeed(3);
        shape.setSpinLeft();

        shape = sim.addShape(x + 150, y + 300, 3);
        shape.setSize(45);
        shape.setSpinSpeed(2);
        shape.setSpinRight();
    }

    public void prepareSystem1() {
        sim.reset();
        prePrepareSystem();

        // System 1 is just four different types.
        SysShape shape;
        int x = 190;
        int y = 165;
        
        shape = sim.addShape(x, y, 6);
        shape.setSize(55);
        shape.setSpinSpeed(2);
        shape.setSpinLeft();

        shape = sim.addShape(x + 200, y, 4);
        shape.setSize(45);
        shape.setSpinSpeed(3);
        shape.setSpinRight();

        shape = sim.addShape(x, y + 200, 5);
        shape.setSize(35);
        shape.setSpinSpeed(1);
        shape.setSpinLeft();

        shape = sim.addShape(x + 200, y + 200, 3);
        shape.setSize(25);
        shape.setSpinSpeed(4);
        shape.setSpinRight();
    }

    /*public void prepareSystem2() {
        sim.reset();

        // System 2 is eight different types.
        SysShape shape;
        int x = 100;
        int y = 200;
        
        shape = sim.addShape(x, y, 6);
        shape.setSize(55);
        shape.setSpinSpeed(2);
        shape.setSpinLeft();

        shape = sim.addShape(x + 200, y, 4);
        shape.setSize(45);
        shape.setSpinSpeed(3);
        shape.setSpinRight();

        shape = sim.addShape(x, y + 200, 5);
        shape.setSize(35);
        shape.setSpinSpeed(1);
        shape.setSpinLeft();

        shape = sim.addShape(x + 200, y + 200, 3);
        shape.setSize(25);
        shape.setSpinSpeed(4);
        shape.setSpinRight();


        shape = sim.addShape(x + 300, y + 100, 5);
        shape.setSize(20);
        shape.setSpinSpeed(1);
        shape.setSpinRight();

        shape = sim.addShape(x + 400, y + 100, 6);
        shape.setSize(30);
        shape.setSpinSpeed(2);
        shape.setSpinLeft();

        shape = sim.addShape(x + 300, y + 200, 3);
        shape.setSize(40);
        shape.setSpinSpeed(3);
        shape.setSpinRight();

        shape = sim.addShape(x + 400, y + 200, 4);
        shape.setSize(50);
        shape.setSpinSpeed(4);
        shape.setSpinLeft();
    }*/

    
    // The chaos exercise.  We just scatter random
    // shit all over and see what the player does to
    // handle it.  This is where we may start to see systemic
    // approaches emerging.  Despite the totally random system
    // we can see if any common trends emerge among experienced
    // folks.
    public void prepareSystem3() {
        sim.reset();
        prePrepareSystem();

        // System 3 is many different types.
        SysShape shape;
        for (int i = 0; i < 25; i++) {
            shape = sim.addShape(Utils.number(50, 550), Utils.number(50, 500), Utils.number(3, 6));
            shape.setMinSize(20); // These are different now.
            shape.setRandomSize();
            shape.setRandomSpinSpeed();
            if (Utils.number(0, 1) == 0) {
                shape.setSpinLeft();
            }
            else {
                shape.setSpinRight();
            }
        }

        // This one will become the one we select, so let's do a few things to it.
        SysShape playerShape = sim.shapes.get(sim.shapes.size()-1);
        // first, make it spin at 50%
        playerShape.setSpinSpeedPercent(50);
        // Now make it spin left
        playerShape.setSpinLeft();
        // Now put it in the middle of the map.
        playerShape.moveTo(275, 275);
        // And give it a common size
        playerShape.setSize(30);

        // Now, don't let anything intersect with our main dude.
        for (SysShape otherShape : sim.shapes) {
            if (otherShape.equals(playerShape)) {
                continue;
            }
            
            while (playerShape.intersects(otherShape) == true) {
                otherShape.moveTo(Utils.number(50, 550), Utils.number(50, 500));
            }
        }
        
        Gos.selectShape(playerShape);


        // You are now blue, make sure we indicate which shape the player IS though,
        // we need some other special way to determine this.
        //Image dots = new Image(dotsURL);
        //Player.getSelectedShape().setFill(new ImagePattern(dots, 0, 0, 1, 1, true));
        /*Image hatch = createHatch();
        ImagePattern pattern = new ImagePattern(hatch, 0.2, 0.2, 0.4, 0.4, true); 
        Player.getSelectedShape().setFill(pattern);
        Player.getSelectedShape().setColor(Color.RED);*/
    }

    /*private Image createHatch() {
        Pane pane = new Pane();
        pane.setPrefSize(5, 5);
        Line fw = new Line(0, 0, 5, 5);
        Line bw = new Line(0, 5, 5, 0);
        fw.setStroke(Color.SKYBLUE);
        bw.setStroke(Color.SKYBLUE);
        fw.setStrokeWidth(1);
        bw.setStrokeWidth(1);
        Rectangle rect = new Rectangle(0, 0, 5, 5);
        rect.setFill(Color.RED);
        pane.getChildren().addAll(rect, fw, bw);
        new Scene(pane);
        return pane.snapshot(null, null);
    }*/
}