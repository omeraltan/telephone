package com.telephone.app.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Rehber.
 */
@Entity
@Table(name = "rehber")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Rehber implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "adi", nullable = false)
    private String adi;

    @NotNull
    @Column(name = "soyadi", nullable = false)
    private String soyadi;

    @NotNull
    @Column(name = "dahili", nullable = false)
    private String dahili;

    @Column(name = "cep")
    private String cep;

    @Column(name = "aciklama")
    private String aciklama;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Rehber id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAdi() {
        return this.adi;
    }

    public Rehber adi(String adi) {
        this.setAdi(adi);
        return this;
    }

    public void setAdi(String adi) {
        this.adi = adi;
    }

    public String getSoyadi() {
        return this.soyadi;
    }

    public Rehber soyadi(String soyadi) {
        this.setSoyadi(soyadi);
        return this;
    }

    public void setSoyadi(String soyadi) {
        this.soyadi = soyadi;
    }

    public String getDahili() {
        return this.dahili;
    }

    public Rehber dahili(String dahili) {
        this.setDahili(dahili);
        return this;
    }

    public void setDahili(String dahili) {
        this.dahili = dahili;
    }

    public String getCep() {
        return this.cep;
    }

    public Rehber cep(String cep) {
        this.setCep(cep);
        return this;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getAciklama() {
        return this.aciklama;
    }

    public Rehber aciklama(String aciklama) {
        this.setAciklama(aciklama);
        return this;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Rehber)) {
            return false;
        }
        return id != null && id.equals(((Rehber) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Rehber{" +
            "id=" + getId() +
            ", adi='" + getAdi() + "'" +
            ", soyadi='" + getSoyadi() + "'" +
            ", dahili='" + getDahili() + "'" +
            ", cep='" + getCep() + "'" +
            ", aciklama='" + getAciklama() + "'" +
            "}";
    }
}
