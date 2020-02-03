package gos.analyzer;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import javafx.scene.control.Button;
import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import java.io.*; 
import java.net.*; // To get the mac address
import java.security.*;
import java.util.Enumeration;

// GUI stuff
import javafx.scene.control.*;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.ChoiceBox;
import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.control.Label;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;

import gos.gui.*;
import gos.*;

// This is essentially like a static class in C#
public final class SkillCalculator {

    private SkillCalculator() { // private constructor
    }

    public static int calc(int min, int max) {
        return (min + rand.nextInt(((max + 1) - min)));
    }
}