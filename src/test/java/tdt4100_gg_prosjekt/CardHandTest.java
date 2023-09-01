package tdt4100_gg_prosjekt;


import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import tdt4100_gg_prosjekt.Card;
import tdt4100_gg_prosjekt.CardDeck;
import tdt4100_gg_prosjekt.CardHand;

public class CardHandTest {

    CardDeck cardDeck;
    CardHand cardHand;
    CardDeck emptyCardDeck;

    @BeforeEach
    public void setup(){
        cardDeck = new CardDeck();
        cardHand = new CardHand();
        cardHand.addCard(new Card(13, 'H'));
        cardHand.addCard(new Card(10, 'D'));
        cardHand.addCard(new Card(7, 'S'));
        cardHand.addCard(new Card(3, 'C'));
    }

    @Test
    public void testGetCard(){
        assertEquals(4,cardHand.getCardCount());
        cardDeck.giveRandomCard(cardHand);
        cardDeck.giveRandomCard(cardHand);
        assertEquals(6,cardHand.getCardCount());

        assertThrows(IllegalArgumentException.class, () -> {
            cardHand.getCard(-1);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            cardHand.getCard(6);
        });
        assertDoesNotThrow(() -> cardHand.getCard(5));
    }


    @Test
    @DisplayName("Legge til og fjerne kort fra hånd")
    public void testAdderAndRemover(){
        Card card = new Card(4,'S');

        //dobbeltsjekker at kortet finnes 0 ganger før adding
        assertEquals(0,(int)cardHand.getMembers()
        .stream().filter(p -> p.toString().equals(card.toString())).count());

        cardHand.addCard(card);

        //tester om kortet finnes 1 ganger etter adding
        assertEquals(1,(int)cardHand.getMembers()
        .stream().filter(p -> p.toString().equals(card.toString())).count());

        //tester at størrelsen er riktig
        assertEquals(5, cardHand.getCardCount());

        cardHand.removeCard(card);

        //tester at kortet finnes 0 ganger igjen etter fjerning
        assertEquals(0,(int)cardHand.getMembers()
        .stream().filter(p -> p.toString().equals(card.toString())).count());
        //tester at størrelsen er tilbake til normal
        assertEquals(4, cardHand.getCardCount());
    }

    @Test 
    @DisplayName("Flytte bestemt kort mellom amounts")
    public void testGiveCard(){
        cardHand.giveCard(0,cardDeck);
        assertEquals(157, cardDeck.getCardCount());
        assertEquals(3,cardHand.getCardCount());

        assertEquals(4,(int)cardDeck.getMembers()
        .stream().filter(p -> p.toString().equals("H13")).count());
        
        assertEquals(0,(int)cardHand.getMembers()
        .stream().filter(p -> p.toString().equals("H13")).count());

        assertThrows(IndexOutOfBoundsException.class, () -> cardHand.giveCard(20,cardDeck));

    }

    @Test
    @DisplayName("Flytte tilfeldig kort mellom amounts")
    public void testGiveRandomCard(){
        
        //sjekk at man ikke kan giveRandomCard før emptyCardHand er konstruert
        assertThrows(NullPointerException.class, () -> cardHand.giveRandomCard(emptyCardDeck));

        //flytt 4 tilfeldige kort fra hånd til deck
        for (int i = 0; i < 4; i++) {
            cardHand.giveRandomCard(cardDeck);
        }
        //sjekk at det totale antall eksemplaret av kort flyttet fra CardHand er 4 (disse finnes det jo et "ekstra" eksemplar av)
        assertEquals(4, (int)cardDeck.getMembers()
        .stream().filter(p -> p.toString().equals("H13")).count()
        + (int)cardHand.getMembers().stream().filter(p -> p.toString().equals("H13")).count());

        //sjekk at et annet tilfeldig kort som ikke fantes i CardHand kun har 3 eksemplarer til sammen
        assertEquals(3, (int)cardDeck.getMembers()
        .stream().filter(p -> p.toString().equals("H4")).count()
        + (int)cardHand.getMembers().stream().filter(p -> p.toString().equals("H4")).count());


        //dobbeltsjekk at antall kort i de respektive amountene er slik tiltenkt.
        assertEquals(160,cardDeck.getCardCount());
        assertEquals(0,cardHand.getCardCount());

    }



    @Test
    @DisplayName("Sjekk verdiberegning av hender")
    public void testGetHandValue(){
        
        /*
        vi vet allerede fra CardTest at getBlackJackValue funker slik den skal. Det vi i hovedsak
        sjekker her er altså hender med esser.
        */
        
        //med kun esser. 
        cardHand = new CardHand();
        cardHand.addCard(new Card(1,'D'));
        for (int i = 11; i < 22; i++) {
            assertEquals(i,cardHand.getHandValue());
            cardHand.addCard(new Card(1, 'S'));
        }

        /*
        Ett ytterligere ess skal i første omgang resultere i én av tre ting:
        1. Det bustes ikke, og esset regnes som 11
        2. Det bustes, og essets verdi "switcher" til 1
        3. Man treffer 21, og esset regnes som 11
        */

        //case nr 1
        cardHand = new CardHand();
        cardHand.addCard(new Card(3,'S'));
        cardHand.addCard(new Card(1,'H'));
        assertEquals(14,cardHand.getHandValue());

        //case nr 2
        cardHand = new CardHand();
        cardHand.addCard(new Card(8, 'H'));
        cardHand.addCard(new Card(8, 'D'));
        cardHand.addCard(new Card(1,'S'));
        assertEquals(17, cardHand.getHandValue());

        //case nr 3
        cardHand = new CardHand();
        cardHand.addCard(new Card(6,'H'));
        cardHand.addCard(new Card(4,'D'));
        assertEquals(10,cardHand.getHandValue());
        cardHand.addCard(new Card(1, 'D'));
        assertEquals(21,cardHand.getHandValue());
    }




    @Test
    @DisplayName ("Sjekk sammenligning av hender")
    public void testCompareTo(){

        CardHand other;
        
        /*merk at denne SKAL returnere positiv verdi hvis "this" 
        har verdi større enn 21 (bust) og "other" har verdi under 21.
        Busting og BlackJack håndteres i GameLogic og skal derfor ikke tas hensyn til her!
        */

        //case 1: this er større
        other = new CardHand();
        other.addCard(new Card(1,'S'));
        assertEquals(19, cardHand.compareTo(other));

        //case 2: de er like store
        other = new CardHand();
        other.addCard(new Card(12, 'S'));
        other.addCard(new Card(11, 'D'));
        other.addCard(new Card(4, 'H'));
        other.addCard(new Card(6, 'D'));
        assertEquals(0, cardHand.compareTo(other));

        //case 3: other er større
        cardHand = new CardHand();
        cardHand.addCard(new Card(4, 'D'));
        other = new CardHand();
        other.addCard(new Card(6,'S'));
        assertEquals(-2, cardHand.compareTo(other));
        

    }
    
}
