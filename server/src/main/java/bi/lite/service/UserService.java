package bi.lite.service;

import bi.lite.entity.User;
import bi.lite.repository.UserRepository;
import org.springframework.stereotype.Service;

/**
 * @author lipengpeng
 */
@Service
public class UserService extends BaseService<User> {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        super(repository);
        this.repository = repository;
    }
}
