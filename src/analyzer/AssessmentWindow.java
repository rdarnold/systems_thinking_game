
package gos.analyzer;

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
import gos.analyzer.STUtils;
import gos.gui.*;

public class AssessmentWindow extends Stage implements ClassInfo  {

    protected int m_nWidth;
    protected int m_nHeight;
    protected Scene m_Scene;
    protected VBox m_MainVBox;

    private HTMLEditor m_htmlEditor;
    private TextArea m_textArea;

    private TableView actionListTable;

    private AssessmentWindow thisScreen;

    // The controls so we can populate them later

    // For the General data area
    TextField tfId = new TextField();
    TextField tfName = new TextField();
    TextField tfEmail = new TextField();
    TextField tfScore1 = new TextField();
    TextField tfScore2 = new TextField();
    TextField tfScore3 = new TextField();
    TextField tfScore4 = new TextField();
    TextField tfPracticeResults = new TextField();
    TextField tfPlayedTimes = new TextField();

    TextField tfTimeStart = new TextField();
    TextField tfTimeEnd = new TextField();
    TextField tfTotalTime = new TextField();
    TextField tfTotalTimeExp = new TextField();
    TextField tfTotalTimeObs = new TextField();
    TextField tfNumExp = new TextField();

    TextField tfSTScore  = new TextField();
    TextField tfDomain1  = new TextField();
    TextField tfDomain2  = new TextField();
    TextField tfDomain3  = new TextField();
    TextField tfDomain4  = new TextField();
    TextField tfSkill11  = new TextField();
    TextField tfSkill12  = new TextField();
    TextField tfSkill13  = new TextField();
    TextField tfSkill14  = new TextField();
    TextField tfSkill15  = new TextField();
    TextField tfSkill21  = new TextField();
    TextField tfSkill22  = new TextField();
    TextField tfSkill23  = new TextField();
    TextField tfSkill31  = new TextField();
    TextField tfSkill32  = new TextField();
    TextField tfSkill33  = new TextField();
    TextField tfSkill34  = new TextField();
    TextField tfSkill41  = new TextField();
    TextField tfSkill42  = new TextField();
    TextField tfSkill43  = new TextField();
    TextField tfSkill44  = new TextField();

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

        // Let's make 4 rows:
        // 1: General
        // 2: Demographics list
        // 3: Exercise summary
        // 4: Answer list
        // 5: Action list
        HBox generalRow = new HBox();
        HBox demographicsRow = new HBox();
        HBox exerciseSummaryRow = new HBox();
        HBox answerListRow = new HBox();
        HBox actionListRow = new HBox();

        createGeneralData(generalRow);

        // First add all the meta data, who the person is, email address, time started, time taken to do
        // various things, and general scores for each of the scenarios

        // Now add the list of actions
        actionListTable = createActionList();
        //m_MainVBox.getChildren().add(actionListTable);
        actionListRow.getChildren().add(actionListTable);

