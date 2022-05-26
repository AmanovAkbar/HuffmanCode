
import java.io.File;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;

import javafx.scene.layout.GridPane;



public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			
			 	primaryStage.setTitle("Huffman Code");
		        Button compressF = new Button();
		        compressF.setText("Compress file");
		        compressF.setOnAction(new EventHandler<ActionEvent>() {
		 
		            public void handle(ActionEvent event) {
		            	FileChooser fileChooser = new FileChooser();
		            	 fileChooser.setTitle("Open Resource File");
		            	 fileChooser.getExtensionFilters().addAll(
		            	         
		            	         new ExtensionFilter("All Files", "*.*"));
		            	 File selectedFile = fileChooser.showOpenDialog(primaryStage);
		            	 long begin = System.currentTimeMillis();
		            	 if (selectedFile != null && !(selectedFile.isDirectory())) {
		            	   HuffmanCode hfc = new HuffmanCode(selectedFile);
		            	   hfc.compress();
		            	  
		            	 }else {
		            		 Alert alert = new Alert(AlertType.ERROR);
			                 alert.setTitle("ERROR!");
			                 alert.setHeaderText("Something is wrong...");
			                 alert.setContentText("Choose again..:) \n");
			          
			                 alert.showAndWait();
		            	 }
		            	 long end = System.currentTimeMillis();
		            	 System.out.println("\n"+(end-begin));
		            }
		        });
		        Button decompress = new Button();
		        decompress.setText("Decompress");
		        decompress.setOnAction(new EventHandler<ActionEvent>() {
		 
		            public void handle(ActionEvent event) {
		            	FileChooser fileChooser = new FileChooser();
		            	 fileChooser.setTitle("Open Resource File");
		            	 fileChooser.getExtensionFilters().addAll(
		            	         new ExtensionFilter("Huffman comressed files", "*.hfm"));
		            	 File selectedFile = fileChooser.showOpenDialog(primaryStage);
		            	 long begin = System.currentTimeMillis();
		            	 if (selectedFile != null) {
		            		 HuffmanCodeFolder hfc = new HuffmanCodeFolder(selectedFile);
		            		 hfc.decompress();
		            	 }else {
		            		 Alert alert = new Alert(AlertType.ERROR);
			                 alert.setTitle("ERROR!");
			                 alert.setHeaderText("Something is wrong...");
			                 alert.setContentText(" Choose again..:) \n");
			          
			                 alert.showAndWait();
		            	 }
		            	 long end = System.currentTimeMillis();
		            	 System.out.println("\n"+(end-begin));
		            }
		        });
		        Button Info = new Button();
		        Info.setText("INFO");
		        Info.setOnAction(new EventHandler<ActionEvent>() {
		 
		            public void handle(ActionEvent event) {
		            	 Alert alert = new Alert(AlertType.INFORMATION);
		                 alert.setTitle("About");
		                 alert.setHeaderText("BEHOLD! Huffman Compression");
		                 alert.setContentText(" Press Compress file to compress a single file.\n "
		                 		+ "Press Compress directory to compress a directory\n"
		                 		+ " Press Decompress to decompress .hfm file\n"
		                 		+ " Press INFO to see this message again");
		          
		                 alert.showAndWait();
		            }
		        });
		        Button CompressD = new Button();
		        CompressD.setText("Compress Directory");
		        CompressD.setOnAction(new EventHandler<ActionEvent>() {
		 
		            public void handle(ActionEvent event) {
		            	DirectoryChooser fileChooser = new DirectoryChooser();
		            	 fileChooser.setTitle("Open Resource File");
		            	
		            	 File selectedFile = fileChooser.showDialog(primaryStage);
		            	 long begin = System.currentTimeMillis();
		            	 if (selectedFile != null) {
		            		 HuffmanCodeFolder hfc = new HuffmanCodeFolder(selectedFile);
		            		 hfc.compress();
		            	 }else {
		            		 Alert alert = new Alert(AlertType.ERROR);
			                 alert.setTitle("ERROR!");
			                 alert.setHeaderText("Something is wrong...");
			                 alert.setContentText(" Choose again..:) \n");
			          
			                 alert.showAndWait();
		            	 }
		            	 long end = System.currentTimeMillis();
		            	 System.out.println("\n"+(end-begin));
		            }
		        });
		        GridPane root = new GridPane();
		        root.setAlignment(Pos.CENTER);
		        root.setHgap(10);
		        root.setVgap(10);
		        root.setPadding(new Insets(25, 25, 25, 25));
		        root.add(compressF, 0, 1);
		        root.add(decompress, 2, 1);
		        root.add(CompressD, 1,1);
		        root.add(Info, 0, 2);
		        Scene scene = new Scene(root,400, 200);
		        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		        primaryStage.setScene(scene);
		        primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
