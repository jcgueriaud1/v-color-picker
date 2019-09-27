package org.vaadin.jeanchristophe;

import com.vaadin.flow.component.AbstractSinglePropertyField;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.PropertyDescriptor;
import com.vaadin.flow.component.PropertyDescriptors;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;

@Tag("paper-slider")
@JsModule("@polymer/paper-slider/paper-slider.js")
@NpmPackage(value = "@polymer/paper-slider", version = "^3.0.1")
class PaperSlider extends AbstractSinglePropertyField<PaperSlider, Integer> implements HasStyle {

    public PaperSlider() {
        super("value",0,false);
        setEditable(true);
        setMax(255);
    }

    public void setEditable(boolean editable) {
        if (editable) {
            getElement().setAttribute("editable", "");
        } else  {
            getElement().removeAttribute("editable");
        }
    }

    public void setMax(int max) {
        getElement().setProperty("max", max);
    }

}
