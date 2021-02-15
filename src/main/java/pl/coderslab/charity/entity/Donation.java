package pl.coderslab.charity.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "donations")
public class Donation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private Integer quantity;
    @NotNull
    @ManyToMany
    private List<Category> categories = new ArrayList<>();
    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_institution")
    private Institution institution;
    @NotNull
    private String street;
    @NotNull
    private String city;
    @NotNull
    private String zipCode;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate pickUpDate;
    @NotNull
    private LocalTime pickUpTime;
    @NotEmpty
    private String pickUpComment;
    @NotNull
    private String phoneNumber;
    private boolean takenFromMe;
    private LocalDate takenFromMeDate;
    private LocalDate creationDate;
    @ManyToOne
    private User user;

}
