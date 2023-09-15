package com.example.ca1;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TreeView;
import javafx.scene.image.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;


public class GalaxyController {
    WritableImage writableImage;
    public ImageView imgOrgViewer;
    public Button setBlckWht;
    public ImageView imgnew;
    @FXML
    private Slider threshold;
    Image image;
    @FXML
    private TreeView analysis;

    // Method to upload an image using a FileChooser dialog
    public  void uploadImage() {
        // Create a FileChooser instance
        FileChooser fc = new FileChooser();
        Window ownerWindow = null;
        // Show the FileChooser dialog and get the selected file
        File selectedFile = fc.showOpenDialog(ownerWindow);

        // Check if a file has been selected
        if (selectedFile != null) {
            // Load the selected image
            System.out.println(selectedFile.toURI().toString());
            image = new Image(selectedFile.toURI().toString());

            // Get the height and width of the image
            int height = (int) image.getHeight();
            int width = (int) image.getWidth();

            // Create a writable image with the same dimensions
            writableImage = new WritableImage(width, height);
            // Get the PixelReader and PixelWriter for the images
            PixelReader pixelReader = image.getPixelReader();
            PixelWriter pixelWriter = writableImage.getPixelWriter();

            // Copy the pixels from the original image to the writable image
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    Color color = pixelReader.getColor(x, y);
                    pixelWriter.setColor(x, y, color);
                }
            }

            // Set the image views to display the images
            imgOrgViewer.setImage(image);
            imgnew.setImage(writableImage);

        } else {
            // No file selected, print an error message
            System.out.println("Not a valid file.");
        }
    }

    // Method to change the image to black and white
    public void changeImageBW() {
        // Create an ImageAnalysis instance and call its blackAndWhite method
        imgOrgViewer.setImage(image);
        ImageAnalysis analysis1 = new ImageAnalysis();
        analysis1.blackAndWhite(image, writableImage, imgnew,imgOrgViewer, threshold.getValue() / 100, number, analysis);
    }

    @FXML
    private Label number;



}