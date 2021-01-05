package pl.coderslab.charity.service;

import org.springframework.stereotype.Service;
import pl.coderslab.charity.entity.Donation;
import pl.coderslab.charity.repository.DonationRepository;

import java.util.List;
import java.util.Optional;

@Service
public class DonationService {
    private final DonationRepository donationRepository;

    public DonationService(DonationRepository donationRepository) {
        this.donationRepository = donationRepository;
    }


    public List<Donation> getDonations(){
        return donationRepository.findAll();
    }

    public Optional<Donation> get(Long id){
        Optional<Donation> optionalDonation = donationRepository.findById(id);
        return optionalDonation;
    }

    public void save(Donation donation){
        donationRepository.save(donation);
    }

    public void update(Donation donation){
        donationRepository.save(donation);
    }

    public void delete(Long id){
        donationRepository.deleteById(id);
    }

    public Integer countAllDonations(){
        return donationRepository.countByQuantity();
    }
}
