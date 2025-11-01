import com.ubb.domain.FriendRequest;
import com.ubb.domain.Friendship;
import com.ubb.infrastructure.FriendshipFileSavingStrategy;
import com.ubb.repository.FileRepository;
import com.ubb.repository.Repository;
import org.junit.jupiter.api.Test;
import java.io.File;
import static org.junit.jupiter.api.Assertions.*;

public class RepositoryTest {
    @Test
    public void fileRepositoryCorrectness(){
        File file = new File("files/repo_test_friendship.csv");
        Repository<Long, Friendship> repo = new FileRepository<Long,Friendship>(file, new FriendshipFileSavingStrategy());

        Friendship f1 = repo.get(1L);
        assertEquals(1L, f1.getId());
        assertEquals(FriendRequest.ACCEPTED, f1.getStatus());
        assertEquals(3,repo.getAll().size());
        repo.delete(3L);
        try{
            repo.get(3L);
            fail();
        }catch(Exception e){
            assertEquals("<<Entity with id 3 does not exist>>",e.getMessage());
        }
        repo.add(new Friendship(3L,3L,3L,FriendRequest.SEND));
        try{
            repo.get(3L);
        }catch(Exception e){
            fail();
        }
        repo.update(new Friendship(3L,3L,3L,FriendRequest.REQUESTED));
        assertEquals(FriendRequest.REQUESTED,repo.get(3L).getStatus());
        repo.update(new Friendship(3L,3L,3L,FriendRequest.SEND));
    }
}
