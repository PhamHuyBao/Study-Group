package com.studygroup.webapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.springframework.data.domain.Persistable;

/**
 * not an ignored comment
 */
@Schema(description = "not an ignored comment")
@JsonIgnoreProperties(value = { "new" })
@Entity
@Table(name = "meeting")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Meeting implements Serializable, Persistable<String> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @Column(name = "topic")
    private String topic;

    @Column(name = "description")
    private String description;

    @Column(name = "meeting_link")
    private String meetingLink;

    @Transient
    private boolean isPersisted;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @JsonIgnoreProperties(value = { "users", "meeting" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Group group;

    @JsonIgnoreProperties(value = { "user", "meeting" }, allowSetters = true)
    @OneToOne(mappedBy = "meeting")
    private MeetingNote meetingNote;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Meeting id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getCreatedDate() {
        return this.createdDate;
    }

    public Meeting createdDate(LocalDate createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public String getTopic() {
        return this.topic;
    }

    public Meeting topic(String topic) {
        this.setTopic(topic);
        return this;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDescription() {
        return this.description;
    }

    public Meeting description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMeetingLink() {
        return this.meetingLink;
    }

    public Meeting meetingLink(String meetingLink) {
        this.setMeetingLink(meetingLink);
        return this;
    }

    public void setMeetingLink(String meetingLink) {
        this.meetingLink = meetingLink;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public Meeting setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    @PostLoad
    @PostPersist
    public void updateEntityState() {
        this.setIsPersisted();
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Meeting user(User user) {
        this.setUser(user);
        return this;
    }

    public Group getGroup() {
        return this.group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Meeting group(Group group) {
        this.setGroup(group);
        return this;
    }

    public MeetingNote getMeetingNote() {
        return this.meetingNote;
    }

    public void setMeetingNote(MeetingNote meetingNote) {
        if (this.meetingNote != null) {
            this.meetingNote.setMeeting(null);
        }
        if (meetingNote != null) {
            meetingNote.setMeeting(this);
        }
        this.meetingNote = meetingNote;
    }

    public Meeting meetingNote(MeetingNote meetingNote) {
        this.setMeetingNote(meetingNote);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Meeting)) {
            return false;
        }
        return id != null && id.equals(((Meeting) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Meeting{" +
            "id=" + getId() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", topic='" + getTopic() + "'" +
            ", description='" + getDescription() + "'" +
            ", meetingLink='" + getMeetingLink() + "'" +
            "}";
    }
}
