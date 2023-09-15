package com.example.ca1;

import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import resources.UnionFind;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImageAnalysis {
    int[] pixels;

    // Method for converting the image to black and white
    public void blackAndWhite (Image image, WritableImage writableImage, ImageView imageView,ImageView imageView1, double threshold, Label number, TreeView treeView) {
        int height = (int) image.getHeight();
        int width = (int) image.getWidth();

        PixelReader pixelReader = image.getPixelReader();
        PixelWriter pixelWriter = writableImage.getPixelWriter();
        pixels = new int[width * height];

        // Copying the original image to the writable image
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                Color color = pixelReader.getColor(x,y);
                pixelWriter.setColor(x,y,color);
            }
        }

        // Converting the image to black and white based on the brightness threshold
        for(int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++) {
                Color color = pixelReader.getColor(j,i);
                if (color.getBrightness() > threshold) {
                    pixelWriter.setColor(j,i,Color.WHITE);
                }
                else {
                    pixelWriter.setColor(j,i,Color.BLACK);
                }
            }
        }

        // Creating an array of pixels representing the black and white image
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                Color color = pixelReader.getColor(x,y);

                // Calculating the threshold value for each pixel based on RGB values
                double thresh = color.getRed() * 0.299 + color.getGreen() * 0.587 + color.getBlue() * 0.114;
                Color color1 = thresh > threshold ? Color.WHITE : Color.BLACK;

                // Storing the pixel value in the pixels array for further processing
                if(color1 == Color.BLACK) {
                    pixels[y * width + x] = -1;
                }
                else {
                    pixels[y * width + x] = y * width + x;
                }
            }
        }


        // Merging adjacent pixels using Union-Find algorithm
        merge(width);

        // Creating a HashMap to store the connected components and their corresponding pixels
        HashMap<Integer, List<Integer>> objectMap = createHashMap(pixels).get("objectMap");

        // Counting the number of stars in the connected components and displaying it on the GUI
        countingStars(objectMap,number);

        // Populating a TreeView with the connected components and their corresponding pixels
        populateTreeView(treeView, objectMap, image);

        // Displaying the black and white image on the GUI
        imageView.setImage(writableImage);

        // Displaying the number of stars on the original image and the black and white image on the GUI
        numberStarsOnImage(objectMap, imageView, imageView1);
    }



    // Merging adjacent pixels using Union-Find algorithm
    public void merge(int width) {
        for(int i = 0; i < pixels.length; i++) {
            if(pixels[i] != -1) {
                if(i + 1 < pixels.length && pixels[i + 1] != -1 && i % width != width - 1)
                    UnionFind.union(pixels, i, i + 1);

                if(i + width < pixels.length && pixels[i + width] != -1)
                    UnionFind.union(pixels, i, i + width);
            }
        }
    }

    // Creating a HashMap to store the connected components and their corresponding pixels
    public static HashMap<String, HashMap<Integer, List<Integer>>> createHashMap(int[] pixels) {
        HashMap<Integer, List<Integer>> objectMap = new HashMap<>(); // Create a HashMap to store mapping of root pixel to a list of pixels
        HashMap<Integer, List<Integer>> objectValue = new HashMap<>();

        for(int i = 0; i < pixels.length; i++) {
            if(pixels[i] != -1) {
                // Find the root pixel using a UnionFind algorithm
                int root = UnionFind.find(pixels, i);
                if(!objectMap.containsKey(root)) {
                    objectMap.put(root, new ArrayList<>()); // If root is not present in objectMap, add it with an empty ArrayList
                    objectValue.put(root, new ArrayList<>());

                }
                objectMap.get(root).add(pixels[i]); // Add pixel to list of pixels associated with root pixel in objectMap
                if(!objectMap.get(root).contains(pixels[i])) { //Add pixel value to list of pixel values associated with root pixel in objectValue, if not already present
                    objectMap.get(root).add(pixels[i]);
                }
            }
        }
        HashMap<String, HashMap<Integer, List<Integer>>> result = new HashMap<>();
        result.put("objectMap", objectMap); // Add "objectMap" key and its corresponding value to the outer HashMap
        result.put("objectValue", objectValue);
        return result;
    }



    public void numberStarsOnImage(HashMap<Integer, List<Integer>> objectMap, ImageView imageView, ImageView imageView1){
        int counter = 0; // Initialize a counter variable to keep track of stars
        for (int root : objectMap.keySet()){   // Iterate over the keys in the objectMap HashMap
            int x = root % (int) imageView.getImage().getWidth(); // Calculate x-coordinate of the star
            int y = root/ (int) imageView.getImage().getWidth(); // Calculate y-coordinate of the star

            Canvas canvas = new Canvas(imageView.getImage().getWidth(), imageView.getImage().getHeight()); // Create a new canvas with the dimensions of the image
            Canvas canvas1 = new Canvas(imageView.getImage().getWidth(), imageView.getImage().getHeight());

            GraphicsContext gc = canvas.getGraphicsContext2D(); // Get the graphics context of the canvas
            GraphicsContext gc1 = canvas1.getGraphicsContext2D();

            gc.setFont(Font.font("Arial", FontWeight.BOLD, 26)); // Set font for graphics context
            gc1.setFont(Font.font("Arial", FontWeight.BOLD, 26));

            gc.setFill(Color.YELLOW); // Set fill color for graphics context
            gc1.setFill(Color.YELLOW);

            gc.fillText(String.valueOf(counter), x, y); // Draw text on canvas with counter value at calculated x and y coordinates
            gc1.fillText(String.valueOf(counter), x, y);

            ImageView numbers = new ImageView(imageView.getImage()); // Create a new ImageView with the image
            ImageView numbers1 = new ImageView(imageView1.getImage());


            Group group = new Group(numbers, canvas); // Create a new Group with ImageView and canvas
            Group group1 = new Group(numbers1, canvas1);

            numbers = new ImageView(group.snapshot(null, null)); // Capture a snapshot of the group and set it as the new image for ImageView
            numbers1 = new ImageView(group1.snapshot(null, null));

            imageView.setImage(numbers.getImage()); // Set the updated image to the original ImageView
            imageView1.setImage(numbers1.getImage());

            counter++; // Increment the counter
        }
    }


    public void countingStars(HashMap<Integer, List<Integer>> objectValue, Label label) {

        int numberStars = 0; // Initialize a variable to store the number of stars
        for(List<Integer> pix : objectValue.values()) { // Iterate over the values in the objectValue HashMap
            if(!pix.isEmpty()) { // If the value is not empty (i.e., star exists)
                numberStars++; // Increment the number of stars
            }
        }
        label.setText(numberStars+""); // Set the label text to the number of stars converted to String
    }

    public void populateTreeView(TreeView treeView, HashMap<Integer, List<Integer>> objectValue, Image image) {
        // Get a PixelReader from the Image object to read the pixel colors
        PixelReader pixelReader = image.getPixelReader();

        // Create a root TreeItem with label "Stars" and set it as the root of the TreeView
        TreeItem<String> root = new TreeItem<>("Stars");
        treeView.setRoot(root);


        // Loop through each key in the objectValue HashMap
        for(Integer key : objectValue.keySet()) {
            List<Integer> value = objectValue.get(key); // Get the value (list of pixels) for the current key
            float r = 0; // Variable to store the average red component of the star's color
            float g = 0; // Variable to store the average green component of the star's color
            float b = 0; // Variable to store the average blue component of the star's color

            // Loop through each pixel in the value (list of pixels) for the current key
            for(int pixels : value) {
                // Get the color of the current pixel from the PixelReader
                Color color = pixelReader.getColor(pixels % (int) image.getWidth(), pixels / (int) image.getWidth());
                // Add the red, green, and blue components of the color to their respective variables
                r += color.getRed();
                g += color.getGreen();
                b += color.getBlue();
            }
            // Calculate the average red, green, and blue components by dividing the sum by the number of pixels
            r /= value.size();
            g /= value.size();
            b /= value.size();

            // Create a new TreeItem for the current star with label "Star <key>"
            TreeItem<String> star = new TreeItem<>("Star " + key );
            // Add child TreeItems for the number of pixels, and the average sulfur, hydrogen, and oxygen components
            star.getChildren().add(new TreeItem<>(value.size() + " pixels"));
            star.getChildren().add(new TreeItem<>("Sulphur: " + r));
            star.getChildren().add(new TreeItem<>("Hydrogen: " + g));
            star.getChildren().add(new TreeItem<>("Oxygen: " + b));
            // Add the star TreeItem as a child of the root TreeItem
            root.getChildren().add(star);


        }

        // Loop through the children of the root TreeItem and update their labels to "Star <index>"
        for(int i = 0; i < root.getChildren().size(); i++) {
            int j = i + 1;
            root.getChildren().get(i).setValue("Star " + j );
        }
    }
}


