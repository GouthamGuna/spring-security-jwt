package in.cerpsoft.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * @NotNull: This constraint is applied to a CharSequence, Collection, Map, or Array, and it is valid as long as it is not null, but it can be empty.
 * @NotEmpty: This constraint is also applied to a CharSequence, Collection, Map, or Array, and it is valid as long as it is not null and its size/length is greater than zero.
 * @NotBlank: This constraint is applied to a String, and it is valid as long as it is not null, and the trimmed length is greater than zero.
 */

@Entity
@Table(name = "_user")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    //@NotEmpty(message = "user name shouldn't be empty.")
    //@NotBlank(message = "user name shouldn't be empty.")
    // @Size(min = 8, message = "user name must be at least 8 characters.")
    private String userName;

    //@NotEmpty(message = "password shouldn't be empty.")
    //@NotBlank(message = "password shouldn't be blank.")
    //@Size(min = 8, message = "password must be at least 8 characters.")
    private String userSecret;

    //@NotBlank(message = "mobile no shouldn't be empty")
    //@Pattern(regexp = "^\\d{10}$", message = "Invalid Mobile no")
    //@Size(min = 10, message = "mobile no must be at least 10 digits.")
    private String mobileNo;

    //@NotBlank(message = "emailId shouldn't be empty")
    //@Size(min = 8, message = "emailId no must be at least 8 characters.")
    //@Email(message = "Email is not valid", regexp = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return userSecret;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
