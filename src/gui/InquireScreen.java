
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
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import gos.*;


// Thoughts, here are some things I might want to address here
/*

I don't really get how this works.
- You can view a guide on YouTube here.

I have too many spikes, how do I control them?
- You can turn the Growth to "No Growth" which prevents spikes from
being created.  This will also prevent your shapes from growing through
rain and circles, so use wisely.

My shapes keep bunching up and getting stuck.  How do I prevent this?
- You can change Gravity to "Reverse" to make your shapes push apart.  Also,
one of the shape types has a movement advantage.  This can be used to move
shapes more easily.

I keep sucking.
- Try thinking of the problem from different angles.  Keep both the forest and
the trees in mind - the overall and the specific parameters.  Both are important, 
sometimes together and sometimes at different times.  A successful strategy 
is likely to incorporate a number of factors, and will change with time as the
system evolves.

This is way too complicated!
- How bad could it be?  There are only 7 things you can change.  Try to simplify
the problem in your mind and focus on the most important parts.  Don't get lost
in the forest.



*/

// Gos - Game of Systems
public class InquireScreen extends GosSceneBase {
    ScrollPane scrollPane;
    VBox mainArea;
    VBox topArea;
    Text helperText;
    ArrayList<Node> tempControlsList;
    MovableButton doneButton;

    private InquireScreen thisScreen;

    public InquireScreen(Gos theGos, BorderPane root, int wid, int hgt) {
        super(theGos, root, wid, hgt);
        thisScreen = this;
    }

    @Override
    public void createBuildingBlocks() {
        tempControlsList = new ArrayList<Node>();
        getStylesheets().add("css/gos.main.css");
    }

    @Override
    public void createMainScene() {
        createMainArea();

        MovableButton btn;

        doneButton = new MovableButton("Back");
        doneButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Player.recordButtonAction(event, thisScreen.className());
                gos.showMainScreen();
            }
        });
        
        // This is the same as in the main screen.
        topArea = new VBox();
        topArea.prefWidthProperty().bind(overallRoot.widthProperty());
        //topArea.setPrefHeight(20);
        topArea.setPadding(new Insets(10, 10, 10, 10));
        topArea.setSpacing(10);
        
        helperText = new Text();
        //currentTaskText.wrappingWidthProperty().bind(topArea.widthProperty());
        helperText.setWrappingWidth(overallRoot.getWidth() - 20);
        topArea.getChildren().add(helperText);
        overallRoot.setTop(topArea);

        topArea.getChildren().add(doneButton);

        setBorders();
        update();
    }

    public void createMainArea() {
        scrollPane = new ScrollPane();
        mainArea = new VBox();
        mainArea.setPadding(new Insets(10));
        //mainArea.setHgap(10);
        //mainArea.setVgap(10);

        HBox.setHgrow(mainArea, Priority.ALWAYS);
        VBox.setVgrow(mainArea, Priority.ALWAYS);
        mainArea.setAlignment(Pos.TOP_LEFT);
        //mainArea.setPadding(new Insets(10, 10, 10, 10));
        mainArea.setSpacing(5);

        scrollPane.setContent(mainArea);
        overallRoot.setCenter(scrollPane);

        //build();
    }

    @Override
    public void finalizeCreation() {
    }

    @Override
    public void onGuiUpdateRequired() {
        update();
    }

    @Override
    public void reset() {
    }

    @Override
    public void update() {
        // This should just update the fields currently on the screen
        updateHelperText();
    }

    public void setBorders() {
        //mainArea.setStyle("-fx-border-color: darkgray;");
        //topArea.setStyle("-fx-border-color: darkgray;");
    }

    public void addOneInfoItem(Accordion accordion, Info info) {
        VBox box = new VBox();
        VBox.setVgrow(box, Priority.ALWAYS);
        box.setPrefWidth(overallRoot.getWidth() - 30);
        box.setMaxWidth(overallRoot.getWidth() - 30);
        Text text = new Text(info.getText());
        text.setWrappingWidth(overallRoot.getWidth() - 40);
        box.getChildren().add(text);
        Utils.addTitledPane(accordion, box, info.getName(), this.className());
    }

    public void build() {
        Accordion accordion = new Accordion();
        VBox.setVgrow(accordion, Priority.ALWAYS);
        for (Info info : Data.infoList) {
            addOneInfoItem(accordion, info);
        }
        mainArea.getChildren().add(accordion);
    }

    public void updateHelperText() {
    }
}