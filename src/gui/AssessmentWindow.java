
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

import java.nio.file.Files; 
import java.nio.file.Paths;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
    
import gos.*;

public class AssessmentWindow extends Stage implements ClassInfo  {

    protected int m_nWidth;
    protected int m_nHeight;
    protected Scene m_Scene;
    protected VBox m_MainVBox;

    private HTMLEditor m_htmlEditor;
    private TextArea m_textArea;

    private TableView table = new TableView();

    private AssessmentWindow thisScreen;

    public static ArrayList<Action> loaded_actions = new ArrayList<Action>();
    //public static ArrayList<Answer> loaded_answers = new ArrayList<Answer>();

    public AssessmentWindow(int wid, int hgt) {
        //super(wid, hgt);

        //super(StageStyle.UNDECORATED);
        //super(StageStyle.TRANSPARENT); // Needed for cool fade ins
        //super(StageStyle.UTILITY); 
        super(StageStyle.DECORATED); 
        thisScreen = this;

        setTitle("Assessment Panel");

        m_nWidth = wid;
        m_nHeight = hgt;

        //initStyle(StageStyle.UNDECORATED);
        initOwner(Gos.stage);
        //initModality(Modality.APPLICATION_MODAL); 

        m_MainVBox = new VBox();
        m_MainVBox.setAlignment(Pos.TOP_CENTER);
        m_MainVBox.setPadding(new Insets(10));
        
        MenuBar menuBar = createMenuBar();//new MenuBar();
        m_MainVBox.getChildren().add(menuBar);

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
        /*m_textArea = new TextArea();
        m_textArea.setText("This is free space for you to take notes or write whatever you want.");
        m_textArea.setPrefHeight(2000);
        m_MainVBox.getChildren().add(m_textArea);*/

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

        
        /*MovableButton btn = new MovableButton("Load Player Data");
        btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                onClickLoad();
            }
        });
        m_MainVBox.getChildren().add(btn);*/
        table.setEditable(false);
        table.prefHeightProperty().bind(m_Scene.heightProperty());
 
        TableColumn col1 = new TableColumn("Action");
        col1.setCellValueFactory(new PropertyValueFactory<>("strAction"));
        col1.prefWidthProperty().bind(table.widthProperty().multiply(0.2));

        TableColumn col2 = new TableColumn("Time");
        col2.setCellValueFactory(new PropertyValueFactory<>("strTime"));
        col2.prefWidthProperty().bind(table.widthProperty().multiply(0.1));

        TableColumn col3 = new TableColumn("Screen");
        col3.setCellValueFactory(new PropertyValueFactory<>("fromScreen"));
        col3.prefWidthProperty().bind(table.widthProperty().multiply(0.2));

        TableColumn col4 = new TableColumn("Desc");
        col4.setCellValueFactory(new PropertyValueFactory<>("desc"));
        col4.prefWidthProperty().bind(table.widthProperty().multiply(0.5));
        
        table.getColumns().addAll(col1, col2, col3, col4);
        
        m_MainVBox.getChildren().add(table);
    }

    private MenuBar createMenuBar() {
        MenuBar bar = new MenuBar();
        Menu menuFile = new Menu("File");
        bar.getMenus().add(menuFile);

        //MenuItem add = new MenuItem("Shuffle",
            //new ImageView(new Image("menusample/new.png")));
        MenuItem add = new MenuItem("Load Player Data");
            add.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                onClickLoadPlayerData();
            }
        });        
 
        menuFile.getItems().addAll(add);
        return bar;
    }

    private void onClickLoadPlayerData() {
        List<String> lines = null;

        FileChooser fileChooser = new FileChooser(); 
        //String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
        fileChooser.setInitialDirectory(new File("C://Ross//Work//Japan//Drones//Code//systems_thinking_game_evolved//data"));   
        fileChooser.setTitle("Open Player Data File");      
        fileChooser.getExtensionFilters().addAll(
         new ExtensionFilter("Text Files", "*.txt"));                              
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile == null)
            return;

        try {
            lines = Files.readAllLines(Paths.get(selectedFile.getPath()));
        } catch (IOException ex) {
            Utils.log("Error loading player data");
        }

        /*for (String line : lines) {
            Utils.log(line);
        }*/

        processLoadedData(lines);

        // Now present it in some viewable fashion
        showLoadedData();
    } 

    private void showLoadedData() {
        table.getItems().clear();

        // We have all the actions loaded up in loaded_actions
        for (Action a : loaded_actions) {
            table.getItems().add(a);
        }
    }

    // We found an ACT: which is start of an action so now load the action
    // and return the index of the last line of the action whatever that was
    private int loadActionFromString(List<String> lines, int start) {
        int i = start;

        List<String> loadLines = new ArrayList<String>();
        loadLines.add(lines.get(i));
        i++;
        loadLines.add(lines.get(i));
        i++;
        loadLines.add(lines.get(i));
        i++;
        loadLines.add(lines.get(i));
        i++;

        // Now we just keep going until we hit another ACT: (or something else that terminates) as the rest are parts of AD
        String line = lines.get(i);
        while (line != null) {
            if (i >= lines.size()) {
                break;
            }
            line = lines.get(i);

            // Only more actions and the scratchpad come after actions right now.  that's just the way the file is set up.
            // It would be nice to future-proof this file and format such that it doesn't depend on the order of the contents.
            // But right now that would involve sending more data and I'm trying to keep it lean.
            if (line.length() >= ("ACT:").length() && line.substring(0, ("ACT:").length()).equals("ACT:") == true) {
                break;
            }
            if (line.length() >= ("ScratchPad:").length() && line.substring(0, ("ScratchPad:").length()).equals("ScratchPad:") == true) {
                break;
            }
            loadLines.add(line);
            i++;
        }

        Action act = new Action(loadLines);
        if (act != null) {
            // We just do this to accomodate my initial data set which had absolute instead of relative
            // timestamps
            act.timestamp -= 1561517543573L;
            loaded_actions.add(act);
        }
        loadLines.clear();
        return i;
    }

    private void processLoadedData(List<String> lines) {
        // So now we process it into some kind of action and answer lists that parallel
        // the ones the player usually has.  That's all the processing we do for now; we can
        // process more advanced stuff within the actions later after we load them up.

        int i = 0;
        for (i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line == null || line.length() < 4) {
                continue;
            }
            if (line.substring(0, 4).equals("ACT:")) {
                i = loadActionFromString(lines, i);
                i--; // Because we're gonna add to it in a second
            }

        }
        //loaded_actions
        //loaded_answers
    }
}