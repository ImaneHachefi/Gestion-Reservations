package main.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Salle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nom;

    @Column(nullable = false)
    private int capacite;

    private String batiment;
    private int etage;

    @OneToMany(mappedBy = "salle", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Reservation> reservations = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "salle_equipement",
            joinColumns = @JoinColumn(name = "salle_id"),
            inverseJoinColumns = @JoinColumn(name = "equipement_id")
    )
    private Set<Equipement> equipements = new HashSet<>();

    public Salle() {}

    public Salle(String nom, int capacite, String batiment, int etage) {
        this.nom = nom;
        this.capacite = capacite;
        this.batiment = batiment;
        this.etage = etage;
    }

    public void addEquipement(Equipement e) {
        equipements.add(e);
        e.getSalles().add(this);
    }

    public void removeEquipement(Equipement e) {
        equipements.remove(e);
        e.getSalles().remove(this);
    }

    public Long getId() { return id; }

    public String getNom() { return nom; }

    public void setNom(String nom) { this.nom = nom; }

    public int getCapacite() { return capacite; }

    public void setCapacite(int capacite) { this.capacite = capacite; }

    public String getBatiment() { return batiment; }

    public void setBatiment(String batiment) { this.batiment = batiment; }

    public int getEtage() { return etage; }

    public void setEtage(int etage) { this.etage = etage; }

    public Set<Reservation> getReservations() { return reservations; }

    public Set<Equipement> getEquipements() { return equipements; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Salle)) return false;
        return Objects.equals(nom, ((Salle) o).nom);
    }

    @Override
    public int hashCode() { return Objects.hash(nom); }

    @Override
    public String toString() {
        return "Salle [id=" + id + ", nom=" + nom + ", capacite=" + capacite
                + ", batiment=" + batiment + ", etage=" + etage + "]";
    }
}