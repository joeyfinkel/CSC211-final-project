package com.example.pokemonviewer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
  private static Scene scene;

  static void setRoot(String fxml) throws IOException {
    scene.setRoot(loadFXML(fxml));
  }


  private static Parent loadFXML(String fxml) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml));

    return fxmlLoader.load();
  }

  @Override
  public void start(Stage stage) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("main-view.fxml"));
    scene = new Scene(loadFXML("main-view.fxml"), 800, 600);

    stage.setTitle("Pokémon Viewer");
    stage.getIcons().add(new Image(String.valueOf(App.class.getResource("assets/Icon.png"))));
    stage.setResizable(false);
    stage.setScene(scene);
    stage.show();
  }

  public static void main(String[] args) {
    launch();
  }
}