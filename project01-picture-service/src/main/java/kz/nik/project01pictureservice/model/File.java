package kz.nik.project01pictureservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "files")
@Getter
@Setter
public class File extends BaseEntity {
    private String path;

    @ManyToOne(fetch = FetchType.LAZY)
    private Picture picture;
}
