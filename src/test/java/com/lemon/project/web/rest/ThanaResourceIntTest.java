package com.lemon.project.web.rest;

import com.lemon.project.ProjectApp;

import com.lemon.project.domain.Thana;
import com.lemon.project.repository.ThanaRepository;
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
 * Test class for the ThanaResource REST controller.
 *
 * @see ThanaResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProjectApp.class)
public class ThanaResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

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

    @Autowired
    private ThanaRepository thanaRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restThanaMockMvc;

    private Thana thana;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ThanaResource thanaResource = new ThanaResource(thanaRepository);
        this.restThanaMockMvc = MockMvcBuilders.standaloneSetup(thanaResource)
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
    public static Thana createEntity(EntityManager em) {
        Thana thana = new Thana();
        thana.setName(DEFAULT_NAME);
        thana.setCode(DEFAULT_CODE);
        thana.setActive(DEFAULT_ACTIVE);
        thana.setLatitude(DEFAULT_LATITUDE);
        thana.setLongitude(DEFAULT_LONGITUDE);
        thana.setLastModifiedBy(DEFAULT_LAST_MODIFIED_BY);
        thana.setLastModifyDate(DEFAULT_LAST_MODIFY_DATE);
        return thana;
    }

    @Before
    public void initTest() {
        thana = createEntity(em);
    }

    @Test
    @Transactional
    public void createThana() throws Exception {
        int databaseSizeBeforeCreate = thanaRepository.findAll().size();

        // Create the Thana
        restThanaMockMvc.perform(post("/api/thanas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(thana)))
            .andExpect(status().isCreated());

        // Validate the Thana in the database
        List<Thana> thanaList = thanaRepository.findAll();
        assertThat(thanaList).hasSize(databaseSizeBeforeCreate + 1);
        Thana testThana = thanaList.get(thanaList.size() - 1);
        assertThat(testThana.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testThana.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testThana.isActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testThana.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testThana.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
        assertThat(testThana.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testThana.getLastModifyDate()).isEqualTo(DEFAULT_LAST_MODIFY_DATE);
    }

    @Test
    @Transactional
    public void createThanaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = thanaRepository.findAll().size();

        // Create the Thana with an existing ID
        thana.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restThanaMockMvc.perform(post("/api/thanas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(thana)))
            .andExpect(status().isBadRequest());

        // Validate the Thana in the database
        List<Thana> thanaList = thanaRepository.findAll();
        assertThat(thanaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = thanaRepository.findAll().size();
        // set the field null
        thana.setName(null);

        // Create the Thana, which fails.

        restThanaMockMvc.perform(post("/api/thanas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(thana)))
            .andExpect(status().isBadRequest());

        List<Thana> thanaList = thanaRepository.findAll();
        assertThat(thanaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = thanaRepository.findAll().size();
        // set the field null
        thana.setCode(null);

        // Create the Thana, which fails.

        restThanaMockMvc.perform(post("/api/thanas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(thana)))
            .andExpect(status().isBadRequest());

        List<Thana> thanaList = thanaRepository.findAll();
        assertThat(thanaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = thanaRepository.findAll().size();
        // set the field null
        thana.setActive(null);

        // Create the Thana, which fails.

        restThanaMockMvc.perform(post("/api/thanas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(thana)))
            .andExpect(status().isBadRequest());

        List<Thana> thanaList = thanaRepository.findAll();
        assertThat(thanaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllThanas() throws Exception {
        // Initialize the database
        thanaRepository.saveAndFlush(thana);

        // Get all the thanaList
        restThanaMockMvc.perform(get("/api/thanas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(thana.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.toString())))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastModifyDate").value(hasItem(DEFAULT_LAST_MODIFY_DATE.toString())));
    }

    @Test
    @Transactional
    public void getThana() throws Exception {
        // Initialize the database
        thanaRepository.saveAndFlush(thana);

        // Get the thana
        restThanaMockMvc.perform(get("/api/thanas/{id}", thana.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(thana.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE.toString()))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY.intValue()))
            .andExpect(jsonPath("$.lastModifyDate").value(DEFAULT_LAST_MODIFY_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingThana() throws Exception {
        // Get the thana
        restThanaMockMvc.perform(get("/api/thanas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateThana() throws Exception {
        // Initialize the database
        thanaRepository.saveAndFlush(thana);
        int databaseSizeBeforeUpdate = thanaRepository.findAll().size();

        // Update the thana
        Thana updatedThana = thanaRepository.findOne(thana.getId());
        // Disconnect from session so that the updates on updatedThana are not directly saved in db
        em.detach(updatedThana);
        updatedThana.setName(UPDATED_NAME);
        updatedThana.setCode(UPDATED_CODE);
        updatedThana.setActive(UPDATED_ACTIVE);
        updatedThana.setLatitude(UPDATED_LATITUDE);
        updatedThana.setLongitude(UPDATED_LONGITUDE);
        updatedThana.setLastModifiedBy(UPDATED_LAST_MODIFIED_BY);
        updatedThana.setLastModifyDate(UPDATED_LAST_MODIFY_DATE);

        restThanaMockMvc.perform(put("/api/thanas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedThana)))
            .andExpect(status().isOk());

        // Validate the Thana in the database
        List<Thana> thanaList = thanaRepository.findAll();
        assertThat(thanaList).hasSize(databaseSizeBeforeUpdate);
        Thana testThana = thanaList.get(thanaList.size() - 1);
        assertThat(testThana.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testThana.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testThana.isActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testThana.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testThana.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testThana.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testThana.getLastModifyDate()).isEqualTo(UPDATED_LAST_MODIFY_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingThana() throws Exception {
        int databaseSizeBeforeUpdate = thanaRepository.findAll().size();

        // Create the Thana

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restThanaMockMvc.perform(put("/api/thanas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(thana)))
            .andExpect(status().isCreated());

        // Validate the Thana in the database
        List<Thana> thanaList = thanaRepository.findAll();
        assertThat(thanaList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteThana() throws Exception {
        // Initialize the database
        thanaRepository.saveAndFlush(thana);
        int databaseSizeBeforeDelete = thanaRepository.findAll().size();

        // Get the thana
        restThanaMockMvc.perform(delete("/api/thanas/{id}", thana.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Thana> thanaList = thanaRepository.findAll();
        assertThat(thanaList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Thana.class);
        Thana thana1 = new Thana();
        thana1.setId(1L);
        Thana thana2 = new Thana();
        thana2.setId(thana1.getId());
        assertThat(thana1).isEqualTo(thana2);
        thana2.setId(2L);
        assertThat(thana1).isNotEqualTo(thana2);
        thana1.setId(null);
        assertThat(thana1).isNotEqualTo(thana2);
    }
}
