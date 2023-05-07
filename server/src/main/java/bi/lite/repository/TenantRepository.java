package bi.lite.repository;

import bi.lite.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author lipengpeng
 */
public interface TenantRepository extends JpaRepository<Tenant, Long> {

}
