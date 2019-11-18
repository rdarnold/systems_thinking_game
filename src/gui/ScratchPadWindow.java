
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
import javafx.scene.web.WebView;
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

public class ScratchPadWindow extends Stage implements ClassInfo  {

    protected int m_nWidth;
    protected int m_nHeight;
    protected Scene m_Scene;
    protected VBox m_MainVBox;

    private HTMLEditor m_htmlEditor;
    private TextArea m_textArea;

    private ScratchPadWindow thisScreen;

    public ScratchPadWindow(int wid, int hgt) {
        //super(wid, hgt);

        //super(StageStyle.UNDECORATED);
        //super(StageStyle.TRANSPARENT); // Needed for cool fade ins
        //super(StageStyle.UTILITY); 
        super(StageStyle.DECORATED); 
        thisScreen = this;

        setTitle("Scratch Pad");

        m_nWidth = wid;
        m_nHeight = hgt;

        //initStyle(StageStyle.UNDECORATED);
        initOwner(Gos.stage);
        //initModality(Modality.APPLICATION_MODAL); 
        
        m_MainVBox = new VBox();
        m_MainVBox.setAlignment(Pos.CENTER);
        m_MainVBox.setPadding(new Insets(10));

        // If I don't set this it won't expand past ~700 pixels high
        m_MainVBox.setPrefHeight(2000); 
        //m_MainVBox.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        //m_MainVBox.setSpacing(10);

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

        // Using TextArea because HTMLEditor sucks and defaults to stupid
        // line spacing that can't be changed
        m_textArea = new TextArea();
        m_textArea.setText("This is free space for you to take notes or write whatever you want.");
        m_textArea.setPrefHeight(2000);
        m_MainVBox.getChildren().add(m_textArea);

        /*m_htmlEditor = new HTMLEditor();
        //String htmlRoot = "<body><style=\"line-height:1.0\"></style><font face=\"Times New Roman\"></font face></body></html>";
        //m_htmlEditor.setHtmlText(htmlRoot);
        
        m_htmlEditor.setStyle("-fx-font-size: 11pt; " + 
                              "-fx-line-spacing: 1.0em;");
        
        // If I don't set this it won't expand past ~700 pixels high
        m_htmlEditor.setPrefHeight(2000);
        m_MainVBox.getChildren().add(m_htmlEditor);*/

        
        //WebView webview = (WebView)m_htmlEditor.lookup("WebView");
        //GridPane.setHgrow(webview, Priority.ALWAYS);
        //GridPane.setVgrow(webview, Priority.ALWAYS);


        //m_htmlEditor.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        // Have to add this next bit to make the editor actually grow
        // to full vertical height.  Otherwise it maxes at like 600 or
        // 700 pixels for an unknown reason.
    }

    public String getText() {
        return m_textArea.getText();
    }

    public void setText(String str) {
        m_textArea.setText(str);
    }

    private void onClose() {
        // Save out the main text or whatever.
        // actually does not appear to be necessary
        close();
    }
}