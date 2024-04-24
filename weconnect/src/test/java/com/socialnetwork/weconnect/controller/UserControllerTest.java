package com.socialnetwork.weconnect.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.socialnetwork.weconnect.Service.AuthenticationService;
import com.socialnetwork.weconnect.Service.UserService;
import com.socialnetwork.weconnect.dto.request.RegisterRequest;
import com.socialnetwork.weconnect.dto.response.ApiResponse;
import com.socialnetwork.weconnect.dto.response.CntResponse;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	AuthenticationService authenticationService;

	private CntResponse cntResponse;
	private RegisterRequest registerRequest;

	@BeforeEach
	void initData() {
		registerRequest = RegisterRequest.builder().userName("tunvv").email("Checktest@gmail.com").password("12345678")
				.birthday("2000-01-01").build();

		cntResponse = CntResponse.builder().resultCnt(1).message("Register success").build();
	}

	@Test
	void createUser() throws Exception {
		//registerRequest.setUserName("joh");
		// GIVEN
		  ObjectMapper objectMapper = new ObjectMapper();
		  objectMapper.registerModules(new JavaTimeModule());
		String content = objectMapper.writeValueAsString(registerRequest);
		
        Mockito.when(authenticationService.register(ArgumentMatchers.any()))
          .thenReturn(cntResponse);
        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("code")
                        .value(1000)

        );
	}
}
