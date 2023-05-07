package bi.lite.service;

import bi.lite.entity.Source;
import bi.lite.entity.Table;
import bi.lite.entity.table.SourceTable;
import bi.lite.repository.SourceRepository;
import bi.lite.util.CodeUtil;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

/**
 * @author lipengpeng
 */
@Service
public class SourceService extends BaseService<Source> {

    private final SourceRepository repository;

    public SourceService(SourceRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Source save(Source entity) {
        if (entity.getId() == null) {
            // 如果是新增，则需要生成编码
            entity.setCode(nextCode());
            // 要加flush，否则没有租户
            final Source added = super.saveAndFlush(entity);
            added.mount();
            return added;
        } else {
            final Source updated = super.save(entity);
            updated.remount();
            return updated;
        }
    }

    /**
     * 数据源编码：租户内唯一的
     *
     * @return 数据源编码
     */
    private String nextCode() {
        while (true) {
            final String sourceCode = CodeUtil.sourceCode();
            // 为了防止冲突，需要校验数据库中是否存在
            final Source source = new Source();
            source.setCode(sourceCode);
            if (!super.exists(Example.of(source))) {
                // 不存在，可以使用
                return sourceCode;
            } // 存在了，再生成一个
        }
    }

    @Override
    public void deleteById(Long id) {
        final Optional<Source> optional = this.findById(id);
        optional.ifPresent(source -> {
            final List<SourceTable> tables = source.getTables();
            // todo check table has been used
            source.unmount();
            super.deleteById(id);
        });
    }

    public void refresh(long id) {
        final Optional<Source> optional = this.findById(id);
        optional.ifPresent(Source::remount);
    }

    public Flux<Table> getTablesBySourceId(long sourceId) {
        final Optional<Source> optional = this.findById(sourceId);
        return optional.map(s -> Flux.fromIterable(s.getTables())
                .map(Table.class::cast))
            .orElse(Flux.empty());
    }
}
