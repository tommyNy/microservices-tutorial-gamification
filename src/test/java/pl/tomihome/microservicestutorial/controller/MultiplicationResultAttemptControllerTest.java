package pl.tomihome.microservicestutorial.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import pl.tomihome.microservicestutorial.domain.Multiplication;
import pl.tomihome.microservicestutorial.domain.MultiplicationResultAttempt;
import pl.tomihome.microservicestutorial.domain.User;
import pl.tomihome.microservicestutorial.service.MultiplicationService;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@WebMvcTest(MultiplicationResultAttemptController.class)
public class MultiplicationResultAttemptControllerTest {

    @MockBean
    private MultiplicationService multiplicationService;

    @Autowired
    private MockMvc mockMvc;

    private JacksonTester<MultiplicationResultAttempt> jsonResult;
    private JacksonTester<List<MultiplicationResultAttempt>> jsonResultAttemptList;

    @Before
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
    }

    @Test
    public void postResultReturnCorrect() throws Exception {
        genericParamerizedTest(true);
    }

    @Test
    public void postResultReturnIncorrect() throws Exception {
        genericParamerizedTest(false);
    }

    void genericParamerizedTest(boolean correct) throws Exception {
        // given
        given(multiplicationService.checkAttempt(any(MultiplicationResultAttempt.class)))
                .willReturn(correct);
        User user = new User("Czesio");
        Multiplication multiplication = new Multiplication(50, 70);
        MultiplicationResultAttempt attempt = new MultiplicationResultAttempt(
                user,
                multiplication,
                3500,
                correct);

        // when
        MockHttpServletResponse response = mockMvc.perform(
                post("/results")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonResult.write(attempt)
                                .getJson()))
                .andReturn()
                .getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(
                jsonResult.write(new MultiplicationResultAttempt(
                        attempt.getUser(),
                        attempt.getMultiplication(),
                        attempt.getResultAttempt(),
                        correct
                )).getJson()
        );
    }

    @Test
    public void getUserStats() throws Exception {
        // given
        User user = new User("Edzio");
        Multiplication multiplication = new Multiplication(50, 70);
        MultiplicationResultAttempt multiplicationResultAttempt = new MultiplicationResultAttempt(
                user,
                multiplication,
                3500,
                true
        );
        List<MultiplicationResultAttempt> recentAttempts =
                Lists.newArrayList(multiplicationResultAttempt, multiplicationResultAttempt);
        given(multiplicationService.getStatsForUser("Edzio")).willReturn(recentAttempts);

        // when
        MockHttpServletResponse response = mockMvc.perform(
                get("/results").param("alias", "Edzio"))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(
                jsonResultAttemptList.write(recentAttempts)
                .getJson());
    }
}
