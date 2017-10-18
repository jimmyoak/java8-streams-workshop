package org.jimmyoak.workshop.stream.explain.e5;

import org.jimmyoak.workshop.stream.explain.utils.Info;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.inOrder;

public class StreamMoreOperations {

  @Test
  public void reduce() throws Exception {
    Integer result = Stream.of(5, 3, 2).reduce(10, (accumulator, value) -> accumulator - value);

    assertThat(result).isEqualTo(0);
  }

  @Test
  @Info("forEach ends stream method chaining")
  public void forEach() throws Exception {
    StringRepository stringRepository = Mockito.mock(StringRepository.class);

    Stream.of("A", "B", "C")
        .forEach(stringRepository::persist);

    InOrder inOrder = inOrder(stringRepository);
    inOrder.verify(stringRepository).persist("A");
    inOrder.verify(stringRepository).persist("B");
    inOrder.verify(stringRepository).persist("C");
  }

  @Test
  public void peek() throws Exception {
    StringRepository stringRepository = Mockito.mock(StringRepository.class);

    String result = Stream.of("A", "B", "C")
        .peek(stringRepository::persist)
        .collect(Collectors.joining());

    InOrder inOrder = inOrder(stringRepository);
    inOrder.verify(stringRepository).persist("A");
    inOrder.verify(stringRepository).persist("B");
    inOrder.verify(stringRepository).persist("C");

    assertThat(result).isEqualTo("ABC");
  }

  @Test
  public void findAnyAndFindFirst() throws Exception {
    Optional<String> any = Stream.of("A", "B", "C").findAny();
    Optional<String> first = Stream.of("A", "B", "C").findFirst();

    assertThat(any).hasValueSatisfying(value -> assertThat(value).isIn("A", "B", "C"));
    assertThat(first).contains("A");
  }

  @Test
  public void distinct() throws Exception {
    List<String> letters = Stream.of("A", "A", "A", "B", "B")
        .distinct()
        .collect(Collectors.toList());

    assertThat(letters).containsExactly("A", "B");
  }

  @Test
  public void count() throws Exception {
    long count = Stream.of("A", "A", "A", "B", "B")
        .count();

    assertThat(count).isEqualTo(5);
  }

  @Test
  public void allMatch() throws Exception {
    boolean result1 = Stream.of("AA", "BB").allMatch(it -> it.length() > 1);
    boolean result2 = Stream.of("A", "BB").allMatch(it -> it.length() > 1);

    assertThat(result1).isTrue();
    assertThat(result2).isFalse();
  }

  @Test
  public void anyMatch() throws Exception {
    boolean result1 = Stream.of("AA", "BB").anyMatch(it -> it.length() > 1);
    boolean result2 = Stream.of("A", "BB").anyMatch(it -> it.length() > 1);
    boolean result3 = Stream.of("A", "B").anyMatch(it -> it.length() > 1);

    assertThat(result1).isTrue();
    assertThat(result2).isTrue();
    assertThat(result3).isFalse();
  }

  @Test
  public void noneMatch() throws Exception {
    boolean result1 = Stream.of("A", "B").noneMatch(String::isEmpty);
    boolean result2 = Stream.of("", "B").noneMatch(String::isEmpty);

    assertThat(result1).isTrue();
    assertThat(result2).isFalse();
  }

  @Test
  public void sorted() throws Exception {
    List<User> sortedUsers = Stream.of(
        new User(2, "Jimmy"),
        new User(5, "Jose"),
        new User(4, "Jose"),
        new User(1, "Adrian")
    ).sorted((left, right) -> {
      if (left.id < right.id) return -1;
      else if (left.id > right.id) return 1;
      else return 0;
    }).collect(Collectors.toList());

    assertThat(sortedUsers)
        .extracting("id")
        .containsExactly(1, 2, 4, 5);
  }

  @Test
  public void max() throws Exception {
    Optional<Integer> max = Stream.of(1, 2, 3)
        .max((left, right) -> {
          if (left < right) return -1;
          else if (left > right) return 1;
          else return 0;
        });

    assertThat(max).contains(3);
  }

  @Test
  public void min() throws Exception {
    Optional<Integer> min = Stream.of(1, 2, 3)
        .min((left, right) -> {
          if (left < right) return -1;
          else if (left > right) return 1;
          else return 0;
        });

    assertThat(min).contains(1);
  }

  private interface StringRepository {
    void persist(String toPersist);
  }

  private static class User {
    final int id;
    final String name;

    public User(int id, String name) {
      this.id = id;
      this.name = name;
    }
  }
}
