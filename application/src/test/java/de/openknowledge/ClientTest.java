package de.openknowledge;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClientTest {

    @Test
    public void testGetter_getsFirstName() {
        //given
        Client testClient = new Client("Hans", "Bauer");

        //when
        String resultFirstName = "Hans";

        //then
        assertEquals(testClient.getFirstName(), resultFirstName);
    }

    @Test
    public void testGetter_getsLastName() {

        Client testClient = new Client("Hans", "Bauer");

        String resultLastName = "Bauer";

        assertEquals(testClient.getLastName(), resultLastName);

    }

}