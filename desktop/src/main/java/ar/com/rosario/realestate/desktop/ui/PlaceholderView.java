package ar.com.rosario.realestate.desktop.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class PlaceholderView {

    private PlaceholderView() {}

    public static Parent build(String screenName, String detail) {
        Label title = new Label(screenName);
        title.getStyleClass().add("title-2");

        Label msg = new Label(detail);
        msg.getStyleClass().add("text-muted");

        VBox root = new VBox(12, title, msg);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
        return root;
    }
}
