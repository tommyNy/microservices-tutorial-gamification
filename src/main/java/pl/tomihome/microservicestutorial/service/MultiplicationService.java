package pl.tomihome.microservicestutorial.service;

import pl.tomihome.microservicestutorial.domain.Multiplication;
import pl.tomihome.microservicestutorial.domain.MultiplicationResultAttempt;

import java.util.List;

public interface MultiplicationService {
    Multiplication createRandomMultiplication();
    boolean checkAttempt(MultiplicationResultAttempt resultAttempt);
    List<MultiplicationResultAttempt> getStatsForUser(String userAlias);
}
