package kz.nik.project01userservice.repository;

import jakarta.transaction.Transactional;
import kz.nik.project01userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User,Long> {
    User findByEmail(String email);
    User findByUsername(String username);
    void deleteByUsername(String username);

}
