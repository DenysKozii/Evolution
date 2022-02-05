package evolution.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Table(name = "skins")
public class Skin extends BaseEntity {
    @Id
    @Column(unique = true, nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer coins;

    private Integer crystals;

    private Integer rating;

//    @Lob
//    @Column(columnDefinition = "MEDIUMBLOB")
//    private String image;

}
