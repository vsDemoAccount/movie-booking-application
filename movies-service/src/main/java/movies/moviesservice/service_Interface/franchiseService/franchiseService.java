package movies.moviesservice.service_Interface.franchiseService;

import movies.moviesservice.dtos.franchiseDTO.franchiseDTO;

import java.util.List;

public interface franchiseService {
    franchiseDTO create(franchiseDTO dto);
    List<franchiseDTO> getAll();
    franchiseDTO getByCode(String code);
    franchiseDTO updateByCode(String code, franchiseDTO dto);
    void deleteByCode(String code);
}