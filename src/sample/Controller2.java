package sample;

import javafx.event.ActionEvent;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

//controller for main game screen
public class Controller2 {
    @FXML
    private TextField guessInput;
    @FXML
    private Button enterInput;
    @FXML
    private Label lettersGuessed;
    @FXML
    private Label wordDisplay;
    @FXML
    private Label wordValue;
    @FXML
    private Label winLabel;
    @FXML
    private ImageView hangImage;
    @FXML
    private Label statusLabel;
    @FXML
    private Pane playAgainPane;


    private Stage stage;
    private Scene scene;
    private Parent root;
    private String theWordValue; //the original word
    private boolean win = false;
    private int chances = 7;
    boolean gameOver = false;
    ArrayList<Character> oldValues = new ArrayList<Character>();
    ArrayList<String> guessed = new ArrayList<String>();

    //set undercases display
    public void setWordDisplay(String letters){
        wordDisplay.setText(letters);
    }

    //set original word
    public void setWordValue(String word){
        wordValue.setText(word);
    }

    //when the enter button is pressed get the guessed word and check if it is in the original word
    public void makeGuess () throws IOException {
        statusLabel.setText("");
        theWordValue = wordValue.getText();
        String guess = guessInput.getText();
        if (guess == "") {
            //nothing was guessed
        } else if (guess.toLowerCase().equals(theWordValue.toLowerCase())){
            //the word was guessed correctly
            wordDisplay.setText(theWordValue);
            winLabel.setText("You win!");
            guessed.add(guess);
            gameOver = true;
        }
        else if (guessed.contains(guess)){
            //guess was already asked
            statusLabel.setText("Already guessed that");
        }
        else if (theWordValue.toLowerCase().contains(guess.toLowerCase())) {
            //guess is in word
            //rebuild undercase string but replace the letters that were guessed with the undercases
            boolean match;
            boolean oldMatch;
            char[] numOfLetters = new char[theWordValue.length()];
            for (int i=0; i<theWordValue.length(); i++){
                match = false;
                oldMatch = false;
                //check for match in guess with current index
                for (int k=0; k<guess.length(); k++){
                    if (guess.charAt(k) == theWordValue.charAt(i)){
                        match = true;
                    } else{

                    }
                }
                //check for already matched values
                for (int k=0; k<oldValues.size(); k++){
                    if (theWordValue.charAt(i) == oldValues.get(k)){
                        oldMatch = true;
                    }
                }
                //replace value if match is true
                if (match){
                    for (int j=0; j<guess.length(); j++){
                        if (guess.charAt(j) == theWordValue.charAt(i)){
                            numOfLetters[i] = guess.charAt(j);
                            oldValues.add(guess.charAt(j));
                        }
                    }
                }
                //if old value was already guessed and is correct replace it
                else if (oldMatch){
                    for (int k=0; k<oldValues.size(); k++){
                        if (oldValues.get(k) == theWordValue.charAt(i)){
                            numOfLetters[i] = oldValues.get(k);
                        }
                    }
                }
                //if there is a space, replace with |, else replace with _
                else if (theWordValue.charAt(i) == ' '){
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
            wordDisplay.setText(letters);

            lettersGuessed.setText(lettersGuessed.getText() + " " + guess);
            guessed.add(guess);
        } else {
            //guess is not in word
            chances--;
            lettersGuessed.setText(lettersGuessed.getText() + " " + guess);
            guessed.add(guess);
        }
        guessInput.setText("");
        checkWin();
        guessInput.requestFocus();
        if (gameOver){
            playAgainPane.setVisible(true);
        }
    }

    public void checkWin(){
        Image myImage = null;
        switch (chances){
            case 6:
                myImage = new Image(getClass().getResourceAsStream("img/1.png"));
                hangImage.setImage(myImage);
                break;
            case 5:
                myImage = new Image(getClass().getResourceAsStream("img/2.png"));
                hangImage.setImage(myImage);
                break;
            case 4:
                myImage = new Image(getClass().getResourceAsStream("img/3.png"));
                hangImage.setImage(myImage);
                break;
            case 3:
                myImage = new Image(getClass().getResourceAsStream("img/4.png"));
                hangImage.setImage(myImage);
                break;
            case 2:
                myImage = new Image(getClass().getResourceAsStream("img/5.png"));
                hangImage.setImage(myImage);
                break;
            case 1:
                myImage = new Image(getClass().getResourceAsStream("img/6.png"));
                hangImage.setImage(myImage);
                gameOver = true;
                winLabel.setTextFill(Color.RED);
                winLabel.setText("Game over");
                wordDisplay.setText("The word was: " + theWordValue);
                break;
        }
        //check if all the letters are filled
        int numCorrect = 0;
        String wordDisplay2 = wordDisplay.getText().replace("|"," ");
        if (gameOver == false){
            for (int i = 0, k = 0; i<wordDisplay2.length(); i=i+2, k++){
                if (wordDisplay2.charAt(i) == theWordValue.charAt(k)){
                    numCorrect++;
                }
            }
        }
        if (numCorrect == theWordValue.length()){
            gameOver = true;
            winLabel.setTextFill(Color.GREEN);
            winLabel.setText("You win!");
        }
    }

    //switch back to title scene
    public void playAgain() throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("title.fxml"));
        root = loader.load();
        stage = (Stage) enterInput.getScene().getWindow();
        scene = new Scene(root, 1000, 800);
        stage.setTitle("Hangman");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public void doNotPlayAgain(){
        Platform.exit();
        System.exit(0);
    }

    public void handle2(KeyEvent e) throws IOException {
        if (e.getCode() == KeyCode.ENTER){
            makeGuess();
        }
    }
}
