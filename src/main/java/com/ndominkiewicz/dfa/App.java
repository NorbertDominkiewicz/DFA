package com.ndominkiewicz.dfa;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @author Norbert Dominkiewicz
 */

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(loadFXML(), 1200, 900);

        stage.setTitle("Automat Deterministyczny Skończony");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    private Parent loadFXML() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/app.fxml"));
        return fxmlLoader.load();
    }

    public static void main(String [] args) {
        launch();
    }
}
