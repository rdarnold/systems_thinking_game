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
import javafx.scene.layout.Region;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
/*import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.CheckBox;
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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

import gos.*;

public class InquirePanelSet extends PanelSetBase {

    InquirePanelBottom panelBottom;
    InquirePanelLeft panelLeft;
    InquirePanelTop panelTop;

    public static OKWindow inquirePanelWindow;

    public InquirePanelSet(MainScreen main) {
        super(main);

        panelBottom = new InquirePanelBottom(this);
        panelLeft = new InquirePanelLeft(this);
        panelTop = new InquirePanelTop(this);

        addPanels(panelLeft, panelTop, panelBottom);
        
        // Should maybe have a helper.xml file where all the helper text is,
        // the stuff that isn't in the info area
        String strTitle = "Ask About the System";
        String strText = 
        "On this screen, you can reveal specific numeric values about the system. " +
        "The left panel shows values that relate to the selected shape, which is displayed " +
        "in the panel.  Click other shapes to select them.  The top panel holds overall " +
        "values not related to any single shape.  Click on any of the buttons to spend " +
        "a Discovery Point and reveal the associated number.  Remember that you only have a " +
        "limited supply of Discovery Points to spend, and you may need them for experimentation " +
        "and observation.  Choose wisely.";
        inquirePanelWindow = new OKWindow(400, 500, strTitle, strText);

        // And set the helper text for the info area
        panelTop.getInfoArea().setHelpText(strText);
    }

    public void forceShowOKWindow() {
        inquirePanelWindow.showAndWait(true);
    }

    @Override
    public void show() {
        panelLeft.setup(InquirePanelLeft.Configs.Default);
        panelTop.setup(InquirePanelTop.Configs.Default);
        super.show();
        inquirePanelWindow.showAndWait();
    }

    @Override
    public void onSelectedShapeUpdated() {
        if (panelLeft != null && panelLeft.isVisible() == true) {
            panelLeft.setSelectedPanelShape();
        }
    }

    @Override
    public void updateOneFrame(boolean running, Turn currentTurn) {
        if (panelLeft != null && panelLeft.isVisible() == true) {
            panelLeft.updateOneFrame(running, currentTurn);
        }
    }

    // For this screen it is labeled as the "Back" button
    public void onCancelButton() {
        parent.onCancelButton();
    }
}