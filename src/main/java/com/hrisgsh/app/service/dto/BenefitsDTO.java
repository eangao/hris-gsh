package com.hrisgsh.app.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.hrisgsh.app.domain.Benefits} entity.
 */
public class BenefitsDTO implements Serializable {

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
        if (!(o instanceof BenefitsDTO)) {
            return false;
        }

        BenefitsDTO benefitsDTO = (BenefitsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, benefitsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BenefitsDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
