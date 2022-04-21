package com.example.pokemonviewer;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

public final class API {
  private API() {
}

  public static JSONObject query(String url) throws IOException {
    return new JSONObject(IOUtils.toString(new URL(url), Charset.forName("UTF-8")));
  }

  public static JSONArray query(String searchParam, String key) throws IOException {
    JSONObject pokemonTypes = new JSONObject(IOUtils.toString(new URL("https://pokeapi.co/api/v2/" + searchParam), Charset.forName("UTF-8")));
    JSONArray results = pokemonTypes.getJSONArray(key);

    return results;
  }
}
