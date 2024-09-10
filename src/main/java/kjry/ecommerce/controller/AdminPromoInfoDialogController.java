package kjry.ecommerce.controller;

import java.text.SimpleDateFormat;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import kjry.ecommerce.dtos.PromoDTO;
import kjry.ecommerce.services.PromoService;
import kjry.ecommerce.services.ValidationUtils;

public class AdminPromoInfoDialogController {

    @FXML
    private VBox alertParentVBox;

    @FXML
    private TextField codeNameTextField;

    @FXML
    private TextField descriptionTextField;

    @FXML
    private TextField durationTextField;

    @FXML
    private TextField idTextField;

    @FXML
    private TextField percentageTextField;

    @FXML
    private Button saveButton;

    @FXML
    private TextField startingDateTextField;

    private PromoDTO promo;
    private Stage parent;
    private boolean viewOnly;
    private boolean isCreated = false;

    @FXML
    void handleSaveButtonAction(ActionEvent event) {
        validateFields();
    }

    private void validateFields() {
        boolean valid = true;

        String[] ids = new String[PromoService.getAllPromo(true).length];
        int i = 0;
        for (PromoDTO x : PromoService.getAllPromo(true)) {
            ids[i] = x.getId();
            i++;
        }
        if (promo.getId() != null) {
            for (int j = 0; j < ids.length; j++) {
                if (ids[j].equals(idTextField.getText())) {
                    ids[j] = "-1";
                    break;
                }
            }
        }

        boolean idValid = ValidationUtils.isUnqiue(idTextField.getText(), ids);
        ValidationUtils.setFieldValidity(idTextField, idValid);
        valid &= idValid;

        boolean nameValid = ValidationUtils.isNotEmpty(codeNameTextField.getText());
        ValidationUtils.setFieldValidity(codeNameTextField, nameValid);
        valid &= nameValid;

        String[] codes = new String[PromoService.getAllPromo(true).length];
        int k = 0;
        for (PromoDTO x : PromoService.getAllPromo(true)) {
            codes[k] = x.getCodeName();
            k++;
        }

        if (promo.getCodeName() != null) {
            for (int j = 0; j < codes.length; j++) {
                if (codes[j].equals(codeNameTextField.getText())) {
                    codes[j] = "-1";
                    break;
                }
            }
        }

        boolean nameValid2 = ValidationUtils.isUnqiue(codeNameTextField.getText(), codes);
        ValidationUtils.setFieldValidity(codeNameTextField, nameValid2);
        valid &= nameValid2;

        boolean dateValid = ValidationUtils.isValidDate(startingDateTextField.getText());
        ValidationUtils.setFieldValidity(startingDateTextField, dateValid);
        valid &= dateValid;

        boolean percentageValid = ValidationUtils.isValidDouble(percentageTextField.getText());
        ValidationUtils.setFieldValidity(percentageTextField, percentageValid);
        valid &= percentageValid;

        boolean descriptionValid = ValidationUtils.isNotEmpty(descriptionTextField.getText());
        ValidationUtils.setFieldValidity(descriptionTextField, descriptionValid);
        valid &= descriptionValid;

        boolean durationValid = ValidationUtils.isValidPositiveInteger(durationTextField.getText());
        ValidationUtils.setFieldValidity(durationTextField, durationValid);
        valid &= durationValid;

        if (valid) {
            savePromo();
        }

    }

    private void savePromo() {
        try {
            promo.setStartingDate(new SimpleDateFormat("dd-MM-yyyy").parse(startingDateTextField.getText()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        promo.setId(idTextField.getText());
        promo.setCodeName(codeNameTextField.getText());
        promo.setDescription(descriptionTextField.getText());
        promo.setPercentage(Double.parseDouble(percentageTextField.getText()));
        promo.setAvailableDay(Integer.parseInt(durationTextField.getText()));
        parent.close();
        isCreated = true;
    }

    public void setPromo(PromoDTO promo, boolean editable) {
        this.promo = promo;
        this.viewOnly = !editable;
        setEditable(editable);
        if (viewOnly) {
            saveButton.setVisible(false);
            saveButton.setManaged(false);
        }
        if (promo.getId() != null) {
            populateFields();
        }
    }

    private void populateFields() {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        startingDateTextField.setText(format.format(promo.getStartingDate()));
        percentageTextField.setText(String.format("%.2f", promo.getPercentage()));
        idTextField.setText(promo.getId());
        durationTextField.setText(String.format("%d", promo.getAvailableDay()));
        descriptionTextField.setText(promo.getDescription());
        codeNameTextField.setText(promo.getCodeName());
    }

    private void setEditable(boolean editable) {
        if (promo.getId() != null) {
            idTextField.setEditable(false);
            idTextField.setStyle("-fx-background-color:#c3c3c3;");
        }else{
            idTextField.setEditable(editable);
        }
        startingDateTextField.setEditable(editable);
        percentageTextField.setEditable(editable);
        durationTextField.setEditable(editable);
        descriptionTextField.setEditable(editable);
        codeNameTextField.setEditable(editable);
    }

    public void setParentStage(Stage parent) {
        this.parent = parent;
    }

    public boolean getIsCreated() {
        return this.isCreated;
    }
}
