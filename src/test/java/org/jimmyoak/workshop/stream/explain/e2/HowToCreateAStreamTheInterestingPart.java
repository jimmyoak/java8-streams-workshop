package org.jimmyoak.workshop.stream.explain.e2;

import org.jimmyoak.workshop.stream.exercises.Exercise1;
import org.jimmyoak.workshop.stream.explain.utils.ExerciseWhenFinished;
import org.jimmyoak.workshop.stream.explain.utils.Info;
import org.junit.Test;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;

@ExerciseWhenFinished(Exercise1.class)
public class HowToCreateAStreamTheInterestingPart {
  @Test
  public void from_collection() throws Exception {
    Collection<String> strings = asList("A", "B", "C");

    Stream<String> stream = strings.stream();

    assertThat(stream).containsExactly("A", "B", "C");
  }

  @Test
  @Info("YOU CAN'T BUT...")
  public void from_map() throws Exception {
    Map<String, Integer> map = singletonMap("one", 1);

    Stream<Map.Entry<String, Integer>> mapKeyValues = map.entrySet().stream();

    assertThat(mapKeyValues).hasSize(1);
  }
}
