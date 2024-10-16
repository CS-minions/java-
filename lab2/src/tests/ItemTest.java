package tests;

import main.Item;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class ItemTest {

    // Test for Diving event
    @Test
    public void testGetNameDiving() {
        Item item = new Item("Diving", "Aquatic Center", "2024-08-10");
        assertEquals("Diving", item.getName());
    }

    @Test
    public void testGetVenueDiving() {
        Item item = new Item("Diving", "Aquatic Center", "2024-08-10");
        assertEquals("Aquatic Center", item.getVenue());
    }

    // Test for Table Tennis event
    @Test
    public void testGetNameTableTennis() {
        Item item = new Item("Table Tennis", "Ping Pong Hall", "2024-08-05");
        assertEquals("Table Tennis", item.getName());
    }

    @Test
    public void testGetVenueTableTennis() {
        Item item = new Item("Table Tennis", "Ping Pong Hall", "2024-08-05");
        assertEquals("Ping Pong Hall", item.getVenue());
    }

    // Test for Weightlifting event
    @Test
    public void testGetNameWeightlifting() {
        Item item = new Item("Weightlifting", "Lifting Arena", "2024-08-11");
        assertEquals("Weightlifting", item.getName());
    }

    @Test
    public void testGetVenueWeightlifting() {
        Item item = new Item("Weightlifting", "Lifting Arena", "2024-08-11");
        assertEquals("Lifting Arena", item.getVenue());
    }
}