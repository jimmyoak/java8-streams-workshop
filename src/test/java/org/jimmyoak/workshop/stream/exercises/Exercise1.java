package org.jimmyoak.workshop.stream.exercises;

import org.junit.Test;

import java.util.List;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class Exercise1 {
  @Test
  public void create_stream_of_strings() throws Exception {
    Stream<String> stream = Stream.of("J", "I", "M", "M", "Y");

    assertThat(stream).containsExactly("J", "I", "M", "M", "Y");
  }

  @Test
  public void create_stream_from_collection() throws Exception {
    List<Integer> numbers = asList(1, 2, 3, 4, 5);
    Stream<Integer> stream = numbers.stream();

    assertThat(stream).containsExactly(1, 2, 3, 4, 5);
  }
}
