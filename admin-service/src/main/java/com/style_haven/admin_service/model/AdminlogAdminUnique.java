package com.style_haven.admin_service.model;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

import com.style_haven.admin_service.service.AdminlogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;
import org.springframework.web.servlet.HandlerMapping;


/**
 * Validate that the adminid value isn't taken yet.
 */
@Target({ FIELD, METHOD, ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = AdminlogAdminUnique.AdminlogAdminUniqueValidator.class
)
public @interface AdminlogAdminUnique {

    String message() default "{Exists.adminlog.admin}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class AdminlogAdminUniqueValidator implements ConstraintValidator<AdminlogAdminUnique, Long> {

        private final AdminlogService adminlogService;
        private final HttpServletRequest request;

        public AdminlogAdminUniqueValidator(final AdminlogService adminlogService,
                final HttpServletRequest request) {
            this.adminlogService = adminlogService;
            this.request = request;
        }

        @Override
        public boolean isValid(final Long value, final ConstraintValidatorContext cvContext) {
            if (value == null) {
                // no value present
                return true;
            }
            @SuppressWarnings("unchecked") final Map<String, String> pathVariables =
                    ((Map<String, String>)request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE));
            final String currentId = pathVariables.get("adminlogId");
            if (currentId != null && value.equals(adminlogService.get(Integer.parseInt(currentId)).getAdmin())) {
                // value hasn't changed
                return true;
            }
            return !adminlogService.adminExists(value);
        }

    }

}
