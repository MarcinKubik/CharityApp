package pl.coderslab.charity.service;

import org.springframework.stereotype.Service;
import pl.coderslab.charity.entity.Donation;
import pl.coderslab.charity.entity.User;
import pl.coderslab.charity.repository.DonationRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class DonationService {
    private final DonationRepository donationRepository;

    public DonationService(DonationRepository donationRepository) {
        this.donationRepository = donationRepository;
    }


    public List<Donation> getDonations() {
        return donationRepository.findAll();
    }

    public Optional<Donation> get(Long id) {
        Optional<Donation> optionalDonation = donationRepository.findById(id);
        return optionalDonation;
    }

    public List<Donation> findDonationsOfUser(User user) {
        return donationRepository.findAllByUser(user);
    }

    public List<Donation> findTakenAndNotTaken(User user, boolean taken){
        return donationRepository.findAllByUserAndTakenFromMe(user, taken);
    }

    public List<Donation> findAllSortedByTakenFromMeDateDesc(User user){
        return donationRepository.findAllByUserOrderByTakenFromMeDateDesc(user);
    }

    public List<Donation> findAllSortedByCreationDateDesc(User user){
        return donationRepository.findAllByUserOrderByCreationDateDesc(user);
    }

    public void save(Donation donation) {
        donation.setCreationDate(LocalDate.now());
        donation.setTakenFromMe(false);
        donationRepository.save(donation);
    }

    public void update(Donation donation) {
        donationRepository.save(donation);
    }

    public void delete(Long id) {
        donationRepository.deleteById(id);
    }

    public Integer countBags() {
        return donationRepository.countBags();
    }

    public long AllDonations() {
        return donationRepository.count();
    }

}
