package com.example.pokemonviewer;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class Helpers {

  private Helpers() {
  }

  public static @NotNull String capitalizeWord(@NotNull String word) {
    return word.substring(0, 1).toUpperCase() + word.substring(1);
  }

  @Contract(pure = true)
  public static @NotNull String capitalizeWords(String @NotNull [] words) {
    String returnVal = "";

    for (String word : words)
      returnVal += capitalizeWord(word) + " ";

    return returnVal.trim();
  }
}
