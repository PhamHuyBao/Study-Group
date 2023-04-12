package com.studygroup.webapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Group.
 */
@Entity
@Table(name = "jhi_group")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Group implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "groupname")
    private String groupname;

    @Column(name = "group_owner_id")
    private String groupOwnerId;

    @Column(name = "category")
    private String category;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "description")
    private String description;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @ManyToMany
    @JoinTable(
        name = "rel_jhi_group__user",
        joinColumns = @JoinColumn(name = "jhi_group_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> users = new HashSet<>();

    @JsonIgnoreProperties(value = { "user", "group", "meetingNote" }, allowSetters = true)
    @OneToOne(mappedBy = "group")
    private Meeting meeting;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Group id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupname() {
        return this.groupname;
    }

    public Group groupname(String groupname) {
        this.setGroupname(groupname);
        return this;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public String getGroupOwnerId() {
        return this.groupOwnerId;
    }

    public Group groupOwnerId(String groupOwnerId) {
        this.setGroupOwnerId(groupOwnerId);
        return this;
    }

    public void setGroupOwnerId(String groupOwnerId) {
        this.groupOwnerId = groupOwnerId;
    }

    public String getCategory() {
        return this.category;
    }

    public Group category(String category) {
        this.setCategory(category);
        return this;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Boolean getStatus() {
        return this.status;
    }

    public Group status(Boolean status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getDescription() {
        return this.description;
    }

    public Group description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getCreatedDate() {
        return this.createdDate;
    }

    public Group createdDate(LocalDate createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public Set<User> getUsers() {
        return this.users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Group users(Set<User> users) {
        this.setUsers(users);
        return this;
    }

    public Group addUser(User user) {
        this.users.add(user);
        return this;
    }

    public Group removeUser(User user) {
        this.users.remove(user);
        return this;
    }

    public Meeting getMeeting() {
        return this.meeting;
    }

    public void setMeeting(Meeting meeting) {
        if (this.meeting != null) {
            this.meeting.setGroup(null);
        }
        if (meeting != null) {
            meeting.setGroup(this);
        }
        this.meeting = meeting;
    }

    public Group meeting(Meeting meeting) {
        this.setMeeting(meeting);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Group)) {
            return false;
        }
        return id != null && id.equals(((Group) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Group{" +
            "id=" + getId() +
            ", groupname='" + getGroupname() + "'" +
            ", groupOwnerId='" + getGroupOwnerId() + "'" +
            ", category='" + getCategory() + "'" +
            ", status='" + getStatus() + "'" +
            ", description='" + getDescription() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}
