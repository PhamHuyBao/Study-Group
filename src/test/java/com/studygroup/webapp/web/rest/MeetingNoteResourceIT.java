package com.studygroup.webapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.studygroup.webapp.IntegrationTest;
import com.studygroup.webapp.domain.MeetingNote;
import com.studygroup.webapp.repository.MeetingNoteRepository;
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
 * Integration tests for the {@link MeetingNoteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MeetingNoteResourceIT {

    private static final String DEFAULT_DETAIL = "AAAAAAAAAA";
    private static final String UPDATED_DETAIL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/meeting-notes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MeetingNoteRepository meetingNoteRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMeetingNoteMockMvc;

    private MeetingNote meetingNote;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MeetingNote createEntity(EntityManager em) {
        MeetingNote meetingNote = new MeetingNote().detail(DEFAULT_DETAIL);
        return meetingNote;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MeetingNote createUpdatedEntity(EntityManager em) {
        MeetingNote meetingNote = new MeetingNote().detail(UPDATED_DETAIL);
        return meetingNote;
    }

    @BeforeEach
    public void initTest() {
        meetingNote = createEntity(em);
    }

    @Test
    @Transactional
    void createMeetingNote() throws Exception {
        int databaseSizeBeforeCreate = meetingNoteRepository.findAll().size();
        // Create the MeetingNote
        restMeetingNoteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(meetingNote)))
            .andExpect(status().isCreated());

        // Validate the MeetingNote in the database
        List<MeetingNote> meetingNoteList = meetingNoteRepository.findAll();
        assertThat(meetingNoteList).hasSize(databaseSizeBeforeCreate + 1);
        MeetingNote testMeetingNote = meetingNoteList.get(meetingNoteList.size() - 1);
        assertThat(testMeetingNote.getDetail()).isEqualTo(DEFAULT_DETAIL);
    }

    @Test
    @Transactional
    void createMeetingNoteWithExistingId() throws Exception {
        // Create the MeetingNote with an existing ID
        meetingNote.setId(1L);

        int databaseSizeBeforeCreate = meetingNoteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMeetingNoteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(meetingNote)))
            .andExpect(status().isBadRequest());

        // Validate the MeetingNote in the database
        List<MeetingNote> meetingNoteList = meetingNoteRepository.findAll();
        assertThat(meetingNoteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMeetingNotes() throws Exception {
        // Initialize the database
        meetingNoteRepository.saveAndFlush(meetingNote);

        // Get all the meetingNoteList
        restMeetingNoteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(meetingNote.getId().intValue())))
            .andExpect(jsonPath("$.[*].detail").value(hasItem(DEFAULT_DETAIL)));
    }

    @Test
    @Transactional
    void getMeetingNote() throws Exception {
        // Initialize the database
        meetingNoteRepository.saveAndFlush(meetingNote);

        // Get the meetingNote
        restMeetingNoteMockMvc
            .perform(get(ENTITY_API_URL_ID, meetingNote.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(meetingNote.getId().intValue()))
            .andExpect(jsonPath("$.detail").value(DEFAULT_DETAIL));
    }

    @Test
    @Transactional
    void getNonExistingMeetingNote() throws Exception {
        // Get the meetingNote
        restMeetingNoteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMeetingNote() throws Exception {
        // Initialize the database
        meetingNoteRepository.saveAndFlush(meetingNote);

        int databaseSizeBeforeUpdate = meetingNoteRepository.findAll().size();

        // Update the meetingNote
        MeetingNote updatedMeetingNote = meetingNoteRepository.findById(meetingNote.getId()).get();
        // Disconnect from session so that the updates on updatedMeetingNote are not directly saved in db
        em.detach(updatedMeetingNote);
        updatedMeetingNote.detail(UPDATED_DETAIL);

        restMeetingNoteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMeetingNote.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMeetingNote))
            )
            .andExpect(status().isOk());

        // Validate the MeetingNote in the database
        List<MeetingNote> meetingNoteList = meetingNoteRepository.findAll();
        assertThat(meetingNoteList).hasSize(databaseSizeBeforeUpdate);
        MeetingNote testMeetingNote = meetingNoteList.get(meetingNoteList.size() - 1);
        assertThat(testMeetingNote.getDetail()).isEqualTo(UPDATED_DETAIL);
    }

    @Test
    @Transactional
    void putNonExistingMeetingNote() throws Exception {
        int databaseSizeBeforeUpdate = meetingNoteRepository.findAll().size();
        meetingNote.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMeetingNoteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, meetingNote.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(meetingNote))
            )
            .andExpect(status().isBadRequest());

        // Validate the MeetingNote in the database
        List<MeetingNote> meetingNoteList = meetingNoteRepository.findAll();
        assertThat(meetingNoteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMeetingNote() throws Exception {
        int databaseSizeBeforeUpdate = meetingNoteRepository.findAll().size();
        meetingNote.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMeetingNoteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(meetingNote))
            )
            .andExpect(status().isBadRequest());

        // Validate the MeetingNote in the database
        List<MeetingNote> meetingNoteList = meetingNoteRepository.findAll();
        assertThat(meetingNoteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMeetingNote() throws Exception {
        int databaseSizeBeforeUpdate = meetingNoteRepository.findAll().size();
        meetingNote.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMeetingNoteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(meetingNote)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MeetingNote in the database
        List<MeetingNote> meetingNoteList = meetingNoteRepository.findAll();
        assertThat(meetingNoteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMeetingNoteWithPatch() throws Exception {
        // Initialize the database
        meetingNoteRepository.saveAndFlush(meetingNote);

        int databaseSizeBeforeUpdate = meetingNoteRepository.findAll().size();

        // Update the meetingNote using partial update
        MeetingNote partialUpdatedMeetingNote = new MeetingNote();
        partialUpdatedMeetingNote.setId(meetingNote.getId());

        partialUpdatedMeetingNote.detail(UPDATED_DETAIL);

        restMeetingNoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMeetingNote.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMeetingNote))
            )
            .andExpect(status().isOk());

        // Validate the MeetingNote in the database
        List<MeetingNote> meetingNoteList = meetingNoteRepository.findAll();
        assertThat(meetingNoteList).hasSize(databaseSizeBeforeUpdate);
        MeetingNote testMeetingNote = meetingNoteList.get(meetingNoteList.size() - 1);
        assertThat(testMeetingNote.getDetail()).isEqualTo(UPDATED_DETAIL);
    }

    @Test
    @Transactional
    void fullUpdateMeetingNoteWithPatch() throws Exception {
        // Initialize the database
        meetingNoteRepository.saveAndFlush(meetingNote);

        int databaseSizeBeforeUpdate = meetingNoteRepository.findAll().size();

        // Update the meetingNote using partial update
        MeetingNote partialUpdatedMeetingNote = new MeetingNote();
        partialUpdatedMeetingNote.setId(meetingNote.getId());

        partialUpdatedMeetingNote.detail(UPDATED_DETAIL);

        restMeetingNoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMeetingNote.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMeetingNote))
            )
            .andExpect(status().isOk());

        // Validate the MeetingNote in the database
        List<MeetingNote> meetingNoteList = meetingNoteRepository.findAll();
        assertThat(meetingNoteList).hasSize(databaseSizeBeforeUpdate);
        MeetingNote testMeetingNote = meetingNoteList.get(meetingNoteList.size() - 1);
        assertThat(testMeetingNote.getDetail()).isEqualTo(UPDATED_DETAIL);
    }

    @Test
    @Transactional
    void patchNonExistingMeetingNote() throws Exception {
        int databaseSizeBeforeUpdate = meetingNoteRepository.findAll().size();
        meetingNote.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMeetingNoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, meetingNote.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(meetingNote))
            )
            .andExpect(status().isBadRequest());

        // Validate the MeetingNote in the database
        List<MeetingNote> meetingNoteList = meetingNoteRepository.findAll();
        assertThat(meetingNoteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMeetingNote() throws Exception {
        int databaseSizeBeforeUpdate = meetingNoteRepository.findAll().size();
        meetingNote.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMeetingNoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(meetingNote))
            )
            .andExpect(status().isBadRequest());

        // Validate the MeetingNote in the database
        List<MeetingNote> meetingNoteList = meetingNoteRepository.findAll();
        assertThat(meetingNoteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMeetingNote() throws Exception {
        int databaseSizeBeforeUpdate = meetingNoteRepository.findAll().size();
        meetingNote.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMeetingNoteMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(meetingNote))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MeetingNote in the database
        List<MeetingNote> meetingNoteList = meetingNoteRepository.findAll();
        assertThat(meetingNoteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMeetingNote() throws Exception {
        // Initialize the database
        meetingNoteRepository.saveAndFlush(meetingNote);

        int databaseSizeBeforeDelete = meetingNoteRepository.findAll().size();

        // Delete the meetingNote
        restMeetingNoteMockMvc
            .perform(delete(ENTITY_API_URL_ID, meetingNote.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MeetingNote> meetingNoteList = meetingNoteRepository.findAll();
        assertThat(meetingNoteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
