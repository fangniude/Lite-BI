package bi.lite.controller;

import bi.lite.entity.Table;
import bi.lite.service.TableService;
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
@RequestMapping("/tables")
public class TableController {

    private final TableService service;

    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public TableController(TableService service) {
        this.service = service;
    }

    @GetMapping("")
    public Flux<Table> findAll() {
        return Flux.fromIterable(this.service.findAll());
    }

    @GetMapping("/{id}")
    public Mono<Table> getById(@PathVariable long id) {
        return Mono.justOrEmpty(this.service.findById(id));
    }

    @PostMapping("")
    public Mono<Table> add(@RequestBody Table table) {
        return Mono.justOrEmpty(this.service.save(table));
    }

    @PutMapping("/{id}")
    public Mono<Table> modify(@PathVariable long id, @RequestBody Table table) {
        table.setId(id);
        return Mono.justOrEmpty(this.service.save(table));
    }

    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable long id) {
        this.service.deleteById(id);
        return Mono.empty();
    }

}
