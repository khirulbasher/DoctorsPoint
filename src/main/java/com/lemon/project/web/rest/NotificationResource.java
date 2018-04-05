package com.lemon.project.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lemon.project.domain.Notification;
import com.lemon.project.repository.NotificationRepository;
import com.lemon.project.security.AuthoritiesConstants;
import com.lemon.project.service.EntityService;
import com.lemon.project.web.rest.errors.BadRequestAlertException;
import com.lemon.project.web.rest.util.HeaderUtil;
import com.lemon.project.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;


/**
 * REST controller for managing Notification.
 */
@RestController
@RequestMapping("/api")
@Secured({AuthoritiesConstants.ADMIN,AuthoritiesConstants.ROLE_MGT})
public class NotificationResource {

    private final Logger log = LoggerFactory.getLogger(NotificationResource.class);

    private static final String ENTITY_NAME = "notification";

    private final NotificationRepository notificationRepository;

    private final EntityService entityService;

    @Inject
    public NotificationResource(NotificationRepository notificationRepository, EntityService entityService) {
        this.notificationRepository = notificationRepository;
        this.entityService = entityService;
    }

    /**
     * POST  /notifications : Create a new notification.
     *
     * @param notification the notification to create
     * @return the ResponseEntity with status 201 (Created) and with body the new notification, or with status 400 (Bad Request) if the notification has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/notifications")
    @Timed
    public ResponseEntity<Notification> createNotification(@Valid @RequestBody Notification notification) throws URISyntaxException {
        log.debug("REST request to save Notification : {}", notification);
        if (notification.getId() != null) {
            throw new BadRequestAlertException("A new notification cannot already have an ID", ENTITY_NAME, "idexists");
        }
        modify(notification);
        Notification result = notificationRepository.save(notification);
        return ResponseEntity.created(new URI("/api/notifications/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /notifications : Updates an existing notification.
     *
     * @param notification the notification to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated notification,
     * or with status 400 (Bad Request) if the notification is not valid,
     * or with status 500 (Internal Server Error) if the notification couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/notifications")
    @Timed
    public ResponseEntity<Notification> updateNotification(@Valid @RequestBody Notification notification) throws URISyntaxException {
        log.debug("REST request to update Notification : {}", notification);
        if (notification.getId() == null) {
            return createNotification(notification);
        }
        modify(notification);
        Notification result = notificationRepository.save(notification);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, notification.getId().toString()))
            .body(result);
    }

    /**
     * GET  /notifications : get all the notifications.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of notifications in body
     */
    @GetMapping("/notifications")
    @Timed
    public ResponseEntity<List<Notification>> getAllNotifications(Pageable pageable) {
        log.debug("REST request to get a page of Notifications");
        Page<Notification> page = notificationRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/notifications");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /notifications/:id : get the "id" notification.
     *
     * @param id the id of the notification to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the notification, or with status 404 (Not Found)
     */
    @GetMapping("/notifications/{id}")
    @Timed
    public ResponseEntity<Notification> getNotification(@PathVariable Long id) {
        log.debug("REST request to get Notification : {}", id);
        Notification notification = notificationRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(notification));
    }

    /**
     * DELETE  /notifications/:id : delete the "id" notification.
     *
     * @param id the id of the notification to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/notifications/{id}")
    @Timed
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        log.debug("REST request to delete Notification : {}", id);
        notificationRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    private void modify(Notification notification) {
        try {
            entityService.modify(notification);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
