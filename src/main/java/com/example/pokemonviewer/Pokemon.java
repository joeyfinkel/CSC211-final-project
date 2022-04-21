package com.example.pokemonviewer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

public class Pokemon {
  public static int selectedCategoryIdx;
  static ArrayList<String> categoryNames = new ArrayList<>();

  private final static Pokemon instance = new Pokemon();

  public static Pokemon getInstance() {
    return instance;
  }

  private static JSONArray pokemonTypes() throws IOException {
    return API.query("type", "results");
  }

  public static JSONArray specificPokemonType(int type) throws IOException {
    return API.query("type/1", "pokemon");
  }

  public static String getPokemonName(String url) throws IOException {
    JSONObject query = API.query(url);

    return query.getString("name");
  }

  public ArrayList<String> getCategoryNames() {
    try {
      JSONArray results = pokemonTypes();

      for (int i = 0; i < results.length(); i++) {
        String name = (String) results.getJSONObject(i).get("name");

        categoryNames.add(Helpers.capitalizeFirstLetter(name));
      }
    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return categoryNames;
  }

  public int getSelectedCategoryIdx() {
    return selectedCategoryIdx;
  }

  public void setSelectedCategoryIdx(int selectedCategoryIdx) {
    Pokemon.selectedCategoryIdx = selectedCategoryIdx;
  }

  public static String selectedCategory() throws IOException {
    String selectedCategory = null;
    JSONArray types = pokemonTypes();

    for (int i = 0; i < types.length(); i++) {
      if (selectedCategoryIdx == i) {
        String category = (String) types.getJSONObject(i).get("name");
        selectedCategory = Helpers.capitalizeFirstLetter(category);
      }
    }

    return selectedCategory;
  }
}
