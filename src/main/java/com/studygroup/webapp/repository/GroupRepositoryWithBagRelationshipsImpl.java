package com.studygroup.webapp.repository;

import com.studygroup.webapp.domain.Group;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class GroupRepositoryWithBagRelationshipsImpl implements GroupRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Group> fetchBagRelationships(Optional<Group> group) {
        return group.map(this::fetchUsers);
    }

    @Override
    public Page<Group> fetchBagRelationships(Page<Group> groups) {
        return new PageImpl<>(fetchBagRelationships(groups.getContent()), groups.getPageable(), groups.getTotalElements());
    }

    @Override
    public List<Group> fetchBagRelationships(List<Group> groups) {
        return Optional.of(groups).map(this::fetchUsers).orElse(Collections.emptyList());
    }

    Group fetchUsers(Group result) {
        return entityManager
            .createQuery("select group from Group group left join fetch group.users where group is :group", Group.class)
            .setParameter("group", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Group> fetchUsers(List<Group> groups) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, groups.size()).forEach(index -> order.put(groups.get(index).getId(), index));
        List<Group> result = entityManager
            .createQuery("select distinct group from Group group left join fetch group.users where group in :groups", Group.class)
            .setParameter("groups", groups)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
