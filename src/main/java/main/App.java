package main;

import main.model.*;
import org.h2.tools.Server;

import javax.persistence.*;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        Server console = null;
        EntityManagerFactory emf = null;
        EntityManager em = null;

        try {
            // Console web H2
            console = Server.createWebServer("-webPort", "8082").start();
            System.out.println("[OK] Console H2 démarrée : " + console.getURL());
            System.out.println("     JDBC URL : jdbc:h2:mem:testdb | User : sa | Mot de passe : (vide)");

            emf = Persistence.createEntityManagerFactory("gestion-reservations-pu");
            em = emf.createEntityManager();
            System.out.println("[OK] Connexion établie et tables créées");

            em.getTransaction().begin();

            Salle salle = new Salle("Salle A", 30, "Bâtiment 1", 2);
            salle.addEquipement(new Equipement("Projecteur"));
            salle.addEquipement(new Equipement("Tableau blanc"));


            User imane = new User("Imane", "H.", "imane@mail.com");
            em.persist(imane);
            em.persist(salle);

            Reservation r = new Reservation(
                    LocalDateTime.of(2026, 10, 20, 9, 0),
                    LocalDateTime.of(2026, 10, 20, 11, 0),
                    "Réunion de projet");
            r.setSalle(salle);
            salle.getReservations().add(r);
            imane.addReservation(r);

            em.getTransaction().commit();
            System.out.println("[OK] Données créées avec succès");

            List<Salle> salles = em.createQuery(
                            "SELECT DISTINCT s FROM Salle s JOIN s.equipements e WHERE e.libelle = :libelle", Salle.class)
                    .setParameter("libelle", "Projecteur")
                    .getResultList();
            System.out.println("\n=== Salles avec projecteur ===");
            salles.forEach(System.out::println);

            System.out.println("\n[OK] Toutes les opérations ont réussi.");
            System.out.println(">>> Ouvre " + console.getURL() + " pour vérifier la base.");
            System.out.println(">>> Appuie sur Entrée pour arrêter (la base mémoire sera supprimée).");
            new Scanner(System.in).nextLine();

        } catch (SQLException e) {
            System.err.println("Erreur au démarrage de la console H2 : " + e.getMessage());
        } catch (RuntimeException e) {
            if (em != null && em.getTransaction().isActive()) em.getTransaction().rollback();
            System.err.println("Erreur : " + e.getMessage());

        } finally {
            if (em != null && em.isOpen()) em.close();
            if (emf != null && emf.isOpen()) emf.close();
            if (console != null) console.stop();
        }
    }
}