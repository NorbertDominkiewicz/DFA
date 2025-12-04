package com.ndominkiewicz.dfa.controller;

import com.ndominkiewicz.dfa.service.DFAService;
import com.ndominkiewicz.dfa.util.Constants;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    private final DFAService service = new DFAService();

    @FXML private AnchorPane contentPane;
    @FXML private TextField delayInput;
    @FXML private TextField wordInput;
    @FXML private HBox lettersDisplay;
    @FXML private Button startButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        startButton.setDisable(true);
        drawStates();
        applyListeners();
    }

    @FXML
    private void handleStartButton() {
        String word = wordInput.getText().trim().toLowerCase();
        String delayText = delayInput.getText().trim();

        service.reset();
        service.setCurrentWord(word);
        service.setLettersDisplay(lettersDisplay);
        service.setDelay(Long.parseLong(delayText));
        service.run();
    }

    private void drawStates() {
        service.draw(contentPane);
    }

    private void applyListeners() {
        wordInput.setOnKeyReleased(actionEvent -> {
            startButton.setDisable(!isInputValid());
        });
    }

    private boolean isInputValid() {
        String word = wordInput.getText();
        String delay = delayInput.getText();

        if (word == null || word.isEmpty()) {
            return false;
        }

        if (delay == null || delay.isEmpty()) {
            return false;
        }

        for (char c : word.toCharArray()) {
            if (!isValidSymbol(c)) {
                return false;
            }
        }
        return true;
    }

    private boolean isValidSymbol(char c) {
        for (char validChar : Constants.ALPHABET) {
            if (c == validChar) {
                return true;
            }
        }
        return false;
    }
}
