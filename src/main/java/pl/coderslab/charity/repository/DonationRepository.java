package pl.coderslab.charity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.coderslab.charity.entity.Donation;

public interface DonationRepository extends JpaRepository<Donation, Long> {
    @Query(value = "SELECT sum(quantity) FROM donations", nativeQuery = true)
    Integer countBags();
    @Query(value = "SELECT count(*) FROM donations", nativeQuery = true)
    Integer countAllDonations();
}

/*public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT * FROM USERS WHERE LASTNAME = ?1",
            countQuery = "SELECT count(*) FROM USERS WHERE LASTNAME = ?1",
            nativeQuery = true)
    Page<User> findByLastname(String lastname, Pageable pageable);
}*/
