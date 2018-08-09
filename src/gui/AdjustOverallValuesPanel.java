package gos.gui;

import java.io.*;
import java.util.*;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
/*import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;*/
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.Node;
import javafx.geometry.Pos;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.ListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ListChangeListener;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.web.HTMLEditor;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Priority;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.binding.Bindings;
 
import gos.*;

public class AdjustOverallValuesPanel extends PanelTopBase implements AdjustValuesInterface {

    // Add more configs of different sets of controls here if needs be.
    enum Configs {
        None,
        Default;
    }

    GridPane mainArea;

    SysShape selected = null;

    Slider globalSlider;
    Slider rainRateSlider;
    Slider rainSpeedSlider;
    ChoiceBox cbDur;
    ChoiceBox<Constants.Dir> cbRain;
    ColorPicker dotColor;
    Slider dotSize;
    Slider dotSpeed;
    //CheckBox gravityReverse;
    ChoiceBox<Constants.GravityRules> cbGravity;
    ChoiceBox<Constants.ArmorRules> cbArmor;
    ChoiceBox<Constants.Paradigms> cbParadigm;
    ChoiceBox<Constants.GrowthRules> cbGrowth;
    CheckBox chkSteal;
    CheckBox chkGive;

    ActionPanelVBox avbGravity;
    ActionPanelVBox avbRain;
    ActionPanelVBox avbParadigm;
    ActionPanelVBox avbGrowth;

    // Which config of panel?  If the config is the same we don't need to remove and re-add
    // everything.  Start with no config because we want to set up at least once.
    private Configs config = Configs.None; 

    @Override
    public void addControl(VBox box) {
        controlsList.add(box);
        mainArea.getChildren().add(box);
    }

    @Override
    public void removeControls() {
        mainArea.getChildren().removeAll(controlsList);
        controlsList.clear();
        localControlsList.clear();
    }

    @Override
    public Node getNodeForModel(SelectionModel model) {
        return getNodeForModel(model, localControlsList);
    }

    public void addToolTips() {
        Info info = null;
        
        info = Data.getInfoByName("Gravity");
        //Utils.addToolTip(cbGravity, info.getText());
        avbGravity.addToolTip(info.getText());

        info = Data.getInfoByName("Rain");
        //Utils.addToolTip(rainRateSlider, info.getText());
        avbRain.addToolTip(info.getText());

        info = Data.getInfoByName("Paradigm");
        //Utils.addToolTip(cbParadigm, info.getText());
        avbParadigm.addToolTip(info.getText());

        //info = Data.getInfoByName("Growth");
        String str = "When Growth is set to 'No Growth' shapes will not grow through environmental " +
        "factors.  However, spikes will also not form.";
        //Utils.addToolTip(cbGrowth, str);   
        avbGrowth.addToolTip(str);
    }

    public void setupOneItem(Node node, int row, int col) {
        GridPane.setRowIndex(node, row);
        GridPane.setColumnIndex(node, col);
        GridPane.setHalignment(node, HPos.CENTER);

        if (ChoiceBox.class.isInstance(node)) {
            ((ChoiceBox)node).getSelectionModel().selectFirst();
        }
    }

    public void setStyles() {
        cbGravity.getStyleClass().add("gos-cb");
        cbParadigm.getStyleClass().add("gos-cb");
        cbGrowth.getStyleClass().add("gos-cb");
        rainRateSlider.getStyleClass().add("gos-slider");
    }

