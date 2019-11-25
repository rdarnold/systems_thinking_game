
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
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.IntegerProperty;

import gos.*;

public class ObservePanelLeft extends PanelLeftBase {
    
    ObservePanelSet m_ParentPanelSet;

    private Label maxTurnText;
    private MovableButton addTurnBtn;
    
    private ObservePanelLeft thisScreen;

    public ObservePanelLeft(ObservePanelSet panelSet) {
        super();
        thisScreen = this;
        m_ParentPanelSet = panelSet;
        create();
    }

    private void create() {
        //dialog = new CreateExperimentDialog();
        
        MovableButton btn;
        String str;
        Tooltip toolTip;
        int ttWidth = 300;

        //Label label = new Label("Observation");
        //label.setPadding(new Insets(20));
        //getChildren().add(label);

        /*btn = new MovableButton("Help");
        //btn.moveTo(0, 200);
        btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                m_ParentPanelSet.forceShowOKWindow();
            }
        });
        getChildren().add(btn);*/

        // Instead of this, I should really set it up so that I can have stuff
        // in the top, on the bottom, then in the middle for a more consistent
        // user experience.  It should be like,
        // - Name of screen (better yet - this goes at the top of the screen)
        // - Help button
        // - Any other points / turns
        //  < various buttons >
        // - Main Screen


        maxTurnText = new Label();
        //getChildren().add(maxTurnText);
        // TODO This doesnt work, it has to be a property.
        maxTurnText.textProperty().bind(Player.getObservation().maxTurnsProperty().asString(
            "Observation Turns: %d"
        ));

        addTurnBtn = new MovableButton("Add Turn");
        btn = addTurnBtn;
        str = "Spend 1 Discovery Point to be able to observe for one extra turn.";
        Utils.addToolTip(btn, str);
        //btn.moveTo(0, 200);
        btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Player.recordButtonAction(event, thisScreen.className());
                //Gos.gos.showExperimentScreen();
                
                if (Gos.checkDiscoveryPoints() == false)
                    return;
                //if (Player.getDiscoveryPoints() > 0) {
                    Player.subDiscoveryPoints();
                    //Player.addMaxObservationTurns();
                    Player.getObservation().addMaxTurns();
                    Gos.gos.mainScene.update();
                //}
                //showChooseExperimentTypeWindow();
            }
        });
        //getChildren().add(btn);
    }

    public void reset() {
        update();
    }

    public void update() {
        if (Player.inPracticeMode() == true) {
            addTurnBtn.setVisible(false);
            maxTurnText.setVisible(false);
        }
        else {
            addTurnBtn.setVisible(true);
            maxTurnText.setVisible(true);
        }
    }
}