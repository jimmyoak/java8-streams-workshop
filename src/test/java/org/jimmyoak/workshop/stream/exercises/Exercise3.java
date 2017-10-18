package org.jimmyoak.workshop.stream.exercises;

import org.jimmyoak.workshop.stream.explain.utils.Info;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.Collections.unmodifiableList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Info("NOW! We can start making something interesting")
public class Exercise3 {
  @Test
  @Info("You receive some pictures to your endpoint and you have to upload them")
  public void uploading_picture_objects() throws Exception {
    String[] inputPictures = {"RAW_BINARY_INFO1", "RAW_BINARY_INFO2", "RAW_BINARY_INFO3"};
    PictureRepository pictureRepository = pictureRepositoryWillPersistReturningAnUploadedPicture();

    List<UploadedPicture> uploadedPictures = Arrays.stream(inputPictures)
        .map(PictureRaw::new)
        .map(UnuploadedPicture::new)
        .map(pictureRepository::uploadAndPersist)
        .collect(Collectors.toList());

    assertThat(uploadedPictures).hasSize(3);
    verify(pictureRepository, times(3)).uploadAndPersist(any());
  }

  @Test
  @Info("Get user name of users which has no pictures")
  public void get_user_name_of_users_which_has_no_pictures() throws Exception {
    List<User> users = asList(
        new User("Jimmy", emptyList()),
        new User("Jose", singletonList(anUploadedPicture())),
        new User("Numa", asList(anUploadedPicture(), anUploadedPicture()))
    );

    List<User> usersWithoutPictures = users.stream()
        .filter(user -> user.pictures.isEmpty())
        .collect(Collectors.toList());

    assertThat(usersWithoutPictures)
        .hasSize(1)
        .allSatisfy(user -> assertThat(user.pictures.isEmpty()).isTrue());
  }

  private UploadedPicture anUploadedPicture() {
    return Mockito.mock(UploadedPicture.class);
  }

  private PictureRepository pictureRepositoryWillPersistReturningAnUploadedPicture() {
    PictureRepository pictureRepository = Mockito.mock(PictureRepository.class);
    when(pictureRepository.uploadAndPersist(any())).then(invocation -> {
      UnuploadedPicture unuploadedPicture = (UnuploadedPicture) invocation.getArguments()[0];
      return unuploadedPicture.uploadedIn(new PictureUrl("http://jimmykoak.com/picture/" + unuploadedPicture.id));
    });
    return pictureRepository;
  }

  private static class User {
    final String name;
    final List<UploadedPicture> pictures;

    public User(String name, List<UploadedPicture> pictures) {
      this.name = name;
      this.pictures = unmodifiableList(pictures);
    }
  }

  private static class UploadedPicture {
    final PictureId id;
    final PictureUrl url;

    UploadedPicture(PictureId id, PictureUrl url) {
      this.id = new PictureId();
      this.url = url;
    }
  }

  private static class UnuploadedPicture {
    final PictureId id;
    final PictureRaw raw;

    UnuploadedPicture(PictureRaw raw) {
      this.id = new PictureId();
      this.raw = raw;
    }

    UploadedPicture uploadedIn(PictureUrl url) {
      return new UploadedPicture(this.id, url);
    }
  }

  private static class PictureId {
    final String value;

    PictureId() {
      this.value = UUID.randomUUID().toString();
    }
  }

  private static class PictureUrl {
    final String value;

    PictureUrl(String value) {
      this.value = value;
    }
  }

  private static class PictureRaw {
    final String value;

    PictureRaw(String value) {
      this.value = value;
    }
  }

  private interface PictureRepository {
    UploadedPicture uploadAndPersist(UnuploadedPicture unuploadedPicture);
  }
}
