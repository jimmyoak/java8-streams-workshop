package org.jimmyoak.workshop.stream.explain.e6;

import org.jimmyoak.workshop.stream.exercises.Exercise5;
import org.jimmyoak.workshop.stream.explain.utils.ExerciseWhenFinished;
import org.jimmyoak.workshop.stream.explain.utils.Info;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@ExerciseWhenFinished(Exercise5.class)
public class MixingStreamTypes {
  @Test
  public void mixing() throws Exception {
    double total = Stream.of(
        new Bill(1, 2.5, new UserId(1)),
        new Bill(2, 2.5, new UserId(2)),
        new Bill(3, 5, new UserId(3))
    ).mapToDouble(bill -> bill.amount)
        .sum();

    assertThat(total).isEqualTo(10);
  }

  @Test
  public void mixing2() throws Exception {
    @Info("Imagine debtorUserIds is some kind of input request which contains user ids of " +
        "users that still debt money to you")
    int[] debtorUserIds = {1, 2, 3};

    @Info("Bill repository will find bills of that debtors")
    BillRepository billRepository = Mockito.mock(BillRepository.class);
    when(billRepository.findByUserId(any(UserId.class))).thenReturn(
        singletonList(new Bill(1, 2, new UserId(1))),
        asList(new Bill(2, 7, new UserId(2)), new Bill(3, 5, new UserId(2))),
        emptyList()
    );

    @Info("Finding the total debt:")
    double totalDebtOfDebtors = Arrays.stream(debtorUserIds)
        .mapToObj(UserId::new)
        .flatMap(userId -> billRepository.findByUserId(userId).stream())
        .mapToDouble(bill -> bill.amount)
        .sum();

    assertThat(totalDebtOfDebtors).isEqualTo(14);
  }

  @Test
  public void boxed() throws Exception {
    List<Integer> collect = IntStream.range(0, 10)
        .boxed()
        .collect(Collectors.toList());

    assertThat(collect).containsExactly(0, 1, 3, 4, 5, 6, 7, 8, 9);
  }

  private static class Bill {
    final int id;
    final double amount;
    final UserId userId;

    Bill(int id, double amount, UserId userId) {
      this.id = id;
      this.amount = amount;
      this.userId = userId;
    }
  }

  private static class UserId {
    final int id;

    UserId(int id) {
      this.id = id;
    }
  }

  private interface BillRepository {
    List<Bill> findByUserId(UserId userId);
  }
}
