package com.exemplo.patio.service;

import com.exemplo.patio.dto.MotoDto;
import com.exemplo.patio.model.Moto;
import com.exemplo.patio.repository.MotoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MotoServiceTest {

    @Mock
    private MotoRepository motoRepository;

    @InjectMocks
    private MotoService motoService;

    @Test
    @DisplayName("Deve cadastrar uma nova moto com sucesso")
    void testCadastrar() {
        MotoDto dto = new MotoDto();
        dto.setPlaca("BRA2E19");
        dto.setModelo("Honda PCX 150");

        when(motoRepository.save(any(Moto.class))).thenAnswer(invocation -> {
            Moto motoSalva = invocation.getArgument(0);
            motoSalva.setId(1L);
            return motoSalva;
        });

        Moto moto = motoService.cadastrar(dto);

        assertNotNull(moto);
        assertEquals(1L, moto.getId());
        assertEquals("BRA2E19", moto.getPlaca());
        assertEquals("Honda PCX 150", moto.getModelo());
        verify(motoRepository, times(1)).save(any(Moto.class));
    }

    @Test
    @DisplayName("Deve listar todas as motos com paginação")
    void testListar() {
        Pageable pageable = Pageable.ofSize(10);
        List<Moto> motos = List.of(
                new Moto(1L, "BRA2E19", "Honda PCX"),
                new Moto(2L, "TST1234", "Yamaha NMax")
        );
        Page<Moto> pagina = new PageImpl<>(motos, pageable, motos.size());

        when(motoRepository.findAll(pageable)).thenReturn(pagina);

        Page<Moto> resultado = motoService.listar(pageable);

        assertNotNull(resultado);
        assertEquals(2, resultado.getTotalElements());
        assertEquals(motos, resultado.getContent());
        verify(motoRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("Deve buscar motos por placa com paginação")
    void testBuscarPorPlaca() {
        String placa = "BRA";
        Pageable pageable = Pageable.ofSize(5);
        List<Moto> motos = List.of(new Moto(1L, "BRA2E19", "Honda PCX"));
        Page<Moto> pagina = new PageImpl<>(motos, pageable, motos.size());

        when(motoRepository.findByPlacaContainingIgnoreCase(placa, pageable)).thenReturn(pagina);

        Page<Moto> resultado = motoService.buscarPorPlaca(placa, pageable);

        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        assertEquals("BRA2E19", resultado.getContent().get(0).getPlaca());
        verify(motoRepository, times(1)).findByPlacaContainingIgnoreCase(placa, pageable);
    }
}