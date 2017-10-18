package org.jimmyoak.workshop.stream.explain.e3;

import org.jimmyoak.workshop.stream.exercises.Exercise2;
import org.jimmyoak.workshop.stream.explain.utils.ExerciseWhenFinished;
import org.jimmyoak.workshop.stream.explain.utils.Info;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@ExerciseWhenFinished(Exercise2.class)
public class CollectingStreams {
  @Test
  public void collect_to_list() throws Exception {
    List<java.lang.Integer> numbers = Stream.of(1, 2, 3).collect(Collectors.toList());

    assertThat(numbers).containsExactly(1, 2, 3);
  }

  @Test
  public void collect_to_map() throws Exception {
    Map<java.lang.Integer, String> userIdToName = Stream.of(
        new User(1, "Jimmy"),
        new User(2, "Oak")
    ).collect(Collectors.toMap(it -> it.id, it -> it.name));

    assertThat(userIdToName)
        .containsEntry(1, "Jimmy")
        .containsEntry(2, "Oak");
  }

  @Test
  public void collect_joining_to_string() throws Exception {
    String userNamesConcatenated = Stream.of("Jimmy", "Oak").collect(Collectors.joining());
    String userNamesConcatenatedWithComa = Stream.of("Jimmy", "Oak").collect(Collectors.joining(", "));

    assertThat(userNamesConcatenated).isEqualTo("JimmyOak");
    assertThat(userNamesConcatenatedWithComa).isEqualTo("Jimmy, Oak");
  }

  @Test
  @Info("No .sum() method")
  public void sum() throws Exception {
    @Info("Unrecommended")
    Integer total = Stream.of(
        new SomeInteger(1),
        new SomeInteger(2),
        new SomeInteger(3)
    ).collect(Collectors.summingInt(value -> value.number));

    @Info("Recommended")
    Integer sameTotal = Stream.of(
        new SomeInteger(1),
        new SomeInteger(2),
        new SomeInteger(3)
    ).mapToInt(value -> value.number)
        .sum();

    assertThat(total).isEqualTo(6);
    assertThat(sameTotal).isEqualTo(6);
  }

  private static class User {
    final int id;
    final String name;

    User(int id, String name) {
      this.id = id;
      this.name = name;
    }
  }

  private static class SomeInteger {
    final int number;

    public SomeInteger(int number) {
      this.number = number;
    }
  }
}
