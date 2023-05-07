package bi.lite.controller;

import bi.lite.entity.Source;
import bi.lite.entity.Table;
import bi.lite.service.SourceService;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author lipengpeng
 */
@RestController
@RequestMapping("/sources")
public class SourceController {

    private final SourceService service;

    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public SourceController(SourceService service) {
        this.service = service;
    }

    /**
     * 查询所有数据源，按照最后修改时间倒序
     *
     * @return 所有数据源
     */
    @GetMapping("")
    public Flux<Source> findAll() {
        return Flux.fromIterable(this.service.findAll());
    }

    /**
     * 根据 ID 查询数据源详情
     *
     * @param id ID
     * @return 详情
     */
    @GetMapping("/{id}")
    public Mono<Source> getById(@PathVariable long id) {
        return Mono.justOrEmpty(this.service.findById(id));
    }

    /**
     * 创建数据源，会同步挂载数据源下面的表
     *
     * @param source 数据源
     * @return 数据源
     */
    @PostMapping("")
    public Mono<Source> add(@RequestBody Source source) {
        return Mono.justOrEmpty(this.service.save(source));
    }

    /**
     * 修改数据源，会同步重新挂载数据源下面的表
     *
     * @param id     数据源 ID
     * @param source 数据源
     * @return 数据源
     */
    @PutMapping("/{id}")
    public Mono<Source> modify(@PathVariable long id, @RequestBody Source source) {
        source.setId(id);
        return Mono.justOrEmpty(this.service.save(source));
    }

    /**
     * 【危险操作】删除数据源，同步删除数据源下面的表，如果下面的表已经被使用，则不可删除
     *
     * @param id 数据源 ID
     * @return 空
     */
    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable long id) {
        this.service.deleteById(id);
        return Mono.empty();
    }

    /**
     * 刷新数据源，重新挂载下面的表
     *
     * @param id 数据源 ID
     * @return 数据源
     */
    @PutMapping("/{id}/refresh")
    public Mono<Void> refresh(@PathVariable long id) {
        this.service.refresh(id);
        return Mono.empty();
    }

    /**
     * 查询数据源下面的所有表
     *
     * @param sourceId 数据源 ID
     * @return 所有的表
     */
    @GetMapping("/{sourceId}/tables")
    public Flux<Table> getTablesBySourceId(@PathVariable long sourceId) {
        return this.service.getTablesBySourceId(sourceId);
    }

}
