package com.lemon.project.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A District.
 */
@Entity
@Table(name = "district")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class District implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active;

    @Column(name = "latitude")
    private String latitude;

    @Column(name = "longitude")
    private String longitude;

    @Column(name = "last_modified_by")
    private Long lastModifiedBy;

    @Column(name = "last_modify_date")
    private LocalDate lastModifyDate;

    @ManyToOne
    private Division division;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public Long getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(Long lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public LocalDate getLastModifyDate() {
        return lastModifyDate;
    }

    public void setLastModifyDate(LocalDate lastModifyDate) {
        this.lastModifyDate = lastModifyDate;
    }

    public Division getDivision() {
        return division;
    }

    public void setDivision(Division division) {
        this.division = division;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        District district = (District) o;
        if (district.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), district.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "District{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", code='" + getCode() + "'" +
            ", active='" + isActive() + "'" +
            ", latitude='" + getLatitude() + "'" +
            ", longitude='" + getLongitude() + "'" +
            ", lastModifiedBy=" + getLastModifiedBy() +
            ", lastModifyDate='" + getLastModifyDate() + "'" +
            "}";
    }
}
