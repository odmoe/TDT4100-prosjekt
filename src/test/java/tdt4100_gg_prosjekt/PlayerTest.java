package tdt4100_gg_prosjekt;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import tdt4100_gg_prosjekt.Card;
import tdt4100_gg_prosjekt.CardDeck;
import tdt4100_gg_prosjekt.Dealer;
import tdt4100_gg_prosjekt.Player;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;

public class PlayerTest {
    
    Player player;
    CardDeck cardDeck;
    CardDeck emptyCardDeck;
    Dealer dealer;

    @BeforeEach
    public void setup(){
        player = new Player(100);
        dealer = new Dealer();
        cardDeck = new CardDeck();
        player.setCurrentBet(10);
    }



    @Test
    @DisplayName("test konstruktør")
    public void testConstructor(){

        //sjekk gyldighet av argumenter. Ønsket at alle negative verdier er ugyldige og resten positiv (0 gyldig).
        assertThrows(IllegalArgumentException.class, () -> player = new Player(-0.1));
        assertThrows(IllegalArgumentException.class, () -> player = new Player(-4000000000000.0));
        assertDoesNotThrow(() -> new Player(0));
        assertDoesNotThrow(() -> new Player(1000000000000000.0));

        //sjekk at gyldig argument og getter samsvarer
        assertEquals(1234, new Player(1234).getBalance());
    }

    @Test
    @DisplayName("test uthenting av korthånd som String[]")
    public void testGetCurrentHand(){

        //la player hitte noen ganger
        for (int i = 0; i < 5; i++) {
            player.hit(cardDeck);
        }
        //sjekk at antall kort utdelt stemmer
        assertEquals(5, player.getCurrentHand().length);

        //sjekk at alle elementer blir strings
        for (int i = 0; i < 5; i++) {
            assertTrue(player.getCurrentHand()[i].getClass().equals(String.class));
        }
        //sjekk at alle elementene blir mappet til riktig toString
        for (int i = 0; i < 5; i++) {
            assertEquals(player.getCard(i).toString(), player.getCurrentHand()[i]);
        }
    }

    @Test
    @DisplayName("test flytting av penger mellom bet og balance")
    public void testWithdrawAndDeposit(){
        //sjekk at den thrower ved ugyldig uttak/innsettelse
        assertThrows(IllegalArgumentException.class, () -> player.withdraw(101));
        assertThrows(IllegalArgumentException.class, () -> player.withdraw(0));
        assertThrows(IllegalArgumentException.class, () -> player.withdraw(-1));
        assertThrows(IllegalArgumentException.class, () -> player.deposit(0));
        assertThrows(IllegalArgumentException.class, () -> player.deposit(-1));
        player.withdraw(10);
        //sjekk at pengene flyttes som tiltenkt
        assertEquals(90,player.getBalance());
        assertEquals(20,player.getCurrentBet());
        player.deposit(50);
        assertEquals(140,player.getBalance());
        


    }

    @Test
    @DisplayName("Sjekk hitting")
    public void testHit(){
        
        //sjekk at den thrower når kortstokken ikke ennå er konstruert
        assertThrows(NullPointerException.class, () -> player.hit(emptyCardDeck));
        
        //dobbeltsjekke at utgangspunktet er 156 kort i stokken og 0 i hånden
        assertEquals(156,cardDeck.getCardCount());
        assertEquals(0,player.getCardCount());
        assertThrows(IllegalArgumentException.class, () -> player.getCard(0));

        player.hit(cardDeck);
        Card c = player.getCard(0);

        //sjekk at kort har blitt fjernet og dukket opp i player's hånd
        assertEquals(155,cardDeck.getCardCount());
        assertEquals(1,player.getCardCount());
        //sjekk at det randomiserte kortet nå finnes kun to ganger i kortstokken vår
        assertEquals(2,(int)cardDeck.getMembers()
        .stream().filter(p -> p.toString().equals(c.toString())).count());

        //sjekk at spiller med currentBet == 0  ikke kan hitte kortstokken
        assertThrows(IllegalArgumentException.class, () -> {
        player.setCurrentBet(0);
        player.hit(cardDeck);
        });

    }

    @Test
    @DisplayName("test å fjern hånden til spiller")
    public void testEraseThisPersonsCardHand(){

        player.hit(cardDeck);
        player.hit(cardDeck);
        player.hit(cardDeck);
        //sjekk at antall kort stemmer før tømming
        assertEquals(3,player.getCardCount());
        player.eraseThisPersonsCardHand();
        //sjekk at korthånden tømmes 
        assertEquals(0,player.getCardCount());
        //sjekk at det ikke throwes noe selv om hånden er tom:
        assertDoesNotThrow(() -> player.eraseThisPersonsCardHand());

    }

    @Test
    @DisplayName("Test delegeringsmetodene")
    public void testDelegationMethods(){

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
        assertEquals(player.getHand().getHandValue(), player.getHandValue());
        //sjekk at getCardCount delegeres riktig
        assertEquals(player.getHand().getCardCount(),player.getCardCount());
        //sjekk at compareTo delegeres riktig
        assertEquals(player.getHand().compareTo(dealer.getHand()), player.compareTo(dealer));


    }



}
