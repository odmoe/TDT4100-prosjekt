package tdt4100_gg_prosjekt;


public class GameLogic {


    private Player player;
    private Dealer dealer;
    private CardDeck cardDeck;

    private boolean hasDealerBusted = false;
    private boolean hasPlayerBusted = false;
    private boolean doesPlayerHaveBlackJack = false;
    private boolean doesDealerHaveBlackJack = false;
    private boolean isPlayerOutOfMoney = false;
    private int winnerAsInt = 0;


    //her bruker vi et såkalt "singleton-pattern", for å sikre at
    //vi kun kan opprette étt objekt av typen GameLogic. Konstruktøren er derfor private
    
    private static GameLogic onlyInstance = new GameLogic();

    private GameLogic() throws IllegalArgumentException {
        this.cleanUpAfterGame();
    }

    public static GameLogic getInstance() throws IllegalArgumentException {
        return onlyInstance;
    }



    public void cleanUpAfterGame() {
        this.player = new Player(100);
        this.cardDeck = new CardDeck();
        this.dealer = new Dealer();

        //denne oppdaterer tilstander
        this.checkIsPlayerOutOfMoney();
        this.checkIfDealerHasBlackJack();
        this.checkIfPlayerHasBlackJack();
        this.checkIfPlayerHasBusted();
        this.checkIfDealerHasBusted();
        this.upDateWinnerAsInt();
    }

    //overloader for å både kunne starte med bestemt amount og uspesifisert (100)
    public void cleanUpAfterGame(double amount) {
        this.player = new Player(amount);
        this.cardDeck = new CardDeck();
        this.dealer = new Dealer();

        //denne oppdaterer tilstander
        this.checkIsPlayerOutOfMoney();
        this.checkIfDealerHasBlackJack();
        this.checkIfPlayerHasBlackJack();
        this.checkIfPlayerHasBusted();
        this.checkIfDealerHasBusted();
        this.upDateWinnerAsInt();
    }
    


    //få nåtidige hands og count til kontroller-bruk
    public String[] getCurrentPlayerHandAsStringArray() throws NullPointerException{
        return this.player.getCurrentHand();
    }
    //merk at "handCount" er håndens verdi
    public String getCurrentPlayerHandCount() {
        return Integer.toString(this.player.getHandValue());
    }

    public String[] getCurrentDealerHandAsStringArray() throws NullPointerException{
        return this.dealer.getCurrentHand();
    }
    //merk at "handCount" er håndens verdi
    public String getCurrentDealerHandCount(){
        return Integer.toString(this.dealer.getHandValue());
    }



    //delegerer nedover!
    public void addBet(double amount) throws IllegalArgumentException {
        this.player.withdraw(amount);
    }

    public void eraseBet() throws IllegalArgumentException {
        this.player.deposit(this.player.getCurrentBet());
        this.player.setCurrentBet(0);
    }


  

    public void hitPlayer() throws IllegalArgumentException {
        //når kortstokken (bestående av 6) går tom blir en ny en konstruert og anvendt helt sømløst
        //Vi foretrekker dette fremfor å ha en enda større en til å begynne med, da dette ville ha muliggjort veldig mange av samme kort i spill samtidig.
        if (cardDeck.getCardCount() > 0) {
            this.player.hit(this.cardDeck);
        } else {
            this.cardDeck = new CardDeck();
            this.player.hit(cardDeck);
        }

        //oppdaterer tilstandene
        this.checkIfPlayerHasBlackJack();
        this.checkIfPlayerHasBusted();
    }

    public void hitDealer() throws IllegalArgumentException {
        this.dealer.hit(this.cardDeck);
    }



    public double getPlayerBalance() {
        return this.player.getBalance();
    }
    public double getPlayerCurrentBet() {
        return this.player.getCurrentBet();
    }

    

    //automatiserte regler for hva dealer skal hit og stand på
    public void dealerTurn() throws IllegalArgumentException {
        while (this.dealer.getHandValue() <= 16){
            this.hitDealer();
        }
        //oppdaterer tilstander
        this.checkIfDealerHasBlackJack();
        this.checkIfDealerHasBusted();
    }


    

    public void upDateWinnerAsInt() {
        this.checkIfDealerHasBlackJack();
        this.checkIfPlayerHasBlackJack();
        this.checkIfPlayerHasBusted();
        this.checkIfDealerHasBusted();
        if (this.hasPlayerBusted) {
            winnerAsInt = -1;
        } else if (this.hasDealerBusted) {
            winnerAsInt = 1;
        } else if (this.player.compareTo(this.dealer) == 0) {
            if (doesPlayerHaveBlackJack) {
                if (doesDealerHaveBlackJack) {
                    winnerAsInt = 0;
                }else{
                    winnerAsInt = 1;
                }
            }else if (doesDealerHaveBlackJack) {
                //denne kodesnutten nås kun hvis kun dealer har BlackJack.
                winnerAsInt = -1;
            }else {
                winnerAsInt = 0;
            }
        } else if (this.player.compareTo(this.dealer) > 0) {
            winnerAsInt = 1;
        } else if (doesPlayerHaveBlackJack) {
            if (doesDealerHaveBlackJack) {
                winnerAsInt = 0;
            }else {
                winnerAsInt = 1;
            }
        }
        else {
            winnerAsInt = -1;
        }
    }
    
