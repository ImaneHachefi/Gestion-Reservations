package main.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime dateDebut;

    @Column(nullable = false)
    private LocalDateTime dateFin;

    private String motif;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutReservation statut = StatutReservation.EN_ATTENTE;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "utilisateur_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "salle_id")
    private Salle salle;


    public Reservation() {}

    public Reservation(LocalDateTime dateDebut, LocalDateTime dateFin, String motif) {
        if (dateDebut == null || dateFin == null || !dateFin.isAfter(dateDebut)) {
            throw new IllegalArgumentException("La date de fin doit être postérieure à la date de début");
        }
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.motif = motif;
    }

    public Long getId() { return id; }

    public LocalDateTime getDateDebut() { return dateDebut; }

    public LocalDateTime getDateFin() { return dateFin; }

    public String getMotif() { return motif; }

    public void setMotif(String motif) { this.motif = motif; }

    public StatutReservation getStatut() { return statut; }

    public void setStatut(StatutReservation statut) { this.statut = statut; }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

    public Salle getSalle() { return salle; }

    public void setSalle(Salle salle) { this.salle = salle; }

    @Override
    public String toString() {
        return "Reservation [id=" + id + ", " + dateDebut + " -> " + dateFin
                + ", statut=" + statut + ", motif=" + motif + "]";
    }
}