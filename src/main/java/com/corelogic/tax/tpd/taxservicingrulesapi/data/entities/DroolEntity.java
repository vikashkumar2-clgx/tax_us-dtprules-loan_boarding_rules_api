package com.corelogic.tax.tpd.taxservicingrulesapi.data.entities;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@Table(name = "drools")
public class DroolEntity {
    @Id
    @Column(
            name = "id",
            columnDefinition = "uuid"
    )
    private UUID id;

    @Column(name="client_id")
    private String clientId;

    @Column(name="rule_id")
    private UUID ruleId;

    @Column(name="file_name")
    private String fileName;

    @Lob
    @Type(type="org.hibernate.type.BinaryType")
    @Column(name="file_content")
    private byte[] fileContent;

    @Column(name="last_updated_ts")
    private ZonedDateTime lastUpdatedTs;

    @Column(name="is_enabled")
    private boolean isEnabled;
}
