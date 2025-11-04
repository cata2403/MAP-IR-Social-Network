import com.ubb.domain.entities.Friendship;
import com.ubb.domain.entities.Message;
import com.ubb.domain.entities.Person;
import com.ubb.domain.entity_types.FriendRequest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

public class DomainTest{
    @Test
    public void personObjectTesting(){
        Person person = new Person(1234L,"1","2","3");
        assertEquals(1234L,person.getId());
        assertEquals("1", person.getUsername());
        assertEquals("2", person.getPassword());
        assertEquals("3", person.getEmail());
        person.setFirstName("A");
        assertEquals("A", person.getFirstName());
        person.setLastName("B");
        assertEquals("B", person.getLastName());
        person.setOccupation("C");
        assertEquals("C", person.getOccupation());
        LocalDate date = LocalDate.now();
        person.setDateOfBirth(date);
        assertEquals(date, person.getDateOfBirth());
        person.setFirstName("Z").setLastName("y");
        assertEquals("Z", person.getFirstName());
        assertEquals("y", person.getLastName());
    }

    @Test
    public void friendshipObjectTesting(){
        Friendship friendship = new Friendship(1234L,1L,2L, FriendRequest.ACCEPTED);
        assertEquals(1234L,friendship.getId());
        assertEquals(1L,friendship.getIdUser1());
        assertEquals(2L,friendship.getIdUser2());
        assertEquals(FriendRequest.ACCEPTED,friendship.getStatus());
    }

    @Test
    public void messageObjectTesting(){
        Message message = new Message(1L,2L,3L,"A");
        assertEquals(1L,message.getId());
        assertEquals("A",message.getMessage());
        assertEquals(2L,message.getIdSender());
        assertEquals(3L,message.getIdReceiver());
        message.setId(2L);
        assertEquals(2L,message.getId());
    }

    @Test
    public void factoryUserCreation(){

    }

}
