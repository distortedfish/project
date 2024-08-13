import java.util.*;
import java.net.*;

import javafx.scene.control.*;
import org.json.simple.*;
import org.json.simple.parser.*;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import javafx.geometry.HPos;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Background;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.AnchorPane;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;

public class wordgame extends Application {
    private static String base = "https://random-word-api.herokuapp.com/word?number=";
    private static VBox box = new VBox();
    private static HBox text = new HBox();
    private static Label lcount = new Label("Words left: ");
    private static Label tcount = new Label("Guesses: 0");
    private static Label hcount = new Label("Best Score: ");
    private static int high;
    private static int left;
    private static int total;
    @Override
    public void start(Stage primaryStage) {
        Scene scene;
        // vbox
        box.setAlignment(Pos.TOP_CENTER);

        // title
        Label title = new Label("title");
        VBox.setMargin(title, new Insets(20, 20, 20, 20));
        title.setFont(new Font(40));

        // settings
        HBox settings = new HBox();
        settings.setSpacing(20);
        settings.setAlignment(Pos.CENTER);
        Label difficulty = new Label("Difficulty: ");
        Button one = new Button("1");
        Button two = new Button("2");
        Button three = new Button("3");
        one.setOnAction(e -> {
            try {
                text.getChildren().clear();
                total = 0;
                game(1);
            } catch (Exception ex) {
                System.out.println("error");
            }
        });
        two.setOnAction(e -> {
            try {
                text.getChildren().clear();
                total = 0;
                game(2);
            } catch (Exception ex) {
                System.out.println("error");
            }
        });
        three.setOnAction(e -> {
            try {
                text.getChildren().clear();
                total = 0;
                game(3);
            } catch (Exception ex) {
                System.out.println("error");
            }
        });
        settings.getChildren().addAll(difficulty, one, two, three, hcount, lcount, tcount);

        // text
        text.setAlignment(Pos.CENTER);
        text.setStyle("-fx-spacing: -0.5em;");
//        text.setMaxWidth(200);
        box.getChildren().addAll(title, settings, text);

        // set stage
        scene = new Scene(box);
        primaryStage.setTitle("game");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public static void game(int diff) throws IOException {
        int words = (int) Math.pow(10, diff);
        left = words / 10;
        lcount.setText("Words left: " + left);
        total = 0;
        tcount.setText("Guesses: " + 0);
        String str = get(words);
        JSONParser parser = new JSONParser();
        try {
            JSONArray obj = (JSONArray) parser.parse(str);
            Set<Integer> set = new HashSet<>();
            while (set.size() < words / 10) {
                set.add((int) (Math.random() * words));
            }
            for (int i = 0; i < words; i++) {
                String s = String.valueOf(obj.get(i));
                Font font = Font.font("Times New Roman", 20);
                if (set.contains(i)) {
                    int index = (int) (Math.random() * s.length());
                    char ch = (char) (Math.random() * 26 + 97);
                    s = s.substring(0, index) + ch + s.substring(index + 1);
                    Hyperlink correct = new Hyperlink(s);
                    correct.setStyle("-fx-text-fill: black");
                    correct.setFont(font);
                    correct.setOnAction(e -> {
                        if (correct.getStyle().equals("-fx-text-fill: black")) {
                            correct.setStyle("-fx-text-fill: green");
                            left--;
                            lcount.setText("Words left: " + left);
                            total++;
                            tcount.setText("Guesses: " + total);
                            if (left == 0) {
                                if (total < high || high == 0) {
                                    high = total;
                                    hcount.setText("Best Score: " + high);
                                }
                                text.getChildren().clear();
                                showAlert();
                                return;
                            }
                        }
                    });
                    text.getChildren().add(correct);
                } else {
                    Hyperlink wrong = new Hyperlink(s);
                    wrong.setStyle("-fx-text-fill: black");
                    wrong.setFont(font);
                    wrong.setOnAction(e -> {
                        if (wrong.getStyle().equals("-fx-text-fill: black")) {
                            wrong.setStyle("-fx-text-fill: red");
                            total++;
                            tcount.setText("Guesses: " + total);
                        }
                    });
                    text.getChildren().add(wrong);
                }


//                Label space = new Label(" ");
//                text.getChildren().add(space);
            }
        } catch (ParseException ex) {
            System.out.println("error");
        }
    }

    public static void showAlert() {
        Alert alert = new Alert(Alert.AlertType.NONE, "Best score: " + high, ButtonType.OK);
        alert.setTitle("You Won!");
        alert.setHeaderText("Done in " + total + " guesses");
        alert.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static String get(int num) throws IOException {
        StringBuilder content = new StringBuilder();
        try {
            URL url = new URL(base + num);
            URLConnection connection = url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null)
            {
                content.append(line + "\n");
            }
            bufferedReader.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return content.toString();
    }
}
