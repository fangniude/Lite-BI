package bi.lite.entity;

import static cn.hutool.core.text.CharSequenceUtil.isNotBlank;

import bi.lite.entity.table.CustomTable;
import bi.lite.entity.table.SourceTable;
import bi.lite.enums.DataType;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.vladmihalcea.hibernate.type.json.JsonType;
import edu.umd.cs.findbugs.annotations.Nullable;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.Type;
import org.jooq.Field;

/**
 * @author lipengpeng
 */
@SuppressFBWarnings("EI_EXPOSE_REP")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@jakarta.persistence.Table(indexes = @Index(name = "uk_table_code", columnList = "tenantCode,code", unique = true))
@DiscriminatorColumn(name = "table_type")
@Comment("数据表")
@JsonTypeInfo(use = Id.NAME, include = As.EXISTING_PROPERTY, property = "tableType", visible = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = SourceTable.class, name = "source"),
    @JsonSubTypes.Type(value = CustomTable.class, name = "custom")
})
public class Table extends BusinessEntity {

    /**
     * 租户内，表的唯一标识
     * <p></p>
     * 数据源表：源编码 + 表名
     * <p></p>
     * 自定义表：程序生成
     */
    @Comment("表编码")
    private String code;

    /**
     * 表显示的名称
     */
    @Comment("表显示名称")
    private String name;

    @Comment("是否缓存")
    private Boolean cache = false;

    @Type(JsonType.class)
    @jakarta.persistence.Column(columnDefinition = "json")
    @Comment("所有列")
    private List<Column> columns;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    public static class Column {

        private String code;
        private String name;
        private DataType dataType;
        private String remarks;

        public Column(@NotNull Field<?> field) {
            this.code = field.getName();
            this.name = isNotBlank(field.getComment()) ? field.getComment() : field.getName();
            this.dataType = DataType.valueOf(field.getDataType());
            this.remarks = field.getComment();
        }

        public Column merge(@Nullable Column c) {
            if (c != null) {
                if (!Objects.equals(c.getName(), c.getCode())
                    && !Objects.equals(c.getName(), c.getRemarks())) {
                    this.name = c.getName();
                }
            }
            return this;
        }
    }
}
