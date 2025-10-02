package com.rodolfoafonso.nobile.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodolfoafonso.nobile.dto.WatchDTO;
import com.rodolfoafonso.nobile.dto.WatchResponseDTO;
import com.rodolfoafonso.nobile.service.WatchService;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import java.math.BigDecimal;
import java.util.List;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@SpringBootTest
@AutoConfigureMockMvc
class WatchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private WatchService watchService;

    @Test
    @WithMockUser(username = "seller@example.com", roles = {"SELLER"})
    void devePermitirQueSellerCadastreRelogio() throws Exception {
        WatchDTO dto = WatchDTO.builder()
                .brand("Rolex")
                .model("Submariner")
                .year(2022)
                .condition("NEW")
                .price(new BigDecimal("75000.00"))
                .images(List.of("https://exemplo.com/imagens/rolex1.png"))
                .build();

        WatchResponseDTO resp = WatchResponseDTO.builder()
                .id(1L)
                .brand(dto.getBrand())
                .model(dto.getModel())
                .year(dto.getYear())
                .condition(dto.getCondition())
                .price(dto.getPrice())
                .images(dto.getImages())
                .sellerId(42L)
                .build();

        when(watchService.create(any(WatchDTO.class))).thenReturn(resp);

        mockMvc.perform(post("/watches/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(String.valueOf(MediaType.APPLICATION_JSON)))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.brand").value("Rolex"));

        verify(watchService, times(1)).create(any(WatchDTO.class));
    }

    @Test
    @WithMockUser(username = "buyer@example.com", roles = {"BUYER"})
    void deveNegarCadastroDeRelogioParaBuyer() throws Exception {
        WatchDTO dto = WatchDTO.builder()
                .brand("Rolex")
                .model("Submariner")
                .year(2022)
                .condition("NEW")
                .price(new BigDecimal("75000.00"))
                .images(List.of())
                .build();

        mockMvc.perform(post("/watches/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isForbidden());

        verify(watchService, never()).create(any());
    }

    @Test
    void deveNegarCadastroDeRelogioParaUsuarioAnonimo() throws Exception {
        WatchDTO dto = WatchDTO.builder()
                .brand("Rolex")
                .model("Submariner")
                .year(2022)
                .condition("NEW")
                .price(new BigDecimal("75000.00"))
                .images(List.of())
                .build();

        mockMvc.perform(post("/watches/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                // ajuste se sua app devolve 403 ao invés de 401
                .andExpect(status().isUnauthorized());

        verify(watchService, never()).create(any());
    }
}

