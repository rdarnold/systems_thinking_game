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
import javafx.stage.Stage;
import javafx.stage.Screen; 
import javafx.stage.Modality;
import javafx.stage.StageStyle;
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
import javafx.geometry.Rectangle2D;
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
import javafx.scene.text.TextAlignment;

import gos.*;

public class ObservePanelTop extends PanelTopBase {

    ObservePanelSet m_ParentPanelSet;

    Label instructionText;
    
    private ObservePanelTop thisScreen;

    public ObservePanelTop(ObservePanelSet panelSet) {
        super();
        thisScreen = this;
        m_ParentPanelSet = panelSet;
        create();
    }

    private void create() {
        getInfoArea().setName("Observe Screen");

        instructionText = new Label();
        instructionText.setWrapText(true);
        instructionText.setPrefWidth(instructionTextWidth);
        update();

        super.addToRightSide(instructionText);
    }

    @Override
    public void update() {
        super.update();
        
        instructionText.setText("Click the 'Play All' button to watch the system for 3 turns. Click " +
        "the '>' button to just watch the system through a single turn. After clicking one of the Play buttons, " +
        "You can drag the slider back " +
        "and forth to check what the system is doing at different points in time." +
        "When you are finished observing, click the 'Main Screen' button to go to the main " +
        "screen and continue the game.  You can return to this screen from the main screen " +
        "at any time.");
        /*if (Player.inPracticeMode()) {
            instructionText.setText("Click the 'Play All' button to watch the system for 3 turns. Click " +
            "the '>' button to just watch the system through a single turn. You can drag the slider back " +
            "and forth to check what the system is doing at diffrent points in time." +
            "When you are finished observing, click the 'Main Screen' button to go to the main " +
            "screen and continue the game.  You can return to this screen from the main screen " +
            "at any time.");
        }
        else {
            instructionText.setText("Click the 'Play All' button to watch the system for 3 turns. Click " +
            "the '>' button to just watch the system through a single turn. You can drag the slider back " +
            "and forth to check what the system is doing at diffrent points in time." +
            "When you are finished observing, click the 'Main Screen' button to go to the main " +
            "screen and continue the game.  You can return to this screen from the main screen " +
            "at any time.");
        }*/

    }
}