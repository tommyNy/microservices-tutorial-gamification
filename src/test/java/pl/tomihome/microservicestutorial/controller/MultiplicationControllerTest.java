package pl.tomihome.microservicestutorial.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
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
import pl.tomihome.microservicestutorial.service.MultiplicationService;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
@WebMvcTest(MultiplicationController.class) //ładuje tylko controllery
@Slf4j
public class MultiplicationControllerTest {

    @MockBean //mock utworzony w given
    private MultiplicationService multiplicationService;

    @Autowired
    private MockMvc mockMvc; //utworzony z @WebMvcTest

    private JacksonTester<Multiplication> json; //dostarcza metody do sprawdzania zawartosci json'ów

    @Before
    public void setUp() {
        JacksonTester.initFields(this, new ObjectMapper());
    }

    @Test
    public void getRandomMultiplicationTest() throws Exception {
        // given
        given(multiplicationService.createRandomMultiplication()).
                willReturn(new Multiplication(70, 20));

        // when
        MockHttpServletResponse response = mockMvc.perform(
                get("/multiplications/random")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // assert
        assertThat(response.getStatus())
                .isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString())
                .isEqualTo(
                        json.write(
                                new Multiplication(70, 20)
                        ).getJson());


    }
}
