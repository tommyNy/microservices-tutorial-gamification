package pl.tomihome.microservicestutorial.domain;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
@Entity
public class Multiplication {

    @Id
    @GeneratedValue
    @Column(name = "MULTIPLICATION_ID")
    private Long id;

    @Column(name = "FACTOR_A")
    private  final int factorA;
    @Column(name = "FACTOR_B")
    private final int factorB;

    Multiplication() {
        this(0, 0);
    }
}
