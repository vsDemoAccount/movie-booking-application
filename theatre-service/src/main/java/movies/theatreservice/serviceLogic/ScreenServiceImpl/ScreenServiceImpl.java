package movies.theatreservice.serviceLogic.ScreenServiceImpl;

import lombok.RequiredArgsConstructor;

import movies.theatreservice.dtos.ScreenDTO.ScreenDTO;
import movies.theatreservice.entity.Screen.Screen;
import movies.theatreservice.entity.Theatre.Theatre;
import movies.theatreservice.exceptions.ResourceNotFoundException;
import movies.theatreservice.mappers.ScreenMapper.ScreenMapper;
import movies.theatreservice.repository.ScreenRepository.ScreenRepository;
import movies.theatreservice.repository.TheatreRepository.TheatreRepository;
import movies.theatreservice.serviceImpl.ScreenService.ScreenService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScreenServiceImpl implements ScreenService {

    private final ScreenRepository screenRepository;
    private final TheatreRepository theatreRepository;
    private final ScreenMapper screenMapper;

    @Override
    public ScreenDTO createScreen(ScreenDTO screenDTO) {
        // 1. Verify Theatre Exists
        Theatre theatre = theatreRepository.findByCode(screenDTO.getTheatreCode())
                .orElseThrow(() -> new ResourceNotFoundException("Theatre", "code", screenDTO.getTheatreCode()));

        // 2. Map and Link
        Screen screen = screenMapper.toEntity(screenDTO);
        screen.setTheatre(theatre);

        // 3. Save
        Screen savedScreen = screenRepository.save(screen);
        return screenMapper.toDTO(savedScreen);
    }

    @Override
    public ScreenDTO getScreenByCode(String code) {
        Screen screen = screenRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Screen", "code", code));
        return screenMapper.toDTO(screen);
    }

    @Override
    public List<ScreenDTO> getAllScreens(String theatreCode) {
        List<Screen> screens;
        if (theatreCode != null && !theatreCode.isEmpty()) {
            screens = screenRepository.findByTheatre_Code(theatreCode);
        } else {
            screens = screenRepository.findAll();
        }

        return screens.stream()
                .map(screenMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ScreenDTO updateScreen(String code, ScreenDTO screenDTO) {
        Screen screen = screenRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Screen", "code", code));

        // Update fields
        screen.setName(screenDTO.getName());
        screen.setFeatures(screenDTO.getFeatures());

        // Handle moving screen to another theatre (rare, but possible)
        if (!screen.getTheatre().getCode().equals(screenDTO.getTheatreCode())) {
            Theatre newTheatre = theatreRepository.findByCode(screenDTO.getTheatreCode())
                    .orElseThrow(() -> new ResourceNotFoundException("Theatre", "code", screenDTO.getTheatreCode()));
            screen.setTheatre(newTheatre);
        }

        Screen updatedScreen = screenRepository.save(screen);
        return screenMapper.toDTO(updatedScreen);
    }

    @Override
    public void deleteScreen(String code) {
        Screen screen = screenRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Screen", "code", code));
        screenRepository.delete(screen);
    }
}
