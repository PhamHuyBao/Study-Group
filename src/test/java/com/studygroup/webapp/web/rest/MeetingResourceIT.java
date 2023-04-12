package com.studygroup.webapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.studygroup.webapp.IntegrationTest;
import com.studygroup.webapp.domain.Meeting;
import com.studygroup.webapp.repository.MeetingRepository;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;
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
 * Integration tests for the {@link MeetingResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MeetingResourceIT {

    private static final LocalDate DEFAULT_CREATED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_TOPIC = "AAAAAAAAAA";
    private static final String UPDATED_TOPIC = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_MEETING_LINK = "AAAAAAAAAA";
    private static final String UPDATED_MEETING_LINK = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/meetings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private MeetingRepository meetingRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMeetingMockMvc;

    private Meeting meeting;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Meeting createEntity(EntityManager em) {
        Meeting meeting = new Meeting()
            .createdDate(DEFAULT_CREATED_DATE)
            .topic(DEFAULT_TOPIC)
            .description(DEFAULT_DESCRIPTION)
            .meetingLink(DEFAULT_MEETING_LINK);
        return meeting;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Meeting createUpdatedEntity(EntityManager em) {
        Meeting meeting = new Meeting()
            .createdDate(UPDATED_CREATED_DATE)
            .topic(UPDATED_TOPIC)
            .description(UPDATED_DESCRIPTION)
            .meetingLink(UPDATED_MEETING_LINK);
        return meeting;
    }

    @BeforeEach
    public void initTest() {
        meeting = createEntity(em);
    }

    @Test
    @Transactional
    void createMeeting() throws Exception {
        int databaseSizeBeforeCreate = meetingRepository.findAll().size();
        // Create the Meeting
        restMeetingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(meeting)))
            .andExpect(status().isCreated());

        // Validate the Meeting in the database
        List<Meeting> meetingList = meetingRepository.findAll();
        assertThat(meetingList).hasSize(databaseSizeBeforeCreate + 1);
        Meeting testMeeting = meetingList.get(meetingList.size() - 1);
        assertThat(testMeeting.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testMeeting.getTopic()).isEqualTo(DEFAULT_TOPIC);
        assertThat(testMeeting.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testMeeting.getMeetingLink()).isEqualTo(DEFAULT_MEETING_LINK);
    }

    @Test
    @Transactional
    void createMeetingWithExistingId() throws Exception {
        // Create the Meeting with an existing ID
        meeting.setId("existing_id");

        int databaseSizeBeforeCreate = meetingRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMeetingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(meeting)))
            .andExpect(status().isBadRequest());

        // Validate the Meeting in the database
        List<Meeting> meetingList = meetingRepository.findAll();
        assertThat(meetingList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMeetings() throws Exception {
        // Initialize the database
        meeting.setId(UUID.randomUUID().toString());
        meetingRepository.saveAndFlush(meeting);

        // Get all the meetingList
        restMeetingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(meeting.getId())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].topic").value(hasItem(DEFAULT_TOPIC)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].meetingLink").value(hasItem(DEFAULT_MEETING_LINK)));
    }

    @Test
    @Transactional
    void getMeeting() throws Exception {
        // Initialize the database
        meeting.setId(UUID.randomUUID().toString());
        meetingRepository.saveAndFlush(meeting);

        // Get the meeting
        restMeetingMockMvc
            .perform(get(ENTITY_API_URL_ID, meeting.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(meeting.getId()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.topic").value(DEFAULT_TOPIC))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.meetingLink").value(DEFAULT_MEETING_LINK));
    }

    @Test
    @Transactional
    void getNonExistingMeeting() throws Exception {
        // Get the meeting
        restMeetingMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMeeting() throws Exception {
        // Initialize the database
        meeting.setId(UUID.randomUUID().toString());
        meetingRepository.saveAndFlush(meeting);

        int databaseSizeBeforeUpdate = meetingRepository.findAll().size();

        // Update the meeting
        Meeting updatedMeeting = meetingRepository.findById(meeting.getId()).get();
        // Disconnect from session so that the updates on updatedMeeting are not directly saved in db
        em.detach(updatedMeeting);
        updatedMeeting
            .createdDate(UPDATED_CREATED_DATE)
            .topic(UPDATED_TOPIC)
            .description(UPDATED_DESCRIPTION)
            .meetingLink(UPDATED_MEETING_LINK);

        restMeetingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMeeting.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMeeting))
            )
            .andExpect(status().isOk());

        // Validate the Meeting in the database
        List<Meeting> meetingList = meetingRepository.findAll();
        assertThat(meetingList).hasSize(databaseSizeBeforeUpdate);
        Meeting testMeeting = meetingList.get(meetingList.size() - 1);
        assertThat(testMeeting.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testMeeting.getTopic()).isEqualTo(UPDATED_TOPIC);
        assertThat(testMeeting.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMeeting.getMeetingLink()).isEqualTo(UPDATED_MEETING_LINK);
    }

    @Test
    @Transactional
    void putNonExistingMeeting() throws Exception {
        int databaseSizeBeforeUpdate = meetingRepository.findAll().size();
        meeting.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMeetingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, meeting.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(meeting))
            )
            .andExpect(status().isBadRequest());

        // Validate the Meeting in the database
        List<Meeting> meetingList = meetingRepository.findAll();
        assertThat(meetingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMeeting() throws Exception {
        int databaseSizeBeforeUpdate = meetingRepository.findAll().size();
        meeting.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMeetingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(meeting))
            )
            .andExpect(status().isBadRequest());

        // Validate the Meeting in the database
        List<Meeting> meetingList = meetingRepository.findAll();
        assertThat(meetingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMeeting() throws Exception {
        int databaseSizeBeforeUpdate = meetingRepository.findAll().size();
        meeting.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMeetingMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(meeting)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Meeting in the database
        List<Meeting> meetingList = meetingRepository.findAll();
        assertThat(meetingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMeetingWithPatch() throws Exception {
        // Initialize the database
        meeting.setId(UUID.randomUUID().toString());
        meetingRepository.saveAndFlush(meeting);

        int databaseSizeBeforeUpdate = meetingRepository.findAll().size();

        // Update the meeting using partial update
        Meeting partialUpdatedMeeting = new Meeting();
        partialUpdatedMeeting.setId(meeting.getId());

        restMeetingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMeeting.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMeeting))
            )
            .andExpect(status().isOk());

        // Validate the Meeting in the database
        List<Meeting> meetingList = meetingRepository.findAll();
        assertThat(meetingList).hasSize(databaseSizeBeforeUpdate);
        Meeting testMeeting = meetingList.get(meetingList.size() - 1);
        assertThat(testMeeting.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testMeeting.getTopic()).isEqualTo(DEFAULT_TOPIC);
        assertThat(testMeeting.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testMeeting.getMeetingLink()).isEqualTo(DEFAULT_MEETING_LINK);
    }

    @Test
    @Transactional
    void fullUpdateMeetingWithPatch() throws Exception {
        // Initialize the database
        meeting.setId(UUID.randomUUID().toString());
        meetingRepository.saveAndFlush(meeting);

        int databaseSizeBeforeUpdate = meetingRepository.findAll().size();

        // Update the meeting using partial update
        Meeting partialUpdatedMeeting = new Meeting();
        partialUpdatedMeeting.setId(meeting.getId());

        partialUpdatedMeeting
            .createdDate(UPDATED_CREATED_DATE)
            .topic(UPDATED_TOPIC)
            .description(UPDATED_DESCRIPTION)
            .meetingLink(UPDATED_MEETING_LINK);

        restMeetingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMeeting.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMeeting))
            )
            .andExpect(status().isOk());

        // Validate the Meeting in the database
        List<Meeting> meetingList = meetingRepository.findAll();
        assertThat(meetingList).hasSize(databaseSizeBeforeUpdate);
        Meeting testMeeting = meetingList.get(meetingList.size() - 1);
        assertThat(testMeeting.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testMeeting.getTopic()).isEqualTo(UPDATED_TOPIC);
        assertThat(testMeeting.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMeeting.getMeetingLink()).isEqualTo(UPDATED_MEETING_LINK);
    }

    @Test
    @Transactional
    void patchNonExistingMeeting() throws Exception {
        int databaseSizeBeforeUpdate = meetingRepository.findAll().size();
        meeting.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMeetingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, meeting.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(meeting))
            )
            .andExpect(status().isBadRequest());

        // Validate the Meeting in the database
        List<Meeting> meetingList = meetingRepository.findAll();
        assertThat(meetingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMeeting() throws Exception {
        int databaseSizeBeforeUpdate = meetingRepository.findAll().size();
        meeting.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMeetingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(meeting))
            )
            .andExpect(status().isBadRequest());

        // Validate the Meeting in the database
        List<Meeting> meetingList = meetingRepository.findAll();
        assertThat(meetingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMeeting() throws Exception {
        int databaseSizeBeforeUpdate = meetingRepository.findAll().size();
        meeting.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMeetingMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(meeting)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Meeting in the database
        List<Meeting> meetingList = meetingRepository.findAll();
        assertThat(meetingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMeeting() throws Exception {
        // Initialize the database
        meeting.setId(UUID.randomUUID().toString());
        meetingRepository.saveAndFlush(meeting);

        int databaseSizeBeforeDelete = meetingRepository.findAll().size();

        // Delete the meeting
        restMeetingMockMvc
            .perform(delete(ENTITY_API_URL_ID, meeting.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Meeting> meetingList = meetingRepository.findAll();
        assertThat(meetingList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
