package movies.theatreservice.serviceLogic.SeatTypeServiceImpl;

import lombok.RequiredArgsConstructor;
import movies.theatreservice.dtos.SeatTypeDTO.SeatTypeDTO;
import movies.theatreservice.entity.SeatType.SeatType;
import movies.theatreservice.exceptions.ResourceNotFoundException;
import movies.theatreservice.mappers.SeatTypeMapper.SeatTypeMapper;
import movies.theatreservice.repository.SeatTypeRepository.SeatTypeRepository;
import movies.theatreservice.serviceImpl.SeatTypeService.SeatTypeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SeatTypeServiceImpl implements SeatTypeService {

    private final SeatTypeRepository seatTypeRepository;
    private final SeatTypeMapper seatTypeMapper;

    @Override
    public SeatTypeDTO createSeatType(SeatTypeDTO seatTypeDTO) {
        // 1. Check if name exists
        if (seatTypeRepository.existsByName(seatTypeDTO.getName())) {
            throw new RuntimeException("Seat Type with name '" + seatTypeDTO.getName() + "' already exists");
        }

        SeatType seatType = seatTypeMapper.toEntity(seatTypeDTO);
        SeatType savedSeatType = seatTypeRepository.save(seatType);

        return seatTypeMapper.toDTO(savedSeatType);
    }

    @Override
    public SeatTypeDTO getSeatTypeByCode(String code) {
        SeatType seatType = seatTypeRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("SeatType", "code", code));
        return seatTypeMapper.toDTO(seatType);
    }

    @Override
    public List<SeatTypeDTO> getAllSeatTypes() {
        return seatTypeRepository.findAll().stream()
                .map(seatTypeMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public SeatTypeDTO updateSeatType(String code, SeatTypeDTO seatTypeDTO) {
        SeatType seatType = seatTypeRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("SeatType", "code", code));

        seatType.setName(seatTypeDTO.getName());

        SeatType updatedSeatType = seatTypeRepository.save(seatType);
        return seatTypeMapper.toDTO(updatedSeatType);
    }

    @Override
    public void deleteSeatType(String code) {
        SeatType seatType = seatTypeRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("SeatType", "code", code));
        seatTypeRepository.delete(seatType);
    }
}