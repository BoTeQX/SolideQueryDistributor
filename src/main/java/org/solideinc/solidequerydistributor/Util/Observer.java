package org.solideinc.solidequerydistributor.Util;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;

public class Observer implements ChangeListener<String> {
    private final TextField textField;
    private static final String TEXT_FIELD_ERROR_CSS_CLASS = "text-field-error";

    public Observer(TextField textField) {
        this.textField = textField;
        this.textField.textProperty().addListener(this);
    }

    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        updateStyleClass(newValue);
    }

    public void checkAndApplyStyle() {
        updateStyleClass(textField.getText());
    }

    private void updateStyleClass(String text) {
        if (text.isEmpty()) {
            if (!textField.getStyleClass().contains(TEXT_FIELD_ERROR_CSS_CLASS)) {
                textField.getStyleClass().add(TEXT_FIELD_ERROR_CSS_CLASS);
                Tooltip tooltip = new Tooltip("Dit veld kan niet leeg zijn");
                Tooltip.install(textField, tooltip);
            }
        } else {
            textField.getStyleClass().remove(TEXT_FIELD_ERROR_CSS_CLASS);
            Tooltip.uninstall(textField, textField.getTooltip());
        }
    }
}