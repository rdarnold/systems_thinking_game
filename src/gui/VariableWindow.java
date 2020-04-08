
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
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
    
import gos.*;

public class VariableWindow extends DialogWindow {

    private Label titleLabel;
    private Label textLabel;
    private VBox m_LeftBox;
    private VBox m_RightBox;
    private MovableButton submitBtn;
    private MovableButton cancelBtn;
    private ArrayList<ChoiceBox> cbList;
    private ArrayList<ChoiceBox> cbOrderedList; // Same but ordered by rank
    private ArrayList<Label> labList;
    private ArrayList<Label> labOrderedList;
    //private boolean showWelcome = true; //false; // True to turn this back on for live mode.

    /*private Label m_labShapeSpinSpeed;
    private Label m_labShapeSpinDir;
    private Label m_labShapeCorners;
    private Label m_labGravity;
    private Label m_labRainRate;
    //private Label m_labMovingDotSpeed;
    private Label m_labGrowth;
    private Label m_labParadigm;*/
    
    private TextArea m_taRationale;
    private boolean m_bCancelled = false;
    
    private VariableWindow thisScreen;

    // Kinda kludgy, might be better to just implement a new class to wrap all this label/
    // choicebox stuff up in one thing, but I think it'll be harder to understand what
    // I did in the future if I do that, and I may have to change this if it doesn't
    // work out well.  So I'm leaving it "exposed" like this because I think it'll be easier
    // to fix and change in the future.
    // This boolean makes it so that the rank change processing only occurs when a user
    // alters the choicebox choice, rather than when the program does (such as when it
    // bumps ranks up upon finding duplicates)
    private boolean m_bBlockChange = false; 

    public VariableWindow(int wid, int hgt) {
        super(wid, hgt);
        thisScreen = this;
        cbList = new ArrayList<ChoiceBox>();
        cbOrderedList = new ArrayList<ChoiceBox>();
        labList = new ArrayList<Label>();
        labOrderedList = new ArrayList<Label>();
        setup();
    }

