package lk.ijse.chatapplication;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.ResourceBundle;

public class RegisterFormController implements Initializable {
    private final static String URL = "jdbc:mysql://localhost:3306/chatapplication";
    private final static Properties props = new Properties();

    static{
        props.setProperty("user", "root");
        props.setProperty("password", "1234");
    }

    public JFXTextField txtUsername;
    public JFXPasswordField txtPassword;
    public JFXTextField txtName;
    public AnchorPane root;

    @Override
    public void initialize(java.net.URL location, ResourceBundle resources) {
        generateNextUserId();
    }

    public void btnRegisterOnAction(ActionEvent actionEvent) throws SQLException {
        try (Connection con = DriverManager.getConnection(URL, props)) {
            String sql = "INSERT INTO user(User_ID ,User_Name, User_Password) VALUES(?,?,?)";

            PreparedStatement pstm = con.prepareStatement(sql);
            pstm.setString(1, txtName.getText());
            pstm.setString(2, txtUsername.getText());
            pstm.setString(3, txtPassword.getText());


            try {
                int affectedRows = pstm.executeUpdate();
                if (affectedRows > 0) {
                    new Alert(Alert.AlertType.CONFIRMATION, "User Added !!").show();
                    generateNextUserId();
                }
            } catch (Exception ex) {
                new Alert(Alert.AlertType.ERROR, "This ID has been previously used!!").show();
            }
        }
        txtPassword.clear();
        txtUsername.clear();
        generateNextUserId();
    }
    private void generateNextUserId() {
        try {
            String nextId = ClientModel.generateNextOrderId();
            txtName.setText(nextId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void btnLoginOnMouseClicked(MouseEvent mouseEvent) throws IOException {
        Stage stage = new Stage();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/View/AddNewUserForm.fxml"))));
        stage.setTitle("Register Form");
        stage.show();
        root.getScene().getWindow().hide();
    }
}
