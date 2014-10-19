

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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
	public void start(Stage stage) throws Exception {
		stage.setTitle(NAME);
		Pane root = new Pane();
		stage.setScene(new Scene(root,800,600));
		stage.show();
		
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
		root.getChildren().add(mainTxt);
		
		userTxt.setLayoutX(0);
		userTxt.setLayoutY(550);
		userTxt.setPrefWidth(150);
		userTxt.setPrefColumnCount(12);
		userTxt.setText("User Name");
		root.getChildren().add(userTxt);
		
		lbl1.setLayoutX(154);
		lbl1.setLayoutY(550);
		lbl1.setText(":");
		root.getChildren().add(lbl1);
		
		messageTxt.setLayoutX(160);
		messageTxt.setLayoutY(550);
		messageTxt.setPrefWidth(540);
		messageTxt.setPrefColumnCount(45);
		messageTxt.setText("Message");
		root.getChildren().add(messageTxt);
		
		sendBtn.setLayoutX(710);
		sendBtn.setLayoutY(550);
		sendBtn.setPrefWidth(80);
		sendBtn.setText("Send");
		sendBtn.setOnAction(sendBtnHandler);
		sendBtn.setDefaultButton(true);
		root.getChildren().add(sendBtn);									
		
		lbl2.setLayoutX(0);
		lbl2.setLayoutY(585);
		lbl2.setText(NAME + " was created by Anton Wolfarth.");			//edit the string to add your name once you have made an edit, if the string starts getting too long you should be able to change the font size with .font() i think
		root.getChildren().add(lbl2);
		
	}

}
