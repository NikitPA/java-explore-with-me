package ru.practicum.main.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main.model.Compilation;

public interface CompilationRepository extends JpaRepository<Compilation, Integer> {

    Page<Compilation> findAllByPinned(Boolean pinned, Pageable pageable);

}
