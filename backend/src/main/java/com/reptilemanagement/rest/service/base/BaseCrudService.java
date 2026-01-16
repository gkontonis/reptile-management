package com.reptilemanagement.rest.service.base;

import com.reptilemanagement.persistence.domain.base.BaseEntity;
import com.reptilemanagement.persistence.domain.base.EntityUpdatable;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Provides crud functionality for entities that are updatable
 *
 * @param <Id>     Primary Key Type
 * @param <Entity> Entity Type that is updatable
 * @param <Dto>    Dto Type
 */
@SuppressWarnings("java:S119")
@Slf4j
public abstract class BaseCrudService<Id, Entity extends BaseEntity<Id> & EntityUpdatable<Dto>, Dto extends BaseDto<Id>>
        extends BaseService<Id, Entity, Dto> {
    protected final String actionCreate = baseAction + AuditAction.CREATE;
    protected final String actionUpdate = baseAction + AuditAction.UPDATE;
    protected final String actionDelete = baseAction + AuditAction.DELETE;

    @Transactional
    public List<Entity> createAsEntity(List<Dto> list, Map<String, Boolean> conditions) {
        this.checkCreateDtoValidity(list);

        return this.saveDtoAsEntities(list, conditions);
    }

    @Transactional
    public Entity createAsEntity(Dto dto, Map<String, Boolean> conditions) {
        var list = List.of(dto);
        this.checkCreateDtoValidity(list);

        var retList = this.saveDtoAsEntities(list, conditions);

        return retList.size() == 1 ? retList.getFirst() : null;
    }

    @Transactional
    public List<Dto> create(List<Dto> list, Map<String, Boolean> conditions) {
        this.checkCreateDtoValidity(list);

        return this.saveDto(list, conditions);
    }

    @Transactional
    public Dto create(Dto dto, Map<String, Boolean> conditions) {
        var list = List.of(dto);
        this.checkCreateDtoValidity(list);

        var retList = this.saveDto(list, conditions);

        return retList.size() == 1 ? retList.getFirst() : null;
    }

    @Transactional
    public Entity create(Entity entity) {
        var list = List.of(entity);
        var retList = this.save(list);

        return retList.size() == 1 ? retList.getFirst() : null;
    }

    @Transactional
    public Dto update(Dto dto, Map<String, Boolean> conditions) {
        this.checkUpdateDtoValidity(dto);

        return this.updateDto(dto, conditions);
    }

    @Transactional
    public void delete(Dto dto) {
        var list = List.of(dto);

        this.deleteDto(list);
    }

    @Transactional
    public void deleteAll(List<Dto> dtos) {
        var entities = getMapper().toEntityList(dtos, null);
        getRepository().deleteAll(entities);

        log.info(MarkerConstants.CRUD, "'{}' deleted '{}' '{}' ",
                authenticationInformationProvider.getAuthenticatedIdentifier(), typeSimpleName,
                dtos.stream().map(BaseDto::getId).toList());

        for (var dto : dtos) {
            this.auditService.from(dto, AuditOperationType.REMOVE, actionDelete);
        }
    }

    @Transactional
    public void deleteById(Id id) {
        getRepository().deleteById(id);

        log.info(MarkerConstants.CRUD, "'{}' deleted '{}' '{}' ",
                authenticationInformationProvider.getAuthenticatedIdentifier(), typeSimpleName, id);

        this.auditService.from(id.toString(), AuditOperationType.REMOVE, auditResource, actionDelete);
    }

    @Transactional
    public void deleteAllById(List<Id> ids) {
        getRepository().deleteAllById(ids);

        log.info(MarkerConstants.CRUD, "'{}' deleted '{}' '{}' ",
                authenticationInformationProvider.getAuthenticatedIdentifier(), typeSimpleName, ids);

        for (var id : ids) {
            this.auditService.from(id.toString(), AuditOperationType.REMOVE, auditResource, actionDelete);
        }
    }

    // main use is for internal entities like AuditLog
    protected List<Entity> save(List<Entity> entities) {
        var dbEntities = getRepository().saveAll(entities);
        logCreate(dbEntities);

        return dbEntities;
    }

    protected List<Entity> saveDtoAsEntities(List<Dto> dtos, Map<String, Boolean> conditions) {
        var entities = getMapper().toEntityList(dtos, conditions);
        var dbEntities = getRepository().saveAll(entities);
        logCreate(dbEntities);

        return entities;
    }

    protected List<Dto> saveDto(List<Dto> dtos, Map<String, Boolean> conditions) {
        var entities = getMapper().toEntityList(dtos, conditions);
        var dbEntities = getRepository().saveAll(entities);
        logCreate(dbEntities);

        return getMapper().toDtoList(dbEntities, conditions);
    }

    /**
     * Updates the fields specified in the entity's update method. Does not handle
     * entity relationships.
     */
    protected Dto updateDto(Dto dto, Map<String, Boolean> conditions) {
        this.checkUpdateDtoValidity(dto);

        var entity = getRepository().findById(dto.getId()).orElseThrow(() -> new IllegalArgumentException(
                "Entity update '" + typeSimpleName + "' with id '" + dto.getId() + "' does not exist"));
        entity.update(dto);
        handleEntityRelationships(entity, dto, conditions);
        var updatedEntity = getRepository().save(entity);

        log.info(MarkerConstants.CRUD, "'{}' updated '{}' '{}' ",
                authenticationInformationProvider.getAuthenticatedIdentifier(), typeSimpleName,
                updatedEntity.getId());
        this.auditService.from(updatedEntity, AuditOperationType.MODIFY, actionUpdate);

        return getMapper().toDto(updatedEntity, conditions);
    }

    /**
     * Template method to handle entity relationships.
     */
    protected void handleEntityRelationships(Entity entity, Dto dto, Map<String, Boolean> conditions) {
        // Default implementation does nothing
    }

    protected void deleteDto(List<Dto> dtos) {
        var entities = getMapper().toEntityList(dtos, null);
        getRepository().deleteAll(entities);

        log.info(MarkerConstants.CRUD, "'{}' deleted '{}' '{}' ",
                authenticationInformationProvider.getAuthenticatedIdentifier(), typeSimpleName,
                dtos.stream().map(Dto::getId).toList());

        for (var entity : entities) {
            this.auditService.from(entity, AuditOperationType.REMOVE, actionDelete);
        }
    }

    protected void checkUpdateDtoValidity(Dto dto) {
        if (dto == null) {
            log.info(MarkerConstants.CRUD, "'{}' tried update '{}' with 'null' object",
                    authenticationInformationProvider.getAuthenticatedIdentifier(), typeSimpleName);
            throw new IllegalArgumentException(
                    "Entity update '" + typeSimpleName + "' with 'null' object is not allowed");
        }

        if (dto.getId() == null) {
            log.info(MarkerConstants.CRUD, "'{}' tried update '{}' with id 'null'",
                    authenticationInformationProvider.getAuthenticatedIdentifier(), typeSimpleName);
            throw new IllegalArgumentException(
                    "Entity update '" + typeSimpleName + "' with 'null' id is not allowed");
        }

        if (!getRepository().existsById(dto.getId())) {
            log.info(MarkerConstants.CRUD, "'{}' tried update '{}' with id '{}' that does not exist",
                    authenticationInformationProvider.getAuthenticatedIdentifier(), typeSimpleName, dto.getId());
            throw new IllegalArgumentException(
                    "Entity update '" + typeSimpleName + "' with id '" + dto.getId() + "' does not exist");
        }
    }

    protected void checkCreateDtoValidity(List<Dto> dtos) {
        for (Dto dto : dtos) {
            if (dto == null) {
                log.info(MarkerConstants.CRUD, "'{}' tried create '{}' with 'null' object",
                        authenticationInformationProvider.getAuthenticatedIdentifier(), typeSimpleName);
                throw new IllegalArgumentException(
                        "Entity creation '" + typeSimpleName + "' with 'null' object is not allowed");
            }

            if (dto.getId() != null) {
                log.info(MarkerConstants.CRUD, "'{}' tried create '{}' with id '{}'",
                        authenticationInformationProvider.getAuthenticatedIdentifier(), typeSimpleName, dto.getId());
                throw new IllegalArgumentException("Entity creation '" + typeSimpleName + "' with id is not allowed");
            }
        }
    }

    protected void logFailedRelatedEntityLoad(String relatedName, String id) {
        log.info(MarkerConstants.CRUD, "'{}' tried update '{}' with nonexistent related entity '{}' '{}'",
            authenticationInformationProvider.getAuthenticatedIdentifier(), typeSimpleName, relatedName, id);
    }

    /**
     * Log the creation of the given entities and audit the creation of the given
     * entities
     *
     * @param dbEntities the entities to log
     */
    private void logCreate(List<Entity> dbEntities) {
        log.info(MarkerConstants.CRUD,
                "'{}' created '{}' '{}' ",
                authenticationInformationProvider.getAuthenticatedIdentifier(),
                typeSimpleName,
                dbEntities.stream().map(BaseEntity::getId).toList());

        // create handled by audit listener
    }
}
