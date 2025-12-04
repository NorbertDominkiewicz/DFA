package com.ndominkiewicz.dfa.service;

import com.ndominkiewicz.dfa.model.State;
import com.ndominkiewicz.dfa.model.Transition;
import com.ndominkiewicz.dfa.util.Constants;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DFAService {
    private final List<State> states = new ArrayList<>();
    private final List<Transition> transitions = new ArrayList<>();

    private State stateQ0, stateQ1, stateQ2, stateQ3, stateQ4, stateQ5;

    private HBox lettersDisplay;
    private long delay;
    private Thread simulationThread;
    private volatile boolean isRunning = false;
    private String currentWord = "";
    private int currentSymbolIndex = 0;
    private State currentState;

    public DFAService() {
        stateQ0 = new State("Q0", State.Type.INITIAL, 200, 200);
        stateQ1 = new State("Q1", State.Type.DEFAULT, 450, 200);
        stateQ2 = new State("Q2", State.Type.DEFAULT, 700, 200);
        stateQ3 = new State("Q3", State.Type.ACCEPTING, 700, 400);
        stateQ4 = new State("Q4", State.Type.DEFAULT, 450, 400);
        stateQ5 = new State("Q5", State.Type.DEFAULT, 200, 400);

        states.add(stateQ0);
        states.add(stateQ1);
        states.add(stateQ2);
        states.add(stateQ3);
        states.add(stateQ4);
        states.add(stateQ5);

        setupTransitions();

        currentState = stateQ0;
    }

    public void draw(AnchorPane contentPane) {
        for (State state : states) contentPane.getChildren().add(state.getViewNode());
        for (Transition transition : transitions) contentPane.getChildren().add(transition.getViewNode());
    }

    public void setLettersDisplay(HBox lettersDisplay) {
        this.lettersDisplay = lettersDisplay;
    }

    public void setDelay(long delay) {
        this.delay = delay > 0 ? delay : 1000;
    }

    public void setCurrentWord(String currentWord) {
        this.currentWord = currentWord;
    }

    public void run() {
        if (isRunning || currentWord.isEmpty()) {
            return;
        }
        isRunning = true;

        fillDisplay();

        simulationThread = new Thread(() -> {
            try {
                Platform.runLater(() -> {
                    highlightState(currentState, Color.ORANGE);
                });
                TimeUnit.MILLISECONDS.sleep((long) (delay * 0.33));

                while (currentSymbolIndex < currentWord.length()) {
                    if (!isRunning) break;

                    char currentChar = currentWord.charAt(currentSymbolIndex);
                    int displayIndex = currentSymbolIndex;

                    Platform.runLater(() ->{
                        highlightSymbol(displayIndex);
                        highlightState(currentState, Color.ORANGE);
                    });

                    TimeUnit.MILLISECONDS.sleep((long) (delay * 0.33));

                    Transition transition = findTransition(currentState, currentChar);

                    if (transition != null) {
                        Platform.runLater(() -> {
                            highlightTransition(transition, Color.ORANGE);
                            resetSymbolHighlights();

                            State nextState = transition.getTargetState();
                            highlightState(currentState, Color.BLACK);

                            currentState = nextState;
                            currentSymbolIndex++;

                            highlightState(currentState, Color.YELLOW);
                        });
                        TimeUnit.MILLISECONDS.sleep((long) (delay * 0.33));
                        Platform.runLater(this::resetHighlights);
                    } else {
                        Platform.runLater(() -> {
                            highlightSymbol(displayIndex);
                            highlightState(currentState, Color.RED);
                            isRunning = false;
                        });
                        break;
                    }
                }
                Platform.runLater(() -> {
                    resetSymbolHighlights();
                    if (currentSymbolIndex >= currentWord.length() && isRunning) {
                        checkFinalResult();
                    } else if (currentSymbolIndex < currentWord.length() && !isRunning) {
                        highlightState(currentState, Color.RED);
                    }
                    isRunning = false;
                });
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
                Platform.runLater(() -> isRunning = false);
            }
        });
        simulationThread.setDaemon(true);
        simulationThread.start();
    }

    public void reset() {
        currentWord = "";
        currentSymbolIndex = 0;
        currentState = stateQ0;
        resetSymbolHighlights();
        resetHighlights();
        if (lettersDisplay != null) {
            lettersDisplay.getChildren().clear();
        }
    }

    private void fillDisplay() {
        for (int letter = 0; letter < currentWord.length(); letter++) {
            lettersDisplay.getChildren().add(
                    new Label(String.valueOf(currentWord.charAt(letter)))
            );
        }
    }

    private void setupTransitions() {
        stateQ0.addTransition(stateQ5, Constants.XZ_SYMBOLS);
        stateQ0.addTransition(stateQ1, Constants.Y_SYMBOL);
        stateQ1.addTransition(stateQ2, Constants.Y_SYMBOL);
        stateQ1.addTransition(stateQ4, Constants.XZ_SYMBOLS);
        stateQ2.addTransition(stateQ1, Constants.Y_SYMBOL);
        stateQ2.addTransition(stateQ3, Constants.XZ_SYMBOLS);
        stateQ3.addTransition(stateQ2, Constants.XZ_SYMBOLS);
        stateQ3.addTransition(stateQ4, Constants.Y_SYMBOL);
        stateQ4.addTransition(stateQ3, Constants.Y_SYMBOL);
        stateQ4.addTransition(stateQ1, Constants.XZ_SYMBOLS);
        stateQ5.addTransition(stateQ5, Constants.XYZ_SYMBOLS);

        for (State state : states) {
            transitions.addAll(state.getTransitions());
        }
    }

    private Transition findTransition(State source, char symbol) {
        for (Transition transition : source.getTransitions()) {
            for (char c : transition.getSymbols()) {
                if (c == symbol) {
                    return transition;
                }
            }
        }
        return null;
    }

    private void checkFinalResult() {
        if (currentState.getType() == State.Type.ACCEPTING) {
            highlightState(currentState, Color.GREEN);
        } else {
            highlightState(currentState, Color.RED);
        }
    }

    private void highlightSymbol(int index) {
        if (lettersDisplay != null && index >= 0 && index < lettersDisplay.getChildren().size()) {
            Platform.runLater(() -> {
                Label symbolLabel = (Label) lettersDisplay.getChildren().get(index);
                symbolLabel.setStyle("-fx-background-color: orange; -fx-font-weight: bold;");
            });
        }
    }

    private void resetSymbolHighlights() {
        if (lettersDisplay != null) {
            Platform.runLater(() -> {
                for (Node node : lettersDisplay.getChildren()) {
                    if (node instanceof Label) {
                        Label symbolLabel = (Label) node;
                        symbolLabel.setStyle("-fx-background-color: transparent; -fx-font-weight: normal;");
                    }
                }
            });
        }
    }

    private void highlightState(State state, Color color) {
        Platform.runLater(() -> {
            state.getView().highlight(color);
        });
    }

    private void highlightTransition(Transition transition, Color color) {
        Platform.runLater(() -> {
            transition.getView().highlight(color);
        });
    }

    private void resetHighlights() {
        Platform.runLater(() -> {
            for (State state : states) {
                state.getView().resetHighlight();
            }
            for (Transition transition : transitions) {
                transition.getView().resetHighlight();
            }
        });
    }
}
