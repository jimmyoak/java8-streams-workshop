package org.jimmyoak.workshop.stream.explain.e0;

import org.jimmyoak.workshop.stream.explain.utils.Info;
import org.junit.Test;

import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static java.util.Arrays.asList;

public class StreamTypes {
  @Test
  public void stream() throws Exception {
    Stream.of("ANYTHING");
  }

  @Test
  @Info("Stream of Integers with concrete methods for creation and usage")
  public void intStream() throws Exception {
    IntStream.range(0, 10); // 10 is excluded
    IntStream.rangeClosed(0, 10); // 10 is included
    IntStream.of(1, 2, 3);
  }

  @Test
  @Info("Stream of Doubles with concrete methods for creation and usage")
  public void doubleStream() throws Exception {
    DoubleStream.of(1, 2, 3);
  }

  @Test
  @Info("Stream of Long with concrete methods for creation and usage")
  public void longStream() throws Exception {
    LongStream.range(0, 10);// 10 is excluded
    LongStream.rangeClosed(0, 10); // 10 is included
    LongStream.of(1, 2, 3);
  }
}
