package com.example.pokemonviewer;

import com.google.gson.stream.JsonToken;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CategoryViewController implements Initializable {
  @FXML
  Text title;
  @FXML
  VBox nameContainer;


  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {

    try {
      JSONArray pokemonType = Pokemon.specificPokemonType(1);
      ArrayList<String> urls = new ArrayList<>();

      title.setText(Pokemon.selectedCategory());

      for (int i = 0; i < pokemonType.length(); i++) {
        JSONObject pokemonList = (JSONObject) pokemonType.getJSONObject(i).get("pokemon");

        urls.add(pokemonList.getString("url"));
      }

      urls.forEach(_url -> {
        Text pokemonName = new Text();

        try {
          pokemonName.setText(Pokemon.getPokemonName(_url));
          nameContainer.getChildren().addAll(pokemonName);

        } catch (IOException e) {
          e.printStackTrace();
        }
      });

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
