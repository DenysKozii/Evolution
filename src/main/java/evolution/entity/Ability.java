package evolution.entity;

import evolution.enums.AbilityType;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "abilities")
public class Ability extends BaseEntity {
    @Id
    @Column(unique = true, nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private Integer coins;

    private Integer crystals;

    private AbilityType type;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "condition_ability_id", referencedColumnName = "id")
    private Ability conditionAbility;

    @ManyToMany(mappedBy = "availableAbilities")
    private List<User> availableUsers = new ArrayList<>();

    @ManyToMany(mappedBy = "gameAbilities")
    private List<User> gameUsers = new ArrayList<>();

    @ManyToMany(mappedBy = "mutatedAbilities")
    private List<User> mutatedUsers = new ArrayList<>();
}
