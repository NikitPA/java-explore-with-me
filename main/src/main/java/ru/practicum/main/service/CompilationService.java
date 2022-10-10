package ru.practicum.main.service;

import org.springframework.data.domain.Page;
import ru.practicum.main.model.dto.CompilationDto;
import ru.practicum.main.model.dto.NewCompilationDto;

public interface CompilationService {
    CompilationDto createCompilation(NewCompilationDto newCompilationDto);

    void deleteCompilation(int compId);

    void deleteEventFromCompilation(int compId, int eventId);

    void addEventFromCompilation(int compId, int eventId);

    void unpinCompilation(int compId);

    void pinCompilation(int compId);

    Page<CompilationDto> findCompilations(int from, int size, Boolean pinned);

    CompilationDto findCompilation(int compId);
}
