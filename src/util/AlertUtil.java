package util;

import javafx.scene.control.Alert;

public  class AlertUtil {
    public static void showAddAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText("Ajout:");
        alert.setContentText("avec succes");
        alert.showAndWait();
    }

    public static void showAddError(String error) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText("Erreur d'ajout:");
        alert.setContentText(error);
        alert.showAndWait();
    }

    public static void showUpdateAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText("modification:");
        alert.setContentText("avec succes");
        alert.showAndWait();
    }

    public static void showUpdateError(String error) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText("Erreur de modification:");
        alert.setContentText(error);
        alert.showAndWait();
    }
    public static void showDelete() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText("Suppression:");
        alert.setContentText("avec succes");
        alert.showAndWait();
    }
    public static void showDeleteError(String error) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText("Suppression:");
        alert.setContentText(error);
        alert.showAndWait();
    }
}


