import com.ubb.domain.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;

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
        LocalDateTime date = LocalDateTime.now();
        person.setDateOfBirth(date);
        assertEquals(date, person.getDateOfBirth());
        person.setFirstName("Z").setLastName("y");
        assertEquals("Z", person.getFirstName());
        assertEquals("y", person.getLastName());
    }

    @Test
    public void duckObjectTesting(){
        Duck duck = new Duck(1234L,"1","2","3");
        assertEquals(1234L,duck.getId());
        assertEquals("1", duck.getUsername());
        assertEquals("2", duck.getPassword());
        assertEquals("3", duck.getEmail());
        duck.setResistance(12.12d).setSpeed(13.13d);
        assertEquals(12.12d, duck.getResistance());
        assertEquals(13.13d, duck.getSpeed());
        duck.setDuckType(DuckType.FLYING);
        assertEquals(DuckType.FLYING, duck.getDuckType());
        duck.setFlockId(123L);
        assertEquals(123L, duck.getFlockId());
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
    public void flockObjectTesting(){
        Flock flock = new Flock(1L,"a");
        assertEquals(1L,flock.getId());
        assertEquals("a",flock.getFlockName());
        flock.addDuck((new Duck(2L,"A","B","C")).setResistance(12.12d).setSpeed(13.13d));
        flock.addDuck((new Duck(3L,"A","B","C")).setResistance(14.14d).setSpeed(15.15d));
        assertEquals(14.14D,flock.getAveragePerformance());
        flock.removeDuck(2L);
        assertEquals(15.15D,flock.getAveragePerformance());
    }

    @Test
    public void factoryUserCreation(){

    }

}
