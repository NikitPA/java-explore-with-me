package ru.practicum.main.service;

import ru.practicum.main.model.dto.CompilationDto;
import ru.practicum.main.model.dto.NewCompilationDto;

import java.util.List;

public interface CompilationService {
    CompilationDto createCompilation(NewCompilationDto newCompilationDto);

    void deleteCompilation(int compId);

    void deleteEventFromCompilation(int compId, int eventId);

    void addEventFromCompilation(int compId, int eventId);

    void unpinCompilation(int compId);

    void pinCompilation(int compId);

    List<CompilationDto> findCompilations(int from, int size, Boolean pinned);

    CompilationDto findCompilation(int compId);
}
