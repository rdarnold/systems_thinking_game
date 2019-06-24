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
//import javafx.scene.layout.ColumnConstraintsBuilder;
import javafx.scene.layout.ColumnConstraints;
 
import gos.*;

public class InquirePanelTop extends PanelTopBase {

    // Add more configs of different sets of controls here if needs be.
    enum Configs {
        None,
        Default;
    }

    // If the config is the same we don't need to remove and re-add everything.
    private Configs config = Configs.None; 

    GridPane mainArea;

    SysShape selected = null;

    RevealButton rvRainSpeed;
    RevealButton rvRainValue;
    RevealButton rvRainPerSecond;

    RevealButton rvDotSize;
    RevealButton rvDotSpeed;
    RevealButton rvDotValue;

    RevealButton rvGivenSize;
    RevealButton rvStolenSize;
    //RevealButton rvGravity;
    RevealButton rvTotalSize;

    InquirePanelSet m_ParentPanelSet;

    private InquirePanelTop thisScreen;

    public InquirePanelTop(InquirePanelSet panelSet) {
        super();
        thisScreen = this;
        m_ParentPanelSet = panelSet;
        getInfoArea().setName("Ask Screen");
    }

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
    public void updateOneFrame(boolean running, Turn currentTurn) { 

    }
    
    public void setupOneItem(Node node, int row, int col) {
        GridPane.setRowIndex(node, row);
        GridPane.setColumnIndex(node, col);
        GridPane.setHalignment(node, HPos.CENTER);

        /*if (ChoiceBox.class.isInstance(node)) {
            ((ChoiceBox)node).getSelectionModel().selectFirst();
        }*/
    }

    public void setup(Configs configType) {
        //clearStyles();
        if (config == configType) {
            setValues();
            return;
        }

        if (mainArea == null) {
            mainArea = new GridPane();
            mainArea.setPadding(new Insets(10));
            mainArea.setHgap(10);
            mainArea.setVgap(10);
            super.addToRightSide(mainArea);

            /*mainArea.getColumnConstraints().setAll(
                ColumnConstraintsBuilder.create().percentWidth(100/3.0).build(),
                ColumnConstraintsBuilder.create().percentWidth(100/3.0).build(),
                ColumnConstraintsBuilder.create().percentWidth(100/3.0).build());*/
        
            ColumnConstraints col1 = new ColumnConstraints();
            col1.setPercentWidth(100/3.0);
            ColumnConstraints col2 = new ColumnConstraints();
            col2.setPercentWidth(100/3.0);
            ColumnConstraints col3 = new ColumnConstraints();
            col3.setPercentWidth(100/3.0);
            mainArea.getColumnConstraints().addAll(col1,col2,col3);
        }

        removeControls();

        Node node;

        int row = 0;
        int col = 0;

        rvRainSpeed = new RevealButton("Rain Speed");
        rvRainSpeed.addToolTip("The speed range of the rain.");
        node = addRevealButton(rvRainSpeed);
        setupOneItem(node, row++, col);

        rvRainValue = new RevealButton("Rain Value");
        rvRainValue.addToolTip("The amount that the rain affects shapes it touches.");
        node = addRevealButton(rvRainValue);
        setupOneItem(node, row++, col);

        rvRainPerSecond = new RevealButton("Rain Per Second");
        rvRainPerSecond.addToolTip("How many rain drops are produced, on average, each second.");
        node = addRevealButton(rvRainPerSecond);
        setupOneItem(node, row++, col);

        row = 0;
        col = 1;
        rvDotSpeed = new RevealButton("Circle Speed");
        rvDotSpeed.addToolTip("The speed range of the moving circles.");
        node = addRevealButton(rvDotSpeed);
        setupOneItem(node, row++, col);

        rvDotValue = new RevealButton("Circle Value");
        rvDotValue.addToolTip("The amount that each moving circle affects the shapes it touches.");
        node = addRevealButton(rvDotValue);
        setupOneItem(node, row++, col);

        rvDotSize = new RevealButton("Circle Size");
        rvDotSize.addToolTip("The size of the moving circles.");
        node = addRevealButton(rvDotSize);
        setupOneItem(node, row++, col);

        row = 0;
        col = 2;
        rvTotalSize = new RevealButton("Total Size");
        rvTotalSize.addToolTip("The sum of sizes across all shapes in the scenario.");
        node = addRevealButton(rvTotalSize);
        setupOneItem(node, row++, col);

        rvStolenSize = new RevealButton("Total Size Stolen");
        rvStolenSize.addToolTip("The total amount of size (mass) that shapes have stolen from each other.");
        node = addRevealButton(rvStolenSize);
        setupOneItem(node, row++, col);

        rvGivenSize = new RevealButton("Total Size Given");
        rvGivenSize.addToolTip("The total amount of size (mass) that shapes have given to each other.");
        node = addRevealButton(rvGivenSize);
        setupOneItem(node, row++, col);

        // Number of dots currently in the system

        // This should be at the end so we can check to see if we have configured yet.
        config = configType;
        update();
    }

    public void setValues() {
        if (config == Configs.None) {
            return;
        }
        
        String str;
        str = ((Data.currentValues.rainSpeedVar * 2F) + " - " + (Data.currentValues.rainSpeedVar * 4F));
        rvRainSpeed.setText(str);
        rvRainValue.setValue(SysShape.getAmountPerRain());
        rvTotalSize.setValue(Gos.sim.calcTotalSize());

        rvDotSize.setValue(Data.currentValues.movingDotSize);
        str = ((Data.currentValues.movingDotSpeedRate * 1F) + " - " + 
            (Data.currentValues.movingDotSpeedRate * 2F));
        rvDotSpeed.setText(str);
        rvDotValue.setValue(SysShape.getAmountPerPatch());
        
        str = String.format("%.2f", Gos.sim.calcRainDropsPerSecond());
        rvRainPerSecond.setText(str);
        rvStolenSize.setValue(Gos.sim.calcTotalSizeStolen());
        rvGivenSize.setValue(Gos.sim.calcTotalSizeGiven());
    }
    
    @Override
    public void update() {
        super.update();

        // Update the RevealButton values as they may have changed since we last came here.
        setValues();
    }
}