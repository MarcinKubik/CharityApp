package pl.coderslab.charity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.coderslab.charity.entity.Donation;

public interface DonationRepository extends JpaRepository<Donation, Long> {
    @Query("SELECT sum(d.quantity) FROM Donation d")
    Integer countBags();
    /*@Query(value = "SELECT count(*) FROM donations", nativeQuery = true)
    Integer countAllDonations();*/
    long count();

}
