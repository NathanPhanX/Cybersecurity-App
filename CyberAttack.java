package lesson;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class CyberAttack {
    public static void display(Stage window, Scene prevScene){
        // Navigation
        HBox nav = UI.navigation(window, prevScene);

        // DoS lesson
        Button dosWindow = new Button("DoS");
        dosWindow.setPrefWidth(Config.bigButtonWidth);
        dosWindow.setPrefHeight(Config.bigButtonHeight);

        // Man in the Middle lesson
        Button mmWindow = new Button("Man in the Middle");
        mmWindow.setPrefWidth(Config.bigButtonWidth);
        mmWindow.setPrefHeight(Config.bigButtonHeight);

        // Layout
        HBox layout = new HBox();
        layout.setSpacing(Config.layoutSpacing2);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(dosWindow, mmWindow);
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(nav);
        borderPane.setCenter(layout);

        // Display
        Scene scene = new Scene(borderPane, Config.windowWidth, Config.windowHeight);
        window.setScene(scene);
        window.setFullScreenExitHint("");
        window.setTitle("Cyber Attack");
        window.show();

        // Action
        dosWindow.setOnAction(e -> DoS.display(window, scene));
        mmWindow.setOnAction(e -> MITM.display(window, scene));
    }
}
