package com.maycdevs.jobsearch.service;

import com.maycdevs.jobsearch.model.ActivitySector;
import com.maycdevs.jobsearch.model.User;
import com.maycdevs.jobsearch.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class UserService implements EntityCRUDService<User>, UserDetailsService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User getOneById(String idInString) throws Exception {
        try {
            Long id = Long.parseLong(idInString);
            return userRepository.findById(id).orElse(null);
        } catch (Exception e) {
            log.debug(e.toString());
            e.printStackTrace();
            throw new Exception("Could not find User with id: "+idInString+" in database because: "+e.toString());
        }
    }

    public User getOneByUsername(String username) throws Exception {
        try {
            return userRepository.findByUsername(username).orElse(null);
        } catch (Exception e) {
            log.debug(e.toString());
            e.printStackTrace();
            throw new Exception("Could not find User with username: "+username+" in database because: "+e.toString());
        }
    }

    @Override
    public User create(User data) throws Exception {
        if(
                data.getActivitySector() != null &&
                data.getFirstname() != null &&
                data.getLastname() != null &&
                data.getEmail() != null &&
                data.getPassword() != null
        ) {
            // on continue l'enregistrement
            String username = data.getUsername();
            if(username == null || username.isBlank()) {
                data.setUsername(data.getEmail());
            }

            try {
                return userRepository.save(data);
            } catch (Exception e) {
                log.debug(e.toString());
                e.printStackTrace();
                throw new Exception("Could not create User with firstname: "+data.getFirstname()+" in database because: "+e.toString());
            }
        } else {
            // faire un switch sur les differents champs cles pour determiner celui ou ceux dont la valeur manque
            if (data.getActivitySector() == null) {
                // error : La propriete activity sector n'a pas de valeur, veuillez entrer une valeur pour ce champ dans le formulaire
            } else if (data.getFirstname() == null) {
                // error : La propriete firstname n'a pas de valeur, veuillez entrer une valeur pour ce champ dans le formulaire
            } else if (data.getLastname() == null) {
                // error : La propriete lastname n'a pas de valeur, veuillez entrer une valeur pour ce champ dans le formulaire
            } else if (data.getEmail() == null) {
                // error : La propriete email n'a pas de valeur, veuillez entrer une valeur pour ce champ dans le formulaire
            } else if (data.getPassword() == null) {
                // error : La propriete password n'a pas de valeur, veuillez entrer une valeur pour ce champ dans le formulaire
            }
        }
        return null;
    }

    @Override
    public User update(String id, User data) throws Exception {
       User current = getOneById(id);
       if (current != null) {
           ActivitySector activitySector = data.getActivitySector();
           if(activitySector != null) {
               current.setActivitySector(activitySector);
           }

           String firstname = data.getFirstname();
           if(firstname != null) {
               current.setFirstname(firstname);
           }

           String lastname = data.getLastname();
           if(lastname != null) {
               current.setLastname(lastname);
           }

           String email = data.getEmail();
           if(email != null) {
               current.setEmail(email);
           }

           String newPhotoLink = data.getPhotoLink();
           if(newPhotoLink != null) {
               current.setPhotoLink(newPhotoLink);
           }

           Date newBirthdate = data.getBirthdate();
           if(newBirthdate != null) {
               current.setBirthdate(newBirthdate);
           }

           try {
               return userRepository.save(current);
           } catch (Exception e) {
               log.debug(e.toString());
               e.printStackTrace();
               throw new Exception("Could not update User with id: "+data.getId()+" in database because: "+e.toString());
           }
       }

       return null;
    }

    @Override
    public Boolean delete(String id) throws Exception {
        User u = getOneById(id);
        if (u != null) {
            try {
                userRepository.delete(u);
                return true;
            } catch (Exception e) {
                log.debug(e.toString());
                e.printStackTrace();
                throw new Exception("Could not delete User with id: "+id+" in database because: "+e.toString());
            }
        }
        return false;
    }

    public User getOneByEmail(String email) throws Exception {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new Exception("Not found user with email: "+email)
        );
    }

    public Boolean changePassword(String id, String newpassword, String oldpassword, String confpassword) throws Exception {
        User u = getOneById(id);
        if (u != null) {
            if(Objects.equals(u.getPassword(), oldpassword)) {

                if(Objects.equals(newpassword, confpassword)) {
                    u.setPassword(newpassword);
                    try {
                        userRepository.save(u);
                        return true;
                    } catch (Exception e) {
                        log.debug(e.toString());
                        e.printStackTrace();
                        throw new Exception("Could not change password of User with id: "+id+" in database because: "+e.toString());
                    }
                } else { /* gestion erreur si newpassword different de confpassword */ }

            } else { /* gestion erreur si oldpassword different de password actuel de user */ }
        }

        return false;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            User u = userRepository.findByUsername(username).orElseThrow(
                    () -> new Exception("Not found user with username: "+username)
            );
            /*
            org.springframework.security.core.userdetails.User.builder()
                    .username()
                    .password()
                    .roles()
                    .build();

            */
            return new org.springframework.security.core.userdetails.User(
                    u.getUsername(),
                    u.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority(u.getRole().getLibelle()))
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

