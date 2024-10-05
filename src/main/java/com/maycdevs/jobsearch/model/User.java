package com.maycdevs.jobsearch.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Calendar;

@Data
@Entity
@Table(name = "user_account")
public class User implements Serializable, UserDetails {

    /*
    N.B: Un user peut avoir le role ADMIN, ACTUATOR, CANDIDAT, RECRUTEUR etc...
    */

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(columnDefinition = "varchar(255) default ''")
    private String username;

    @Column(nullable = false, columnDefinition = "varchar(255) default ''")
    private String firstname;

    @Column(nullable = false, columnDefinition = "varchar(255) default ''")
    private String lastname;

    @Column(columnDefinition = "varchar(255) default 'Monsieur'")
    private String civility;

    @Column(nullable = false, columnDefinition = "varchar(255) default ''")
    private String email;

    @Column(columnDefinition = "varchar(255) default ''")
    private String phone;

    @Column(nullable = false, columnDefinition = "varchar(255) default ''")
    private String password;

    @Column(name = "first_connection")
    private Boolean firstConnection = false;

    private Date birthdate = null;

    @Column(name = "date_creation")
    private Date dateDeCreation = null;

    // represente la photo de profil de l'utilisateur
    @OneToOne
    private Attachement photo = null;
    private String photoLink = null;

    // propriete de liaison avec la table "ActivitySector",
    // represente le secteur d'activite de l'utilisateur
    // Cette propriete est obligatoire si l'utilisateur a pour role : candidat, recruteur.
    @ManyToOne
    @JoinColumn(name = "activity_sector_id")
    private ActivitySector activitySector;

    // propriete de liaison avec la table "Company",
    // represente l'entreprise dans laquelle exerce l'utilisateur
    // Cette propriete est obligatoire si l'utilisateur a pour role : recruteur.
    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    // propriete de liaison avec la table "Role",
    // represente le role l'utilisateur, un utilisateur ne peut avoir qu'un seul role
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    // s'execute avant de sauvegarder les donnees dans la bdd a cause de @PrePersist
    @PrePersist
    void beforeInsertion() {
        if (this.dateDeCreation == null) {
            this.dateDeCreation = Calendar.getInstance().getTime();
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
