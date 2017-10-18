package org.jimmyoak.workshop.stream.exercises;

import org.junit.Test;

import java.util.Arrays;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class Exercise2 {
  @Test
  public void collect_stream_into_map() throws Exception {
    Stream<User> userStream = Stream.of(
        new User(1, "Jimmy"),
        new User(2, "Kane"),
        new User(3, "Oak")
    );

    Map<Integer, User> userDictionary = userStream.collect(Collectors.toMap(it -> it.userId, it -> it));

    assertThat(userDictionary)
        .hasEntrySatisfying(1, it -> assertThat(it.name).isEqualTo("Jimmy"))
        .hasEntrySatisfying(2, it -> assertThat(it.name).isEqualTo("Kane"))
        .hasEntrySatisfying(3, it -> assertThat(it.name).isEqualTo("Oak"));
  }

  @Test
  public void average() throws Exception {
    IntStream numbers = IntStream.of(5, 10, 15);

    OptionalDouble average = numbers.average();

    assertThat(average.getAsDouble()).isEqualTo(10);
  }

  @Test
  public void name_as_array_into_string() throws Exception {
    String[] nameParts = {"Jimmy", "Kane", "Oak"};

    String fullName = Arrays.stream(nameParts)
        .map(String::valueOf)
        .collect(Collectors.joining(","));

    assertThat(fullName).isEqualTo("Jimmy Kane Oak");
  }

  private static class User {
    final int userId;
    final String name;

    public User(int userId, String name) {
      this.userId = userId;
      this.name = name;
    }
  }
}