    public int getWinnerAsInt(){
        return winnerAsInt;
        
    }

    public void newBettingRound() throws IllegalArgumentException {

        //dette fikser opp i pengene
        if (getWinnerAsInt() == 1) {
            if (doesPlayerHaveBlackJack) {
                //dette er blackjack-premien
                this.player.deposit(this.player.getCurrentBet() * 3);
            }else {
                //dette er vanlig premie
            this.player.deposit(this.player.getCurrentBet() * 2);
            }
        }else if (getWinnerAsInt() == 0) {
            this.player.deposit(this.player.getCurrentBet());
        }


        //innsatsen nulles ut etter hver runde.
        this.player.setCurrentBet(0);
        
        //nuller ut tilstandene tilknyttet runden
        this.hasDealerBusted = false;
        this.hasPlayerBusted = false;
        this.doesDealerHaveBlackJack = false;
        this.doesPlayerHaveBlackJack = false;

        //dette fikser opp i kortene
        this.player.eraseThisPersonsCardHand();
        this.dealer.eraseThisPersonsCardHand();

        checkIsPlayerOutOfMoney();

    }

  

    //checkerne er oppdateringsmetoder for tilstandene tilknyttet busting og blackjack. 
    //Getterne er det eneste som er synlig i kontrolleren. Her har vi med vilje unngått å ha gettere
    //med innebygd logikk, for å strukturere programmet bedre.
    private void checkIfPlayerHasBlackJack(){

        this.doesPlayerHaveBlackJack = this.player.getHandValue() == 21 && this.player.getCardCount() == 2;
    }
    public boolean getDoesPlayerHaveBlackJack(){
        return this.doesPlayerHaveBlackJack;
    }



    private void checkIfDealerHasBlackJack(){

        this.doesDealerHaveBlackJack = this.dealer.getHandValue() == 21 && this.dealer.getCardCount() == 2;
    }
    public boolean getDoesDealerHaveBlackJack(){
        return this.doesDealerHaveBlackJack;
    }


    private void checkIfDealerHasBusted() {

        this.hasDealerBusted = this.dealer.getHandValue() > 21;
    }
    public boolean getHasDealerBusted(){
        return hasDealerBusted;
    }



    private void checkIfPlayerHasBusted() {
        this.hasPlayerBusted = this.player.getHandValue() > 21;
    }
    public boolean getHasPlayerBusted(){
        return hasPlayerBusted;
    }

    private void checkIsPlayerOutOfMoney() {
        this.isPlayerOutOfMoney = this.player.getBalance() <= 0 ;
    }
    public boolean getIsPlayerOutOfMoney(){
        return isPlayerOutOfMoney;
    }




    //INGEN av metodene under brukes i kontrolleren, men har sett oss nødt til å lage de for å
    //lettere kunne skrive tester i gameLogic. Dette er det eneste stedet de brukes, bare hjelpemetoder.
    public void erasePlayerHand(){
        this.player.eraseThisPersonsCardHand();
    }
    public void eraseDealerHand() {
        this.dealer.eraseThisPersonsCardHand();
    } 
    
    public void givePlayerABlackJackHand(){
        erasePlayerHand();
        cardDeck = new CardDeck();
        //dette er et ess
        cardDeck.giveCard(0, this.player.getHand());
        //dette er en knekt
        cardDeck.giveCard(10, this.player.getHand());
        this.checkIfPlayerHasBlackJack();
    }
    public void giveDealerABlackJackHand(){
        eraseDealerHand();
        cardDeck = new CardDeck();
        //dette er et ess
        cardDeck.giveCard(0, this.dealer.getHand());
        //dette er en knekt
        cardDeck.giveCard(10, this.dealer.getHand());
        this.checkIfDealerHasBlackJack();
    }

    public void giveDealerTwentyOneHand(){
        eraseDealerHand();
        cardDeck = new CardDeck();
        cardDeck.giveCard(11, this.dealer.getHand());
        cardDeck.giveCard(5, this.dealer.getHand());
        cardDeck.giveCard(4, this.dealer.getHand());
        checkIfDealerHasBlackJack();
    }
    public void givePlayerTwentyOneHand(){
        erasePlayerHand();
        cardDeck = new CardDeck();
        cardDeck.giveCard(11, this.player.getHand());
        cardDeck.giveCard(5, this.player.getHand());
        cardDeck.giveCard(4, this.player.getHand());
        checkIfPlayerHasBlackJack();
    }

    public void giveDealerATwelvie(){
        eraseDealerHand();
        cardDeck = new CardDeck();
        cardDeck.giveCard(9, this.dealer.getHand());
        cardDeck.giveCard(2, this.dealer.getHand());
    }


}
