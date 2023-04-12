package com.studygroup.webapp.repository;

import com.studygroup.webapp.domain.MeetingNote;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MeetingNote entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MeetingNoteRepository extends JpaRepository<MeetingNote, Long> {}
