package com.example.demo;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HelloApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        VBox rootVBox = new VBox();
        HBox buttonBox = new HBox();
        VBox contentBox = new VBox();
        Button tab[] = new Button[7];

        Scene scene = new Scene(rootVBox, 880, 450);

        rootVBox.setStyle("-fx-background-color: rgb(150, 240, 250);");

        tab[0] = new Button("Address");
        tab[1] = new Button("Car");
        tab[2] = new Button("Car_Part");
        tab[3] = new Button("Customer");
        tab[4] = new Button("Device");
        tab[5] = new Button("Manufacture");
        tab[6] = new Button("Orders");

        for (int i = 0; i < 7; i++) {
            int fi = i;
            tab[i].setOnAction(e -> {
                contentBox.getChildren().clear();
                contentBox.getChildren().add(AnchorCreater.createTabContent(tab[fi].getText()));
            });
            buttonBox.getChildren().add(tab[i]);
        }

        rootVBox.getChildren().addAll(buttonBox, contentBox);

        stage.setScene(scene);
        stage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });

        stage.show();
    }
}
