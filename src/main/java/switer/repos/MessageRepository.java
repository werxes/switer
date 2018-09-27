package switer.repos;

import org.springframework.data.repository.CrudRepository;
import switer.domains.Message;

import java.util.List;
public interface MessageRepository extends CrudRepository<Message, Integer>
{
    List<Message> findByTag(String tag);
}
