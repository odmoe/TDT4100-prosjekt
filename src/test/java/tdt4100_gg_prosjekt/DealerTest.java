package tdt4100_gg_prosjekt;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import tdt4100_gg_prosjekt.Card;
import tdt4100_gg_prosjekt.CardDeck;
import tdt4100_gg_prosjekt.Dealer;
import tdt4100_gg_prosjekt.Player;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;

public class DealerTest {

    Dealer dealer;
    CardDeck cardDeck;
    CardDeck emptyCardDeck;
    Player player;

    @BeforeEach
    public void setup(){
        dealer = new Dealer();
        cardDeck = new CardDeck();
    }


    @Test
    @DisplayName("Test dealer sin hitting")
    public void testHit(){
        //sjekk at den thrower når kortstokken ikke ennå er konstruert
        assertThrows(NullPointerException.class, () -> dealer.hit(emptyCardDeck));
        
        //dobbeltsjekke at utgangspunktet er 156 kort i stokken og 0 i hånden
        assertEquals(156,cardDeck.getCardCount());
        assertEquals(0,dealer.getCardCount());
        assertThrows(IllegalArgumentException.class, () -> dealer.getCard(0));

        dealer.hit(cardDeck);
        Card c = dealer.getHand().getCard(0);

        //sjekk at kort har blitt fjernet og dukket opp i player's hånd
        assertEquals(155,cardDeck.getCardCount());
        assertEquals(1,dealer.getCardCount());
        //sjekk at det randomiserte kortet nå finnes kun to ganger i kortstokken vår
        assertEquals(2,(int)cardDeck.getMembers()
        .stream().filter(p -> p.toString().equals(c.toString())).count());

    }

    @Test
    @DisplayName("test uthenting av korthånd som String[]")

    public void testGetCurrentHand(){
        //la dealer hitte noen ganger
        for (int i = 0; i < 5; i++) {
            dealer.hit(cardDeck);
        }
        //sjekk at antall kort utdelt stemmer
        assertEquals(5, dealer.getCurrentHand().length);

        //sjekk at alle elementer blir strings
        for (int i = 0; i < 5; i++) {
            assertTrue(dealer.getCurrentHand()[i].getClass().equals(String.class));
        }
        //sjekk at alle elementene blir mappet til riktig toString
        for (int i = 0; i < 5; i++) {
            assertEquals(dealer.getCard(i).toString(), dealer.getCurrentHand()[i]);
        }
    }

    @Test
    @DisplayName("test å fjern hånden til dealer")
    public void testEraseThisPersonsCardHand(){

        dealer.hit(cardDeck);
        dealer.hit(cardDeck);
        dealer.hit(cardDeck);
        //sjekk at antall kort stemmer før tømming
        assertEquals(3,dealer.getCardCount());
        dealer.eraseThisPersonsCardHand();
        //sjekk at korthånden tømmes 
        assertEquals(0,dealer.getCardCount());
        //sjekk at det ikke throwes noe selv om hånden er tom:
        assertDoesNotThrow(() -> dealer.eraseThisPersonsCardHand());

    }

    @Test
    @DisplayName("Test delegeringsmetodene")
    public void testDelegationMethods(){
        
        player = new Player(100);
        player.setCurrentBet(10);

        /*Funksjonaliteten til metodene det delegeres videre til er allerede sjekket i 
        CardHand-klassen. Det vi sjekker her er altså om Player-klassen sine metoder
        delegerer til riktig metoder i CardHand.
        */
        player.hit(cardDeck);
        player.hit(cardDeck);
        player.hit(cardDeck);
        dealer.hit(cardDeck);
        dealer.hit(cardDeck);
        dealer.hit(cardDeck);
        //sjekk at getHandValue delegeres riktig
        assertEquals(dealer.getHand().getHandValue(), dealer.getHandValue());
        //sjekk at getCardCount delegeres riktig
        assertEquals(dealer.getHand().getCardCount(),dealer.getCardCount());
        //sjekk at compareTo delegeres riktig
        assertEquals(dealer.getHand().compareTo(player.getHand()), dealer.compareTo(player));


    }
    
}
