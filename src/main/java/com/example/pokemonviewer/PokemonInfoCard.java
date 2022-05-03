package com.example.pokemonviewer;

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
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public final class PokemonInfoCard {
  static Map<String, String> info;

  public PokemonInfoCard(String link, @NotNull String statType) throws IOException {
    switch (statType) {
      case "pokedex_data" -> this.info = Pokemon.getPokedexData(link);
      case "base_stats" -> this.info = Pokemon.getBaseStats(link);
      case "abilities" -> this.info = Pokemon.getAbilities(link);
    }
  }

  public Map<String, String> getInfo() {
    return info;
  }

  private @NotNull Text createTextField(String value) {
    Text textField = new Text(value);
    textField.setFill(Paint.valueOf("#feca05"));
    return textField;
  }

  private void populateGridPane(Object wrapper) {
    AtomicInteger idx = new AtomicInteger();

    while (idx.get() < this.info.size()) {
      this.info
          .forEach((k, v) -> {
            if (wrapper instanceof GridPane) {
              Text key = createTextField(Helpers.capitalizeFirstLetter(k));
              Text value = createTextField(v);

              ((GridPane) wrapper).add(key, 0, idx.get());
              ((GridPane) wrapper).add(value, 1, idx.get());
            } else {
              ScrollPane main = (ScrollPane) wrapper;
              VBox wrapperBox = new VBox(5);
              Text abilityText = createTextField(Helpers.capitalizeFirstLetter(k));
              Text abilityDesc = createTextField(v);


              wrapperBox.getChildren().addAll(abilityText, abilityDesc);
              main.setContent(wrapperBox);
            }

            idx.getAndIncrement();
          });
    }
  }

  public static void populateCard(String link, @NotNull Map<String, Object> dataObj) {
    if (info != null)
      info.clear();
    dataObj.forEach((key, value) -> {
      try {
        String name = Pokemon.getName(link);

        switch (key) {
          case "name" -> {
            Text text = (Text) value;
            text.setText(name);
          }
          case "image" -> {
            Image image = new Image(Pokemon.getPicture(link));
            ImageView imageRef = (ImageView) value;
            imageRef.setImage(image);
          }
          case "evolution" -> {
            GridPane evolvedCard = new GridPane();
            VBox wrapper = (VBox) value;
            Text title = new Text("Evolutions");
            String speciesUrl = Pokemon.getSpeciesUrl(link);
            String evolutionChainUrl = Pokemon.getEvolutionChainUrl(speciesUrl);
            ArrayList<String> evolvesTo = Pokemon.getEvolutionNames(evolutionChainUrl, name);
            String nextEvolution, evolutionUrl = "";
            boolean isMax = false;

            for (int i = 0; i < evolvesTo.size(); i++) {
              ArrayList<String> evolutionList = evolvesTo;

              if (evolutionList.get(i).equalsIgnoreCase(name)) {
                System.out.println(evolutionList.get(i) + "=" + name);
                if (i == evolvesTo.size()) {
                  isMax = true;
                  nextEvolution = evolutionList.get(i);
                } else {
                  nextEvolution = evolutionList.get(i + 1);
                }
                evolutionUrl = API.BASE_URL + "pokemon/" + nextEvolution;
              }

            }

            if (isMax) {
              Text noEvolutionText = new Text(name + "has no evolutions");
              noEvolutionText.setFill(Paint.valueOf("#feca05"));
              noEvolutionText.setFont(Font.font("system", 15));

              wrapper.getChildren().clear();
              wrapper.getChildren().addAll(title, noEvolutionText);
            } else {
              String finalEvolutionUrl = evolutionUrl;
              evolvedCard = new PokemonCard(evolutionUrl, 12, 100, 100)
                  .onClick(e -> populateCard(finalEvolutionUrl, dataObj)).onHover().getCard();

              title.setFill(Paint.valueOf("#feca05"));
              title.setFont(Font.font("system", FontWeight.BOLD, 15));
              wrapper.getChildren().clear();
              wrapper.getChildren().addAll(title, evolvedCard);
            }
          }
          default -> new PokemonInfoCard(link, key)
              .populateGridPane(value);
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    });

  }

}