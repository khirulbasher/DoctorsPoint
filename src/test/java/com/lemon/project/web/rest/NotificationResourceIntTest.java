package com.lemon.project.web.rest;

import com.lemon.project.ProjectApp;

import com.lemon.project.domain.Notification;
import com.lemon.project.repository.NotificationRepository;
import com.lemon.project.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static com.lemon.project.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.lemon.project.domain.enumeration.Priority;
import com.lemon.project.domain.enumeration.ActionType;
/**
 * Test class for the NotificationResource REST controller.
 *
 * @see NotificationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProjectApp.class)
public class NotificationResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE = "BBBBBBBBBB";

    private static final Priority DEFAULT_PRIORITY = Priority.LOW;
    private static final Priority UPDATED_PRIORITY = Priority.GENERAL;

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final ActionType DEFAULT_ACTION_TYPE = ActionType.URL;
    private static final ActionType UPDATED_ACTION_TYPE = ActionType.INTENT;

    private static final String DEFAULT_ACTION_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_ACTION_CONTENT = "BBBBBBBBBB";

    private static final Long DEFAULT_LAST_MODIFIED_BY = 1L;
    private static final Long UPDATED_LAST_MODIFIED_BY = 2L;

    private static final LocalDate DEFAULT_LAST_MODIFY_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_MODIFY_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_OCCUR_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_OCCUR_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_APPROVAL_STATUS = 1;
    private static final Integer UPDATED_APPROVAL_STATUS = 2;

    private static final String DEFAULT_APPROVAL_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_APPROVAL_COMMENT = "BBBBBBBBBB";

    private static final String DEFAULT_FROM_ENTITY = "AAAAAAAAAA";
    private static final String UPDATED_FROM_ENTITY = "BBBBBBBBBB";

    private static final String DEFAULT_FROM_ENTITY_ID = "AAAAAAAAAA";
    private static final String UPDATED_FROM_ENTITY_ID = "BBBBBBBBBB";

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restNotificationMockMvc;

    private Notification notification;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final NotificationResource notificationResource = new NotificationResource(notificationRepository);
        this.restNotificationMockMvc = MockMvcBuilders.standaloneSetup(notificationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Notification createEntity(EntityManager em) {
        Notification notification = new Notification();
        notification.setTitle(DEFAULT_TITLE);
        notification.setMessage(DEFAULT_MESSAGE);
        notification.setPriority(DEFAULT_PRIORITY);
        notification.setActive(DEFAULT_ACTIVE);
        notification.setActionType(DEFAULT_ACTION_TYPE);
        notification.setActionContent(DEFAULT_ACTION_CONTENT);
        notification.setLastModifiedBy(DEFAULT_LAST_MODIFIED_BY);
        notification.setLastModifyDate(DEFAULT_LAST_MODIFY_DATE);
        notification.setOccurDate(DEFAULT_OCCUR_DATE);
        notification.setApprovalStatus(DEFAULT_APPROVAL_STATUS);
        notification.setApprovalComment(DEFAULT_APPROVAL_COMMENT);
        notification.setFromEntity(DEFAULT_FROM_ENTITY);
        notification.setFromEntityId(DEFAULT_FROM_ENTITY_ID);
        return notification;
    }

    @Before
    public void initTest() {
        notification = createEntity(em);
    }

    @Test
    @Transactional
    public void createNotification() throws Exception {
        int databaseSizeBeforeCreate = notificationRepository.findAll().size();

        // Create the Notification
        restNotificationMockMvc.perform(post("/api/notifications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(notification)))
            .andExpect(status().isCreated());

        // Validate the Notification in the database
        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeCreate + 1);
        Notification testNotification = notificationList.get(notificationList.size() - 1);
        assertThat(testNotification.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testNotification.getMessage()).isEqualTo(DEFAULT_MESSAGE);
        assertThat(testNotification.getPriority()).isEqualTo(DEFAULT_PRIORITY);
        assertThat(testNotification.isActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testNotification.getActionType()).isEqualTo(DEFAULT_ACTION_TYPE);
        assertThat(testNotification.getActionContent()).isEqualTo(DEFAULT_ACTION_CONTENT);
        assertThat(testNotification.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testNotification.getLastModifyDate()).isEqualTo(DEFAULT_LAST_MODIFY_DATE);
        assertThat(testNotification.getOccurDate()).isEqualTo(DEFAULT_OCCUR_DATE);
        assertThat(testNotification.getApprovalStatus()).isEqualTo(DEFAULT_APPROVAL_STATUS);
        assertThat(testNotification.getApprovalComment()).isEqualTo(DEFAULT_APPROVAL_COMMENT);
        assertThat(testNotification.getFromEntity()).isEqualTo(DEFAULT_FROM_ENTITY);
        assertThat(testNotification.getFromEntityId()).isEqualTo(DEFAULT_FROM_ENTITY_ID);
    }

    @Test
    @Transactional
    public void createNotificationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = notificationRepository.findAll().size();

        // Create the Notification with an existing ID
        notification.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restNotificationMockMvc.perform(post("/api/notifications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(notification)))
            .andExpect(status().isBadRequest());

        // Validate the Notification in the database
        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = notificationRepository.findAll().size();
        // set the field null
        notification.setTitle(null);

        // Create the Notification, which fails.

        restNotificationMockMvc.perform(post("/api/notifications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(notification)))
            .andExpect(status().isBadRequest());

        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMessageIsRequired() throws Exception {
        int databaseSizeBeforeTest = notificationRepository.findAll().size();
        // set the field null
        notification.setMessage(null);

        // Create the Notification, which fails.

        restNotificationMockMvc.perform(post("/api/notifications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(notification)))
            .andExpect(status().isBadRequest());

        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPriorityIsRequired() throws Exception {
        int databaseSizeBeforeTest = notificationRepository.findAll().size();
        // set the field null
        notification.setPriority(null);

        // Create the Notification, which fails.

        restNotificationMockMvc.perform(post("/api/notifications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(notification)))
            .andExpect(status().isBadRequest());

        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkActionTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = notificationRepository.findAll().size();
        // set the field null
        notification.setActionType(null);

        // Create the Notification, which fails.

        restNotificationMockMvc.perform(post("/api/notifications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(notification)))
            .andExpect(status().isBadRequest());

        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllNotifications() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList
        restNotificationMockMvc.perform(get("/api/notifications?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notification.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE.toString())))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].actionType").value(hasItem(DEFAULT_ACTION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].actionContent").value(hasItem(DEFAULT_ACTION_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastModifyDate").value(hasItem(DEFAULT_LAST_MODIFY_DATE.toString())))
            .andExpect(jsonPath("$.[*].occurDate").value(hasItem(DEFAULT_OCCUR_DATE.toString())))
            .andExpect(jsonPath("$.[*].approvalStatus").value(hasItem(DEFAULT_APPROVAL_STATUS)))
            .andExpect(jsonPath("$.[*].approvalComment").value(hasItem(DEFAULT_APPROVAL_COMMENT.toString())))
            .andExpect(jsonPath("$.[*].fromEntity").value(hasItem(DEFAULT_FROM_ENTITY.toString())))
            .andExpect(jsonPath("$.[*].fromEntityId").value(hasItem(DEFAULT_FROM_ENTITY_ID.toString())));
    }

    @Test
    @Transactional
    public void getNotification() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get the notification
        restNotificationMockMvc.perform(get("/api/notifications/{id}", notification.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(notification.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE.toString()))
            .andExpect(jsonPath("$.priority").value(DEFAULT_PRIORITY.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.actionType").value(DEFAULT_ACTION_TYPE.toString()))
            .andExpect(jsonPath("$.actionContent").value(DEFAULT_ACTION_CONTENT.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY.intValue()))
            .andExpect(jsonPath("$.lastModifyDate").value(DEFAULT_LAST_MODIFY_DATE.toString()))
            .andExpect(jsonPath("$.occurDate").value(DEFAULT_OCCUR_DATE.toString()))
            .andExpect(jsonPath("$.approvalStatus").value(DEFAULT_APPROVAL_STATUS))
            .andExpect(jsonPath("$.approvalComment").value(DEFAULT_APPROVAL_COMMENT.toString()))
            .andExpect(jsonPath("$.fromEntity").value(DEFAULT_FROM_ENTITY.toString()))
            .andExpect(jsonPath("$.fromEntityId").value(DEFAULT_FROM_ENTITY_ID.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingNotification() throws Exception {
        // Get the notification
        restNotificationMockMvc.perform(get("/api/notifications/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNotification() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);
        int databaseSizeBeforeUpdate = notificationRepository.findAll().size();

        // Update the notification
        Notification updatedNotification = notificationRepository.findOne(notification.getId());
        // Disconnect from session so that the updates on updatedNotification are not directly saved in db
        em.detach(updatedNotification);
        updatedNotification.setTitle(UPDATED_TITLE);
        updatedNotification.setMessage(UPDATED_MESSAGE);
        updatedNotification.setPriority(UPDATED_PRIORITY);
        updatedNotification.setActive(UPDATED_ACTIVE);
        updatedNotification.setActionType(UPDATED_ACTION_TYPE);
        updatedNotification.setActionContent(UPDATED_ACTION_CONTENT);
        updatedNotification.setLastModifiedBy(UPDATED_LAST_MODIFIED_BY);
        updatedNotification.setLastModifyDate(UPDATED_LAST_MODIFY_DATE);
        updatedNotification.setOccurDate(UPDATED_OCCUR_DATE);
        updatedNotification.setApprovalStatus(UPDATED_APPROVAL_STATUS);
        updatedNotification.setApprovalComment(UPDATED_APPROVAL_COMMENT);
        updatedNotification.setFromEntity(UPDATED_FROM_ENTITY);
        updatedNotification.setFromEntityId(UPDATED_FROM_ENTITY_ID);

        restNotificationMockMvc.perform(put("/api/notifications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedNotification)))
            .andExpect(status().isOk());

        // Validate the Notification in the database
        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeUpdate);
        Notification testNotification = notificationList.get(notificationList.size() - 1);
        assertThat(testNotification.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testNotification.getMessage()).isEqualTo(UPDATED_MESSAGE);
        assertThat(testNotification.getPriority()).isEqualTo(UPDATED_PRIORITY);
        assertThat(testNotification.isActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testNotification.getActionType()).isEqualTo(UPDATED_ACTION_TYPE);
        assertThat(testNotification.getActionContent()).isEqualTo(UPDATED_ACTION_CONTENT);
        assertThat(testNotification.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testNotification.getLastModifyDate()).isEqualTo(UPDATED_LAST_MODIFY_DATE);
        assertThat(testNotification.getOccurDate()).isEqualTo(UPDATED_OCCUR_DATE);
        assertThat(testNotification.getApprovalStatus()).isEqualTo(UPDATED_APPROVAL_STATUS);
        assertThat(testNotification.getApprovalComment()).isEqualTo(UPDATED_APPROVAL_COMMENT);
        assertThat(testNotification.getFromEntity()).isEqualTo(UPDATED_FROM_ENTITY);
        assertThat(testNotification.getFromEntityId()).isEqualTo(UPDATED_FROM_ENTITY_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingNotification() throws Exception {
        int databaseSizeBeforeUpdate = notificationRepository.findAll().size();

        // Create the Notification

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restNotificationMockMvc.perform(put("/api/notifications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(notification)))
            .andExpect(status().isCreated());

        // Validate the Notification in the database
        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteNotification() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);
        int databaseSizeBeforeDelete = notificationRepository.findAll().size();

        // Get the notification
        restNotificationMockMvc.perform(delete("/api/notifications/{id}", notification.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Notification.class);
        Notification notification1 = new Notification();
        notification1.setId(1L);
        Notification notification2 = new Notification();
        notification2.setId(notification1.getId());
        assertThat(notification1).isEqualTo(notification2);
        notification2.setId(2L);
        assertThat(notification1).isNotEqualTo(notification2);
        notification1.setId(null);
        assertThat(notification1).isNotEqualTo(notification2);
    }
}
