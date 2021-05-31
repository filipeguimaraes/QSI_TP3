package Controller;

import Model.Atraso;
import Model.Data;
import Model.Endereco;
import Model.Protocolos;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;


public class Controlador implements Initializable {

    @FXML
    private Text showpath;

    @FXML
    private Button ficheiro;

    @FXML
    private Button executar;

    @FXML
    private LineChart<String, Number> atraso;

    @FXML
    private CategoryAxis atrasoX;

    @FXML
    private NumberAxis atrasoY;

    @FXML
    private ScatterChart<String, Number> protocolos;

    @FXML
    private CategoryAxis protocolosX;

    @FXML
    private NumberAxis protocolosY;

    @FXML
    private BarChart<Number, String> enderecosOrigem;

    @FXML
    private CategoryAxis origemX;

    @FXML
    private NumberAxis origemY;


    @FXML
    void chooseFile(ActionEvent event) {
        Node node = (Node) event.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(thisStage);
        if (selectedFile != null &&
                selectedFile.getPath().contains(".csv")) {
            try {
                Data.getInstance(selectedFile.getAbsolutePath());
                showpath.setText(selectedFile.getName());
                executar.setDisable(false);
                ficheiro.setDisable(true);
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Erro: " + e.getMessage());
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Ficheiro inválido.");
            alert.showAndWait();
        }
    }

    @FXML
    void executa(MouseEvent event) {
        executar.setDisable(true);

        setAtraso(Data.getInstance().getAtraso());
        setProtocolos(Data.getInstance().getProtocolos());
        setEnderecosOrigem(Data.getInstance().getEnderecos().getEnderecosOrigem());
        setEnderecosDestino(Data.getInstance().getEnderecos().getEnderecosOrigem(), Data.getInstance().getEnderecos().getDestination());

    }

    private void setAtraso(Atraso atrasoData) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Atraso");

        for (int i = 0; i < atrasoData.getAtrasos().size()-29 || i < atrasoData.getTempo().size()-29; i++) {
            //if (atrasoData.getTempo().get(i) > 10) {
                float media = 0;
                for (int j = i; j< i+29;j++){
                    media += atrasoData.getAtrasos().get(j);
                }
                series.getData().add(new XYChart.Data<>(atrasoData.getTempo().get(i).toString(), media));

            i+=30;
        }

        atraso.getData().add(series);
    }

    private void setProtocolos(Protocolos protocolosData) {
        Map<String, List<Integer>> protocolosMap = protocolosData.getProtocolos();
        for (String proto : protocolosMap.keySet()) {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(proto);

            for (int i = 0; i < protocolosMap.get(proto).size(); i++) {
                series.getData().add(new XYChart.Data<>(String.valueOf(i), protocolosMap.get(proto).get(i)));
            }

            protocolos.getData().add(series);
        }
    }

    private void setEnderecosOrigem(List<Endereco> origem) {
        XYChart.Series<Number, String> series1 = new XYChart.Series<>();
        series1.setName("Origem");
        int i = 0;
        for (Endereco addr : origem) {
            series1.getData().add(new XYChart.Data<>(Math.log(addr.getOcorrencias()),addr.getEndereco()));
            i++;
            if(i == 8) break;
        }
        enderecosOrigem.getData().add(series1);
    }

    private void setEnderecosDestino(List<Endereco> origem, Map<String,Integer> destino) {
        XYChart.Series<Number, String> series1 = new XYChart.Series<>();
        series1.setName("Destino");
        int i = 0;
        for (Endereco addr : origem) {
            i++;
            if (destino.containsKey(addr.getEndereco())){
                series1.getData().add(new XYChart.Data<>(Math.log(destino.get(addr.getEndereco())),addr.getEndereco()));
            }
            if (i == 8) break;
        }
        System.out.println("Dest: "+i);
        enderecosOrigem.getData().add(series1);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        atrasoX.setLabel("Tempo (segundos)");
        atrasoY.setLabel("Atraso (segundos)");
        protocolosX.setLabel("Tempo (segundos)");
        protocolosY.setLabel("Ocorrências");
        origemX.setLabel("Endereço");
        origemY.setLabel("Ocorrências (log)");

    }
}
