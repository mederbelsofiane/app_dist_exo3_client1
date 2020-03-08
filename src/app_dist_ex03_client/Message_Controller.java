package app_dist_ex03_client;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class Message_Controller implements Initializable {

    @FXML
    public HBox hbox;
        @FXML public Circle circle;

    private String ColorReceive = "#FFFFFF";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void setText(Text t, String background_color) {
        TextFlow text = new TextFlow();
        text.setStyle("-fx-background-color: " + background_color + "; -fx-background-radius: 10;-fx-padding : 8");
        text.getChildren().add(t);
        text.setMaxWidth(200);
        if (background_color.equals(this.ColorReceive)) {
            hbox.getChildren().add(1, text);
             circle.setFill(Color.RED);
        } else {
            hbox.getChildren().add(0, text);
        }
    }

    public void setImage(ImageView image, String background_color) {
        StackPane stack = new StackPane();
        stack.setStyle("-fx-background-color: " + background_color + "; -fx-background-radius: 10;-fx-padding : 2");
        stack.getChildren().add(image);
        if (background_color.equals(this.ColorReceive)) {
            hbox.getChildren().add(1, stack);
             circle.setFill(Color.RED);
        } else {
            hbox.getChildren().add(0, stack);
        }
    }
    public void setFile(Button btn, String background_color) {
        StackPane stack = new StackPane();
        stack.setStyle("-fx-background-color: " + background_color + "; -fx-background-radius: 10;-fx-padding : 4");
        stack.getChildren().add(btn);
        if (background_color.equals(this.ColorReceive)) {
            hbox.getChildren().add(1, stack);
             circle.setFill(Color.RED);
        } else {
            hbox.getChildren().add(0, stack);
        }
    }

}
