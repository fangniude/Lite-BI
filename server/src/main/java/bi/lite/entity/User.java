package bi.lite.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

/**
 * @author lipengpeng
 */
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@Table(indexes = @Index(name = "uk_user_account", columnList = "account", unique = true))
@SQLDelete(sql = "UPDATE user SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
@Comment("用户")
public class User extends BaEntity {

    @Comment("账号")
    private String account;

    @Comment("名称")
    private String name;

    @Comment("邮箱")
    private String email;

    @Comment("密码")
    private String password;

    @Comment("盐")
    private String salt;

    @Comment("删除标记")
    private Boolean deleted = Boolean.FALSE;
}
