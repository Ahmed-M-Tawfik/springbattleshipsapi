package com.altawfik.springbattleshipsapi.controller.endpoint;

import com.altawfik.springbattleshipsapi.api.request.PlayerNumber;
import com.altawfik.springbattleshipsapi.api.request.PlayerSetupRequest;
import com.altawfik.springbattleshipsapi.api.response.BattleResponse;
import com.altawfik.springbattleshipsapi.api.response.BattleStateResponse;
import com.altawfik.springbattleshipsapi.api.response.PlayerResponse;
import com.altawfik.springbattleshipsapi.controller.OperationController;
import com.altawfik.springbattleshipsapi.error.BattleNotFoundExceptionBuilder;
import com.altawfik.springbattleshipsapi.error.InvalidPlayerNameExceptionBuilder;
import com.altawfik.springbattleshipsapi.errorhandling.WebErrorHandlerConfig;
import com.altawfik.springbattleshipsapi.model.Ship;
import com.altawfik.springbattleshipsapi.service.BattleInitialisationService;
import com.altawfik.springbattleshipsapi.service.BattleRetrievalService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {OperationController.class, WebErrorHandlerConfig.class})
@ActiveProfiles("test")
@WebMvcTest(value = OperationController.class, properties = "embedded.containers.enabled=false")
public class OperationControllerEndpointTest {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    private BattleInitialisationService battleInitialisationService;
    @MockBean
    private BattleRetrievalService battleRetrievalService;

    @Test
    public void shouldGenerateUuidWhenAllocatingNewBattle() throws Exception {
        UUID expectedId = UUID.randomUUID();
        when(battleInitialisationService.newBattle()).thenReturn(expectedId);

        mockMvc.perform(post("/battle/allocate"))
               .andExpect(status().isCreated())
               .andExpect(content().string("\"" + expectedId + "\""));
    }

    @Test
    public void shouldAcceptPlayerWhenInitialisingBattle() throws Exception {
        UUID id = UUID.randomUUID();
        String playerName = "playerName";
        PlayerSetupRequest playerSetupRequest = new PlayerSetupRequest(PlayerNumber.PLAYER_TWO, playerName);

        mockMvc.perform(post(String.format("/battle/initialise/%s/player", id))
               .content(asJsonString(playerSetupRequest))
               .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk());

        verify(battleInitialisationService).initPlayer(id, playerSetupRequest);
    }

    @Test
    public void shouldNotAcceptPlayerGivenInitialisingBattleWhenPlayerNameInvalid() throws Exception {
        UUID id = UUID.randomUUID();
        String invalidBlankPlayerName = " ";
        PlayerSetupRequest playerSetupRequest = new PlayerSetupRequest(PlayerNumber.PLAYER_TWO, invalidBlankPlayerName);

        doThrow(new InvalidPlayerNameExceptionBuilder(invalidBlankPlayerName).build()).when(battleInitialisationService).initPlayer(id, playerSetupRequest);

        mockMvc.perform(post(String.format("/battle/initialise/%s/player", id))
                                .content(asJsonString(playerSetupRequest))
                                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.error.message").value("Invalid payload"));
    }

    @Test
    public void shouldReturnBattleRepresentationWhenBattleExists() throws Exception {
        UUID id = UUID.randomUUID();

        BattleResponse battleResponse = BattleResponse.builder()
                .state(BattleStateResponse.Initialisation)
                .players(new PlayerResponse[]{new PlayerResponse("playerName", new Ship[0])}).build();
        when(battleRetrievalService.getBattle(id)).thenReturn(battleResponse);

        mockMvc.perform(get(String.format("/battle/%s", id)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state").value("Initialisation"));
    }

    @Test
    public void shouldThrowErrorWhenBattleDoesNotExist() throws Exception {
        UUID id = UUID.randomUUID();

        doThrow(new BattleNotFoundExceptionBuilder(id).build()).when(battleRetrievalService).getBattle(id);

        mockMvc.perform(get(String.format("/battle/%s", id)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error.message").value(
                        String.format(BattleNotFoundExceptionBuilder.NOT_FOUND_MESSAGE, id)));
    }

    static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
