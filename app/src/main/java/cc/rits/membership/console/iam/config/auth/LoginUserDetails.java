package cc.rits.membership.console.iam.config.auth;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import cc.rits.membership.console.iam.domain.model.UserModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Login User Details
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LoginUserDetails extends UserModel implements UserDetails {

    /**
     * authorities
     */
    Collection<? extends GrantedAuthority> authorities;

    public LoginUserDetails(final UserModel userModel, final Collection<? extends GrantedAuthority> authorities) {
        this.setId(userModel.getId());
        this.setFirstName(userModel.getFirstName());
        this.setLastName(userModel.getLastName());
        this.setEmail(userModel.getEmail());
        this.setPassword(userModel.getPassword());
        this.setEntranceYear(userModel.getEntranceYear());
        this.setUserGroups(userModel.getUserGroups());
        this.authorities = authorities;
    }

    @Override
    public String getUsername() {
        return this.getEmail();
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
