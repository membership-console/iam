package cc.rits.membership.console.iam.infrastructure.api;

import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import cc.rits.membership.console.iam.domain.model.ClientModel;
import cc.rits.membership.console.iam.domain.model.UserModel;
import cc.rits.membership.console.iam.domain.repository.IClientRepository;
import cc.rits.membership.console.iam.exception.ErrorCode;
import cc.rits.membership.console.iam.exception.UnauthorizedException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Argument Resolver
 */
@Component
@RequiredArgsConstructor
public class RestControllerArgumentResolver implements HandlerMethodArgumentResolver {

    private final UserDetailsService userDetailsService;

    private final IClientRepository clientRepository;

    @Override
    public boolean supportsParameter(@NonNull final MethodParameter parameter) {
        return (parameter.getParameterType().equals(UserModel.class) || parameter.getParameterType().equals(ClientModel.class));
    }

    @Override
    public Object resolveArgument(@NonNull final MethodParameter parameter, @Nullable final ModelAndViewContainer mavContainer,
        @Nullable final NativeWebRequest webRequest, @Nullable final WebDataBinderFactory binderFactory) {
        if (this.supportsParameter(parameter)) {
            final var authentication = SecurityContextHolder.getContext().getAuthentication();
            if (parameter.getParameterType().equals(UserModel.class)) {
                return this.userDetailsService.loadUserByUsername(authentication.getName());
            } else if (parameter.getParameterType().equals(ClientModel.class)) {
                return this.clientRepository.selectByClientId(authentication.getName()) //
                    .orElseThrow(() -> new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN));
            } else {
                return WebArgumentResolver.UNRESOLVED;
            }
        } else {
            return WebArgumentResolver.UNRESOLVED;
        }
    }

}
