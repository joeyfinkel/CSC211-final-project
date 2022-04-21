package com.example.pokemonviewer;

public final class Helpers {

  private Helpers() {
  }

  public static String capitalizeFirstLetter(String word) {
    return word.substring(0, 1).toUpperCase() + word.substring(1);
  }
}
