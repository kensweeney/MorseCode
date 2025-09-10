package com.ken.camel;

import javax.sound.sampled.LineUnavailableException;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PlayMorseCodeUI extends Application {
    private  PlayMorseCode morsePlayer;
    // No explicit constructor; use default

    @Override
    public void start(Stage primaryStage) {
        if(morsePlayer == null) {
            try {
                morsePlayer = new PlayMorseCode(false);
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            }
        }
        TextArea inputField = new TextArea();
        inputField.setPrefColumnCount(80);
        inputField.setPrefRowCount(24);
        inputField.setPromptText("Enter text to play as Morse code");

        ComboBox<Integer> wpmCombo = new ComboBox<>();
        wpmCombo.getItems().addAll(5, 10, 15, 20, 25, 30, 35, 40, 45, 50);
        wpmCombo.setValue(morsePlayer.wpm); // Default to current wpm
        wpmCombo.setPromptText("Words Per Minute (WPM)");
        wpmCombo.valueProperty().addListener((obs, oldVal, newVal) -> {
            morsePlayer.setWpm(newVal);
        });

        inputField.addEventHandler(javafx.scene.input.KeyEvent.KEY_PRESSED, event -> {
            switch (event.getCode()) {
                case ENTER:
                    String text = inputField.getText();
                    if (text != null && !text.isEmpty()) {
                        try {
                            morsePlayer.playString(text);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                    event.consume(); // Prevent adding a new line
                    break;
                default:
                    break;
            }
        });
        Button playButton = new Button("Play Morse Code");

        playButton.setOnAction(e -> {
            String text = inputField.getText();
            if (text != null && !text.isEmpty()) {
                try {
                    morsePlayer.playString(text);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        VBox root = new VBox(10, wpmCombo, inputField, playButton);
        root.setStyle("-fx-padding: 20;");
        Scene scene = new Scene(root, 800, 400);
        primaryStage.setTitle("Play Morse Code");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
