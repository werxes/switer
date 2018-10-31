package switer.repos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import switer.domains.Message;
import switer.domains.User;
import switer.domains.dto.MessageDto;

public interface MessageRepository extends JpaRepository<Message, Long> {
	@Query("select new switer.domains.dto.MessageDto(" + "   m, " + "   count(ml), "
			+ "   sum(case when ml = :user then 1 else 0 end) > 0" + ") " + "from Message m left join m.likes ml "
			+ "group by m")
	Page<MessageDto> findAll(Pageable pageable, @Param("user") User user);

	@Query("select new switer.domains.dto.MessageDto(" + "   m, " + "   count(ml), "
			+ "   sum(case when ml = :user then 1 else 0 end) > 0" + ") " + "from Message m left join m.likes ml "
			+ "where m.tag = :tag " + "group by m")
	Page<MessageDto> findByTag(@Param("tag") String tag, Pageable pageable, @Param("user") User user);

	@Query("select new switer.domains.dto.MessageDto(" + "   m, " + "   count(ml), "
			+ "   sum(case when ml = :user then 1 else 0 end) > 0" + ") " + "from Message m left join m.likes ml "
			+ "where m.author = :author " + "group by m")
	Page<MessageDto> findByUser(Pageable pageable, @Param("author") User author, @Param("user") User user);
}
