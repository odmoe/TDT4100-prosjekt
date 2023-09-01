package tdt4100_gg_prosjekt;

import java.util.List;

public interface CardAmount {

    //dette interfacet er et generelt rammeverk for hva en CardAmount-klasse må inneholde. Ikke alle disse metodene har blitt anvendt for 
    //CardHand-klassen, men det er fint til videreutvikling.

    public Card getCard(int i);
    public int getCardCount();
    public List<Card> getMembers();
    public void addCard(Card card);
    public void removeCard(Card card);
    public void giveCard(int i, CardAmount receiver); 
    public void giveRandomCard(CardAmount receiver);
}
