package org.jimmyoak.workshop.stream.explain.e4;

import org.jimmyoak.workshop.stream.exercises.Exercise3;
import org.jimmyoak.workshop.stream.explain.utils.ExerciseWhenFinished;
import org.jimmyoak.workshop.stream.explain.utils.Info;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@ExerciseWhenFinished(Exercise3.class)
public class StreamCommonOperations {
  @Test
  @Info("Transforms stream values applying given function")
  public void map() throws Exception {
    List<Integer> stringLengths = Stream.of("a", "bb", "ccc")
        .map(String::length)
        .collect(Collectors.toList());

    assertThat(stringLengths).containsExactly(1, 2, 3);
  }

  @Test
  @Info("Transforms stream values applying given function that returns other streams")
  public void flatMap() throws Exception {
    List<Integer> stringLengths = Stream.of("a", "bb", "ccc")
        .flatMap(this::getLengths)
        .collect(Collectors.toList());

    assertThat(stringLengths).containsExactly(1, 2, 3);
  }

  @Test
  public void filter() throws Exception {
    List<String> atLeastThreeLetters = Stream.of("a", "bb", "ccc")
        .filter(it -> it.length() >= 2)
        .collect(Collectors.toList());

    assertThat(atLeastThreeLetters).containsExactly("bb", "ccc");
  }

  private Stream<Integer> getLengths(String... strings) {
    return Arrays.stream(strings).map(String::length);
  }
}
