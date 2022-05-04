package com.example.pokemonviewer;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

public class Pokemon {
  public static String id;
  public static String selectedCategoryUrl;
  public static HashMap<String, String> selectedCategory;

  private final static Pokemon instance = new Pokemon();

  public static String getId() {
    return id;
  }

  public static void setId(String id) {
    Pokemon.id = id;
  }

  public static String getSelectedCategoryUrl() {
    return selectedCategoryUrl;
  }

  public void setSelectedCategoryUrl(String url) {
    Pokemon.selectedCategoryUrl = url;
  }

  public static HashMap<String, String> getSelectedCategory() {
    return selectedCategory;
  }

  public static void setSelectedCategory(HashMap<String, String> selectedCategory) {
    Pokemon.selectedCategory = selectedCategory;
  }

  public static Pokemon getInstance() {
    return instance;
  }

  public static @NotNull String pokemonId(@NotNull String url) {
    String endBaseLink = url.substring(API.BASE_URL.length(), url.length() - 1);
    String id = endBaseLink.replace("pokemon/", "");

    return id;
  }

  /**
   * Queries the API to get all the Pokémon categories.
   *
   * @return An array with all the Pokémon categories.
   * @throws IOException
   */
  public static @NotNull LinkedHashMap<String, String> getTypes() throws IOException {
    LinkedHashMap<String, String> categories = new LinkedHashMap<>();
    JSONArray types = API.query("type").getJSONArray("results");

    for (int i = 0; i < types.length(); i++) {
      String name = (String) types.getJSONObject(i).get("name");
      String url = (String) types.getJSONObject(i).get("url");

      categories.put(Helpers.capitalizeWord(name), url);
    }

    return categories;
  }

  /**
   * Gets the Pokémon category specified.
   *
   * @param type The index of the selected category from the dropdown.
   * @return An array with the Pokémon of the {@code type} specified.
   * @throws IOException
   */
  public static JSONArray getTypes(String type) throws IOException {
    return API.query("type/", type).getJSONArray("pokemon");
  }

  /**
   * Gets the links for every Pokémon in the {@code selectedCategory}.
   *
   * @param url The selected category from the dropdown.
   * @return A list with all the Pokémon categories.
   * @throws IOException
   */
  public static @NotNull List<String> urls(String url) throws IOException {
    List<String> urls = new ArrayList<>();
    JSONArray types = API.queryURL(url).getJSONArray("pokemon");

    // Iterate over every Pokémon in the category
    for (int i = 0; i < types.length(); i++) {
      JSONObject pokemon = (JSONObject) types.getJSONObject(i).get("pokemon");

      urls.add(i, pokemon.getString("url"));
    }

    return urls;
  }

  /**
   * Gets the names of the Pokémon from the url belonging to the Pokémon.
   *
   * @param url The url of the Pokémon to get the name for.
   * @return The name of the Pokémon from the url.
   * @throws IOException
   */
  public static @NotNull String getName(String url) throws IOException {
    return Helpers.capitalizeWord(API.queryURL(url).getString("name"));
  }

  public static String getSpeciesUrl(String url) throws IOException {
    JSONObject speciesObj = API.queryURL(url).getJSONObject("species");
    return speciesObj.getString("url");
  }

  public static String getEvolutionChainUrl(String url) throws IOException {
    JSONObject evolutionChain = API.queryURL(url).getJSONObject("evolution_chain");
    return evolutionChain.getString("url");
  }

  public static @NotNull ArrayList<JSONObject> getNextEvolution(@NotNull JSONObject obj) {
    JSONArray evolvesTo = obj.getJSONArray("evolves_to");
    ArrayList<JSONObject> evolutions = new ArrayList<>();

    for (int i = 0; i < evolvesTo.length(); i++) {
      evolutions.add(evolvesTo.getJSONObject(i));
    }
    return evolutions;
  }

