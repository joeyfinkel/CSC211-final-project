package com.example.pokemonviewer;

import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
  @FXML
  private Pane mainPane;
  @FXML
  private ChoiceBox choiceBox;



  /* Populates the dropdown with all the Pokémon categories. */
  private void initializeChoiceBox(Pokemon pokemon) {
    choiceBox.setValue("Categories");
    choiceBox.getItems().addAll(pokemon.getCategoryNames());
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    Pokemon pokemon = Pokemon.getInstance();

    initializeChoiceBox(pokemon);

    choiceBox.getSelectionModel().selectedIndexProperty().addListener((selectedItem) -> {
      ReadOnlyIntegerProperty categoryIdx = (ReadOnlyIntegerProperty) selectedItem;

      try {
        Pane categoryView = FXMLLoader.load(getClass().getResource("category-view.fxml"));

        pokemon.setSelectedCategoryIdx(categoryIdx.getValue() + 1);
        mainPane.getChildren().clear();
        mainPane.getChildren().add(categoryView);
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
  }

}