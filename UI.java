package lesson;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class UI {
    public static Button backButton(Stage window, Scene scene){
        Button backButton = new Button("Back");
        backButton.setPrefWidth(Config.smallButtonWidth);
        backButton.setPrefHeight(Config.smallButtonHeight);
        backButton.setOnAction(e -> {
            window.setScene(scene);
            window.setFullScreenExitHint("");
        });
        return backButton;
    }

    public static Button exitButton(Stage window){
        Button exitButton = new Button("Exit Program");
        exitButton.setPrefHeight(Config.smallButtonHeight);
        exitButton.setOnAction(e -> {
            boolean[] exit = confirmBox("Are you sure?", "Yes", "No", "", true);

            if(exit[1])
                window.close();
        });
        return exitButton;
    }

    // This method return an array of boolean with length 2
    // The first boolean indicates whether the user answer the question and the second boolean indicates the user's answer
    public static boolean[] confirmBox(String text, String yesText, String noText, String info, boolean showButton){
        final boolean[] answer = new boolean[2];
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);

        // Label
        Label label = new Label();
        label.setText(text);
        label.setWrapText(true);

        // Yes button
        Button yes = new Button(yesText);
        yes.setPrefWidth(Config.smallButtonWidth);
        yes.setPrefHeight(Config.smallButtonHeight);
        yes.setOnAction(e -> {
            answer[0] = true;
            answer[1] = true;
            window.close();
        });

        // No button
        Button no = new Button(noText);
        no.setPrefWidth(Config.smallButtonWidth);
        no.setPrefHeight(Config.smallButtonHeight);
        no.setOnAction(e -> {
            answer[0] = true;
            answer[1] = false;
            window.close();
        });

        // Additional text
        Label additionalText = new Label();
        additionalText.setText(info);
        additionalText.setTextFill(Color.color(1, 0, 0));

        // Layout
        VBox layout = new VBox();
        if(showButton)
            layout.getChildren().addAll(label, yes, no, additionalText);
        else
            layout.getChildren().addAll(label, additionalText);
        layout.setAlignment(Pos.CENTER);
        layout.setSpacing(Config.layoutSpacing1);
        Scene scene = new Scene(layout, Config.popupWidth, Config.popupHeight);
        window.setScene(scene);
        window.showAndWait();

        return answer;
    }

    public static HBox navigation(Stage window, Scene scene){
        // Back button region
        HBox back = new HBox(backButton(window, scene));
        back.setAlignment(Pos.TOP_LEFT);
        HBox.setHgrow(back, Priority.ALWAYS);

        // Exit button region
        HBox exit = new HBox(exitButton(window));
        exit.setAlignment(Pos.TOP_RIGHT);
        HBox.setHgrow(exit, Priority.ALWAYS);

        return new HBox(back, exit);
    }
}
