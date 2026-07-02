package org.viaborders.domain.route;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

/**
 * Route from origin to destination country was not found.
 *
 * <p>Exception use cases:
 *
 * <ul>
 *   <li>Route not found.</li>
 *   <li>Route not exist.</li>
 * </ul>
 */
@Builder
public class RouteNotFoundException extends RuntimeException {

  /**
   * Auxiliary variable for exception cause in instance builder.
   *
   * @see RuntimeException#getCause()
   */
  private final Throwable exceptionCause;

  /**
   * Auxiliary variable for exception message in instance builder.
   *
   * @see RuntimeException#getMessage()
   */
  private final String exceptionMessage;

  @Getter
  @NonNull
  private final String destination;

  /**
   * Prefix exception message with {@link #origin} and {@link #destination} if set with
   * {@link #exceptionMessage} in builder.
   */
  private final boolean includeCountryInMessage;

  @Getter
  @NonNull
  private final String origin;

  /**
   * Constructor.
   */
  private RouteNotFoundException(@NonNull String origin,
                                 @NonNull String destination,
                                 String exceptionMessage,
                                 Throwable exceptionCause,
                                 boolean includeCountryInMessage) {
    super(exceptionMessage, exceptionCause);
    this.origin = origin;
    this.destination = destination;

    // Not used anymore, but required to be initialized because of final and preserve consistent
    // state after object creation.
    this.exceptionMessage = exceptionMessage;
    this.exceptionCause = exceptionCause;
    this.includeCountryInMessage = includeCountryInMessage;
  }

  /**
   * Builder.
   *
   * <p>According to {@link #exceptionMessage} and {@link #exceptionCause} values, if
   * they are set, create a new {@link RouteNotFoundException} based on this four combination of
   * constructors based on extended {@link RuntimeException}:
   *
   * <ul>
   *   <li>{@link RuntimeException#RuntimeException()}</li>
   *   <li>{@link RuntimeException#RuntimeException(String)}</li>
   *   <li>{@link RuntimeException#RuntimeException(Throwable)}</li>
   *   <li>{@link RuntimeException#RuntimeException(String, Throwable)}</li>
   * </ul>
   */
  @SuppressWarnings("unused")
  public static class RouteNotFoundExceptionBuilder {

    /**
     * Create instance.
     */
    public RouteNotFoundException build() {
      if (isBlank(origin)) {
        throw new IllegalArgumentException("origin is required and not blank");
      }
      if (isBlank(destination)) {
        throw new IllegalArgumentException("destination is required and not blank");
      }

      String formattedMessage = null;
      if (isNotBlank(exceptionMessage)) {
        formattedMessage =
            constructMessage(origin, destination, exceptionMessage, includeCountryInMessage);
      }

      if (exceptionCause != null) {
        if (formattedMessage != null) {
          // new with: message, cause
          return new RouteNotFoundException(origin, destination, formattedMessage, exceptionCause,
              includeCountryInMessage);
        }

        // Exception message when creating: new RuntimeException(Throwable cause).
        //
        // Creating this message explicitly to preserve standard Java behavior.
        //
        // If you'll call constructor with just `null` value for message,
        // the message in super(message=null, cause) will be explicitly overwritten with `null`,
        // instead to preserve its default value as it had not been specified,
        // like a calling super without message: super(cause).
        var defaultJavaMessage = String.format("%s: %s",
            exceptionCause.getClass().getName(), exceptionCause.getMessage());

        // new with: cause
        return new RouteNotFoundException(origin, destination, defaultJavaMessage, exceptionCause,
            includeCountryInMessage);
      }

      if (formattedMessage != null) {
        // new with: message
        return new RouteNotFoundException(origin, destination, formattedMessage, null,
            includeCountryInMessage);
      }

      // new: empty, without message, without cause
      return new RouteNotFoundException(origin, destination, null, null, includeCountryInMessage);
    }

    private String constructMessage(String origin,
                                    String destination,
                                    String message,
                                    boolean includeCountryInMessage) {
      if (!includeCountryInMessage) {
        return message;
      }

      return String.format("""
                           origin: %s, destination: %s
                           %s""", origin, destination, message);

    }
  }
}
