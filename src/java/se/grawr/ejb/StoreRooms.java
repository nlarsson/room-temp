package se.grawr.ejb;

import java.util.List;
import se.grawr.entity.Room;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class StoreRooms {

    @PersistenceContext
    private EntityManager em;

    public StoreRooms() {
        
    }
    
    public void store(Room room) {
        //Persistence
        em.persist(room);
    }

    public boolean isValidRoom(Room room) {
        switch (room.getRoomName()) {
            case "room1":
            case "room2":
                return true;
            default:
                return false;
        }
    }

    //Get the values for a room
    public List<Room> getRoom(Room room) {
        List<Room> roomValues =  em.createNamedQuery("getRoom")
                                .setParameter("roomName", room.getRoomName())
                                .getResultList();
        return roomValues;
    }
}
