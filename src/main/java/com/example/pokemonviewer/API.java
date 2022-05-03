package com.example.pokemonviewer;

import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

public final class API {
  public static final String BASE_URL = "https://pokeapi.co/api/v2/";

  private API() {
  }

  /**
   * Queries the {@link API#BASE_URL} with a search option.
   * @param searchParam The search option to add onto {@link API#BASE_URL}.
   * @return A {@link JSONObject} of the queried URL.
   * @throws IOException
   */
  public static @NotNull JSONObject query(String searchParam) throws IOException {
    return new JSONObject(IOUtils.toString(new URL(BASE_URL + searchParam), Charset.forName("UTF-8")));
  }

  /**
   * Queries the {@link API#BASE_URL} with a search option and id. The id corresponds to the Pokémon.
   * @param searchParam The search option to add onto {@link API#BASE_URL}.
   * @param id The specific Pokemon to search for.
   * @return  A {@link JSONObject} of the queried URL.
   * @throws IOException
   */
  public static @NotNull JSONObject query(String searchParam, String id) throws IOException {
    return new JSONObject(IOUtils.toString(new URL(BASE_URL + searchParam + id), Charset.forName("UTF-8")));
  }

  /**
   * Queries a specific URL.
   * @param url The url to query.
   * @return  A {@link JSONObject} of the queried URL.
   * @throws IOException
   */
  @Contract("_ -> new")
  public static @NotNull JSONObject queryURL(String url) throws IOException {
    return new JSONObject(IOUtils.toString(new URL(url), Charset.forName("UTF-8")));
  }
}
