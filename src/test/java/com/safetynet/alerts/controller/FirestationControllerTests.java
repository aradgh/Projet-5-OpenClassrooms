package com.safetynet.alerts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.*;
import com.safetynet.alerts.service.FirestationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FirestationController.class)
class FirestationControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FirestationService firestationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testAddFirestation() throws Exception {
        Firestation firestation = new Firestation("123 Main St", 1);
        Mockito.doNothing().when(firestationService).addFirestation(any(Firestation.class));

        mockMvc.perform(post("/firestation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(firestation)))
            .andExpect(status().isCreated())
            .andExpect(content().string("Firestation added successfully."));
    }

    @Test
    void testUpdateFirestation_Success() throws Exception {
        Firestation firestation = new Firestation("123 Main St", 1);
        Mockito.when(firestationService.updateFirestation(any(Firestation.class))).thenReturn(true);

        mockMvc.perform(put("/firestation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(firestation)))
            .andExpect(status().isOk())
            .andExpect(content().string("Firestation updated successfully."));
    }

    @Test
    void testUpdateFirestation_NotFound() throws Exception {
        Firestation firestation = new Firestation("123 Main St", 1);
        Mockito.when(firestationService.updateFirestation(any(Firestation.class))).thenReturn(false);

        mockMvc.perform(put("/firestation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(firestation)))
            .andExpect(status().isNotFound())
            .andExpect(content().string("Firestation not found."));
    }

    @Test
    void testDeleteFirestation_Success() throws Exception {
        UUID firestationId = UUID.randomUUID();
        Mockito.when(firestationService.deleteFirestation(firestationId)).thenReturn(true);

        mockMvc.perform(delete("/firestation")
                .param("firestationId", firestationId.toString()))
            .andExpect(status().isOk())
            .andExpect(content().string("Firestation deleted successfully."));
    }

    @Test
    void testDeleteFirestation_NotFound() throws Exception {
        UUID firestationId = UUID.randomUUID();
        Mockito.when(firestationService.deleteFirestation(firestationId)).thenReturn(false);

        mockMvc.perform(delete("/firestation")
                .param("firestationId", firestationId.toString()))
            .andExpect(status().isNotFound())
            .andExpect(content().string("Firestation not found."));
    }

    @Test
    void testGetPersonsByStation() throws Exception {
        int stationNumber = 1;
        FirestationCoverageDTO coverageDTO = new FirestationCoverageDTO();
        coverageDTO.setPersons(Set.of()); // Initialiser `persons` avec un ensemble vide
        coverageDTO.setNumberOfAdults(5); // Exemple d'initialisation
        coverageDTO.setNumberOfChildren(2); // Exemple d'initialisation

        Mockito.when(firestationService.getCoverageByStation(stationNumber)).thenReturn(coverageDTO);

        mockMvc.perform(get("/firestation")
                .param("stationNumber", String.valueOf(stationNumber)))
            .andExpect(status().isOk())
            .andExpect(content().string(coverageDTO.toString()));
    }


    @Test
    void testGetFireAlertByAddress_ResidentsFound() throws Exception {
        String address = "123 Main St";
        FireAlertDTO fireAlertDTO = new FireAlertDTO();
        fireAlertDTO.setResidents(Set.of(new ResidentInfoDTO("Doe", "123-456-7890", 44,List.of("xanax"),List.of("pollen"))));

        Mockito.when(firestationService.getResidentsByAddress(address)).thenReturn(fireAlertDTO);

        mockMvc.perform(get("/fire")
                .param("address", address))
            .andExpect(status().isOk())
            .andExpect(content().string(fireAlertDTO.toString()));
    }

    @Test
    void testGetFireAlertByAddress_NoResidents() throws Exception {
        String address = "123 Main St";
        FireAlertDTO fireAlertDTO = new FireAlertDTO(0, Set.of()); // Résidents initialisés à un Set vide

        Mockito.when(firestationService.getResidentsByAddress(address)).thenReturn(fireAlertDTO);

        mockMvc.perform(get("/fire")
                .param("address", address))
            .andExpect(status().isOk())
            .andExpect(content().string("No residents found at the specified address."));
    }

    @Test
    void testGetFireAlertByAddress_NullResidents() throws Exception {
        String address = "123 Main St";
        FireAlertDTO fireAlertDTO = new FireAlertDTO();
        fireAlertDTO.setResidents(null); // Simuler un `null`

        Mockito.when(firestationService.getResidentsByAddress(address)).thenReturn(fireAlertDTO);

        mockMvc.perform(get("/fire")
                .param("address", address))
            .andExpect(status().isOk())
            .andExpect(content().string("No residents found at the specified address."));
    }

    @Test
    void testGetFloodStations_HouseholdsFound() throws Exception {
        Set<Integer> stations = Set.of(1, 2);

        // Créez un objet FloodStationDTO valide
        ResidentInfoDTO residentInfo = new ResidentInfoDTO("Doe", "123-456-7890", 44,List.of("xanax"),List.of("pollen"));
        FloodStationDTO floodStationDTO = new FloodStationDTO("123 Main St", Set.of(residentInfo));

        // Mock des données de retour
        List<FloodStationDTO> households = List.of(floodStationDTO);
        Mockito.when(firestationService.getHouseholdsByStations(stations)).thenReturn(households);

        // Exécution du test
        mockMvc.perform(get("/flood/stations")
                .param("stations", "1,2"))
            .andExpect(status().isOk())
            .andExpect(content().string(households.stream()
                .map(FloodStationDTO::toString)
                .collect(Collectors.joining("\n"))));
    }

    @Test
    void testGetFloodStations_NoHouseholds() throws Exception {
        Set<Integer> stations = Set.of(1, 2);

        Mockito.when(firestationService.getHouseholdsByStations(stations)).thenReturn(List.of());

        mockMvc.perform(get("/flood/stations")
                .param("stations", "1,2"))
            .andExpect(status().isOk())
            .andExpect(content().string("No households found for the specified stations."));
    }
}
