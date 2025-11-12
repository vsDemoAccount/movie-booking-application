package movies.moviesservice.Mappers.PersonMapper;

import org.springframework.stereotype.Component;
import movies.moviesservice.entity.person.Person;
import movies.moviesservice.dtos.PersonDTO.PersonDto;

@Component
public class PersonMapper {
    public PersonDto toDto(Person p) {
        if (p == null) return null;
        PersonDto d = new PersonDto();
        d.code = p.getCode();
        d.name = p.getName();
        d.birthDate = p.getBirthDate();
        d.bio = p.getBio();
        d.photoUrl = p.getPhotoUrl();
        return d;
    }

    public Person toEntity(PersonDto d) {
        if (d == null) return null;
        Person p = new Person();
        p.setName(d.name);
        p.setBirthDate(d.birthDate);
        p.setBio(d.bio);
        p.setPhotoUrl(d.photoUrl);
        // code and timestamps are handled by entity lifecycle if null
        p.setCode(d.code);
        return p;
    }
}
