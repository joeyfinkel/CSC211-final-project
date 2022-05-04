package com.example.pokemonviewer;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class CategoryViewController {
  String selectedCategory;
  List<String> links;
  int initialPokemon = 15;
  @FXML
  Text title;
  @FXML
  Text pokemonName;
  @FXML
  Label btnBack;
  @FXML
  Label btnClose;
  @FXML
  ImageView pokemonImage;
  @FXML
  VBox pokemonCardWrapper;
  @FXML
  Pane infoCard;
  @FXML
  ScrollPane sPane;
  @FXML
  GridPane pokedexDataContainer;
  @FXML
  GridPane baseStatsContainer;
  @FXML
  GridPane menuWrapper;
  @FXML
  ScrollPane abilitiesWrapper;
  @FXML
  VBox pokemonEvolutionsWrapper;

  @Contract("_, _ -> param1")
  private @NotNull HBox createRow(HBox hbox, @NotNull List<String> links) {
    new Thread(() -> links.forEach(link -> {
      try {
        GridPane pokemonCard = new PokemonCard(link).onClick(e -> {
          PokemonInfoCard.setInfo(new HashMap<>());
          PokemonInfoCard.populateCard(link, new HashMap<>() {{
            put("name", pokemonName);
            put("image", pokemonImage);
            put("pokedex_data", pokedexDataContainer);
            put("base_stats", baseStatsContainer);
            put("abilities", abilitiesWrapper);
            put("evolution", pokemonEvolutionsWrapper);
          }});

          menuWrapper.setDisable(true);
          sPane.setDisable(true);
          pokemonCardWrapper.setOpacity(.2);
          infoCard.setVisible(true);
        }).onHover().getCard();

        hbox.setMinWidth(800);
        hbox.setAlignment(Pos.CENTER);
        Platform.runLater(() -> hbox.getChildren().add(pokemonCard));
      } catch (IOException e) {
        e.printStackTrace();
      }
    })).start();
    return hbox;
  }

  private void createRows(@NotNull List<String> urls) {
    new Thread(() -> {
      int index = 0;

      while (index < urls.size()) {
        HBox hbox = new HBox(150);
        List<String> links = urls.subList(index, index + 5);

        Platform.runLater(() -> pokemonCardWrapper.getChildren().addAll(createRow(hbox, links)));

        index += 5;
      }
    }).start();
  }

  /**
   * Displays more rows of Pokémon cards on scroll.
   */
  @FXML
  private void handleScroll() throws IOException {
    int linkIdx = this.initialPokemon;
    int idx = 0;
    this.links = Pokemon.urls(this.selectedCategory).subList(linkIdx, Pokemon.urls(this.selectedCategory).size());

    // Only trigger when scrolling down
    if(sPane.getVvalue() == 1.2) {
      while (idx < this.links.size() / 3) {
        if (((this.links.size() / 3) == pokemonCardWrapper.getChildren().size())
            || idx > this.links.size()) return;
        createRows(this.links.subList(idx, idx += 5));
      }
    }
  }

  @FXML
  private void backToMainMenu() {
    try {
      App.setRoot("main-view.fxml");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @FXML
  private void closeInfoCard() {
    infoCard.setVisible(false);
    menuWrapper.setDisable(false);
    sPane.setDisable(false);
    pokemonCardWrapper.setOpacity(1);
    PokemonInfoCard.info.clear();
  }

  public void initialize() throws IOException {
    String titleText = String.valueOf(Pokemon.getSelectedCategory().keySet()).replace("[", "").replace("]", "");
    this.selectedCategory = String.valueOf(Pokemon.getSelectedCategory().values()).replace("[", "").replace("]", "");
    this.links = Pokemon.urls(this.selectedCategory).subList(0, this.initialPokemon);

    pokemonCardWrapper.setViewOrder(10);
    title.setText(titleText);
    title.setFont(Font.font("system", FontWeight.BOLD, 45));
    createRows(this.links);
  }
}