    public void setup(Configs configType) {
        clearStyles(controlsList);
        if (config == configType) {
            setValues();
            return;
        }
        
        config = configType;

        super.m_MainVBox.setAlignment(Pos.BOTTOM_CENTER);

        if (mainArea == null) {
            mainArea = new GridPane();
            //mainArea.setPadding(new Insets(10));
            //mainArea.setHgap(10);
            //mainArea.setVgap(10);
            
            ColumnConstraints colConstraints;
            RowConstraints rowConstraints;
            
            /*rowConstraints = new RowConstraints();
            rowConstraints.setPercentHeight(33);
            mainArea.getRowConstraints().add(rowConstraints);
            rowConstraints = new RowConstraints();
            rowConstraints.setPercentHeight(33);
            mainArea.getRowConstraints().add(rowConstraints);
            rowConstraints = new RowConstraints();
            rowConstraints.setPercentHeight(33);
            mainArea.getRowConstraints().add(rowConstraints);*/

            colConstraints = new ColumnConstraints();
            colConstraints.setPercentWidth(25);
            mainArea.getColumnConstraints().add(colConstraints);
            colConstraints = new ColumnConstraints();
            colConstraints.setPercentWidth(25);
            mainArea.getColumnConstraints().add(colConstraints);
            colConstraints = new ColumnConstraints();
            colConstraints.setPercentWidth(25);
            mainArea.getColumnConstraints().add(colConstraints);
            colConstraints = new ColumnConstraints();
            colConstraints.setPercentWidth(25);
            mainArea.getColumnConstraints().add(colConstraints);

            super.m_MainVBox.getChildren().add(mainArea);
        }

        removeControls();

        Node node;
        Slider slider;

        int row = 0;
        int col = 0;

        cbDur = new ChoiceBox();
        cbDur.getItems().add("1 second");
        cbDur.getItems().add("2 seconds");
        cbDur.getItems().add("3 seconds");
        cbDur.getItems().add("4 seconds");
        cbDur.getItems().add("5 seconds");
        //node = addChoiceBox(cbDur, "Turn Duration:");
        //setupOneItem(node, row++, col);

        cbGravity = new ChoiceBox<Constants.GravityRules>();
        cbGravity.getItems().setAll(Constants.GravityRules.values());
        avbGravity = addChoiceBox(cbGravity, "Gravity");
        //setupOneItem(avbGravity, 2, col);
        setupOneItem(avbGravity, row++, col);

        globalSlider = createSlider();
        //node = addSliderControl(globalSlider, "Movement Rate");
        //setupOneItem(node, row++, col);

        row = 0;
        col++; 
        cbRain = new ChoiceBox<Constants.Dir>();
        cbRain.getItems().setAll(Constants.Dir.values());
        cbRain.getSelectionModel().selectFirst();
        //node = addChoiceBox(cbRain, "Rain Origin");
        //setupOneItem(node, row++, col);

        rainRateSlider = createSlider();
        avbRain = addSliderControl(rainRateSlider, "Rain Rate");
        //setupOneItem(avbRain, 2, col);
        setupOneItem(avbRain, row++, col);

        rainSpeedSlider = createSlider();
        //node = addSliderControl(rainSpeedSlider, "Rain Speed");
        //setupOneItem(node, row++, col);

        row = 0;
        col++; 
        dotColor = new ColorPicker(Data.currentValues.getMovingDotColor());
        //node = addColorControl(dotColor, "Moving Dot Color");
        //setupOneItem(node, row++, col);

        dotSize = createSlider();
        //node = addSliderControl(dotSize, "Moving Dot Size");
        //setupOneItem(node, row++, col);

        dotSpeed = createSlider();
        //node = addSliderControl(dotSpeed, "Circle Speed");
        //setupOneItem(node, row++, col);
        
        cbGrowth = new ChoiceBox<Constants.GrowthRules>();
        cbGrowth.getItems().setAll(Constants.GrowthRules.values());
        avbGrowth = addChoiceBox(cbGrowth, "Growth");
        //setupOneItem(avbGrowth, 2, col);
        setupOneItem(avbGrowth, row++, col);

        //row = 0;
        //col++; 

        cbArmor = new ChoiceBox<Constants.ArmorRules>();
        cbArmor.getItems().setAll(Constants.ArmorRules.values());
        //node = addChoiceBox(cbArmor, "Armor Rules");
        //setupOneItem(node, row++, col);
        
        // Tooltip needed
        chkSteal = new CheckBox();
        //node = addCheckBox(chkSteal, "Shapes Cannot Steal");
        //setupOneItem(node, row++, col);

        chkGive = new CheckBox();
        //node = addCheckBox(chkGive, "Shapes Cannot Give");
        //setupOneItem(node, row++, col);

        row = 0;
        col++; 
        cbParadigm = new ChoiceBox<Constants.Paradigms>();
        cbParadigm.getItems().setAll(Constants.Paradigms.values());
        avbParadigm = addChoiceBox(cbParadigm, "Paradigm");
        //setupOneItem(avbParadigm, 2, col);
        setupOneItem(avbParadigm, row++, col);

        /*node = addTempText("(TODO - Rules, Goals, Paradigms, etc.)");
        GridPane.setRowIndex(node, row + 1);
        GridPane.setColumnIndex(node, col + 3);
        GridPane.setHalignment(node, HPos.CENTER);*/

        // Actually a high leverage point would be how much the spinning
        // affects each other, like how fast each thing steals from each other.
        // All else aside that is the most significant feedback loop in the system.
        // People should be able to manipulate that one, perhaps indirectly through
        // use of the culture traits, or by setting all spin speeds to be equal..

        setStyles();
        addToolTips();
        clearStyles(controlsList);
        setValues();
        setupListeners(localControlsList);
    }

