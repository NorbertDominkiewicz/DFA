package com.ndominkiewicz.dfa.model;

import com.ndominkiewicz.dfa.view.TransitionView;
import javafx.scene.Node;

public class Transition {
    private final State sourceState;
    private final State targetState;
    private final char [] symbols;
    private final TransitionView view;

    public Transition(State sourceState, State targetState, char [] symbols) {
        this.sourceState = sourceState;
        this.targetState = targetState;
        this.symbols = symbols;

        view = new TransitionView(sourceState.getView().getStateBounds(), targetState.getView().getStateBounds(), symbolsToString());
    }

    public Node getViewNode() {
        return view;
    }

    public TransitionView getView() {
        return view;
    }

    public State getSourceState() {
        return sourceState;
    }

    public State getTargetState() {
        return targetState;
    }

    public char[] getSymbols() {
        return symbols;
    }

    private String symbolsToString() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < symbols.length; i++) {
            if (i == symbols.length - 1) {
                result.append(symbols[i]);
            } else {
                result.append(symbols[i]).append(",");
            }
        }
        return result.toString();
    }
}
