package lesson;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

import java.util.Random;

public class DoS {
    public static void display(Stage window, Scene prevScene){
        // Navigation
        HBox nav = UI.navigation(window, prevScene);

        // Next button
        Button nextButton = new Button("Go To Simulation");
        nextButton.setPrefHeight(Config.smallButtonHeight);
        HBox next = new HBox(nextButton);
        next.setAlignment(Pos.BOTTOM_RIGHT);
        HBox.setHgrow(next, Priority.ALWAYS);

        // Layout
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(nav);
        borderPane.setBottom(next);

        // Display
        Scene lessons = new Scene(borderPane, Config.windowWidth, Config.windowHeight);
        window.setScene(lessons);
        window.setTitle("DoS");

        // Action
        Random r = new Random();
        nextButton.setOnAction(e -> {
            String instruction = "Answer the following question correctly to move to the simulation\n";
            String errorMsg = "";

            // Loop until the user answers correctly or exit the quiz
            while (true){
                // Pull question and answer from the DoS quiz randomly
                String quiz_info = Config.dosQuiz[r.nextInt(Config.dosQuiz.length)];
                String question = quiz_info.split("/")[0].strip();
                String answer = quiz_info.split("/")[1].strip();
                boolean quizAnswer = answer.equals("True");

                // Get user answer
                boolean[] userAnswer = UI.confirmBox(instruction +  question, "True", "False", errorMsg, true);

                // Check user answer
                if(userAnswer[0]) {
                    if (userAnswer[1] == quizAnswer) {
                        SimulationDoS.display(window, lessons);
                        break;
                    }
                    else {
                        errorMsg = "That's incorrect. Try again!";
                    }
                }
                else{
                    break;
                }
            }
        });
    }
}
