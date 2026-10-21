/**
 * File: null.java
 * Created by: Nguyen Thi Huynh Nhu
 * Date: 10/21/2026
 */

package iuh.fit.demospringdatajpa.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    /**
     * Mật khẩu đã mã hóa BCrypt, không bao giờ lưu dạng thô.
     */
    @Column(nullable = false, length = 100)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;
}