package bi.lite.config;

import bi.lite.entity.User;
import java.util.Optional;
import lombok.NonNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

/**
 * @author lipengpeng
 */
@Configuration
public class UserAuditor implements AuditorAware<User> {

    private final User defaultUser;

    public UserAuditor() {
        // 前期不做用户，用默认的 admin
        defaultUser = new User();
        defaultUser.setId(1L);
    }

    @NonNull
    @Override
    public Optional<User> getCurrentAuditor() {
        return Optional.of(defaultUser);
    }
}
