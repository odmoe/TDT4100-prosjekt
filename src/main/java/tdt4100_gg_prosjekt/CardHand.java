package tdt4100_gg_prosjekt;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class CardHand implements CardAmount, Comparable<CardHand> {


    private List<Card> members = new ArrayList<>();
    // private static final char[] suits = {'S','H','D','C'};

    

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
    public List<Card> getMembers() {
        return this.members;
    }

    @Override
    public void addCard(Card card) throws IllegalArgumentException{
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


    //denne beregner håndverdi i BlackJack-sammenheng
    public int getHandValue() { 
        int sum = 0;
        int numOfAces = this.getMembers().stream().filter(p -> p.getValue() == 1).collect(Collectors.toList()).size();
        for (Card card : this.getMembers()) {
            sum += card.getBlackJackValue();
        }
        while (sum > 21 && numOfAces > 0) {
                sum -= 10;
                numOfAces -= 1;

        }
        return sum;
        
    }

    //vi trenger kun én måte å sammenligne hender på
    @Override
    public int compareTo(CardHand other) {
        return this.getHandValue() - other.getHandValue();
    }


    @Override
    public void giveCard(int i, CardAmount receiver) throws IllegalArgumentException{
        receiver.addCard(this.members.get(i));
        this.removeCard(this.members.get(i));
    }


    @Override
    public void giveRandomCard(CardAmount receiver) throws IllegalArgumentException{
        Random rand = new Random();
        Card toGive = this.getCard(rand.nextInt(this.getCardCount()));
        receiver.addCard(toGive);
        this.removeCard(toGive);
    }
    
}