    public void populateCheck(CheckBox box, boolean val) {
        if (val == true) {
            box.setSelected(true);
        }
        else {
            box.setSelected(false);
        }
    }

    @Override
    public void onChanged(Node node) { 
        setValues(node);
    }
    
    public void setValues() {
        setValues(null);
    }

    public void setToCurrentValues() {
        cbDur.getSelectionModel().select(Data.currentValues.turnSeconds - 1);
        cbRain.getSelectionModel().select(Data.currentValues.rainOrigin);
        dotColor.setValue(Data.currentValues.getMovingDotColor());
        populateCheck(chkSteal, Data.currentValues.noTakeSize);
        populateCheck(chkGive, Data.currentValues.noGiveSize);
        cbGravity.getSelectionModel().select(Data.currentValues.gravityRules);
        cbArmor.getSelectionModel().select(Data.currentValues.armorRules);
        cbParadigm.getSelectionModel().select(Data.currentValues.paradigm);
        cbGrowth.getSelectionModel().select(Data.currentValues.growthRules);
        globalSlider.setValue(getSliderPositionForValue(
            Data.startingValues.globalMoveRate, Data.currentValues.globalMoveRate));
        rainRateSlider.setValue(getSliderPositionForValue(
            Data.startingValues.rainRate, Data.currentValues.rainRate));
        rainSpeedSlider.setValue(getSliderPositionForValue(
            Data.startingValues.rainSpeedVar, Data.currentValues.rainSpeedVar));
        dotSpeed.setValue(getSliderPositionForValue(
            Data.startingValues.movingDotSpeedRate, Data.currentValues.movingDotSpeedRate));
        dotSize.setValue(getSliderPositionForValue(
            Data.startingValues.movingDotSize, Data.currentValues.movingDotSize));
    }
    
