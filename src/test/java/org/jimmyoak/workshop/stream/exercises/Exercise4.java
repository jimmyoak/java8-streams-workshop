package org.jimmyoak.workshop.stream.exercises;

import org.junit.Test;

import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class Exercise4 {
  @Test
  public void join_strings_using_reduce() throws Exception {
    Stream<String> letters = Stream.of("Jimmy", "Kane", "Oak");

    Optional<String> fullName = letters.reduce((accumulator, value) -> accumulator + " " + value);

    assertThat(fullName).contains("Jimmy Kane Oak");
  }

  @Test
  public void count_distinct_elements() throws Exception {
    Stream<Integer> numbers = Stream.of(1, 2, 3, 1, 3, 2, 4, 5, 3, 5, 6, 7, 3, 5, 6);

    long count = numbers
        .distinct()
        .count();

    assertThat(count).isEqualTo(7);
  }

  @Test
  public void find_most_expensive_bill() throws Exception {
    Stream<Bill> bills = Stream.of(
        new Bill(1, 250),
        new Bill(2, 350),
        new Bill(3, 150),
        new Bill(4, 550)
    );

    Optional<Bill> mostExpensiveBill = bills.max(Comparator.comparingDouble(bill -> bill.amount));

    assertThat(mostExpensiveBill).hasValueSatisfying(bill -> assertThat(bill.id).isEqualTo(4));
  }

  private static class Bill {
    final int id;
    final double amount;

    public Bill(int id, double amount) {
      this.id = id;
      this.amount = amount;
    }
  }
}
