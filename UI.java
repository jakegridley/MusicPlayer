package Assignment.components;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;


/**
 * JavaFX App
 */
public class UI extends Application {
	
    @Override
    public void start(Stage s)
    {
        // set title for the stage
        s.setTitle("Music Player");
  
        // create a button
       
  
        // create a grid pane
        GridPane gPane = new GridPane();
        gPane.setGridLinesVisible(true);
        //gPane.setHgap(100);
        //gPane.setVgap(100);
        Button button = new Button("button");
        // create a label
        Label l = new Label("button not selected");
  
        gPane.add(button, 100, 100);
        gPane.add(l, 150, 150);
        
        // action event
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                l.setText("   button   selected    ");
            }
        };
  
        // when button is pressed
        button.setOnAction(event);
  
        // add button
       
  
        // create a scene
        Scene sc = new Scene(gPane, 600, 600);
  
        // set the scene
        s.setScene(sc);
  
        s.show();
    }
  
    public static void main(String args[])
    {
        // launch the application
        launch(args);
    }
}