package bi.lite.entity.table;

import bi.lite.entity.Table;
import com.vladmihalcea.hibernate.type.json.JsonType;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.Type;

/**
 * @author lipengpeng
 */
@SuppressFBWarnings("EI_EXPOSE_REP")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@DiscriminatorValue("custom")
public class CustomTable extends Table {

    @Type(JsonType.class)
    @jakarta.persistence.Column(columnDefinition = "json")
    @Comment("处理步骤")
    private List<Step> steps;


    public interface Step {

    }
}
