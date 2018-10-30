package switer.repos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import switer.domains.Message;
import switer.domains.User;

public interface MessageRepository extends JpaRepository<Message, Long> {
	Page<Message> findAll(Pageable pageable);

	Page<Message> findByTag(String tag, Pageable pageable);

	@Query("from Message m where m.author = :author")
	Page<Message> findbyUser(Pageable pageable, @Param("author") User author);
}
