package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Controller1 {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private PasswordField wordText;
    @FXML
    private RadioButton rButton1, rButton2;
    @FXML
    private Button startButton;

    private String word;


    //get the word and change scene to the game scene and also display undercases for the word initially
    public void enterGame() throws IOException {
        wordText.requestFocus();
        word = wordText.getText();
        if (word == "" && rButton1.isSelected()){
            //if nothing is entered do nothing
        } else{
            if (rButton2.isSelected()){
                List<String> wordsList = readFile();
                int index = new java.util.Random().nextInt(wordsList.size());
                word = wordsList.get(index);
                System.out.println("the random word is: " + word);
            }
            FXMLLoader loader = new FXMLLoader(getClass().getResource("game.fxml"));
            root = loader.load();

            Controller2 controller2 = loader.getController();

            //display undercases for number of letters in word
            char[] numOfLetters = new char[word.length()];
            for (int i=0; i<word.length(); i++){
                if (word.charAt(i) == ' '){
                    numOfLetters[i] = '|';
                } else{
                    numOfLetters[i] = '_';
                }
            }

            StringBuilder builder = new StringBuilder();
            for (char value: numOfLetters){
                builder.append(value).append(" ");
            }
            String letters = builder.toString();
            controller2.setWordDisplay(letters);
            controller2.setWordValue(word);

            stage = (Stage) startButton.getScene().getWindow();
            scene = new Scene(root, 1000, 800);
            stage.setTitle("Hangman");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        }
    }


    //if radio button 2 is selected make text field uneditable
    public void changeWordText(){
        wordText.setEditable(false);
    }

    //if radio button 1 is selected again return text to be editable
    public void replaceWordText(){
        wordText.setEditable(true);
        wordText.requestFocus();
    }

    public List<String> readFile() throws IOException{
        BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/resources/words.txt")));
        String line = reader.readLine();
        List<String> words = new ArrayList<String>();
        while((line = reader.readLine()) != null) {
            words.add(line);
        }
        return words;
    }

    public void handle (KeyEvent e) throws IOException {
        if (e.getCode() == KeyCode.ENTER){
            enterGame();
        }
    }
}
