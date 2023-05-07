package bi.lite.service;

import bi.lite.entity.Tenant;
import bi.lite.repository.TenantRepository;
import org.springframework.stereotype.Service;

/**
 * @author lipengpeng
 */
@Service
public class TenantService extends BaseService<Tenant> {

    private final TenantRepository repository;

    public TenantService(TenantRepository repository) {
        super(repository);
        this.repository = repository;
    }
}
