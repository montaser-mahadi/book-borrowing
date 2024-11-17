package com.github.montaser.library.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.montaser.library.dto.BorrowerDto;
import com.github.montaser.library.service.BorrowerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.List;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = BorrowerController.class)
@AutoConfigureMockMvc
class BorrowerControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BorrowerService borrowerService;

    private BorrowerDto testBorrowerDto;

    @BeforeEach
    void setUp() {
        testBorrowerDto = new BorrowerDto();
        testBorrowerDto.setId(1L);
        testBorrowerDto.setName("Jane Doe");
        testBorrowerDto.setEmail("janedoe@example.com");
    }

    @Test
    void testRegisterBorrower() throws Exception {
        Mockito.when(borrowerService.registerBorrower(Mockito.any(BorrowerDto.class))).thenReturn(testBorrowerDto);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/borrowers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testBorrowerDto)));

        resultActions.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(testBorrowerDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(testBorrowerDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(testBorrowerDto.getEmail()));
    }

    @Test
    void testGetAllBorrowers() throws Exception {
        List<BorrowerDto> borrowerDTOList = Collections.singletonList(testBorrowerDto);
        Mockito.when(borrowerService.getAllBorrowers()).thenReturn(borrowerDTOList);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/borrowers"));

        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(testBorrowerDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(testBorrowerDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email").value(testBorrowerDto.getEmail()));
    }
}
