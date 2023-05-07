package bi.lite.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Comment;

/**
 * @author lipengpeng
 */
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@Entity
@Table(indexes = @Index(name = "uk_tenant_code", columnList = "code", unique = true))
@Comment("租户")
public class Tenant extends BaseEntity {

    @Comment("编码")
    private String code;

    @Comment("名称")
    private String name;
}
