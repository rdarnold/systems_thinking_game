
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
import javafx.scene.control.Alert.AlertType;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.IntegerProperty;

import gos.*;

public class ExperimentPanelLeft extends PanelLeftBase {

    ExperimentPanelSet m_ParentPanelSet;
    //CreateExperimentDialog experimentDialog;

    MovableButton exp1;
    MovableButton exp2;
    MovableButton exp3;
    MovableButton exp4;
    MovableButton exp5;

    MovableButton del1;
    MovableButton del2;
    MovableButton del3;
    MovableButton del4;
    MovableButton del5;

    public Label discoveryPointText;
    
    private ExperimentPanelLeft thisScreen;

    public ExperimentPanelLeft(ExperimentPanelSet panelSet) {
        super();
        thisScreen = this;
        m_ParentPanelSet = panelSet;
        create();
    }

    void showAlert(MovableButton expBtn, MovableButton delBtn) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirm");
        alert.setHeaderText("Delete Experiment");
        alert.setContentText("Delete this experiment to make room for a different one?  Your Discover Point will not be returned.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            expBtn.setUserData(null);
            expBtn.setDisable(true);
            delBtn.setDisable(true);
        } 
    }


    private void create() {
        //dialog = new CreateExperimentDialog();
        
        MovableButton btn;
        String str;
        Tooltip toolTip;
        int ttWidth = 300;

        Label label = new Label("Experiments");
        label.setPadding(new Insets(20));
        getChildren().add(label);

        /*discoveryPointText = new Label();
        //discoveryPointText.setPadding(new Insets(5));
        getChildren().add(discoveryPointText);
        discoveryPointText.textProperty().bind(Player.discoveryPointsProperty().asString(
            "Discovery Points: %d"
        ));*/

        btn = new MovableButton("Create New");
        str = "Create a new experiment.";
        Utils.addToolTip(btn, str);
        //btn.moveTo(0, 200);
        btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Player.recordButtonAction(event, thisScreen.className());
                //Gos.sim.restoreCurrentSnap();
                Gos.sim.restoreBaseSnap();
                //Gos.gos.showExperimentScreen();
                if (Gos.checkDiscoveryPoints() == false)
                    return;
                //if (Player.getDiscoveryPoints() > 0) {
                    // Ugh maybe I should tie these two panels together better.
                    //Gos.gos.mainScene.showExperimentCreationPanel();
                    m_ParentPanelSet.onCreateButton();
                //}
                //showChooseExperimentTypeWindow();
            }
        });
        getChildren().add(btn);

        Utils.addVerticalSpace(this, 30);

        HBox hbox = new HBox();
        getChildren().add(hbox);
        hbox.setSpacing(5);
        hbox.setAlignment(Pos.CENTER);

        exp1 = new MovableButton("Re-Run"); //Exp. 1");
        str = "Run the experiment again.";
        Utils.addToolTip(exp1, str);
        exp1.setDisable(true);
        exp1.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Player.recordButtonAction(event, thisScreen.className());
                // Just runs it, you can't see what you changed.
                Experiment exp = (Experiment)exp1.getUserData();
                exp.play(Gos.sim);
            }
        });
        //hbox.getChildren().add(exp1);

        del1 = new MovableButton("X");
        str = "Delete experiment 1.";
        Utils.addToolTip(del1, str);
        del1.setPrefWidth(10);
        del1.setDisable(true);
        del1.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Player.recordButtonAction(event, thisScreen.className());
                showAlert(exp1, del1);
            }
        });
        //hbox.getChildren().add(del1);

        hbox = new HBox();
        getChildren().add(hbox);
        hbox.setSpacing(5);
        hbox.setAlignment(Pos.CENTER);

        exp2 = new MovableButton("Re-Run Exp. 2");
        str = "Run experiment 2.";
        Utils.addToolTip(exp2, str);
        exp2.setDisable(true);
        exp2.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Player.recordButtonAction(event, thisScreen.className());
                Experiment exp = (Experiment)exp2.getUserData();
                exp.play(Gos.sim);
            }
        });
        //hbox.getChildren().add(exp2);
        
        del2 = new MovableButton("X");
        str = "Delete experiment 2.";
        Utils.addToolTip(del2, str);
        del2.setPrefWidth(10);
        del2.setDisable(true);
        del2.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Player.recordButtonAction(event, thisScreen.className());
                showAlert(exp2, del2);
            }
        });
        //hbox.getChildren().add(del2);

        hbox = new HBox();
        getChildren().add(hbox);
        hbox.setSpacing(5);
        hbox.setAlignment(Pos.CENTER);

        exp3 = new MovableButton("Re-Run Exp. 3");
        str = "Run experiment 3.";
        Utils.addToolTip(exp3, str);
        exp3.setDisable(true);
        exp3.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Player.recordButtonAction(event, thisScreen.className());
                Experiment exp = (Experiment)exp3.getUserData();
                exp.play(Gos.sim);
            }
        });
        //hbox.getChildren().add(exp3);

        del3 = new MovableButton("X");
        str = "Delete experiment 3.";
        Utils.addToolTip(del3, str);
        del3.setPrefWidth(10);
        del3.setDisable(true);
        del3.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Player.recordButtonAction(event, thisScreen.className());
                showAlert(exp3, del3);
            }
        });
        //hbox.getChildren().add(del3);

        hbox = new HBox();
        //getChildren().add(hbox);
        hbox.setSpacing(5);
        hbox.setAlignment(Pos.CENTER);

        exp4 = new MovableButton("Re-Run Exp. 4");
        str = "Run experiment 4.";
        Utils.addToolTip(exp4, str);
        exp4.setDisable(true);
        exp4.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Player.recordButtonAction(event, thisScreen.className());
                Experiment exp = (Experiment)exp4.getUserData();
                exp.play(Gos.sim);
            }
        });
        //hbox.getChildren().add(exp4);
        
        del4 = new MovableButton("X");
        str = "Delete experiment 4.";
        Utils.addToolTip(del4, str);
        del4.setPrefWidth(10);
        del4.setDisable(true);
        del4.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Player.recordButtonAction(event, thisScreen.className());
                showAlert(exp4, del4);
            }
        });
        hbox.getChildren().add(del4);

        hbox = new HBox();
        //getChildren().add(hbox);
        hbox.setSpacing(5);
        hbox.setAlignment(Pos.CENTER);

        exp5 = new MovableButton("Re-Run Exp. 5");
        str = "Run experiment 5.";
        Utils.addToolTip(exp5, str);
        exp5.setDisable(true);
        exp5.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Player.recordButtonAction(event, thisScreen.className());
                Experiment exp = (Experiment)exp5.getUserData();
                exp.play(Gos.sim);
            }
        });
        //hbox.getChildren().add(exp5);
        
        del5 = new MovableButton("X");
        str = "Delete experiment 5.";
        Utils.addToolTip(del5, str);
        del5.setPrefWidth(10);
        del5.setDisable(true);
        del5.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Player.recordButtonAction(event, thisScreen.className());
                showAlert(exp5, del5);
            }
        });
        //hbox.getChildren().add(del5);

        Utils.addVerticalSpace(this, 30);

        btn = new MovableButton("Restore");
        str = "Restore the system to its current non-experimented state.";
        Utils.addToolTip(btn, str);
        //btn.moveTo(0, 200);
        btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Player.recordButtonAction(event, thisScreen.className());
                Gos.sim.restoreCurrentSnap();
            }
        });
        getChildren().add(btn);

        /*Utils.addVerticalSpace(this, 30);

        btn = new MovableButton("Main Screen");
        btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Player.recordButtonAction(event, thisScreen.className());
                Gos.sim.restoreCurrentSnap();
                Gos.mainScene.showDefaultAreas();
                Gos.mainScene.enableDefaultButtons();
            }
        });
        getChildren().add(btn);*/
    }

    public ArrayList<SystemSnapshot> generateSnapshotList() {
        ArrayList<SystemSnapshot> shots = new ArrayList<SystemSnapshot>();
        if (exp1.getUserData() != null) {
            Experiment e = (Experiment)exp1.getUserData();
            shots.add(e.getSnap());
        } 
        if (exp2.getUserData() != null) {
            Experiment e = (Experiment)exp2.getUserData();
            shots.add(e.getSnap());
        }
        if (exp3.getUserData() != null) {
            Experiment e = (Experiment)exp3.getUserData();
            shots.add(e.getSnap());
        }
        if (exp4.getUserData() != null) {
            Experiment e = (Experiment)exp4.getUserData();
            shots.add(e.getSnap());
        }
        if (exp5.getUserData() != null) {
            Experiment e = (Experiment)exp5.getUserData();
            shots.add(e.getSnap());
        }

        return shots;
    }

    public MovableButton getNextFreeExpButton() {
        // Updated for just one experiment
        return exp1;

        /*
        if (exp1.getUserData() == null) {
            return exp1;
        } 
        else if (exp2.getUserData() == null) {
            return exp2;
        }
        else if (exp3.getUserData() == null) {
            return exp3;
        }
        else if (exp4.getUserData() == null) {
            return exp4;
        }
        else if (exp5.getUserData() == null) {
            return exp5;
        }
        return null;*/
    }

    private MovableButton getDelBtnForExpButton(MovableButton expButton) {
        if (expButton == exp1) {
            return del1;
        }
        else if (expButton == exp2) {
            return del2;
        }
        else if (expButton == exp3) {
            return del3;
        }
        else if (expButton == exp4) {
            return del4;
        }
        else if (expButton == exp5) {
            return del5;
        }
        
        return null;
    }

    public boolean addExperiment(Experiment exp) {
        MovableButton expButton = getNextFreeExpButton();
        if (expButton == null) {
            return false;
        }
        expButton.setDisable(false);
        expButton.setUserData(exp);

        MovableButton delButton = getDelBtnForExpButton(expButton);
        if (delButton != null)
            delButton.setDisable(false);
        return true;
    }

    public void removeExperiment(MovableButton btn) {
        btn.setDisable(true);
        btn.setUserData(null);
        MovableButton delButton = getDelBtnForExpButton(btn);
        if (delButton != null)
            delButton.setDisable(true);
    }

    @Override
    public void reset() {
        removeExperiment(exp1);
        removeExperiment(exp2);
        removeExperiment(exp3);
        removeExperiment(exp4);
        removeExperiment(exp5);
        update();
    }

    @Override
    public void update() {
        
    }
}