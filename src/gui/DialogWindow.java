package gos.gui;

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
import javafx.util.Duration;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.Transition;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Rectangle2D;
    
import gos.*;

public class DialogWindow extends Stage implements ClassInfo  {

    DialogWindow m_thisDialogWindow;

    int m_nWidth;
    int m_nHeight;
    Scene m_Scene;
    VBox m_MainVBox;

    ArrayList<Node> m_nTempControls = new ArrayList<Node>();

    private Timeline fadeInTimeline;
    private Timeline fadeOutTimeline;

    // Variables to handle dragging the window around
    private double m_nPressedX;
    private double m_nPressedY;
    private double m_nDraggedX;
    private double m_nDraggedY;

    public DialogWindow(int wid, int hgt) {
        //super(StageStyle.UNDECORATED);
        super(StageStyle.TRANSPARENT); // Needed for cool fade ins

        m_thisDialogWindow = this;

        m_nWidth = wid;
        m_nHeight = hgt;

        //initStyle(StageStyle.UNDECORATED);
        initOwner(Gos.stage);
        initModality(Modality.APPLICATION_MODAL); 
        
        m_MainVBox = new VBox();
        m_MainVBox.setAlignment(Pos.CENTER);
        m_MainVBox.setPadding(new Insets(10));
        m_MainVBox.setSpacing(10);

        m_Scene = new Scene(m_MainVBox, wid, hgt);
        m_Scene.getStylesheets().add("css/gos.main.css");
        m_Scene.setFill(Color.TRANSPARENT); //Makes scene background transparent
        
        m_MainVBox.setStyle("-fx-border-color: black; " +
                        "-fx-border-radius: 3; " +
                        //"-fx-border-insets: 3 2 1 0; " +
                        "-fx-border-width: 2;");
        //box.setStyle("-fx-background-insets: 0 0 -1 0, 0, 1, 2;");
        //box.setStyle("-fx-background-radius: 3px, 3px, 2px, 1px;");
        setScene(m_Scene);

        //dialog.initStyle(StageStyle.UNDECORATED);
        setResizable(false);
        setMaxWidth(wid);
        setMinWidth(wid);
        setMaxHeight(hgt);
        setMinHeight(hgt);
        
        fadeInTimeline = new Timeline();
        KeyFrame key = new KeyFrame(Duration.millis(400),
                        new KeyValue (getScene().getRoot().opacityProperty(), 1)); 
        fadeInTimeline.getKeyFrames().add(key);   

        fadeOutTimeline = new Timeline();
        key = new KeyFrame(Duration.millis(400),
                        new KeyValue (getScene().getRoot().opacityProperty(), 0)); 
        fadeOutTimeline.getKeyFrames().add(key);   
        fadeOutTimeline.setOnFinished((ae) -> onFadeOutFinished());

        // Add the drag and press listener, this allows our window
        // to be dragged around despite not having the system bar, which
        // is just nice and convenient for the user.
        m_MainVBox.setOnMousePressed(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent e) {
                m_nPressedX = e.getScreenX();
                m_nPressedY = e.getScreenY();
            }
        });

        m_MainVBox.setOnMouseDragged(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent e) {
                m_nDraggedX = e.getScreenX();
                m_nDraggedY = e.getScreenY();

                double differenceX = m_nDraggedX - m_nPressedX;
                double differenceY = m_nDraggedY - m_nPressedY;
                
                m_nPressedX = m_nDraggedX;
                m_nPressedY = m_nDraggedY;

                m_thisDialogWindow.setX(m_thisDialogWindow.getX() + differenceX);
                m_thisDialogWindow.setY(m_thisDialogWindow.getY() + differenceY); 
            }
        });
    }

    @Override
    public void showAndWait() {
        // If we are already showing, don't try to show again.
        if (this.isShowing() == true) {
            return;
        }

        getScene().getRoot().opacityProperty().set(0);

        if (Gos.stage.isShowing() == true) {
            // If stage is visible, center on the stage
            setX(Gos.stage.getX() + Gos.stage.getWidth() / 2 - m_nWidth / 2);
            setY(Gos.stage.getY() + Gos.stage.getHeight() / 2 - m_nHeight / 2);
        }
        else {
            // Otherwise, center on the screen.
            Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
            setX((bounds.getWidth() / 2) - (m_nWidth / 2));
            setY((bounds.getHeight() / 2) - (m_nHeight / 2));
        }
        
        /*Timeline fadeInTimeline = new Timeline();
        KeyFrame key = new KeyFrame(Duration.millis(400),
                        new KeyValue (getScene().getRoot().opacityProperty(), 1)); 
        fadeInTimeline.getKeyFrames().add(key);   */
        fadeInTimeline.play();
        super.showAndWait();
    }

    @Override
    public void close() {
        //FadeTransition ft = new FadeTransition(Duration.millis(3000), this);
        //ft.setFromValue(1);
        //ft.setToValue(0);
        //ft.play();

        /*Timeline fadeOutTimeline = new Timeline();
        KeyFrame key = new KeyFrame(Duration.millis(400),
                        new KeyValue (getScene().getRoot().opacityProperty(), 0)); 
        fadeOutTimeline.getKeyFrames().add(key);   
        fadeOutTimeline.setOnFinished((ae) -> super.close()); */
        fadeOutTimeline.play();
    }

    protected void onFadeOutFinished() {
        super.close();
    }

    protected void removeTempControl(Node node) {
        m_MainVBox.getChildren().remove(node);
        m_nTempControls.remove(node);
    }

    protected void removeTempControls() {
        m_MainVBox.getChildren().removeAll(m_nTempControls);
        m_nTempControls.clear();
    }

    protected void addTempControl(Node node) {
        m_MainVBox.getChildren().add(node);
        m_nTempControls.add(node);
    }
    
    protected Label addCenteredLabel() {
        return addCenteredLabel("");
    }

    protected Label addCenteredLabel(String str) {
        Label text = new Label(str);
        text.setWrapText(true);
        text.setMaxWidth(m_nWidth - 20);
        text.setAlignment(Pos.CENTER);
        text.setTextAlignment(TextAlignment.CENTER);
        addTempControl(text);
        return text;
    }

    protected Label addLeftLabel() {
        return addLeftLabel("");
    }

    protected Label addLeftLabel(String str) {
        Label text = new Label(str);
        text.setWrapText(true);
        text.setMaxWidth(m_nWidth - 20);
        text.setAlignment(Pos.CENTER_LEFT);
        text.setTextAlignment(TextAlignment.LEFT);
        addTempControl(text);
        return text;
    }
}