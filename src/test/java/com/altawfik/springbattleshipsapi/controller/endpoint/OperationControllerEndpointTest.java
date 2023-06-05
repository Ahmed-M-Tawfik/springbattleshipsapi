package com.altawfik.springbattleshipsapi.controller.endpoint;

import com.altawfik.springbattleshipsapi.api.request.PlayerNumber;
import com.altawfik.springbattleshipsapi.api.request.PlayerSetupRequest;
import com.altawfik.springbattleshipsapi.api.request.ShipPlacementRequest;
import com.altawfik.springbattleshipsapi.api.response.BattleResponse;
import com.altawfik.springbattleshipsapi.api.response.BattleStateResponse;
import com.altawfik.springbattleshipsapi.api.response.PlayerResponse;
import com.altawfik.springbattleshipsapi.controller.OperationController;
import com.altawfik.springbattleshipsapi.error.InvalidPlayerNameExceptionBuilder;
import com.altawfik.springbattleshipsapi.errorhandling.WebErrorHandlerConfig;
import com.altawfik.springbattleshipsapi.errorhandling.exception.ContentExceptionBuilder;
import com.altawfik.springbattleshipsapi.model.BoardCoordinate;
import com.altawfik.springbattleshipsapi.model.Ship;
import com.altawfik.springbattleshipsapi.model.ShipOrientation;
import com.altawfik.springbattleshipsapi.service.BattleInitialisationService;
import com.altawfik.springbattleshipsapi.service.BattleRetrievalService;
import com.altawfik.springbattleshipsapi.service.shipconfig.StandardShipConfigurationProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

    private final ObjectMapper objectMapper = new ObjectMapper();

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
        UUID battleId = UUID.randomUUID();
        String playerName = "playerName";
        PlayerSetupRequest playerSetupRequest = new PlayerSetupRequest(PlayerNumber.PLAYER_TWO, playerName);

        mockMvc.perform(post(String.format("/battle/initialise/%s/player", battleId))
               .content(asJsonString(playerSetupRequest))
               .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk());


        verify(battleInitialisationService).initPlayer(battleId, playerSetupRequest);
    }

    @Test
    public void shouldNotAcceptPlayerGivenInitialisingBattleWhenPlayerNameInvalid() throws Exception {
        UUID battleId = UUID.randomUUID();
        String invalidBlankPlayerName = " ";
        PlayerSetupRequest playerSetupRequest = new PlayerSetupRequest(PlayerNumber.PLAYER_TWO, invalidBlankPlayerName);

        doThrow(new InvalidPlayerNameExceptionBuilder(invalidBlankPlayerName).build()).when(battleInitialisationService).initPlayer(battleId, playerSetupRequest);

        mockMvc.perform(post(String.format("/battle/initialise/%s/player", battleId))
                                .content(asJsonString(playerSetupRequest))
                                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.error.message").value("Invalid payload"));
    }

    @Test
    public void shouldReturnBattleRepresentationWhenBattleExists() throws Exception {
        UUID battleId = UUID.randomUUID();

        BattleResponse battleResponse = BattleResponse.builder()
                .state(BattleStateResponse.Initialisation)
                .players(new PlayerResponse[]{new PlayerResponse("playerName", new Ship[0])}).build();
        when(battleRetrievalService.getBattle(battleId)).thenReturn(battleResponse);

        mockMvc.perform(get(String.format("/battle/%s", battleId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state").value("Initialisation"));
    }

    @Test
    public void shouldThrowErrorWhenBattleDoesNotExist() throws Exception {
        UUID battleId = UUID.randomUUID();

        doThrow(new ContentExceptionBuilder(HttpStatus.NOT_FOUND, String.format("Battle with UUID %s not found", battleId)).build())
                .when(battleRetrievalService).getBattle(battleId);

        mockMvc.perform(get(String.format("/battle/%s", battleId)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error.message").value(
                        String.format("Battle with UUID %s not found", battleId)));
    }

    @Test
    public void shouldPlaceShipOnBoard() throws Exception {
        UUID battleId = UUID.randomUUID();
        PlayerNumber playerNumber = PlayerNumber.PLAYER_ONE;
        BoardCoordinate coord = new BoardCoordinate(5, 5);
        ShipPlacementRequest shipPlacementRequest = new ShipPlacementRequest(0, ShipOrientation.HORIZONTAL_RIGHT, coord);

        mockMvc.perform(put(String.format("/battle/initialise/%s/player/%s/board", battleId, playerNumber))
                        .content(asJsonString(shipPlacementRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(battleInitialisationService).placeShipOnBoard(battleId, playerNumber, shipPlacementRequest);
    }

    @Test
    public void shouldRetrieveUnplacedShips() throws Exception {
        UUID battleId = UUID.randomUUID();
        PlayerNumber playerNumber = PlayerNumber.PLAYER_ONE;

        Ship[] unplacedShips = new StandardShipConfigurationProvider().getShips();
        when(battleInitialisationService.getUnplacedShips(battleId, playerNumber)).thenReturn(unplacedShips);

        MvcResult result = mockMvc.perform(get(String.format("/battle/initialise/%s/player/%s/unplaced-ships", battleId, playerNumber)))
                .andExpect(status().isOk())
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        Ship[] responseShips = objectMapper.readValue(responseJson, Ship[].class);

        assertThat(responseShips).isEqualTo( // replicate deserialization to ignore parentShip in ShipSections
                objectMapper.readValue(asJsonString(unplacedShips), Ship[].class));

        verify(battleInitialisationService).getUnplacedShips(battleId, playerNumber);
    }

    @Test
    public void startBattle() throws Exception {
        UUID battleId = UUID.randomUUID();

        mockMvc.perform(post(String.format("/battle/initialise/%s/start", battleId)))
                .andExpect(status().isOk());
    }

    private String asJsonString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
