package com.example.GestoreAgenti.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import com.example.GestoreAgenti.model.Servizio;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ServizioControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllServiziReturnsEmptyListWhenDatabaseIsEmpty() throws Exception {
        mockMvc.perform(get("/api/servizi"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void createServizioPersistsEntityAndMakesItAccessibleViaApi() throws Exception {
        Servizio request = new Servizio();
        request.setNome("Consulenza Business Intelligence");
        request.setDescrizione("Analisi dei dati commerciali e costruzione dashboard");
        request.setPrezzoBase(new BigDecimal("1500.00"));
        request.setCommissionePercentuale(new BigDecimal("0.15"));

        String payload = objectMapper.writeValueAsString(request);

        MvcResult creationResult = mockMvc.perform(post("/api/servizi")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idServizio").isNumber())
                .andExpect(jsonPath("$.nome").value("Consulenza Business Intelligence"))
                .andExpect(jsonPath("$.descrizione").value("Analisi dei dati commerciali e costruzione dashboard"))
                .andExpect(jsonPath("$.prezzoBase").value(1500.00))
                .andExpect(jsonPath("$.commissionePercentuale").value(0.15))
                .andReturn();

        JsonNode creationBody = objectMapper.readTree(creationResult.getResponse().getContentAsString());
        long createdId = creationBody.get("idServizio").asLong();

        assertThat(createdId).isPositive();

        mockMvc.perform(get("/api/servizi/" + createdId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idServizio").value(createdId))
                .andExpect(jsonPath("$.nome").value("Consulenza Business Intelligence"))
                .andExpect(jsonPath("$.prezzoBase").value(1500.00))
                .andExpect(jsonPath("$.commissionePercentuale").value(0.15));
    }
}
