package com.studygroup.webapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.studygroup.webapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MeetingTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Meeting.class);
        Meeting meeting1 = new Meeting();
        meeting1.setId("id1");
        Meeting meeting2 = new Meeting();
        meeting2.setId(meeting1.getId());
        assertThat(meeting1).isEqualTo(meeting2);
        meeting2.setId("id2");
        assertThat(meeting1).isNotEqualTo(meeting2);
        meeting1.setId(null);
        assertThat(meeting1).isNotEqualTo(meeting2);
    }
}
