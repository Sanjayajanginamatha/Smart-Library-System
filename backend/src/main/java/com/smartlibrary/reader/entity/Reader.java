package com.smartlibrary.reader.entity;

import com.smartlibrary.department.entity.Department;
import com.smartlibrary.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "readers",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_reader_usn", columnNames = "usn")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reader {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            unique = true
    )
    private User user;

    @Column(nullable = false, unique = true)
    private String usn;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "department_id",
            nullable = false
    )
    private Department department;

    @Column(nullable = false)
    private String phone;
}