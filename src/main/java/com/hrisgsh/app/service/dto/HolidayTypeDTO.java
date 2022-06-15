package com.hrisgsh.app.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.hrisgsh.app.domain.HolidayType} entity.
 */
public class HolidayTypeDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HolidayTypeDTO)) {
            return false;
        }

        HolidayTypeDTO holidayTypeDTO = (HolidayTypeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, holidayTypeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HolidayTypeDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
