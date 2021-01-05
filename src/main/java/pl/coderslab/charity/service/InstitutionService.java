package pl.coderslab.charity.service;

import org.springframework.stereotype.Service;
import pl.coderslab.charity.entity.Institution;
import pl.coderslab.charity.repository.InstitutionRepository;

import java.util.List;
import java.util.Optional;

@Service
public class InstitutionService {
    private final InstitutionRepository institutionRepository;

    public InstitutionService(InstitutionRepository institutionRepository) {
        this.institutionRepository = institutionRepository;
    }

    public List<Institution> getInstitutions(){
        return institutionRepository.findAll();
    }

    public Optional<Institution> get(Long id){
        Optional<Institution> optionalInstitution = institutionRepository.findById(id);
        return optionalInstitution;
    }

    public void save(Institution institution){
        institutionRepository.save(institution);
    }

    public void update(Institution institution){
        institutionRepository.save(institution);
    }

    public void delete(Long id){
        institutionRepository.deleteById(id);
    }

    public List<Institution> find4Institutions(){
        return institutionRepository.findFirst4ByOrderByIdAsc();
    }
}
