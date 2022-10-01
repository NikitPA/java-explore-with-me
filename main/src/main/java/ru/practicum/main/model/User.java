package ru.practicum.main.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    @Column(nullable = false)
    @Size(min = 1, max = 55)
    private String name;

    @Column(nullable = false)
    private String email;

    private boolean isSubscribe;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_friends",
            joinColumns = {@JoinColumn(name = "users_id")},
            inverseJoinColumns = {@JoinColumn(name = "subscription_id")}
    )
    private Set<User> subscriptions = new HashSet<>();

    public void addFriend(User subscription) {
        subscriptions.add(subscription);
    }

}
