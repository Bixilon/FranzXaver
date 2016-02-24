package afester.javafx.examples.svg;

import afester.javafx.components.SnapSlider;
import afester.javafx.examples.Example;
import afester.javafx.svg.GradientPolicy;
import afester.javafx.svg.SvgLoader;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Example("Using SvgLoader to render an SVG file")
public class SvgLoaderResultViewer extends Application {

    // The SVG image's top node, as returned from the SvgLoader
    private Group svgImage = new Group();

    /* in order to properly scale the svgImage node, we need an additional
     * intermediate node 
     */
    private Group imageLayout = new Group();

    private final HBox mainLayout = new HBox();
    private SvgLoader loader = new SvgLoader();
    private String currentFile;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("SVGLoader result viewer");

        ListView<String> listPanel = createListPanel();
        Pane optionsPanel = createOptionsPanel();

        VBox leftPanel = new VBox();
        leftPanel.getChildren().add(listPanel);
        leftPanel.getChildren().add(optionsPanel);

        mainLayout.getChildren().add(leftPanel);
        mainLayout.getChildren().add(imageLayout);

        listPanel.getSelectionModel().select(0);

        // show the generated scene graph
        Scene scene = new Scene(mainLayout, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    private ListView<String> createListPanel() {

        ObservableList<String> ol = FXCollections.observableList(getTestFiles());
        ListView<String> listView = new ListView<String>(ol);
        listView.getSelectionModel().getSelectedItems().addListener(
            new ListChangeListener<String>() {

                @Override
                public void onChanged(
                        javafx.collections.ListChangeListener.Change<? extends String> change) {
                    selectFile(change.getList().get(0));
                }
            }
        );
        
        return listView;
    }

    private Pane createOptionsPanel() {
        // gradient policy
        HBox gradientPolicy = new HBox();
        gradientPolicy.setSpacing(10);
        gradientPolicy.getChildren().add(new Label("Gradient Transformation Policy:"));

        ComboBox<GradientPolicy> comboBox = new ComboBox<>();
        comboBox.getItems().addAll(GradientPolicy.values());
        comboBox.setOnAction(e -> {
            loader.setGradientTransformPolicy(comboBox.getSelectionModel().getSelectedItem());
            selectFile(currentFile);
        }); 
        gradientPolicy.getChildren().add(comboBox);
        comboBox.getSelectionModel().select(GradientPolicy.USE_SUPPORTED);

        // show/hide viewport rectangle
        CheckBox showViewport = new CheckBox("Show Viewport");
        showViewport.setOnAction(e -> {
            loader.setAddViewboxRect(showViewport.isSelected());
            selectFile(currentFile);
        } );

        // Zoom
        SnapSlider zoomSlider = new SnapSlider(0.25, 2.0, 1.0);
        zoomSlider.setShowTickLabels(true);
        zoomSlider.setShowTickMarks(true);
        zoomSlider.setMajorTickUnit(0.25);
        zoomSlider.setMinorTickCount(0);
        zoomSlider.setLabelFormatter(new StringConverter<Double>() {

            @Override
            public String toString(Double value) {
                return String.format("%d %%", (int) (value * 100));
            }

            @Override
            public Double fromString(String string) {
                throw new UnsupportedOperationException();
            }
        });

        zoomSlider.finalValueProperty().addListener( (val, oldVal, newVal) -> {
            setScale((Double) newVal);
        });

        HBox zoomControl = new HBox();
        zoomControl.setSpacing(10);
        zoomControl.getChildren().add(new Label("Zoom:"));
        zoomControl.getChildren().add(zoomSlider);
        HBox.setHgrow(zoomSlider, Priority.ALWAYS);

        // build control panel
        VBox controlPanel = new VBox();
        controlPanel.setSpacing(10);
        controlPanel.setPadding(new Insets(10, 10, 10, 10));
        controlPanel.getChildren().add(gradientPolicy);
        controlPanel.getChildren().add(showViewport);
        controlPanel.getChildren().add(zoomControl);
        
        return controlPanel;
    }

    private void setScale(Double scale) {
        svgImage.setScaleX(scale);
        svgImage.setScaleY(scale);
    }


    private void selectFile(String string) {
        currentFile = string;
        InputStream svgFile = null;
        try {
            svgFile = new FileInputStream("data/" + string);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        imageLayout.getChildren().remove(svgImage);
        svgImage = loader.loadSvg(svgFile);
        imageLayout.getChildren().add(svgImage);
    }

    
    private List<String> getTestFiles() {
        List<String> result = new ArrayList<>();

        File directory = new File("data");
        String[] files = directory.list();
        for (String file : files) {
            if (file.endsWith(".svg")) {
                result.add(file);
            }
        }

        return result;
    }
}