package bi.lite.service;

import bi.lite.entity.Source;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.FluentQuery;

/**
 * @author lipengpeng
 */
public class BaseService<T> {

    private final JpaRepository<T, Long> repository;

    public BaseService(JpaRepository<T, Long> repository) {
        this.repository = repository;
    }


    public T save(T entity) {
        return this.repository.save(entity);
    }


    public Optional<T> findById(Long id) {
        return this.repository.findById(id);
    }


    public boolean existsById(Long id) {
        return this.repository.existsById(id);
    }


    public long count() {
        return this.repository.count();
    }

    public void deleteById(Long id) {
        this.repository.deleteById(id);
    }

    public void delete(T entity) {
        this.repository.delete(entity);
    }

    public void deleteAllById(Iterable<? extends Long> ids) {
        this.repository.deleteAllById(ids);
    }

    public void deleteAll(Iterable<? extends T> entities) {
        this.repository.deleteAll(entities);
    }

    public void deleteAll() {
        this.repository.deleteAll();
    }


    public List<T> saveAll(Iterable<T> entities) {
        return this.repository.saveAll(entities);
    }

    public List<T> findAll() {
        return this.repository.findAll(Sort.sort(Source.class).by(Source::getLastModifiedTime).descending());
    }

    public List<T> findAllById(Iterable<Long> ids) {
        return this.repository.findAllById(ids);
    }


    public Page<T> findAll(Pageable pageable) {
        return this.repository.findAll(pageable);
    }


    public List<T> findAll(Sort sort) {
        return this.repository.findAll(sort);
    }


    public Optional<T> findOne(Example<T> example) {
        return this.repository.findOne(example);
    }


    public Page<T> findAll(Example<T> example, Pageable pageable) {
        return this.repository.findAll(example, pageable);
    }

    public long count(Example<T> example) {
        return this.repository.count(example);
    }

    public boolean exists(Example<T> example) {
        return this.repository.exists(example);
    }

    public <S extends T, R> R findBy(Example<S> example,
        Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return this.repository.findBy(example, queryFunction);
    }


    public void flush() {
        this.repository.flush();
    }

    public T saveAndFlush(T entity) {
        return this.repository.saveAndFlush(entity);
    }

    public List<T> saveAllAndFlush(Iterable<T> entities) {
        return this.repository.saveAllAndFlush(entities);
    }

    public void deleteAllInBatch(Iterable<T> entities) {
        this.repository.deleteAllInBatch(entities);
    }

    public void deleteAllByIdInBatch(Iterable<Long> ids) {
        this.repository.deleteAllByIdInBatch(ids);
    }

    public void deleteAllInBatch() {
        this.repository.deleteAllInBatch();
    }

    public T getReferenceById(Long id) {
        return this.repository.getReferenceById(id);
    }


    public List<T> findAll(Example<T> example) {
        return this.repository.findAll(example);
    }

    public List<T> findAll(Example<T> example, Sort sort) {
        return this.repository.findAll(example, sort);
    }
}
