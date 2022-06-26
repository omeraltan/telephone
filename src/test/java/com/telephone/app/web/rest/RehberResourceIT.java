package com.telephone.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.telephone.app.IntegrationTest;
import com.telephone.app.domain.Rehber;
import com.telephone.app.repository.RehberRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link RehberResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RehberResourceIT {

    private static final String DEFAULT_ADI = "AAAAAAAAAA";
    private static final String UPDATED_ADI = "BBBBBBBBBB";

    private static final String DEFAULT_SOYADI = "AAAAAAAAAA";
    private static final String UPDATED_SOYADI = "BBBBBBBBBB";

    private static final String DEFAULT_DAHILI = "AAAAAAAAAA";
    private static final String UPDATED_DAHILI = "BBBBBBBBBB";

    private static final String DEFAULT_CEP = "AAAAAAAAAA";
    private static final String UPDATED_CEP = "BBBBBBBBBB";

    private static final String DEFAULT_ACIKLAMA = "AAAAAAAAAA";
    private static final String UPDATED_ACIKLAMA = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/rehbers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RehberRepository rehberRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRehberMockMvc;

    private Rehber rehber;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Rehber createEntity(EntityManager em) {
        Rehber rehber = new Rehber()
            .adi(DEFAULT_ADI)
            .soyadi(DEFAULT_SOYADI)
            .dahili(DEFAULT_DAHILI)
            .cep(DEFAULT_CEP)
            .aciklama(DEFAULT_ACIKLAMA);
        return rehber;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Rehber createUpdatedEntity(EntityManager em) {
        Rehber rehber = new Rehber()
            .adi(UPDATED_ADI)
            .soyadi(UPDATED_SOYADI)
            .dahili(UPDATED_DAHILI)
            .cep(UPDATED_CEP)
            .aciklama(UPDATED_ACIKLAMA);
        return rehber;
    }

    @BeforeEach
    public void initTest() {
        rehber = createEntity(em);
    }

    @Test
    @Transactional
    void createRehber() throws Exception {
        int databaseSizeBeforeCreate = rehberRepository.findAll().size();
        // Create the Rehber
        restRehberMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rehber)))
            .andExpect(status().isCreated());

        // Validate the Rehber in the database
        List<Rehber> rehberList = rehberRepository.findAll();
        assertThat(rehberList).hasSize(databaseSizeBeforeCreate + 1);
        Rehber testRehber = rehberList.get(rehberList.size() - 1);
        assertThat(testRehber.getAdi()).isEqualTo(DEFAULT_ADI);
        assertThat(testRehber.getSoyadi()).isEqualTo(DEFAULT_SOYADI);
        assertThat(testRehber.getDahili()).isEqualTo(DEFAULT_DAHILI);
        assertThat(testRehber.getCep()).isEqualTo(DEFAULT_CEP);
        assertThat(testRehber.getAciklama()).isEqualTo(DEFAULT_ACIKLAMA);
    }

    @Test
    @Transactional
    void createRehberWithExistingId() throws Exception {
        // Create the Rehber with an existing ID
        rehber.setId(1L);

        int databaseSizeBeforeCreate = rehberRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRehberMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rehber)))
            .andExpect(status().isBadRequest());

        // Validate the Rehber in the database
        List<Rehber> rehberList = rehberRepository.findAll();
        assertThat(rehberList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAdiIsRequired() throws Exception {
        int databaseSizeBeforeTest = rehberRepository.findAll().size();
        // set the field null
        rehber.setAdi(null);

        // Create the Rehber, which fails.

        restRehberMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rehber)))
            .andExpect(status().isBadRequest());

        List<Rehber> rehberList = rehberRepository.findAll();
        assertThat(rehberList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSoyadiIsRequired() throws Exception {
        int databaseSizeBeforeTest = rehberRepository.findAll().size();
        // set the field null
        rehber.setSoyadi(null);

        // Create the Rehber, which fails.

        restRehberMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rehber)))
            .andExpect(status().isBadRequest());

        List<Rehber> rehberList = rehberRepository.findAll();
        assertThat(rehberList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDahiliIsRequired() throws Exception {
        int databaseSizeBeforeTest = rehberRepository.findAll().size();
        // set the field null
        rehber.setDahili(null);

        // Create the Rehber, which fails.

        restRehberMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rehber)))
            .andExpect(status().isBadRequest());

        List<Rehber> rehberList = rehberRepository.findAll();
        assertThat(rehberList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRehbers() throws Exception {
        // Initialize the database
        rehberRepository.saveAndFlush(rehber);

        // Get all the rehberList
        restRehberMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rehber.getId().intValue())))
            .andExpect(jsonPath("$.[*].adi").value(hasItem(DEFAULT_ADI)))
            .andExpect(jsonPath("$.[*].soyadi").value(hasItem(DEFAULT_SOYADI)))
            .andExpect(jsonPath("$.[*].dahili").value(hasItem(DEFAULT_DAHILI)))
            .andExpect(jsonPath("$.[*].cep").value(hasItem(DEFAULT_CEP)))
            .andExpect(jsonPath("$.[*].aciklama").value(hasItem(DEFAULT_ACIKLAMA)));
    }

    @Test
    @Transactional
    void getRehber() throws Exception {
        // Initialize the database
        rehberRepository.saveAndFlush(rehber);

        // Get the rehber
        restRehberMockMvc
            .perform(get(ENTITY_API_URL_ID, rehber.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rehber.getId().intValue()))
            .andExpect(jsonPath("$.adi").value(DEFAULT_ADI))
            .andExpect(jsonPath("$.soyadi").value(DEFAULT_SOYADI))
            .andExpect(jsonPath("$.dahili").value(DEFAULT_DAHILI))
            .andExpect(jsonPath("$.cep").value(DEFAULT_CEP))
            .andExpect(jsonPath("$.aciklama").value(DEFAULT_ACIKLAMA));
    }

    @Test
    @Transactional
    void getNonExistingRehber() throws Exception {
        // Get the rehber
        restRehberMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRehber() throws Exception {
        // Initialize the database
        rehberRepository.saveAndFlush(rehber);

        int databaseSizeBeforeUpdate = rehberRepository.findAll().size();

        // Update the rehber
        Rehber updatedRehber = rehberRepository.findById(rehber.getId()).get();
        // Disconnect from session so that the updates on updatedRehber are not directly saved in db
        em.detach(updatedRehber);
        updatedRehber.adi(UPDATED_ADI).soyadi(UPDATED_SOYADI).dahili(UPDATED_DAHILI).cep(UPDATED_CEP).aciklama(UPDATED_ACIKLAMA);

        restRehberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRehber.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRehber))
            )
            .andExpect(status().isOk());

        // Validate the Rehber in the database
        List<Rehber> rehberList = rehberRepository.findAll();
        assertThat(rehberList).hasSize(databaseSizeBeforeUpdate);
        Rehber testRehber = rehberList.get(rehberList.size() - 1);
        assertThat(testRehber.getAdi()).isEqualTo(UPDATED_ADI);
        assertThat(testRehber.getSoyadi()).isEqualTo(UPDATED_SOYADI);
        assertThat(testRehber.getDahili()).isEqualTo(UPDATED_DAHILI);
        assertThat(testRehber.getCep()).isEqualTo(UPDATED_CEP);
        assertThat(testRehber.getAciklama()).isEqualTo(UPDATED_ACIKLAMA);
    }

    @Test
    @Transactional
    void putNonExistingRehber() throws Exception {
        int databaseSizeBeforeUpdate = rehberRepository.findAll().size();
        rehber.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRehberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rehber.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rehber))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rehber in the database
        List<Rehber> rehberList = rehberRepository.findAll();
        assertThat(rehberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRehber() throws Exception {
        int databaseSizeBeforeUpdate = rehberRepository.findAll().size();
        rehber.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRehberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rehber))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rehber in the database
        List<Rehber> rehberList = rehberRepository.findAll();
        assertThat(rehberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRehber() throws Exception {
        int databaseSizeBeforeUpdate = rehberRepository.findAll().size();
        rehber.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRehberMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rehber)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Rehber in the database
        List<Rehber> rehberList = rehberRepository.findAll();
        assertThat(rehberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRehberWithPatch() throws Exception {
        // Initialize the database
        rehberRepository.saveAndFlush(rehber);

        int databaseSizeBeforeUpdate = rehberRepository.findAll().size();

        // Update the rehber using partial update
        Rehber partialUpdatedRehber = new Rehber();
        partialUpdatedRehber.setId(rehber.getId());

        partialUpdatedRehber.adi(UPDATED_ADI).soyadi(UPDATED_SOYADI).dahili(UPDATED_DAHILI).cep(UPDATED_CEP);

        restRehberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRehber.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRehber))
            )
            .andExpect(status().isOk());

        // Validate the Rehber in the database
        List<Rehber> rehberList = rehberRepository.findAll();
        assertThat(rehberList).hasSize(databaseSizeBeforeUpdate);
        Rehber testRehber = rehberList.get(rehberList.size() - 1);
        assertThat(testRehber.getAdi()).isEqualTo(UPDATED_ADI);
        assertThat(testRehber.getSoyadi()).isEqualTo(UPDATED_SOYADI);
        assertThat(testRehber.getDahili()).isEqualTo(UPDATED_DAHILI);
        assertThat(testRehber.getCep()).isEqualTo(UPDATED_CEP);
        assertThat(testRehber.getAciklama()).isEqualTo(DEFAULT_ACIKLAMA);
    }

    @Test
    @Transactional
    void fullUpdateRehberWithPatch() throws Exception {
        // Initialize the database
        rehberRepository.saveAndFlush(rehber);

        int databaseSizeBeforeUpdate = rehberRepository.findAll().size();

        // Update the rehber using partial update
        Rehber partialUpdatedRehber = new Rehber();
        partialUpdatedRehber.setId(rehber.getId());

        partialUpdatedRehber.adi(UPDATED_ADI).soyadi(UPDATED_SOYADI).dahili(UPDATED_DAHILI).cep(UPDATED_CEP).aciklama(UPDATED_ACIKLAMA);

        restRehberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRehber.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRehber))
            )
            .andExpect(status().isOk());

        // Validate the Rehber in the database
        List<Rehber> rehberList = rehberRepository.findAll();
        assertThat(rehberList).hasSize(databaseSizeBeforeUpdate);
        Rehber testRehber = rehberList.get(rehberList.size() - 1);
        assertThat(testRehber.getAdi()).isEqualTo(UPDATED_ADI);
        assertThat(testRehber.getSoyadi()).isEqualTo(UPDATED_SOYADI);
        assertThat(testRehber.getDahili()).isEqualTo(UPDATED_DAHILI);
        assertThat(testRehber.getCep()).isEqualTo(UPDATED_CEP);
        assertThat(testRehber.getAciklama()).isEqualTo(UPDATED_ACIKLAMA);
    }

    @Test
    @Transactional
    void patchNonExistingRehber() throws Exception {
        int databaseSizeBeforeUpdate = rehberRepository.findAll().size();
        rehber.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRehberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, rehber.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rehber))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rehber in the database
        List<Rehber> rehberList = rehberRepository.findAll();
        assertThat(rehberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRehber() throws Exception {
        int databaseSizeBeforeUpdate = rehberRepository.findAll().size();
        rehber.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRehberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rehber))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rehber in the database
        List<Rehber> rehberList = rehberRepository.findAll();
        assertThat(rehberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRehber() throws Exception {
        int databaseSizeBeforeUpdate = rehberRepository.findAll().size();
        rehber.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRehberMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(rehber)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Rehber in the database
        List<Rehber> rehberList = rehberRepository.findAll();
        assertThat(rehberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRehber() throws Exception {
        // Initialize the database
        rehberRepository.saveAndFlush(rehber);

        int databaseSizeBeforeDelete = rehberRepository.findAll().size();

        // Delete the rehber
        restRehberMockMvc
            .perform(delete(ENTITY_API_URL_ID, rehber.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Rehber> rehberList = rehberRepository.findAll();
        assertThat(rehberList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
