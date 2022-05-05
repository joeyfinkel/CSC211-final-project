module com.example.pokemonviewer {
  requires javafx.controls;
  requires javafx.fxml;

  requires org.controlsfx.controls;
  requires org.kordamp.bootstrapfx.core;
  requires org.jetbrains.annotations;
  requires org.apache.commons.io;
  requires org.json;

  opens com.example.pokemonviewer to javafx.fxml;
  exports com.example.pokemonviewer;
}