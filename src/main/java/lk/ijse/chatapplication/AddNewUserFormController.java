package lk.ijse.chatapplication;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.chatapplication.Model.ClientModel;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AddNewUserFormController extends Application implements Initializable {
    public JFXTextField txtUsername;
    static String username;
    public AnchorPane root;
    public JFXPasswordField txtPassword;
    public JFXComboBox comUsername;
    public ArrayList<String> arrayList = new ArrayList<>();

    public void btnAddOnAction(ActionEvent actionEvent) throws SQLException, IOException {
        String user_name = (String) comUsername.getSelectionModel().getSelectedItem();
        String password = ClientModel.searchByUser_Name(user_name);
        username = user_name;

        String id = ClientModel.searchByUser_ID(user_name);
        if (searchArray(id)) {
            if (password.equalsIgnoreCase(txtPassword.getText())) {
                Stage stage = new Stage();
                stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/View/ClientForm.fxml"))));
                stage.setTitle(user_name + "'s Chat");
                stage.show();
                arrayList.add(id);
            } else {
                new Alert(Alert.AlertType.ERROR, "Incorrect Password !").show();
            }
        }else {
            new Alert(Alert.AlertType.ERROR, "Already used !").show();
        }
        txtPassword.clear();
    }

    public void btnRegisterOnMouseCliced(MouseEvent mouseEvent) throws IOException {
        Stage stage = new Stage();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/View/RegisterForm.fxml"))));
        stage.setTitle("Register Form");
        stage.show();
        root.getScene().getWindow().hide();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadUserNames();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/View/AddNewUserForm.fxml"))));
        stage.show();

        new Thread(()->{
            Server server = new Server();
            try {
                server.Server();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    public boolean searchArray(String username){
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).equalsIgnoreCase(username)){
                return false;
            }
        }
        return true;
    }

    public void loadUserNames() throws SQLException {
        try{
            List<String> username = ClientModel.getUserName();
            ObservableList<String> obList = FXCollections.observableArrayList();

            for (String un : username){
                obList.add(un);
            }
            comUsername.setItems(obList);
        }catch (SQLException e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "SQL Error !!").show();
        }
    }
}
