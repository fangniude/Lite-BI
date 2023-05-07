package bi.lite.entity;


import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.TenantId;

/**
 * @author lipengpeng
 */
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@MappedSuperclass
public class BusinessEntity extends BaseEntity {

    @TenantId
    @Comment("租户编码")
    private String tenantCode;
}
