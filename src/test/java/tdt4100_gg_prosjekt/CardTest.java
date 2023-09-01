package tdt4100_gg_prosjekt;


import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import tdt4100_gg_prosjekt.Card;



public class CardTest {
    
    @Test
    public void testConstructor(){
        Card card;
        for (int i = 1 ; i < 14; i++) {
            for (Character c : new Character[] {'S','H','D','C'}) {
                card = new Card(i,c);

                //tester gettere
                assertEquals(i, card.getValue());
                assertEquals(c, card.getSuit());

                //tester når i blir veldig stor
                assertThrows(IllegalArgumentException.class, () -> new Card(1000, 'S'));
                assertThrows(IllegalArgumentException.class, () -> new Card(-1000, 'S'));
                //tester når en ugyldig char benyttes
                assertThrows(IllegalArgumentException.class, () -> new Card(5, 'G'));
                //edge cases
                assertThrows(IllegalArgumentException.class, () -> new Card(0,'S'));
                assertThrows(IllegalArgumentException.class, () -> new Card(14,'S'));

            }
        }
    }

    @Test
    @DisplayName("Private felter")
    public void testPrivateFields(){
        for (Field field : Card.class.getFields()) {
            assertTrue(Modifier.isPrivate(field.getModifiers()));
        }
    }

    @Test
    @DisplayName("toString-metoden vår")
    public void testToString(){
        assertEquals("S4", new Card(4,'S').toString());
        assertEquals("H7", new Card(7, 'H').toString());
    }


    @Test
    @DisplayName("BlackJack-verdi")
    public void testGetBlackJackValue(){

        //disse sjekker for billedkort
        assertEquals(10, new Card(13, 'H').getBlackJackValue());
        assertEquals(10, new Card(12, 'H').getBlackJackValue());
        assertEquals(10, new Card(11, 'H').getBlackJackValue());
        assertEquals(11, new Card(1, 'H').getBlackJackValue());

        //denne sjekker for øvrige verdier
        for (int i = 2 ; i < 10; i++) {
            Card card = new Card(i, 'S');
            assertEquals(i, card.getBlackJackValue());
        }
    }
}
