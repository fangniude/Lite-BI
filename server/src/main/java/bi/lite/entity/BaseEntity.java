package bi.lite.entity;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

/**
 * @author lipengpeng
 */
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@MappedSuperclass
public class BaseEntity extends BaEntity {

    @CreatedBy
    @ManyToOne
    @JoinColumn(name = "created_by_id")
    @Comment("创建人")
    private User creator;

    @LastModifiedBy
    @ManyToOne
    @JoinColumn(name = "last_modified_by_id")
    @Comment("最后更新人")
    private User lastModifier;
}
