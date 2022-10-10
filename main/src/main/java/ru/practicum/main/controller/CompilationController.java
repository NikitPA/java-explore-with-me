package ru.practicum.main.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.main.model.dto.CompilationDto;
import ru.practicum.main.model.dto.NewCompilationDto;
import ru.practicum.main.service.CompilationService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class CompilationController {

    private final CompilationService compilationService;

    @PostMapping("/admin/compilations")
    public ResponseEntity<CompilationDto> createCompilation(@Valid @RequestBody NewCompilationDto newCompilationDto) {
        return ResponseEntity.ok(compilationService.createCompilation(newCompilationDto));
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
    public ResponseEntity<Page<CompilationDto>> getCompilations(
            @RequestParam(name = "from", defaultValue = "0") int from,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "pinned", required = false) Boolean pinned) {
        return ResponseEntity.ok(compilationService.findCompilations(from, size, pinned));
    }

    @GetMapping("/compilations/{compId}")
    public ResponseEntity<CompilationDto> getCompilation(@PathVariable(name = "compId") int compId) {
        return ResponseEntity.ok(compilationService.findCompilation(compId));
    }

}
