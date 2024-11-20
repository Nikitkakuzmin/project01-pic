package kz.nik.project01pictureservice.repository;


import kz.nik.project01pictureservice.model.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface FileRepository extends JpaRepository<File, Long> {
}
