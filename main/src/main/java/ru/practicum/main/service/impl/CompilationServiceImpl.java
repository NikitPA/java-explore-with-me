package ru.practicum.main.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.main.exception.CompilationNotFoundException;
import ru.practicum.main.model.Compilation;
import ru.practicum.main.model.Event;
import ru.practicum.main.model.dto.CompilationDto;
import ru.practicum.main.model.dto.NewCompilationDto;
import ru.practicum.main.repository.CompilationRepository;
import ru.practicum.main.service.CompilationService;
import ru.practicum.main.service.EventService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventService eventService;
    private final ModelMapper mapper;

    @Override
    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) {
        Compilation compilation = mapper.map(newCompilationDto, Compilation.class);
        compilation.setEvents(newCompilationDto.getEvents()
                .stream().map(eventService::findById)
                .collect(Collectors.toList()));
        Compilation compilationSave = compilationRepository.save(compilation);
        return mapper.map(compilationSave, CompilationDto.class);
    }

    @Override
    public void deleteCompilation(int compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new CompilationNotFoundException(compId));
        compilationRepository.delete(compilation);
    }

    @Override
    public void deleteEventFromCompilation(int compId, int eventId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new CompilationNotFoundException(compId));
        Event event = eventService.findById(eventId);
        compilation.getEvents().remove(event);
        compilationRepository.save(compilation);
    }

    @Override
    public void addEventFromCompilation(int compId, int eventId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new CompilationNotFoundException(compId));
        Event event = eventService.findById(eventId);
        compilation.getEvents().add(event);
        compilationRepository.save(compilation);
    }

    @Override
    public void unpinCompilation(int compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new CompilationNotFoundException(compId));
        compilation.setPinned(false);
        compilationRepository.save(compilation);
    }

    @Override
    public void pinCompilation(int compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new CompilationNotFoundException(compId));
        compilation.setPinned(true);
        compilationRepository.save(compilation);
    }

    @Override
    public List<CompilationDto> findCompilations(int from, int size, Boolean pinned) {
        int page = from / size;
        PageRequest pageRequest = PageRequest.of(page, size);
        if(pinned == null) {
            Page<Compilation> compilations = compilationRepository.findAll(pageRequest);
            return compilations.stream()
                    .map(compilation -> mapper.map(compilation, CompilationDto.class))
                    .collect(Collectors.toList());
        }
        Page<Compilation> compilations = compilationRepository.findAllByPinned(pinned, pageRequest);
        return compilations.stream()
                .map(compilation -> mapper.map(compilation, CompilationDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public CompilationDto findCompilation(int compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new CompilationNotFoundException(compId));
        return mapper.map(compilation, CompilationDto.class);
    }

}
