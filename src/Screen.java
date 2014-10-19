

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Screen extends Application{

	private final String NAME = "BroadChat_v0.1.2"; 			//name, add any changes add to the v no.
	private TextArea mainTxt = new TextArea();					//main area where the conversation appears
	private TextField userTxt = new TextField();				//user name
	private TextField messageTxt = new TextField();				//message to send
	private final Label lbl1 = new Label();						//label for the ':' that goes in between the userTxt and messageTxt
	private Button sendBtn = new Button();						//kinda obvious
	private final Label lbl2 = new Label();						//used as a footer, add to it if you add anything, ill mark where later in the code
	
	public static void main(String[] args) {					
		launch(args);
	}

	@Override
	public void start(Stage primaryStage){
		
	      

		
		//Menu bar Items
				Menu fileMenu = new Menu("File");
				MenuItem fileRandomthing1 = new MenuItem("randomthing1");
				MenuItem fileRandomthing2 = new MenuItem("randomthing2");
				MenuItem fileExitMenu = new MenuItem("Exit");
				
				Menu aboutMenu = new Menu("About");
				MenuItem aboutReadmeMenu = new MenuItem("Readme");
				MenuItem aboutCredits = new MenuItem("Credits");
				CheckMenuItem viewAutoRefreshMenu = new CheckMenuItem("Auto Refresh");

				// Assembling the menu bar
				fileMenu.getItems().addAll(fileRandomthing1, new SeparatorMenuItem(), fileRandomthing2, new SeparatorMenuItem(), fileExitMenu);
				aboutMenu.getItems().addAll(aboutReadmeMenu, aboutCredits);
				MenuBar menuBar = new MenuBar();
				menuBar.getMenus().addAll(fileMenu, aboutMenu);

					
		
	
  	 
	Pane rootBody = new Pane();
	
	
	
	
	BorderPane root = new BorderPane();
	root.setTop(menuBar);
	root.setCenter(rootBody);
	
	Scene scene = new Scene(root,800,600);
	primaryStage.setTitle(NAME);
	primaryStage.setScene(scene);
	primaryStage.show();
	
	
		
		
		//btn handler
		EventHandler<ActionEvent> sendBtnHandler = new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent arg0) {
				//dummy for presentation
				//change to send as broadcast
				//update mainTxt through receiving broadcast
				String message = ">" + userTxt.getText() + ": " + messageTxt.getText() + "\n";
				mainTxt.appendText(message);
				messageTxt.clear();
			}	
		};
		
		

		
		
		mainTxt.setLayoutX(0);
		mainTxt.setLayoutY(0);
		mainTxt.setPrefColumnCount(66);
		mainTxt.setPrefRowCount(41);
		mainTxt.setPrefWidth(800);
		mainTxt.setPrefHeight(550);
		mainTxt.setWrapText(true);
		//mainTxt.setDisable(true);
		rootBody.getChildren().add(mainTxt);
		
		userTxt.setLayoutX(0);
		userTxt.setLayoutY(550);
		userTxt.setPrefWidth(150);
		userTxt.setPrefColumnCount(12);
		userTxt.setText("User Name");
		rootBody.getChildren().add(userTxt);
		
		lbl1.setLayoutX(154);
		lbl1.setLayoutY(550);
		lbl1.setText(":");
		rootBody.getChildren().add(lbl1);
		
		messageTxt.setLayoutX(160);
		messageTxt.setLayoutY(550);
		messageTxt.setPrefWidth(540);
		messageTxt.setPrefColumnCount(45);
		messageTxt.setText("Message");
		rootBody.getChildren().add(messageTxt);
		
		sendBtn.setLayoutX(710);
		sendBtn.setLayoutY(550);
		sendBtn.setPrefWidth(80);
		sendBtn.setText("Send");
		sendBtn.setOnAction(sendBtnHandler);							//sends to the button handler where the bulk of code will be for the output, see if you can make an event handler for the input stream of data when receiving it will add automatically,
		sendBtn.setDefaultButton(true);
		rootBody.getChildren().add(sendBtn);									//also try to use the broadcast address if you can, may have to code in permissions, if to should be able to unicast so all IPs with a for loop judging the size of the loop by the subnet mask
		
		lbl2.setLayoutX(0);
		lbl2.setLayoutY(585);
		lbl2.setText(NAME + " was created by Anton Wolfarth & Thomas Kneller.");			//edit the string to add your name once you have made an edit, if the string starts getting too long you should be able to change the font size with .font() i think
		rootBody.getChildren().add(lbl2);
		
	}

}
