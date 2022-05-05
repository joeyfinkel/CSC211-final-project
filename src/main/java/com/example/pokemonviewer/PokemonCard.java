package com.example.pokemonviewer;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.io.IOException;

import static com.example.pokemonviewer.Pokemon.getId;

public final class PokemonCard {
  GridPane card;

  public PokemonCard(String url) throws IOException {
    this.card = Card.getInstance().create(url);
    Pokemon.setId(getId(url));
  }

  public PokemonCard(String url, int fontSize, String color, double width, double height) throws IOException {
    this.card = Card.getInstance().create(url, fontSize, color, width, height);
    Pokemon.setId(getId(url));
  }

  public GridPane getCard() {
    return card;
  }

  /**
   * The on hover event handler for the {@link PokemonCard}.
   * @return this so method can be chained.
   */
  public PokemonCard onHover() {
    card.setOnMouseEntered(e -> card.setCursor(Cursor.HAND));
    return this;
  }

  /**
   * The onClick event handler for the {@link PokemonCard}.
   * @param value The callback which is the event itself.
   * @return this so method can be chained.
   */
  public PokemonCard onClick(EventHandler<? super MouseEvent> value) {
    card.setOnMouseClicked(value);
    return this;
  }
}
