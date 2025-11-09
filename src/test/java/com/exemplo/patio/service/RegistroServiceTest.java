package com.exemplo.patio.service;

import com.exemplo.patio.dto.RegistroDto;
import com.exemplo.patio.model.Moto;
import com.exemplo.patio.model.Registro;
import com.exemplo.patio.repository.MotoRepository;
import com.exemplo.patio.repository.RegistroRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistroServiceTest {

    @Mock
    private RegistroRepository registroRepo;

    @Mock
    private MotoRepository motoRepo;

    @InjectMocks
    private RegistroService registroService;

    private RegistroDto registroDto;
    private Moto moto;

    @BeforeEach
    void setUp() {
        registroDto = new RegistroDto();
        registroDto.setPlaca("BRA2E19");

        moto = new Moto(1L, "BRA2E19", "Honda PCX 150");
    }

    @Test
    @DisplayName("Deve realizar check-in com sucesso")
    void testCheckIn_Success() {
        when(motoRepo.findByPlaca("BRA2E19")).thenReturn(Optional.of(moto));
        when(registroRepo.findByMotoPlacaAndCheckOutIsNull("BRA2E19")).thenReturn(Optional.empty());

        when(registroRepo.save(any(Registro.class))).thenAnswer(invocation -> {
            Registro regSalvo = invocation.getArgument(0);
            regSalvo.setId(1L);
            return regSalvo;
        });

        Registro registroFeito = registroService.checkIn(registroDto);

        assertNotNull(registroFeito);
        assertEquals(1L, registroFeito.getId());
        assertEquals(moto.getPlaca(), registroFeito.getMoto().getPlaca());
        assertNotNull(registroFeito.getCheckIn());
        assertNull(registroFeito.getCheckOut());

        verify(motoRepo, times(1)).findByPlaca("BRA2E19");
        verify(registroRepo, times(1)).findByMotoPlacaAndCheckOutIsNull("BRA2E19");
        verify(registroRepo, times(1)).save(any(Registro.class));
    }

    @Test
    @DisplayName("Deve falhar check-in se a moto não for encontrada")
    void testCheckIn_Fail_MotoNaoEncontrada() {
        when(motoRepo.findByPlaca("BRA2E19")).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            registroService.checkIn(registroDto);
        });

        assertEquals("Moto não encontrada", exception.getMessage());
        verify(registroRepo, never()).save(any(Registro.class));
    }

    @Test
    @DisplayName("Deve falhar check-in se a moto já estiver no pátio")
    void testCheckIn_Fail_MotoJaNoPatio() {
        when(motoRepo.findByPlaca("BRA2E19")).thenReturn(Optional.of(moto));

        Registro registroAtivo = new Registro(1L, moto, LocalDateTime.now().minusHours(1), null);
        when(registroRepo.findByMotoPlacaAndCheckOutIsNull("BRA2E19")).thenReturn(Optional.of(registroAtivo));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            registroService.checkIn(registroDto);
        });

        assertEquals("Moto já no pátio", exception.getMessage());
        verify(registroRepo, never()).save(any(Registro.class));
    }
}