    private void setup() {
        int wid = m_nWidth;
        int hgt = m_nHeight;
        int space = 10;
        int labelWid = wid - 20;

        String str;
        VBox box = m_MainVBox;

        m_bCancelled = false;

        /*str = "Please rate the variables by how important you think they are right now. " +
              "1 is most important, 7 is least important.";*/
        str = "Please rate the variables by how important you think they are right now. " +
              "10 is extremely important, 0 is not at all important.  You may rate " +
              "variables at the same level.  You do not have to rate all variables.";
        titleLabel = addCenteredLabel(str);
        Utils.addVerticalSpace(box, space);

        // Set up one hbox in the middle that has two vboxes side by side
        // in it so that one can have a list of the labels and the
        // other can have a list of the inputs 1-8
        HBox middleBox = new HBox();
        m_MainVBox.getChildren().add(middleBox);

        m_LeftBox = new VBox();
        m_LeftBox.setAlignment(Pos.CENTER_RIGHT);
        m_LeftBox.setPadding(new Insets(10));
        middleBox.getChildren().add(m_LeftBox);
        m_RightBox = new VBox();
        m_RightBox.setAlignment(Pos.CENTER_LEFT);
        m_RightBox.setPadding(new Insets(10));
        middleBox.getChildren().add(m_RightBox);

        addLabel("Gravity Direction");
        addLabel("Gravity Well Location");
        addLabel("Rain Rate");
        addLabel("Growth"); //Circle Speed");
        addLabel("Paradigm");
        addLabel("Shape Spin Speed");
        addLabel("Shape Spin Direction");
        addLabel("Shape Type");
        addLabel("Shape Color");

        // Should this also have a rationale area?  Probably.
        Label lab = new Label("Rationale:");
        m_MainVBox.getChildren().add(lab);
        m_taRationale = new TextArea();
        m_taRationale.setWrapText(true);
        m_taRationale.setPrefHeight(150);
        m_MainVBox.getChildren().add(m_taRationale);

        submitBtn = new MovableButton("Submit");
        submitBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Player.recordButtonAction(event, thisScreen.className());
                onSubmit();
            }
        });
        addTempControl(submitBtn);

        cancelBtn = new MovableButton("Cancel");
        cancelBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Player.recordButtonAction(event, thisScreen.className());
                onCancel();
            }
        });
        addTempControl(cancelBtn);

        getScene().setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                processKeyRelease(keyEvent.getCode());
            }
        });
    }

    public void processKeyRelease(KeyCode key) {
        ChoiceBox focusedCb = null;
        if (getScene().focusOwnerProperty().get() instanceof ChoiceBox) {
            focusedCb = (ChoiceBox)getScene().focusOwnerProperty().get();
        }
        else {
            return;
        }

        if (focusedCb == null) {
            return;
        }

        int rank = 0;
        switch (key) {
            case DIGIT0:
                rank = 0;
                break;
            case DIGIT1:
                rank = 1;
                break;
            case DIGIT2:
                rank = 2;
                break;
            case DIGIT3:
                rank = 3;
                break;
            case DIGIT4:
                rank = 4;
                break;
            case DIGIT5:
                rank = 5;
                break;
            case DIGIT6:
                rank = 6;
                break;
            case DIGIT7:
                rank = 7;
                break;
            case DIGIT8:
                rank = 8;
                break;
            case DIGIT9:
                rank = 9;
                break;
            // Unfortunately no easy way to select '10' with just a button press
            /*case DIGIT10:
                rank = 10;
                break;*/
        }

        int index = getChoiceBoxIndexForRank(rank);
        focusedCb.getSelectionModel().select(index);
    }

    // Look for the label that has the passed in ChoiceBox as its user data.
    private Label getLabelByCb(ChoiceBox box) {
        for (Label lab : labList) {
            ChoiceBox cb = (ChoiceBox)lab.getUserData();
            if (cb.equals(box) == true) {
                return lab;
            }
        }
        return null;
    }

    // Can't believe I have to do this, javafx falls way short here.
    private ChoiceBox getCbBySelectionModel(SelectionModel model) {
        for (ChoiceBox cb : cbList) {
            if (cb.getSelectionModel().equals(model) == true) {
                return cb;
            }
        }
        return null;
    }

    // Is ranking them better, top to bottom, so it's easier to see?
    // assigning numbers seems kinda hard to visually deal with.
    private Label addLabel(String text) {
        Label lab = new Label(text);
        labList.add(lab);
        labOrderedList.add(lab);
        lab.setPrefWidth(200);
        lab.setPrefHeight(31);
        lab.setMaxWidth(Double.MAX_VALUE);
        lab.setAlignment(Pos.CENTER_RIGHT);
        m_LeftBox.getChildren().add(lab);
        ChoiceBox cb = new ChoiceBox();
        cbList.add(cb);
        cbOrderedList.add(cb);
        cb.setPrefWidth(50);
        cb.setPadding(new Insets(1));
        m_RightBox.getChildren().add(cb);

        // Label user value is it's rank choicebox
        lab.setUserData(cb);

        // User value is their rank; default everything to the dash, which is -1
        cb.setUserData(-1);

        cb.getItems().add("-");
        cb.getItems().add("10");
        cb.getItems().add("9");
        cb.getItems().add("8");
        cb.getItems().add("7");
        cb.getItems().add("6");
        cb.getItems().add("5");
        cb.getItems().add("4");
        cb.getItems().add("3");
        cb.getItems().add("2");
        cb.getItems().add("1");
        cb.getItems().add("0");
        //cb.getItems().add("6");
        //cb.getItems().add("7");
        
        cb.getSelectionModel().selectFirst();

        cb.getSelectionModel().selectedIndexProperty().addListener(
            new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> 
                observableValue, Number oldVal, Number newVal) {
                    if (m_bBlockChange == true)
                        return;

                    // But how to get the source?
                    ReadOnlyIntegerProperty prop = (ReadOnlyIntegerProperty)observableValue;
                    SelectionModel model = (SelectionModel)prop.getBean();
                    // I have to use this kludgy method since javafx doesn't seem to
                    // provide a way to do this basic functionality
                    ChoiceBox box = getCbBySelectionModel(model);
                    int rank = getRankForChoiceBoxIndex(newVal.intValue());
                    box.setUserData(rank);
                    if (oldVal != newVal) {
                        // Basically we use this as a way to block further changes
                        // that come programmatically after the user has made a change.
                        m_bBlockChange = true;
                        onRankChanged(box, rank);
                        m_bBlockChange = false;
                    }
                }
        });

        return lab;
    }

    private void onRankChanged(ChoiceBox box, int rank) {
        // Fix all our ranks if we now have any dupes
        //checkRanks(box);

        // Reorder the list appropriately, and set all the 
        // ranks to match.
        reorderLists(box);
        
        // Now our lists are correctly ordered so remove and re-
        // add all our nodes.
        m_LeftBox.getChildren().removeAll(labList);
        m_RightBox.getChildren().removeAll(cbList);
        m_LeftBox.getChildren().addAll(labOrderedList);
        m_RightBox.getChildren().addAll(cbOrderedList);
    }

    // Based on the order we added these numbers to the choicebox.
    private int getRankForChoiceBoxIndex(int index) {
        switch (index) {
            case 0:  return -1;
            case 11: return 0;
            case 10: return 1;
            case 9:  return 2;
            case 8:  return 3;
            case 7:  return 4;
            case 6:  return 5;
            case 5:  return 6;
            case 4:  return 7;
            case 3:  return 8;
            case 2:  return 9;
            case 1:  return 10;
        }
        return 0;
    }

    private int getChoiceBoxIndexForRank(int rank) {
        switch (rank) {
            case -1: return 0;  // Because we show "-" first, we want the index as 0
            case 0:  return 11;
            case 1:  return 10;
            case 2:  return 9;
            case 3:  return 8;
            case 4:  return 7;
            case 5:  return 6;
            case 6:  return 5;
            case 7:  return 4;
            case 8:  return 3;
            case 9:  return 2;
            case 10: return 1;  // Then second, we show 10.  
        }
        return 0;
    }

    private int getRankForChoiceBox(ChoiceBox cb) {
        if (cb.getValue() == null) {
            return 0;
        }
        String str = cb.getValue().toString();
        if (str.equals("-")) {
            return -1;
        } else if (str.equals("0")) {
            return 0;
        } else if (str.equals("1")) {
            return 1;
        } else if (str.equals("2")) {
            return 2;
        } else if (str.equals("3")) {
            return 3;
        } else if (str.equals("4")) {
            return 4;
        } else if (str.equals("5")) {
            return 5;
        } else if (str.equals("6")) {
            return 6;
        } else if (str.equals("7")) {
            return 7;
        } else if (str.equals("8")) {
            return 8;
        } else if (str.equals("9")) {
            return 9;
        } else if (str.equals("10")) {
            return 10;
        }
        return -1;
    }

    private ChoiceBox findHighestRanked(ArrayList<ChoiceBox> cbl) {
        ChoiceBox highest = cbl.get(0);
        for (ChoiceBox cb : cbl) {
            if ((int)cb.getUserData() == 0)
                continue;

            if ((int)highest.getUserData() == 0 && (int)cb.getUserData() != 0) {
                highest = cb;
            }
            //else if ((int)cb.getUserData() < (int)highest.getUserData()) {
            else if ((int)cb.getUserData() > (int)highest.getUserData()) {
                highest = cb;
            }
        }

        return highest;
    }

    // Check to see if we have duped a rank.  If so, bump all those
    // below it.
    /*private boolean checkForDupe(ChoiceBox box) {
        boolean dupeExists = false;
        int rank = (int)box.getUserData();
        for (ChoiceBox cb : cbList) {
            if (cb.equals(box))
                continue;
            int rank2 = (int)cb.getUserData();
            if (rank2 == rank) {
                return true;
            }
        }

        return false;
    }*/

    // Check if we have any dupes with the one passed in, if so, bump
    // rank
    /*private void checkRanks(ChoiceBox box) {
        if (checkForDupe(box) == false)
            return;

        int rank = (int)box.getUserData();
        ChoiceBox changed = null;
        for (ChoiceBox cb : cbList) {
            if (cb.equals(box))
                continue;
            int rank2 = (int)cb.getUserData();
            if (rank2 == rank) {
                changed = cb;
                cb.setUserData(rank2 + 1);
                if (rank2 + 1 > 7) {
                    cb.setUserData(0);
                }
            }
        }

        if (changed != null) {
            // Call recursively until nothing is
            // changed.
            checkRanks(changed);
        }
    }*/

    private void reorderLists(ChoiceBox changedBox) {
        // Clear out the ordered one so we can add from scratch
        cbOrderedList.clear();

        // Now add them all by rank
        while (cbList.size() > 0) {
            ChoiceBox cb = findHighestRanked(cbList);

            int rank = (int)cb.getUserData();

            if (changedBox != null) {
                // If our changed box is the same rank as the current
                // highest, and it's still in the list, use it instead
                // so that we put it at the "top" of any that rank the same
                // as it, which is what the user would have intended.
                if ((int)changedBox.getUserData() == rank &&
                    cbList.contains(changedBox) == true) {
                        cb = changedBox;
                        changedBox = null; // We no longer need it.
                    }
            }
            cbList.remove(cb);
            cbOrderedList.add(cb);
        }
        
        // Now we have the ordered list correctly ordered, and
        // the other list is empty.  So re-add to the other list.
        cbList.addAll(cbOrderedList);

        // Now we order labLists as well, and set the correct selections
        labOrderedList.clear();
        for (int i = 0; i < cbOrderedList.size(); i++) {
            ChoiceBox cb = cbOrderedList.get(i);
            cb.getSelectionModel().select(getChoiceBoxIndexForRank((int)cb.getUserData()));
            Label lab = getLabelByCb(cb);
            labOrderedList.add(lab);
        }

        labList.clear();
        labList.addAll(labOrderedList);  
    }

    /*private boolean checkDupe(ChoiceBox box) {
        int rank = getRankForChoiceBox(box);
        for (ChoiceBox cb : cbList) {
            if (box.equals(cb) == true) {
                continue;
            }
            if (getRankForChoiceBox(cb) == rank) {
                return true;
            }
        }
        return false;
    }*/

    /*private boolean checkAnswers() {
        // Check to see that everything has a number
        // and that all numbers are different.
        String val;
        for (ChoiceBox cb : cbList) {
            if (getRankForChoiceBox(cb) == 0) {
                return false;
            }
            //if (checkDupe(cb) == true) {
              //  return false;
            //}
        }
        return true;
    }*/

    private void onSubmit() {
        // Some message that says hey hang on, you can't
        // rate things that way.
        /*if (checkAnswers() == false) {
            return;
        }*/

        // Now log the answers.
        String str = "";
        for (int i = 0; i < labList.size(); i++) {
            str += labList.get(i).getText() + ": " + 
                getRankForChoiceBox(cbList.get(i)) + "\r\n";
        }

        // Now add the rationale
        str += "Rationale:\r\n" + m_taRationale.getText();

        Player.recordAction(Action.Type.SubmitVarRating, str, thisScreen.className());

        m_bCancelled = false;
        close();
    }

    private void onCancel() {
        m_bCancelled = true;
        close();
    }

    public boolean getCancelled() {
        return m_bCancelled;
    }

    @Override
    public void showAndWait() {
        // Clear all the choiceboxes
       /* for (ChoiceBox cb : cbList) {
            cb.setUserData(0);
            cb.getSelectionModel().selectFirst();
        }*/

        m_taRationale.setText("");
        m_bCancelled = false;

        super.showAndWait();
    }
}