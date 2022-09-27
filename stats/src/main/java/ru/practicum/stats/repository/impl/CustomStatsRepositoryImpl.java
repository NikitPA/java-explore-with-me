package ru.practicum.stats.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.stats.model.EndpointHit;
import ru.practicum.stats.model.ViewStats;
import ru.practicum.stats.repository.CustomStatsRepository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomStatsRepositoryImpl implements CustomStatsRepository {

    private final EntityManager entityManager;

    @Override
    public List<ViewStats> findStats(LocalDateTime start, LocalDateTime end, String[] uris, Boolean unique) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ViewStats> query = criteriaBuilder.createQuery(ViewStats.class);
        Root<EndpointHit> endpointHit = query.from(EndpointHit.class);
        List<Predicate> predicates = new ArrayList<>();
        query.select(criteriaBuilder.construct(ViewStats.class,
                endpointHit.get("app"),
                endpointHit.get("uri"),
                unique ? criteriaBuilder.countDistinct(endpointHit.get("ip")) : criteriaBuilder.count(endpointHit.get("ip"))
        ));
        query.groupBy(endpointHit.get("app"), endpointHit.get("uri"), endpointHit.get("ip"));
        predicates.add(criteriaBuilder.between(endpointHit.get("timestamp"), start, end));
        if(uris != null) {
            predicates.add(endpointHit.in((Object[]) uris));
        }
        query.where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(query).getResultList();
    }
}
