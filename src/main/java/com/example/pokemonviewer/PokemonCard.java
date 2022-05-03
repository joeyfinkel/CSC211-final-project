package com.example.pokemonviewer;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.io.IOException;

import static com.example.pokemonviewer.Pokemon.pokemonId;

public final class PokemonCard {
  GridPane card;

  public PokemonCard(String url) throws IOException {
    this.card = Card.getInstance().create(url);
    Pokemon.setId(pokemonId(url));
  }

  public PokemonCard(String url, int fontSize, double width, double height) throws IOException {
    this.card = Card.getInstance().create(url, fontSize, width, height);
    System.out.println(url);
    Pokemon.setId(pokemonId(url));
  }

  public GridPane getCard() {
    return card;
  }

  public PokemonCard onHover() {
    card.setOnMouseEntered(e -> card.setCursor(Cursor.HAND));
    return this;
  }

  public PokemonCard onClick(EventHandler<? super MouseEvent> value) {
    card.setOnMouseClicked(value);
    return this;
  }
}
