package main.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Equipement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String libelle;

    @ManyToMany(mappedBy = "equipements")
    private Set<Salle> salles = new HashSet<>();

    public Equipement() {}

    public Equipement(String libelle) { this.libelle = libelle; }

    public Long getId() { return id; }

    public String getLibelle() { return libelle; }

    public void setLibelle(String libelle) { this.libelle = libelle; }

    public Set<Salle> getSalles() { return salles; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Equipement)) return false;
        return Objects.equals(libelle, ((Equipement) o).libelle);
    }

    @Override
    public int hashCode() { return Objects.hash(libelle); }

    @Override
    public String toString() { return "Equipement [id=" + id + ", libelle=" + libelle + "]"; }
}