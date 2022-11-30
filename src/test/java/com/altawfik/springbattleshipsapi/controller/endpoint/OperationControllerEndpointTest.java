package com.altawfik.springbattleshipsapi.controller.endpoint;

import com.altawfik.springbattleshipsapi.api.request.PlayerNumber;
import com.altawfik.springbattleshipsapi.api.request.PlayerSetupRequest;
import com.altawfik.springbattleshipsapi.controller.OperationController;
import com.altawfik.springbattleshipsapi.error.InvalidPlayerNameException;
import com.altawfik.springbattleshipsapi.service.BattleInitialisationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static com.altawfik.springbattleshipsapi.controller.endpoint.ControllerEndpointTestUtils.asJsonString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(value = OperationController.class, properties = "embedded.containers.enabled=false")
public class OperationControllerEndpointTest {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    private BattleInitialisationService battleInitialisationService;

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

        doThrow(InvalidPlayerNameException.class).when(battleInitialisationService).initPlayer(id, playerSetupRequest);

        mockMvc.perform(post(String.format("/battle/initialise/%s/player", id))
                                .content(asJsonString(playerSetupRequest))
                                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.error.message").value("Invalid player name provided: " + invalidBlankPlayerName));
    }
}
