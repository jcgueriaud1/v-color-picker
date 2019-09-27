package org.vaadin.jeanchristophe;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.shared.Registration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CssImport("./custom-color-style.css")
public class ColorPicker extends CustomField<String> {

    private ColorI18n colorI18n = new ColorI18n();
    private HorizontalLayout layout = new HorizontalLayout();
    private ComboBox<String> colorField;
    private Button openAdvancedButton;

    private List<String> presetColors = new ArrayList<>();
    private String internalColor;
    private Button okButton;
    private Button cancelButton;
    private HorizontalLayout presetLayout;
    private Map<String, Div> presetColorsDiv;

    private HorizontalLayout advancedLayout;
    private Div preview = new Div();

    private PaperSlider greenPaperSlider = new PaperSlider();
    private PaperSlider redPaperSlider = new PaperSlider();
    private PaperSlider bluePaperSlider = new PaperSlider();

    private Registration customValueRegistration;

    private boolean allowCustomValue = true;

    public ColorPicker() {
        layout.setSpacing(false);
        layout.setPadding(false);
        this.colorField = new ComboBox<>();
        colorField.setPattern("^#?[a-fA-F0-9]{6}");
        colorField.setRenderer(this.getColorFieldRenderer());
        setAllowCustomValue(allowCustomValue);
        openAdvancedButton = new Button(VaadinIcon.PAINTBRUSH.create());
        openAdvancedButton.setThemeName(ButtonVariant.LUMO_ICON.getVariantName());
        openAdvancedButton.addClickListener(event -> openAdvancedDialog());
        layout.add(colorField, openAdvancedButton);
        layout.addClassName("color-picker");
        add(layout);
    }

    public ColorPicker(String label) {
        this();
        setLabel(label);
    }

    public Renderer<String> getColorFieldRenderer() {
        return new ComponentRenderer<>(color ->
        {
            HorizontalLayout layout = new HorizontalLayout();
            Icon icon = VaadinIcon.CIRCLE.create();
            icon.setColor(color);
            layout.addAndExpand(new Span(color));
            layout.add(icon);
            return layout;
        });
    }

    private void openAdvancedDialog() {
        final Dialog dialog = createDialog();
        okButton = new Button(colorI18n.getOkText());
        okButton.setThemeName(ButtonVariant.LUMO_PRIMARY.getVariantName());
        // save the dialog value
        okButton.addClickListener(e -> {
            colorField.setValue(internalColor);
            dialog.close();
        });
        cancelButton = new Button(colorI18n.getCancelText());
        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(false);
        layout.setPadding(false);
        layout.setAlignItems(FlexComponent.Alignment.STRETCH);
        layout.addClassName("color-picker");
        cancelButton.addClickListener(e -> dialog.close());


        if (getPresetColors() != null && !getPresetColors().isEmpty()) {
            Details detailsPreset = new Details(colorI18n.getPresetTitle(),
                    createPresetLayout());
            detailsPreset.setOpened(true);
            layout.add(detailsPreset);
        }

        if (isAllowCustomValue()) {
            Details detailsAdvanced = new Details(colorI18n.getAdvancedTitle(),
                    createAdvancedLayout());
            detailsAdvanced.setOpened(true);
            layout.add(detailsAdvanced);
        }
        layout.add(new HorizontalLayout(okButton, cancelButton));
        dialog.add(layout);
        // set the default value for the dialog
        setInternalColor(colorField.getValue(), false);
        dialog.open();
    }


    private HorizontalLayout createPresetLayout() {
        if (presetLayout == null) {
            presetColorsDiv = new HashMap<>();
            presetLayout = new HorizontalLayout();
            // create the list of preset colors
            presetColors.forEach(color -> {
                Div colorDiv = new Div();
                colorDiv.addClassName("preset-color");
                colorDiv.getStyle().set("background-color", color);
                colorDiv.addClickListener(event -> setInternalColor(color, false));
                presetLayout.add(colorDiv);
                presetColorsDiv.put(color, colorDiv);
            });
        }
        return presetLayout;
    }

