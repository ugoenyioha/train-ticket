package com.trainticket.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.util.UUID;

/**
 * @author fdse
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Money {
    @Id
    private UUID id;

    @Column(length = 36)
    private String userId;
    private String money; //NOSONAR

}
