package io.mailtrap.model.response.suppressions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SuppressionType {
  HARD_BOUNCE("hard bounce"),
  SPAM_COMPLAINT("spam complaint"),
  UNSUBSCRIPTION("unsubscription"),
  MANUAL_IMPORT("manual import");

  private final String value;

  SuppressionType(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  @JsonCreator
  public static SuppressionType fromValue(String value) {
    for (SuppressionType type : SuppressionType.values()) {
      if (type.value.equalsIgnoreCase(value)) {
        return type;
      }
    }
    throw new IllegalArgumentException("Unknown value: " + value);
  }
}
