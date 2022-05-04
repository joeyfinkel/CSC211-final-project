package com.example.pokemonviewer;

import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public final class PokemonInfoCard {
  static Map<String, String> info = new HashMap<>();

  public PokemonInfoCard(String link, @NotNull String statType) throws IOException {
    info.clear();
    switch (statType) {
      case "pokedex_data" -> info = Pokemon.getPokedexData(link);
      case "base_stats" -> info = Pokemon.getBaseStats(link);
      case "abilities" -> info = Pokemon.getAbilities(link);
    }
  }

  public Map<String, String> getInfo() {
    return info;
  }

  public static void setInfo(Map<String, String> info) {
    PokemonInfoCard.info = info;
  }

  private static @NotNull Label createLabel(String value) {
    Label label = new Label(value);
    label.setTextFill(Paint.valueOf("#feca05"));
    label.setWrapText(true);
    return label;
  }

  private static @NotNull Label createLabel(String value, boolean bolded) {
    Label label = new Label(value);
    label.setTextFill(Paint.valueOf("#feca05"));
    if (bolded) label.setStyle("-fx-font-weight: bold");
    return label;
  }

  private static void addToGrid(@NotNull GridPane gridPane, String k, String v, @NotNull AtomicInteger idx) {
    @NotNull Label key = createLabel(Helpers.capitalizeWord(k));
    @NotNull Label value = createLabel(v);

    gridPane.add(key, 0, idx.get());
    gridPane.add(value, 1, idx.get());
  }

  private static void addToScrollPane(@NotNull ScrollPane sp, String k, String v, @NotNull AtomicInteger idx) {
    VBox vBox = new VBox();
    GridPane gridPane = new GridPane();
    @NotNull Label abilityText = createLabel(k, true);
    @NotNull Label abilityDesc = createLabel(v);

    vBox.getChildren().addAll(abilityText, abilityDesc);
    gridPane.add(vBox, 0, idx.get());
    abilityDesc.setTranslateX(5);
    sp.setContent(gridPane);
  }

  private void populateGridPane(Object wrapper) {
    AtomicInteger idx = new AtomicInteger();

    while (idx.get() < info.size()) {
      info.forEach((k, v) -> {
        if (wrapper instanceof GridPane) addToGrid((GridPane) wrapper, k, v, idx);
        else addToScrollPane((ScrollPane) wrapper, k, v, idx);

        idx.getAndIncrement();
      });
    }
  }

  private static void addToCard(@NotNull String key, Object value, String link) throws IOException {
    switch (key) {
      case "name" -> {
        Text text = (Text) value;
        text.setText(Pokemon.getName(link));
      }
      case "image" -> {
        Image image = new Image(Pokemon.getPicture(link));
        ImageView imageRef = (ImageView) value;
        imageRef.setImage(image);
      }
      default -> throw new Error(key + " is not valid");
    }
  }

  public static void populateCard(String link, @NotNull Map<String, Object> dataObj) {
    info.clear();

    dataObj.forEach((key, value) -> {
      try {
        switch (key) {
          case "name", "image" -> addToCard(key, value, link);
          case "evolution" -> {
            GridPane evolvedCard;
            VBox wrapper = (VBox) value;
            Text title = new Text("Evolutions");
            String name = Pokemon.getName(link);
            String speciesUrl = Pokemon.getSpeciesUrl(link);
            String evolutionChainUrl = Pokemon.getEvolutionChainUrl(speciesUrl);
            ArrayList<String> evolvesTo = Pokemon.getEvolutionNames(evolutionChainUrl, name);
            String nextEvolution, evolutionUrl = "";
            boolean isMax = false;


            for (int i = 0; i < evolvesTo.size() - 1; i++) {
              ArrayList<String> evolutionList = evolvesTo;

              if (evolutionList.get(i).equalsIgnoreCase(name)) {
                nextEvolution = i == evolvesTo.size() ? evolutionList.get(i) : evolutionList.get(i + 1);
                evolutionUrl = API.BASE_URL + "pokemon/" + nextEvolution;
              }

            }

            String finalEvolutionUrl = evolutionUrl;
            evolvedCard = new PokemonCard(evolutionUrl, 12, "#2b72b8", 100, 100)
                .onClick(e -> {
                  info.clear();
                  populateCard(finalEvolutionUrl, dataObj);
                }).onHover().getCard();

            title.setFill(Paint.valueOf("#feca05"));
            title.setFont(Font.font("system", FontWeight.BOLD, 18));
            wrapper.getChildren().clear();
            wrapper.getChildren().addAll(title, evolvedCard);
          }
          default -> new PokemonInfoCard(link, key).populateGridPane(value);
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    });

  }

}