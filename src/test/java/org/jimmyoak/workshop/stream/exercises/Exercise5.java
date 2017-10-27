package org.jimmyoak.workshop.stream.exercises;

import org.junit.Test;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;
import static java.util.stream.IntStream.rangeClosed;
import static org.assertj.core.api.Assertions.assertThat;

public class Exercise5 {
  @Test
  public void find_if_number_is_prime() throws Exception {
    int primeNumber = 7;
    int notPrimeNumber = 8;

    assertThat(isPrime(primeNumber)).isTrue();
    assertThat(isPrime(notPrimeNumber)).isFalse();
  }

  private boolean isPrime(int number) {
    return range(2, number)
        .noneMatch(it -> number % it == 0);
  }

  @Test
  public void find_primes_until_50() throws Exception {
    List<Integer> primeNumbers = rangeClosed(1, 50)
        .filter(this::isPrime)
        .boxed()
        .collect(toList());

    assertThat(primeNumbers).containsExactly(1, 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47);
  }
}
