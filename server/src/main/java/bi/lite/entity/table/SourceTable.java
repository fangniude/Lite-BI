package bi.lite.entity.table;

import static cn.hutool.core.text.CharSequenceUtil.isBlank;

import bi.lite.entity.Source;
import bi.lite.entity.Table;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Comment;

/**
 * @author lipengpeng
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@DiscriminatorValue("source")
public class SourceTable extends Table {

    @ManyToOne
    @JoinColumn(name = "source_id")
    @Comment("数据源ID")
    private Source source;

    @Comment("表结构中的备注")
    private String remarks;

    public SourceTable(Source source, org.jooq.Table<?> t) {
        super(t.getName(), fetchName(t), false, getColumns(t));
        this.source = source;
        this.remarks = t.getComment();
    }

    private static List<Column> getColumns(org.jooq.Table<?> t) {
        return Arrays.stream(t.fields()).map(Column::new).toList();
    }

    private static String fetchName(org.jooq.Table<?> t) {
        return isBlank(t.getComment()) ? t.getName() : t.getComment();
    }

    public SourceTable merge(SourceTable sourceTable) {
        // 1. name
        if (Objects.equals(super.getName(), this.remarks)
            || Objects.equals(super.getName(), super.getCode())) {
            super.setName(sourceTable.getName());
        }

        // 2. remarks
        this.remarks = sourceTable.getRemarks();

        // 3. columns
        final Map<String, Column> dbColumns = this.getColumns().stream()
            .collect(Collectors.toMap(Column::getCode, Function.identity()));
        final List<Column> columns = sourceTable.getColumns().stream()
            .map(c -> c.merge(dbColumns.get(c.getCode())))
            .toList();
        super.setColumns(columns);

        return this;
    }
}
