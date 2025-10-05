package movies.moviesservice.entity.RegionRights;


import jakarta.persistence.*;
import lombok.*;
import movies.moviesservice.entity.Movie;

import java.time.LocalDate;

@Entity
@Table(name = "region_rights")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegionRights {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @Column(length = 2, nullable = false)
    private String countryCode;

    private Boolean available;
    private LocalDate fromDate;
    private LocalDate toDate;
}
