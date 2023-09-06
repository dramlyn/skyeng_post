package mail_app.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(uniqueConstraints = { @UniqueConstraint(name = "UniqueIndexAndAddress",columnNames = {"index", "address"})})
public class PostOffice {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "seq_office"
    )
    private long id;
    @Column(nullable = false, name = "index")
    private int index;
    @Column(nullable = false, name = "address")
    private String address;
    @Column(nullable = false)
    private String name;
}
