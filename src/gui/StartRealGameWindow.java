
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

public class StartRealGameWindow extends DialogWindow {

    private Label titleLabel;
    private Label textLabel;
    private MovableButton okBtn;
    
    private StartRealGameWindow thisScreen;

    public StartRealGameWindow(int wid, int hgt) {
        super(wid, hgt);
        thisScreen = this;

        setup();
    }

    private void setup() {
        
        int space = 10;
        titleLabel = addCenteredLabel();
        Utils.addVerticalSpace(m_MainVBox, space);

        String str = "Now that the practice mode is over, the real game begins!";
        Label lbl = addLeftLabel(str);
        Utils.addVerticalSpace(m_MainVBox, space);

        textLabel = addLeftLabel();
        Utils.addVerticalSpace(m_MainVBox, space);

        Info info = Data.getInfoByName("Game Play");
        if (info != null) {
            titleLabel.setText(info.getName());
            textLabel.setText(info.getText());
        }

        str =  "Your Discovery Points are visible on the top left of the screen. " +
        "You'll notice two new buttons " +
        "on the left side of the main screen - Ask and Experiment.  Along with Observe, these three " +
        "buttons are the ways that you can spend Discovery Points.\r\n\r\n" +
        "Between turns, you'll also now be asked to rate the importance of the variables you can " +
        "change in the Change System screen.\r\n\r\nNew info is below. " +
        "You can always access all of this information using the Info button from the main screen.";
        lbl = addLeftLabel(str);
        
        Accordion accordion = new Accordion();
        VBox.setVgrow(accordion, Priority.ALWAYS);

        //info = Data.getInfoByName("System Description");
        //addOneInfoItem(accordion, info);
        info = Data.getInfoByName("Discovery Points");
        addOneInfoItem(accordion, info);
        info = Data.getInfoByName("Observe");
        addOneInfoItem(accordion, info);
        info = Data.getInfoByName("Ask");
        addOneInfoItem(accordion, info);
        info = Data.getInfoByName("Experiment");
        addOneInfoItem(accordion, info);
        addTempControl(accordion);
        
        okBtn = new MovableButton("OK");
        okBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //Gos.gos.showMainScreen();
                Player.recordButtonAction(event, thisScreen.className());
                onOK();
            }
        });
        addTempControl(okBtn);
    }

    @Override
    public void showAndWait() {
        super.showAndWait();
    }

    private void onOK() {
        close();
    }

    // Don't transition on the close but rather on the completion of the
    // fadeout.
    @Override
    protected void onFadeOutFinished() {
        super.onFadeOutFinished();
        
        // Now finally show the main stage, if it isn't showing already.
        //Gos.stage.show();

        // Ok so for now, we are actually going to skip to the observation
        // screen from here.
        //Gos.mainScene.showFirstPanelSet(); 
        //Gos.mainScene.showMainPanelSet();
    }
    
    public void addOneInfoItem(Accordion accordion, Info info) {
        VBox box = new VBox();
        VBox.setVgrow(box, Priority.ALWAYS);
        box.setPrefWidth(m_nWidth - 40);
        box.setMaxWidth(m_nWidth - 40);
        Text text = new Text(info.getText());
        text.setWrappingWidth(m_nWidth - 40);
        box.getChildren().add(text);
        Utils.addTitledPane(accordion, box, info.getName(), this.className());
    }
}