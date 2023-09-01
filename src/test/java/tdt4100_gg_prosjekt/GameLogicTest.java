package tdt4100_gg_prosjekt;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import tdt4100_gg_prosjekt.GameLogic;


public class GameLogicTest {

    //

    GameLogic gameLogic = GameLogic.getInstance();

    @BeforeEach
    public void setup(){
        gameLogic.cleanUpAfterGame();
    }



    


    //hjelpemetode for testCleanUpAfterGame
    private void testReset(){
        assertEquals(100, gameLogic.getPlayerBalance());
        assertEquals(0, gameLogic.getPlayerCurrentBet());
        assertEquals(0, gameLogic.getWinnerAsInt());
        assertEquals("0", gameLogic.getCurrentDealerHandCount());
        assertEquals("0", gameLogic.getCurrentPlayerHandCount());
        assertEquals(false, gameLogic.getDoesDealerHaveBlackJack());
        assertEquals(false, gameLogic.getDoesPlayerHaveBlackJack());
        assertEquals(false, gameLogic.getHasDealerBusted());
        assertEquals(false, gameLogic.getHasPlayerBusted()); 
        assertEquals(false, gameLogic.getIsPlayerOutOfMoney()); 
    }


    //dette er en hjelpemetode som setter i gang litt prosesser som endrer feltene i gameLogic
    private void messUpSomeFields(){
        gameLogic.addBet(100.0);

        for (int i = 0; i < 10; i++) {
            gameLogic.hitPlayer();
        }
        gameLogic.hitDealer();
        gameLogic.hitDealer();
        gameLogic.upDateWinnerAsInt();
    }



    @Test
    @DisplayName("test cleanup-metoden. Merk at denne også kalles gjennom konstruktøren til GameLogic")
    public void testCleanUpAfterGame(){
        this.testReset();
        //her endrer vi litt tilstander for å se om cleanUpAfterGame() klarer å rydde opp.
        this.messUpSomeFields();

        gameLogic.cleanUpAfterGame();
        this.testReset();        
    }


    @Test
    @DisplayName("test uthenting av hender som StringArrays og hitting")
    public void testGettersForHandsAsStringArraysAndHitting(){


        Random rand = new Random();
        int r = rand.nextInt(3,100);
        int s = rand.nextInt(3,100);
        //sjekk at dealer kan hitte selv om player har tom bet
        assertDoesNotThrow(() -> gameLogic.hitDealer());
        gameLogic.cleanUpAfterGame();
        //sjekk at player ikke kan hittemed tom bet
        assertThrows(IllegalArgumentException.class, () -> gameLogic.hitPlayer());
        gameLogic.addBet(10.0);
        for (int i = 0; i < r; i++) {
            gameLogic.hitPlayer();
        }
        for (int i = 0; i < s; i++) {
            gameLogic.hitDealer();
        }
        //sjekk at riktig antall kort dukker opp i hånden
        assertEquals(r,gameLogic.getCurrentPlayerHandAsStringArray().length);
        assertEquals(s,gameLogic.getCurrentDealerHandAsStringArray().length);
        //går gjennom 50 ganger at boolean-feltene doesPlayerHaveBlackJack og hasPlayerBusted stemmer overens
        //med de andre tilstandene dvs antall kort og handcount.
        for (int i = 0; i < 50; i++) {
            gameLogic.cleanUpAfterGame();
            gameLogic.addBet(10);
            int j = rand.nextInt(10);
            for (int k = 0; k < j; k++) {
                gameLogic.hitPlayer();
                assertEquals(gameLogic.getDoesPlayerHaveBlackJack(), 
                    (gameLogic.getCurrentPlayerHandAsStringArray().length == 2 && Integer.parseInt(gameLogic.getCurrentPlayerHandCount()) == 21));
                assertEquals(gameLogic.getHasPlayerBusted(),
                    Integer.parseInt(gameLogic.getCurrentPlayerHandCount()) > 21);
            }
            
        }
    }

