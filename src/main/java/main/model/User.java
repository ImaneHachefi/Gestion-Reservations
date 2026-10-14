package main.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "utilisateur")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String prenom;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false, unique = true)
    private String email;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Reservation> reservations = new HashSet<>();

    public User() {}

    public User(String prenom, String nom, String email) {
        this.prenom = prenom;
        this.nom = nom;

        this.email = email;
    }

    public void addReservation(Reservation r) {
        reservations.add(r);
        r.setUser(this);
    }

    public void cancelReservation(Reservation r) {
        reservations.remove(r);
        r.setUser(null);
    }

    public Long getId() { return id; }

    public String getPrenom() { return prenom; }

    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getNom() { return nom; }

    public void setNom(String nom) { this.nom = nom; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public Set<Reservation> getReservations() { return reservations; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        return Objects.equals(email, ((User) o).email);
    }

    @Override
    public int hashCode() { return Objects.hash(email); }


    @Override
    public String toString() {
        return "User [id=" + id + ", " + prenom + " " + nom + ", email=" + email + "]";
    }
}