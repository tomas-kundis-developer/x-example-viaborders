package org.viaborders.domain.route;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;

import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;

class RouteNotFoundExceptionTest {

  @Test
  void testRouteNotFound_origin_destination() {
    RouteNotFoundException exception = RouteNotFoundException.builder()
        .origin("test-origin")
        .destination("test-destination")
        .build();

    assertThat(exception.getOrigin()).isEqualTo("test-origin");
    assertThat(exception.getDestination()).isEqualTo("test-destination");

    assertThat(exception.getMessage()).isNull();
    assertThat(exception).hasNoCause();
  }

  @Test
  void testRouteNotFound_origin_destination_message() {
    // implicit: includeCountryInMessage = false

    RouteNotFoundException exception1 = RouteNotFoundException.builder()
        .origin("test-origin")
        .destination("test-destination")
        .exceptionMessage("test-message")
        .build();


    assertThat(exception1.getOrigin()).isEqualTo("test-origin");
    assertThat(exception1.getDestination()).isEqualTo("test-destination");

    assertThat(exception1)
        .hasMessage("test-message")
        .hasNoCause();

    // explicit: includeCountryInMessage = false

    RouteNotFoundException exception2 = RouteNotFoundException.builder()
        .origin("test-origin")
        .destination("test-destination")
        .exceptionMessage("test-message")
        .includeCountryInMessage(false)
        .build();


    assertThat(exception2.getOrigin()).isEqualTo("test-origin");
    assertThat(exception2.getDestination()).isEqualTo("test-destination");

    assertThat(exception2)
        .hasMessage("test-message")
        .hasNoCause();
  }

  @Test
  void testRouteNotFound_origin_destination_message_include_origin_destination() {
    RouteNotFoundException exception = RouteNotFoundException.builder()
        .origin("test-origin")
        .destination("test-destination")
        .exceptionMessage("test-message")
        .includeCountryInMessage(true)
        .build();


    assertThat(exception.getOrigin()).isEqualTo("test-origin");
    assertThat(exception.getDestination()).isEqualTo("test-destination");

    assertThat(exception)
        .hasMessage("origin: test-origin, destination: test-destination\ntest-message")
        .hasNoCause();
  }

  @Test
  void testRouteNotFound_origin_destination_cause() {
    var cause = new NoSuchElementException("test-cause");

    RouteNotFoundException exception = RouteNotFoundException.builder()
        .origin("test-origin")
        .destination("test-destination")
        .exceptionCause(cause)
        .build();

    assertThat(exception.getOrigin()).isEqualTo("test-origin");
    assertThat(exception.getDestination()).isEqualTo("test-destination");

    // Default message when Throwable cause is specified
    assertThat(exception)
        .hasMessage("java.util.NoSuchElementException: test-cause")
        .hasCause(cause);
  }

  @Test
  void testRouteNotFound_origin_destination_cause_message() {
    var cause = new NoSuchElementException("test-cause");

    // implicit: includeCountryInMessage = false

    RouteNotFoundException exception1 = RouteNotFoundException.builder()
        .origin("test-origin")
        .destination("test-destination")
        .exceptionCause(cause)
        .exceptionMessage("test-message")
        .build();

    assertThat(exception1.getOrigin()).isEqualTo("test-origin");
    assertThat(exception1.getDestination()).isEqualTo("test-destination");

    assertThat(exception1).hasMessage("test-message")
        .hasCause(cause);

    // explicit: includeCountryInMessage = false

    RouteNotFoundException exception2 = RouteNotFoundException.builder()
        .origin("test-origin")
        .destination("test-destination")
        .exceptionCause(cause)
        .exceptionMessage("test-message")
        .includeCountryInMessage(false)
        .build();


    assertThat(exception2.getOrigin()).isEqualTo("test-origin");
    assertThat(exception2.getDestination()).isEqualTo("test-destination");

    assertThat(exception2).hasMessage("test-message")
        .hasCause(cause);
  }

  @Test
  void testRouteNotFound_origin_destination_cause_message_include_origin_destination() {
    var cause = new NoSuchElementException("test-cause");

    // implicit: includeCountryInMessage = false

    RouteNotFoundException exception = RouteNotFoundException.builder()
        .origin("test-origin")
        .destination("test-destination")
        .exceptionCause(cause)
        .exceptionMessage("test-message")
        .includeCountryInMessage(true)
        .build();

    assertThat(exception.getOrigin()).isEqualTo("test-origin");
    assertThat(exception.getDestination()).isEqualTo("test-destination");

    assertThat(exception).hasMessage(
            "origin: test-origin, destination: test-destination\ntest-message")
        .hasCause(cause);
  }

  @Test
  void testRouteNotFound_no_origin() {
    Throwable thrown = catchThrowable(() -> RouteNotFoundException.builder()
        // missed required origin
        .destination("test-destination")
        .build());

    then(thrown)
        .isInstanceOf(IllegalArgumentException.class)
        .hasNoCause()
        .hasMessage("origin is required and not blank");
  }

  @Test
  void testRouteNotFound_no_destination() {
    Throwable thrown = catchThrowable(() -> RouteNotFoundException.builder()
        .origin("test-origin")
        // missed required destination
        .build());

    then(thrown)
        .isInstanceOf(IllegalArgumentException.class)
        .hasNoCause()
        .hasMessage("destination is required and not blank");
  }

}