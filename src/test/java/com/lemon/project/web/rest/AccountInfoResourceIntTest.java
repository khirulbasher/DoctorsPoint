package com.lemon.project.web.rest;

import com.lemon.project.ProjectApp;

import com.lemon.project.domain.AccountInfo;
import com.lemon.project.repository.AccountInfoRepository;
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

/**
 * Test class for the AccountInfoResource REST controller.
 *
 * @see AccountInfoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProjectApp.class)
public class AccountInfoResourceIntTest {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String DEFAULT_LATITUDE = "AAAAAAAAAA";
    private static final String UPDATED_LATITUDE = "BBBBBBBBBB";

    private static final String DEFAULT_LONGITUDE = "AAAAAAAAAA";
    private static final String UPDATED_LONGITUDE = "BBBBBBBBBB";

    private static final Long DEFAULT_LAST_MODIFIED_BY = 1L;
    private static final Long UPDATED_LAST_MODIFIED_BY = 2L;

    private static final LocalDate DEFAULT_LAST_MODIFY_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_MODIFY_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_BIRTH_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BIRTH_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;

    private static final String DEFAULT_VILLAGE = "AAAAAAAAAA";
    private static final String UPDATED_VILLAGE = "BBBBBBBBBB";

    @Autowired
    private AccountInfoRepository accountInfoRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAccountInfoMockMvc;

    private AccountInfo accountInfo;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AccountInfoResource accountInfoResource = new AccountInfoResource(accountInfoRepository);
        this.restAccountInfoMockMvc = MockMvcBuilders.standaloneSetup(accountInfoResource)
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
    public static AccountInfo createEntity(EntityManager em) {
        AccountInfo accountInfo = new AccountInfo();
        accountInfo.setFirstName(DEFAULT_FIRST_NAME);
        accountInfo.setLastName(DEFAULT_LAST_NAME);
        accountInfo.setEmail(DEFAULT_EMAIL);
        accountInfo.setActive(DEFAULT_ACTIVE);
        accountInfo.setLatitude(DEFAULT_LATITUDE);
        accountInfo.setLongitude(DEFAULT_LONGITUDE);
        accountInfo.setLastModifiedBy(DEFAULT_LAST_MODIFIED_BY);
        accountInfo.setLastModifyDate(DEFAULT_LAST_MODIFY_DATE);
        accountInfo.setBirthDate(DEFAULT_BIRTH_DATE);
        accountInfo.setUserId(DEFAULT_USER_ID);
        accountInfo.setVillage(DEFAULT_VILLAGE);
        return accountInfo;
    }

    @Before
    public void initTest() {
        accountInfo = createEntity(em);
    }

    @Test
    @Transactional
    public void createAccountInfo() throws Exception {
        int databaseSizeBeforeCreate = accountInfoRepository.findAll().size();

        // Create the AccountInfo
        restAccountInfoMockMvc.perform(post("/api/account-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accountInfo)))
            .andExpect(status().isCreated());

        // Validate the AccountInfo in the database
        List<AccountInfo> accountInfoList = accountInfoRepository.findAll();
        assertThat(accountInfoList).hasSize(databaseSizeBeforeCreate + 1);
        AccountInfo testAccountInfo = accountInfoList.get(accountInfoList.size() - 1);
        assertThat(testAccountInfo.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testAccountInfo.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testAccountInfo.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testAccountInfo.isActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testAccountInfo.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testAccountInfo.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
        assertThat(testAccountInfo.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testAccountInfo.getLastModifyDate()).isEqualTo(DEFAULT_LAST_MODIFY_DATE);
        assertThat(testAccountInfo.getBirthDate()).isEqualTo(DEFAULT_BIRTH_DATE);
        assertThat(testAccountInfo.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testAccountInfo.getVillage()).isEqualTo(DEFAULT_VILLAGE);
    }

    @Test
    @Transactional
    public void createAccountInfoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = accountInfoRepository.findAll().size();

        // Create the AccountInfo with an existing ID
        accountInfo.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAccountInfoMockMvc.perform(post("/api/account-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accountInfo)))
            .andExpect(status().isBadRequest());

        // Validate the AccountInfo in the database
        List<AccountInfo> accountInfoList = accountInfoRepository.findAll();
        assertThat(accountInfoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountInfoRepository.findAll().size();
        // set the field null
        accountInfo.setFirstName(null);

        // Create the AccountInfo, which fails.

        restAccountInfoMockMvc.perform(post("/api/account-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accountInfo)))
            .andExpect(status().isBadRequest());

        List<AccountInfo> accountInfoList = accountInfoRepository.findAll();
        assertThat(accountInfoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountInfoRepository.findAll().size();
        // set the field null
        accountInfo.setEmail(null);

        // Create the AccountInfo, which fails.

        restAccountInfoMockMvc.perform(post("/api/account-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accountInfo)))
            .andExpect(status().isBadRequest());

        List<AccountInfo> accountInfoList = accountInfoRepository.findAll();
        assertThat(accountInfoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountInfoRepository.findAll().size();
        // set the field null
        accountInfo.setActive(null);

        // Create the AccountInfo, which fails.

        restAccountInfoMockMvc.perform(post("/api/account-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accountInfo)))
            .andExpect(status().isBadRequest());

        List<AccountInfo> accountInfoList = accountInfoRepository.findAll();
        assertThat(accountInfoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAccountInfos() throws Exception {
        // Initialize the database
        accountInfoRepository.saveAndFlush(accountInfo);

        // Get all the accountInfoList
        restAccountInfoMockMvc.perform(get("/api/account-infos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accountInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.toString())))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastModifyDate").value(hasItem(DEFAULT_LAST_MODIFY_DATE.toString())))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].village").value(hasItem(DEFAULT_VILLAGE.toString())));
    }

    @Test
    @Transactional
    public void getAccountInfo() throws Exception {
        // Initialize the database
        accountInfoRepository.saveAndFlush(accountInfo);

        // Get the accountInfo
        restAccountInfoMockMvc.perform(get("/api/account-infos/{id}", accountInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(accountInfo.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE.toString()))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY.intValue()))
            .andExpect(jsonPath("$.lastModifyDate").value(DEFAULT_LAST_MODIFY_DATE.toString()))
            .andExpect(jsonPath("$.birthDate").value(DEFAULT_BIRTH_DATE.toString()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()))
            .andExpect(jsonPath("$.village").value(DEFAULT_VILLAGE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAccountInfo() throws Exception {
        // Get the accountInfo
        restAccountInfoMockMvc.perform(get("/api/account-infos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAccountInfo() throws Exception {
        // Initialize the database
        accountInfoRepository.saveAndFlush(accountInfo);
        int databaseSizeBeforeUpdate = accountInfoRepository.findAll().size();

        // Update the accountInfo
        AccountInfo updatedAccountInfo = accountInfoRepository.findOne(accountInfo.getId());
        // Disconnect from session so that the updates on updatedAccountInfo are not directly saved in db
        em.detach(updatedAccountInfo);
        updatedAccountInfo.setFirstName(UPDATED_FIRST_NAME);
        updatedAccountInfo.setLastName(UPDATED_LAST_NAME);
        updatedAccountInfo.setEmail(UPDATED_EMAIL);
        updatedAccountInfo.setActive(UPDATED_ACTIVE);
        updatedAccountInfo.setLatitude(UPDATED_LATITUDE);
        updatedAccountInfo.setLongitude(UPDATED_LONGITUDE);
        updatedAccountInfo.setLastModifiedBy(UPDATED_LAST_MODIFIED_BY);
        updatedAccountInfo.setLastModifyDate(UPDATED_LAST_MODIFY_DATE);
        updatedAccountInfo.setBirthDate(UPDATED_BIRTH_DATE);
        updatedAccountInfo.setUserId(UPDATED_USER_ID);
        updatedAccountInfo.setVillage(UPDATED_VILLAGE);

        restAccountInfoMockMvc.perform(put("/api/account-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAccountInfo)))
            .andExpect(status().isOk());

        // Validate the AccountInfo in the database
        List<AccountInfo> accountInfoList = accountInfoRepository.findAll();
        assertThat(accountInfoList).hasSize(databaseSizeBeforeUpdate);
        AccountInfo testAccountInfo = accountInfoList.get(accountInfoList.size() - 1);
        assertThat(testAccountInfo.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testAccountInfo.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testAccountInfo.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testAccountInfo.isActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testAccountInfo.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testAccountInfo.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testAccountInfo.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testAccountInfo.getLastModifyDate()).isEqualTo(UPDATED_LAST_MODIFY_DATE);
        assertThat(testAccountInfo.getBirthDate()).isEqualTo(UPDATED_BIRTH_DATE);
        assertThat(testAccountInfo.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testAccountInfo.getVillage()).isEqualTo(UPDATED_VILLAGE);
    }

    @Test
    @Transactional
    public void updateNonExistingAccountInfo() throws Exception {
        int databaseSizeBeforeUpdate = accountInfoRepository.findAll().size();

        // Create the AccountInfo

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAccountInfoMockMvc.perform(put("/api/account-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accountInfo)))
            .andExpect(status().isCreated());

        // Validate the AccountInfo in the database
        List<AccountInfo> accountInfoList = accountInfoRepository.findAll();
        assertThat(accountInfoList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAccountInfo() throws Exception {
        // Initialize the database
        accountInfoRepository.saveAndFlush(accountInfo);
        int databaseSizeBeforeDelete = accountInfoRepository.findAll().size();

        // Get the accountInfo
        restAccountInfoMockMvc.perform(delete("/api/account-infos/{id}", accountInfo.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<AccountInfo> accountInfoList = accountInfoRepository.findAll();
        assertThat(accountInfoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AccountInfo.class);
        AccountInfo accountInfo1 = new AccountInfo();
        accountInfo1.setId(1L);
        AccountInfo accountInfo2 = new AccountInfo();
        accountInfo2.setId(accountInfo1.getId());
        assertThat(accountInfo1).isEqualTo(accountInfo2);
        accountInfo2.setId(2L);
        assertThat(accountInfo1).isNotEqualTo(accountInfo2);
        accountInfo1.setId(null);
        assertThat(accountInfo1).isNotEqualTo(accountInfo2);
    }
}
