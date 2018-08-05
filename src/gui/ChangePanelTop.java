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

public class ChangePanelTop extends AdjustOverallValuesPanel {

    ChangePanelSet m_ParentPanelSet;

    private ChangePanelTop thisScreen;

    Label infoLabel;

    public ChangePanelTop(ChangePanelSet panelSet) {
        super();
        thisScreen = this;
        m_ParentPanelSet = panelSet;
        getInfoArea().setName("Change System"); //\r\nScreen");

        /*Label lab = new Label("Use the sliders and drop-down selection boxes to " +
        "make changes to the system.  Overall system parameters are along the top, " +
        "and shape-specific parameters are on the left side.  Click the 'Submit' button " +
        "when you are ready to submit your changes.");
        lab.setPrefWidth(instructionTextWidth);
        lab.setWrapText(true);
        super.m_MainVBox.getChildren().add(lab);

        Utils.addVerticalSpace(super.m_MainVBox, 20);*/

        infoLabel = new Label();
        infoLabel.setWrapText(true);
        infoLabel.setPrefWidth(instructionTextWidth);
        infoLabel.setStyle("-fx-font-weight: bold; ");
        infoLabel.textProperty().bind(Bindings.createStringBinding(() -> 
            "Task " + (Player.getCurrentTaskNumberTracker() + 1) +
            ", " + Player.getCurrentTaskNameTracker() +
            ": " + Player.getCurrentTaskTextTracker(),
            Player.currentTaskNumberTrackerProperty(),
            Player.currentTaskNameTrackerProperty(),
            Player.currentTaskTextTrackerProperty()
        ));
        super.m_MainVBox.getChildren().add(infoLabel);

        Utils.addVerticalSpace(super.m_MainVBox, 60);
    }

    public void submit() {
        super.submit(Data.currentValues);
    }
    
    @Override
    public void onChanged(Node node) { 
        m_ParentPanelSet.onValueChanged(node);
    }
}