package org.jimmyoak.workshop.stream.explain.e1;

import org.jimmyoak.workshop.stream.explain.utils.Info;
import org.junit.Test;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class HowToCreateAStreamTheBoringPart {
  @Test
  public void empty() throws Exception {
    Stream<String> empty = Stream.empty();

    assertThat(empty).isEmpty();
  }

  @Test
  public void create_with_single_value() throws Exception {
    Object anObject = new Object();

    Stream<Object> streamOfObject = Stream.of(anObject);

    assertThat(streamOfObject).contains(anObject);
  }

  @Test
  public void create_with_many_values() throws Exception {
    Stream<String> abc = Stream.of("a", "b", "c");

    assertThat(abc).hasSize(3);
  }

  @Test
  public void create_stream_with_iterator() throws Exception {
    Stream<Integer> infiniteStream = Stream.iterate(0, t -> t + 1);

    Stream<Integer> finiteStream = infiniteStream.limit(5);

    assertThat(finiteStream).hasSize(5);
  }

  @Test
  public void create_stream_with_generator() throws Exception {
    @Info("Infinite stream in this step. It has no limits so we should limit it.")
    Stream<Integer> infiniteStream = Stream.generate(() -> ThreadLocalRandom.current().nextInt());

    Stream<Integer> finiteStream = infiniteStream.limit(5);

    assertThat(finiteStream).hasSize(5);
  }

  @Test
  public void concat_two_stream() throws Exception {
    Stream<Integer> oneTwoThree = Stream.of(1, 2, 3);
    Stream<Integer> fourFiveSix = Stream.of(4, 5, 6);

    Stream<Integer> oneTwoThreeFourFiveSix = Stream.concat(oneTwoThree, fourFiveSix);

    assertThat(oneTwoThreeFourFiveSix).containsExactly(1, 2, 3, 4, 5, 6);
  }

  @Test
  @Info("The difference between add and accept method is that add returns builder for fluent interface and accept returns void")
  public void create_with_builder() throws Exception {
    Stream.Builder<Object> builder = Stream.builder();
    builder.accept(0);

    for (int i = 1; i <= 6; i++) {
      builder.add(i);
    }

    Stream<Object> numbers = builder.add(7).add(8).build();

    assertThat(numbers).containsExactly(0, 1, 2, 3, 4, 5, 6, 7, 8);
  }
}
