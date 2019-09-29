package org.vaadin.jeanchristophe;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("")
public class DemoView extends VerticalLayout {

    public DemoView() {
        ColorPicker colorPicker = new ColorPicker();
        colorPicker.setLabel("3 presets");
        colorPicker.setPresetColors("#00ff00", "#ff0000", "#ff00ff");
        add(colorPicker);

        ColorPicker colorPicker2 = new ColorPicker();
        colorPicker2.setLabel("9 presets");
        colorPicker2.setPresetColors("#00ff00", "#ff0000", "#ff00ff", "#336654", "#3300f5", "#f45977", "#f3d55d", "#333333", "#119966");
        add(colorPicker2);

        // You can also use getTranslation
        ColorPicker colorPickerCustomLabel = new ColorPicker("Change label");
        colorPickerCustomLabel.setColorPickerI18N(new CustomColorPickerI18N());
        add(colorPickerCustomLabel);

        ColorPicker colorPickerNoCustomValue = new ColorPicker("No Custom Value");
        colorPickerNoCustomValue.setAllowCustomValue(false);
        add(colorPickerNoCustomValue);
    }

    public static class CustomColorPickerI18N extends ColorPickerI18n {

        @Override
        public String getBlueLabel() {
            return "Blue";
        }

        @Override
        public String getGreenLabel() {
            return  "Green";
        }

        @Override
        public String getRedLabel() {
            return "Red";
        }
    }
}
