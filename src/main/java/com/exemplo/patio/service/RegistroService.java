package com.exemplo.patio.service;

import com.exemplo.patio.dto.RegistroDto;
import com.exemplo.patio.model.Moto;
import com.exemplo.patio.model.Registro;
import com.exemplo.patio.repository.MotoRepository;
import com.exemplo.patio.repository.RegistroRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class RegistroService {
    private final RegistroRepository registroRepo;
    private final MotoRepository motoRepo;

    public RegistroService(RegistroRepository registroRepo, MotoRepository motoRepo) {
        this.registroRepo = registroRepo;
        this.motoRepo = motoRepo;
    }

    public Registro checkIn(RegistroDto dto) {
        Moto moto = motoRepo.findByPlaca(dto.getPlaca()).orElseThrow(() -> new EntityNotFoundException("Moto não encontrada"));
        registroRepo.findByMotoPlacaAndCheckOutIsNull(dto.getPlaca()).ifPresent(r -> { throw new IllegalStateException("Moto já no pátio"); });
        return registroRepo.save(new Registro(null, moto, LocalDateTime.now(), null));
    }

    @Transactional
    public Registro checkOut(RegistroDto dto) {
        Registro reg = registroRepo.findByMotoPlacaAndCheckOutIsNull(dto.getPlaca()).orElseThrow(() -> new EntityNotFoundException("Registro de entrada não encontrado"));
        reg.setCheckOut(LocalDateTime.now());
        return registroRepo.save(reg);
    }

    public Page<Registro> listar(String placa, Pageable pageable) {
        return registroRepo.findByMotoPlaca(placa, pageable);
    }
}