package controller.exceptions;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.extern.log4j.Log4j;
import model.bl.FinancialBl;
import model.entity.FType;
import model.entity.Financial;
import model.tools.Validator;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;



@Log4j
public class FinancialController implements Initializable {
    @FXML
    private TextField idTxt, amountTxt;
    @FXML
    private TextArea descriptionTxt;
    @FXML
    private DatePicker dTimeDate;
    @FXML
    private ComboBox<String> fTypeCmb,searchFTypeCmb;
    @FXML
    private Button saveBtn, editBtn, removeBtn, addBtn;
    @FXML
    private TableView<Financial> financialTbl;
    @FXML
    private TableColumn<Financial, String> idCol, amountCol, fTypeCol, datTimeCol, descriptionCol;

    public static String save(int amount, String fType, LocalDateTime dateTime, String description) {
        try {
            Financial financial = Financial
                    .builder()
                    .amount(amount)
                    .fType(FType.valueOf(fType))
                    .dateTime(LocalDateTime.now())
                    .description(Validator.validateDescription(description, "Invalid Description!!!"))
                    .build();
            FinancialBl.save(financial);
            log.info("Financial Saved\n" + financial.toString());
            return "Info : Financial Saved\n" + financial.toString();
        } catch (Exception e) {
            log.error("Error Save : " + e.getMessage());
            return "Error Save : " + e.getMessage();
        }
    }

    public static String edit(int id, int amount, String fType, LocalDateTime dateTime, String description) {
        try {
            Financial financial = Financial
                    .builder()
                    .id(id)
                    .amount(amount)
                    .fType(FType.valueOf(fType))
                    .dateTime(dateTime)
                    .description(Validator.validateDescription(description, "Invalid Description!!!"))
                    .build();
            FinancialBl.edit(financial);
            log.info("Financial Edited\n" + financial.toString());
            return "Info : Financial Edited\n" + financial.toString();
        } catch (Exception e) {
            log.error("Error Edit : " + e.getMessage());
            return "Error Edit : " + e.getMessage();
        }
    }

    public static String remove(int id) {
        try {
            Financial financial = FinancialBl.remove(id);
            log.info("Financial Removed\n" + financial.toString());
            return "Info : Financial Removed\n" + financial.toString();
        } catch (Exception e) {
            log.error("Error Remove : " + e.getMessage());
            return "Error Remove : " + e.getMessage();
        }
    }

    public static List<Financial> findAll() {
        try {
            log.info("FindAll");
            return FinancialBl.findAll();
        } catch (Exception e) {
            log.error("Error FindAll : " + e.getMessage());
            return null;
        }
    }

    public static Financial findById(int id) {
        try {
            log.info("FindById : " + id);
            return FinancialBl.findById(id);
        } catch (Exception e) {
            log.error("Error FindById :" + e.getMessage());
            return null;
        }
    }

    public static List<Financial> findByFType(String fType) {
        try {
            log.info("findByFType : " + fType);
            return FinancialBl.findByFType(fType);
        } catch (Exception e) {
            log.error("Error findByFType : " + e.getMessage());
            return null;
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        for (FType value : FType.values()) {
            fTypeCmb.getItems().add(value.name());
            searchFTypeCmb.getItems().add(value.name());
        }
        resetForm();

        addBtn.setOnAction((event) -> {
            resetForm();
        });

        saveBtn.setOnAction((event) ->{

            String message = save(
                    Integer.parseInt(amountTxt.getText()),
                    fTypeCmb.getSelectionModel().getSelectedItem(),
                    dTimeDate.getValue().atStartOfDay(),
                    descriptionTxt.getText()
            );

            if (message.startsWith("Info")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
                alert.show();
                resetForm();
            } else if (message.startsWith("Error")) {
                Alert alert = new Alert(Alert.AlertType.ERROR, message);
                alert.show();
            }
        });

        editBtn.setOnAction((event) ->{
            String message = edit(
                    Integer.parseInt(idTxt.getText()),
                    Integer.parseInt(amountTxt.getText()),
                    fTypeCmb.getSelectionModel().getSelectedItem(),
                    dTimeDate.getValue().atStartOfDay(),
                    descriptionTxt.getText()
            );


            if (message.startsWith("Info")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
                alert.show();
                resetForm();
            } else if (message.startsWith("Error")) {
                Alert alert = new Alert(Alert.AlertType.ERROR, message);
                alert.show();
            }
        });

        removeBtn.setOnAction((event) -> {
            String message = remove(Integer.parseInt(idTxt.getText()));

            if (message.startsWith("Info")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
                alert.show();
                resetForm();
            } else if (message.startsWith("Error")) {
                Alert alert = new Alert(Alert.AlertType.ERROR, message);
                alert.show();
            }
        });

        searchFTypeCmb.setOnAction((event) -> {
            refreshTable(findByFType(searchFTypeCmb.getValue()));
        });

        financialTbl.setOnMouseReleased((event) -> {
            selectFinancial();
        });

        financialTbl.setOnKeyReleased((event) -> {
            selectFinancial();
        });

    }

    public void resetForm() {
        idTxt.clear();
        amountTxt.clear();
        fTypeCmb.getSelectionModel().select(0);
        searchFTypeCmb.getSelectionModel().select(0);
        dTimeDate.setValue(LocalDate.now());
        descriptionTxt.clear();
        refreshTable(findAll());
    }
    public void refreshTable(List<Financial> financialList) {
        try {
            ObservableList<Financial> observableList = FXCollections.observableList(financialList);

            idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
            fTypeCol.setCellValueFactory(new PropertyValueFactory<>("fType"));
            datTimeCol.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
            descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));


            financialTbl.setItems(observableList);
        }
        catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.show();
        }
    }

    public void selectFinancial() {
        Financial financial = financialTbl.getSelectionModel().getSelectedItem();
        idTxt.setText(String.valueOf(financial.getId()));
        amountTxt.setText(String.valueOf(financial.getAmount()));
        fTypeCmb.getSelectionModel().select(financial.getFType().name());
        dTimeDate.setValue(LocalDate.from(financial.getDateTime()));
        descriptionTxt.setText(financial.getDescription());

    }
}