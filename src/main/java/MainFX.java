import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Classe Main da aplicação em javaFX.
 *
 * @author Filipe Miguel Teixeira Freitas Guimarães - A865308
 */
public class MainFX extends Application {
    /**
     * Lança a aplicação.
     *
     * @param args Argumentos.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Responsável por abrir a primeira view e definir as opções de janela.
     *
     * @param stage Stage a aparecer.
     * @throws Exception Caso não encontre a view pretendida.
     */
    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("TP3");
        Scene start = new Scene(FXMLLoader.load(getClass().getResource("/View/main.fxml")));
        start.getStylesheets().add("/stylesheet/material.css");
        stage.setScene(start);
        stage.centerOnScreen();
        stage.show();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });
    }

}
