package com.ndominkiewicz.dfa.view;

import com.ndominkiewicz.dfa.model.geometry.NodeBounds;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

import java.util.Objects;

public class TransitionView extends AnchorPane {
    private enum Direction {
        TOP,
        BOTTOM,
        LEFT,
        RIGHT,
        TO_SELF
    }

    private final NodeBounds sourceBounds;
    private final NodeBounds targetBounds;

    private final Label transitionLabel = new Label();
    private final Path transitionPath = new Path();

    private final Direction direction;

    public TransitionView(NodeBounds sourceBounds, NodeBounds targetBounds, String symbols) {
        this.sourceBounds = sourceBounds;
        this.targetBounds = targetBounds;

        direction = evaluateDirection();

        setupStyle();
        setupPath();
        setupLabel(symbols);
    }

    public void highlight(Color color) {
        transitionPath.setStroke(color);
        transitionPath.setStrokeWidth(3);
    }

    public void resetHighlight() {
        transitionPath.setStroke(Color.BLACK);
        transitionPath.setStrokeWidth(2);
    }

    private void setupStyle() {
        getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/ndominkiewicz/dfa/styles/state.css")).toExternalForm());
        transitionLabel.getStyleClass().add("char");
    }

    private void setupPath() {
        Polygon arrow = new Polygon();
        int fixedSize = 10;
        int offset;
        switch (direction) {
            case TOP -> {
                offset = 10;
                transitionPath.setStrokeWidth(2);
                transitionPath.getElements().addAll(
                        new MoveTo(sourceBounds.getCenterX() + offset, sourceBounds.getStartY()),
                        new LineTo(targetBounds.getCenterX() + offset, targetBounds.getEndY())
                );
                arrow.getPoints().addAll(new Double[] {
                        targetBounds.getCenterX() + offset , targetBounds.getEndY() - 2,
                        targetBounds.getCenterX() + offset  - fixedSize, targetBounds.getEndY() + fixedSize,
                        targetBounds.getCenterX() + offset  + fixedSize, targetBounds.getEndY() + fixedSize
                });
            }
            case LEFT -> {
                offset = -10;
                transitionPath.setStrokeWidth(2);
                transitionPath.getElements().addAll(
                        new MoveTo(sourceBounds.getStartX(), sourceBounds.getCenterY() + offset),
                        new LineTo(targetBounds.getEndX(), targetBounds.getCenterY() + offset)
                );
                arrow.getPoints().addAll(
                        targetBounds.getEndX() - 2, targetBounds.getCenterY() + offset,
                        targetBounds.getEndX() + fixedSize, targetBounds.getCenterY() + offset - fixedSize,
                        targetBounds.getEndX() + fixedSize, targetBounds.getCenterY() + offset + fixedSize
                );
            }

            case RIGHT -> {
                offset = 10;
                transitionPath.setStrokeWidth(2);
                transitionPath.getElements().addAll(
                        new MoveTo(sourceBounds.getEndX(), sourceBounds.getCenterY() + offset),
                        new LineTo(targetBounds.getStartX(), targetBounds.getCenterY() + offset)
                );
                arrow.getPoints().addAll(
                        targetBounds.getStartX() + 2, targetBounds.getCenterY() + offset,
                        targetBounds.getStartX() - fixedSize, targetBounds.getCenterY() + offset - fixedSize,
                        targetBounds.getStartX() - fixedSize, targetBounds.getCenterY() + offset + fixedSize
                );
            }
            case BOTTOM -> {
                offset = -10;
                transitionPath.setStrokeWidth(2);
                transitionPath.getElements().addAll(
                        new MoveTo(sourceBounds.getCenterX() + offset, sourceBounds.getEndY()),
                        new LineTo(targetBounds.getCenterX() + offset, targetBounds.getStartY())
                );
                arrow.getPoints().addAll(new Double[] {
                        targetBounds.getCenterX() + offset , targetBounds.getStartY() + 2,
                        targetBounds.getCenterX() + offset  - fixedSize, targetBounds.getStartY() - fixedSize,
                        targetBounds.getCenterX() + offset  + fixedSize, targetBounds.getStartY() - fixedSize
                });
            }
            case TO_SELF -> {
                transitionPath.setStrokeWidth(2);
                final double centerX = sourceBounds.getCenterX();

                final double LOOP_DEPTH = 70;
                final double LOOP_WIDTH_OFFSET = 30;
                final double ARROW_OFFSET_RAD = 10;
                final double startY = sourceBounds.getEndY();

                double startX = centerX - ARROW_OFFSET_RAD;
                transitionPath.getElements().add(new MoveTo(startX, startY));

                double ctrl1X = centerX - LOOP_WIDTH_OFFSET;
                double ctrl1Y = startY + LOOP_DEPTH;

                double ctrl2X = centerX + LOOP_WIDTH_OFFSET;
                double ctrl2Y = startY + LOOP_DEPTH;

                double endX = centerX + ARROW_OFFSET_RAD;
                double endY = startY;

                transitionPath.getElements().add(
                        new CubicCurveTo(
                                ctrl1X, ctrl1Y,
                                ctrl2X, ctrl2Y,
                                endX, endY
                        )
                );
            }
        }
        getChildren().addAll(transitionPath, arrow);
    }

    private void setupLabel(String symbols) {
        transitionLabel.setText(symbols);
        double labelX, labelY;

        switch (direction) {
            case TOP -> {
                labelX = (sourceBounds.getCenterX() + targetBounds.getCenterX()) / 2 + 10;
                labelY = (sourceBounds.getStartY() + targetBounds.getEndY()) / 2;
                labelX += 5;
            }
            case BOTTOM -> {
                labelX = (sourceBounds.getCenterX() + targetBounds.getCenterX()) / 2 - 10;
                labelY = (sourceBounds.getEndY() + targetBounds.getStartY()) / 2;

                labelX -= 30;
            }
            case LEFT -> {
                labelX = (sourceBounds.getStartX() + targetBounds.getEndX()) / 2;
                labelY = (sourceBounds.getCenterY() + targetBounds.getCenterY()) / 2 - 10;
                labelY -= 25;
            }
            case RIGHT -> {
                labelX = (sourceBounds.getEndX() + targetBounds.getStartX()) / 2;
                labelY = (sourceBounds.getCenterY() + targetBounds.getCenterY()) / 2 + 10;
                labelY += 5;
            }
            default -> {
                labelX = sourceBounds.getCenterX() + 40;
                labelY = sourceBounds.getCenterY() - 40;
            }
        }

        transitionLabel.setLayoutX(labelX);
        transitionLabel.setLayoutY(labelY);

        getChildren().add(transitionLabel);
    }

    private Direction evaluateDirection() {
        if (sourceBounds.getCenterX() == targetBounds.getCenterX() &&
                sourceBounds.getCenterY() == targetBounds.getCenterY()) {
            return Direction.TO_SELF;
        }

        double width = calculateWidth();
        double height = calculateHeight();

        if (width > height) {
            return (targetBounds.getCenterX() > sourceBounds.getCenterX())
                    ? Direction.RIGHT
                    : Direction.LEFT;
        } else {
            return (targetBounds.getCenterY() > sourceBounds.getCenterY())
                    ? Direction.BOTTOM
                    : Direction.TOP;
        }
    }

    private double calculateWidth() {
        return Math.abs(targetBounds.getCenterX() - sourceBounds.getCenterX());
    }

    private double calculateHeight() {
        return Math.abs(targetBounds.getCenterY() - sourceBounds.getCenterY());
    }
}
