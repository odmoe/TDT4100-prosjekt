package tdt4100_gg_prosjekt;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class BlackJackAppController {
    
    @FXML
    private AnchorPane logInPage, startPage, gamePage, finishPage, betPane, playerDeskPage;
    @FXML
    private GridPane playerCardPane;
    @FXML 
    private Button newGameButton;

    @FXML
    private Text winText, lossText, dealerBustText, playerBustText, drawText, blackJackText;

    @FXML
    private Label currentBetDisplayer, totalAmountDisplayer;
    @FXML
    private Button eraseBetButton, startGamblingButton, tenButton, fiveButton, continueButton;

    @FXML
    private Label dealerCount, playerCount; 
    @FXML
    private Button hitButton, standButton, allInButton, doubleButton, logInButton, newUserButton;
 
    @FXML
    private ImageView card1, card2, card3, card4, card5, card6, card7, card8, card9, card10,
    dealerCard1, dealerCard2, dealerCard3, dealerCard4, dealerCard5, dealerCard6, dealerCard7, dealerCard8, dealerCard9, dealerCard10,
    dealerMustHit;

    @FXML
    private TextField userNameField, passWordField;


    GameLogic gameLogic;
    String[] dealerHandAsStringArray;
    String[] playerHandAsStringArray;
    //singleton
    FileHandler fileHandler = FileHandler.getInstance();

    //disse brukes som slotsene i GUIen som skal fylles med kort-bilderr
    List<ImageView> imageViewListOfAllPlayerCards = new ArrayList<>();
    List<ImageView> imageViewListOfAllDealerCards = new ArrayList<>();


    /*de følgende feltene blir brukt på denne måten
        - currentUserName er det nåværende angivelige brukernavnet
        - currentPassWord er det nåværende angivelige passordet
        - currentPlayer er en godkjent spiller og settes kun når currentUserName og currentPassWord er validert
    */
    String currentUserName;
    String currentPassWord;
    String currentPlayer;
    Double currentBalance;


    @FXML
    //observatør - observert
    public void initialize(){
        passWordField.textProperty().addListener((observable, oldValue, newValue) -> {
            currentPassWord = newValue;
            buttonEnabler();
        });
        userNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            currentUserName = newValue;
            buttonEnabler();
        });
    }




    //hjelpemetode som genererer riktig bilde til kort som brukes som argument
    private Image generateImageFromCardString(String s){
        Image toReturn = new Image(new File("src/main/resources/tdt4100_gg_prosjekt/Carddeck/" + s + ".png").toURI().toString());
        return toReturn;
    }

    //hjelpemetode for å kommunisere feilmeldinger til brukeren.
    private void popUpForErrors(Exception e) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        VBox dialogVbox = new VBox(20);
        dialogVbox.getChildren().add(new Text(e.getMessage()));
        Scene dialogScene = new Scene(dialogVbox, 250, 100);
        dialog.setScene(dialogScene);
        dialog.show();
    }
    //overload for strings
    private void popUpForErrors(String s) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        VBox dialogVbox = new VBox(20);
        dialogVbox.getChildren().add(new Text(s));
        Scene dialogScene = new Scene(dialogVbox, 250, 100);
        dialog.setScene(dialogScene);
        dialog.show();
    } 
    //hjelpemetode for å sjekke om et bilde er tomt
    private boolean isEmpty(ImageView imageView) {
        Image image = imageView.getImage();
        return image == null || image.isError();
    }       


    /*her kan det se ut som det blir mye logikk, men pga mye interaksjon med
    FXML-objekt er det å foretrekke å ha det i kontrolleren fremfor BackEnd.
    */

    public void onNewUserButton(){
        try {
            if (!fileHandler.doesUserNameAlreadyExist(currentUserName)) {

                //denne inneholder validering og thrower ved "ulovlig" navn.
                fileHandler.newUserToFile(currentUserName,currentPassWord);
                currentPlayer = currentUserName;
                currentBalance = 100.0;
                logInPage.setVisible(false);
                startPage.setVisible(true);
            }else {
                popUpForErrors("User already exists");
            }
            //dette skal egentlig ikke skje, men for sikkerhets skyld
        } catch (Exception e) {
            popUpForErrors(e);
        }
    }

    public void onLogInButton(){
        try {
            if (!fileHandler.doesUserNameAlreadyExist(currentUserName)) {
                popUpForErrors("User not found");
            } else if (!fileHandler.checkUser(currentUserName, currentPassWord)){
                popUpForErrors("Wrong password");
            }else {
                //dette er det som faktisk skal skje ved riktig input
                currentPlayer = currentUserName;
                currentBalance = Double.parseDouble(fileHandler.getUsersFromFile("Balance").get(currentUserName));
                logInPage.setVisible(false);
                startPage.setVisible(true);
            }
        } catch (Exception e) {
            popUpForErrors(e);
        }


    }




    //hjelpemetode for å gjøre login- og registreringsknappen synlig når passord og brukernavn er fylt ut
    private void buttonEnabler(){
        boolean disable = !(currentPassWord != null && currentUserName != null && !currentPassWord.isBlank() && !currentUserName.isBlank());
        logInButton.setDisable(disable);
        newUserButton.setDisable(disable);
    }


    

    public void onNewGameButton() {
        gameLogic = GameLogic.getInstance();
        this.newGame();
    }


    public void newGame(){
        try {
            finishPage.setVisible(false);
            startPage.setVisible(false);
            gamePage.setVisible(true);
            dealerMustHit.setVisible(true);
            if (currentBalance != 0) {
                gameLogic.cleanUpAfterGame(currentBalance);
            } else {
                gameLogic.cleanUpAfterGame(100);
            }
            totalAmountDisplayer.setText(gameLogic.getPlayerBalance()+"");
            currentBetDisplayer.setText(gameLogic.getPlayerCurrentBet()+"");
        } catch (Exception e) {
            popUpForErrors(e);
        }
    }

    public void onFiveButton() {
        try {
            gameLogic.addBet(5);
            totalAmountDisplayer.setText(gameLogic.getPlayerBalance()+"");
            currentBetDisplayer.setText(gameLogic.getPlayerCurrentBet()+"");
        } catch (Exception e) {
            this.popUpForErrors("Can't bet more, balance is zero");
        }
    }
    public void onAllInButton() {
        try {
            gameLogic.addBet(gameLogic.getPlayerBalance());
            totalAmountDisplayer.setText(gameLogic.getPlayerBalance()+"");
            currentBetDisplayer.setText(gameLogic.getPlayerCurrentBet()+"");
        } catch (Exception e) {
            this.popUpForErrors("Can't go all in when balance is zero");
        }
    }


    public void onTenButton() {
        try {
            gameLogic.addBet(10);
            totalAmountDisplayer.setText(gameLogic.getPlayerBalance()+"");
            currentBetDisplayer.setText(gameLogic.getPlayerCurrentBet()+"");
        } catch (Exception e) {
            popUpForErrors("Can't bet more, balance is zero");
        }
    }



    public void onEraseBetButton() {
        try {
            gameLogic.eraseBet();
            currentBetDisplayer.setText(gameLogic.getPlayerCurrentBet()+"");
            totalAmountDisplayer.setText(gameLogic.getPlayerBalance()+"");
        } catch (Exception e) {
            popUpForErrors(e);
        }
    }

    public void onStartGamblingButton () {
        try {
            gameLogic.hitPlayer();
            gameLogic.hitPlayer();

            gamePage.setVisible(false);
            playerDeskPage.setVisible(true);
            lossText.setVisible(false);
            winText.setVisible(false);
            drawText.setVisible(false);
            blackJackText.setVisible(false);
            dealerBustText.setVisible(false);
            playerBustText.setVisible(false);
            continueButton.setVisible(false);
            hitButton.setVisible(true);
            standButton.setVisible(true);
            doubleButton.setVisible(true);

        

            //nr 2 kommer lengre ned
            gameLogic.hitDealer();

            this.playerHandAsStringArray = gameLogic.getCurrentPlayerHandAsStringArray();
            this.dealerHandAsStringArray = gameLogic.getCurrentDealerHandAsStringArray();

            card1.setImage(this.generateImageFromCardString(this.playerHandAsStringArray[0]));
            card2.setImage(this.generateImageFromCardString(this.playerHandAsStringArray[1]));
            dealerCard1.setImage(this.generateImageFromCardString(this.dealerHandAsStringArray[0]));

            dealerCount.setText(gameLogic.getCurrentDealerHandCount());
            playerCount.setText(gameLogic.getCurrentPlayerHandCount());


            gameLogic.hitDealer(); //denne kommer til slutt så ikke det siste kortet til dealeren "avsløres" i counten

            if (gameLogic.getDoesPlayerHaveBlackJack()) {
                this.onStandButton();
            }
        } catch (Exception e) {
            if (e.getMessage().equals("Can't hit when bet is zero")) {
                popUpForErrors("Can't gamble without betting");
            }else {
            popUpForErrors(e);
            }
        }
        
    }

    public void onHitButton(){
        //spilleren får nytt kort
        gameLogic.hitPlayer();
        // vi henter ut den oppdaterte hånden
        this.playerHandAsStringArray = gameLogic.getCurrentPlayerHandAsStringArray();
        // vi finner riktig kort å displaye
        Image cardImageToDisplay = this.generateImageFromCardString(this.playerHandAsStringArray[playerHandAsStringArray.length-1]);
        //vi definerer en liste med alle "slotsene" for å displaye bildene.
        //for å unngå at slotsene ble "null" måtte disse addes manuelt
        imageViewListOfAllPlayerCards.addAll(Arrays.asList(card1, card2, card3, card4, card5, card6, card7, card8, card9, card10));

        //vi finner riktig slot, merk at "orElse(null)" ikke kan oppstå
        ImageView correctSlot = imageViewListOfAllPlayerCards.stream().filter(p -> isEmpty(p)).findFirst().orElse(null);

        //til slutt setter vi denne sloten til riktig cardImage
        correctSlot.setImage(cardImageToDisplay);

        //NB NB NB ::det meste i denne metoden er nødvendig logikk i kontrolleren fordi vi ikke ønsker å fikle med GUI-objekt i BackEnd.

        playerCount.setText(gameLogic.getCurrentPlayerHandCount());

        if (gameLogic.getHasPlayerBusted()) {
            this.whenFinish();
        }
    }

    public void onDoubleButton(){
        try {
            gameLogic.addBet(gameLogic.getPlayerCurrentBet());
            onHitButton();
            onStandButton();
        } catch (Exception e) {
            popUpForErrors("Can't double when balance is zero");
        }
    }


    public void onStandButton() {
        //overfør turen til dealeren
        if (gameLogic.getDoesDealerHaveBlackJack()) {
            dealerCard2.setImage(this.generateImageFromCardString(this.dealerHandAsStringArray[1]));
        }else if ( ! gameLogic.getDoesPlayerHaveBlackJack()) {
            
            gameLogic.dealerTurn();
            dealerHandAsStringArray = gameLogic.getCurrentDealerHandAsStringArray();

            //disse må legges til her og ikke i deklareringen, ellers blir de satt til null
            imageViewListOfAllDealerCards.addAll(Arrays.asList(dealerCard1, dealerCard2, dealerCard3, dealerCard4, dealerCard5, dealerCard6, dealerCard7, dealerCard8, dealerCard9, dealerCard10));
            try {
                for (int i = 1; i < dealerHandAsStringArray.length; i++) {
                    imageViewListOfAllDealerCards.get(i).setImage(this.generateImageFromCardString(dealerHandAsStringArray[i]));
                }
            } catch (Exception e) {
            }
        }

        this.dealerCount.setText(gameLogic.getCurrentDealerHandCount());


        this.whenFinish();

    }

    public void whenFinish(){
        hitButton.setVisible(false);
        standButton.setVisible(false);
        doubleButton.setVisible(false);
        continueButton.setVisible(true);
        gameLogic.upDateWinnerAsInt();

        //her gir vi spilleren info om hvem som vant
        if (gameLogic.getWinnerAsInt() == 1) {
            if (gameLogic.getHasDealerBusted()) {
                dealerBustText.setVisible(true);
            } else if (gameLogic.getDoesPlayerHaveBlackJack()) {
                //håndter blackjack
                blackJackText.setVisible(true);
            } else {
                winText.setVisible(true);
            }
            
        } else if (gameLogic.getWinnerAsInt() == 0) {
            drawText.setVisible(true);
        } else {
            if (gameLogic.getHasPlayerBusted()) {
                playerBustText.setVisible(true);
            } else {
                lossText.setVisible(true);
            }
        }
    }


    public void onContinueButton(){
        gameLogic.newBettingRound();

        imageViewListOfAllPlayerCards.forEach(i -> i.setImage(null));
        imageViewListOfAllDealerCards.forEach(i -> i.setImage(null));

        if (gameLogic.getIsPlayerOutOfMoney()) {
            playerDeskPage.setVisible(false);
            finishPage.setVisible(true);

            /*her "lurer" vi programmet. CurrentBalance er kun en hjelpeverdi i kontrolleren,
            og ikke alltid den faktiske tilstanden i spillogikken*/
            currentBalance = 100.0;
        }else {
            playerDeskPage.setVisible(false);
            gamePage.setVisible(true);
            currentBetDisplayer.setText(gameLogic.getPlayerCurrentBet()+"");
            totalAmountDisplayer.setText(gameLogic.getPlayerBalance()+"");
        }

        try {
            fileHandler.newBalance(currentPlayer, gameLogic.getPlayerBalance()+"");
        } 
        catch (Exception e) {
            popUpForErrors(e);
        }
        
    }

    

}
