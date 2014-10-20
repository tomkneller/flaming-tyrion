
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Screen extends Application{

	private final String NAME = "BroadChat_v0.2.1"; 			//name, add any changes add to the v no.
	
	private TextArea mainTxt = new TextArea();					//main area where the conversation appears
	private TextField userTxt = new TextField();				//user name
	private TextField messageTxt = new TextField();				//message to send
	private final Label lbl1 = new Label();						//label for the ':' that goes in between the userTxt and messageTxt
	private Button sendBtn = new Button();						//kinda obvious
	private final Label lbl2 = new Label();						//used as a footer, add to it if you add anything, ill mark where later in the code
	
	//Menu bar Items
	private MenuBar menuBar = new MenuBar();
	
	private Menu fileMenu = new Menu("File");							//MenuBar/file
	private MenuItem fileRandomthing1 = new MenuItem("randomthing1");
	private MenuItem fileRandomthing2 = new MenuItem("randomthing2");
	private MenuItem fileExitMenu = new MenuItem("Exit");
	
	private Menu aboutMenu = new Menu("About");							//MenuBar/About
	private MenuItem aboutReadmeMenu = new MenuItem("Readme");
	private MenuItem aboutCredits = new MenuItem("Credits");
	
	
	
	
	public static void main(String[] args) {					
		launch(args);
	}
	

	@Override
	public void start(Stage primaryStage){
		
		checkUpdate();
		BorderPane root = new BorderPane();
		Pane rootBody = new Pane();
		root.setTop(menuBar);
		root.setCenter(rootBody);
		root.setBottom(lbl2);
		
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
		
		
		// Assembling the menu bar
		fileMenu.getItems().addAll(fileRandomthing1, new SeparatorMenuItem(), fileRandomthing2, new SeparatorMenuItem(), fileExitMenu);
		aboutMenu.getItems().addAll(aboutReadmeMenu, aboutCredits);
		menuBar.getMenus().addAll(fileMenu, aboutMenu);
		
		
		
		//menu bar actions
		fileExitMenu.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent close) {
				System.exit(0);
			}
		});
		
		aboutCredits.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent e){
				new creditsDialog(Screen.this);
			}

		});
		
		aboutReadmeMenu.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent a){
				new readMeDialog(Screen.this);
			}

		});
		
		
		//main layout
		mainTxt.setLayoutX(0);
		mainTxt.setLayoutY(0);
		mainTxt.setPrefColumnCount(66);
		mainTxt.setPrefRowCount(41);
		mainTxt.setPrefWidth(800);
		mainTxt.setPrefHeight(530);
		mainTxt.setWrapText(true);
		//mainTxt.setDisable(true);
		rootBody.getChildren().add(mainTxt);
		
		userTxt.setLayoutX(0);
		userTxt.setLayoutY(530);
		userTxt.setPrefWidth(150);
		userTxt.setPrefColumnCount(12);
		userTxt.setText("User Name");
		rootBody.getChildren().add(userTxt);
		
		lbl1.setLayoutX(154);
		lbl1.setLayoutY(530);
		lbl1.setText(":");
		rootBody.getChildren().add(lbl1);
		
		messageTxt.setLayoutX(160);
		messageTxt.setLayoutY(530);
		messageTxt.setPrefWidth(540);
		messageTxt.setPrefColumnCount(45);
		messageTxt.setText("Message");
		rootBody.getChildren().add(messageTxt);
		
		sendBtn.setLayoutX(710);
		sendBtn.setLayoutY(530);
		sendBtn.setPrefWidth(80);
		sendBtn.setText("Send");
		sendBtn.setOnAction(sendBtnHandler);							//sends to the button handler where the bulk of code will be for the output, see if you can make an event handler for the input stream of data when receiving it will add automatically,
		sendBtn.setDefaultButton(true);
		rootBody.getChildren().add(sendBtn);									//also try to use the broadcast address if you can, may have to code in permissions, if to should be able to unicast so all IPs with a for loop judging the size of the loop by the subnet mask
		
		//footer

		lbl2.setText(NAME + " was created by Anton Wolfarth, Thomas Kneller, Alexander Savill & Connor Unsworth.");			//edit the string to add your name once you have made an edit, if the string starts getting too long you should be able to change the font size with .font() i think

	}



public void checkUpdate()
{
	try{
		URL url = new URL("https://github.com/WolfAntian/flaming-tyrion/blob/master/README.md");
		Scanner onlineUpdate = new Scanner(url.openStream()); 	
	try{
	    File file = new File("README.md");
	    Scanner localUpdate = new Scanner(file);
	    
	    String onlineVersion = null;
	    String localVersion = null;
	    
	    while(onlineUpdate.hasNextLine())
	    {
	    	if (onlineUpdate.nextLine().contains("version:"))
	    	{
	    		onlineVersion = (onlineUpdate.nextLine());
	    	}
	    }
	    
	    while(localUpdate.hasNextLine())
	        {
	    	if (localUpdate.nextLine().contains("version:"))
	    	{
	    		localVersion = (localUpdate.nextLine());
	    	}
	        }
	    if (!onlineVersion.contains(localVersion))
	    {
	    	System.out.println("Unfortunately you have an old version");
	    }
	}       	
	catch(Exception ex)
	    {  
			System.out.println("Oops there has been an error. Sorry! :(");
	    }
	}
	catch(IOException downloadError)
	{
		System.out.println("Sorry but a connection cannot be established to us.");
	}
}


class creditsDialog {Screen parent;Label ct1;creditsDialog(Screen parent){

	this.parent=parent;
	ct1 = new Label();
	ct1.setText("Programmed by :- \n Anton Wolfarth \n Thomas Kneller \n Alexander Savill \n Connor Unsworth");
	ct1.setLayoutX(40);
	ct1.setLayoutY(10);
	
	Stage dialogStage = new Stage();
	Pane dialogRoot = new Pane();
	dialogStage.setScene(new Scene(dialogRoot, 200, 200));
	dialogStage.setTitle("Credits");
	dialogRoot.getChildren().add(ct1);
	dialogStage.initStyle(StageStyle.UTILITY);
	dialogStage.initModality(Modality.WINDOW_MODAL);
	dialogStage.show();
}
}


//Creates dialog and uses scanner to parse ReadMe.md file and outputs contents
class readMeDialog{Screen parent;TextArea rMD1;readMeDialog(Screen parent){

	this.parent=parent;
	rMD1 = new TextArea();
	try{
	    File file = new File("README.md");
	    Scanner readMeScanner = new Scanner(file);
	    	
	    while(readMeScanner.hasNextLine())
	        {
	    		rMD1.appendText(readMeScanner.nextLine() + "\n");
	        }
		}
	catch(Exception ex)
	    {  
			rMD1.setText("Oops. The ReadMe file seems to be missing");	
	    }
	rMD1.setLayoutX(30);
	rMD1.setLayoutY(10);
	Stage dialogStage = new Stage();
	Pane dialogRoot = new Pane();
	dialogStage.setScene(new Scene(dialogRoot, 590, 200));
	dialogStage.setTitle("ReadMe");
	dialogRoot.getChildren().add(rMD1);
	dialogStage.initStyle(StageStyle.UTILITY);
	dialogStage.initModality(Modality.WINDOW_MODAL);
	dialogStage.show();
}
}
}


