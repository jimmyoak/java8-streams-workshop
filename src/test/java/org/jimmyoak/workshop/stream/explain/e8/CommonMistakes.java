package org.jimmyoak.workshop.stream.explain.e8;

import org.jimmyoak.workshop.stream.explain.utils.Info;
import org.junit.Test;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class CommonMistakes {
  @Test
  @Info("An already used stream throws an exception. Streams can be chained and used only once.")
  public void stream_are_not_reusable() throws Exception {
    Stream<Integer> numbers = Stream.of(1, 2, 3, 2, 3);
    Stream<Integer> distinct = numbers.distinct();//we use numbers here

    assertThatExceptionOfType(IllegalStateException.class)
        .isThrownBy(() -> numbers.map(String::valueOf).collect(toList()));

    List<String> distinctAsString = distinct.map(String::valueOf).collect(toList());
    assertThat(distinctAsString).containsExactly("1", "2", "3");
  }

  @Test
  @Info("New instances are returned but the old ones chained in operations are in illegal state.")
  public void are_immutable() throws Exception {
    Stream<Integer> numbers = Stream.of(1, 2, 3);
    Stream<Integer> distinct = numbers.distinct();

    assertThat(numbers).isNotSameAs(distinct);
    assertThatExceptionOfType(IllegalStateException.class)
        .isThrownBy(() -> numbers.map(String::valueOf));
  }
}
