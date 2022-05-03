package com.example.pokemonviewer;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import static javafx.scene.layout.GridPane.setHalignment;
import static javafx.scene.layout.GridPane.setValignment;

/**
 * The base class for creating a card.
 *
 * @author joeyf
 */
public class Card {
  private static Text title;
  private static double width = 0;
  private static double height = 0;
  private static Card cardInstance = null;

  private Card() {
  }

  public static Card getInstance() {
    if (cardInstance == null)
      cardInstance = new Card();

    return cardInstance;
  }

  private static void setCardStyle(@NotNull GridPane card, double width, double height) {
    setHalignment(title, HPos.CENTER);
    setValignment(title, VPos.CENTER);
    card.setStyle("-fx-background: #2B72B8;"
        + "-fx-background-radius: 30px; "
        + "-fx-border-radius: 30px;");
    card.setMinSize(width, height);
    card.setMaxSize(width, height);
    card.setAlignment(Pos.CENTER);
    card.setTranslateZ(5);
  }

  /**
   * Creates the title section for the card.
   *
   * @param text The title for the card.
   * @return The title node.
   */
  public static @NotNull Text cardTitle(String text, int fontSize) {
    Text title = new Text(text);

    title.setFill(Paint.valueOf("#feca05"));
    title.setStyle("-fx-font-weight: bold; -fx-font-size: " + fontSize);
    title.setTextAlignment(TextAlignment.CENTER);

    return title;
  }

  /**
   * Creates a Pokémon card for {@code url} specified.
   *
   * @param url The url of the specific Pokémon.
   * @return A Pokémon card with the name and image.
   */
  public GridPane create(String url) throws IOException {
    GridPane card = new GridPane();
    ImageView image = new ImageView(Pokemon.getPicture(url));
    title = cardTitle(Pokemon.getName(url), 24);

    setCardStyle(card, width, height);
    card.add(title, 0, 0);
    card.add(image, 0, 1);

    return card;
  }

  /**
   * Creates a Pokémon card for {@code url} specified.
   *
   * @param url The url of the specific Pokémon.
   * @return A Pokémon card with the name and image.
   */
  public GridPane create(String url, int fontSize, double width, double height) throws IOException {
    GridPane card = new GridPane();
    ImageView image = new ImageView(Pokemon.getPicture(url));
    title = cardTitle(Pokemon.getName(url), fontSize);

    setCardStyle(card, width, height);
    card.add(title, 0, 0);
    card.add(image, 0, 1);

    return card;
  }
}
