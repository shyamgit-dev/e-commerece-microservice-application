package com.user.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.annotations.SoftDeleteType;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@SoftDelete(
        columnName = "is_active",
        strategy = SoftDeleteType.ACTIVE
)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String name;
    private String email;
    private String username;
    private String password;

    @ColumnDefault("true")
    @Column(
            name = "is_active",
            insertable = false,
            updatable = false
    )
    private boolean active;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
