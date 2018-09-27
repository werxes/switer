package switer.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import switer.domains.User;

public interface UserRepository extends JpaRepository<User, Long>
{
    User findByUsername(String usrename);
}
