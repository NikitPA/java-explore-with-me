package ru.practicum.stats.repository;

import ru.practicum.stats.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface CustomStatsRepository {

    List<ViewStats> findStats(LocalDateTime start, LocalDateTime end, String[] uris, Boolean unique);

}