    // When we change anything, there are times when we only allow one change
    // per turn so we would have to change everything back.
    public void setValues(Node node) {
        if (node == null) {
            setToCurrentValues();
            return;
        }

        // So if any of these changeAllowed things are TRUE, that means that this
        // was NOT the value that was just changed, - we just changed something else,
        // so we would want to change these back to their original.
        /*
        if (changeAllowed(cbDur, node) == true) {
            cbDur.getSelectionModel().select(Data.currentValues.turnSeconds - 1);
        }

        if (changeAllowed(cbRain, node) == true) {
            cbRain.getSelectionModel().select(Data.currentValues.rainOrigin);
        }

        if (changeAllowed(dotColor, node) == true) {
            dotColor.setValue(Data.currentValues.getMovingDotColor());
        }

        if (changeAllowed(chkSteal, node) == true) {
            populateCheck(chkSteal, Data.currentValues.noTakeSize);
        }
        if (changeAllowed(chkGive, node) == true) {
            populateCheck(chkGive, Data.currentValues.noGiveSize);
        }

        if (changeAllowed(cbGravity, node) == true) {
            cbGravity.getSelectionModel().select(Data.currentValues.gravityRules);
        }
        if (changeAllowed(cbArmor, node) == true) {
            cbArmor.getSelectionModel().select(Data.currentValues.armorRules);
        }
        if (changeAllowed(cbParadigm, node) == true) {
            cbParadigm.getSelectionModel().select(Data.currentValues.paradigm);
        }

        if (changeAllowed(globalSlider, node) == true) {
            globalSlider.setValue(getSliderPositionForValue(
                Data.startingValues.globalMoveRate, Data.currentValues.globalMoveRate));
        }
        if (changeAllowed(rainRateSlider, node) == true) {
            rainRateSlider.setValue(getSliderPositionForValue(
                Data.startingValues.rainRate, Data.currentValues.rainRate));
        }
        if (changeAllowed(rainSpeedSlider, node) == true) {
            rainSpeedSlider.setValue(getSliderPositionForValue(
                Data.startingValues.rainSpeedVar, Data.currentValues.rainSpeedVar));
        }
        if (changeAllowed(dotSpeed, node) == true) {
            dotSpeed.setValue(getSliderPositionForValue(
                Data.startingValues.movingDotSpeedRate, Data.currentValues.movingDotSpeedRate));
        }
        if (changeAllowed(dotSize, node) == true) {
            dotSize.setValue(getSliderPositionForValue(
                Data.startingValues.movingDotSize, Data.currentValues.movingDotSize));
        }

        // Now we say, if anything isnot normal, color it, if it is
        // normal, uncolor it, so it's easy to see what's different.
        updateChangedValueColors();*/
    }

    private void setStyleForControl(Node control, boolean colored) {
        VBox box = getVBoxForLocalControl(control);
        // This is how you call directly from an interface to be sure
        // its method is called. I wouldn't want to accidentally recursively
        // call back into this method and I'm not sure how the calling would
        // work in this case.  So, I'm explicit.
        AdjustValuesInterface.super.setStyleForControl(box, colored);
    }
    

    private boolean isDifferent(Slider slider, double val, double start_val) {
        double actual = start_val * getMultiplierForSlider(slider); 
        return (actual != val);
    }

    private void updateChangedValueColors() {
        // Look through and if anything is different, mark it for coloration.

        // ChoiceBoxes
        setStyleForControl(cbDur, isDifferent(cbDur, Data.currentValues.turnSeconds - 1));
        setStyleForControl(cbRain, isDifferent(cbRain, Data.currentValues.rainOrigin));
        setStyleForControl(cbGravity, isDifferent(cbGravity, Data.currentValues.gravityRules));
        setStyleForControl(cbArmor, isDifferent(cbArmor, Data.currentValues.armorRules));
        setStyleForControl(cbParadigm, isDifferent(cbParadigm, Data.currentValues.paradigm));
        setStyleForControl(cbGrowth, isDifferent(cbGrowth, Data.currentValues.growthRules));
        
        // Sliders
        setStyleForControl(globalSlider, isDifferent(globalSlider, 
            Data.currentValues.globalMoveRate, Data.startingValues.globalMoveRate));
        setStyleForControl(rainRateSlider, isDifferent(rainRateSlider, 
            Data.currentValues.rainRate, Data.startingValues.rainRate));
        setStyleForControl(rainSpeedSlider, isDifferent(rainSpeedSlider, 
            Data.currentValues.rainSpeedVar, Data.startingValues.rainSpeedVar));
        setStyleForControl(dotSpeed, isDifferent(dotSpeed, 
            Data.currentValues.movingDotSpeedRate, Data.startingValues.movingDotSpeedRate));
        setStyleForControl(dotSize, isDifferent(dotSize, 
            Data.currentValues.movingDotSize, Data.startingValues.movingDotSize));
        /*setStyleForControl(globalSlider, isDifferent(globalSlider, 50));
        setStyleForControl(rainRateSlider, isDifferent(rainRateSlider, 50));
        setStyleForControl(dotSpeed, isDifferent(dotSpeed, 50));
        setStyleForControl(dotSize, isDifferent(dotSize, 50));
        setStyleForControl(rainSpeedSlider, isDifferent(rainSpeedSlider, 50));*/

        // Checkboxes
        setStyleForControl(chkSteal, isDifferent(chkSteal, Data.currentValues.noTakeSize));
        setStyleForControl(chkGive, isDifferent(chkGive, Data.currentValues.noGiveSize));

        // Other
        setStyleForControl(dotColor, isDifferent(dotColor, 
            Data.currentValues.movingDotR,
            Data.currentValues.movingDotG,
            Data.currentValues.movingDotB));
    }

