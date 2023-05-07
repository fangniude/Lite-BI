package bi.lite.repository;

import bi.lite.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author lipengpeng
 */
public interface UserRepository extends JpaRepository<User, Long> {

}