    @Test
    @DisplayName("Sjekk uthenting av counten til dealer og player som strings")
    public void testGetCurrentHandCountsAsStrings(){
        //her endres countene
        messUpSomeFields();
        //sjekk at countene er ints ulik 0
        assertFalse(gameLogic.getCurrentDealerHandCount().equals("0"));
        assertFalse(gameLogic.getCurrentPlayerHandCount().equals("0"));
        assertTrue(gameLogic.getCurrentPlayerHandCount().matches("[0-9]+"));
        assertTrue(gameLogic.getCurrentDealerHandCount().matches("[0-9]+"));
    }

    @Test
    @DisplayName("Sjekk å legge til og fjern bets")
    public void testAddAndEraseBet(){
        //sjekk at throw med negativ
        assertThrows(IllegalArgumentException.class, () -> gameLogic.addBet(-0.0));
        //sjekk at saldo er 100 og bet 0
        assertEquals(0,gameLogic.getPlayerCurrentBet());
        assertEquals(100, gameLogic.getPlayerBalance());
        gameLogic.addBet(5);
        //sjekk med 5
        assertEquals(5, gameLogic.getPlayerCurrentBet());
        assertEquals(95, gameLogic.getPlayerBalance());
        //sjekk at thrower når legger til større bet enn saldo
        assertThrows(IllegalArgumentException.class, () -> gameLogic.addBet(96));
        //sjekk at ikke thrower når legger til hele nåværende saldo i bet
        assertDoesNotThrow(() -> gameLogic.addBet(95));
        //test erasebet
        gameLogic.eraseBet();
        assertEquals(0, gameLogic.getPlayerCurrentBet());
        assertEquals(100, gameLogic.getPlayerBalance());
        //sjekk med 10
        gameLogic.addBet(10);
        assertEquals(10, gameLogic.getPlayerCurrentBet());
        assertEquals(90, gameLogic.getPlayerBalance());
        //sjekk at thrower når legger til større bet enn saldo
        assertThrows(IllegalArgumentException.class, () -> gameLogic.addBet(91));
        //sjekk at ikke thrower når legger til hele nåværende saldo i bet
        assertDoesNotThrow(() -> gameLogic.addBet(90));
        //test erasebet igjen
        gameLogic.eraseBet();
        assertEquals(0,gameLogic.getPlayerCurrentBet());
        assertEquals(100, gameLogic.getPlayerBalance());
    }

    //denne er veldig viktig for spillet!!!
    @Test
    @DisplayName("sjekk at dealer spiller etter standardiserte regler")
    public void testDealerTurn(){
        //loop for å la den testes flere ganger
        for (int k = 0; k < 50; k++) {
            gameLogic.cleanUpAfterGame();

            int sum = 0;
            //her er ønsket oppførsel at dealeren hitter til han har minst 17 i count
            gameLogic.dealerTurn();
            /* LES DETTE!!!
            måten vi har valgt å løse dette på er litt kreativ
            Vi trenger i prinsippet kun å sjekke at dealeren har oppnådd en sum på minst 17, 
            i tillegg til å sjekke at counten før han hittet SISTE gang var maximum 16.
            Hvis disse kravene oppfylles har dealeren fulgt reglene*/

            //her lagres counten til dealeren
            sum = Integer.parseInt(gameLogic.getCurrentDealerHandCount());
            //sjekk at summen er minst 17
            assertTrue(sum >= 17);
            

            //fordi vi ikke har tilgang til de faktiske kortobjektene må vi 
            //løse det på en litt finnulig måte
            String[] handAsArray = gameLogic.getCurrentDealerHandAsStringArray();
            
            String lastCard = handAsArray[handAsArray.length-1];
            //finn normal kortverdi til lastCard
            Integer lastCardVal = Integer.parseInt(lastCard.substring(1,lastCard.length()));

            //sjekk at counten var <= 16 avhengig av hva det siste kortet faktisk var.
            if (lastCardVal == 1) {
                assertTrue(sum - 1 <= 16 || sum - 11 <= 16);
            }else if (lastCardVal == 10|| lastCardVal == 11 || lastCardVal == 12|| lastCardVal == 13) {
                assertTrue(sum - 10 <= 16);
            }else {
                assertTrue(sum - lastCardVal <= 16);
            }
            //her sjekker  vi at booleanene hasDealerBusted og doesDealerHaveBlackJack samsvarer med de andre tilstandene
            assertEquals( ( sum == 21 && handAsArray.length == 2), gameLogic.getDoesDealerHaveBlackJack());
            assertEquals( (sum > 21), gameLogic.getHasDealerBusted());
        }
        
    }


