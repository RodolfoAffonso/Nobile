package com.rodolfoafonso.nobile.controller;
import com.rodolfoafonso.nobile.dto.WatchDTO;
import com.rodolfoafonso.nobile.dto.WatchResponseDTO;
import com.rodolfoafonso.nobile.service.WatchService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/watches")
public class WatchController {

    private final WatchService watchService;

    @PostMapping("/register")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<WatchResponseDTO> createWatch(@RequestBody WatchDTO dto) {
        WatchResponseDTO response = watchService.create(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<WatchResponseDTO>> getAllWatches() {
        List<WatchResponseDTO> watches = watchService.getAll();
        return ResponseEntity.ok(watches);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WatchResponseDTO> getWatchById(@PathVariable Long id) {
        WatchResponseDTO watch = watchService.getById(id);
        return ResponseEntity.ok(watch);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WatchResponseDTO> updateWatch(
            @PathVariable Long id,
            @RequestBody WatchDTO dto) {
        WatchResponseDTO updated = watchService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWatch(@PathVariable Long id) {
        watchService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

