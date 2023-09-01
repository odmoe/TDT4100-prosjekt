package tdt4100_gg_prosjekt;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//import javax.sound.midi.Receiver;


public class CardDeck implements CardAmount {

    
    private List<Card> members = new ArrayList<>();
    private static final char[] suits = {'S','H','D','C'}; //lager en array med suits så vi kan iterere i konstruktøren

    //konstruktøren, denne lager en "mega-kortstokk" av tre kortstokker
    public CardDeck() throws IllegalArgumentException{

        for (int j = 0; j < 3; j++) {
            for (char c : suits) {
                for (int i = 1; i <= 13; i++) { 
                    this.members.add(new Card(i,c));
                }
            }
        }

    }

    // denne konstruktøren benyttes ikke for øyeblikket men vi overloader for valgfrihet
    public CardDeck(int k) throws IllegalArgumentException {
        if (k > 0) {
            for (int j = 0; j < k; j++) {
                for (char c : suits) {
                    for (int i = 1; i <= 13; i++) { 
                        this.members.add(new Card(i,c));
                    }
                }
            }
        }else {
            throw new IllegalArgumentException("Number of decks can't be negative");}
    }
    
    @Override
    public List<Card> getMembers()   {
        return this.members;
    }

    @Override
    public Card getCard(int i) throws IllegalArgumentException{
        try {
            return this.members.get(i);
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Card index out of range");
        }
    }

    @Override
    public int getCardCount() {
        return this.members.size();
    }

    @Override
    //denne thrower fordi man kan passe et ny-konstruert Card som argument
    public void addCard(Card card) throws IllegalArgumentException {
        this.members.add(card);
    }

    @Override
    public void removeCard(Card card) throws IllegalArgumentException{
        try {
            this.members.remove(card);
        } catch (Exception e) {
            throw new IllegalArgumentException("Absent card can't be removed");
        }
    }


    @Override
    public void giveCard(int i, CardAmount receiver) throws IllegalArgumentException{
        receiver.addCard(this.getCard(i));
        this.removeCard(this.getCard(i));
    }

    @Override
    public void giveRandomCard(CardAmount receiver) throws IllegalArgumentException {
        Random rand = new Random();
        Card toGive = this.getCard(rand.nextInt(this.getCardCount()));
        receiver.addCard(toGive);
        this.removeCard(toGive);
    }

    
}