  public static @NotNull ArrayList<String> evolvesTo(String evolutionUrl) throws IOException {
    JSONObject chain = API.queryURL(evolutionUrl).getJSONObject("chain");
    ArrayList<JSONObject> evolvesToObj = getNextEvolution(chain);
    ArrayList<String> evolutions = new ArrayList<>();

    evolvesToObj.forEach(element -> {
      String firstEvolutionName = element.getJSONObject("species").getString("name");
      JSONArray nextEvolutionArr = element.getJSONArray("evolves_to");

      evolutions.add(firstEvolutionName);

      if (nextEvolutionArr.length() > 0) {
        JSONObject nextEvolutionObj = nextEvolutionArr.getJSONObject(0);
        String nextEvolutionName = nextEvolutionObj.getJSONObject("species").getString("name");
        evolutions.add(nextEvolutionName);
      }

    });

    return evolutions;
  }

  public static @NotNull ArrayList<String> getEvolutionNames(String url, String currName) throws IOException {
    ArrayList<String> evolutions = evolvesTo(url);

    evolutions.add(0, currName);
    return evolutions;
  }

  public static String getPicture(String url) throws IOException {
    JSONObject images = (JSONObject) API.queryURL(url).get("sprites");
    return images.getString("front_default");
  }

  @Contract("_ -> new")
  public static @NotNull HashMap<String, String> getPokedexData(String link) throws IOException {
    return new HashMap<>() {
      {
        put("number", API.queryURL(link).get("id").toString());
        put("type", Helpers.capitalizeWord(API.queryURL(String.valueOf(getSelectedCategory().values())
            .replace("[", "").replace("]", "")).get("name").toString()));
        put("height", API.queryURL(link).get("height").toString());
        put("weight", API.queryURL(link).get("weight").toString());
      }
    };
  }

  public static @NotNull HashMap<String, String> getBaseStats(String link) throws IOException {
    JSONArray baseStatObj = API.queryURL(link).getJSONArray("stats");

    return new HashMap<>() {
      {
        for (int i = 0; i < baseStatObj.length(); i++) {
          JSONObject statObj = (JSONObject) baseStatObj.get(i);
          JSONObject statNameObj = (JSONObject) statObj.get("stat");
          String stat = statNameObj.get("name").toString();

          if (stat.contains("-")) {
            String first = stat.substring(0, stat.indexOf("-"));
            String second = stat.substring(stat.indexOf("-") + 1);

            put(Helpers.capitalizeWords(new String[]{first, second}), statObj.get("base_stat").toString());
          } else if (stat.equalsIgnoreCase("hp")) {
            put(stat.toUpperCase(), statObj.get("base_stat").toString());
          } else put(stat, statObj.get("base_stat").toString());

        }
      }
    };
  }

  private static @NotNull HashMap<String, String> getAbilitiesObj(String url) throws IOException {
    JSONArray abilitiesObj = API.queryURL(url).getJSONArray("abilities");

    return new HashMap<>() {
      {
        for (int i = 0; i < abilitiesObj.length(); i++) {
          JSONObject entry = abilitiesObj.getJSONObject(i).getJSONObject("ability");
          String abilityName = entry.get("name").toString();
          String abilityUrl = entry.get("url").toString();

          put(abilityName, abilityUrl);
        }
      }
    };
  }

  public static @NotNull HashMap<String, String> getAbilities(String url) throws IOException {
    return new HashMap<>() {{
      getAbilitiesObj(url).forEach((key, value) -> {
        try {
          JSONArray flavors = API.queryURL(value).getJSONArray("flavor_text_entries");

          for (int i = 0; i < flavors.length(); i++) {
            JSONObject flavorObj = flavors.getJSONObject(i);
            JSONObject languageObj = flavorObj.getJSONObject("language");
            String language = languageObj.getString("name");
            String flavorText = flavorObj.getString("flavor_text");

            if (language.equals("en")) {
              if (key.contains("-")) {
                String first = key.substring(0, key.indexOf("-"));
                String second = key.substring(key.indexOf("-") + 1);

                put(Helpers.capitalizeWords(new String[]{first, second}), flavorText);
              } else
                put(Helpers.capitalizeWord(key), flavorText);
            }
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      });
    }};
  }
}
