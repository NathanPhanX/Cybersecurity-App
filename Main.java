package lesson;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.control.*;

public class Main extends Application {
    Stage window;
    Scene scene;

    @Override
    public void start(Stage primaryStage){
        // Window
        window = primaryStage;

        // Settings
        window.setOnCloseRequest(e -> {
            e.consume();

            if(UI.confirmBox("Are you sure?", "Yes", "No", "", true)[1])
                window.close();
        });
        window.setFullScreenExitHint("");
        window.setTitle("Main");

        // Exit button
        HBox exitButton = new HBox(UI.exitButton(window));
        exitButton.setAlignment(Pos.TOP_RIGHT);
        HBox.setHgrow(exitButton, Priority.ALWAYS);

        // DoS lesson
        Button cyberAttack = new Button("Cyber Attack");
        cyberAttack.setPrefWidth(Config.bigButtonWidth);
        cyberAttack.setPrefHeight(Config.bigButtonHeight);
        cyberAttack.setOnAction(e -> CyberAttack.display(window, scene));

        // Man in the Middle lesson
        Button networking = new Button("Networking");
        networking.setPrefWidth(Config.bigButtonWidth);
        networking.setPrefHeight(Config.bigButtonHeight);

        // Layout
        HBox layout = new HBox();
        layout.setSpacing(Config.layoutSpacing2);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(cyberAttack, networking);
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(exitButton);
        borderPane.setCenter(layout);

        // Display
        scene = new Scene(borderPane, Config.windowWidth, Config.windowHeight);
        window.setScene(scene);
        window.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
