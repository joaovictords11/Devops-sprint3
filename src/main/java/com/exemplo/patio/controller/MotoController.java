package com.exemplo.patio.controller;

import com.exemplo.patio.dto.MotoDto;
import com.exemplo.patio.model.Moto;
import com.exemplo.patio.service.MotoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/motos")
public class MotoController {
    private final MotoService service;
    public MotoController(MotoService service) { this.service = service; }

    @GetMapping
    public Page<Moto> listar(Pageable pageable) { return service.listar(pageable); }

    @PostMapping
    public Moto cadastrar(@RequestBody @Valid MotoDto dto) { return service.cadastrar(dto); }

    @GetMapping("/search")
    public Page<Moto> buscarPorPlaca(@RequestParam String placa, Pageable pageable) {
        return service.buscarPorPlaca(placa, pageable);
    }

}