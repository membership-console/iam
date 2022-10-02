package cc.rits.membership.console.iam.config.auth;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cc.rits.membership.console.iam.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;

/**
 * IAM User Details Service
 */
@Service
@RequiredArgsConstructor
public class IamUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
        final var authorities = AuthorityUtils.createAuthorityList("ROLE_USER");
        return this.userRepository.selectByEmail(email) //
            .map(userModel -> new LoginUserDetails(userModel, authorities)) //
            .orElseThrow(() -> new UsernameNotFoundException(null));
    }

}
