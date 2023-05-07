package bi.lite.entity.source;

import bi.lite.entity.Source;
import bi.lite.entity.Table;
import bi.lite.entity.table.SourceTable;
import bi.lite.service.TableService;
import bi.lite.util.DslUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.spring.SpringUtil;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Comment;
import org.jooq.CloseableDSLContext;
import org.jooq.Meta;
import org.jooq.Name;
import org.jooq.Schema;
import org.jooq.impl.DSL;

/**
 * @author lipengpeng
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@DiscriminatorValue("Mysql")
public class MysqlSource extends Source {

    @Comment("主机名")
    private String host;

    @Comment("端口号")
    private String port;

    @Comment("数据库名称")
    private String database;

    @Comment("用户名")
    private String username;

    @Comment("密码")
    private String password;

    @Override
    public String getSourceType() {
        return "Mysql";
    }

    @Override
    public String jdbcUrl() {
        return MessageFormat.format(
            "jdbc:mysql://{0}:{1}/{2}?user={3}&password={4}&characterEncoding=UTF-8&serverTimezone=UTC&useUnicode=true",
            this.host, this.port, this.database, this.username,
            null == this.password ? "" : this.password
        );
    }

    @SuppressFBWarnings("RCN_REDUNDANT_NULLCHECK_WOULD_HAVE_BEEN_A_NPE")
    @Override
    public void mount() {
        // 1. 创建schema
        super.createSourceSchemaIfNotExists();

        // 2. 创建 foreign server
        final String fsName = super.foreignServerName();
        final Name name = DSL.name(fsName);
        DslUtil.dslContext().execute("DROP SERVER IF EXISTS {0} CASCADE", name);
        DslUtil.dslContext()
            .execute("CREATE SERVER {0} FOREIGN DATA WRAPPER mysql_fdw OPTIONS (host {1}, port {2})",
                name, this.host, this.port);

        // 3. 创建用户mapping
        DslUtil.dslContext()
            .execute("CREATE USER MAPPING FOR postgres SERVER {0} OPTIONS (username {1}, password {2})",
                name, username, password);

        // 4. 导入整个 Schema
        DslUtil.dslContext()
            .execute("IMPORT FOREIGN SCHEMA {0} FROM SERVER {1} INTO {2}",
                DSL.name(this.database), name, DSL.name(super.localSchema()));

        // 5. 创建表的元数据
        try (final CloseableDSLContext using = DSL.using(this.jdbcUrl())) {
            final Meta meta = using.meta();
            final List<Schema> schemas = meta.getSchemas(DSL.name(this.database));
            if (!CollUtil.isEmpty(schemas)) {
                final Schema schema = schemas.get(0);
                final List<org.jooq.Table<?>> tables = schema.getTables();
                final List<Table> sourceTables = tables.stream()
                    .map(t -> new SourceTable(this, t))
                    .map(Table.class::cast)
                    .toList();
                SpringUtil.getBean(TableService.class).saveAll(sourceTables);
            }
        }
    }

    @Override
    public void unmount() {
        // 1. 删除所有的元数据
        SpringUtil.getBean(TableService.class).deleteAll(this.getTables());

        // 2. 删除 schema，含其中所有的表
        DslUtil.dslContext().dropSchemaIfExists(DSL.name(super.localSchema())).cascade().execute();

        // 3. 删除用户 mapping
        final Name fs = DSL.name(super.foreignServerName());
        DslUtil.dslContext().execute("DROP USER MAPPING IF EXISTS FOR postgres SERVER {0}", fs);

        // 4. 删除 foreign server
        DslUtil.dslContext().execute("DROP SERVER IF EXISTS {0}", fs);
    }

    @Override
    public void remount() {
        // 1. 删除 Schema
        DslUtil.dslContext().dropSchemaIfExists(DSL.name(super.localSchema())).cascade().execute();

        // 2. 重新创建 Schema
        super.createSourceSchemaIfNotExists();

        // 3. 导入整个 Schema
        DslUtil.dslContext()
            .execute("IMPORT FOREIGN SCHEMA {0} FROM SERVER {1} INTO {2}",
                DSL.name(this.database), DSL.name(super.foreignServerName()), DSL.name(super.localSchema()));

        // 4. 更新元数据
        try (final CloseableDSLContext using = DSL.using(this.jdbcUrl())) {
            final Meta meta = using.meta();
            final List<Schema> schemas = meta.getSchemas(DSL.name(this.database));
            if (!CollUtil.isEmpty(schemas)) {
                final Schema schema = schemas.get(0);
                final List<org.jooq.Table<?>> tables = schema.getTables();
                final Map<String, SourceTable> sourceTables = tables.stream()
                    .map(t -> new SourceTable(this, t))
                    .collect(Collectors.toMap(SourceTable::getCode, Function.identity()));

                final Map<String, SourceTable> dbTables = this.getTables().stream()
                    .collect(Collectors.toMap(SourceTable::getCode, Function.identity()));

                // 1. 数据源中已经删除的，则删除元数据
                final List<Long> deletedIds = dbTables.values().stream()
                    .filter(t -> !sourceTables.containsKey(t.getCode()))
                    .map(SourceTable::getId)
                    .toList();
                if (CollUtil.isNotEmpty(deletedIds)) {
                    SpringUtil.getBean(TableService.class).deleteAllByIdInBatch(deletedIds);
                }

                // 2. 数据源 和 数据库中都存在的，则更新
                final List<Table> updated = dbTables.values().stream()
                    .filter(t -> sourceTables.containsKey(t.getCode()))
                    .map(t -> t.merge(sourceTables.get(t.getCode())))
                    .map(Table.class::cast)
                    .toList();
                if (CollUtil.isNotEmpty(updated)) {
                    SpringUtil.getBean(TableService.class).saveAll(updated);
                }

                // 3. 数据源多出来的，则新增 元数据
                final List<Table> added = sourceTables.values().stream()
                    .filter(t -> !dbTables.containsKey(t.getCode()))
                    .map(Table.class::cast)
                    .toList();
                if (CollUtil.isNotEmpty(added)) {
                    SpringUtil.getBean(TableService.class).saveAll(added);
                }
            }
        }
    }
}
