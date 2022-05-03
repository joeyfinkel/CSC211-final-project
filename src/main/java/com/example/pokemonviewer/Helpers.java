package com.example.pokemonviewer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public final class Helpers {

  private Helpers() {
  }

  public static String capitalizeFirstLetter(String word) {
    return word.substring(0, 1).toUpperCase() + word.substring(1);
  }

  public static String prettyPrintJSON(String json) {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    return gson.toJson(JsonParser.parseString(json));
  }
}
