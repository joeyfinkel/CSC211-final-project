module com.example.pokemonviewer {
  requires javafx.controls;
  requires javafx.fxml;

  requires org.controlsfx.controls;
  requires org.kordamp.bootstrapfx.core;
  requires json;
  requires gson;
  requires org.jetbrains.annotations;
  requires org.apache.commons.io;

  opens com.example.pokemonviewer to javafx.fxml;
  exports com.example.pokemonviewer;
}