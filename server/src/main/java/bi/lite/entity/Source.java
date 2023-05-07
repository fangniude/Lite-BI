package bi.lite.entity;

import bi.lite.entity.source.MysqlSource;
import bi.lite.entity.table.SourceTable;
import bi.lite.util.DslUtil;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Comment;
import org.jooq.impl.DSL;

/**
 * 不能用抽象类，因为查询时往往是查该租户下所有数据源，而不是某一数据库类型的
 *
 * @author lipengpeng
 */
@SuppressFBWarnings("EI_EXPOSE_REP")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(indexes = @Index(name = "uk_source_code", columnList = "tenantCode,code", unique = true))
@DiscriminatorColumn(name = "source_type")
@Comment("数据源")
@JsonTypeInfo(use = Id.NAME, include = As.EXISTING_PROPERTY, property = "sourceType", visible = true)
@JsonSubTypes({@Type(value = MysqlSource.class, name = "Mysql")})
public class Source extends BusinessEntity {

    @Comment("编码")
    private String code;

    @Comment("名称")
    private String name;

    @OneToMany(targetEntity = SourceTable.class, mappedBy = "source")
    private List<SourceTable> tables;

    /**
     * 数据源类型
     *
     * @return 数据源类型
     */
    public String getSourceType() {
        throw new UnsupportedOperationException();
    }

    /**
     * 生成 JDBC URL
     *
     * @return URL
     */
    public String jdbcUrl() {
        throw new UnsupportedOperationException();
    }

    /**
     * 数据源挂载
     */
    public void mount() {
        throw new UnsupportedOperationException();
    }

    /**
     * 数据源反挂载
     */
    public void unmount() {
        throw new UnsupportedOperationException();
    }

    /**
     * 数据源重新挂载
     */
    public void remount() {
        throw new UnsupportedOperationException();
    }

    public String localSchema() {
        return String.format("%s_%s", super.getTenantCode(), this.code);
    }

    public void createSourceSchemaIfNotExists() {
        DslUtil.dslContext().createSchemaIfNotExists(DSL.name(localSchema())).execute();
    }

    protected String foreignServerName() {
        return String.format("fs_%s", this.code);
    }
}
