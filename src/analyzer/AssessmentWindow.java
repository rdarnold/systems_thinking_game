
package gos.analyzer;

import java.io.*;
import java.util.*;
import java.util.ArrayList;
import java.util.stream.Collectors;
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
import java.nio.file.Path;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.ZoneOffset;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.text.DecimalFormat;
import java.time.ZonedDateTime;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

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
    private TableView answerListTable;

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
    TextField tfTotalTimeSA = new TextField();
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

        // First add all the meta data, who the person is, email address, time started, time taken to do
        // various things, and general scores for each of the scenarios
        createGeneralData(generalRow);
        
        // Now add the list of answers
        answerListTable = createAnswerList();
        //m_MainVBox.getChildren().add(actionListTable);
        answerListRow.getChildren().add(answerListTable);

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
        tfTotalTimeSA.setEditable(false);
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
        col2.getChildren().add(tfTotalTimeSA);

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

        int wid = 250;
        tfId.setPrefWidth(wid);
        tfName.setPrefWidth(wid);
        tfEmail.setPrefWidth(wid);
        tfScore1.setPrefWidth(wid);
        tfScore2.setPrefWidth(wid);
        tfScore3.setPrefWidth(wid);
        tfScore4.setPrefWidth(wid);
        tfPracticeResults.setPrefWidth(wid);
        tfPlayedTimes.setPrefWidth(wid);
    
        tfTimeStart.setPrefWidth(wid);
        tfTimeEnd.setPrefWidth(wid);
        tfTotalTime.setPrefWidth(wid);
        tfTotalTimeExp.setPrefWidth(wid);
        tfTotalTimeObs.setPrefWidth(wid);
        tfNumExp.setPrefWidth(wid);
        tfTotalTimeSA.setPrefWidth(wid);

        tfSTScore.setPrefWidth(wid);
        tfDomain1.setPrefWidth(wid);
        tfDomain2.setPrefWidth(wid);
        tfDomain3.setPrefWidth(wid);
        tfDomain4.setPrefWidth(wid);

        wid = 350;
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

    private TableView createExerciseList() {
        TableView table = new TableView();
        table.setEditable(false);
        table.prefHeightProperty().bind(m_Scene.heightProperty());
        table.prefWidthProperty().bind(m_Scene.widthProperty());
 
        TableColumn col1 = new TableColumn("Turn");
        col1.setCellValueFactory(new PropertyValueFactory<>("strTurn"));
        col1.prefWidthProperty().bind(table.widthProperty().multiply(0.1));
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
                    Utils.log("Click on answer column 1, row " + cell.getIndex());
                }
            });
            return cell;
        });

        TableColumn col2 = new TableColumn("Score");
        col2.setCellValueFactory(new PropertyValueFactory<>("strScore"));
        col2.prefWidthProperty().bind(table.widthProperty().multiply(0.3));
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
                    Utils.log("Click on answer column 2, row " + cell.getIndex());
                }
            });
            return cell;  
        });

        TableColumn col3 = new TableColumn("Answer");
        col3.setCellValueFactory(new PropertyValueFactory<>("strAnswer"));
        col3.prefWidthProperty().bind(table.widthProperty().multiply(0.6));
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
                    Utils.log("Click on answer column 3, row " + cell.getIndex());
                }
            });
            return cell;
        });
        
        table.getColumns().addAll(col1, col2, col3); 
        return table;
    }

    private TableView createAnswerList() {
        TableView table = new TableView();
        table.setEditable(false);
        table.prefHeightProperty().bind(m_Scene.heightProperty());
        table.prefWidthProperty().bind(m_Scene.widthProperty());
 
        TableColumn col1 = new TableColumn("Time");
        col1.setCellValueFactory(new PropertyValueFactory<>("strTime"));
        col1.prefWidthProperty().bind(table.widthProperty().multiply(0.1));
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
                    Utils.log("Click on answer column 1, row " + cell.getIndex());
                }
            });
            return cell;
        });

        TableColumn col2 = new TableColumn("Question");
        col2.setCellValueFactory(new PropertyValueFactory<>("strQuestion"));
        col2.prefWidthProperty().bind(table.widthProperty().multiply(0.3));
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
                    Utils.log("Click on answer column 2, row " + cell.getIndex());
                }
            });
            return cell;
        });

        TableColumn col3 = new TableColumn("Answer");
        col3.setCellValueFactory(new PropertyValueFactory<>("strAnswer"));
        col3.prefWidthProperty().bind(table.widthProperty().multiply(0.6));
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
                    Utils.log("Click on answer column 3, row " + cell.getIndex());
                }
            });
            return cell;
        });
        
        table.getColumns().addAll(col1, col2, col3); 
        return table;
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

        MenuItem add;
        
        add = new MenuItem("Download Data");
            add.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                onClickDownloadData();
            }
        });        
 
        menuFile.getItems().addAll(add);

        //MenuItem add = new MenuItem("Shuffle",
            //new ImageView(new Image("menusample/new.png")));
        add = new MenuItem("Load Player Data");
            add.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                onClickLoadPlayerData();
            }
        });        
 
        menuFile.getItems().addAll(add);

        add = new MenuItem("Write Excel Data");
            add.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                onClickWriteExcelData();
            }
        });        
 
        menuFile.getItems().addAll(add);
        
        add = new MenuItem("Load and Write All Excel Data");
            add.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                onClickWriteAllExcelData();
            }
        });        
 
        menuFile.getItems().addAll(add);
        return bar;
    }

    private void onClickBefore(Action action) {
        Utils.log("Timestamp is: " + action.getStrTime());

        SystemSnapshot snap = AssessmentData.createSnapshotFromSubmitAction(action, true);
        // We load it up from the existing string using a snapshot construct which
        // has the capability to load from string
        /*SystemSnapshot snap = new SystemSnapshot(Gos.sim);

        // First find the values string to use
        String valStr = findBeforeValuesWithinString(action.getDesc());
        Values values = new Values(valStr);
        snap.setValuesTo(values);

        // Now find the string to load...
        String snapStr = findSnapshotWithinString(action.getDesc()); 
        snap.setFromString(snapStr);*/

        // Then we restore it to the sim
        snap.restore(Gos.sim);

        // Show the system as we loaded in desc
        Gos.mainScene.showChangePanelSet(); 
    }

    private void onClickAfter(Action action) {
        Utils.log("Timestamp is: " + action.getStrTime());

        SystemSnapshot snap = AssessmentData.createSnapshotFromSubmitAction(action, false);

        // We load it up from the existing string using a snapshot construct which
        // has the capability to load from string
        /*SystemSnapshot snap = new SystemSnapshot(Gos.sim);

        // First find the values string to use
        String valStr = findAfterValuesWithinString(action.getDesc());
        Values values = new Values(valStr);
        snap.setValuesTo(values);

        // Now find the string to load...
        String snapStr = findSnapshotWithinString(action.getDesc()); 
        snap.setFromString(snapStr);*/

        // Then we restore it to the sim
        snap.restore(Gos.sim);

        // Show the system as we loaded in desc
        Gos.mainScene.showChangePanelSet(); 
    }

    private void onClickDownloadData() {
        // Let's download and organize all the data from the site.
        // We can also do any post-processing if we need to do something to them.
        // I think we'll organize by month, and then by ID.

        // A folder is a file with mimeType = 'application/vnd.google-apps.folder'
        FileTransfer.downloadFiles();
    }

    private boolean loadPlayerFile(File selectedFile) {
        List<String> lines = null;
        if (selectedFile == null)
            return false;

        try {
            lines = Files.readAllLines(Paths.get(selectedFile.getPath()));
        } catch (IOException ex) {
            Utils.log("Error loading player data: " + ex + ", filename: " + Paths.get(selectedFile.getPath()));
            Utils.log("Trying ISO_8859_1 encoding...");

            try {
                lines = Files.readAllLines(Paths.get(selectedFile.getPath()), StandardCharsets.ISO_8859_1);
            }
            catch (IOException ex2) {
                Utils.log("Error loading player data: " + ex2 + ", filename: " + Paths.get(selectedFile.getPath()));
                return false;
            }
        }

        Utils.log("Loading player file: " + selectedFile.getPath());
        Player.loadPlayerData(lines);
       
        // Now build the time slice arrays and such so we have everything organized by time
        Utils.log("Building time slices");
        AssessmentData.buildTimeSlices();

        // Now present it in some viewable fashion
        Utils.log("Showing data");
        showLoadedData();

        Utils.log("Load complete");
        return true;
    }

    private void onClickLoadPlayerData() {

        FileChooser fileChooser = new FileChooser(); 
        //String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
        fileChooser.setInitialDirectory(new File("C://Ross//Work//Japan//Drones//Code//systems_thinking_game_evolved//data//using//Final"));   
        fileChooser.setTitle("Open Player Data File");      
        fileChooser.getExtensionFilters().addAll(
         new ExtensionFilter("Text Files", "*.txt"));                              
        File selectedFile = fileChooser.showOpenDialog(null);

        loadPlayerFile(selectedFile);
        /*if (selectedFile == null)
            return;

        try {
            lines = Files.readAllLines(Paths.get(selectedFile.getPath()));
        } catch (IOException ex) {
            Utils.log("Error loading player data: " + ex + ", filename: " + Paths.get(selectedFile.getPath()));
            Utils.log("Trying ISO_8859_1 encoding...");

            try {
                lines = Files.readAllLines(Paths.get(selectedFile.getPath()), StandardCharsets.ISO_8859_1);
            }
            catch (IOException ex2) {
                Utils.log("Error loading player data: " + ex2 + ", filename: " + Paths.get(selectedFile.getPath()));
                return;
            }
        }

        Player.loadPlayerData(lines);
       
        // Now build the time slice arrays and such so we have everything organized by time
        AssessmentData.buildTimeSlices();

        // Now present it in some viewable fashion
        showLoadedData();*/
    } 

    private void onClickWriteExcelData() {
        Workbook wb = AssessmentData.createExcelFile();
        AssessmentData.writeExcelData(wb, 1);
        AssessmentData.finalizeExcelData(wb, true);
    }

    private void onClickWriteAllExcelData() {
        // So iterate through the "using" folder, load each player,
        // and write out to the same excel file
        List<Path> txtFiles = null;
        try {
            txtFiles = Files.walk(Paths.get("C://Ross//Work//Japan//Drones//Code//systems_thinking_game_evolved//data//using//Final"))
                            .filter(p -> p.toString().endsWith(".txt"))
                            .collect(Collectors.toList());
        } catch (IOException ex) {
            Utils.log("Error loading player data file list");
            return;
        }

        Workbook wb = AssessmentData.createExcelFile();
        int rowNum = 1;
        for (Path path : txtFiles) {
            if (loadPlayerFile(path.toFile()) == true) {
                AssessmentData.writeExcelData(wb, rowNum);
                rowNum++;
            } 
        }

        AssessmentData.finalizeExcelData(wb);
    }

    private void showLoadedGeneralData() {
        // Now the general data
        tfId.setText("ID: " + Player.getId());
        tfName.setText("Name: " + Player.getName());
        tfEmail.setText("Email: " + Player.getEmail());
        tfPracticeResults.setText("Tutorial: " + AssessmentData.printTutorialDataString());        
        tfPlayedTimes.setText("Played Times: " + Player.getTimesPlayed());
        tfScore1.setText("Four Shapes R1: " + Utils.printHoursMinsSecsFromMS(AssessmentData.calcTimeForTask(3, 0, 3, 1)));
        tfScore2.setText("Four Shapes R2: " + Utils.printHoursMinsSecsFromMS(AssessmentData.calcTimeForTask(3, 1, 4, 0)));
        tfScore3.setText("Chaos R1: " + Utils.printHoursMinsSecsFromMS(AssessmentData.calcTimeForTask(4, 0, 4, 1)));
        tfScore4.setText("Chaos R2: " + Utils.printHoursMinsSecsFromMS(AssessmentData.calcTimeForTask(4, 1, 5, 0)));

        // Now we want, total time spent experimenting, total experiments done, total time spent observing'
        ZonedDateTime date = ZonedDateTime.ofInstant(Instant.ofEpochMilli(Player.getStartTime()), ZoneId.systemDefault());
        String strTime = DateTimeFormatter.ofPattern("yyyy-MM-dd: HH:mm:ss X").format(date);
        tfTimeStart.setText("Start: " + strTime);
        
        date = ZonedDateTime.ofInstant(Instant.ofEpochMilli(Player.getEndTime()), ZoneId.systemDefault());
        strTime = DateTimeFormatter.ofPattern("yyyy-MM-dd: HH:mm:ss X").format(date);
        tfTimeEnd.setText("End: " + strTime);

        tfTotalTime.setText("Total Time: " + Utils.printHoursMinsSecsFromMS(Player.getEndTime() - Player.getStartTime()));

        tfTotalTimeExp.setText("Total Time Exp: " + Utils.printHoursMinsSecsFromMS(AssessmentData.calcTotalTimeExp()));
        tfTotalTimeObs.setText("Total Time Obs: " + Utils.printHoursMinsSecsFromMS(AssessmentData.calcTotalTimeObs()));
        tfNumExp.setText("Num Experiments: " + AssessmentData.printNumExps());

        tfTotalTimeSA.setText("Total Time Self-Assess: " + Utils.printHoursMinsSecsFromMS(AssessmentData.calcSelfAssessmentTime()));

        tfSTScore.setText("Overall STM: ");
        tfDomain1.setText("Mindset: ");
        tfDomain2.setText("Structure: ");
        tfDomain3.setText("Content: ");
        tfDomain4.setText("Behavior: ");

        tfSkill11.setText("1.1 Explore Multiple Perspectives: " + STUtils.calc(STSkills.ExploreMultiplePerspectives));
        tfSkill12.setText("1.2 Consider Wholes and Parts: " + STUtils.calc(STSkills.ConsiderWholesAndParts));
        tfSkill13.setText("1.3 Effectively Resp to Unc and Amb: " + STUtils.calc(STSkills.EffectivelyRespondToUncertaintyAndAmbiguity));
        tfSkill14.setText("1.4 Consider Issues Appropriately: " + STUtils.calc(STSkills.ConsiderIssuesAppropriately));
        tfSkill15.setText("1.5 Use Mental Modeling and Abstraction: " + STUtils.calc(STSkills.UseMentalModelingAndAbstraction));
        tfSkill21.setText("2.1 Recognize Systems: " + STUtils.calc(STSkills.RecognizeSystems));
        tfSkill22.setText("2.2 Maintain Boundaries: " + STUtils.calc(STSkills.MaintainBoundaries));
        tfSkill23.setText("2.3 Differentiate and Quantify Elements: " + STUtils.calc(STSkills.DifferentiateAndQuantifyElements));
        tfSkill31.setText("3.1 Identify Relationships: " + STUtils.calc(STSkills.IdentifyRelationships));
        tfSkill32.setText("3.2 Characterize Relationships: " + STUtils.calc(STSkills.CharacterizeRelationships));
        tfSkill33.setText("3.3 Identify Feedback Loops: " + STUtils.calc(STSkills.IdentifyFeedbackLoops));
        tfSkill34.setText("3.4 Characterize Feedback Loops: " + STUtils.calc(STSkills.CharacterizeFeedbackLoops));
        tfSkill41.setText("4.1 Describe Past System Behavior: " + STUtils.calc(STSkills.DescribePastSystemBehavior));
        tfSkill42.setText("4.2 Predict Future System Behavior: " + STUtils.calc(STSkills.PredictFutureSystemBehavior));
        tfSkill43.setText("4.3 Respond to Changes over Time: " + STUtils.calc(STSkills.RespondToChangesOverTime));
        tfSkill44.setText("4.4 Use Leverage Points: " + STUtils.calc(STSkills.UseLeveragePoints));
    }

    private void showLoadedData() {
        // Load it into the various controls
        showLoadedGeneralData();

        // Answer table
        answerListTable.getItems().clear();

        // We have all the answers loaded up
        for (Answer ans : Player.answers) {
            answerListTable.getItems().add(ans);
        }

        // Action table
        actionListTable.getItems().clear();

        // We have all the actions loaded up
        for (Action a : Player.actions) {
            actionListTable.getItems().add(a);
        }
    }
}