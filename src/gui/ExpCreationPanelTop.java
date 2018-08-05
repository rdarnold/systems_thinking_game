
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
import javafx.beans.binding.Bindings;

import gos.*;

public class ExpCreationPanelTop extends AdjustOverallValuesPanel {

    ExpCreationPanelSet m_ParentPanelSet;
    
    private ExpCreationPanelTop thisScreen;
    
    public ExpCreationPanelTop(ExpCreationPanelSet panelSet) {
        super();
        thisScreen = this;
        m_ParentPanelSet = panelSet;
        getInfoArea().setName("Experiment Creation");

        Label lab = new Label("Use the sliders and drop-down selection boxes to " +
        "make changes to the system for this experiment.  Overall system parameters are along the top, " +
        "and shape-specific parameters are on the left side.");
        lab.setPrefWidth(instructionTextWidth);
        lab.setWrapText(true);
        super.m_MainVBox.getChildren().add(lab);

        Utils.addVerticalSpace(super.m_MainVBox, 60);
    }

    public void submit(Experiment exp) {
        super.submit(exp.getSnap().getValues());
    }
    
    @Override
    public void onChanged(Node node) { 
        m_ParentPanelSet.onValueChanged(node);
    }
}