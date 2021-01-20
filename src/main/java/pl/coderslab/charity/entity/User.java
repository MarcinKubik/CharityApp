package pl.coderslab.charity.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Pattern(regexp = "[A-ZÓŹŻĆŁŚĆ]{1}[a-zóżźćąęłśń]{2,}", message = "Podaj poprawne imię")
    private String name;
    @NotBlank
    @Pattern(regexp = "[A-ZÓŹŻĆŁŚĆ]{1}[a-zóżźćąęłśń]{2,}", message = "Podaj poprawne nazwisko")
    private String surname;
    @NotBlank
    @Pattern(regexp = "[_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.([a-zA-Z]{2,}){1}",
            message = "Podaj poprawny adres email")
    @Column(unique = true)
    private String email;
    @NotBlank
    @Pattern(regexp = "\\S{8,}", message = "Hasło musi zawierać co najmniej 8 znaków")
    private String password;
    @NotBlank
    @Transient
    @Pattern(regexp = "\\S{8,}", message = "Hasło musi zawierać co najmniej 8 znaków")
    private String password2;
    private int enabled;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    public String getFullName(){
        return name + " " + surname;
    }
}