    // Based on the standard formula, get the correct position this slider should
    // be in based on the starting and current values, with the median as 50,
    // top as 100 and bottom as zero.  We just reverse the getMultiplier formula.
    // So 2.0x should be 100,  1.0x should be 50, 0.5x should be 0, according to current formula.
    private double getSliderPositionForValue(double start_val, double val) {
        double pos = 0;
        if (start_val == val)
            pos = 50;
        else if (val > start_val) {
            pos = 50;
            // so we want to take 0 - 1 and convert it
            // to 0-50, so just multiply by 50.
            // But first we need to get the ratios from the values themselves
            // to operate on.
            double mult = val / start_val;
            // Multiplier is from 1.0 to 2.0 so normalize it by subtracting the lower.
            pos += (mult - 1) * 50;
        }
        else {
            double mult = val / start_val;
            pos = (mult - 0.5) * 100;
        }
        return (pos);
    }

    // So get the variability in a variable from the slider.
    // For now we are doing 0 is 0.5, and full is double.
    private double getMultiplierForSlider(Slider slider) {
        double mult = 1;
        double val = slider.getValue();
        if (val == 50)
            mult = 1;
        else if (val > 50) {
            val -= 50;
            // So now our val is 0-50, make it so that 50 is 2
            // and 0 is 1.
            mult = (1 + (val / 50F));
        }
        else {
            // Now, val is 0-50, make it so that 50 is 1 and
            // 0 is 0.5
            mult = (0.5 + (val / 100F));
        }
        return mult;
    }

    public void submit(Values values) {
        // Apply changes.
        // Basically take what we have on the screen and apply it to current values.
        values.globalMoveRate = 
            Data.startingValues.globalMoveRate * getMultiplierForSlider(globalSlider);

        values.turnSeconds = cbDur.getSelectionModel().getSelectedIndex() + 1;

        values.rainOrigin = cbRain.getSelectionModel().getSelectedIndex();

        values.rainRate = 
            Data.startingValues.rainRate * getMultiplierForSlider(rainRateSlider);

        values.rainSpeedVar = 
            Data.startingValues.rainSpeedVar * getMultiplierForSlider(rainSpeedSlider);

        values.movingDotSpeedRate = 
            Data.startingValues.movingDotSpeedRate * getMultiplierForSlider(dotSpeed);

        values.movingDotSize = 
            Data.startingValues.movingDotSize * getMultiplierForSlider(dotSize);

        Color color = (Color)dotColor.getValue();
        // Extremely strange that I need to multiply by 255 in this case as sometimes
        // I seem to get back the 0-255 value .. but here I get back a 0-1 number.
        values.movingDotR = (int)(255.0 * color.getRed());
        values.movingDotG = (int)(255.0 * color.getGreen());
        values.movingDotB = (int)(255.0 * color.getBlue());

        values.gravityRules = cbGravity.getSelectionModel().getSelectedIndex();
        values.armorRules = cbArmor.getSelectionModel().getSelectedIndex();
        values.paradigm = cbParadigm.getSelectionModel().getSelectedIndex();
        values.growthRules = cbGrowth.getSelectionModel().getSelectedIndex();

        values.noTakeSize = chkSteal.isSelected();
        values.noGiveSize = chkGive.isSelected();
    }
}