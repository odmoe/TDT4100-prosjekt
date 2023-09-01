package tdt4100_gg_prosjekt;

public interface Person extends Comparable<Person>{

    public CardHand getHand();
    public void hit(CardDeck cardDeck);
    public String[] getCurrentHand();
    public void eraseThisPersonsCardHand();
    public int compareTo(Person other);
    public int getHandValue();
    public int getCardCount();
    public Card getCard(int i);

}
