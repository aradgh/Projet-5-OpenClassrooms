package com.safetynet.alerts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.service.MedicalRecordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MedicalRecordController.class)
class MedicalRecordControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MedicalRecordService medicalRecordService;

    @Autowired
    private ObjectMapper objectMapper;

    private MedicalRecord medicalRecord;

    @BeforeEach
    void setUp() {
        medicalRecord = new MedicalRecord();
        medicalRecord.setFirstName("John");
        medicalRecord.setLastName("Doe");
        medicalRecord.setBirthdate("01/01/1990");
        medicalRecord.setMedications(List.of("med1", "med2"));
        medicalRecord.setAllergies(List.of("allergy1", "allergy2"));
    }

    @Test
    void testAddMedicalRecord_Success() throws Exception {
        doNothing().when(medicalRecordService).addMedicalRecord(any(MedicalRecord.class));

        mockMvc.perform(post("/medicalRecord")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(medicalRecord)))
            .andExpect(status().isCreated())
            .andExpect(content().string("Medical record added successfully."));

        verify(medicalRecordService, times(1)).addMedicalRecord(any(MedicalRecord.class));
    }

    @Test
    void testAddMedicalRecord_Error() throws Exception {
        doThrow(IOException.class).when(medicalRecordService).addMedicalRecord(any(MedicalRecord.class));

        mockMvc.perform(post("/medicalRecord")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(medicalRecord)))
            .andExpect(status().isInternalServerError())
            .andExpect(content().string("An error occurred while adding the medical record."));

        verify(medicalRecordService, times(1)).addMedicalRecord(any(MedicalRecord.class));
    }

    @Test
    void testUpdateMedicalRecord_Success() throws Exception {
        when(medicalRecordService.updateMedicalRecord(any(MedicalRecord.class))).thenReturn(true);

        mockMvc.perform(put("/medicalRecord")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(medicalRecord)))
            .andExpect(status().isOk())
            .andExpect(content().string("Medical record updated successfully."));

        verify(medicalRecordService, times(1)).updateMedicalRecord(any(MedicalRecord.class));
    }

    @Test
    void testUpdateMedicalRecord_NotFound() throws Exception {
        when(medicalRecordService.updateMedicalRecord(any(MedicalRecord.class))).thenReturn(false);

        mockMvc.perform(put("/medicalRecord")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(medicalRecord)))
            .andExpect(status().isNotFound())
            .andExpect(content().string("Medical record not found."));

        verify(medicalRecordService, times(1)).updateMedicalRecord(any(MedicalRecord.class));
    }

    @Test
    void testUpdateMedicalRecord_InternalServerError() throws Exception {
        // Simuler une exception lors de l'appel au service
        doThrow(new RuntimeException("Simulated exception"))
            .when(medicalRecordService)
            .updateMedicalRecord(any(MedicalRecord.class));

        // Effectuer une requête PUT avec un corps JSON valide
        mockMvc.perform(put("/medicalRecord")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(medicalRecord)))
            .andExpect(status().isInternalServerError()) // Vérifier le code de statut
            .andExpect(content().string("An error occurred while updating the medical record.")); // Vérifier le message

        // Vérifier que le service a bien été appelé une fois
        verify(medicalRecordService, times(1)).updateMedicalRecord(any(MedicalRecord.class));
    }


    @Test
    void testDeleteMedicalRecord_Success() throws Exception {
        UUID id = UUID.randomUUID();
        when(medicalRecordService.deleteMedicalRecord(id)).thenReturn(true);

        mockMvc.perform(delete("/medicalRecord")
                .param("medicalRecordId", id.toString()))
            .andExpect(status().isOk())
            .andExpect(content().string("Medical record deleted successfully."));

        verify(medicalRecordService, times(1)).deleteMedicalRecord(id);
    }

    @Test
    void testDeleteMedicalRecord_NotFound() throws Exception {
        UUID id = UUID.randomUUID();
        when(medicalRecordService.deleteMedicalRecord(id)).thenReturn(false);

        mockMvc.perform(delete("/medicalRecord")
                .param("medicalRecordId", id.toString()))
            .andExpect(status().isNotFound())
            .andExpect(content().string("Medical record not found."));

        verify(medicalRecordService, times(1)).deleteMedicalRecord(id);
    }

    @Test
    void testDeleteMedicalRecord_Error() throws Exception {
        UUID id = UUID.randomUUID();
        doThrow(IOException.class).when(medicalRecordService).deleteMedicalRecord(id);

        mockMvc.perform(delete("/medicalRecord")
                .param("medicalRecordId", id.toString()))
            .andExpect(status().isInternalServerError())
            .andExpect(content().string("An error occurred while deleting the medical record."));

        verify(medicalRecordService, times(1)).deleteMedicalRecord(id);
    }
}
