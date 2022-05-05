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
import java.util.Hashtable;
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

  /**
   * Creates a row of Pokémon cards.
   *
   * @param row   The {@link HBox} that represents a row.
   * @param links A list of Pokémon links. Each row will be created with these links.
   * @return A row of Pokémon cards.
   */
  @Contract("_, _ -> param1")
  private @NotNull HBox createRow(HBox row, @NotNull List<String> links) {
    // Create a new thread to speed help with loading in the cards.
    new Thread(() -> links.forEach(link -> {
      try {
        GridPane pokemonCard = new PokemonCard(link).onClick(e -> {
          HashMap<String, Object> cardInfo = new HashMap<>() {{
            put("name", pokemonName);
            put("image", pokemonImage);
            put("pokedex_data", pokedexDataContainer);
            put("base_stats", baseStatsContainer);
            put("abilities", abilitiesWrapper);
            put("evolution", pokemonEvolutionsWrapper);
          }};

          PokemonInfoCard.populateCard(link, cardInfo);

          menuWrapper.setDisable(true);
          sPane.setDisable(true);
          pokemonCardWrapper.setDisable(true);
          infoCard.setVisible(true);
        }).onHover().getCard();

        row.setMinWidth(800);
        row.setAlignment(Pos.CENTER);
        Platform.runLater(() -> row.getChildren().add(pokemonCard));
      } catch (IOException e) {
        e.printStackTrace();
      }
    })).start();
    return row;
  }

  /**
   * Creates multiple rows of Pokémon cards that are displayed on the GUI.
   *
   * @param urls A list of Pokémon links. Each row will be created with these links.
   */
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
    if (sPane.getVvalue() == 1.2) {
      while (idx < this.links.size() / 3) {
        if (((this.links.size() / 3) == pokemonCardWrapper.getChildren().size())
                || idx > this.links.size()) return;
        createRows(this.links.subList(idx, idx += 5));
      }
    }
  }

  /**
   * The event handler for the back button.
   * Once clicked, you will be returned to the main menu.
   */
  @FXML
  private void backToMainMenu() {
    try {
      App.setRoot("main-view.fxml");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * The event handler for the X button on the info card.
   * Once clicked, the info card will be closed.
   */
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
