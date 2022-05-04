package com.example.pokemonviewer;

import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.ComboBox;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ResourceBundle;

public class MainController implements Initializable {
  LinkedHashMap<String, String> pokemonTypes;
  @FXML
  private ComboBox<String> comboBox;

  /**
   * Populates the dropdown with all the Pokémon categories.
   *
   */
  private void initializeDropDown() throws IOException {
    this.pokemonTypes = Pokemon.getTypes();

    comboBox.setValue("Categories");
    comboBox.getItems().addAll(this.pokemonTypes.keySet());
  }

  /**
   * The event listener for the dropdown.
   */
  @FXML
  private void selectCategory() {
    comboBox.getSelectionModel().selectedIndexProperty().addListener((selectedItem) -> {
      try {
        ReadOnlyIntegerProperty categoryIdx = (ReadOnlyIntegerProperty) selectedItem;
        var types = this.pokemonTypes.keySet().toArray();

        Pokemon.setSelectedCategory(new HashMap<>() {
          {
            put((String) types[categoryIdx.getValue()],
                pokemonTypes.get((String) types[categoryIdx.getValue()]));
          }
        });

        App.setRoot("category-view.fxml");
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    comboBox.setOnMouseEntered(e -> comboBox.setCursor(Cursor.HAND));

    try {
      initializeDropDown();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}