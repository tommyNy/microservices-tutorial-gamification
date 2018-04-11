package pl.tomihome.microservicestutorial.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import pl.tomihome.microservicestutorial.domain.Multiplication;
import pl.tomihome.microservicestutorial.domain.MultiplicationResultAttempt;
import pl.tomihome.microservicestutorial.domain.User;
import pl.tomihome.microservicestutorial.event.EventDispatcher;
import pl.tomihome.microservicestutorial.event.MultiplicationSolvedEvent;
import pl.tomihome.microservicestutorial.repository.MultiplicationResultAttemptRepository;
import pl.tomihome.microservicestutorial.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class MultiplicationServiceImpl implements MultiplicationService {

    private RandomGeneratorService randomGeneratorService;
    private MultiplicationResultAttemptRepository attemptRepository;
    private UserRepository userRepository;
    private EventDispatcher eventDispatcher;

    @Autowired
    public MultiplicationServiceImpl(RandomGeneratorService randomGeneratorService,
                                     MultiplicationResultAttemptRepository attemptRepository,
                                     UserRepository userRepository,
                                     EventDispatcher eventDispatcher) {
        this.randomGeneratorService = randomGeneratorService;
        this.attemptRepository = attemptRepository;
        this.userRepository = userRepository;
        this.eventDispatcher = eventDispatcher;
    }

    @Override
    public Multiplication createRandomMultiplication() {
        int factoryA = randomGeneratorService.generateRandomFactory();
        int factoryB = randomGeneratorService.generateRandomFactory();
        return new Multiplication(factoryA, factoryB);
    }

    @Transactional //?
    @Override
    public boolean checkAttempt(MultiplicationResultAttempt attempt) {
        log.info("select for user");
        Optional<User> user = userRepository.findByAlias(attempt.getUser().getAlias()); //TODO learn about optional
        log.info("user: " + user);

        //??? zapobiega wyslaniu zgloszen oznaczonych jako poprawne
        //jesli true rzuca IllegalArgumentException
        Assert.isTrue(!attempt.isCorrect(), "nie mozesz wyslac zgloszenia oznaczonego jako poprawne. " +
                "My weryfikujemy poprawnosc!");

        boolean correct = attempt.getResultAttempt() == attempt.getMultiplication().getFactorA() *
                attempt.getMultiplication().getFactorB();

        //kopia z racji, ze MultiplicationResultAttempt jest final
        MultiplicationResultAttempt checkedAttempt =
                new MultiplicationResultAttempt(
                        user.orElse(attempt.getUser()),
                        attempt.getMultiplication(),
                        attempt.getResultAttempt(),
                        correct);
        //przechowuje tylko sprawdzone zgloszenia
        attemptRepository.save(checkedAttempt);

        //wysylka event
        eventDispatcher.send(new MultiplicationSolvedEvent(
                checkedAttempt.getId(),
                checkedAttempt.getUser().getId(),
                checkedAttempt.isCorrect())
        );

        return correct;
    }

    @Override
    public List<MultiplicationResultAttempt> getStatsForUser(String userAlias) {
        return attemptRepository.findTop5ByUserAliasOrderByIdDesc(userAlias);
    }
}
