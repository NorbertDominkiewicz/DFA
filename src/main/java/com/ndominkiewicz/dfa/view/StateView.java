package com.ndominkiewicz.dfa.view;

import com.ndominkiewicz.dfa.model.State;
import com.ndominkiewicz.dfa.model.geometry.NodeBounds;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;

import java.util.Objects;

public class StateView extends StackPane {
    private static final int SIZE = 120;
    private static final int RADIUS = 40;

    private final Circle stateCircle = new Circle();
    private final Label stateLabel = new Label();
    private final NodeBounds stateBounds;
    private final String name;
    private final State.Type type;

    public StateView(int posX, int posY, String name, State.Type type) {
        this.name = name;
        this.type = type;

        stateBounds = new NodeBounds(SIZE, posX, posY);

        setPrefSize(SIZE, SIZE);
        setLayoutX(posX);
        setLayoutY(posY);

        setupStyles();
        setupCircle();
        setupLabel();
    }

    public void highlight(Color color) {
        stateCircle.setStroke(color);
        stateCircle.setStrokeWidth(3);
    }

    public void resetHighlight() {
        stateCircle.setStroke(Color.BLACK);
        stateCircle.setStrokeWidth(2);
    }

    public NodeBounds getStateBounds() {
        return stateBounds;
    }

    private void setupCircle() {
        stateCircle.setRadius(RADIUS);
        stateCircle.setFill(Color.WHITE);
        stateCircle.setStroke(Color.BLACK);
        stateCircle.setStrokeWidth(2);

        if (type.equals(State.Type.ACCEPTING)) {
            Circle outerCircle = new Circle();
            outerCircle.setRadius(RADIUS + 6);
            outerCircle.setFill(Color.TRANSPARENT);
            outerCircle.setStroke(Color.BLACK);
            outerCircle.setStrokeWidth(2);

            getChildren().add(outerCircle);
        }

        if (type.equals(State.Type.INITIAL)) {
            double arrowOffset = RADIUS + 20;
            double arrowSize = 10;
            double centerY = SIZE / 2.0;
            double centerX = SIZE / 2.0;

            Polygon initialArrow = new Polygon();
            initialArrow.setFill(Color.ORANGERED);
            initialArrow.getPoints().addAll(new Double[]{
                    centerX - arrowOffset - arrowSize, centerY - arrowSize / 2,
                    centerX - arrowOffset - arrowSize, centerY + arrowSize / 2,
                    centerX - arrowOffset, centerY
            });
            initialArrow.setTranslateX(-arrowOffset);
            initialArrow.setTranslateY(0);

            getChildren().add(initialArrow);
        }

        getChildren().add(stateCircle);
    }

    private void setupLabel() {
        stateLabel.setText(name);
        getChildren().add(stateLabel);
        setAlignment(Pos.CENTER);
    }

    private void setupStyles() {
        getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/ndominkiewicz/dfa/styles/state.css")).toExternalForm());
        getStyleClass().add("root");
    }
}
