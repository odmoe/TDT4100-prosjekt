package tdt4100_gg_prosjekt;


import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import tdt4100_gg_prosjekt.Card;
import tdt4100_gg_prosjekt.CardDeck;
import tdt4100_gg_prosjekt.CardHand;

public class CardDeckTest {


    CardDeck cardDeck;
    CardHand cardHand;

    @BeforeEach
    public void setup(){
        cardDeck = new CardDeck();
    }


    @Test
    public void testConstructor(){
        //sjekk at det blir riktig antall kort
        assertEquals(156, cardDeck.getCardCount());
        //sjekk at alle mulige kort har nøyaktig 3 tilfeller i kortstokken
        for (int i = 1; i < 14; i++) {
            for (Character c : new Character[]{'S','H','D','C'}) {
                Card card = new Card(i, c);
                long numOfOcurrences = cardDeck.getMembers().stream().filter(p -> p.toString().equals(card.toString())).count();
                assertEquals(3,numOfOcurrences);
            }
        }

    }

    @Test
    @DisplayName("Uthenting av bestemte kort og størrelse")
    public void testGetCard(){
        assertEquals(156,cardDeck.getCardCount());
        //sjekker at den thrower/utløser unntak hvis indeks er ute av range
        assertThrows(IllegalArgumentException.class, () -> {
            cardDeck.getCard(-1);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            cardDeck.getCard(156);
        });
        //sjekker at den gir oss riktig kort. S1 er pga utformingen på
        //konstruktøren vår det første kortet som ligger i medlemslista
        assertEquals("S1", cardDeck.getCard(0).toString());
        assertEquals("C13", cardDeck.getCard(155).toString());
    }

    @Test
    @DisplayName("Legge til og fjerne kort")
    public void testAdderAndRemover(){
        Card card = new Card(4,'S');
        //dobbeltsjekker at kortet finnes 3 ganger før adding
        assertEquals(3,(int)cardDeck.getMembers()
        .stream().filter(p -> p.toString().equals(card.toString())).count());

        cardDeck.addCard(card);

        //tester om kortet finnes 4 ganger etter adding
        assertEquals(4,(int)cardDeck.getMembers()
        .stream().filter(p -> p.toString().equals(card.toString())).count());
        //tester at størrelsen er riktig
        assertEquals(157, cardDeck.getCardCount());

        cardDeck.removeCard(card);

        //tester at kortet finnes 3 ganger igjen etter fjerning
        assertEquals(3,(int)cardDeck.getMembers()
        .stream().filter(p -> p.toString().equals(card.toString())).count());
        //tester at størrelsen er tilbake til normal
        assertEquals(156, cardDeck.getCardCount());
    }

    @Test 
    @DisplayName("Flytte bestemt kort mellom amounts")
    public void testGiveCard(){
        cardHand = new CardHand();


        Card card = cardDeck.getCard(5);
        cardDeck.giveCard(5,cardHand);
        assertEquals(155, cardDeck.getCardCount());
        assertEquals(1,cardHand.getCardCount());
        assertEquals(card,cardHand.getCard(0));

    }

    @Test
    @DisplayName("Flytte tilfeldig kort mellom amounts")
    public void testGiveRandomCard(){
        
        //sjekk at man ikke kan giveRandomCard før cardHand er konstruert
        assertThrows(NullPointerException.class, () -> cardDeck.giveRandomCard(cardHand));
        cardHand = new CardHand();

        //flytt 5 tilfeldige kort fra deck til hånd
        for (int i = 0; i < 5; i++) {
            cardDeck.giveRandomCard(cardHand);
        }
        //sjekk at det totale antall eksemplaret av alle kort som er flyttet er nøyaktig 3.
        for (Card c : cardHand.getMembers()) {

            assertEquals(3,(int)cardDeck.getMembers()
            .stream().filter(p -> p.toString().equals(c.toString())).count()                                //antall tilfeller i decken
            + (int)cardHand.getMembers().stream().filter(p -> p.toString().equals(c.toString())).count()); // + antall tilfeller i hånden
        }
        //dobbeltsjekk at antall kort i de respektive amountene er slik tiltenkt.
        assertEquals(151,cardDeck.getCardCount());
        assertEquals(5,cardHand.getCardCount());

    }
}