    private HorizontalLayout createAdvancedLayout() {
        if (advancedLayout == null) {
            advancedLayout = new HorizontalLayout();
            preview.addClassName("preview");
            VerticalLayout colorsLayout = new VerticalLayout();
            colorsLayout.setPadding(false);
            colorsLayout.setSpacing(false);
            Label redPaperSliderLabel = new Label(colorI18n.getRedLabel());
            redPaperSlider.setId("red-color");
            redPaperSliderLabel.setFor(redPaperSlider);
            redPaperSlider.addValueChangeListener(this::paperSliderChanged);
            redPaperSlider.addClassName("red");
            HorizontalLayout redLayout = new HorizontalLayout(redPaperSliderLabel, redPaperSlider);
            redLayout.setSpacing(false);
            redLayout.setAlignItems(FlexComponent.Alignment.CENTER);
            colorsLayout.add(redLayout);

            Label greenPaperSliderLabel = new Label(colorI18n.getGreenLabel());
            greenPaperSlider.setId("green-color");
            greenPaperSliderLabel.setFor(greenPaperSlider);
            greenPaperSlider.addClassName("green");
            greenPaperSlider.addValueChangeListener(this::paperSliderChanged);

            HorizontalLayout greenLayout = new HorizontalLayout(greenPaperSliderLabel, greenPaperSlider);
            greenLayout.setSpacing(false);
            greenLayout.setAlignItems(FlexComponent.Alignment.CENTER);
            colorsLayout.add(greenLayout);

            Label bluePaperSliderLabel = new Label(colorI18n.getBlueLabel());
            greenPaperSlider.setId("blue-color");
            greenPaperSliderLabel.setFor(greenPaperSlider);
            bluePaperSlider.addClassName("blue");
            bluePaperSlider.addValueChangeListener(this::paperSliderChanged);


            HorizontalLayout blueLayout = new HorizontalLayout(bluePaperSliderLabel, bluePaperSlider);
            blueLayout.setSpacing(false);
            blueLayout.setAlignItems(FlexComponent.Alignment.CENTER);
            colorsLayout.add(blueLayout);
            advancedLayout.setAlignItems(FlexComponent.Alignment.CENTER);
            advancedLayout.add(colorsLayout, preview);
        }

        return advancedLayout;
    }

    private void setInternalColor(int r, int g, int b) {
        String hex = String.format("#%02x%02x%02x", r, g, b);
        setInternalColor(hex, false);
    }



    private void setInternalColor(String color, boolean isFromCustom) {
        internalColor = color;
        preview.getStyle().set("background-color", color);
        // select
        if (presetColorsDiv != null) {
            presetColorsDiv.forEach((s, div) -> div.removeClassName("selected"));
            if (presetColorsDiv.containsKey(color)) {
                presetColorsDiv.get(color).addClassName("selected");
            }
        }
        if (!isFromCustom) {
            // set custom
            if (internalColor != null && !internalColor.isEmpty()) {
                redPaperSlider.setValue(Integer.valueOf(color.substring(1, 3), 16));
                greenPaperSlider.setValue(Integer.valueOf(color.substring(3, 5), 16));
                bluePaperSlider.setValue(Integer.valueOf(color.substring(5, 7), 16));
            }
        }
    }

    @Override
    protected String generateModelValue() {
        return colorField.getValue();
    }

    @Override
    protected void setPresentationValue(String newPresentationValue) {
        colorField.setValue(newPresentationValue);
    }

    public Dialog createDialog() {
        return new Dialog();
    }

    public List<String> getPresetColors() {
        return presetColors;
    }

    public void setPresetColors(List<String> presetColors) {
        if (presetLayout != null) {
            presetLayout = null;
            presetColorsDiv = null;
        }
        this.colorField.setItems(presetColors);
        this.presetColors = presetColors;
    }

    public void setPresetColors(String... presetColors) {
        setPresetColors(Arrays.asList(presetColors));
    }

    private void paperSliderChanged(ComponentValueChangeEvent<PaperSlider, Integer> e) {
        if (e.isFromClient()) {
            setInternalColor(redPaperSlider.getValue(), greenPaperSlider.getValue(), bluePaperSlider.getValue());
        }
    }

    public boolean isAllowCustomValue() {
        return allowCustomValue;
    }

    /**
     *
     * @param allowCustomValue
     */
    public void setAllowCustomValue(boolean allowCustomValue) {
        colorField.setAllowCustomValue(allowCustomValue);
        if (customValueRegistration !=  null) {
            customValueRegistration.remove();
        }
        if (allowCustomValue) {
            customValueRegistration = colorField.addCustomValueSetListener(event ->
                    colorField.setValue(event.getDetail()));
        }
        this.allowCustomValue = allowCustomValue;
    }

    /**
     *
     * @param colorI18n translation for Color Picker
     */
    public void setColorI18n(ColorI18n colorI18n) {
        this.colorI18n = colorI18n;
    }
}

