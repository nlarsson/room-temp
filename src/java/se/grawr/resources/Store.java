package se.grawr.resources;

import se.grawr.ejb.StoreRooms;
import se.grawr.entity.Room;

import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.json.Json;
import javax.json.stream.JsonParser;
import javax.ws.rs.Consumes;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("/store")
public class Store {

    private static final Logger logger
            = Logger.getLogger("se.grawr.resources.Store");
    @EJB
    private StoreRooms sr;

    public Store() {
        sr = new StoreRooms();
    }
    
    //Make use of POST so JSON can be sent directly to the app
    @POST
    @Consumes("application/json")
    public void setMsg(String msg) {
        logger.log(Level.INFO, "STORE POST msg: {0}", msg);
        //extract json and create a room object
        StringReader reader = new StringReader(msg);
        JsonParser parser = Json.createParser(reader);
        Room room = new Room();
        for (JsonParser.Event event = parser.next(); parser.hasNext(); event = parser.next()) {
            if (event.equals(JsonParser.Event.KEY_NAME)) {
                switch (parser.getString()) {
                    case "room":
                        //move to the value
                        parser.next();
                        room.setRoomName(parser.getString());
                        break;
                    case "temp":
                        //move to the value
                        parser.next();
                        room.setTemperature(parser.getInt());
                        break;
                    default:
                        //Don't do anything for now
                }
            }
        }
        if (sr.isValidRoom(room)) {
            logger.log(Level.INFO, "STORE Got a valid room {0}", room.getRoomName());
            //Store the room data
            sr.store(room);
        }
    }
}
