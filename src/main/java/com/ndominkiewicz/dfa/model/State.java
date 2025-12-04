package com.ndominkiewicz.dfa.model;

import com.ndominkiewicz.dfa.view.StateView;
import javafx.scene.Node;

import java.util.ArrayList;
import java.util.List;

public class State {
    public enum Type {
        DEFAULT,
        INITIAL,
        ACCEPTING
    }

    private final String name;
    private final Type type;
    private final List<Transition> transitions = new ArrayList<>();
    private final StateView view;

    public State(String name, Type type, int posX, int posY) {
        this.name = name;
        this.type = type;

        view = new StateView(posX, posY, name, type);
    }

    public void addTransition(State target, char[] symbols) {
        Transition transition = new Transition(this, target, symbols);

        transitions.add(transition);
    }

    public StateView getView() {
        return view;
    }

    public Type getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public List<Transition> getTransitions() {
        return transitions;
    }

    public Node getViewNode() {
        return view;
    }
}
