package org.vaadin.jeanchristophe;

import com.vaadin.flow.data.validator.RegexpValidator;

/**
 * Validate the color
 */
public class ColorValidator extends RegexpValidator {

    private static final String colorRegExp = "^#?[a-fA-F0-9]{6}";

    public ColorValidator(String errorMessage) {
        super(errorMessage, colorRegExp);
    }

}
