package com.studygroup.webapp.web.rest;

import com.studygroup.webapp.domain.Meeting;
import com.studygroup.webapp.repository.MeetingRepository;
import com.studygroup.webapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.studygroup.webapp.domain.Meeting}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class MeetingResource {

    private final Logger log = LoggerFactory.getLogger(MeetingResource.class);

    private static final String ENTITY_NAME = "meeting";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MeetingRepository meetingRepository;

    public MeetingResource(MeetingRepository meetingRepository) {
        this.meetingRepository = meetingRepository;
    }

    /**
     * {@code POST  /meetings} : Create a new meeting.
     *
     * @param meeting the meeting to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new meeting, or with status {@code 400 (Bad Request)} if the meeting has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/meetings")
    public ResponseEntity<Meeting> createMeeting(@RequestBody Meeting meeting) throws URISyntaxException {
        log.debug("REST request to save Meeting : {}", meeting);
        if (meeting.getId() != null) {
            throw new BadRequestAlertException("A new meeting cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Meeting result = meetingRepository.save(meeting);
        return ResponseEntity
            .created(new URI("/api/meetings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /meetings/:id} : Updates an existing meeting.
     *
     * @param id the id of the meeting to save.
     * @param meeting the meeting to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated meeting,
     * or with status {@code 400 (Bad Request)} if the meeting is not valid,
     * or with status {@code 500 (Internal Server Error)} if the meeting couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/meetings/{id}")
    public ResponseEntity<Meeting> updateMeeting(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Meeting meeting
    ) throws URISyntaxException {
        log.debug("REST request to update Meeting : {}, {}", id, meeting);
        if (meeting.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, meeting.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!meetingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        meeting.setIsPersisted();
        Meeting result = meetingRepository.save(meeting);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, meeting.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /meetings/:id} : Partial updates given fields of an existing meeting, field will ignore if it is null
     *
     * @param id the id of the meeting to save.
     * @param meeting the meeting to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated meeting,
     * or with status {@code 400 (Bad Request)} if the meeting is not valid,
     * or with status {@code 404 (Not Found)} if the meeting is not found,
     * or with status {@code 500 (Internal Server Error)} if the meeting couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/meetings/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Meeting> partialUpdateMeeting(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Meeting meeting
    ) throws URISyntaxException {
        log.debug("REST request to partial update Meeting partially : {}, {}", id, meeting);
        if (meeting.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, meeting.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!meetingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Meeting> result = meetingRepository
            .findById(meeting.getId())
            .map(existingMeeting -> {
                if (meeting.getCreatedDate() != null) {
                    existingMeeting.setCreatedDate(meeting.getCreatedDate());
                }
                if (meeting.getTopic() != null) {
                    existingMeeting.setTopic(meeting.getTopic());
                }
                if (meeting.getDescription() != null) {
                    existingMeeting.setDescription(meeting.getDescription());
                }
                if (meeting.getMeetingLink() != null) {
                    existingMeeting.setMeetingLink(meeting.getMeetingLink());
                }

                return existingMeeting;
            })
            .map(meetingRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, meeting.getId())
        );
    }

    /**
     * {@code GET  /meetings} : get all the meetings.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of meetings in body.
     */
    @GetMapping("/meetings")
    public List<Meeting> getAllMeetings(@RequestParam(required = false) String filter) {
        if ("meetingnote-is-null".equals(filter)) {
            log.debug("REST request to get all Meetings where meetingNote is null");
            return StreamSupport
                .stream(meetingRepository.findAll().spliterator(), false)
                .filter(meeting -> meeting.getMeetingNote() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all Meetings");
        return meetingRepository.findAll();
    }

    /**
     * {@code GET  /meetings/:id} : get the "id" meeting.
     *
     * @param id the id of the meeting to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the meeting, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/meetings/{id}")
    public ResponseEntity<Meeting> getMeeting(@PathVariable String id) {
        log.debug("REST request to get Meeting : {}", id);
        Optional<Meeting> meeting = meetingRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(meeting);
    }

    /**
     * {@code DELETE  /meetings/:id} : delete the "id" meeting.
     *
     * @param id the id of the meeting to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/meetings/{id}")
    public ResponseEntity<Void> deleteMeeting(@PathVariable String id) {
        log.debug("REST request to delete Meeting : {}", id);
        meetingRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }
}
