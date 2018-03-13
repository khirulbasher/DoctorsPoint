package com.lemon.project.web.rest;

import com.lemon.project.ProjectApp;

import com.lemon.project.domain.Cash;
import com.lemon.project.repository.CashRepository;
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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static com.lemon.project.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.lemon.project.domain.enumeration.TransactionType;
/**
 * Test class for the CashResource REST controller.
 *
 * @see CashResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProjectApp.class)
public class CashResourceIntTest {

    private static final BigDecimal DEFAULT_CASH = new BigDecimal(1);
    private static final BigDecimal UPDATED_CASH = new BigDecimal(2);

    private static final LocalDate DEFAULT_LAST_TRANSACTION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_TRANSACTION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final TransactionType DEFAULT_TRANSACTION_TYPE = TransactionType.CASH_IN;
    private static final TransactionType UPDATED_TRANSACTION_TYPE = TransactionType.CASH_OUT;

    @Autowired
    private CashRepository cashRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCashMockMvc;

    private Cash cash;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CashResource cashResource = new CashResource(cashRepository);
        this.restCashMockMvc = MockMvcBuilders.standaloneSetup(cashResource)
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
    public static Cash createEntity(EntityManager em) {
        Cash cash = new Cash();
        cash.setCash(DEFAULT_CASH);
        cash.setLastTransactionDate(DEFAULT_LAST_TRANSACTION_DATE);
        cash.setTransactionType(DEFAULT_TRANSACTION_TYPE);
        return cash;
    }

    @Before
    public void initTest() {
        cash = createEntity(em);
    }

    @Test
    @Transactional
    public void createCash() throws Exception {
        int databaseSizeBeforeCreate = cashRepository.findAll().size();

        // Create the Cash
        restCashMockMvc.perform(post("/api/cash")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cash)))
            .andExpect(status().isCreated());

        // Validate the Cash in the database
        List<Cash> cashList = cashRepository.findAll();
        assertThat(cashList).hasSize(databaseSizeBeforeCreate + 1);
        Cash testCash = cashList.get(cashList.size() - 1);
        assertThat(testCash.getCash()).isEqualTo(DEFAULT_CASH);
        assertThat(testCash.getLastTransactionDate()).isEqualTo(DEFAULT_LAST_TRANSACTION_DATE);
        assertThat(testCash.getTransactionType()).isEqualTo(DEFAULT_TRANSACTION_TYPE);
    }

    @Test
    @Transactional
    public void createCashWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cashRepository.findAll().size();

        // Create the Cash with an existing ID
        cash.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCashMockMvc.perform(post("/api/cash")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cash)))
            .andExpect(status().isBadRequest());

        // Validate the Cash in the database
        List<Cash> cashList = cashRepository.findAll();
        assertThat(cashList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCashIsRequired() throws Exception {
        int databaseSizeBeforeTest = cashRepository.findAll().size();
        // set the field null
        cash.setCash(null);

        // Create the Cash, which fails.

        restCashMockMvc.perform(post("/api/cash")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cash)))
            .andExpect(status().isBadRequest());

        List<Cash> cashList = cashRepository.findAll();
        assertThat(cashList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCash() throws Exception {
        // Initialize the database
        cashRepository.saveAndFlush(cash);

        // Get all the cashList
        restCashMockMvc.perform(get("/api/cash?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cash.getId().intValue())))
            .andExpect(jsonPath("$.[*].cash").value(hasItem(DEFAULT_CASH.intValue())))
            .andExpect(jsonPath("$.[*].lastTransactionDate").value(hasItem(DEFAULT_LAST_TRANSACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].transactionType").value(hasItem(DEFAULT_TRANSACTION_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getCash() throws Exception {
        // Initialize the database
        cashRepository.saveAndFlush(cash);

        // Get the cash
        restCashMockMvc.perform(get("/api/cash/{id}", cash.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cash.getId().intValue()))
            .andExpect(jsonPath("$.cash").value(DEFAULT_CASH.intValue()))
            .andExpect(jsonPath("$.lastTransactionDate").value(DEFAULT_LAST_TRANSACTION_DATE.toString()))
            .andExpect(jsonPath("$.transactionType").value(DEFAULT_TRANSACTION_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCash() throws Exception {
        // Get the cash
        restCashMockMvc.perform(get("/api/cash/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCash() throws Exception {
        // Initialize the database
        cashRepository.saveAndFlush(cash);
        int databaseSizeBeforeUpdate = cashRepository.findAll().size();

        // Update the cash
        Cash updatedCash = cashRepository.findOne(cash.getId());
        // Disconnect from session so that the updates on updatedCash are not directly saved in db
        em.detach(updatedCash);
        updatedCash.setCash(UPDATED_CASH);
        updatedCash.setLastTransactionDate(UPDATED_LAST_TRANSACTION_DATE);
        updatedCash.setTransactionType(UPDATED_TRANSACTION_TYPE);

        restCashMockMvc.perform(put("/api/cash")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCash)))
            .andExpect(status().isOk());

        // Validate the Cash in the database
        List<Cash> cashList = cashRepository.findAll();
        assertThat(cashList).hasSize(databaseSizeBeforeUpdate);
        Cash testCash = cashList.get(cashList.size() - 1);
        assertThat(testCash.getCash()).isEqualTo(UPDATED_CASH);
        assertThat(testCash.getLastTransactionDate()).isEqualTo(UPDATED_LAST_TRANSACTION_DATE);
        assertThat(testCash.getTransactionType()).isEqualTo(UPDATED_TRANSACTION_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingCash() throws Exception {
        int databaseSizeBeforeUpdate = cashRepository.findAll().size();

        // Create the Cash

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCashMockMvc.perform(put("/api/cash")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cash)))
            .andExpect(status().isCreated());

        // Validate the Cash in the database
        List<Cash> cashList = cashRepository.findAll();
        assertThat(cashList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCash() throws Exception {
        // Initialize the database
        cashRepository.saveAndFlush(cash);
        int databaseSizeBeforeDelete = cashRepository.findAll().size();

        // Get the cash
        restCashMockMvc.perform(delete("/api/cash/{id}", cash.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Cash> cashList = cashRepository.findAll();
        assertThat(cashList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cash.class);
        Cash cash1 = new Cash();
        cash1.setId(1L);
        Cash cash2 = new Cash();
        cash2.setId(cash1.getId());
        assertThat(cash1).isEqualTo(cash2);
        cash2.setId(2L);
        assertThat(cash1).isNotEqualTo(cash2);
        cash1.setId(null);
        assertThat(cash1).isNotEqualTo(cash2);
    }
}
