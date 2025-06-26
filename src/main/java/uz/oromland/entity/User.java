package uz.oromland.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import uz.oromland.enums.Gender;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldNameConstants
@Entity
@ToString
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 64)
    @Column(unique = true, nullable = false)
    private String username;

    @NotBlank
    @Size(max = 64)
    private String firstName;

    @NotBlank
    @Size(max = 64)
    private String lastName;

    @Email
    @NotBlank
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank
    @Size(min = 8, max = 128)
    private String password;

    @NotBlank
    @Size(max = 16)
    private String phoneNumber;

    @NotNull
    private Boolean isActive = true;

    @NotNull
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Gender gender;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    @NotNull
    private Role role;

    @ManyToOne
    private Attachment attachment;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return role.getPermission().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.name()))
                .collect(Collectors.toList());


    }
    
    @Override
    public boolean isAccountNonExpired() {
        return isActive;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isActive;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isActive;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }
}

