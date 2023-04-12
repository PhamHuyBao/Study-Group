package com.studygroup.webapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.studygroup.webapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MeetingNoteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MeetingNote.class);
        MeetingNote meetingNote1 = new MeetingNote();
        meetingNote1.setId(1L);
        MeetingNote meetingNote2 = new MeetingNote();
        meetingNote2.setId(meetingNote1.getId());
        assertThat(meetingNote1).isEqualTo(meetingNote2);
        meetingNote2.setId(2L);
        assertThat(meetingNote1).isNotEqualTo(meetingNote2);
        meetingNote1.setId(null);
        assertThat(meetingNote1).isNotEqualTo(meetingNote2);
    }
}
