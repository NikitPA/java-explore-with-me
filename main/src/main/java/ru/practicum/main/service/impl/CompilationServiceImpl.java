package ru.practicum.main.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.exception.NotFoundException;
import ru.practicum.main.model.Compilation;
import ru.practicum.main.model.Event;
import ru.practicum.main.model.dto.CompilationDto;
import ru.practicum.main.model.dto.NewCompilationDto;
import ru.practicum.main.repository.CompilationRepository;
import ru.practicum.main.service.CompilationService;
import ru.practicum.main.service.EventService;

import java.text.MessageFormat;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventService eventService;
    private final ModelMapper mapper;

    @Transactional
    @Override
    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) {
        Compilation compilation = mapper.map(newCompilationDto, Compilation.class);
        compilation.setEvents(newCompilationDto.getEvents()
                .stream().map(eventService::findById)
                .collect(Collectors.toSet()));
        Compilation compilationSave = compilationRepository.save(compilation);
        return mapper.map(compilationSave, CompilationDto.class);
    }

    @Transactional
    @Override
    public void deleteCompilation(int compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException(
                        MessageFormat.format("Compilation {0} not found.", compId))
                );
        compilationRepository.delete(compilation);
    }

    @Transactional
    @Override
    public void deleteEventFromCompilation(int compId, int eventId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException(
                        MessageFormat.format("Compilation {0} not found.", compId))
                );
        Event event = eventService.findById(eventId);
        compilation.getEvents().remove(event);
        compilationRepository.save(compilation);
    }

    @Transactional
    @Override
    public void addEventFromCompilation(int compId, int eventId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException(
                        MessageFormat.format("Compilation {0} not found.", compId))
                );
        Event event = eventService.findById(eventId);
        compilation.getEvents().add(event);
        compilationRepository.save(compilation);
    }

    @Transactional
    @Override
    public void unpinCompilation(int compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException(
                        MessageFormat.format("Compilation {0} not found.", compId))
                );
        compilation.setPinned(false);
        compilationRepository.save(compilation);
    }

    @Transactional
    @Override
    public void pinCompilation(int compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException(
                        MessageFormat.format("Compilation {0} not found.", compId))
                );
        compilation.setPinned(true);
        compilationRepository.save(compilation);
    }

    @Override
    public Page<CompilationDto> findCompilations(int from, int size, Boolean pinned) {
        int page = from / size;
        PageRequest pageRequest = PageRequest.of(page, size);
        if (pinned == null) {
            return compilationRepository
                    .findAll(pageRequest)
                    .map(compilation -> mapper.map(compilation, CompilationDto.class));
        }
        return compilationRepository
                .findAllByPinned(pinned, pageRequest)
                .map(compilation -> mapper.map(compilation, CompilationDto.class));
    }

    @Override
    public CompilationDto findCompilation(int compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException(
                        MessageFormat.format("Compilation {0} not found.", compId))
                );
        return mapper.map(compilation, CompilationDto.class);
    }

}
