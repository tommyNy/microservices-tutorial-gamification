package pl.tomihome.microservicestutorial.service;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.tomihome.microservicestutorial.domain.Multiplication;
import pl.tomihome.microservicestutorial.domain.MultiplicationResultAttempt;
import pl.tomihome.microservicestutorial.domain.User;
import pl.tomihome.microservicestutorial.event.EventDispatcher;
import pl.tomihome.microservicestutorial.event.MultiplicationSolvedEvent;
import pl.tomihome.microservicestutorial.repository.MultiplicationResultAttemptRepository;
import pl.tomihome.microservicestutorial.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;


public class MultiplicationServiceImplTest {

    private MultiplicationServiceImpl multiplicationServiceImpl;

    @Mock
    private RandomGeneratorService randomGeneratorService;

    @Mock
    private MultiplicationResultAttemptRepository attemptRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EventDispatcher eventDispatcher;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        multiplicationServiceImpl = new MultiplicationServiceImpl(
                randomGeneratorService,
                attemptRepository,
                userRepository,
                eventDispatcher);
    }

    @Test
    public void checkCorrectAttemptTest() {
        // given
        Multiplication multiplication = new Multiplication(50, 60);
        User user = new User("Edzio");

        MultiplicationResultAttempt attempt =
                new MultiplicationResultAttempt(user, multiplication, 3000, false);

        MultiplicationResultAttempt verifiedAttempt =
                new MultiplicationResultAttempt(user, multiplication, 3000, true);

        MultiplicationSolvedEvent event =
                new MultiplicationSolvedEvent(attempt.getId(), attempt.getUser().getId(), true);

        given(userRepository.findByAlias("Edzio"))
                .willReturn(Optional.empty());

        // when
        boolean attemptResult = multiplicationServiceImpl.checkAttempt(attempt);

        // assert
        assertThat(attemptResult).isTrue();
        verify(attemptRepository).save(verifiedAttempt);
        verify(eventDispatcher).send(eq(event));
    }

    @Test
    public void checkIncorrectAttemptTest() {
        // given
        Multiplication multiplication = new Multiplication(50, 60);
        User user = new User("Edzio");

        MultiplicationResultAttempt attempt =
                new MultiplicationResultAttempt(user, multiplication, 3010, false);

        MultiplicationSolvedEvent event =
                new MultiplicationSolvedEvent(attempt.getId(), attempt.getUser().getId(), false);

        given(userRepository.findByAlias("Edzio"))
                .willReturn(Optional.empty());

        // when
        boolean attemptResult = multiplicationServiceImpl.checkAttempt(attempt);

        // assert
        assertThat(attemptResult).isFalse();
        verify(attemptRepository).save(attempt);
        verify(eventDispatcher).send(eq(event));
    }

    @Test
    public void retrieveStatsTest() {
        // given
        Multiplication multiplication = new Multiplication(50, 60);
        User user = new User("Edzio");
        MultiplicationResultAttempt attempt1 = new
                MultiplicationResultAttempt(
                user, multiplication, 3010, false);
        MultiplicationResultAttempt attempt2 = new
                MultiplicationResultAttempt(
                user, multiplication, 3051, false);
        List<MultiplicationResultAttempt> latestAttempts =
                Lists.newArrayList(attempt1, attempt2);

        given(userRepository.findByAlias("Edzio")).willReturn(Optional.empty());
        given(attemptRepository.findTop5ByUserAliasOrderByIdDesc("Edzio")).willReturn(latestAttempts);

        // when
        List<MultiplicationResultAttempt> latestAttemptsResult = multiplicationServiceImpl.getStatsForUser("Edzio");

        // then
        assertThat(latestAttemptsResult).isEqualTo(latestAttempts);
    }
}
