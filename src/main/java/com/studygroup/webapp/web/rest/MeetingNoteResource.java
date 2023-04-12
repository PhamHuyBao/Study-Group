package com.studygroup.webapp.web.rest;

import com.studygroup.webapp.domain.MeetingNote;
import com.studygroup.webapp.repository.MeetingNoteRepository;
import com.studygroup.webapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.studygroup.webapp.domain.MeetingNote}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class MeetingNoteResource {

    private final Logger log = LoggerFactory.getLogger(MeetingNoteResource.class);

    private static final String ENTITY_NAME = "meetingNote";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MeetingNoteRepository meetingNoteRepository;

    public MeetingNoteResource(MeetingNoteRepository meetingNoteRepository) {
        this.meetingNoteRepository = meetingNoteRepository;
    }

    /**
     * {@code POST  /meeting-notes} : Create a new meetingNote.
     *
     * @param meetingNote the meetingNote to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new meetingNote, or with status {@code 400 (Bad Request)} if the meetingNote has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/meeting-notes")
    public ResponseEntity<MeetingNote> createMeetingNote(@RequestBody MeetingNote meetingNote) throws URISyntaxException {
        log.debug("REST request to save MeetingNote : {}", meetingNote);
        if (meetingNote.getId() != null) {
            throw new BadRequestAlertException("A new meetingNote cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MeetingNote result = meetingNoteRepository.save(meetingNote);
        return ResponseEntity
            .created(new URI("/api/meeting-notes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /meeting-notes/:id} : Updates an existing meetingNote.
     *
     * @param id the id of the meetingNote to save.
     * @param meetingNote the meetingNote to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated meetingNote,
     * or with status {@code 400 (Bad Request)} if the meetingNote is not valid,
     * or with status {@code 500 (Internal Server Error)} if the meetingNote couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/meeting-notes/{id}")
    public ResponseEntity<MeetingNote> updateMeetingNote(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MeetingNote meetingNote
    ) throws URISyntaxException {
        log.debug("REST request to update MeetingNote : {}, {}", id, meetingNote);
        if (meetingNote.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, meetingNote.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!meetingNoteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MeetingNote result = meetingNoteRepository.save(meetingNote);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, meetingNote.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /meeting-notes/:id} : Partial updates given fields of an existing meetingNote, field will ignore if it is null
     *
     * @param id the id of the meetingNote to save.
     * @param meetingNote the meetingNote to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated meetingNote,
     * or with status {@code 400 (Bad Request)} if the meetingNote is not valid,
     * or with status {@code 404 (Not Found)} if the meetingNote is not found,
     * or with status {@code 500 (Internal Server Error)} if the meetingNote couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/meeting-notes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MeetingNote> partialUpdateMeetingNote(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MeetingNote meetingNote
    ) throws URISyntaxException {
        log.debug("REST request to partial update MeetingNote partially : {}, {}", id, meetingNote);
        if (meetingNote.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, meetingNote.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!meetingNoteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MeetingNote> result = meetingNoteRepository
            .findById(meetingNote.getId())
            .map(existingMeetingNote -> {
                if (meetingNote.getDetail() != null) {
                    existingMeetingNote.setDetail(meetingNote.getDetail());
                }

                return existingMeetingNote;
            })
            .map(meetingNoteRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, meetingNote.getId().toString())
        );
    }

    /**
     * {@code GET  /meeting-notes} : get all the meetingNotes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of meetingNotes in body.
     */
    @GetMapping("/meeting-notes")
    public List<MeetingNote> getAllMeetingNotes() {
        log.debug("REST request to get all MeetingNotes");
        return meetingNoteRepository.findAll();
    }

    /**
     * {@code GET  /meeting-notes/:id} : get the "id" meetingNote.
     *
     * @param id the id of the meetingNote to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the meetingNote, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/meeting-notes/{id}")
    public ResponseEntity<MeetingNote> getMeetingNote(@PathVariable Long id) {
        log.debug("REST request to get MeetingNote : {}", id);
        Optional<MeetingNote> meetingNote = meetingNoteRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(meetingNote);
    }

    /**
     * {@code DELETE  /meeting-notes/:id} : delete the "id" meetingNote.
     *
     * @param id the id of the meetingNote to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/meeting-notes/{id}")
    public ResponseEntity<Void> deleteMeetingNote(@PathVariable Long id) {
        log.debug("REST request to delete MeetingNote : {}", id);
        meetingNoteRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
