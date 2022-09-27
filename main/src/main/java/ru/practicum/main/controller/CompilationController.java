package ru.practicum.main.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.model.dto.CompilationDto;
import ru.practicum.main.model.dto.NewCompilationDto;
import ru.practicum.main.service.CompilationService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CompilationController {

    private final CompilationService compilationService;

    @PostMapping("/admin/compilations")
    public ResponseEntity<CompilationDto> createCompilation(@Valid @RequestBody NewCompilationDto newCompilationDto) {
        return new ResponseEntity<>(compilationService.createCompilation(newCompilationDto), HttpStatus.OK);
    }

    @DeleteMapping("/admin/compilations/{compId}")
    public void deleteCompilation(@PathVariable(name = "compId") int compId) {
        compilationService.deleteCompilation(compId);
    }

    @DeleteMapping("/admin/compilations/{compId}/events/{eventId}")
    public void deleteEventFromCompilation(@PathVariable(name = "compId") int compId,
                                           @PathVariable(name = "eventId") int eventId) {
        compilationService.deleteEventFromCompilation(compId, eventId);
    }

    @PatchMapping("/admin/compilations/{compId}/events/{eventId}")
    public void addEventFromCompilation(@PathVariable(name = "compId") int compId,
                                        @PathVariable(name = "eventId") int eventId) {
        compilationService.addEventFromCompilation(compId, eventId);
    }

    @DeleteMapping("/admin/compilations/{compId}/pin")
    public void unpinCompilation(@PathVariable(name = "compId") int compId) {
        compilationService.unpinCompilation(compId);
    }

    @PatchMapping("/admin/compilations/{compId}/pin")
    public void pinCompilation(@PathVariable(name = "compId") int compId) {
        compilationService.pinCompilation(compId);
    }

    @GetMapping("/compilations")
    public ResponseEntity<List<CompilationDto>> getCompilations(
            @RequestParam(name = "from", defaultValue = "0") int from,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "pinned", required = false) Boolean pinned) {
        return new ResponseEntity<>(compilationService.findCompilations(from, size, pinned), HttpStatus.OK);
    }

    @GetMapping("/compilations/{compId}")
    public ResponseEntity<CompilationDto> getCompilation(@PathVariable(name = "compId") int compId) {
        return new ResponseEntity<>(compilationService.findCompilation(compId), HttpStatus.OK);
    }

}
