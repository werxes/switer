package switer.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import switer.domains.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	List<Comment> findByText(String text);

	// @Query(value = "SELECT id, text FROM comment WHERE id = :id", nativeQuery =
	// true)
	// List<Comment> findByNativeQuery(@Param("id") Long id);

	@Modifying
	@Query("SELECT c FROM Comment c WHERE c.id = :id")
	List<Comment> findByNativeQuery(@Param("id") Long id);

	@Modifying
	@Transactional
	@Query(value = "update comment set text = ? where id = ?", nativeQuery = true)
	int updateCommentNative(@Param("text") String text, @Param("id") Long id);

	@Modifying
	@Transactional
	@Query(value = "delete from comment where id = ?", nativeQuery = true)
	void deleteCommentNative(@Param("id") Long id);

}
