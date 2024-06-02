package com.summitsync.api.calendar;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Objects;

@Data
@Builder
public class CalendarEventDateSet {
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null) {
            return false;
        }

        if (getClass() != o.getClass()) {
            return false;
        }

        var other = (CalendarEventDateSet) o;

        return Objects.equals(this.startTime, other.startTime) && Objects.equals(this.endTime, other.endTime);
    }

    @Override
    public int hashCode() {
        return startTime.hashCode() | endTime.hashCode();
    }
}
