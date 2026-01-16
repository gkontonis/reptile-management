package com.reptilemanagement.rest.service.base;

import com.reptilemanagement.persistence.domain.base.BaseEntity;
import com.reptilemanagement.persistence.dto.base.BaseDto;
import com.reptilemanagement.persistence.mapper.base.BaseMapper;
import com.reptilemanagement.shared.audit.AuditAction;
import com.reptilemanagement.shared.audit.AuditOperationType;
import com.reptilemanagement.shared.audit.AuditResourceCollector;
import com.reptilemanagement.shared.audit.AuditService;
import com.reptilemanagement.shared.auth.AuthenticationInformationProvider;
import com.reptilemanagement.shared.constants.MarkerConstants;
import com.reptilemanagement.utils.ReflectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

import static com.reptilemanagement.shared.audit.AuditAction.ACCESS;
import static com.reptilemanagement.utils.ReflectionUtil.getRuntimeArgumentClass;

/**
 * Provides basic find functionality for entities that are not updated
 *
 * @param <Id>     Primary Key Type
 * @param <Entity> Entity Type
 * @param <Dto>    Dto Type
 */
@SuppressWarnings("java:S119")
@Slf4j
public abstract class BaseService<Id, Entity extends BaseEntity<Id>, Dto extends BaseDto<Id>> {
    protected final String typeSimpleName;

    protected AuditService auditService;
    protected AuthenticationInformationProvider authenticationInformationProvider;
    protected String auditResource;

    protected final String baseAction;
    protected final String opAccess;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") // it's an abstract class
    @Autowired
    public final void setAuditService(AuditService auditService) {
        this.auditService = auditService;
    }

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    public final void setAuthenticationInformationProvider(AuthenticationInformationProvider authenticationInformationProvider) {
        this.authenticationInformationProvider = authenticationInformationProvider;
    }

    protected BaseService() {
        Class<?> classType = getRuntimeArgumentClass(getClass(), 1);
        this.typeSimpleName = ReflectionUtil.getSimpleRuntimeTypeName(getClass(), 1);

        this.auditResource = AuditResourceCollector.getInstance().getResourceType(classType);
        this.baseAction = AuditResourceCollector.getInstance().getBaseActionFromClazz(classType);
        this.opAccess = baseAction + ACCESS;
    }

    protected abstract JpaRepository<Entity, Id> getRepository();

    protected abstract BaseMapper<Entity, Dto> getMapper();

    public abstract Sort getDefaultSort();

    /**
     * Find an entity by id
     *
     * @param id the id of the entity to find
     * @return the entity
     * @throws NoSuchElementException   if the entity does not exist in the database
     * @throws IllegalArgumentException if the id is null
     */
    @Transactional(readOnly = true)
    public Dto findById(Id id, Map<String, Boolean> conditions) throws NoSuchElementException, IllegalArgumentException {
        this.auditService.from(id.toString(), AuditOperationType.ACCESS, auditResource, opAccess);

        return getMapper().toDto(getRepository().findById(id).orElseThrow(), conditions);
    }

    @Transactional(readOnly = true)
    public Entity findEntityById(Id id) throws NoSuchElementException, IllegalArgumentException {
        this.auditService.from(id.toString(), AuditOperationType.ACCESS, auditResource, opAccess);

        return getRepository().findById(id).orElseThrow();
    }

    @Transactional(readOnly = true)
    public Page<Dto> findAll(int page, int size, String[] sort, Map<String, Boolean> conditions) {
        var result = getRepository().findAll(genSortedPageRequest(page, size, sort));
        return processResult(conditions, result);
    }

    @Transactional(readOnly = true)
    public List<Dto> findAllByIds(List<Id> ids, Map<String, Boolean> conditions) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }

        var result = getRepository().findAllById(ids.stream().filter(Objects::nonNull).toList());

        for (var dto : result) {
            this.auditService.from(dto.getId().toString(), AuditOperationType.ACCESS, auditResource, opAccess);
        }

        return getMapper().toDtoList(result, conditions);
    }

    /**
     * Find all entities by example
     * Usage:
     *
     * <pre>
     * ExampleMatcher matcher = ExampleMatcher.matching()
     *         .withIgnorePaths("lastname")
     *         .withIncludeNullValues()
     *         .withStringMatcher(StringMatcher.ENDING);
     *
     * Example<Entity> example = Example.of(entity, matcher);
     * </pre>
     *
     * @param example the example to find by
     * @param page    the page to start at
     * @param size    the number of entities to return
     * @param sort    the sorting to use
     */
    @Transactional(readOnly = true)
    public Page<Dto> findByExample(Example<Entity> example, int page, int size, String[] sort, Map<String, Boolean> conditions) {
        var result = getRepository().findAll(example, genSortedPageRequest(page, size, sort));
        return processResult(conditions, result);
    }

    /**
     * Generate a page request with the given page and size
     *
     * @param page the page to start at
     * @param size the number of entities to return
     * @param sort the sorting to use (if null, the default sort is used)
     * @return a page request
     */
    protected PageRequest genSortedPageRequest(int page, int size, String[] sort) {
        return PageRequest.of(page, size, sort != null ? Sort.by(sort) : getDefaultSort());
    }

    /**
     * Generate a page request with the given page and size
     *
     * @param page the page to start at
     * @param size the number of entities to return
     * @param sort the sorting to use (if null, the default sort is used)
     * @return a page request
     */
    protected PageRequest genSortedPageRequest(int page, int size, Sort sort) {
        return PageRequest.of(page, size, sort != null ? sort : getDefaultSort());
    }

    /**
     * Generate a pair of the given entities and the total number of entities
     *
     * @param conditions the conditions to use
     * @param result     the result to use
     */
    private Page<Dto> processResult(Map<String, Boolean> conditions, Page<Entity> result) {
        Page<Dto> temp = result.map(item -> getMapper().toDto(item, conditions));

        var log = this.auditService.buildBaseAuditLog(AuditOperationType.ACCESS, auditResource, opAccess + ".page");
        this.auditService.createAuditLog(log);

        return temp;
    }
}