    /*Følgende test er svært omfattende. Det den gjør er å sjekke at alle ulike situasjoner
    med ulike korthender hos dealer og player fører til at winnerAsInt blir satt til riktig verdi.
    Metodene "checkHasPlayerBusted" i GameLogic-klassen er jo kun verktøy for å få til nettopp
    dette, så de testes indirekte gjennom denne testen. Vi har strukturert det slik at vi har delt 
    opp i fire forskjellige scenarioer:
        - 1. Player har en vanlig hånd
        - 2. Player har BlackJack
        - 3. Player har 21, men IKKE BlackJack
        - 4. Player har bustet
    For hver av disse scenarioene tester vi hva som skjer for hver av mulighetene til dealerhånden.
    På denne måten blir alle cases tatt hånd om
     */
    @Test
    @DisplayName("testing av winnerAsInt-feltet avhengig av spillets andre tilstander")
    public void testWinnerAsIntFunctionality(){
        //fordi vi ikke har tilgang til å dele ut bestemte kort til spilleren må
        //vi igjen løse det på en litt spesiell måte
        gameLogic.addBet(10);

        //FØRST MED PLAYERHÅND MED BLACKJACK
        gameLogic.givePlayerABlackJackHand();
        gameLogic.hitDealer();
        gameLogic.upDateWinnerAsInt();
        //PlayerBJ mot annen hånd skal gi 1
        assertEquals(1,gameLogic.getWinnerAsInt());
        gameLogic.giveDealerABlackJackHand();
        gameLogic.upDateWinnerAsInt();
        //BJ mot BJ skal gi 0
        assertEquals(0, gameLogic.getWinnerAsInt());
        gameLogic.giveDealerTwentyOneHand();
        gameLogic.upDateWinnerAsInt();
        //BJ mot 21 skal gi 1
        assertEquals(1,gameLogic.getWinnerAsInt());
        gameLogic.hitDealer();
        gameLogic.upDateWinnerAsInt();
        //fortsatt 1 hvis dealer buster
        assertEquals(1,gameLogic.getWinnerAsInt());

        //DERETTER MED PLAYERHÅND MED 21
        gameLogic.givePlayerTwentyOneHand();
        gameLogic.eraseDealerHand();
        gameLogic.hitDealer();
        gameLogic.upDateWinnerAsInt();
        //21 mot annen hånd under 21 skal gi 1
        assertEquals(1,gameLogic.getWinnerAsInt());
        gameLogic.giveDealerTwentyOneHand();
        gameLogic.upDateWinnerAsInt();
        //21 mot 21 skal gi 0
        assertEquals(0,gameLogic.getWinnerAsInt());
        gameLogic.giveDealerABlackJackHand();
        gameLogic.upDateWinnerAsInt();
        //21 mot BJ skal gi -1
        assertEquals(-1, gameLogic.getWinnerAsInt());
        gameLogic.giveDealerTwentyOneHand();
        gameLogic.hitDealer();
        gameLogic.upDateWinnerAsInt();
        assertEquals(1, gameLogic.getWinnerAsInt());

        //PLAYERHÅND MED EN VANLIG VERDI
        gameLogic.erasePlayerHand();
        gameLogic.hitPlayer();
        gameLogic.giveDealerATwelvie();
        gameLogic.upDateWinnerAsInt();
        //alle singelhands er mindre enn 12, altså skal winnerAsInt være -1
        assertEquals(-1, gameLogic.getWinnerAsInt());
        gameLogic.giveDealerABlackJackHand();
        gameLogic.upDateWinnerAsInt();
        //singelhand mot en BJ gir -1
        assertEquals(-1,gameLogic.getWinnerAsInt());

        gameLogic.giveDealerTwentyOneHand();
        gameLogic.upDateWinnerAsInt();
        //singelhand mot 21 gir -1
        assertEquals(-1, gameLogic.getWinnerAsInt());
        gameLogic.hitDealer();
        gameLogic.upDateWinnerAsInt();
        //singelhand mot en bust gir 1
        assertEquals(1,gameLogic.getWinnerAsInt());

        //PLAYERHÅND MED BUST

        gameLogic.givePlayerTwentyOneHand();
        gameLogic.hitPlayer();
        gameLogic.hitDealer();
        gameLogic.upDateWinnerAsInt();
        //bust mot en vanlig hånd gir -1
        assertEquals(-1, gameLogic.getWinnerAsInt());
        gameLogic.giveDealerABlackJackHand();
        gameLogic.upDateWinnerAsInt();
        //bust mot BJ gir -1
        assertEquals(-1,gameLogic.getWinnerAsInt());
        gameLogic.giveDealerTwentyOneHand();
        gameLogic.upDateWinnerAsInt();
        //bust mot 21 gir -1
        assertEquals(-1, gameLogic.getWinnerAsInt());
        gameLogic.hitDealer();
        //bust mot bust gir -1. Dette er fordi dealeren ikke trenger å hitte hvis player buster. Det er altså i prinsippet
        //ikke mulig at player og dealer buster begge to. 
        assertEquals(-1, gameLogic.getWinnerAsInt());


    }

