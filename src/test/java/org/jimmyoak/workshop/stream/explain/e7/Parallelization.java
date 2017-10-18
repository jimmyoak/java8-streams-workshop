package org.jimmyoak.workshop.stream.explain.e7;

import org.jimmyoak.workshop.stream.explain.utils.Info;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.singletonList;
import static java.util.stream.IntStream.rangeClosed;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class Parallelization {
  @Test
  @Info("Order may not be preserved when parallizing")
  public void parallel() throws Exception {
    List<Integer> numbers = Stream.of(1, 2, 3)
        .parallel()
        .peek(it -> System.out.println(Thread.currentThread().getName()))
        .map(it -> it + 1)
        .collect(Collectors.toList());

    assertThat(numbers).containsOnly(2, 3, 4);
  }

  @Test
  public void parallelizeHeavyOperation() throws Exception {
    PictureRepository pictureRepository = Mockito.mock(PictureRepository.class);
    when(pictureRepository.findByUserId(any())).thenAnswer(invocation -> {
      Thread.sleep(1000);
      return singletonList(new Picture("http://doge.com/picture.jpg"));
    });

    List<Stream<Picture>> allUserPicturesWithNoParalellization = runWithChrono(
        "Sequential",
        () -> rangeClosed(1, 10)
            .mapToObj(UserId::new)
            .map(userId -> pictureRepository.findByUserId(userId).stream())
            .collect(Collectors.toList())
    );

    List<Stream<Picture>> allUserPicturesWithParalellization = runWithChrono(
        "Parallel",
        () -> rangeClosed(1, 10)
            .mapToObj(UserId::new)
            .parallel()
            .map(userId -> pictureRepository.findByUserId(userId).stream())
            .collect(Collectors.toList())
    );

    assertThat(allUserPicturesWithNoParalellization).hasSize(10);
    assertThat(allUserPicturesWithParalellization).hasSize(10);
  }

  @Test
  @Info("Oh nice! Why not to use it always? Because thread execution and coordination is really expensive in real world cases." +
      "The best option is to try both and make a performance test. Take in account immutability and atomicity when working in parallel.")
  public void expensive() throws Exception {
    SomethingRepository somethingRepository = Mockito.mock(SomethingRepository.class);
    when(somethingRepository.persist(any())).then(returnsFirstArg());

    runWithChrono(
        "Simple operation",
        () -> rangeClosed(1, 1000)
            .mapToObj(String::valueOf)
            .map(Something::new)
            .map(somethingRepository::persist)
            .collect(Collectors.toList())
    );

    runWithChrono(
        "Simple parallelized operation",
        () -> rangeClosed(1, 1000)
            .parallel()
            .mapToObj(String::valueOf)
            .map(Something::new)
            .map(somethingRepository::persist)
            .collect(Collectors.toList())
    );
  }

  @Test
  @Info("Stream can be parallel or sequential and you cannot mix them")
  public void going_back_to_sequential() throws Exception {
    PictureRepository pictureRepository = Mockito.mock(PictureRepository.class);
    when(pictureRepository.findByUserId(any())).thenAnswer(invocation -> {
      Thread.sleep(1000);
      return singletonList(new Picture("http://doge.com/picture.jpg"));
    });

    List<String> pictureUrls = rangeClosed(1, 10)
        .mapToObj(UserId::new)
        .parallel()
        .flatMap(userId -> pictureRepository.findByUserId(userId).stream())
        .sequential()
        .map(picture -> picture.url)
        .collect(Collectors.toList());
  }

  private <T> T runWithChrono(String name, Runnable<T> runnable) {
    long start = System.currentTimeMillis();
    T result = runnable.run();
    long time = System.currentTimeMillis() - start;

    System.out.println("Runned (" + name + ") with: " + time);
    return result;
  }

  @FunctionalInterface
  private interface Runnable<T> {
    T run();
  }

  private static class UserId {
    final int value;

    UserId(int value) {
      this.value = value;
    }
  }

  private static class Picture {
    final String url;

    Picture(String url) {
      this.url = url;
    }

  }

  private static class Something {
    final String value;

    Something(String value) {
      this.value = value;
    }
  }

  private interface PictureRepository {
    List<Picture> findByUserId(UserId userId);
  }

  private interface SomethingRepository {
    Something persist(Something somethingToPersist);
  }
}
