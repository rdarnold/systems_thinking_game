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
import javafx.scene.text.TextAlignment;

import gos.*;

// Gos - Game of Systems
public class AnswerScreen extends GosSceneBase {

    GridPane mainArea;
    ArrayList<Node> tempControlsList;
    ArrayList<TextField> tempTextFields;
    ArrayList<TextArea> tempTextAreas;
    ArrayList<CheckBox> tempCheckBoxes;
    ArrayList<ToggleGroup> tempToggleGroups;
    MovableButton doneButton;
    Text questionNameText;
    Text questionNumText;
    Text questionText;
    Text instructionText;
    Text exerciseText;
    
    private AnswerScreen thisScreen;

    public AnswerScreen(Gos theGos, BorderPane root, int wid, int hgt) {
        super(theGos, root, wid, hgt);
        thisScreen = this;
    }

    @Override
    public void createBuildingBlocks() {
        tempControlsList = new ArrayList<Node>();
        tempTextFields = new ArrayList<TextField>();
        tempTextAreas = new ArrayList<TextArea>();
        tempCheckBoxes = new ArrayList<CheckBox>();
        tempToggleGroups = new ArrayList<ToggleGroup>();
        getStylesheets().add("css/gos.main.css");
    }

    @Override
    public void createMainScene() {
        createMainArea();
        //createTopArea();

        MovableButton btn;

        doneButton = new MovableButton("Submit");
        doneButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Player.recordButtonAction(event, thisScreen.className());
                submitAnswers();
            }
        });

        setBorders();
    }

    public void createMainArea() {
        VBox box = new VBox();
        box.setPadding(new Insets(20));
        box.setSpacing(10);
        box.setAlignment(Pos.CENTER);

        questionNameText = new Text();
        questionNameText.setFill(Color.GRAY);
        questionNumText = new Text();
        questionNumText.setFill(Color.GRAY);
        questionText = new Text();
        instructionText = new Text();
        //questionText.setWrappingWidth(overallRoot.getWidth() - 120);
        box.getChildren().add(questionNameText);
        box.getChildren().add(questionNumText);
        box.getChildren().add(questionText);
        //box.getChildren().add(instructionText);

        mainArea = new GridPane();
        mainArea.setPadding(new Insets(10));
        mainArea.setHgap(10);
        mainArea.setVgap(10);

        //HBox.setHgrow(mainArea, Priority.ALWAYS);
        //VBox.setVgrow(mainArea, Priority.ALWAYS);
        mainArea.setAlignment(Pos.CENTER);
        //mainArea.setPadding(new Insets(10, 10, 10, 10));
        //mainArea.setSpacing(10);

        box.getChildren().add(mainArea);
        //overallRoot.setCenter(mainArea);
        overallRoot.setTop(box);
    }

    @Override
    public void setSim(Simulator s, SimRunner runner) {
        super.setSim(s, runner);
        //updateTaskText();
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
        //updateTaskText();
    }

    public void setBorders() {
        //mainArea.setStyle("-fx-border-color: darkgray;");
        //topArea.setStyle("-fx-border-color: darkgray;");
        
        mainArea.setStyle("-fx-background-color: #E0EEEE; " + 
                 "-fx-border-color: darkgray; " +
                 "-fx-border-radius: 3; " +
                 "-fx-border-width: 2;");
    }

    public void tempAdd(Node node) {
        mainArea.getChildren().add(node);
        tempControlsList.add(node);
    }

    // Some parts of the screen are rebuilt depending on the task at hand
    public void rebuild() {
        if (tempControlsList.size() > 0) {
            mainArea.getChildren().removeAll(tempControlsList);
            tempControlsList.clear();
            tempTextFields.clear();
            tempTextAreas.clear();
            tempCheckBoxes.clear();
            tempToggleGroups.clear();
        }

        //mainArea.getColumnConstraints().removeAll();
        //mainArea.getRowConstraints().removeAll();

        // Define this by the task parameters.
        Question question = Player.getCurrentQuestion();
        Exercise exercise = Player.getCurrentExercise();

        // Check if we have a prior answer to this question - if so, 
        // we want to populate the question with the answer the player already
        // provided 
        Answer priorAnswer = Player.getPriorAnswer(question);
        //exerciseText.setText(exercise.getAnswerScreenText());

        questionNameText.setText("Current Question: " + question.getName());
        questionNumText.setText("Question " + (question.getId() + 1) + " / " + exercise.getNumQuestions());

        // Let's put the question text right above the question.
        questionText.setText(question.getText());

        int row = 1;
        // Maybe a task should have multiple answers associated with it,
        // and each one of those has different types and choices.
        switch (question.getAnswerType()) {
            case Input: {
                //instructionText.setText("Please type your answer in the field.");
                //mainArea.getColumnConstraints().add(new ColumnConstraints(100)); // column 1 is 100 wide
                //mainArea.getColumnConstraints().add(new ColumnConstraints(200)); // column 2 is 200 wide
                TextField field = new TextField();
                field.setPrefWidth(400);
                tempTextFields.add(field);
                tempAdd(field);
                GridPane.setRowIndex(field, row);
                GridPane.setColumnIndex(field, 2);
                GridPane.setHalignment(field, HPos.CENTER);

                if (priorAnswer != null) {
                    field.setText(Utils.stripLastCRLF(priorAnswer.getStrAnswer()));
                }
                break;
            }
            case MultilineInput: {
                //instructionText.setText("Please type your answer in the field.");
                //mainArea.getColumnConstraints().add(new ColumnConstraints(100)); // column 1 is 100 wide
                //mainArea.getColumnConstraints().add(new ColumnConstraints(200)); // column 2 is 200 wide
                TextArea area = new TextArea();
                area.setWrapText(true);
                tempTextAreas.add(area);
                tempAdd(area);
                GridPane.setRowIndex(area, row);
                GridPane.setColumnIndex(area, 2);
                GridPane.setHalignment(area, HPos.CENTER);
                
                if (priorAnswer != null) {  
                    area.setText(Utils.stripLastCRLF(priorAnswer.getStrAnswer()));
                }
                break;
            }
            case Choice: {
                int num = 0;
               // instructionText.setText("Please choose the best option.");
                instructionText.setText("");
                ArrayList<String> options = question.getAnswerOptions();
                ToggleGroup group = new ToggleGroup();
                tempToggleGroups.add(group);
                for (String opt : options) {
                    RadioButton btn = new RadioButton(opt);
                    btn.setToggleGroup(group);
                    btn.setUserData(num);
                    btn.wrapTextProperty().setValue(true);
                    btn.setTextAlignment(TextAlignment.RIGHT);
                    tempAdd(btn);
                    GridPane.setRowIndex(btn, row);
                    GridPane.setColumnIndex(btn, 2);
                    GridPane.setHalignment(btn, HPos.RIGHT);
                    row++;

                    if (priorAnswer != null) {
                        if (priorAnswer.answerOptionSelected(num) == true) {
                            btn.setSelected(true);
                        }
                    }
                    num++;
                }
                break;
            }
            case Check: {
                int num = 0;
                //instructionText.setText("Please choose as many options as you wish.");
                ArrayList<String> options = question.getAnswerOptions();
                for (String opt : options) {
                    CheckBox btn = new CheckBox(opt);
                    tempCheckBoxes.add(btn);
                    btn.setUserData(num);
                    tempAdd(btn);
                    GridPane.setRowIndex(btn, row);
                    GridPane.setColumnIndex(btn, 2);
                    GridPane.setHalignment(btn, HPos.LEFT); 
                    row++;

                    if (priorAnswer != null) {
                        if (priorAnswer.answerOptionSelected(num) == true) {
                            btn.setSelected(true);
                        }
                    }
                    num++;
                }
                break;
            }
        }

        GridPane.setRowIndex(doneButton, row + 1);
        GridPane.setColumnIndex(doneButton, 2);
        GridPane.setHalignment(doneButton, HPos.CENTER);
        tempAdd(doneButton);

        //GridPane.setRowIndex(cancelButton, row + 2);
        //GridPane.setColumnIndex(cancelButton, 2);
        //GridPane.setHalignment(cancelButton, HPos.CENTER);
        //tempAdd(cancelButton);

        update();
    }

    private boolean submitAnswers() {
        if (checkAnswers() == false) {
            return false;
        }
        // Save them to some data 
        saveAnswers();
        
        //gos.completeQuestion();
        if (Player.nextQuestion() == null) {
            // Ok move to the next part.
            //Utils.log("Out of questions.");
            simRunner.finishQuestions();
            Utils.log(Player.getAnswerData());
        }
        else {
            rebuild();
        }
        return true;
    }

    private boolean checkAnswers() {
        Question question = Player.getCurrentQuestion();
        if (question == null) {
            return true;
        }

        // Make sure we actually have answers filled out.
        switch (question.getAnswerType()) {
            case Input: {
                // Fields must have some amount of text.
                for (TextField field : tempTextFields) {
                    // Nothing is going to have an answer so small.
                    if (field.getLength() < 2) {
                        return false;
                    }
                }
                return true;
            }
            case MultilineInput: {
                // Fields must have some amount of text.
                for (TextArea area : tempTextAreas) {
                    // Nothing is going to have an answer so small.
                    if (area.getLength() < 2) {
                        return false;
                    }
                }
                return true;
            }
            case Choice: {
                // Something in each toggle group must be selected
                for (ToggleGroup group : tempToggleGroups) {
                    if (group.getSelectedToggle() == null) {
                        return false;
                    }
                }
                return true;
            }
            case Check: {
                // At least one checkbox must be filled out
                for (CheckBox btn : tempCheckBoxes) {
                    if (btn.isSelected() == true) {
                        return true;
                    }
                }
                return false;
            }
        }
        return true;
    }
    
    private boolean saveAnswers() {
        Question question = Player.getCurrentQuestion();
        if (question == null) {
            return false;
        }

        StringBuilder sb = new StringBuilder();

        // We have aleady checked them at this point.
        switch (question.getAnswerType()) {
            case Input: {
                for (TextField field : tempTextFields) {
                    // There's actually only one of these right now.
                    sb.append(field.getText() + "\r\n");
                }
                break;
            }
            case MultilineInput: {
                for (TextArea area : tempTextAreas) {
                    // There's actually only one of these right now.
                    sb.append(area.getText() + "\r\n");
                }
                break;
            }
            case Choice: {
                // Something in each toggle group must be selected
                for (ToggleGroup group : tempToggleGroups) {
                    RadioButton sel = (RadioButton)group.getSelectedToggle();
                    // There's actually only one of these right now.
                    sb.append(sel.getUserData() + ": " + sel.getText() + "\r\n");
                }
                break;
            }
            case Check: {
                // At least one checkbox must be filled out
                for (CheckBox btn : tempCheckBoxes) {
                    if (btn.isSelected() == true) {
                        // Set the text
                        sb.append(btn.getUserData()  + ": " + btn.getText() + "\r\n");
                    }
                }
                break;
            }
        }
        Player.recordAnswer(question, sb.toString());
        return true;
    }
}