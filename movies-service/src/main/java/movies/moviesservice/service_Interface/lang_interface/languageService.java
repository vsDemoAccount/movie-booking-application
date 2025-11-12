package movies.moviesservice.service_Interface.lang_interface;

import movies.moviesservice.dtos.languageDTO.languageDTO;

import java.util.List;

public interface languageService {
    languageDTO create(languageDTO dto);
    List<languageDTO> getAll();
    languageDTO getByCode(String code);
    languageDTO updateByCode(String code, languageDTO dto);
    void deleteByCode(String code);
}
