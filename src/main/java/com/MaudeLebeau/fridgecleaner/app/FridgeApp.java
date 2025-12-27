package com.MaudeLebeau.fridgecleaner.app;

import com.MaudeLebeau.fridgecleaner.repository.SchemaInitializer;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FridgeApp extends javafx.application.Application {
    @Override public void start(Stage stage) throws Exception {
        try {
            SchemaInitializer.init();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        FXMLLoader loader = new FXMLLoader(
                FridgeApp.class.getResource("/com/MaudeLebeau/fridgecleaner/ui/MainView.fxml"));
        Scene scene = new Scene(loader.load(), 980, 620);
        stage.setTitle("Fridge Cleaner");
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) { launch(args);}
}
