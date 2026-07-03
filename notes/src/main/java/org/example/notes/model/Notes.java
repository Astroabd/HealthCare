package org.example.notes.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "notes")
public class Notes {

    @Id
    private String id;
    private Long patientId;
    private String Content;
    private LocalDateTime CreatedAt;

}
