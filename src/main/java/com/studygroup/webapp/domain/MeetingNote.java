package com.studygroup.webapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;

/**
 * A MeetingNote.
 */
@Entity
@Table(name = "meeting_note")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MeetingNote implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "detail")
    private String detail;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @JsonIgnoreProperties(value = { "user", "group", "meetingNote" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Meeting meeting;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MeetingNote id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDetail() {
        return this.detail;
    }

    public MeetingNote detail(String detail) {
        this.setDetail(detail);
        return this;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public MeetingNote user(User user) {
        this.setUser(user);
        return this;
    }

    public Meeting getMeeting() {
        return this.meeting;
    }

    public void setMeeting(Meeting meeting) {
        this.meeting = meeting;
    }

    public MeetingNote meeting(Meeting meeting) {
        this.setMeeting(meeting);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MeetingNote)) {
            return false;
        }
        return id != null && id.equals(((MeetingNote) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MeetingNote{" +
            "id=" + getId() +
            ", detail='" + getDetail() + "'" +
            "}";
    }
}
