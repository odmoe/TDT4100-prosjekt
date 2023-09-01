package tdt4100_gg_prosjekt;

import java.util.stream.Collectors;


public class Dealer implements Person {

    private CardHand dealerHand = new CardHand();


    @Override
    public CardHand getHand() {
        return this.dealerHand;
    }

    @Override
    public void hit(CardDeck cardDeck) throws IllegalArgumentException{
        cardDeck.giveRandomCard(this.dealerHand);
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

    @Override
    public void eraseThisPersonsCardHand() {
        this.dealerHand = new CardHand();
    }

    @Override
    public int compareTo(Person other) {
        return this.getHand().getHandValue() - other.getHand().getHandValue();
    }

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
        return this.dealerHand.getCard(i);
    }

}
