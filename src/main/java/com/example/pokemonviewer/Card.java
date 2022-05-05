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
  private static Card cardInstance = null;

  private Card() {
  }

  /**
   * Instantiates a new {@link Card}.
   *
   * @return An instance of a {@link Card}.
   */
  public static Card getInstance() {
    if (cardInstance == null)
      cardInstance = new Card();

    return cardInstance;
  }

  /**
   * Adds styles to the card.
   *
   * @param card   The card to add the styles to.
   * @param color  The background color of the card.
   * @param width  The width of the card.
   * @param height The height of the card.
   */
  private static void setCardStyle(@NotNull GridPane card, String color, double width, double height) {
    setHalignment(title, HPos.CENTER);
    setValignment(title, VPos.CENTER);
    card.setStyle("-fx-background: " + color + ";"
            + "-fx-background-color: " + color + ";"
            + "-fx-background-radius: 30px; "
            + "-fx-border-radius: 30px;");
    card.setMinSize(width, height);
    card.setMaxSize(width, height);
    card.setAlignment(Pos.CENTER);
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

    double width = 0;
    double height = 0;
    setCardStyle(card, "#2B72B8", width, height);
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
  public GridPane create(String url, int fontSize, String color, double width, double height) throws IOException {
    GridPane card = new GridPane();
    ImageView image = new ImageView(Pokemon.getPicture(url));
    title = cardTitle(Pokemon.getName(url), fontSize);

    setCardStyle(card, color, width, height);
    card.add(title, 0, 0);
    card.add(image, 0, 1);

    return card;
  }
}
