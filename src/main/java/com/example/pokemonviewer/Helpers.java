package com.example.pokemonviewer;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class Helpers {

  private Helpers() {
  }

  /**
   * Capitalizes the first letter in a word.
   * @param word The word to capitalize.
   * @return The capitalized word.
   */
  public static @NotNull String capitalizeWord(@NotNull String word) {
    return word.substring(0, 1).toUpperCase() + word.substring(1);
  }

  /**
   * Capitalizes multiple words.
   * @param words The words to capitalize.
   * @return All the words capitalized.
   */
  @Contract(pure = true)
  public static @NotNull String capitalizeWords(String @NotNull [] words) {
    StringBuilder returnVal = new StringBuilder();

    for (String word : words)
      returnVal.append(capitalizeWord(word)).append(" ");

    return returnVal.toString().trim();
  }
}