        m_MainVBox.getChildren().add(generalRow);
        m_MainVBox.getChildren().add(demographicsRow);
        m_MainVBox.getChildren().add(exerciseSummaryRow);
        m_MainVBox.getChildren().add(answerListRow);
        m_MainVBox.getChildren().add(actionListRow);
    }

    private void createGeneralData(HBox generalRow) {
        // Within General, we want 3 columns
        VBox col1 = new VBox();
        VBox col2 = new VBox();
        VBox col3 = new VBox();
        VBox col4 = new VBox();
        generalRow.getChildren().add(col1);
        generalRow.getChildren().add(col2);
        generalRow.getChildren().add(col3);
        generalRow.getChildren().add(col4);

        tfId.setEditable(false);
        tfName.setEditable(false);
        tfEmail.setEditable(false);
        tfScore1.setEditable(false);
        tfScore2.setEditable(false);
        tfScore3.setEditable(false);
        tfScore4.setEditable(false);
        tfPracticeResults.setEditable(false);
        tfPlayedTimes.setEditable(false);
    
        tfTimeStart.setEditable(false);
        tfTimeEnd.setEditable(false);
        tfTotalTime.setEditable(false);
        tfTotalTimeExp.setEditable(false);
        tfTotalTimeObs.setEditable(false);
        tfNumExp.setEditable(false);

        tfSTScore.setEditable(false);
        tfDomain1.setEditable(false);
        tfDomain2.setEditable(false);
        tfDomain3.setEditable(false);
        tfDomain4.setEditable(false);

        tfSkill11.setEditable(false);
        tfSkill12.setEditable(false);
        tfSkill13.setEditable(false);
        tfSkill14.setEditable(false);
        tfSkill15.setEditable(false);
        tfSkill21.setEditable(false);
        tfSkill22.setEditable(false);
        tfSkill23.setEditable(false);
        tfSkill31.setEditable(false);
        tfSkill32.setEditable(false);
        tfSkill33.setEditable(false);
        tfSkill34.setEditable(false);
        tfSkill41.setEditable(false);
        tfSkill42.setEditable(false);
        tfSkill43.setEditable(false);
        tfSkill44.setEditable(false);
        
        col1.getChildren().add(tfId);
        col1.getChildren().add(tfName);
        col1.getChildren().add(tfEmail);
        col1.getChildren().add(tfPracticeResults);
        col1.getChildren().add(tfPlayedTimes);
        col1.getChildren().add(tfScore1);
        col1.getChildren().add(tfScore2);
        col1.getChildren().add(tfScore3);
        col1.getChildren().add(tfScore4);

        col2.getChildren().add(tfTimeStart);
        col2.getChildren().add(tfTimeEnd);
        col2.getChildren().add(tfTotalTime);
        col2.getChildren().add(tfTotalTimeExp);
        col2.getChildren().add(tfTotalTimeObs);
        col2.getChildren().add(tfNumExp);

        col3.getChildren().add(tfSTScore);
        col3.getChildren().add(tfDomain1);
        col3.getChildren().add(tfDomain2);
        col3.getChildren().add(tfDomain3);
        col3.getChildren().add(tfDomain4);
        
        col4.getChildren().add(tfSkill11);
        col4.getChildren().add(tfSkill12);
        col4.getChildren().add(tfSkill13);
        col4.getChildren().add(tfSkill14);
        col4.getChildren().add(tfSkill15);
        col4.getChildren().add(tfSkill21);
        col4.getChildren().add(tfSkill22);
        col4.getChildren().add(tfSkill23);
        col4.getChildren().add(tfSkill31);
        col4.getChildren().add(tfSkill32);
        col4.getChildren().add(tfSkill33);
        col4.getChildren().add(tfSkill34);
        col4.getChildren().add(tfSkill41);
        col4.getChildren().add(tfSkill42);
        col4.getChildren().add(tfSkill43);
        col4.getChildren().add(tfSkill44);

        int wid = 350;
        tfSkill11.setPrefWidth(wid);
        tfSkill12.setPrefWidth(wid);
        tfSkill13.setPrefWidth(wid);
        tfSkill14.setPrefWidth(wid);
        tfSkill15.setPrefWidth(wid);
        tfSkill21.setPrefWidth(wid);
        tfSkill22.setPrefWidth(wid);
        tfSkill23.setPrefWidth(wid);
        tfSkill31.setPrefWidth(wid);
        tfSkill32.setPrefWidth(wid);
        tfSkill33.setPrefWidth(wid);
        tfSkill34.setPrefWidth(wid);
        tfSkill41.setPrefWidth(wid);
        tfSkill42.setPrefWidth(wid);
        tfSkill43.setPrefWidth(wid);
        tfSkill44.setPrefWidth(wid);
    }

    private TableView createActionList() {
        TableView table = new TableView();
        table.setEditable(false);
        table.prefHeightProperty().bind(m_Scene.heightProperty());
        table.prefWidthProperty().bind(m_Scene.widthProperty());
 
        TableColumn col1 = new TableColumn("Action");
        col1.setCellValueFactory(new PropertyValueFactory<>("strAction"));
        col1.prefWidthProperty().bind(table.widthProperty().multiply(0.2));
        col1.setCellFactory(tc -> {
            TableCell<Object, String> cell = new TableCell<Object, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(item);
                }
            };

            cell.setOnMouseClicked(event -> {
                if (!cell.isEmpty()) {
                    Utils.log("Click on column 1, row " + cell.getIndex());
                }
            });
            return cell;
        });

        TableColumn col2 = new TableColumn("Time");
        col2.setCellValueFactory(new PropertyValueFactory<>("strTime"));
        col2.prefWidthProperty().bind(table.widthProperty().multiply(0.1));
        col2.setCellFactory(tc -> {
            TableCell<Object, String> cell = new TableCell<Object, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(item);
                }
            };

            cell.setOnMouseClicked(event -> {
                if (!cell.isEmpty()) {
                    Utils.log("Click on column 2, row " + cell.getIndex());
                }
            });
            return cell;
        });

        TableColumn col3 = new TableColumn("Screen");
        col3.setCellValueFactory(new PropertyValueFactory<>("fromScreen"));
        col3.prefWidthProperty().bind(table.widthProperty().multiply(0.25));
        col3.setCellFactory(tc -> {
            TableCell<Object, String> cell = new TableCell<Object, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(item);
                }
            };

            cell.setOnMouseClicked(event -> {
                if (!cell.isEmpty()) {
                    Utils.log("Click on column 3, row " + cell.getIndex());
                }
            });
            return cell;
        });

        TableColumn col4 = new TableColumn("Ex/Tsk/Trn");
        col4.setCellValueFactory(new PropertyValueFactory<>("exTaskTurn"));
        col4.prefWidthProperty().bind(table.widthProperty().multiply(0.1));
        col4.setCellFactory(tc -> {
            TableCell<Object, String> cell = new TableCell<Object, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(item);
                }
            };

            cell.setOnMouseClicked(event -> {
                if (!cell.isEmpty()) {
                    Utils.log("Click on column 4, row " + cell.getIndex());
                }
            });
            return cell;
        });

        TableColumn col5 = new TableColumn("Desc");
        col5.setCellValueFactory(new PropertyValueFactory<>("desc"));
        col5.prefWidthProperty().bind(table.widthProperty().multiply(0.45));
        col5.setCellFactory(tc -> {
            TableCell<Object, String> cell = new TableCell<Object, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    // Need to null it out beforehand otherwise for some reason random cells
                    // start to get populated with the Before/After buttons
                    setText("");
                    setGraphic(null);
                    super.updateItem(item, empty);

                    // If it starts with BEFORE_VALUES then it's a SUBMIT in which case we can show buttons
                    if (item != null && item.length() >= ("BEFORE_VALUES").length() && item.substring(0, ("BEFORE_VALUES").length()).equals("BEFORE_VALUES") == true) {
                        Button beforeBtn = new Button("Before");
                        beforeBtn.setPrefHeight(15);
                        beforeBtn.setPrefWidth(100);
                        beforeBtn.setOnAction(event -> {
                            Action action = (Action)getTableView().getItems().get(getIndex());
                            onClickBefore(action);
                        });
                
                        Button afterBtn = new Button("After");
                        afterBtn.setPrefHeight(15);
                        afterBtn.setPrefWidth(100);
                        afterBtn.setOnAction(event -> {
                            Action action = (Action)getTableView().getItems().get(getIndex());
                            onClickAfter(action);
                        });

                        HBox pane = new HBox(beforeBtn, afterBtn);
                        setGraphic(pane);
                        setText("");
                        return;
                    }

                    // Otherwise just set the text
                    setText(item);
                }
            };
 
            cell.setOnMouseClicked(event -> {
                if (!cell.isEmpty()) {
                    Utils.log("Click on column 5, row " + cell.getIndex());
                }
            });
            return cell;
        });
        
        table.getColumns().addAll(col1, col2, col3, col4, col5); 
        return table;
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

    private void onClickBefore(Action action) {
        Utils.log("Timestamp is: " + action.getStrTime());

        // Show the system as we loaded in desc
        Gos.mainScene.showChangePanelSet(); 
    }

    private void onClickAfter(Action action) {
        Utils.log("Timestamp is: " + action.getStrTime());
        
        // Show the system as we loaded in desc
        Gos.mainScene.showChangePanelSet(); 
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

        Player.loadPlayerData(lines);
       // processLoadedData(lines);

        // Now present it in some viewable fashion
        showLoadedData();
    } 

    private String printTutorialDataString() {
        String ret = "";

        // Figure out how to display tutorial data
        String tdata = Player.getTutorialData();

        Utils.log(tdata);

        if (tdata.indexOf("TURN 1: GOOD") != -1) { ret += "Y, "; } else { ret += "N, "; }
        if (tdata.indexOf("TURN 2: GOOD") != -1) { ret += "Y, "; } else { ret += "N, "; }
        if (tdata.indexOf("TURN 3: GOOD") != -1) { ret += "Y, "; } else { ret += "N, "; }
        if (tdata.indexOf("TURN 4: GOOD") != -1) { ret += "Y, "; } else { ret += "N, "; }
        if (tdata.indexOf("TURN 5: GOOD") != -1) { ret += "Y"; } else { ret += "N"; }

        return ret;
    }

    private void showLoadedGeneralData() {
        tfId.setText("ID: " + Player.getId());
        tfName.setText("Name: " + Player.getName());
        tfEmail.setText("Email: " + Player.getEmail());
        tfPracticeResults.setText("Tutorial: " + printTutorialDataString());        
        tfPlayedTimes.setText("Played Times: " + Player.getTimesPlayed());
        tfScore1.setText("Four Shapes R1: ");
        tfScore2.setText("Four Shapes R2: ");
        tfScore3.setText("Chaos R1: ");
        tfScore4.setText("Chaos R2: ");

        // Now we want, total time spent experimenting, total experiments done, total time spent observing
        tfTimeStart.setText("Start Time: ");
        tfTimeEnd.setText("End Time: ");
        tfTotalTime.setText("Total Time: ");
        tfTotalTimeExp.setText("Total Time Exp: ");
        tfTotalTimeObs.setText("Total Time Obs: ");
        tfNumExp.setText("Num Experiments: ");

        tfSTScore.setText("Systems Thinking Score: ");
        tfDomain1.setText("Mindset: ");
        tfDomain2.setText("Structure: ");
        tfDomain3.setText("Content: ");
        tfDomain4.setText("Behavior: ");

        tfSkill11.setText("1.1 Explore Multiple Perspectives: " + STUtils.calc(STUtils.STSkills.ExploreMultiplePerspectives));
        tfSkill12.setText("1.2 Consider Wholes and Parts: " + STUtils.calc(STUtils.STSkills.ConsiderWholesAndParts));
        tfSkill13.setText("1.3 Effectively Resp to Unc and Amb: " + STUtils.calc(STUtils.STSkills.EffectivelyRespondToUncertaintyAndAmbiguity));
        tfSkill14.setText("1.4 Consider Issues Appropriately: " + STUtils.calc(STUtils.STSkills.ConsiderIssuesAppropriately));
        tfSkill15.setText("1.5 Use Mental Modeling and Abstraction: " + STUtils.calc(STUtils.STSkills.UseMentalModelingAndAbstraction));
        tfSkill21.setText("2.1 Recognize Systems: " + STUtils.calc(STUtils.STSkills.RecognizeSystems));
        tfSkill22.setText("2.2 Maintain Boundaries: " + STUtils.calc(STUtils.STSkills.MaintainBoundaries));
        tfSkill23.setText("2.3 Differentiate and Quantify Elements: " + STUtils.calc(STUtils.STSkills.DifferentiateAndQuantifyElements));
        tfSkill31.setText("3.1 Identify Relationships: " + STUtils.calc(STUtils.STSkills.IdentifyRelationships));
        tfSkill32.setText("3.2 Characterize Relationships: " + STUtils.calc(STUtils.STSkills.CharacterizeRelationships));
        tfSkill33.setText("3.3 Identify Feedback Loops: " + STUtils.calc(STUtils.STSkills.IdentifyFeedbackLoops));
        tfSkill34.setText("3.4 Characterize Feedback Loops: " + STUtils.calc(STUtils.STSkills.CharacterizeFeedbackLoops));
        tfSkill41.setText("4.1 Describe Past System Behavior: " + STUtils.calc(STUtils.STSkills.DescribePastSystemBehavior));
        tfSkill42.setText("4.2 Predict Future System Behavior: " + STUtils.calc(STUtils.STSkills.PredictFutureSystemBehavior));
        tfSkill43.setText("4.3 Respond to Changes over Time: " + STUtils.calc(STUtils.STSkills.RespondToChangesOverTime));
        tfSkill44.setText("4.4 Use Leverage Points: " + STUtils.calc(STUtils.STSkills.UseLeveragePoints));
    }

    private void showLoadedData() {
        // Load it into the various controls
        showLoadedGeneralData();

        // Now show it in the table
        actionListTable.getItems().clear();

        // We have all the actions loaded up
        for (Action a : Player.actions) {
            actionListTable.getItems().add(a);
        }
    }
}