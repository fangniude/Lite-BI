package bi.lite.service;

import bi.lite.entity.Table;
import bi.lite.repository.TableRepository;
import org.springframework.stereotype.Service;

/**
 * @author lipengpeng
 */
@Service
public class TableService extends BaseService<Table> {

    private final TableRepository repository;

    public TableService(TableRepository repository) {
        super(repository);
        this.repository = repository;
    }

}