    @Test
    @DisplayName("testing av oppstart av ny betting-runde")
    public void testNewBettingRound(){
        gameLogic.cleanUpAfterGame();
        gameLogic.addBet(20);
        gameLogic.givePlayerABlackJackHand();
        gameLogic.hitDealer();
        gameLogic.upDateWinnerAsInt();
        gameLogic.newBettingRound();
        //test at betales 3/1 når BlackJack
        assertEquals(140.0, gameLogic.getPlayerBalance());
        assertEquals(0.0, gameLogic.getPlayerCurrentBet());
        //sjekk at korthendene er tomme
        assertEquals(0,gameLogic.getCurrentDealerHandAsStringArray().length);
        assertEquals(0, gameLogic.getCurrentPlayerHandAsStringArray().length);
        gameLogic.cleanUpAfterGame();
        gameLogic.addBet(20);
        gameLogic.giveDealerTwentyOneHand();
        gameLogic.hitDealer();
        gameLogic.upDateWinnerAsInt();
        gameLogic.newBettingRound();
        //test vanlig tilbakebetaling
        assertEquals(120.0, gameLogic.getPlayerBalance());
        assertEquals(0.0, gameLogic.getPlayerCurrentBet());
        assertTrue(!gameLogic.getIsPlayerOutOfMoney());
        gameLogic.cleanUpAfterGame();
        gameLogic.addBet(20);
        gameLogic.giveDealerABlackJackHand();
        gameLogic.hitPlayer();
        gameLogic.upDateWinnerAsInt();
        gameLogic.newBettingRound();
        //test ved tap
        assertEquals(80.0, gameLogic.getPlayerBalance());
        assertEquals(0.0, gameLogic.getPlayerCurrentBet());
        assertTrue(!gameLogic.getIsPlayerOutOfMoney());
        gameLogic.cleanUpAfterGame();
        gameLogic.addBet(100.0);
        gameLogic.giveDealerABlackJackHand();
        gameLogic.hitPlayer();
        gameLogic.upDateWinnerAsInt();
        gameLogic.newBettingRound();
        assertEquals(0.0, gameLogic.getPlayerBalance());
        assertEquals(0.0,gameLogic.getPlayerCurrentBet());
        assertTrue(gameLogic.getIsPlayerOutOfMoney());


    }

}
