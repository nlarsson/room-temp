package se.grawr.resources;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import se.grawr.entity.Room;
import se.grawr.ejb.StoreRooms;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonArrayBuilder;
import javax.json.JsonException;
import javax.json.JsonWriter;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 * REST Web Service
 *
 */
@Path("/get")
public class Fetch {

    private static final Logger logger
            = Logger.getLogger("se.grawr.resources.Store");
    @EJB
    private StoreRooms sr;

    public Fetch() {
        sr = new StoreRooms();
    }

    @GET
    @Produces("text/plain")
    public String getFirstPage() {
        return "Webapi is up and running.";
    }
    
    
    @GET @Path("/{room}")
    @Produces("text/plain")
    public String getRoom(@PathParam("room") String roomName) {
        logger.log(Level.INFO, "FETCH Room: {0}", roomName);
        //Room here is just used for a placeholder for the room name
        Room room = new Room(roomName, 0);
        
        if (sr.isValidRoom(room)) {
            List roomList = sr.getRoom(room);
            logger.log(Level.INFO, "FETCH list size: {0}", "" + roomList.size());
            
            JsonArrayBuilder valuesArray = Json.createArrayBuilder();
            for (Object temp : roomList) {
                Room tempRoom = (Room) temp;
                valuesArray.add(Json.createObjectBuilder()
                        .add("room", tempRoom.getRoomName())
                        .add("temp", tempRoom.getTemperature())
                );
            }
            JsonObject obj = Json.createObjectBuilder()
                    .add("values", valuesArray)
                    .build();
            
            String output = "";
            try {
                StringWriter sw = new StringWriter();
                JsonWriter writer = Json.createWriter(sw);
                writer.writeObject(obj);
                writer.close();

                output = sw.toString();
                sw.close();
            } catch (JsonException | IOException | IllegalStateException e) {
                //Handle errors
            }
            
            return output;
        }
        // If the room specified isn't valid; just return a empty JSON object
        return "{}";
    }
}
