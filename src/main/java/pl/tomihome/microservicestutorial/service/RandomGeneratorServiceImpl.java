package pl.tomihome.microservicestutorial.service;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class RandomGeneratorServiceImpl implements RandomGeneratorService {

    private final int MIN_FACTOR = 11;
    private final int MAX_FACTOR = 99;

    @Override
    public int generateRandomFactory() {
        return new Random().nextInt((MAX_FACTOR - MIN_FACTOR) + 1) + MIN_FACTOR;
    }
}
