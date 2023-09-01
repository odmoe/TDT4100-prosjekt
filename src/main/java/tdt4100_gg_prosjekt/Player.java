package tdt4100_gg_prosjekt;

import java.util.stream.Collectors;



public class Player implements Person{

    private CardHand playerHand = new CardHand();
    private double balance;
    private double currentBet;
    private int age = 18; //det er ikke lov å gamble før man er 18


    public Player(double balance) throws IllegalArgumentException{
        if (!isBalanceValid(balance)) {
            throw new IllegalArgumentException("Can't construct player with negative balance");
        }
        this.balance = balance;
        this.currentBet = 0.0;
    }

    /*vi forbyr å lage en person med negativ saldo.
    Denne betingelsen kunne vi ha lagt i selve konstruktøren 
    fordi den er såpass enkel, men dersom vi senere ønsker
    å legge til flere betingelser kan det være greit å ha en egen valideringsmetode.
    Samme gjelder for validering av deposit og withdraw
    */

    private boolean isBalanceValid(double balance) {
        return balance >= 0.0;
    }

    //gettere
    public int getAge() {
        return this.age;
    }

    public double getBalance() {
        return this.balance;
    }

    @Override
    public CardHand getHand() {
        return this.playerHand;
    }


    
    @Override
    public String[] getCurrentHand() throws NullPointerException{
        try {
            return this.getHand().getMembers().stream().map(Card::toString)
            .collect(Collectors.toList()).toArray(new String[0]);
        } catch (Exception e) {
            throw new NullPointerException("Couldn't find the demanded hand");
        }
    }


    public double getCurrentBet() {
        return currentBet;
    }


    public void setCurrentBet(double currentBet) throws IllegalArgumentException{
        if (currentBet >= 0) {
            this.currentBet = currentBet;
        }else {
            throw new IllegalArgumentException("Can't set negative bet");
        }
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setHand(CardHand hand){
        this.playerHand = hand;
    }




    //endring av saldo
    public void withdraw(double amount)  throws IllegalArgumentException{
        if (!isValidWithdrawal(amount)) {
            throw new IllegalArgumentException("Can't withdraw resulting in negative amount");
        }
        this.balance -= amount;
        this.currentBet += amount;
    }
    //validerer om uttak er for stort
    private boolean isValidWithdrawal(double amount) {
        return this.balance >= amount && amount > 0;
    }
    

    //deposit betyr her å flytte fra "stake"/"bet" til selve balance
    public void deposit(double amount) throws IllegalArgumentException {
        if (!isValidDeposit(amount)) {
            throw new IllegalArgumentException("Can't remove an empty bet");
        }
        this.balance += amount;
    }

    //denne validatoren er også svært enkel, og kunne vært direkte implementert i deposit-metoden,
    //men for videreutvikling av appen har vi foretrukket en tydelig struktur.
    private boolean isValidDeposit(double amount) {
        return amount > 0;
    }



    //dette er i prinsippet bare delegering nedover i klassene
    @Override
    public void hit(CardDeck cardDeck) throws IllegalArgumentException{
        if (this.currentBet == 0) {
            throw new IllegalArgumentException("Can't hit when bet is zero");
        }
        cardDeck.giveRandomCard(this.playerHand);
    }


    @Override
    public void eraseThisPersonsCardHand() {
        //her ønsker vi at ingenting skjer dersom hånden allerede er tom.
        if (this.getCardCount() != 0) {
            this.playerHand = new CardHand();
        }
    }

    @Override
    public int compareTo(Person other) {
        return this.getHand().compareTo(other.getHand());
    }

    //delegering ned til spillerens hånd
    @Override
    public int getHandValue() {
        return this.getHand().getHandValue();
    }

    @Override
    public int getCardCount() {
        return this.getHand().getCardCount();
    }

    @Override
    public Card getCard(int i) {
        return this.playerHand.getCard(i);
    }
    

    
}
