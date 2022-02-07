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

    private Integer plasma;

    private Integer dna;

    private AbilityType type;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany
    @JoinTable(name = "condition_abilities",
            joinColumns = @JoinColumn(name = "ability_id"),
            inverseJoinColumns = @JoinColumn(name = "condition_id"))
    private List<Ability> conditionAbilities = new ArrayList<>();

    @ManyToMany(mappedBy = "availableAbilities")
    private List<User> availableUsers = new ArrayList<>();

    @ManyToMany(mappedBy = "boughtAbilities")
    private List<User> boughtUsers = new ArrayList<>();

    @ManyToMany(mappedBy = "mutatedAbilities")
    private List<User> mutatedUsers = new ArrayList<>();

    @ManyToMany(mappedBy = "gameAbilities")
    private List<User> gameUsers = new ArrayList<>();
}
