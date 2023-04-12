package com.studygroup.webapp.repository;

import com.studygroup.webapp.domain.Group;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface GroupRepositoryWithBagRelationships {
    Optional<Group> fetchBagRelationships(Optional<Group> group);

    List<Group> fetchBagRelationships(List<Group> groups);

    Page<Group> fetchBagRelationships(Page<Group> groups);
}
