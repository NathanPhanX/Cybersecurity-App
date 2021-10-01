package lesson;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;

public class SimulationDoS {
    public static void display(Stage window, Scene prevScene){
        final int[] minLen = {692};  // This variable indicates the minimum length of text in cmd that user cannot delete

        // Navigation
        HBox nav = UI.navigation(window, prevScene);

        // Pseudo terminal
        TextArea cmd = new TextArea();
        cmd.setStyle("-fx-control-inner-background:#000000; -fx-text-fill: green; -fx-font-size: 14");
        cmd.setPrefHeight(Config.cmdHeight);
        cmd.setMaxWidth(Config.cmdWidth);
        cmd.setWrapText(true);

        // Pseudo terminal layout
        VBox cmdLayout = new VBox();
        cmdLayout.setAlignment(Pos.CENTER);
        cmdLayout.getChildren().addAll(cmd);

        // Prevent user delete important simulation content
        cmd.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if(event.getCode() == KeyCode.BACK_SPACE || event.getCode() == KeyCode.DELETE){
                String[] text = cmd.getText().split("\n");
                int len = cmd.getLength() - text[text.length-1].length();
                if(cmd.getCaretPosition() <= len || cmd.getCaretPosition() <= minLen[0])
                    event.consume();
            }
        });

        // View hints button
        Button[] hints = new Button[3];
        for(int i = 0; i < 3; i++){
            hints[i] = new Button("Hint " + (i + 1));
            hints[i].setPrefWidth(Config.smallButtonWidth);
            hints[i].setPrefHeight(Config.smallButtonHeight);

            if(i == 0)
                hints[i].setOnAction(e -> UI.confirmBox("Maybe the website suffers from a DoS attack, maybe not", "", "", "", false));
            else if(i == 1)
                hints[i].setOnAction(e -> UI.confirmBox("Recall the prevention methods with 'iptables' and 'sudo' command", "", "", "", false));
            else
                hints[i].setOnAction(e -> UI.confirmBox("Seriously!? The commands are following:\n" +
                        "'sudo iptables -I INPUT -s (ip address) -j DROP'\n'sudo service iptables save'", "", "", "", false));
        }

        // Hint button layout
        HBox hintLayout = new HBox();
        hintLayout.setAlignment(Pos.CENTER);
        hintLayout.setSpacing(Config.layoutSpacing1);
        hintLayout.getChildren().addAll(hints);

        // Check and restart button
        Button restart = new Button("Restart");
        restart.setPrefWidth(Config.smallButtonWidth);
        restart.setPrefHeight(Config.smallButtonHeight);
        restart.setOnAction(e -> display(window, prevScene));
        Button check = new Button("Check");
        check.setPrefWidth(Config.smallButtonWidth);
        check.setPrefHeight(Config.smallButtonHeight);

        // Check and restart button layout
        HBox restartCheckLayout = new HBox();
        restartCheckLayout.setAlignment(Pos.CENTER);
        restartCheckLayout.setSpacing(Config.layoutSpacing1);
        restartCheckLayout.getChildren().addAll(restart, check);

        // Layout
        VBox layout = new VBox();
        layout.setSpacing(Config.layoutSpacing1);
        layout.getChildren().addAll(cmdLayout, hintLayout, restartCheckLayout);
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(nav);
        borderPane.setCenter(layout);

        // Display
        Scene lessons = new Scene(borderPane, Config.windowWidth, Config.windowHeight);
        window.setScene(lessons);
        window.setTitle("DoS Defend Simulation");

        // Animation
        String[] scan = scanSimulation();
        String[] dosIP = scan[1].split(" ");
        String[] normalIP = scan[2].split(" ");
        animate(cmd, scan[0]);

        // User interactive
        boolean[] isDone = new boolean[3];
        cmd.setOnKeyPressed(keyEvent -> {
            if(keyEvent.getCode() == KeyCode.ENTER){
                String userInput = cmd.getText();
                String[] results = validate(userInput, dosIP, normalIP);
                if(results[1].equals("1")){
                    if(dosIP[0].equals(results[2]))
                        isDone[0] = true;
                    else if(dosIP[1].equals(results[2]))
                        isDone[1] = true;
                }
                else if(results[1].equals("2")){
                    if(isDone[0] && isDone[1])
                        isDone[2] = true;
                }
                cmd.setText(cmd.getText() + results[0] + "\n> ");
                cmd.positionCaret(cmd.getLength());
                minLen[0] = cmd.getLength();
            }
        });

        // Check if the user finishes simulation
        check.setOnAction(e -> {
            if(isDone[0] && isDone[1] && isDone[2]){
                cmd.setEditable(false);
                UI.confirmBox("Congratulations! You've completed the simulation", "", "", "", false);
            }
            else if(isDone[0] && isDone[1]){
                UI.confirmBox("Almost there! Maybe something needs to be saved", "", "", "", false);
            }
            else{
                UI.confirmBox("We're still under attack! ", "", "", "", false);
            }

        });
    }

    // Validate user input
    private static String[] validate(String userInput, String[] dosIP, String[] normalIP){
        String[] arr = userInput.split("\n");
        String[] correctIP = new String[dosIP.length];
        String[] incorrectIP = new String[normalIP.length];

        // Get correct command to block DoS IP address
        for(int i = 0; i < dosIP.length; i++)
            correctIP[i] = "sudo iptables -I INPUT -s " + dosIP[i] + " -j DROP";

        // Get incorrect command to block normal IP address
        for(int i = 0; i < normalIP.length; i++)
            incorrectIP[i] = "sudo iptables -I INPUT -s " + normalIP[i] + " -j DROP";

        // Check
        String command = arr[arr.length-1].strip();
        if(!command.contains("> ")){
            return new String[]{"Invalid command\nA command must start with '>'", "0", ""};
        }
        else {
            command = command.split(">")[1].strip();
            if (command.equals(correctIP[0]) || command.equals(correctIP[1])) {
                String tempIP;
                if(command.equals(correctIP[0]))
                    tempIP = dosIP[0];
                else
                    tempIP = dosIP[1];

                return new String[]{"Excellent. The DoS IP is blocked", "1", tempIP};
            }
            else if (command.equals("sudo service iptables save")) {
                return new String[]{"The IP table is updated", "2", ""};
            }
            else {
                for (String s : incorrectIP) {
                    if (command.equals(s))
                        return new String[]{"Wrong IP is blocked. Maybe double-check RPS information", "0", ""};
                }
            }

            if (command.contains("iptables") && command.contains("sudo"))
                return new String[]{"Invalid command format", "0", ""};
            else if (!command.contains("sudo"))
                return new String[]{"Incorrect. Hint: how to execute command as the root user?", "0", ""};
            else
                return new String[]{"Invalid command. Don't give up, but hints are always available", "0", ""};
        }
    }

    // Generate random network traffic scan
    private static String[] scanSimulation(){
        Random r = new Random();
        StringBuilder info = new StringBuilder("IP Address\t\tRPS (Request Per Second)\n");
        int[] dosAgent = {r.nextInt(10), r.nextInt(10)};
        StringBuilder ip = new StringBuilder();

        // Ensure there's always 2 DoS attack agent
        while (dosAgent[0] == dosAgent[1])
            dosAgent[1] = r.nextInt(10);

        // Generate 10 random events
        for(int i = 0; i < 10; i++){
            StringBuilder ipAddress = new StringBuilder();
            String rps;

            // Generate random ip address
            for(int j = 0; j < 4; j++){
                ipAddress.append(r.nextInt(156) + 100);
                if(j < 3)
                    ipAddress.append(".");
            }
            ip.append(ipAddress).append(' ');

            // Generate rps
            if(dosAgent[0] == i || dosAgent[1] == i)
                rps = Integer.toString(r.nextInt(5000) + 5000);
            else
                rps = Integer.toString(r.nextInt(98) + 2);

            info.append(ipAddress.toString()).append("\t").append(rps).append("\n");
        }

        // Get IP addresses of DoS agent and normal agent
        StringBuilder normalIP = new StringBuilder();
        StringBuilder dosIP = new StringBuilder();
        String[] allIP = ip.toString().split(" ");
        for(int i = 0; i < allIP.length; i++){
            if(i == dosAgent[0] || i == dosAgent[1])
                dosIP.append(allIP[i]).append(" ");
            else
                normalIP.append(allIP[i]).append(" ");
        }

        return new String[]{info.toString(), dosIP.toString(), normalIP.toString()};
    }

    // Perform animation
    private static void animate(TextArea cmd, String scan){
        final IntegerProperty i = new SimpleIntegerProperty(0);
        String instruction = "WELCOME TO THE SIMULATION, COMPUTER SCIENTIST\n" +
                "YOU'RE WORKING ON A LINUX OPERATING SYSTEM AND YOU RECEIVE COMPLAINTS FROM THE COMPANY THAT THE THEIR WEBSITE IS SUPER SLOW AND UNRESPONSIVE\n" +
                "FORTUNATELY, WITH UNMATCHABLE SKILLS AND KNOWLEDGE, YOU IMMEDIATELY DEPLOY NETWORK TRAFFIC MONITOR WITH RESULTS SHOWN BELOW\n" +
                "LET'S GET THIS PROBLEM SOLVED QUICKLY\n\nServer Total RPS Capacity: 1000\n" +
                "Network Traffic Scanning .............................................. Done\n" + scan + "\n" + "> ";
        Timeline timeline = new Timeline();
        KeyFrame keyFrame = new KeyFrame(
                Duration.seconds(Config.animationTime),
                event -> {
                    if (i.get() > instruction.length()) {
                        cmd.positionCaret(cmd.getLength());
                        timeline.stop();
                    }
                    else {
                        cmd.setText(instruction.substring(0, i.get()));
                        i.set(i.get() + 1);
                    }
                });
        timeline.getKeyFrames().add(keyFrame);
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }
}
